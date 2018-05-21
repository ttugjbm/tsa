import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.xml.bind.DatatypeConverter;

import lgcns.DataBase;
import lgcns.IPFSTemDir;
import lgcns.TSAServer;
import lgcns.ContractRequestor;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Inf.JService;
import Srv.Impl01;
import Srv.Impl02;
import Srv.Impl03;
import Srv.RunApp;

public class Jtest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    // @Test
    public void test() {
        Map data = new HashMap();
        // fail("Not yet implemented");
        List<JService> list = new ArrayList();
        list.add(new Impl01());
        list.add(new Impl02());
        list.add(new Impl03());
        RunApp.runService(data, list);
    }

    @Test
    public void test2() {
        Map data = new HashMap();
        data.put("token", "304c0201013031300d060960864801650304020105000420438097e943c60a2e0665ca9fd9b59cdc362386c8505d8010e40f6208c466a8e506092a864886f70d01010b020601633e9486050101ff");
        data.put("deptCd", "1234");
        data.put("log", new StringBuffer());
        List<JService> list = new ArrayList();
        list.add(new TSAServer()); // 증명서발급한다
        list.add(new IPFSTemDir()); // 요청서 증명서
        list.add(new ContractRequestor()); // 블럭체인에 넣는다
        // list.add(new DataBase());
        // ip.createIPFS(ipfs, (String) read.get("token"), DatatypeConverter.printHexBinary(tsaRes), read.toJSONString());
        RunApp.runService(data, list);
        StringBuffer log = (StringBuffer) data.get("log");
        System.out.println("=============끝------------------");
        System.out.println(log.toString());
    }

    protected PrintStream outputFile(String name) throws FileNotFoundException {
        return new PrintStream(new BufferedOutputStream(new FileOutputStream(name)), true);
    }
}
