
package lgcns;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Srv.App;

public class DataBase extends App {

    DataSource ds;

    public DataBase() {
        // TODO Auto-generated constructor stub
        Context initContext;
        try {
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/oracle");
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

 
    public List<Map<String, String>> doSelect(JSONObject req) throws SQLException {
        Connection conn = null;
        List<Map<String, String>> list = new ArrayList();
        try { // JSONObject read = (JSONObject) JSONValue.parse(req);
            conn = ds.getConnection();
            PreparedStatement preparedStatement = null;
            String sql = "select HASH,IPFS_RES,DEPT_CD,CREATE_DATE, CONTRACT  from  BC_TSA order by CREATE_DATE desc ";
            preparedStatement = conn.prepareStatement(sql);
            int i = 1;
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Map<String, String> data01 = new HashMap();
                String hash = rs.getString("HASH");
                String ipfsRes = rs.getString("IPFS_RES");
                System.out.println(hash);
                System.out.println(ipfsRes);
                data01.put("hash", hash);
                data01.put("ipfsRes", ipfsRes);
                data01.put("deptCd", rs.getString("DEPT_CD"));
                data01.put("createDate", rs.getString("CREATE_DATE"));
                data01.put("contract", rs.getString("CONTRACT"));
                
                list.add(data01);
            }
            return list;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // Insert into CJJH.BC_TSA (HASH,URL,FILE_NAME,DEPT_CD,CREATE_DATE) values ('12313','12313','12313','123123',to_date('18/05/14','RR/MM/DD'));
    public void insert() {
        Connection conn = null;
        try { // JSONObject read = (JSONObject) JSONValue.parse(req);
            String deptCd = (String) data.get("deptCd");
            String hash = (String) data.get("hash");
            String ipfRes = (String) data.get("ipfs");
            String contract = (String) data.get("contract");
            String createDate = (String) data.get("createDate");
            System.out.println("hash in dbinsert::" + hash);
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = null;
            String sql = "Insert into BC_TSA (HASH,IPFS_RES, DEPT_CD,CONTRACT,CREATE_DATE) values (?,?,?,?,?) ";
            preparedStatement = conn.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, hash);
            preparedStatement.setString(i++, ipfRes);
            preparedStatement.setString(i++, deptCd);
            preparedStatement.setString(i++, contract);
            preparedStatement.setString(i++, createDate);
            preparedStatement.execute();
            conn.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doSvc() {
        // TODO Auto-generated method stub
        if (data.get("dbCmd").equals("insert")) {
            insert();
        } else if (data.get("dbCmd").equals("select")) {
        } else {
            // /nothing
        }
    }

    @Override
    public boolean stop() {
        // TODO Auto-generated method stub
        return false;
    }
}
