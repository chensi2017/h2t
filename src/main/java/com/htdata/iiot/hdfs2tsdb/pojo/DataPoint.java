package com.htdata.iiot.hdfs2tsdb.pojo;

import java.util.HashMap;
import java.util.Map;

public class DataPoint {
	
	/**
	 * 
        "metric": "sys.cpu.nice",
        "timestamp": 1346846400,
        "value": 18,
        "tags": {
           "host": "web01",
           "dc": "lga"
        }
    }
	 */
	private String metric;
	private long timestamp;
	private Double value;
	private Map<String, String> tags = new HashMap<String, String>();
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp/1000;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Map<String, String> getTags() {
		return tags;
	}
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
	public void addTags(String k,Object v) {
		this.tags.put(k, v.toString());
	}
	
}