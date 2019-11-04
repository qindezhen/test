package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import test.DBManage;
import test.MD5;

/**
 * 初始化密码
 * @author gengjp
 *
 */
public class resetUserPassword {
	
	public static void resetUserPsw(){
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		MD5 md5 = new MD5();
		DBManage manage = new DBManage();
		manage.setAutoCommit(false);
		ResultSet rs = null;
		
		try {
		String str="";
		String wherestr="";
		
		String sql ="select account from sys_user";
		while(true){
			System.out.println("请选择……");
			System.out.println("0、退出。");
			System.out.println("1、更新全部用户。");
			System.out.println("2、更新指定用户。");
			System.out.println("3、更新指定分行。");
			str = stdin.readLine();
			
			if("0".equals(str)){
				System.out.println("程序退出！");
				return;
			}else if("1".equals(str)){
				break;
			}else if("2".equals(str)){
				System.out.print("请输入操作员Id: ");
				wherestr = stdin.readLine();
				sql += " where account='"+wherestr+"' ";
				break;
			}else if("3".equals(str)){
				System.out.print("请输入分行/支行编号: ");
				wherestr = stdin.readLine();
				sql += " where deptcode='"+wherestr+"' ";
				break;
			}else {
				System.out.println("输入错误！");	
			}
		}
		rs = manage.sqlQuery(sql);
		while (rs.next()) {
			String account = rs.getString(1).trim();
			String psw = md5.calcMD5(rs.getString(1).trim()+"123");
			manage.sqlUpdate("update sys_user set psw = '"+ psw + "' ,flag=1 where account='" + account + "'");
			System.out.println("======更新"+account+"的密码为"+psw+"成功！======");
		}
		manage.commit();
		System.out.println("======更新完毕！======");
		}catch (Exception e) {
			manage.rollback();
			manage.setAutoCommit(true);
			System.err.println("======发生"+ e.toString() +"异常！======");
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			manage.finalize();
		}
	}
	
	public static void main(String[] args) {
		resetUserPsw();
	}
}
