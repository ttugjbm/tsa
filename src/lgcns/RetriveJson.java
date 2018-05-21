
package lgcns;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.tsp.TSPException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import io.ipfs.api.IPFS;

public class RetriveJson {

    DataBase db;

    public RetriveJson() throws NamingException {
        db = new DataBase();
    }

    public void doService(JSONObject read, HttpServletResponse response) throws IOException {
        JSONObject tsaData = new JSONObject();
        JSONArray tsaList = new JSONArray();
        List<Map<String, String>> list = null;
        Map<String, List> data02 = new HashMap();
        byte[] tsaRes = null;
        try {
            list = db.doSelect(read);
            data02.put("tsa", list);
            // System.out.println("ipfs ::: "+addResult.toJSONString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(JSONArray.toJSONString(list));
        System.out.println(JSONArray.toJSONString(list));
    }
}
