
package lgcns;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;

/**
 * Generates self-signed X509 certificates and private/public key pairs.
 * 
 * @author <a href="mailto:marko.luksa@gmail.com">Marko Luksa</a>
 */
public class CertificateGenerator {

    private static CertificateGenerator instance = new CertificateGenerator();
    private static final int KEY_SIZE = 1024;

    private CertificateGenerator() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE, new SecureRandom());
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot generate RSA key pair", e);
        }
    }

    public X509Certificate generateCertificate(KeyPair pair, String dn) throws CertificateException {
        try {
            X509v3CertificateBuilder builder = new X509v3CertificateBuilder(new X500Name("CN=" + dn), BigInteger.valueOf(new SecureRandom().nextLong()), new Date(System.currentTimeMillis() - 10000),
                    new Date(System.currentTimeMillis() + 24L * 3600 * 1000), new X500Name("CN=" + dn), SubjectPublicKeyInfo.getInstance(pair.getPublic().getEncoded()));
            /*
             * KeyUsage usage = new KeyUsage(KeyUsage.keyCertSign
             * | KeyUsage.digitalSignature | KeyUsage.keyEncipherment
             * | KeyUsage.dataEncipherment | KeyUsage.cRLSign);
             * ExtendedKeyUsage
             * builder.addExtension(Extension.keyUsage, false, usage);
             */
            builder.addExtension(X509Extension.basicConstraints, true, new BasicConstraints(false));
            builder.addExtension(X509Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature));
            builder.addExtension(X509Extension.extendedKeyUsage, true, new ExtendedKeyUsage(KeyPurposeId.id_kp_timeStamping));
            /*
             * KeyPurposeId[] kpid = new KeyPurposeId[2];
             * kpid[0] = KeyPurposeId.id_kp_clientAuth ;
             * kpid[1] = KeyPurposeId.id_kp_timeStamping;
             * ExtendedKeyUsage ev2 = new ExtendedKeyUsage(kpid);
             * builder.addExtension(X509Extension.extendedKeyUsage, true, ev2);
             */
            // builder.addExtension(X509Extension.extendedKeyUsage, true, new ExtendedKeyUsage(KeyPurposeId.id_kp_clientAuth));
            //
            X509CertificateHolder holder = builder.build(createContentSigner(pair));
            Certificate certificate = holder.toASN1Structure();
            // return certificate;
            return convertToJavaCertificate(certificate);
        } catch (OperatorCreationException e) {
            throw new RuntimeException("Cannot generate X509 certificate", e);
        } catch (CertIOException e) {
            throw new RuntimeException("Cannot generate X509 certificate", e);
        } catch (IOException e) {
            throw new RuntimeException("Cannot generate X509 certificate", e);
        }
    }

    private X509Certificate convertToJavaCertificate(Certificate certificate) throws CertificateException, IOException {
        InputStream is = new ByteArrayInputStream(certificate.getEncoded());
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        } finally {
            is.close();
        }
    }

    private ContentSigner createContentSigner(KeyPair pair) throws IOException, OperatorCreationException {
        AlgorithmIdentifier signatureAlgorithmId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
        AlgorithmIdentifier digestAlgorithmId = new DefaultDigestAlgorithmIdentifierFinder().find(signatureAlgorithmId);
        AsymmetricKeyParameter privateKey = PrivateKeyFactory.createKey(pair.getPrivate().getEncoded());
        return new BcRSAContentSignerBuilder(signatureAlgorithmId, digestAlgorithmId).build(privateKey);
    }

    public static CertificateGenerator getInstance() {
        return instance;
    }
}
