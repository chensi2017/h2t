package com.htdata.iiot.tool;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.*;

public class HFileAnalysis {
    private static Logger log = Logger.getLogger(HFileAnalysis.class);
    public void analysis(Path path, FileSystem fs){
        log.info("正在解析文件:"+path);
        try {
            InputStream in = fs.open(path);
            Reader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String str;
            while((str=br.readLine())!=null){
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("解析文件出错:"+path+"\r\n"+e);
        }
    }

    public void toTSDB(String line){

    }
}
