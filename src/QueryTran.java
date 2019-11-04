import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class QueryTran {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		URL url = new URL("http://124.254.47.180:8080/otsquery/query/queryRemanentTicketAction.do?method=queryTrain&orderRequest.from_station_telecode=ty&orderRequest.to_station_telecode=jn&orderRequest.train_date=2013-08-30");   
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.connect();  
		//打印请求相应的头部文件   
		Map<String,List<String>> header = conn.getHeaderFields();  
		for(String key : header.keySet()){  
		     System.out.println(key + ":" + header.get(key));  
		}  
		//打印相应内容   
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));  
		String str = null;  
		while((str = br.readLine()) != null){  
		  System.out.println(str);  
		}   
		conn.disconnect();  
	}

}
