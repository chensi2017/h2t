package com.htdata.iiot.hdfs2tsdb.Config;

import com.htiiot.common.config.ConfigFactory;

public class Configs {
    public static final class BaseConfig{
        public static int threads=1;
        public static boolean log= false;
        //tsdb api addr
        public static String url="http://192.168.0.79:4242/api/put";
    }
    public static final class HdfsConfig{
        public static String HDFS_PATH = "";

    }

    public static String getConf(){
        return "Config info:\r\ntsdb-api-addr:"+BaseConfig.url+"\r\nHDFS-PATH:"+HdfsConfig.HDFS_PATH+"\r\nThreadNum:"+BaseConfig.threads;
    }
}
