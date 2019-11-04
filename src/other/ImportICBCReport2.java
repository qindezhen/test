package other;

import test.DBManage;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImportICBCReport2 {
	
	DBManage con = null;
	private static final Logger logger = Logger.getLogger(ImportICBCReport2.class);
	/**
	 * @param args
	 * 主程序
	 * 根据不同的处理，需要调用不同的方法
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("E:/workspace_guangzhou/test/log4j2.xml");
		
		ImportICBCReport2 impFunction = new ImportICBCReport2();
		//
//		impFunction.datado();
//		impFunction.datahb();
//		impFunction.start();
//		impFunction.start2();
//		impFunction.start3();
		
		impFunction.checkExpress();
//		impFunction.checkExpress3();
		
		
	}
	
	
	/*
	 * 排重处理
	 * 按照相同客户代码、报表日期、是否合并报表为一条记录，由原表生成“原表_2”表
	 */
	@SuppressWarnings("unused")
	private void datado(){
		//原表
		String[] impTables = new String[]{"gh_fhva200091","gh_fhva200092","gh_fhva200093","gh_fhva200094","gh_fhva200095","gh_fhva200431","gh_fhva200433","gh_fhva200434","gh_fhva200435","gh_fhva200111"};
		//排重后的新表
		String[] impTables2 = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200094_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200434_2","gh_fhva200435_2","gh_fhva200111_2"};
//		String[] typecodes = new String[]{"1","1","3","4","5","1","3","4","5","1"};
		//报表日期
		String[] repnos   = new String[]{"TA209100001","TA209100001","TA209100001","TA209100001","TA209100001","TA243100001","TA243100001","TA243100001","TA243100001","TA211100001"};
		//客户代码
		String[] custids  = new String[]{"TA209100002","TA209100002","TA209100002","TA209100002","TA209100002","TA243100002","TA243100002","TA243100002","TA243100002","TA211100002"};
		//是否合并
		String[] calibers = new String[]{"VA200091094","VA200092082","VA200093033","VA200094044","VA200095031","VA200431061","VA200433034","VA200434030","VA200435030","VA200111103"};
//		String[] hhs = new String[]{"area_code","VA200092001","area_code","area_code","area_code","area_code","area_code","area_code","area_code","area_code"};
		String sqlStr = "";
//		String[] fields = new String[200];
//		int[] fieldnum = new int[]{96,83,35,46,33,63,36,32,};
		int total = 0,num = 0;
		
		try{
			con = new DBManage();
			
			for(int i=0;i<impTables.length;i++){
				logger.debug("处理表"+impTables[i]);
				con.sqlUpdate("delete from "+impTables2[i]);
				
				total=0;
				sqlStr = "select "+custids[i]+","+repnos[i]+",substr("+calibers[i]+",1,1) caliber from "+impTables[i]+" group by "+custids[i]+","+repnos[i]+",substr("+calibers[i]+",1,1)";
				ResultSet sqlRst = con.sqlQuery(sqlStr);
				while(sqlRst.next()){
					sqlStr = "insert into "+impTables2[i]+" select * from "+impTables[i]+" where "+custids[i]+"='"+sqlRst.getString(custids[i])+"' and "+repnos[i]+"='"+sqlRst.getString(repnos[i])+"' and substr("+calibers[i]+",1,1)='"+sqlRst.getString("caliber")+"' and rownum<=1";
					num = con.sqlUpdate(sqlStr);
					if(num<0){
						con.finalize();
						return;
					}
					
					total += num;
				}
				sqlRst.close();
				
				logger.debug("完成："+total);
			}
			logger.debug("完成all");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
		}finally{			
			con.finalize();
		
		}
	}
	/*
	 * 删除“原表_2”表中存在于旧表中的数据
	 * ghold_01对应CM2002_TA200091
	 * ghold_02对应CM2002_TA200431
	 */
	@SuppressWarnings("unused")
	private void datahb(){
		//"原表_2"表
		String[] impTables = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200094_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200434_2","gh_fhva200435_2"};
		//报表日期
		String[] repnos   = new String[]{"TA209100001","TA209100001","TA209100001","TA209100001","TA209100001","TA243100001","TA243100001","TA243100001","TA243100001"};
		//客户代码
		String[] custids  = new String[]{"TA209100002","TA209100002","TA209100002","TA209100002","TA209100002","TA243100002","TA243100002","TA243100002","TA243100002"};		
		//是否合并报表("原表_2"表中)
		String[] calibers = new String[]{"VA200091094","VA200092082","VA200093033","VA200094044","VA200095031","VA200431061","VA200433034","VA200434030","VA200435030"};
		//是否合并报表(CM2002_TA200091/CM2002_TA200431表中)
		String[] calibers2 =  new String[]{"TA209120002","TA209120002","TA209120002","TA209120002","TA209120002","TA243120002","TA243120002","TA243120002","TA243120002"};
		//CM2002_TA200091/CM2002_TA200431
		String[] oldtables = new String[]{"ghold_01","ghold_01","ghold_01","ghold_01","ghold_01","ghold_02","ghold_02","ghold_02","ghold_02"};
		int num=0;
		
		try{
			con = new DBManage();
			for(int i=0;i<impTables.length;i++){
				logger.debug("处理表"+impTables[i]);
				num=con.sqlUpdate("delete from "+impTables[i]+" where ("+custids[i]+","+repnos[i]+",substr("+calibers[i]+",1,1)) in(select cisno,workdate,"+calibers2[i]+" from "+oldtables[i]+")");
				if(num<0){
					con.finalize();
					return;
				}else if(num>0){
					logger.debug("num："+num);
				}
			}
			
			logger.debug("处理表完成！");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
		}finally{			
			con.finalize();
		
		}
	}
	/*
	 * 按照import02对应关系把“原表_2”中的数据导入到CI_REPDATA中
	 * 
	 */
	public void start(){
		
//		String[] impTables= new String[]{"gh_fhva200433_2"};
//		String[] typecodes= new String[]{"3"              };
//		String[] repnos   = new String[]{"TA243100001"    };
//		String[] custids  = new String[]{"TA243100002"    };
//		String[] calibers = new String[]{"VA200433034"    };
//		String[] hhs      = new String[]{"area_code"      };

		//"原表_2"
		String[] impTables= new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200094_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200434_2","gh_fhva200435_2","gh_fhva200111_2"};
		//对应的行业类型
		String[] typecodes= new String[]{"1"              ,"1"              ,"3"              ,"4"              ,"5"              ,"1"              ,"3"              ,"4"              ,"5"              ,"1"};
		//报表日期
		String[] repnos   = new String[]{"TA209100001"    ,"TA209100001"    ,"TA209100001"    ,"TA209100001"    ,"TA209100001"    ,"TA243100001"    ,"TA243100001"    ,"TA243100001"    ,"TA243100001"    ,"TA211100001"};
		//客户代码
		String[] custids  = new String[]{"TA209100002"    ,"TA209100002"    ,"TA209100002"    ,"TA209100002"    ,"TA209100002"    ,"TA243100002"    ,"TA243100002"    ,"TA243100002"    ,"TA243100002"    ,"TA211100002"};		
		//报表口径
		String[] calibers = new String[]{"VA200091094"    ,"VA200092082"    ,"VA200093033"    ,"VA200094044"    ,"VA200095031"    ,"VA200431061"    ,"VA200433034"    ,"VA200434030"    ,"VA200435030"    ,"VA200111103"};
		//String[] hhs      = new String[]{"area_code"      ,"VA200092001"    ,"area_code"      ,"area_code"      ,"area_code"      ,"area_code"      ,"area_code"      ,"area_code"      ,"area_code"      ,"area_code"};
//		
		String sqlStr = "",insertSql="";
		String fieldname="";
		String serno="",frepcode="";
		int num =0, k=0;
		StringBuffer sb = new StringBuffer();
		
		try{
						

			con = new DBManage();
			
			logger.debug("清空报表明细数据");
			//con.sqlUpdate("delete from CI_REPDATA");
			insertSql = "insert into CI_REPDATA(CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME)";
			for(int i=0;i<impTables.length;i++){
				logger.debug("开始导入"+impTables[i]+"财务报表数据……");			
				sb.setLength(0);
				k=0;
				sqlStr = "select * from import02 where tname='"+impTables[i]+"' order by tname,frepcode,serno";
				//sqlStr = "select * from import02 where tname='gh_fhva200092' and frepcode='1001' and serno=43 order by tname,frepcode,serno";
				ResultSet sqlRst = con.sqlQuery(sqlStr);
				while(sqlRst.next()){
					fieldname = sqlRst.getString("icbcfield");
					if(fieldname==null||fieldname.trim().equals("")) fieldname = "";
					else fieldname = "round(to_number("+fieldname+")*10000,2)";
					
					serno = sqlRst.getString("serno");
					frepcode = sqlRst.getString("frepcode");
					sb.setLength(0);
//					if(k!=1) sb.append(" union ");
					//sb.append(" select rep."+custids[i]+",rep."+hhs[i]+",(case rep."+repnos[i]+" when '200600' then '200501' else rep."+repnos[i]+" end)||'01',(case rep."+repnos[i]+" when '200600' then '4' else '1' end)||(case substr(rep."+calibers[i]+",1,1) when '1' then '4' else '1' end)||'1'||'"+typecodes[i]+"'||'00','"+frepcode+"',fram.rowno,fram.colno,fram.subcode,'',"+(fieldname.equals("")?"0":fieldname)+",fram.showname from "+impTables[i]+" rep,FR_FREPORTFRAM fram where fram.frepcode='"+frepcode+"' and fram.serno="+serno);
					sb.append(" select rep."+custids[i]+",'0769',(case rep."+repnos[i]+" when '200600' then '200501' else rep."+repnos[i]+" end)||'01',(case rep."+repnos[i]+" when '200600' then '4' else '1' end)||(case substr(rep."+calibers[i]+",1,1) when '1' then '4' else '1' end)||'1'||'"+typecodes[i]+"'||'00','"+frepcode+"',fram.rowno,fram.colno,fram.subcode,'',nvl(("+(fieldname.equals("")?"0":fieldname)+"),''),fram.showname from "+impTables[i]+" rep,FR_FREPORTFRAM fram where fram.frepcode='"+frepcode+"' and fram.serno="+serno);
					//logger.debug("完成导入："+serno);
					
					logger.debug("\t\t导入行次"+serno);	
					num = con.sqlUpdate(insertSql+sb.toString());
					if(num<0){
						con.finalize();
						return;
					}
					logger.debug("\t\t导入完成"+num);
					k+=num;
				}
				sqlRst.close();
				

				
				logger.debug("完成导入"+impTables[i]+"财务报表数据。"+k);
			}
			
		
//			logger.debug("生成年报数据开始……");
//			sqlStr = " select CUSTID,REGIONCODE,substr(REPNO,1,4)||'0101','4'||substr(CALIBER,2),FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME from CI_REPDATA where substr(REPNO,5,4)='1201' and substr(CALIBER,1,1)='1'";
//			num = con.sqlUpdate(insertSql+sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}
//			insertSql = "";
//			logger.debug("生成年报数据完成");
//			
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
//			logger.debug("插入报表基本信息……");
//	       sqlStr = "insert into CI_REPDATALDX(CUSTID,REGIONCODE,REPNO,CALIBER,NANDO,TYPECODE           ,CURRENTTYPE,COUNTINGHOUSE,AUDITREPNO,AUDITRST,AUDITDATE,AUDITOR,MERGESCOPE,REPCUSTMGR,CREDIBILITY,SUBMITDATE,STATE)";
//	                 sqlStr += " select distinct CUSTID,REGIONCODE,REPNO,CALIBER,'0'  ,substr(CALIBER,4,1),'CNY'      ,''           ,''        ,''      ,''       ,''     ,''        ,''        ,''         ,'2009-11-25','1'   from CI_REPDATALIST";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}			
//			logger.debug("插入报表基本信息完成。");	
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
			
			logger.debug("处理完成！");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
			System.err.println("insertSql:"+insertSql);
			System.err.println("sqlStr:"+sqlStr);
		}finally{
			con.finalize();
		}
		
	}
	
	/*
	 * 通过import02对应关系把ghold_01_02、ghold_02_02表中的数据导入到ci_repdata中
	 * ghold_01_02对应ghold_01，ghold_01_02中去除了未完成的数据，ghold_02_02同理。
	 */
	public void start2(){
		
//		String[] impTables = new String[]{"gh_fhva200433_2"};
//		String[] oldtables = new String[]{"ghold_02"    };
//		String[] typecodes = new String[]{"3"              };
//		String[] calibers2 = new String[]{"TA243120002"    };
//		String[] oldhycode = new String[]{"TA243120003"    };
//		String[] oldhyvalue =new String[]{"33"             };


//		String[] impTables = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200094_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200434_2","gh_fhva200435_2"};
//		String[] oldtables = new String[]{"ghold_01"       ,"ghold_01"       ,"ghold_01"       ,"ghold_01"       ,"ghold_01"       ,"ghold_02"       ,"ghold_02"       ,"ghold_02"       ,"ghold_02"};
//		String[] typecodes = new String[]{"1"              ,"1"              ,"3"              ,"4"              ,"5"              ,"1"              ,"3"              ,"4"              ,"5","1"};
//		String[] calibers2 =  new String[]{"TA209120002"   ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};
//		String[] oldhycode =  new String[]{"TA209120003"   ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"};
//		String[] oldhyvalue =  new String[]{"1"            ,"2"              ,"3"              ,"4"              ,"5"              ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};

		String[] impTables = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200091_2","gh_fhva200094_2","gh_fhva200091_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200431_2","gh_fhva200434_2","gh_fhva200431_2","gh_fhva200435_2"};
		String[] oldtables = new String[]{"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"};
		String[] typecodes = new String[]{"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"              ,"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"};
		String[] calibers2 = new String[]{"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};
		String[] oldhycode = new String[]{"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"};
		String[] oldhyvalue =new String[]{"11"             ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"              ,"11"            ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"};
		//import02表中对应关系
//		String[] impTables = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200091_2","gh_fhva200094_2","gh_fhva200091_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200431_2","gh_fhva200434_2","gh_fhva200431_2","gh_fhva200435_2"};
//		//老表
//		String[] oldtables = new String[]{"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"};
//		//老表对应的行业类型
//		String[] typecodes = new String[]{"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"              ,"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"};
//		//老表对应的是否合并
//		String[] calibers2 = new String[]{"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};
//		//老表对应的行业类型字段
//		String[] oldhycode = new String[]{"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"};
////		老表对应的行业类型字段值
//		String[] oldhyvalue =new String[]{"11"             ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"              ,"11"            ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"};

		String sqlStr = "",insertSql="";
		String fieldname="";
		String serno="",frepcode="";
		int num =0 ,k=0;
		StringBuffer sb = new StringBuffer();
		
		try{
						

			con = new DBManage();
			
//			logger.debug("旧数据处理开始……");
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
					sb.setLength(0);
//					if(k!=1) sb.append(" union ");
					//sb.append(" select rep."+custids[i]+",rep."+hhs[i]+",(case rep."+repnos[i]+" when '200600' then '200501' else rep."+repnos[i]+" end)||'01',(case rep."+repnos[i]+" when '200600' then '4' else '1' end)||(case substr(rep."+calibers[i]+",1,1) when '1' then '4' else '1' end)||'1'||'"+typecodes[i]+"'||'00','"+frepcode+"',fram.rowno,fram.colno,fram.subcode,'',"+(fieldname.equals("")?"0":fieldname)+",fram.showname from "+impTables[i]+" rep,FR_FREPORTFRAM fram where fram.frepcode='"+frepcode+"' and fram.serno="+serno);
					sb.append(" select rep.cisno,'0769',(case substr(rep.workdate,5,2) when '00' then to_char(to_number(substr(rep.workdate,1,4))-1)||'01' else rep.workdate end)||'01',(case substr(rep.workdate,5,2) when '00' then '4' else '1' end)||(case substr(rep."+calibers2[i]+",1,1) when '1' then '4' else '1' end)||'1'||'"+typecodes[i]+"'||'00','"+frepcode+"',fram.rowno,fram.colno,fram.subcode,'',nvl(("+(fieldname.equals("")?"0":fieldname)+"),''),fram.showname from "+oldtables[i]+" rep,FR_FREPORTFRAM fram where rep."+oldhycode[i]+"='"+oldhyvalue[i]+"' and fram.frepcode='"+frepcode+"' and fram.serno="+serno);
					//logger.debug("完成导入："+serno);
					
					logger.debug("\t\t导入行次"+serno);	
					num = con.sqlUpdate(insertSql+sb.toString());
					if(num<0){
						con.finalize();
						return;
					}
					logger.debug("\t\t导入完成"+num);
					k+=num;
				}
				sqlRst.close();
				
//				num = con.sqlUpdate(insertSql+sb.toString());
//				if(num<0){
//					con.finalize();
//					return;
//				}
				//System.err.println(insertSql+sb.toString());
				logger.debug("完成导入"+impTables[i]+"财务报表数据。"+k);
			}
			
			//如果年报不存在，则根据12月份数据生成年报
//			logger.debug("生成年报数据开始……");
//			sqlStr = " select CUSTID,REGIONCODE,substr(REPNO,1,4)||'0101','4'||substr(CALIBER,2),FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME from CI_REPDATA a where substr(repno,5,4)='1201' and substr(caliber,1,1)='1' and  not exists(select custid from ci_repdata where custid=a.custid and REGIONCODE=a.REGIONCODE and substr(repno,1,4)=substr(a.repno,1,4) and substr(CALIBER,2)=substr(a.CALIBER,2) and FREPCODE=a.FREPCODE and substr(repno,5,4)='0101' and substr(caliber,1,1)='4')";
//			//sqlStr = " select CUSTID,REGIONCODE,substr(REPNO,1,4)||'0101','4'||substr(CALIBER,2),FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME from CI_REPDATA a where coursecode='3002010' and substr(repno,5,4)='1201' and substr(caliber,1,1)='1' and  not exists(select custid from ci_repdata where custid=a.custid and REGIONCODE=a.REGIONCODE and substr(repno,1,4)=substr(a.repno,1,4) and substr(CALIBER,2)=substr(a.CALIBER,2) and FREPCODE=a.FREPCODE and substr(repno,5,4)='0101' and substr(caliber,1,1)='4' and coursecode='3002010')";
//			System.err.println(insertSql+sqlStr);
//			num = con.sqlUpdate(insertSql+sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}
//			insertSql = "";
//			logger.debug("生成年报数据完成:"+num);
//			
//			//根据ci_repdata中的数据生成CI_REPDATALIST、CI_REPDATALDX/CI_CUSBASINFO/CI_CUSCHARACTER
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
//			logger.debug("插入报表明细状态完成。"+num);
//			
//			logger.debug("清空报表基本信息");
//			con.sqlUpdate("delete from CI_REPDATALDX");
//			logger.debug("插入报表基本信息……");
//	       sqlStr = "insert into CI_REPDATALDX(CUSTID,REGIONCODE,REPNO,CALIBER,NANDO,TYPECODE           ,CURRENTTYPE,COUNTINGHOUSE,AUDITREPNO,AUDITRST,AUDITDATE,AUDITOR,MERGESCOPE,REPCUSTMGR,CREDIBILITY,SUBMITDATE,STATE)";
//	                 sqlStr += " select distinct CUSTID,REGIONCODE,REPNO,CALIBER,'0'  ,substr(CALIBER,4,1),'CNY'      ,''           ,''        ,''      ,''       ,''     ,''        ,''        ,''         ,'2009-11-25','1'   from CI_REPDATALIST";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}			
//			logger.debug("插入报表基本信息完成。"+num);	
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
//			logger.debug("生成客户信息。"+num);
//			
//			con.sqlUpdate("delete from CI_CUSCHARACTER where (CUSTID,REGIONCODE) in(select CUSTID,REGIONCODE from CI_CUSBASINFO where CUSTMGR='admin')");
//			sqlStr = "insert into CI_CUSCHARACTER(CUSTID,REGIONCODE,character1)";
//			sqlStr += " select CUSTID,REGIONCODE,'0' from CI_CUSBASINFO where (CUSTID,REGIONCODE) in(select CUSTID,REGIONCODE from CI_CUSBASINFO where CUSTMGR='admin')";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}	
//			logger.debug("生成客户信息完成。"+num);
			
			logger.debug("处理完成2。");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
			System.err.println("insertSql:"+insertSql);
			System.err.println("sqlStr:"+sqlStr);
		}finally{
			con.finalize();
		}
		
	}

	public void start3(){
		
//		String[] impTables = new String[]{"gh_fhva200433_2"};
//		String[] oldtables = new String[]{"ghold_02"    };
//		String[] typecodes = new String[]{"3"              };
//		String[] calibers2 = new String[]{"TA243120002"    };
//		String[] oldhycode = new String[]{"TA243120003"    };
//		String[] oldhyvalue =new String[]{"33"             };


//		String[] impTables = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200094_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200434_2","gh_fhva200435_2"};
//		String[] oldtables = new String[]{"ghold_01"       ,"ghold_01"       ,"ghold_01"       ,"ghold_01"       ,"ghold_01"       ,"ghold_02"       ,"ghold_02"       ,"ghold_02"       ,"ghold_02"};
//		String[] typecodes = new String[]{"1"              ,"1"              ,"3"              ,"4"              ,"5"              ,"1"              ,"3"              ,"4"              ,"5","1"};
//		String[] calibers2 =  new String[]{"TA209120002"   ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};
//		String[] oldhycode =  new String[]{"TA209120003"   ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"};
//		String[] oldhyvalue =  new String[]{"1"            ,"2"              ,"3"              ,"4"              ,"5"              ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};

//		String[] impTables = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200091_2","gh_fhva200094_2","gh_fhva200091_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200431_2","gh_fhva200434_2","gh_fhva200431_2","gh_fhva200435_2"};
//		String[] oldtables = new String[]{"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_01_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"    ,"ghold_02_01"};
//		String[] typecodes = new String[]{"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"              ,"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"};
//		String[] calibers2 = new String[]{"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};
//		String[] oldhycode = new String[]{"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"};
//		String[] oldhyvalue =new String[]{"11"             ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"              ,"11"            ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"};
		//import02表中对应关系
		String[] impTables = new String[]{"gh_fhva200091_2","gh_fhva200092_2","gh_fhva200093_2","gh_fhva200091_2","gh_fhva200094_2","gh_fhva200091_2","gh_fhva200095_2","gh_fhva200431_2","gh_fhva200431_2","gh_fhva200433_2","gh_fhva200431_2","gh_fhva200434_2","gh_fhva200431_2","gh_fhva200435_2"};
		//老表
		String[] oldtables = new String[]{"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_01_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"       ,"ghold_02_02"};
		//老表对应的行业类型
		String[] typecodes = new String[]{"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"              ,"1"              ,"1"              ,"3"              ,"1"              ,"4"              ,"1"              ,"5"};
		//老表对应的是否合并
		String[] calibers2 = new String[]{"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA209120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"    ,"TA243120002"};
		//老表对应的行业类型字段
		String[] oldhycode = new String[]{"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA209120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"    ,"TA243120003"};
//		老表对应的行业类型字段值
		String[] oldhyvalue =new String[]{"11"             ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"              ,"11"            ,"22"             ,"33"             ,"41"             ,"44"             ,"51"             ,"55"};

		String sqlStr = "",insertSql="";
		String fieldname="";
		String serno="",frepcode="";
		int num =0 ,k=0;
		StringBuffer sb = new StringBuffer();
		
		try{
						

			con = new DBManage();
			
			logger.debug("旧数据3处理开始……");
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
					sb.setLength(0);
//					if(k!=1) sb.append(" union ");
					//sb.append(" select rep."+custids[i]+",rep."+hhs[i]+",(case rep."+repnos[i]+" when '200600' then '200501' else rep."+repnos[i]+" end)||'01',(case rep."+repnos[i]+" when '200600' then '4' else '1' end)||(case substr(rep."+calibers[i]+",1,1) when '1' then '4' else '1' end)||'1'||'"+typecodes[i]+"'||'00','"+frepcode+"',fram.rowno,fram.colno,fram.subcode,'',"+(fieldname.equals("")?"0":fieldname)+",fram.showname from "+impTables[i]+" rep,FR_FREPORTFRAM fram where fram.frepcode='"+frepcode+"' and fram.serno="+serno);
					sb.append(" select rep.cisno,'0769',(case substr(rep.workdate,5,2) when '00' then to_char(to_number(substr(rep.workdate,1,4))-1)||'01' else rep.workdate end)||'01',(case substr(rep.workdate,5,2) when '00' then '4' else '1' end)||(case substr(rep."+calibers2[i]+",1,1) when '1' then '4' else '1' end)||'1'||'"+typecodes[i]+"'||'00','"+frepcode+"',fram.rowno,fram.colno,fram.subcode,'',nvl(("+(fieldname.equals("")?"0":fieldname)+"),''),fram.showname from "+oldtables[i]+" rep,FR_FREPORTFRAM fram where rep."+oldhycode[i]+"='"+oldhyvalue[i]+"' and fram.frepcode='"+frepcode+"' and fram.serno="+serno);
					//logger.debug("完成导入："+serno);
					
					logger.debug("\t\t导入行次"+serno);	
					num = con.sqlUpdate(insertSql+sb.toString());
					if(num<0){
						con.finalize();
						return;
					}
					logger.debug("\t\t导入完成"+num);
					k+=num;
				}
				sqlRst.close();
				
//				num = con.sqlUpdate(insertSql+sb.toString());
//				if(num<0){
//					con.finalize();
//					return;
//				}
				//System.err.println(insertSql+sb.toString());
				logger.debug("完成导入"+impTables[i]+"财务报表数据。"+k);
			}
			
			//如果年报不存在，则根据12月份数据生成年报
//			logger.debug("生成年报数据开始……");
//			sqlStr = " select CUSTID,REGIONCODE,substr(REPNO,1,4)||'0101','4'||substr(CALIBER,2),FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME from CI_REPDATA a where substr(repno,5,4)='1201' and substr(caliber,1,1)='1' and  not exists(select custid from ci_repdata where custid=a.custid and REGIONCODE=a.REGIONCODE and substr(repno,1,4)=substr(a.repno,1,4) and substr(CALIBER,2)=substr(a.CALIBER,2) and FREPCODE=a.FREPCODE and substr(repno,5,4)='0101' and substr(caliber,1,1)='4')";
//			//sqlStr = " select CUSTID,REGIONCODE,substr(REPNO,1,4)||'0101','4'||substr(CALIBER,2),FREPCODE,ROWNO,COLNO,COURSECODE,FSTCOLDATA,SNDCOLDATA,SHOWNAME from CI_REPDATA a where coursecode='3002010' and substr(repno,5,4)='1201' and substr(caliber,1,1)='1' and  not exists(select custid from ci_repdata where custid=a.custid and REGIONCODE=a.REGIONCODE and substr(repno,1,4)=substr(a.repno,1,4) and substr(CALIBER,2)=substr(a.CALIBER,2) and FREPCODE=a.FREPCODE and substr(repno,5,4)='0101' and substr(caliber,1,1)='4' and coursecode='3002010')";
//			num = con.sqlUpdate(insertSql+sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}
//			insertSql = "";
//			logger.debug("生成年报数据完成:"+num);
//			
//			//根据ci_repdata中的数据生成CI_REPDATALIST、CI_REPDATALDX/CI_CUSBASINFO/CI_CUSCHARACTER
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
//			logger.debug("插入报表明细状态完成。"+num);
//			
//			logger.debug("清空报表基本信息");
//			con.sqlUpdate("delete from CI_REPDATALDX");
//			logger.debug("插入报表基本信息……");
//	       sqlStr = "insert into CI_REPDATALDX(CUSTID,REGIONCODE,REPNO,CALIBER,NANDO,TYPECODE           ,CURRENTTYPE,COUNTINGHOUSE,AUDITREPNO,AUDITRST,AUDITDATE,AUDITOR,MERGESCOPE,REPCUSTMGR,CREDIBILITY,SUBMITDATE,STATE)";
//	                 sqlStr += " select distinct CUSTID,REGIONCODE,REPNO,CALIBER,'0'  ,substr(CALIBER,4,1),'CNY'      ,''           ,''        ,''      ,''       ,''     ,''        ,''        ,''         ,'2009-11-25','1'   from CI_REPDATALIST";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}			
//			logger.debug("插入报表基本信息完成。"+num);	
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
//			logger.debug("生成客户信息。"+num);
//			
//			con.sqlUpdate("delete from CI_CUSCHARACTER where (CUSTID,REGIONCODE) in(select CUSTID,REGIONCODE from CI_CUSBASINFO where CUSTMGR='admin')");
//			sqlStr = "insert into CI_CUSCHARACTER(CUSTID,REGIONCODE,character1)";
//			sqlStr += " select CUSTID,REGIONCODE,'0' from CI_CUSBASINFO where (CUSTID,REGIONCODE) in(select CUSTID,REGIONCODE from CI_CUSBASINFO where CUSTMGR='admin')";
//			num = con.sqlUpdate(sqlStr);
//			if(num<0){
//				con.finalize();
//				return;
//			}	
//			logger.debug("生成客户信息完成。"+num);
//			
			logger.debug("处理完成3。");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
			System.err.println("insertSql:"+insertSql);
			System.err.println("sqlStr:"+sqlStr);
		}finally{
			con.finalize();
		}
		
	}
	/*
	 * 通过FR_FREPORTFRAM定义的公式，进行表内表间校验
	 * tmp1_repdata存放不符合校验规则的报表
	 */
	private void checkExpress(){
		String sqlStr = "",calexpress="",str="";
		StringBuffer sb =new StringBuffer();
		StringBuffer kmsb =new StringBuffer();
		StringBuffer sumsb =new StringBuffer();
		List<String> list = null; 
		int num=0;
		String tabname = "tmp_repdata01";
		//String[] arcode = new String[]{"12020020","12020200","12020201","12020202","12020203","12020204","12020206","12020207","12020208","12020211","12020212","12020213","12020214","12020215","12020217","12020221","12020223","12020227","12020229","12020262","12020277","12020281","12020291","12020480","12020504","12020505","12020520","12020521","12020531","12020540","12020831","12020834","12020851","12020871","12020891","12020901","12020920","12030009","12032020","12032030","12032040","12032060","12032070","12032080","12032100","12032110","12032130","12032150","12032190","12032270","12032280","12032810","12032820","12032830","12032840","12032850","12032860","12032870","12032880","12040600","12040620","12040640","12040650","12040660","12040680","12040700","12040750","12040800","12040850","12040900","12050024","12052100","12052200","12052302","12052400","12052601","12052700","12052800","12052900","12060101","12060132","12060201","12060202","12060203","12060210","12060211","12070111","12070112","12070151","12070211","12070212","12070311","12070411","12070511","12070611","12070711","12070811","12080110","12080130","12080150","12080170","12080180","12080200","12080300","12080400","12080500","12080600","12080700","12080800","12092100","12092200","12092300","12092400","12092600","12092700","12092800","12092901","12100016","12101992","12102000","12102010","12102052","12102060","12102230","12102310","12102510","12102610","12102710","12102810","12102910","12110120","12110140","12110160","12110180","12110200","12110220","12110240","12110260","12110280","12110320","16010001","16010008","16020001","16020002","16020003","16020004","16020005","16020006","16020007","16020008","16020011","16020013","16020014","16020016","16020017","16020018","16020019","16020020","16020021","16020022","16020023","16020031","16020200","16029999","16030011","16030015","16030016","16030017","16030021","16030025","16030031","16030041","16030051","16030061","16030071","16030081","16030091","16030101","16030102","16030110","16030120","16030658","16040009","16040101","16040102","16040103","16040104","16040105","16040106","16040107","16040108","16040110","16050031","16050051","16050061","16050118","16050201","16050220","16050306","16060202","16060203","16060204","16060205","16060208","16060209","16060210","16060212","16060213","16060214","16060215","16060217","16060218","16060219","16060221","16060225","16060226","16070001","16070002","16070003","16070004","16070005","16070006","16070007","16070008","16070009","16070010","16070011","16070012","16070013","16070014","16070015","16070017","16070018","16070020","16070023","16070027","16070028","16070029","16070030","16080001","16080005","16080014","16080015","16080016","16080020","16080026","16080030","16080033","16080041","16080044","16080045","16080047","16080048","16080051","16090013","16090015","16090022","16090023","16090024","16090025","16090026","16090027","16090028","16090029","16090100","16090200","16090300","16090400","16100101","16100104","16100111","16100201","16100202","16100203","16100204","16100205","16100206","16100207","16100208","16100210","16100211","16100212","16101400","16110020","16110021","16110022","16110023","16110024","16110025","16110026","16110027","16110028","16110029","16110032","16120100","16120200","16120300","16120400","16120500","16120600","16120700","16120800","16120900","16121000","16121200","16121300","16121400","16121600","16121700","16121800","16121900","16122000","16122100","16122200","16122300","16122400","16122500","16122600","16122700","16130001","16130002","16130003","16130004","16130005","16130006","16130007","16130008","16130009","16130010","16130012","16130013","16130014","16130211","16130212","16130515","16130516","16140100","16140200","16140300","16140500","16140600","16140700","16140800","16140900","16141000","16141100","16141200","16141300","16150001","16150003","16150004","16150005","16150006","16150008","16150009","16150011","16150012","16160200","16160201","16160202","16160203","16160204","16160206","16160208","16160310","16160405","16170001","16170002","16170003","16170004","16170006","16170007","16170008","16170009","16170010","16170011","16170012","16170013","16170014","16170015","16170016","16170017","16170018","16170019","16170020","16170021","16170022"};
		
		
		try{
			logger.debug("报表数据校验开始……");
			con = new DBManage();
//			con.sqlUpdate("delete from tmp1_repdata");
//			sqlStr = "update CI_REPDATALDX set state='1'";
//			totalnum = con.sqlUpdate(sqlStr);
//			if(totalnum<0){
//				con.finalize();
//				return;
//			}
			String tmpstr = "insert into tmp1_repdata(CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,COURSECODE)";
//			for(int m=0;m<arcode.length;m++)
//			{
			//表内勾稽关系校验
			sqlStr = "select * from FR_FREPORTFRAM where frepcode in(select distinct frepcode from import02) and length(trim(calexpress))>0 order by FREPCODE,SERNO";
			//sqlStr = "select * from FR_FREPORTFRAM where frepcode in(select distinct frepcode from import02) and length(trim(calexpress))>0 and frepcode in('5001','5002') order by FREPCODE,SERNO";
			ResultSet sqlRst = con.sqlQuery(sqlStr);
			String subcode = "";
			while(sqlRst.next()){
				
				subcode = sqlRst.getString("subcode");
				calexpress = sqlRst.getString("calexpress");
				
				list = parseCalExpress(calexpress);
				logger.debug("校验：["+subcode+"]"+sqlRst.getString("showname")+"……");
				
				sb.setLength(0);
				kmsb.setLength(0);
				sumsb.setLength(0);				
				sb.append("select distinct custid,REGIONCODE,repno,caliber,frepcode,'"+subcode+"' COURSECODE  from (select custid,REGIONCODE,repno,caliber,frepcode");
				sb.append(", case when coursecode='"+subcode+"' then sndcoldata else 0 end as a0");
				sumsb.append(" sum(a0) - (");
				kmsb.append(",'"+subcode+"'");
				for(int i=1;i<=list.size();i++){
					str = list.get(i-1);

					if(str.substring(0, 1).equals("n")){
						sb.append(", case when coursecode='"+str.substring(1)+"' then round(coalesce(sndcoldata,0),2) else 0 end as a"+i);
						kmsb.append(",'"+str.substring(1)+"'");
						sumsb.append(" sum(a"+i+") ");
					}else{
						sumsb.append(str.substring(1));
					} 					
				}
				sumsb.append(")");
				sb.append(" from "+tabname+" where coursecode in ("+kmsb.toString().substring(1)+")) tmp");
				sb.append(" group by custid,REGIONCODE,repno,caliber,frepcode having abs(round((").append(sumsb).append("),2))>0");

				num = con.sqlUpdate(tmpstr+sb.toString());
				if(num<0){
					con.finalize();
					return;
				}else if(num>0){
					logger.debug("num："+num);
				}

			}

			//sqlRst2.close();
			sqlRst.close();
//			}
			//校验资产=负债+所有者权益
			logger.debug("资产=负债+所有者权益处理……");
			sb.setLength(0);
			sb.append("select distinct custid,REGIONCODE,repno,caliber,frepcode,'1001059' COURSECODE  from ( select custid,REGIONCODE,repno,caliber,frepcode");
			sb.append(", case when coursecode='1001113' then round(coalesce(sndcoldata,0),2) else 0 end as a1");
			sb.append(", case when coursecode='1001059' then round(coalesce(sndcoldata,0),2) else 0 end as a2");
			sb.append(" from "+tabname+" where coursecode in ('1001113','1001059')");
			sb.append(") X group by custid,REGIONCODE,repno,caliber,frepcode having abs(round((sum(X.a1)- sum(X.a2)),2))>0");
			num = con.sqlUpdate(tmpstr+sb.toString());
			if(num<0){
				con.finalize();
				return;
			}else if(num>0){
				logger.debug("num："+num);
			}
			
			//校验资产>0
			logger.debug("资产>0处理……");
			sb.setLength(0);
			sb.append("select CUSTID,REGIONCODE,REPNO,CALIBER,FREPCODE,COURSECODE from "+tabname+" where COURSECODE = '1001059' and (sndcoldata=0 or sndcoldata is null)");
			num = con.sqlUpdate(tmpstr+sb.toString());
			if(num<0){
				con.finalize();
				return;
			}else if(num>0){
				logger.debug("num："+num);
			}
			
//			num = con.sqlUpdate("update ci_repdataldx set state='1'");
//			if(num<0){
//				con.finalize();
//				return;
//			}else if(num>0){
//				logger.debug("num："+num);
//			}
//			//把不符合校验的ci_repdataldx状态设置为0
//			num = con.sqlUpdate("update ci_repdataldx set state='0' where (CUSTID,REGIONCODE,REPNO,CALIBER) in (select CUSTID,REGIONCODE,REPNO,CALIBER from tmp1_repdata)");
//			if(num<0){
//				con.finalize();
//				return;
//			}else if(num>0){
//				logger.debug("num："+num);
//			}
			
			logger.debug("checkExpress报表数据校验完成");
			
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
		calExpress = calExpress.replace(" ","");
		calExpress = calExpress.replace("[","");
		calExpress = calExpress.replace("]","");
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
	/*
	 * 校验是否同一期报表是否同时存在资产负债、损益、现金流量表
	 * 一般企业必须同时存在三张资产负债、损益、现金流量表
	 * 其他类型必须存在资产负债、损益
	 * tmp_repdataldx存放不符合校验规则的数据
	 */
	@SuppressWarnings("unused")
	private void checkExpress3(){
		String sqlStr = "";
		String sqlinsert = "insert into tmp_repdataldx(CUSTID,REGIONCODE,REPNO,CALIBER)";
		int num = 0;
		try{
			logger.debug("报表数据校验开始3……");
			con = new DBManage();
			con.sqlUpdate("delete from tmp_repdataldx");
			
			logger.debug("处理报表种类……");
			sqlStr = " select CUSTID,REGIONCODE,REPNO,CALIBER from (select a.CUSTID,a.REGIONCODE,a.REPNO,a.CALIBER,a.TYPECODE,b.FREPCODE from CI_REPDATALDX a,CI_REPDATALIST b where a.CUSTID=b.CUSTID and a.REGIONCODE=b.REGIONCODE and a.REPNO=b.REPNO and a.CALIBER=b.CALIBER and b.FREPCODE in('1001','1002','1003')) tmp group by CUSTID,REGIONCODE,REPNO,CALIBER having count(*)<3";
			num = con.sqlUpdate(sqlinsert+sqlStr);
			if(num<0){
				con.finalize();
				return;
			}else if(num>0){
				logger.debug("num："+num);
			}
			
			sqlStr = " select CUSTID,REGIONCODE,REPNO,CALIBER from (select a.CUSTID,a.REGIONCODE,a.REPNO,a.CALIBER,a.TYPECODE,b.FREPCODE from CI_REPDATALDX a,CI_REPDATALIST b where a.CUSTID=b.CUSTID and a.REGIONCODE=b.REGIONCODE and a.REPNO=b.REPNO and a.CALIBER=b.CALIBER and b.FREPCODE in('3001','3002')) tmp group by CUSTID,REGIONCODE,REPNO,CALIBER having count(*)<2";
			num = con.sqlUpdate(sqlinsert+sqlStr);
			if(num<0){
				con.finalize();
				return;
			}else if(num>0){
				logger.debug("num："+num);
			}
			
			sqlStr = " select CUSTID,REGIONCODE,REPNO,CALIBER from (select a.CUSTID,a.REGIONCODE,a.REPNO,a.CALIBER,a.TYPECODE,b.FREPCODE from CI_REPDATALDX a,CI_REPDATALIST b where a.CUSTID=b.CUSTID and a.REGIONCODE=b.REGIONCODE and a.REPNO=b.REPNO and a.CALIBER=b.CALIBER and b.FREPCODE in('4001','4002')) tmp group by CUSTID,REGIONCODE,REPNO,CALIBER having count(*)<2";			
			num = con.sqlUpdate(sqlinsert+sqlStr);
			if(num<0){
				con.finalize();
				return;
			}else if(num>0){
				logger.debug("num："+num);
			}
			
			sqlStr = " select CUSTID,REGIONCODE,REPNO,CALIBER from (select a.CUSTID,a.REGIONCODE,a.REPNO,a.CALIBER,a.TYPECODE,b.FREPCODE from CI_REPDATALDX a,CI_REPDATALIST b where a.CUSTID=b.CUSTID and a.REGIONCODE=b.REGIONCODE and a.REPNO=b.REPNO and a.CALIBER=b.CALIBER and b.FREPCODE in('5001','5002')) tmp group by CUSTID,REGIONCODE,REPNO,CALIBER having count(*)<2";
			num = con.sqlUpdate(sqlinsert+sqlStr);
			if(num<0){
				con.finalize();
				return;
			}else if(num>0){
				logger.debug("num："+num);
			}
			
			//把不符合校验官则的ci_repdataldx状态设置为0
			num =  con.sqlUpdate("update ci_repdataldx set state='0' where (CUSTID,REGIONCODE,REPNO,CALIBER) in(select CUSTID,REGIONCODE,REPNO,CALIBER from tmp_repdataldx)");
			if(num<0){
				con.finalize();
				return;
			}else if(num>0){
				logger.debug("num："+num);
			}
			
			logger.debug("checkExpress3完成");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.toString());
		}finally{			
			con.finalize();
		}
	}
	
	public void aaaa(){
		
	}
	
}
