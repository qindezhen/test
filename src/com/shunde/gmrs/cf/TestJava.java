package com.shunde.gmrs.cf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * �����ź��˹�������
 * @author luke.lee
 *
 */
public class TestJava {
	public static void main(String[] args) throws IOException {
		String dshUrl = "http://192.168.117.47:7004/dsh/rest/credit/P_CU_RR_GLRS";
		String content ="";
		URL address_url = new URL(dshUrl);
		
		HttpURLConnection conn = (HttpURLConnection)address_url.openConnection();
		
		conn.setConnectTimeout(10000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setInstanceFollowRedirects(false);
		conn.setRequestProperty("Content-Type", "application/json");
		
		String reqId = "ds_7777";
		System.out.println(reqId);
		OutputStream os = conn.getOutputStream();
		String json = "{"
				+ "\"reqId\":\""+reqId+"\","
				+ "\"certType\":\"25\","
				+ "\"certNo\":\"371525200008524\","
				+ "\"cifCustNo\":\"8531000156545\","
				+ "\"custName\":\"光耀集团有限公司\","
				+ "\"riskSignId\":\"05356770865\","
				+ "\"riskSignStat\":\"02\""
				+ "}";
	
		
		os.write(json.getBytes());
		os.flush();
		os.close();
		
		InputStream in = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			content += line;
		}
		if (content.startsWith("null"))
			content = content.substring(4, content.length());
		System.out.println(content);
		
		
		reader.close();
		in.close();
		conn.disconnect();
	}
}
