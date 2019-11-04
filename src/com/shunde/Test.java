package com.shunde;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Test {
	public static void main(String[] args){
		try{
			Socket socket = new Socket("127.0.0.1",9999);
			OutputStream os = socket.getOutputStream();
			String str = "<?xml version=\"1.0\" encoding=\"GBK\"?><Root><Head><errorCode>111</errorCode><errorMessage>1</errorMessage></Head><Body><Object><Record><bill_no>10003800900077</bill_no></Record></Object></Body></Root>";
			byte[] bytes = str.getBytes("utf-8");
			System.out.println("数据byte长度："+bytes.length);
			os.write(Constant.intTobytes(bytes.length));
			os.write(bytes);
			os.flush();
			InputStream is = null;
			
			is = socket.getInputStream();
			byte[] intByte = new byte[4];
			is.read(intByte);
			int msgLength = Constant.bytesToint(intByte);
			System.out.println("int:"+msgLength);
			int readPosition = 0;
			byte[] byteArray = new byte[msgLength];
			while(readPosition < msgLength){
				int value = is.read(byteArray, readPosition, msgLength-readPosition);
				if(value == -1){
					break;
				}
				readPosition += value;
				
			}
			System.out.println(new String(byteArray,"utf-8"));
			System.out.println("byteArray Length:"+byteArray.length+" string Length:"+new String(byteArray,"utf-8").length());
			if(os != null){
				os.close();
			}
			if(is != null){
				is.close();
			}
			if (socket != null) {
				socket.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
