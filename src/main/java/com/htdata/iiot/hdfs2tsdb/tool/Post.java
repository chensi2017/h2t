package com.htdata.iiot.hdfs2tsdb.tool;

import com.alibaba.fastjson.JSONObject;
import com.htdata.iiot.hdfs2tsdb.Config.Configs;
import com.htdata.iiot.hdfs2tsdb.Start;
import com.htdata.iiot.hdfs2tsdb.pojo.DataPoint;
import com.htdata.iiot.hdfs2tsdb.pojo.StreamingData;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Post {
	private static Logger logger = Logger.getLogger(Post.class);
	String url = null;

	public Post(String url) {
		this.url = url;
	}

	/**
	 *      * 爬虫工具类      * @param url 传入连接地址      * @param param  设置网页编码格式     
	 * * @return     
	 */
	public String SendPost(String param) {
		// 定义一个字符串用来存储网页内容
		String result = "";
		// 定义一个缓冲字符输入流
		BufferedReader in = null;

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "close");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			int responseCode = conn.getResponseCode();
			InputStream stream=null;
			if(Configs.BaseConfig.log&&responseCode>=400){
				stream = conn.getErrorStream();
			}else{
				stream = conn.getInputStream();
			}
			
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(stream, "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += "/n" + line;
			}
			if(responseCode>=400){
				logger.error(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				// e2.printStackTrace();
			}
		}
		return result;

	}

	public void send(List<StreamingData> list) {
		List<DataPoint> metrics = process(list);
		if(metrics.size()!=0){
			String jsonString = JSONObject.toJSONString(metrics);
			SendPost(jsonString);
			Start.metricAdder.add(metrics.size());
			Start.longAdder.add(list.size());
		}
	}

	public static List<DataPoint> process(List<StreamingData> list) {
		List<DataPoint> convent = DataParser.convent(list);
		return convent;

	}
}
