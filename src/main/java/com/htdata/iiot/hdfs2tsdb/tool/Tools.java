package com.htdata.iiot.hdfs2tsdb.tool;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tools {
    private static Logger log = Logger.getLogger(Tools.class);
    private static List<Path> fList = new ArrayList<>();

    /**
     * average a list into small list
     * @param allList
     * @param threadNum
     * @return
     */
    public static <T> List<T>[] avgList(List<T> allList,int threadNum){
        List<T>[] listA = new List[threadNum];
        for(int i=0;i<listA.length;i++){
            listA[i] = new ArrayList();
        }
        for(int i=0;i<allList.size();i++){
            listA[i%threadNum].add(allList.get(i));
        }
        return listA;
    }

    /**
     * 获取hdfs给定路径下所有文件地址
     * @param path 路径
     * @param fs
     * @return  所有文件地址的集合
     */
    public static List<Path> getFileList(String path,FileSystem fs){
        getFilePath(path,fs);
        return fList;
    }

    public static void getFilePath(String path,FileSystem fs){
        try {
            FileStatus[] status = fs.listStatus(new Path(path));
            for(FileStatus file:status){
                if(!file.isDirectory()){
                    fList.add(file.getPath());
                }else{
                    getFilePath(file.getPath().toString(),fs);
                }
            }
        } catch (IOException e) {
            log.error("IO异常！路径:"+path+"\r\n"+e);
            e.printStackTrace();
        }
    }

}
