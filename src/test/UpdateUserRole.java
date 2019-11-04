package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 初始化用户角色
 * @author gengjp
 *
 */
public class UpdateUserRole {

	
	public static void updateUserRole(){
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		DBManage manage = new DBManage();
		manage.setAutoCommit(false);
		ResultSet rs = null;
		
		try {
			String str="";
			String wherestr="";
			String message="";
			String sql ="select account, LEVEL_CODE from sys_user a ,SYS_DEPARTMENT b where a.deptcode=b.deptcode and a.flag=1 ";
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
					sql += " and a.account='"+wherestr+"' ";
					break;
				}else if("3".equals(str)){
					System.out.print("请输入分行/支行编号: ");
					wherestr = stdin.readLine();
					sql += " and a.deptcode='"+wherestr+"' ";
					break;
				}else {
					System.out.println("输入错误！");	
				}
			}
			rs = manage.sqlQuery(sql);
			String sql2 = "delete from SYS_USER_ROLE ";
			if("1".equals(str)){
				
			}else if("2".equals(str)){
				sql2 += " where account='"+wherestr+"'";
			}else if("3".equals(str)){
				sql2 += " where account in (select account from sys_user where deptcode='"+wherestr+"')";
			}
			manage.sqlUpdate(sql2);
			while (rs.next()) {
				String account = rs.getString(1).trim();
				int level = rs.getInt(2);
				if(level ==0){//总行
					manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('"+account+"',9816)");
					message = "评级管理岗";
				}else if(level ==1){//分行
					manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('"+account+"',9815)");
					message = "评级审核岗";
				}else{
					manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('"+account+"',9814)");
					message = "评级发起岗";
				}
				System.out.println("======更新"+account+"的角色为"+ message);
			}
			manage.commit();
			manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('y00000a8',9817)");
			manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('y00000a7',9817)");
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
		updateUserRole();

	}

}
