package test;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.sql.DataSource;
//import com.shunde.common.util.PageListData;
;
/**
 * 
 * 
 * ���ݿ������
 * 
 * 
 */
public class DBManage
extends java.lang.Object {
	Connection db2Con = null;
	Statement query = null;
	Statement query2 = null;
	Statement update = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	String ErrMsg = null;
	String SqlState = null;
	CallableStatement st = null;
	// ��ҳ��ʾ
	// sucess connction marker
	public boolean marker = false;
	private String driver = null;
	private String url = null;
	private String user = null;
	private String password = null;
	static int conFlag = 1;// 0-�����ӳ� 1-�����ļ�
	//
	static public void setConFlag(int flag) {
		conFlag = flag;
	}
	static public int getConFlag() {
		return conFlag;
	}
	/**
	 * 
	 * 
	 * ��ָ���ļ�����
	 * 
	 * 
	 * @param name
	 *            ����Ϊ�ļ���
	 * 
	 * 
	 * @return �ɹ����� true
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public boolean loadConfig() {
		boolean rc = false;
		java.util.Properties prop = null;
		try {
			prop = new java.util.Properties();
			prop.load(getClass().getResourceAsStream("dbsource.conf"));
			driver = prop.getProperty("driver").trim();
			url = prop.getProperty("url").trim();
			user = prop.getProperty("user").trim();
			password = prop.getProperty("password").trim();
			rc = true;
		}
		catch (IOException ex) {
			rc = false;
			ex.printStackTrace();
		}
		finally {
			return rc;
		}
	}
	public DBManage() {
		try {
			db2Con = null;
			if (conFlag == 1) {
				loadConfig();
				Class.forName(driver);
				db2Con = DriverManager.getConnection(url, user, password);
			} else {
				InitialContext ctx = new InitialContext();
				DataSource ds = (DataSource) ctx
						.lookup("java:comp/env/jdbc/arms");
				db2Con = ds.getConnection();
			}
			db2Con.setAutoCommit(true); // �����������ݿ�Ϊ�Զ��ύ
		}
		catch (Exception e) {
			db2Con = null;
			System.out.println("{��ȡ����:}==== >ʧ��<==== ");
			e.printStackTrace();
			ErrMsg = e.getMessage();
		}
		try {
			query = db2Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);
			query2 = db2Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);
			update = db2Con.createStatement();
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
		}
		marker = true;
	}
	/**
	 * 
	 * 
	 * ��ʱ
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public Connection getCon() {
		return this.db2Con;
	}
	/**
	 * 
	 * 
	 * ��ȡ���ݿ��Ƿ��Զ��ύ
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean getAutoCommit() {
		boolean bool = false;
		try {
			bool = db2Con.getAutoCommit();
			return bool;
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
		}
		return bool;
	}
	/**
	 * 
	 * 
	 * �������ݿ��Ƿ��Զ��ύ
	 * 
	 *  @ boolean autoCommit
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	public void setAutoCommit(boolean autoCommit) {
		try {
			db2Con.setAutoCommit(autoCommit);
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
		}
	}
	/**
	 * 
	 * 
	 * ���ݿ�����ύ
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	public void commit() {
		try {
			db2Con.commit();
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
		}
	}
	/**
	 * 
	 * 
	 * ���ݿ����ع�
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	public void rollback() {
		try {
			db2Con.rollback();
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
		}
	}
	/**
	 * 
	 * 
	 * ִ�����ݿ��ѯ���ܣ����ز�ѯ���
	 * 
	 *  @ param ��׼��ѯsql���
	 * 
	 *  @ return ��ѯ�����ResultSet
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public ResultSet sqlQuery(String sql) {
		if (sql == null) {
			sql = "";
		}
		sql = sql.trim();
		if (sql.equals("")) {
			return null;
		}
		try {
			if (rs != null)
				rs.close();
			if (query != null)
				query.close();
			query = db2Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = query.executeQuery(sql);
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
			rs = null;
		}
		finally {
			return rs;
		}
	}
	/**
	 * 
	 * 
	 * ִ�����ݿ��ѯ����(�õڶ��������ִ�У���sqlQuery����ͻ)�����ز�ѯ���
	 * 
	 *  @ param ��׼��ѯsql���
	 * 
	 *  @ return ��ѯ�����ResultSet
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public ResultSet sqlQuery2(String sql) {
		if (sql == null) {
			sql = "";
		}
		sql = sql.trim();
		if (sql.equals("")) {
			return null;
		}
		try {
			if (rs2 != null)
				rs2.close();
			if (query2 != null)
				query2.close();
			query2 = db2Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs2 = query2.executeQuery(sql);
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
			rs2 = null;
		}
		finally {
			return rs2;
		}
	}
	/**
	 * 
	 * 
	 * ִ�����ݿ��ѯ���ܣ��������������ļ�¼����ʹ��ʱ��ע�⣩
	 * 
	 *  @ param ��׼��ѯsql���
	 * 
	 *  @ return ��¼��Int
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public int sqlQueryCount(String sql) {
		int rows = 0;
		if (sql == null) {
			sql = "";
		}
		sql = sql.trim();
		if (sql.equals("")) {
			return -1;
		}
		try {
			Statement sql_query = db2Con.createStatement(ResultSet.
			TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rstmp = sql_query.executeQuery(sql);
			rstmp.last();
			rows = rstmp.getRow();
			if (rstmp != null)
				rstmp.close();
			if (sql_query != null)
				sql_query.close();
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
			rows = -1;
		}
		finally {
			return rows;
		}
	}
	/**
	 * 
	 * 
	 * ִ�����ݿ��ѯ���ܣ�����ĳ�����ֶ�Ψһ��¼���ַ�����ʹ��ʱ��ע�⣩
	 * 
	 *  @ param ��׼��ѯsql���
	 * 
	 *  @ return ��ѯ���String
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public String sqlQuerySingle(String sql) {
		String str = "";
		if (sql == null) {
			sql = "";
		}
		sql = sql.trim();
		if (sql.equals("")) {
			return str;
		}
		try {
			Statement sql_query = db2Con.createStatement(ResultSet.
			TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rstmp = sql_query.executeQuery(sql);
			if (rstmp.next()) {
				str = rstmp.getString(1);
				if (str == null)
					str = "";
				else
					str = str.trim();
			}
			else
				str = "";
			if (rstmp != null)
				rstmp.close();
			if (sql_query != null)
				sql_query.close();
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
			str = "";
		}
		finally {
			return str;
		}
	}
	/**
	 * 
	 * 
	 * ִ�����ݿ�ɾ����������޸Ĺ��ܣ�����ִ�н��
	 * 
	 *  @ param ��׼sql���(����ѯsql���)
	 * 
	 *  @ return ִ�н�������м�¼
	 * 
	 *  @ exception thrown SQLException
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public int sqlUpdate(String sql) {
		if (sql == null)
			sql = "";
		sql = sql.trim();
		int ret = 0;
		try {
			ret = update.executeUpdate(sql);
		}
		catch (SQLException e) {
			ret = -1;
			ErrMsg = e.getMessage();
			SqlState = e.getSQLState();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
		}
		finally {
			return ret;
		}
	}
	/**
	 * 
	 * 
	 * �������̵���
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public java.sql.CallableStatement spCall(String sql) {
		if (sql == null) {
			sql = "";
		}
		sql = sql.trim();
		if (sql.equals("")) {
			return null;
		}
		try {
			st = db2Con.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
			st = null;
		}
		finally {
			return st;
		}
	}
	/**
	 * 
	 * 
	 * ��ô�����Ϣ.
	 * 
	 *  @ �޲���
	 * 
	 *  @ return ������Ϣ
	 * 
	 * 
	 */
	public String getErrMsg() {
		if (ErrMsg == null)
			ErrMsg = "";
		ErrMsg = ErrMsg.trim();
		return ErrMsg;
	}
	/**
	 * 
	 * 
	 * ��ô�����Ϣ.
	 * 
	 *  @ �޲���
	 * 
	 *  @ return ������Ϣ
	 * 
	 * 
	 */
	public String getSqlState() {
		if (SqlState == null)
			SqlState = "";
		SqlState = SqlState.trim();
		return SqlState;
	}
	/**
	 * 
	 * 
	 * ɨβ����.
	 * 
	 *  @ �޲���
	 * 
	 *  @ return ��
	 * 
	 * 
	 */
	public void finalize() {
		close();
	}
	/**
	 * 
	 * 
	 * �����������ݿ�.
	 * 
	 *  @ �޲���
	 * 
	 * 
	 * @return ���ӳɹ�����־
	 * 
	 * 
	 */
	public boolean reconnect() {
		try {
			close();
			db2Con = null;
			if (loadConfig()) {
				Class.forName(driver);
				db2Con = DriverManager.getConnection(url, user, password);
			} else {
				String driverName = "weblogic.jdbc.pool.Driver";
				Driver armsDriver = (Driver) Class.forName(driverName)
						.newInstance();
				String driverURL = "jdbc:weblogic:pool:cmisdbpool";
				db2Con = armsDriver.connect(driverURL, null);
			}
			if (db2Con == null)
				return false;
			else {
				try {
					query = db2Con.createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					update = db2Con.createStatement();
				}
				catch (SQLException e) {
					ErrMsg = e.getMessage();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
	}
	/**
	 * �ر����ݿ����ӣ��ͷ���Դ.
	 * 
	 *  @ �޲���
	 * 
	 *  @ return ��
	 * 
	 * 
	 */
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (query != null)
				query.close();
			if (rs2 != null)
				rs2.close();
			if (query2 != null)
				query2.close();
			if (update != null)
				update.close();
			if (db2Con != null)
				db2Con.close();
		}
		catch (Exception e) {
			ErrMsg = e.getMessage();
		}
	}
	/**
	 * 
	 * 
	 * ִ�����ݿ��ѯ���ܣ������ط�ҳ��ѯ���.
	 * 
	 * 
	 * Pagisql :Ҫ��ҳ��Query�ַ�����[��ʽΪselect * from tablename where ...]
	 * 
	 * 
	 * request :�������ݹ����еı�����[�������Ʒ�ҳʱ��pages����]
	 * 
	 * 
	 */
	/**
	 * 
	 * 
	 * ִ�����ݿ��ѯ���ܣ������ط�ҳ��ѯ���.
	 * 
	 * 
	 * Pagisql :Ҫ��ҳ��Query�ַ�����[��ʽΪselect * from tablename where ...]
	 * 
	 * 
	 * request :�������ݹ����еı�����[�������Ʒ�ҳʱ��pages����]
	 * 
	 * 
	 */
	// ��ȡ��ǰҳ��
	/**
	 * 
	 * 
	 * ���������,���ؽ���Զ���λ��ָ��ѡ��
	 * 
	 * 
	 * ��������
	 * 
	 * 
	 * zdmc:������<select>��ǩ������
	 * 
	 * 
	 * zdval:���������������<select>��ǩ��ֵ�����ؽ���Զ���λ����ѡ��
	 * 
	 * 
	 * sqlStr:SQL��ѯ���
	 * 
	 * 
	 * dh:1�����������<select name = 'zdmc'> </select>��ǩ
	 * 
	 * 
	 * dh:2�����������<select> </select>��ǩ
	 * 
	 * 
	 * dh:���������ֻ���Option�ַ���
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public String getSelect(String zdmc, String sql, String zdval, int dh) {
		String dm, mc, zdoption;
		if (dh == 1)
			zdoption = " <SELECT name='" + zdmc + "' > \n ";
		else if (dh == 2)
			zdoption = " <SELECT> \n ";
		else
			zdoption = "";
		try {
			Statement sql_query = db2Con.createStatement(ResultSet.
			TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rstmp = sql_query.executeQuery(sql);
			while (rstmp.next()) {
				dm = rstmp.getString(1).trim();
				mc = rstmp.getString(2).trim();
				if (dm.equals(zdval))
					zdoption = zdoption + "<OPTION selected value='" + dm
							+ "'>" + mc +
							"</OPTION> \n";
				else
					zdoption = zdoption + "<OPTION value='" + dm + "'>" + mc +
					"</OPTION> \n";
			}
			if (rstmp != null)
				rstmp.close();
			if (sql_query != null)
				sql_query.close();
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
			zdoption = "";
		}
		finally {
			if ((dh == 1) || (dh == 2))
				zdoption = zdoption + "</SELECT> \n";
			return zdoption;
		}
	}
	/**
	 * 
	 * 
	 * ����ѡ�����ݿ����
	 * 
	 * 
	 * ����:tab ������
	 * 
	 * 
	 * ����:defval ȱʡֵ
	 * 
	 * 
	 * ����:dmzd ������ֶ�
	 * 
	 * 
	 * ����:mczd �������ֶ�
	 * 
	 * 
	 * ����:cond ���ݿ�����
	 * 
	 * 
	 */
	public String seloption(String tab, String defval, String dmzd,
			String mczd,
			String cond) {
		String sql = "SELECT " + dmzd + " AS DM," + mczd + " AS MC FROM " + tab
				+
				" " + cond;
		return seloption(sql, defval);
	}
	/*
	 * 
	 * 
	 * �������ַ�����������SELECT��OPTIONѡ��
	 * 
	 * 
	 */
	public String seloption(String dm[], String mc[], String defval) {
		String zdoption = "";
		for (int i = 0; i < dm.length; i++) {
			if (dm[i].equals(defval.trim()))
				zdoption = zdoption + "<OPTION value='" + dm[i] + "' selected>"
						+ mc[i] +
						"</OPTION> \n";
			else
				zdoption = zdoption + "<OPTION value='" + dm[i] + "'>" + mc[i] +
				"</OPTION> \n";
		}
		return zdoption;
	}
	/*
	 * 
	 * 
	 * �������ݿ�����SELECT��OPTIONѡ��(ȱʡֵΪ��)
	 * 
	 * 
	 */
	public String seloption(String sql) {
		return seloption(sql, "");
	}
	/**
	 * 
	 * 
	 * �������ݿ�����SELECT��OPTIONѡ��
	 * 
	 * 
	 */
	@SuppressWarnings("finally")
	public String seloption(String sql, String defval) {
		String dm, mc, zdoption = "";
		try {
			Statement sql_query = db2Con.createStatement(ResultSet.
			TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rstmp = sql_query.executeQuery(sql);
			while (rstmp.next()) {
				dm = rstmp.getString(1).trim();
				mc = rstmp.getString(2).trim();
				if (dm.equals(defval.trim()))
					zdoption = zdoption + "<OPTION value='" + dm
							+ "' selected>" + mc +
							"</OPTION> \n";
				else
					zdoption = zdoption + "<OPTION value='" + dm + "'>" + mc +
					"</OPTION> \n";
			}
			if (rstmp != null)
				rstmp.close();
			if (sql_query != null)
				sql_query.close();
		}
		catch (SQLException e) {
			ErrMsg = e.getMessage();
			System.out.println("SUN:sqlQuery error[" + sql + "][" + ErrMsg
					+ "]");
			zdoption = "";
		}
		finally {
			return zdoption;
		}
	}
	/**
	 * 
	 * 
	 * �ֵ����ϵͳ���ݿ������
	 * 
	 * 
	 * ���������
	 * 
	 * 
	 * ��������
	 * 
	 * 
	 * zdmc:������<select>��ǩ������
	 * 
	 * 
	 * sql:SQL��ѯ���
	 * 
	 * 
	 * dh:1�����������<select name = 'zdmc'> </select>��ǩ
	 * 
	 * 
	 * dh:2�����������<select> </select>��ǩ
	 * 
	 * 
	 * dh:���������ֻ���Option�ַ���
	 * 
	 * 
	 */
	public String seloption(String zdmc, String sql, int dh) {
		return getSelect(zdmc, sql, "", dh);
	}
	public static void main(String args[]){
//		DBManage dbmng = new DBManage();
		System.out.println("���Գɹ�");
	}
}
/*
 * 
 * 
 * The End *
 * 
 * 
 */
