package com.htdata.iiot;

import com.htdata.iiot.tool.HFileAnalysis;
import com.htdata.iiot.tool.Tools;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class Start {
    private static Logger log = Logger.getLogger(Start.class);

    public static void main(String[] args) {

        //directory path
        String dir = "hdfs://192.168.0.78:8020/tmp/2018";
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


}
