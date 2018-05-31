package com.htdata.iiot.hdfs2tsdb.pojo;

public class StreamingMetricData {
	private String k;
	private Object v;
	private String dn;
	private long ts;

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public Object getV() {
		return v;
	}

	public void setV(Object v) {
		this.v = v;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	@Override
	public String toString() {
		return "StreamingMetricData [k=" + k + ", v=" + v + ", dn=" + dn + ", ts=" + ts + "]";
	}

}
