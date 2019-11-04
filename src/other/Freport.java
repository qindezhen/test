package other;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Clob;
import org.apache.log4j.xml.DOMConfigurator;

import other.FAReportCate;
import other.FAReportFram;
import other.FinCourseDic;
import test.DBManage;

public class Freport {
	
	
	public static void main(String[] args) {
//		runcodeFR_FREPORTCATE();
//		runcodeFR_FREPORTFRAM();		
//		
//		runFR_FREPORTFRAM();		
//		runFR_FREPORTCATE();
		
		runcodeIDX_INDEXFORMULE();
//		test();
//		todata();
	}
	
	public static void test(){
		String sqlStr = "";
		DBManage manage = new DBManage();
		sqlStr = "select distinct indexcode,tradetype from idx_indexformule";
		java.sql.ResultSet rs = manage.sqlQuery(sqlStr);
		try {
			while (rs.next()) {
				sqlStr = "insert into tmp select * from idx_indexformule where indexcode='"+rs.getString(1)+"' and tradetype='"+rs.getString(2)+"' and rownum=1";
				manage.sqlUpdate(sqlStr);
			}
			manage.commit();
			System.out.println("======更新成功！======");
		} catch (Exception e) {
			manage.rollback();
			System.out.println("======发生异常！======");
			e.printStackTrace();
		}finally{
			manage.finalize();
		}
	}
	public static void runcodeFR_FREPORTCATE(){
		String sqlStr = "",tmp="",tmp2;
		DBManage manage = new DBManage();
		sqlStr = "select VEREXPRESS,FREPCODE from FR_FREPORTCATE where length(trim(VEREXPRESS))>0";
		java.sql.ResultSet rs = manage.sqlQuery(sqlStr);
		try {
			while (rs.next()) {
				tmp = rs.getString(1);
				tmp2 = rs.getString(2);
				tmp = tmp.replace("[","");
				tmp = tmp.replace("]","");
				tmp=tmp.replaceAll("([\\d]{7})", "\\[$1\\]");
				manage.sqlUpdate("update FR_FREPORTCATE set VEREXPRESS='"+tmp+"',VEREXPRESSname='"+tmp+"' where FREPCODE='" + tmp2+"'");
			}
			manage.commit();
			System.out.println("======更新成功！======");
		} catch (Exception e) {
			manage.rollback();
			System.out.println("======发生异常！======");
			e.printStackTrace();
		}finally{
			manage.finalize();
		}
	}

	public static void runcodeFR_FREPORTFRAM(){
		String sqlStr = "",tmp="",tmp2,tmp3;
		DBManage manage = new DBManage();
		sqlStr = "select CALEXPRESS,FREPCODE,SERNO from FR_FREPORTFRAM where substr(frepcode,1,1) in('3','4','5') and length(trim(CALEXPRESS))>0";
		java.sql.ResultSet rs = manage.sqlQuery(sqlStr);
		try {
			while (rs.next()) {
				tmp = rs.getString(1);
				tmp2 = rs.getString(2);
				tmp3 = rs.getString(3);
				tmp = tmp.replace("[","");
				tmp = tmp.replace("]","");
				tmp=tmp.replaceAll("([\\d]{7})", "\\[$1\\]");
				manage.sqlUpdate("update FR_FREPORTFRAM set CALEXPRESS='"+tmp+"',CALEXPRESSname='"+tmp+"' where FREPCODE='" + tmp2 + "' and SERNO="+tmp3);
			}
			manage.commit();
			System.out.println("======更新成功！======");
		} catch (Exception e) {
			manage.rollback();
			System.out.println("======发生异常！======");
			e.printStackTrace();
		}finally{
			manage.finalize();
		}
	}
	
	public static void runcodeIDX_INDEXFORMULE(){

		DOMConfigurator.configure("E:/wkp/dgshnp/test/log4j2.xml");
		DBManage manage = new DBManage();
		Connection conn = manage.getCon();
		manage.setAutoCommit(false);		
		
		String sqlStr = "",tmp="";
		Clob foclob = null;
		String indexcode = "",tradetype="";
//		sqlStr = "select * from IDX_INDEXFORMULE where indexcode in(select indexcode from IDX_INDEXDIC where algtype='010503' and indextype in('010210','010211')) and formula is not null and algexplain is not null order by INDEXCODE,TRADETYPE";
		sqlStr = "select * from IDX_INDEXFORMULE where formula is not null and algexplain is not null and indexcode in(select indexcode from idx_indexdic where indextype='010210') order by INDEXCODE,TRADETYPE";
		java.sql.ResultSet sqlRst = manage.sqlQuery(sqlStr);
		try {
			while (sqlRst.next()) {
				indexcode = sqlRst.getString("INDEXCODE");
				tradetype=sqlRst.getString("TRADETYPE");
				
				foclob = (Clob)sqlRst.getClob("formula");
				if(foclob == null) continue;
				tmp = foclob.getSubString((long)1,(int)foclob.length());
				tmp = tmp.replace("[-11.", "[-13.");
				tmp = tmp.replace("[-21.", "[-23.");
				tmp = tmp.replace("[-31.", "[-33.");

				
//				foclob2 = (Clob)sqlRst.getClob("algexplain");
//				tmp2 = foclob2.getSubString((long)1,(int)foclob2.length());			
//				tmp2 = tmp2.replace("上一期.", "上一年年末.");
//				tmp2 = tmp2.replace("上两期.", "上两年年末.");
//				tmp2 = tmp2.replace("上三期.", "上三年年末.");



				sqlStr = "update IDX_INDEXFORMULE set formula=? where INDEXCODE='"+indexcode+"' and TRADETYPE='"+tradetype+"'";
//				sqlStr = "update IDX_INDEXFORMULE set algexplain=? where INDEXCODE='"+indexcode+"' and TRADETYPE='"+tradetype+"'";

				PreparedStatement psm = conn.prepareStatement(sqlStr);
				byte[] formula=tmp.getBytes();
				InputStream inputformula = new ByteArrayInputStream(formula);
				psm.setBinaryStream(1, inputformula,(int)formula.length);
				psm.executeUpdate();
				        
			}
			manage.commit();
			sqlRst.close();
			
			System.out.println("======更新成功！======");
		} catch (Exception e) {
			manage.rollback();
			System.out.println("======发生异常！======");
			e.printStackTrace();
		}finally{
			manage.finalize();
		}	
	}
	//FR_FREPORTFRAM 报表结构
	public static void runFR_FREPORTFRAM(){
		DBManage dbconn = new DBManage();// 定义数据库连接实例 
		List<FAReportCate> list = new ArrayList<FAReportCate>();
		ResultSet rs = null;
		List<FinCourseDic> list2 =new ArrayList<FinCourseDic>();
		List<FAReportFram> list3 =new ArrayList<FAReportFram>();
		String sql1="";
		String sql2="";
		String sql4="";
		String sql ="select frepcode,trim(typecode) as  typecode from FR_FREPORTCATE";
		
		try {
			
			rs = dbconn.sqlQuery(sql);
			while (rs.next()) {
				FAReportCate cate = new FAReportCate();
				cate.setFrepcode(rs.getString("frepcode"));
				cate.setTypecode(rs.getString("typecode"));
				list.add(cate);
			}
			if(rs != null){
				rs.close();
			}
			for (int i = 0; i < list.size(); i++) {
				FAReportCate cate  = list.get(i);
				sql1= "select frepcode,serno,calexpressname from FR_FREPORTFRAM where frepcode='"+cate.getFrepcode()+"' order by serno asc";
				
				sql2="select coursecode,coursename from IDX_FINCOURSEDIC where trim(tradetype)='"+cate.getTypecode()+"' order by coursecode";
				
				
				rs = dbconn.sqlQuery(sql1);
				
				while (rs.next()) {
					FAReportFram fram = new FAReportFram();
					fram.setFrepcode(rs.getString("frepcode"));
					fram.setSerno(rs.getInt("serno"));
					fram.setCalexpressname(rs.getString("calexpressname"));
					list3.add(fram);
				}
				if(rs != null){
					rs.close();
				}
				
				rs =  dbconn.sqlQuery(sql2);
				while (rs.next()) {
					FinCourseDic courseDic = new FinCourseDic();
					courseDic.setCoursecode(rs.getString("coursecode"));
					courseDic.setCoursename(rs.getString("coursename"));
					list2.add(courseDic);
				}
				if(rs != null){
					rs.close();
				}
				for (int k = 0; k < list3.size(); k++) {
					FAReportFram fram = list3.get(k);
					String label = fram.getCalexpressname();
					if(label!= null ){
						for (int f = 0; f < list2.size(); f++) {
							FinCourseDic dic = list2.get(f);
							label = label.replaceAll("\\["+dic.getCoursecode()+"\\]", dic.getCoursename());
						}
						
						sql4="update FR_FREPORTFRAM set calexpressname='"+label+"' where frepcode='"+fram.getFrepcode()+"' and serno="+fram.getSerno();
						
						int count = dbconn.sqlUpdate(sql4);
						if(count<0){
							dbconn.rollback();
						}else{
							dbconn.commit();
						}
					}else{
						continue;
					}
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			dbconn.close();
		}finally{
			dbconn.finalize();
		}
		System.out.println("处理完成");
	}
	
	// FR_FREPORTCATE 财务报表种类
	public static void runFR_FREPORTCATE(){
		DBManage dbconn = new DBManage();// 定义数据库连接实例 
		ResultSet rs = null;
		List<FAReportCate> list1 =new ArrayList<FAReportCate>();
		List<FinCourseDic> list2 =new ArrayList<FinCourseDic>();
		String sql2="";
		String sql3="";
		String sql ="select frepcode,frepname,trim(typecode) as  typecode ,verexpressname from FR_FREPORTCATE";
		
		try {
			
			rs = dbconn.sqlQuery(sql);
			while (rs.next()) {
				FAReportCate cate = new FAReportCate();
				cate.setFrepcode(rs.getString("frepcode"));
				cate.setFrepname(rs.getString("frepname"));
				cate.setTypecode(rs.getString("typecode"));
				cate.setVerexpressname(rs.getString("verexpressname"));
				list1.add(cate);
			}
			if(rs != null){
				rs.close();
			}
			for (int i = 0; i < list1.size(); i++) {
				FAReportCate cate = list1.get(i);
				
				sql2="select coursecode,coursename from IDX_FINCOURSEDIC where trim(tradetype)='"+cate.getTypecode()+"' order by coursecode";
				
				rs =  dbconn.sqlQuery(sql2);
				while (rs.next()) {
					FinCourseDic courseDic = new FinCourseDic();
					courseDic.setCoursecode(rs.getString("coursecode"));
					courseDic.setCoursename(rs.getString("coursename"));
					list2.add(courseDic);
				}
				if(rs != null){
					rs.close();
				}
				if(cate.getVerexpressname()!=null ){
					String label = cate.getVerexpressname();
					for (int k = 0; k < list2.size(); k++) {
						FinCourseDic dic = list2.get(k);
						label = label.replaceAll("\\["+dic.getCoursecode()+"\\]", "本期"+"."+cate.getFrepname()+"."+dic.getCoursename());
					}
					
					sql3="update FR_FREPORTCATE set verexpressname='"+label+"' where frepcode='"+cate.getFrepcode()+"' ";
					
					int count = dbconn.sqlUpdate(sql3);
					if(count<0){
						dbconn.rollback();
					}else{
						dbconn.commit();
					}
				}else{
					continue;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			dbconn.close();
		}finally{
			dbconn.finalize();
		}
		System.out.println("处理完成");
	}
	
//	public static void todata(){
//
//		DOMConfigurator.configure("E:/wkp/dgshnp/test/log4j2.xml");
//		DBManage manage = new DBManage();
////		manage.setAutoCommit(false);		
//		
//		String sqlStr = "",tmp="",tmp2;
//		List list;
//		CLOB foclob = null,foclob2 = null;
//		int num = 0;
//		java.sql.ResultSet sqlRst2 = null;
//		String indexcode = "",tradetype="";
////		sqlStr = "select * from IDX_INDEXFORMULE where indexcode in(select indexcode from IDX_INDEXDIC where algtype='010503' and indextype in('010210','010211')) and formula is not null and algexplain is not null order by INDEXCODE,TRADETYPE";
//
//		for(int j=5;j>0;j--){
////		sqlStr = "select  from idx_indexdic where "
//		sqlStr = "select * from IDX_INDEXFORMULE where indexcode in(select indexcode from idx_indexdic where state='6' and indexlevel="+j+")  and formula is not null and tradetype='1'";
//		java.sql.ResultSet sqlRst = manage.sqlQuery(sqlStr);
//		try {logger.debug("begin……………………");
//			while (sqlRst.next()) {
//				indexcode = sqlRst.getString("INDEXCODE");
//				tradetype=sqlRst.getString("TRADETYPE");
//				
//
//				foclob2 = (CLOB)sqlRst.getClob("formula");
//				tmp2 = foclob2.getSubString((long)1,(int)foclob2.length());
//				
//				list = parser(tmp2,"B\\[[^]]+\\]");
//				for(int i=0;i<list.size();i++){
//					tmp = (String)list.get(i);
//					sqlStr = "update idx_indexdic set state='6' where indexcode='"+tmp.substring(tmp.indexOf(".")+1, tmp.length()-1)+"'";
//					System.err.println(sqlStr);
//					manage.sqlUpdate(sqlStr);
//				}
//				
//				        
//			}
//			logger.debug("end…………………");
////			manage.commit();
//			sqlRst.close();
//			
//			System.out.println("======更新成功！======");
//		} catch (Exception e) {
////			manage.rollback();
//			System.out.println("======发生异常！======");
//			e.printStackTrace();
//		}finally{
////			
//		}	
//		}
//		manage.finalize();
//	}
	
	public static List<String> parser(String str,String regexp) throws Exception{
		List<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile(regexp).matcher(str);
		String tmp = "";
		while(m.find()){
			tmp = m.group();
			if(list.contains(tmp)) continue;
			
			list.add(tmp);
		}
		return list;
	}
}
