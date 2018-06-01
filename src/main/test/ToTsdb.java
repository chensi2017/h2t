import com.alibaba.fastjson.JSONObject;
import com.htdata.iiot.hdfs2tsdb.pojo.DataPoint;
import com.htdata.iiot.hdfs2tsdb.tool.Post;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToTsdb {
    @Test
    public void save(){
        DataPoint dp = new DataPoint();
        dp.setValue(22.22);
        dp.setMetric("22222222222222222222222222222222");
//        long ts = new Date().getTime();
//        System.out.println(ts);
        dp.setTimestamp(1527756714921l);
        dp.addTags("tid","2");
        dp.addTags("did","22");
        Post p = new Post("http://192.168.0.79:4242/api/put?details");
        List<DataPoint> list = new ArrayList<>();
        list.add(dp);
        String s = JSONObject.toJSONString(list);
        p.SendPost(s);
    }
}
