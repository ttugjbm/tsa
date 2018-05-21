
package lgcns;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampResponseGenerator;
import org.bouncycastle.tsp.TimeStampTokenGenerator;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Srv.App;

public class TSAServer extends App {

    TimeStampTokenGenerator timeStampTokenGenerator = null;
    private final String algorithm = "SHA1PRNG";
    private String hash;

    public String getHash() {
        return hash;
    }

    public TSAServer() {
        try {
            CertificateGenerator cgt = CertificateGenerator.getInstance();
            KeyPair kp = cgt.generateKeyPair();
            X509Certificate x509 = cgt.generateCertificate(kp, "jjhCert");
            X509Certificate[] certs = new X509Certificate[1];
            certs[0] = x509;
            makeToken(kp.getPrivate(), certs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeToken(final PrivateKey privateKey, final Certificate[] chain) throws Exception {
        try {
            Certificate certificate = chain[0];
            ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").setProvider(new BouncyCastleProvider()).build(privateKey);
            JcaDigestCalculatorProviderBuilder digestCalculatorProviderBuilder = new JcaDigestCalculatorProviderBuilder();
            // digestCalculatorProviderBuilder.setProvider(BouncyCastleProvider.PROVIDER_NAME);
            DigestCalculatorProvider digestCalculatorProvider = digestCalculatorProviderBuilder.build();
            JcaSignerInfoGeneratorBuilder signerInfoGeneratorBuilder = new JcaSignerInfoGeneratorBuilder(digestCalculatorProvider);
            SignerInfoGenerator signerInfoGenerator = signerInfoGeneratorBuilder.build(signer, (X509Certificate) certificate);
            DigestCalculator digestCalculator = digestCalculatorProvider.get(signerInfoGenerator.getDigestAlgorithm());
            this.timeStampTokenGenerator = new TimeStampTokenGenerator(signerInfoGenerator, digestCalculator, new ASN1ObjectIdentifier("1.1.1"));
            JcaCertStore certStore = new JcaCertStore(Arrays.asList(chain));
            this.timeStampTokenGenerator.addCertificates(certStore);
        } catch (Exception e) {
            // throw new TimeStampException(e);
            throw e;
        }
    }

    private Set<String> makeSetOfProperty(final String nonParsedPropery) {
        Set<String> retval = null;
        if (nonParsedPropery != null) {
            final String[] subStrings = nonParsedPropery.split(";");
            if (subStrings.length > 0) {
                retval = new HashSet<String>();
                retval.addAll(Arrays.asList(subStrings));
            }
        }
        return retval;
    }

    private transient SecureRandom random;
    private final BigInteger LOWEST = new BigInteger("0080000000000000", 16);
    private final BigInteger HIGHEST = new BigInteger("7FFFFFFFFFFFFFFF", 16);

    private BigInteger getSerialNumber() throws Exception {
        if (random == null) {
            try {
                random = SecureRandom.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new Exception(e);
            }
        }
        final byte[] sernobytes = new byte[8];
        boolean ok = false;
        BigInteger serialNumber = null;
        while (!ok) {
            random.nextBytes(sernobytes);
            serialNumber = new BigInteger(sernobytes).abs();
            // Must be within the range 0080000000000000 - 7FFFFFFFFFFFFFFF
            if ((serialNumber.compareTo(LOWEST) >= 0) && (serialNumber.compareTo(HIGHEST) <= 0)) {
                ok = true;
            }
        }
        return serialNumber;
    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    @Override
    public void doSvc() {
        // TODO Auto-generated method stub
        String deptCd = (String) data.get("deptCd");
        String token = (String) data.get("token");
        StringBuffer log = (StringBuffer) data.get("log");
        TimeStampResponse tsResponse = null;
        try {
          
            log.append("deptCd: + deptCd\n");
            log.append("token: + token\n");
            // DatatypeConverter.printHexBinary(array);
            TimeStampRequest reqTsa = new TimeStampRequest(DatatypeConverter.parseHexBinary(token));
            String digestOID = reqTsa.getMessageImprintAlgOID().getId();
            String tSAPolicyOID = reqTsa.getReqPolicy().getId();;
            hash = bytesToHex(reqTsa.getMessageImprintDigest());
            System.out.println("-----------------hash--------------------:" + hash);;
            System.out.println("digestOID:" + digestOID);
            System.out.println("tSAPolicyOID:" + tSAPolicyOID);
            log.append("digestOID:" + digestOID + "\n");
            log.append("tSAPolicyOID:" + tSAPolicyOID + "\n");
            if (tSAPolicyOID == null) {
                tSAPolicyOID = "1.2.3";
            }
            TimeStampResponseGenerator timeStampResponseGen = new TimeStampResponseGenerator(timeStampTokenGenerator, TSPAlgorithms.ALLOWED, makeSetOfProperty(tSAPolicyOID));
            tsResponse = timeStampResponseGen.generate(reqTsa, getSerialNumber(), new Date());
            data.put("res", DatatypeConverter.printHexBinary(tsResponse.getEncoded()));
            data.put("hash", hash);
            log.append("res:" + DatatypeConverter.printHexBinary(tsResponse.getEncoded()) + "\n");
            // return tsResponse.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        }
    }

    @Override
    public boolean stop() {
        // TODO Auto-generated method stub
        return false;
    }
}
