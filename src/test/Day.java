package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Day {

	public static void main(String[] args) throws ParseException {

		
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		System.out.println("TO_DATE('"+ format.format(d)+ "','YYYY-MM-DD HH24:MI:SS.FF')");
		String caliberTran="411510";
		System.out.println(caliberTran.substring(1,2)+caliberTran.substring(3,4));
		
		
	}
}
