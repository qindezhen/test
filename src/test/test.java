package test;

import java.text.SimpleDateFormat;
import java.util.Date;


public class test {
	public static void main(String[] args){
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currTime =new SimpleDateFormat("yyyy-MM-dd").format(new Date());; //��ǰϵͳʱ��
		System.out.println(currTime);
	}
}
