
package lgcns;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Srv.App;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

public class IPFSTemDir extends App {

    public static int REQ = 1;
    public static int RES = 2;
    public static int HEAD = 3;
    private String resUrl;
    private IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");

    public String getResUrl() {
        return resUrl;
    }

    @Override
    public void doSvc() {
        String req = (String) data.get("req");
        String head = (String) data.get("head");
        String res = (String) data.get("res");
        List fileList = null;
        String dirPath = null;
        try {
            File tmDir = FileUtils.getTempDirectory();
            Path rootDirectory = FileSystems.getDefault().getPath(tmDir.getCanonicalPath());
            Path tempDirectory = Files.createTempDirectory(rootDirectory, "");
            dirPath = tempDirectory.toString();
            System.out.println(dirPath);
            fileList = new ArrayList();
            fileList.add(new File(dirPath + "/req.txt"));
            fileList.add(new File(dirPath + "/res.ber"));
            fileList.add(new File(dirPath + "/head.txt"));;
            FileUtils.writeStringToFile((File) fileList.get(0), req, "utf-8");
            FileUtils.writeStringToFile((File) fileList.get(1), res, "utf-8");
            FileUtils.writeStringToFile((File) fileList.get(2), head, "utf-8");
            NamedStreamable.FileWrapper dir = new NamedStreamable.FileWrapper((File) fileList.get(0));
            MerkleNode addResult = ipfs.add(dir).get(0);
            StringBuffer log = (StringBuffer) data.get("log");
            System.out.println(addResult.toJSONString());
            log.append("ipfs res hexStr : " + addResult.toJSONString() + "\n");
            NamedStreamable.FileWrapper dir2 = new NamedStreamable.FileWrapper((File) fileList.get(1));
            addResult = ipfs.add(dir2).get(0);
            System.out.println(addResult.toJSONString());
            JSONObject readIpfs = (JSONObject) JSONValue.parse(addResult.toJSONString());
            resUrl = (String) readIpfs.get("Hash");
            SimpleDateFormat _time = new SimpleDateFormat("yyyyMMddHHmmss");
            String createDate = _time.format(System.currentTimeMillis());
            data.put("ipfs", resUrl);
            data.put("createDate", createDate);
            log.append("ipfs url : " + resUrl + "\n");
            log.append("createDate : " + createDate + "\n");
            NamedStreamable.FileWrapper dir3 = new NamedStreamable.FileWrapper((File) fileList.get(2));
            addResult = ipfs.add(dir3).get(0);
            System.out.println(addResult.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                FileUtils.deleteDirectory(new File(dirPath));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean stop() {
        // TODO Auto-generated method stub
        return false;
    }
}
