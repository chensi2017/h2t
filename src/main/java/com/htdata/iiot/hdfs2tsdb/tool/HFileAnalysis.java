package com.htdata.iiot.hdfs2tsdb.tool;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.htdata.iiot.hdfs2tsdb.Config.Configs;
import com.htdata.iiot.hdfs2tsdb.Start;
import com.htdata.iiot.hdfs2tsdb.pojo.DataPoint;
import com.htiiot.common.util.JedisMultiPool;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import java.io.*;
import java.util.*;

public class HFileAnalysis {
    private static Logger log = Logger.getLogger(HFileAnalysis.class);
    private Post post;
    public void analysis(Path path, FileSystem fs){
        log.info("正在解析文件:"+path);
        try {
            InputStream in = fs.open(path);
            Reader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String str;
            while((str=br.readLine())!=null){
                toTsdb(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("解析文件出错:"+path+"\r\n"+e);
        }
    }

    public void initPost(){
        if(post==null) {
            post = new Post(Configs.BaseConfig.url);
        }
    }

    public void toTsdb(String line){
        initPost();
        List<DataPoint> metrics = formateLine(line);
//        System.out.println(metrics);
        if(metrics.size()!=0){
            String jsonString = JSONObject.toJSONString(metrics);
            post.SendPost(jsonString);
            Start.metricAdder.add(metrics.size());
            Start.longAdder.add(1);
        }
    }

    //1,28829,1526263901458,,,,39.78,84.01,,,,21.48,,89.13,92.13,,,,17.95,76.24,52.93
    public List<DataPoint> formateLine(String line){
        String[] strs = line.split(",",-1);
        String tid = strs[0];
        String did = strs[1];
        long ts = Long.parseLong(strs[2]);
        String[] values = Arrays.copyOfRange(strs, 3, strs.length);
        String key = "k2h:"+tid+":"+did;
        Jedis jedis = JedisMultiPool.getJedis();
        Set<String> set = jedis.smembers(key);
        jedis.close();
        if(set.size()!=values.length){
            log.error("数据中的value与redis中dn数量不匹配!!!");
            log.error("redis-key:"+key+";dn数量:"+set.size()+";line:"+line+";value数量:"+values.length);
            return null;
        }
        List<DataPoint> list = new ArrayList<>();
        Iterator<String> it = set.iterator();
        int count = 0;
        while(it.hasNext()){
            String dn = it.next();
            String value = values[count];
            if(Strings.isNullOrEmpty(value)){
                count++;
                continue;
            }
            DataPoint dp = new DataPoint();
            dp.setMetric(dn);
            dp.setTimestamp(ts);
            dp.setValue(Double.parseDouble(value));
            dp.addTags("tid",tid);
            dp.addTags("did",did);
            list.add(dp);
        }
        return list;
    }

}
