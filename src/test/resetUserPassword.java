package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import test.DBManage;
import test.MD5;

/**
 * ��ʼ������
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
				sql += " where account='"+wherestr+"' ";
				break;
			}else if("3".equals(str)){
				System.out.print("���������/֧�б��: ");
				wherestr = stdin.readLine();
				sql += " where deptcode='"+wherestr+"' ";
				break;
			}else {
				System.out.println("�������");	
			}
		}
		rs = manage.sqlQuery(sql);
		while (rs.next()) {
			String account = rs.getString(1).trim();
			String psw = md5.calcMD5(rs.getString(1).trim()+"123");
			manage.sqlUpdate("update sys_user set psw = '"+ psw + "' ,flag=1 where account='" + account + "'");
			System.out.println("======����"+account+"������Ϊ"+psw+"�ɹ���======");
		}
		manage.commit();
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
		resetUserPsw();
	}
}
