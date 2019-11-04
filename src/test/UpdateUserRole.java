package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ��ʼ���û���ɫ
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
				System.out.println("��ѡ�񡭡�");
				System.out.println("0���˳���");
				System.out.println("1������ȫ���û���");
				System.out.println("2������ָ���û���");
				System.out.println("3������ָ�����С�");
				str = stdin.readLine();
				
				if("0".equals(str)){
					System.out.println("�����˳���");
					return;
				}else if("1".equals(str)){
					break;
				}else if("2".equals(str)){
					System.out.print("���������ԱId: ");
					wherestr = stdin.readLine();
					sql += " and a.account='"+wherestr+"' ";
					break;
				}else if("3".equals(str)){
					System.out.print("���������/֧�б��: ");
					wherestr = stdin.readLine();
					sql += " and a.deptcode='"+wherestr+"' ";
					break;
				}else {
					System.out.println("�������");	
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
				if(level ==0){//����
					manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('"+account+"',9816)");
					message = "���������";
				}else if(level ==1){//����
					manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('"+account+"',9815)");
					message = "������˸�";
				}else{
					manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('"+account+"',9814)");
					message = "���������";
				}
				System.out.println("======����"+account+"�Ľ�ɫΪ"+ message);
			}
			manage.commit();
			manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('y00000a8',9817)");
			manage.sqlUpdate("insert into SYS_USER_ROLE(account,ROLE_ID) values ('y00000a7',9817)");
			System.out.println("======������ϣ�======");
			}catch (Exception e) {
				manage.rollback();
				manage.setAutoCommit(true);
				System.err.println("======����"+ e.toString() +"�쳣��======");
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
