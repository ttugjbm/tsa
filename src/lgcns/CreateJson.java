
package lgcns;


import io.ipfs.api.IPFS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.tsp.TSPException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import Inf.JService;
import Srv.RunApp;

public class CreateJson {

    public void doService(JSONObject read, HttpServletResponse response) throws IOException {
        JSONObject tsaData = new JSONObject();
        List<JService> list = new ArrayList();
        list.add(new TSAServer()); // 증명서발급한다
        list.add(new IPFSTemDir()); // 요청서 증명서
        list.add(new ContractRequestor()); // 블럭체인에 넣는다
        list.add(new DataBase());
 
 
        
        String token = (String) read.get("token");
        String deptCd = (String) read.get("deptCd");
        
        Map data = new HashMap();
        data.put("token", token);
        data.put("deptCd", deptCd);
        data.put("log", new StringBuffer());
        data.put("dbCmd", "insert");
        
        RunApp.runService(data, list);
        if (data.get("res") == null) {
            tsaData.put("success", "fail");
            tsaData.put("res", "");
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(tsaData.toString());
            System.out.println(tsaData.toString());
        } else {
            tsaData.put("success", "ok");
            tsaData.put("res", (String) data.get("res"));
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(tsaData.toString());
            System.out.println(tsaData.toString());
        }
    }
}
