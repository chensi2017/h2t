package com.htdata.iiot.hdfs2tsdb.pojo;

import java.util.ArrayList;
import java.util.List;

public class StreamingData {

	private long tid;
	private long did;
	private long ts;
	private List<StreamingMetricData> data = new ArrayList<StreamingMetricData>();

	public long getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public long getDid() {
		return did;
	}

	public void setDid(long did) {
		this.did = did;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public List<StreamingMetricData> getData() {
		return data;
	}

	public void setData(List<StreamingMetricData> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "StreamingData [tid=" + tid + ", did=" + did + ", ts=" + ts + ", data=" + data + "]";
	}

}
