package com.htdata.iiot.hdfs2tsdb.tool;

import com.alibaba.fastjson.JSONObject;
import com.htdata.iiot.hdfs2tsdb.pojo.DataPoint;
import com.htdata.iiot.hdfs2tsdb.pojo.StreamingData;
import com.htdata.iiot.hdfs2tsdb.pojo.StreamingMetricData;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class DataParser {
	private static Logger logger = Logger.getLogger(DataParser.class);

	private static DataParser dataParser = new DataParser();

	public static DataParser getDataParser() {
		return dataParser;
	}

	public StreamingData toStreamingData(String data) {
		return JSONObject.parseObject(data, StreamingData.class);
	}

	public static List<DataPoint> convent(List<StreamingData> streamDataList){
		List<DataPoint> metrics = new ArrayList<>();
		for(StreamingData streamData:streamDataList) {
			List<DataPoint> convent = convent(streamData);
			metrics.addAll(convent);
		}
		return metrics;
	}
	public static List<DataPoint> convent(StreamingData streamData){
		List<DataPoint> metrics = new ArrayList<>();
		List<StreamingMetricData> data = streamData.getData();
		for(StreamingMetricData metricData:data){
			DataPoint dataPoint = new DataPoint();
			try {
				dataPoint.setValue(Double.parseDouble(metricData.getV().toString()));
			} catch (Exception e) {
				logger.error(e.getMessage()+":\n"+streamData);
				continue;
			}
			dataPoint.addTags("tid", streamData.getTid());
			dataPoint.addTags("did", streamData.getDid());
			dataPoint.setMetric(metricData.getDn());
			long ts = metricData.getTs()==0?streamData.getTs():metricData.getTs();
			dataPoint.setTimestamp(ts);
			metrics.add(dataPoint);
		}
		return metrics;
	}


}
