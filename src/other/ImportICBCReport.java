package other;

import test.DBManage;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public class ImportICBCReport {
	
	DBManage con = null;
	private static final Logger logger = Logger.getLogger(ImportICBCReport.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("E:/workspace_guangzhou/test/log4j.xml");
		
		ImportICBCReport impFunction = new ImportICBCReport();
		impFunction.start();
		
		//impFunction.checkExpress();
		//impFunction.checkExpress2();
	}
	
	public void start(){
		
//		String[] impTables = new String[]{"gh_fhva200092"};
//		String[] typecodes = new String[]{"1"};
//		String[] repnos   = new String[]{"TA209100001"};
//		String[] custids  = new String[]{"TA209100002"};		
//		String[] calibers = new String[]{"VA200092082"};
//		String[] hhs = new String[]{"VA200092001"};

		
		String[] impTables = new String[]{"gh_fhva200091","gh_fhva200092","gh_fhva200093","gh_fhva200094","gh_fhva200095","gh_fhva200431","gh_fhva200433","gh_fhva200434","gh_fhva200435","gh_fhva200111"};
		String[] typecodes = new String[]{"1","1","3","4","5","1","3","4","5","1"};
		String[] repnos   = new String[]{"TA209100001","TA209100001","TA209100001","TA209100001","TA209100001","TA243100001","TA243100001","TA243100001","TA243100001","TA211100001"};
		String[] custids  = new String[]{"TA209100002","TA209100002","TA209100002","TA209100002","TA209100002","TA243100002","TA243100002","TA243100002","TA243100002","TA211100002"};		
		String[] calibers = new String[]{"VA200091094","VA200092082","VA200093033","VA200094044","VA200095031","VA200431061","VA200433034","VA200434030","VA200435030","VA200111103"};
		String[] hhs = new String[]{"area_code","VA200092001","area_code","area_code","area_code","area_code","area_code","area_code","area_code","area_code"};
		
		String sqlStr = "",insertSql="";
		String fieldname="";
		String serno="",frepcode="";
		int num =0 ,k=0;
		StringBuffer sb = new StringBuffer();
		
		try{
						

			con = new DBManage();
			
			logger.debug("清空报表明细数据");
			con.sqlUpdate("delete from CI_REPDATA");
			insertSql = "insert into CI_REPDATA(CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME)";
			for(int i=0;i<impTables.length;i++){
				logger.debug("开始导入"+impTables[i]+"财务报表数据……");			
				sb.setLength(0);
				k=0;
				sqlStr = "select * from import02 where tname='"+impTables[i]+"' order by tname,frepcode,serno";
				//sqlStr = "select * from import02 where tname='gh_fhva200092' and frepcode='1001' and serno=43 order by tname,frepcode,serno";
				ResultSet sqlRst = con.sqlQuery(sqlStr);
				while(sqlRst.next()){
					k++;
					fieldname = sqlRst.getString("icbcfield");
					if(fieldname==null||fieldname.trim().equals("")) fieldname = "";
					else fieldname = "round(to_number("+fieldname+")*10000,2)";
					
					serno = sqlRst.getString("serno");
					frepcode = sqlRst.getString("frepcode");
					if(k!=1) sb.append(" union ");
					sb.append(" select rep."+custids[i]+",rep."+hhs[i]+",(case rep."+repnos[i]+" when '200600' then '200501' else rep."+repnos[i]+" end)||'01',(case rep."+repnos[i]+" when '200600' then '4' else '1' end)||(case substr(rep."+calibers[i]+",1,1) when '1' then '4' else '1' end)||'1'||'"+typecodes[i]+"'||'00','"+frepcode+"',fram.rowno,fram.colno,fram.subcode,'',"+(fieldname.equals("")?"0":fieldname)+",fram.showname from "+impTables[i]+" rep,FR_FREPORTFRAM fram where fram.frepcode='"+frepcode+"' and fram.serno="+serno);
					
					//logger.debug("完成导入："+serno);
				}
				sqlRst.close();
				
				num = con.sqlUpdate(insertSql+sb.toString());
				if(num<0){
					con.finalize();
					return;
				}
				
				logger.debug("完成导入"+impTables[i]+"财务报表数据。");
			}
			
			//sqlStr = "update CI_REPDATA set SNDCOLDATA=SNDCOLDATA*10000";
			//con.sqlUpdate(sqlStr);
			
			logger.debug("生成年报数据开始……");
			sqlStr = " select CUSTID,REGIONCODE,substr(REPNO,1,4)||'0101','4'||substr(CALIBER,2),FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME from CI_REPDATA where substr(REPNO,5,4)='1201' and substr(CALIBER,1,1)='1'";
			num = con.sqlUpdate(insertSql+sqlStr);
			if(num<0){
				con.finalize();
				return;
			}
			insertSql = "";
			logger.debug("生成年报数据完成");
			
//			logger.debug("清空报表明细状态");
//			con.sqlUpdate("delete from CI_REPDATALIST");
//			logger.debug("插入报表明细状态开始……");
//			sqlStr = "insert into CI_REPDATALIST(CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,STATE,ALTERTIMES,REMARK,ALTERDATE)";
//			sqlStr += " select distinct CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,'3',0,'','' from CI_REPDATA";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}			
//			logger.debug("插入报表明细状态完成。");
//			
//			logger.debug("清空报表基本信息");
//			con.sqlUpdate("delete from CI_REPDATALDX");
//			logger.debug("插入报表明细状态开始……");
//	       sqlStr = "insert into CI_REPDATALDX(CUSTID,REGIONCODE,REPNO,CALIBER,NANDO,TYPECODE           ,CURRENTTYPE,COUNTINGHOUSE,AUDITREPNO,AUDITRST,AUDITDATE,AUDITOR,MERGESCOPE,REPCUSTMGR,CREDIBILITY,SUBMITDATE,STATE)";
//	                 sqlStr += " select distinct CUSTID,REGIONCODE,REPNO,CALIBER,'0'  ,substr(CALIBER,4,1),'CNY'      ,''           ,''        ,''      ,''       ,''     ,''        ,''        ,''         ,'2009-11-25','1'   from CI_REPDATALIST";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}			
//			logger.debug("插入报表明细状态完成。");	
//			
//			//logger.debug("清空客户信息表");
//			con.sqlUpdate("delete from CI_CUSBASINFO where CUSTMGR='admin'");			
//			logger.debug("生成客户信息开始……");
//			sqlStr = "insert into CI_CUSBASINFO(CUSTID,REGIONCODE,CUSTCNAME,CUSTMGR,DEPTCODE,STATE)";
//			sqlStr += " select distinct CUSTID,REGIONCODE,'导入数据'||CUSTID,'admin','002','0' from CI_REPDATALDX";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}
//			con.sqlUpdate("delete from CI_CUSCHARACTER where (CUSTID,REGIONCODE) in(select CUSTID,REGIONCODE from CI_CUSBASINFO where CUSTMGR='admin')");
//			sqlStr = "insert into CI_CUSCHARACTER(CUSTID,REGIONCODE,character1)";
//			sqlStr += " select CUSTID,REGIONCODE,'0' from CI_CUSBASINFO where (CUSTID,REGIONCODE) in(select CUSTID,REGIONCODE from CI_CUSBASINFO where CUSTMGR='admin')";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}	
//			logger.debug("生成客户信息完成。");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
			System.err.println("insertSql:"+insertSql);
			System.err.println("sqlStr:"+sqlStr);
		}finally{
			con.finalize();
		}
		
	}
	
	@SuppressWarnings("unused")
	private void checkExpress(){
		String sqlStr = "",calexpress="",str="";
		StringBuffer sb =new StringBuffer();
		List<String> list = null; 
		int totalnum = 0,num=0;
		
		try{
			logger.debug("报表数据校验开始……");
			con = new DBManage();
			sqlStr = "update CI_REPDATALDX set state='1'";
			totalnum = con.sqlUpdate(sqlStr);
			if(totalnum<0){
				con.finalize();
				return;
			}
			String tmpstr = "insert into tmp_repdata(CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SNDCOLDATA2,SHOWNAME)";
			String tmp2 = "";
			sqlStr = "select * from FR_FREPORTFRAM where frepcode in(select distinct frepcode from import02) and length(trim(calexpress))>0 order by FREPCODE,SERNO";
			ResultSet sqlRst = con.sqlQuery(sqlStr);
			ResultSet sqlRst2 = null;
			while(sqlRst.next()){
				logger.debug("校验："+sqlRst.getString("showname")+"……");
				
				calexpress = sqlRst.getString("calexpress");
				
				list = parseCalExpress(calexpress);
				sb.setLength(0);
				sb.append("select * from (select round(coalesce(sndcoldata,0),2) leftvalue,round((");
				for(int i=0;i<list.size();i++){
					str = list.get(i);
					
					if(str.substring(0, 1).equals("n")){
						sb.append("coalesce((select sndcoldata from ci_repdata where CUSTID=lt.CUSTID and REGIONCODE=lt.REGIONCODE and REPNO=lt.REPNO and CALIBER=lt.CALIBER and FREPCODE=lt.FREPCODE and COURSECODE='"+str.substring(1)+"'),0)");
					}else{
						sb.append(str.substring(1));
					}
				}
				sb.append("),2) rightvalue,");
				sb.append(" CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,ROWNO,COLNO,COURSECODE,SHOWNAME from ci_repdata lt where lt.COURSECODE='"+sqlRst.getString("subcode")+"') tmp where tmp.leftvalue!=tmp.rightvalue");
				
				
				tmp2 = " select CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,ROWNO,COLNO,COURSECODE,leftvalue,rightvalue,0,SHOWNAME from ("+sb.toString()+") unsame";
				num = con.sqlUpdate(tmpstr+tmp2);
				if(num<0){
					con.finalize();
					return;
				}
//				sqlRst2 = con.sqlQuery2(sb.toString());
//				int countnum = 0;
//				while(sqlRst2.next()){
//					countnum++;
//					if(countnum>=20){
//						logger.debug("……………………");
//						break;
//					}
//					
//					logger.debug("校验公式不通过：客户["+sqlRst2.getString("custid")+"],行号["+sqlRst2.getString("REGIONCODE")+"],报表期数["+sqlRst2.getString("repno")+"],口径["+sqlRst2.getString("caliber")+"],报表类型["+sqlRst.getString("frepcode")+"],科目["+sqlRst.getString("showname")+"],公式["+sqlRst.getString("calexpress")+"],差额["+sqlRst2.getString("leftvalue")+"-"+sqlRst2.getString("rightvalue")+"="+df.format(sqlRst2.getDouble("leftvalue")-sqlRst2.getDouble("rightvalue"))+"]");
//				}
				
				//sqlStr = "update CI_REPDATALDX set state='0' where state='1' and (CUSTID,REGIONCODE,REPNO,CALIBER) in (select CUSTID,REGIONCODE,REPNO,CALIBER from ("+sb.toString()+") unsame)";
//				num = con.sqlUpdate(sqlStr);
//				if(num<0){
//					con.finalize();
//					return;
//				}	

			}
			
			//sqlRst2.close();
			sqlRst.close();
			
			logger.debug("报表数据校验完成");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
		}finally{			
			con.finalize();
		}
	}
	
	@SuppressWarnings({ "unused", "null" })
	private void checkExpress2(){
		String sqlStr = "",calexpress="",str="";
		StringBuffer sb =new StringBuffer();
		List<String> list = null; 
		DecimalFormat df = new DecimalFormat("#,##0.00");
		int totalnum = 0,num=0;
		try{
			logger.debug("报表数据校验开始2……");
			con = new DBManage();
//			sqlStr = "update CI_REPDATALDX set SUBMITDATE='2009-11-25'";
//			totalnum = con.sqlUpdate(sqlStr);
//			if(totalnum<0){
//				con.finalize();
//				return;
//			}			
			sqlStr = "select * from FR_FREPORTFRAM where frepcode in(select distinct frepcode from import02) and length(trim(calexpress))>0 order by FREPCODE,SERNO";
			ResultSet sqlRst = con.sqlQuery(sqlStr);
			ResultSet sqlRst2 = null;
			while(sqlRst.next()){
				logger.debug("校验："+sqlRst.getString("showname")+"……");
				
				calexpress = sqlRst.getString("calexpress");
				
				list = parseCalExpress(calexpress);
				sb.setLength(0);
				sb.append("select * from (select round(coalesce(sndcoldata,0),2) leftvalue,round((");
				for(int i=0;i<list.size();i++){
					str = list.get(i);
					
					if(str.substring(0, 1).equals("n")){
						sb.append("coalesce((select sndcoldata from ci_repdata where CUSTID=lt.CUSTID and REGIONCODE=lt.REGIONCODE and REPNO=lt.REPNO and CALIBER=lt.CALIBER and FREPCODE=lt.FREPCODE and COURSECODE='"+str.substring(1)+"'),0)");
					}else{
						sb.append(str.substring(1));
					}
				}
				sb.append("),2) rightvalue,");
				sb.append(" CUSTID,REGIONCODE,REPNO,CALIBER from ci_repdata lt where lt.COURSECODE='"+sqlRst.getString("subcode")+"') tmp where tmp.leftvalue!=tmp.rightvalue");
//				sqlRst2 = con.sqlQuery2(sb.toString());
//				int countnum = 0;
//				while(sqlRst2.next()){
//					countnum++;
//					if(countnum>=20){
//						logger.debug("……………………");
//						break;
//					}
//					
//					logger.debug("校验公式不通过：客户["+sqlRst2.getString("custid")+"],行号["+sqlRst2.getString("REGIONCODE")+"],报表期数["+sqlRst2.getString("repno")+"],口径["+sqlRst2.getString("caliber")+"],报表类型["+sqlRst.getString("frepcode")+"],科目["+sqlRst.getString("showname")+"],公式["+sqlRst.getString("calexpress")+"],差额["+sqlRst2.getString("leftvalue")+"-"+sqlRst2.getString("rightvalue")+"="+df.format(sqlRst2.getDouble("leftvalue")-sqlRst2.getDouble("rightvalue"))+"]");
//				}
				
				sqlStr = "update CI_REPDATALDX set SUBMITDATE='2009-11-24' where SUBMITDATE='2009-11-25' and (CUSTID,REGIONCODE,REPNO,CALIBER) in (select CUSTID,REGIONCODE,REPNO,CALIBER from ("+sb.toString()+") unsame)";
				num = con.sqlUpdate(sqlStr);
				if(num<0){
					con.finalize();
					return;
				}
			}
			
			sqlRst2.close();
			sqlRst.close();
			
			logger.debug("报表数据校验完成2");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
		}finally{			
			con.finalize();
		}
	}
	
	private List<String> parseCalExpress(String calExpress){
		List<String> list = new ArrayList<String>();
		String kmbegin = "0",signbegin = "0",str = "";
		calExpress = calExpress.replaceAll(" ","");
		for(int i=0;i<calExpress.length();i++){
			int hc = calExpress.substring(i,i+1).hashCode();
			if((hc>=48&&hc<=57)||(hc>=97&&hc<=122)||(hc>=65&&hc<=90)){
				signbegin = "0";
				if(kmbegin.equals("0")) {
					kmbegin = "1";
					if(!str.equals("")) list.add("c"+str);
						
					str = calExpress.substring(i,i+1);
				}else {
					str += calExpress.substring(i,i+1);
				}
			}else{
				kmbegin = "0";
				if(signbegin.equals("0")) {
					signbegin = "1";
					if(!str.equals("")) list.add("n"+str);
						
					str = calExpress.substring(i,i+1);
				}else {
					str += calExpress.substring(i,i+1);
				}
			}
		}
		
		if(kmbegin.equals("1")){
			if(!str.equals("")) list.add("n"+str);
		}else{
			if(!str.equals("")) list.add("c"+str);
		}
		
		return list;
	}
}
