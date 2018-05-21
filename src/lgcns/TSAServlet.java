
package lgcns;


import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.tsp.TSPException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Servlet implementation class TSAServlet
 */
public class TSAServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

     
    
    
    public static String etherUrl  = null ;
    public static String walletPw  = null ;
    public static String walletPh  = null ;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TSAServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
     

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
      
        etherUrl = config.getInitParameter("etherUrl");
        walletPw = config.getInitParameter("walletPw");
        walletPh = config.getInitParameter("walletPh");
        
        System.out.println("TSAServlet.init:"+etherUrl);
        System.out.println("TSAServlet.init:"+walletPw);
        System.out.println("TSAServlet.init:"+walletPh);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        System.out.println("Enter!! doGet ");
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            CMD c = new CMD(request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
