
package lgcns;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CMD {

    public CMD(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String _readInputJson = "";
        if (br != null) {
            _readInputJson = br.readLine();
        }
        JSONObject read = (JSONObject) JSONValue.parse(_readInputJson);
        String cmd = (String) read.get("cmd");
          
        
        if (cmd.equals("create")) {
            CreateJson j = new CreateJson();
            j.doService(read, response);
           
        } else if (cmd.equals("retrive")) {
            RetriveJson j = new RetriveJson();
            j.doService(read, response);
        }
    }
}
