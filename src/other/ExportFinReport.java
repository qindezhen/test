package other;

import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import test.DBManage;


public class ExportFinReport {
	DBManage con = null;
	private static final Logger logger = Logger.getLogger(ExportFinReport.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("E:/workspace_guangzhou/test/log4j2.xml");
//		ExportFinReport exp = new ExportFinReport();
	}
	
	public void todo(){
		String sqlStr = "";
		ResultSet sqlRst = null;
		
		try{
			con = new DBManage();
			sqlStr = "select custid,regioncode,'0'||substr(caliber,2,1) repatrr,(case when trim(typecode) in('0','1','2') then '02' else '10' end) reptype,'0'||substr(caliber,1,1) repperiod,currenttype,substr(repno,1,6) repno,substr(caliber,3,1) isaudit,countinghouse,auditrepno,auditrst,auditdate,mergescope,auditor,repcustmgr,submitdate aldate,submitdate pldate,submitdate cashflowdate,repno,caliber from ci_repdataldx where state='1' and substr(caliber,4,1)!='0'";
			sqlRst = con.sqlQuery(sqlStr);
			while(sqlRst.next()){
				
			}
			logger.debug("Íê³Éall");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
		}finally{			
			con.finalize();
		
		}
	
	}
}
