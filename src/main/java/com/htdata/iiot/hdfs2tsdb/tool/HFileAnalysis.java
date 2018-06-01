package com.htdata.iiot.hdfs2tsdb.tool;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.htdata.iiot.hdfs2tsdb.Config.Configs;
import com.htdata.iiot.hdfs2tsdb.Start;
import com.htdata.iiot.hdfs2tsdb.pojo.DataPoint;
import com.htiiot.common.util.JedisMultiPool;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

/*class T implements Runnable{
    private List<String> list;
    private HFileAnalysis h;
    public T(List<String> list,HFileAnalysis h){
        this.list = list;
        this.h = h;
    }

    @Override
    public void run() {
        for(String line:list){
            h.toTsdb(line);
        }
    }
}*/
public class HFileAnalysis {
    private static Logger log = Logger.getLogger(HFileAnalysis.class);
    private Post post;
    public void analysis(Path path, FileSystem fs){
        log.info("正在解析文件:"+path);
        try {
            InputStream in = fs.open(path);
            Reader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);

            /*List<String> lines = new ArrayList<>();
            String str = null;
            while ((str=br.readLine())!=null){
                lines.add(str);
            }
            List<String>[] lists = Tools.avgList(lines, 10);

            for(int i=0;i<10;i++){
                new Thread(new T(lists[i],this)).start();
            }*/

            /*for(int i=0;i<20;i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                String[] ss = new String[50];
                                String line = br.readLine();
                                if (line == null) {
                                    break;
                                } else {
                                    toTsdb(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }).start();
            }*/
            for(int i=0;i<20;i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                boolean flag = false;
                                String[] ss = new String[100];
                                for(int i=0;i<100;i++) {
                                    String line = br.readLine();
                                    if(line==null){
                                        flag = true;
                                        break;
                                    }
                                    ss[i] = line;
                                }
                                toTsdb(ss);
                                if(flag){
                                    break;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }).start();
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
//        long start = System.currentTimeMillis();
        List<DataPoint> metrics = formateLine(line);
//        System.out.println("formate line time:"+(System.currentTimeMillis() - start));
//        long s2 = System.currentTimeMillis();
        if(metrics.size()!=0&&metrics!=null){
            String jsonString = JSONObject.toJSONString(metrics);
            post.SendPost(jsonString);
            Start.metricAdder.add(metrics.size());
            Start.longAdder.add(1);
        }
//        System.out.println("send message time:"+(System.currentTimeMillis() - s2));
    }

    public void toTsdb(String[] lines){
        initPost();
        List<DataPoint> allDp = new ArrayList<>();
        int c = 0;
        for(String line:lines){
            if(StringUtils.isNotEmpty(line)) {
                List<DataPoint> metrics = formateLine(line);
                allDp.addAll(metrics);
                c++;
            }
        }
        if(allDp.size()!=0&&allDp!=null){
            String jsonString = JSONObject.toJSONString(allDp);
            post.SendPost(jsonString);
            Start.metricAdder.add(allDp.size());
            Start.longAdder.add(c);
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
        if(set.size()<values.length){
            log.error("数据中的value与redis中dn数量不匹配!!!");
            log.error("redis-key:"+key+";dn数量:"+set.size()+";line:"+line+";value数量:"+values.length);
            return null;
        }
        List<DataPoint> list = new ArrayList<>();
        Iterator<String> it = set.iterator();
        int count = 0;
        while(it.hasNext()){
            if(count>values.length){
                break;
            }
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
