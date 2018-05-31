package com.htdata.iiot.hdfs2tsdb;

import com.htdata.iiot.hdfs2tsdb.Config.Configs;
import com.htdata.iiot.hdfs2tsdb.cmd.Cmd;
import com.htdata.iiot.hdfs2tsdb.tool.HFileAnalysis;
import com.htdata.iiot.hdfs2tsdb.tool.Tools;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;


public class Start {

    private static Logger log = Logger.getLogger(Start.class);
    public static LongAdder longAdder = new LongAdder();
    public static LongAdder metricAdder = new LongAdder();

    public static void main(String[] args) {
        Cmd.parserCmd(args);
        //directory path
        String dir = Configs.HdfsConfig.HDFS_PATH;
        log.info("已启动解析程序...");
        long startTime = System.currentTimeMillis();
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
        new Thread(new Runnable() {
            private long oldcount = 0;
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(10000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    long count = longAdder.sum();
                    long mcount = metricAdder.sum();
                    long endTime = System.currentTimeMillis();
                    long thisTime = endTime - startTime;
                    log.info("获取到数据:"+count+"\t平均速率:"+(count*1000/thisTime)+"\t测点数:"+mcount+"\t测点速率:"+(mcount*1000/thisTime)+"\t this time speed :"+((count-oldcount)/10));
                    oldcount = count;
                }
            }
        }).start();

    }
}
