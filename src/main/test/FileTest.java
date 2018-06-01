import com.alibaba.fastjson.JSON;
import com.htdata.iiot.hdfs2tsdb.tool.HFileAnalysis;
import com.htdata.iiot.hdfs2tsdb.tool.Tools;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

public class FileTest {
    public static void main(String[] args) {

//directory path
        String dir = "hdfs://192.168.0.78:8020/chen/test";
//opentsdb url path
        String tsdbUrl = "";

        Configuration conf = new Configuration();
        try {
            FileSystem fs = FileSystem.get(URI.create(dir),conf);
            System.out.println(URI.create(dir));

            List<Path> fList = Tools.getFileList(dir, fs);
            HFileAnalysis analysis = new HFileAnalysis();
            for(Path p : fList){
                analysis.analysis(p,fs);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void t(){
//        String line = "1,106,1526263362458,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,60.17,97.77,,,,76.2,,,,,,,,,,,,,,67.63,90.84,78.94,79.75,,,,,,,,,,,,,,,,,,,,,,,,41.54,,,,,,80.11,8.92,,59.52,78.24,,48.75,";
//        line.split(",");
        String line = ",,,1,,,";
        String[] ss = line.split(",",-1);
//        String[] ss = StringUtils.split(",,,1,,,",",");
        System.out.println(ss.length);

    }

}
