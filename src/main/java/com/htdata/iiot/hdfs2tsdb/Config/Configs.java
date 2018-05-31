package com.htdata.iiot.hdfs2tsdb.Config;

import com.htiiot.common.config.ConfigFactory;

public class Configs {
    public static final class BaseConfig{
        public static int threads=10;
        public static boolean log= false;
        public static String url="http://127.0.0.1:4242/api/put?summary";
    }
    public static final class HdfsConfig{
        public static String HDFS_PATH = ConfigFactory.getConfigMgr().get("hdfs_tsdb","hdfs.path","");

    }
}
