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
 * 数据库操作类
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
	// 分页显示
	// sucess connction marker
	public boolean marker = false;
	private String driver = null;
	private String url = null;
	private String user = null;
	private String password = null;
	static int conFlag = 1;// 0-从连接池 1-配置文件
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
	 * 从指定文件载入
	 * 
	 * 
	 * @param name
	 *            参数为文件名
	 * 
	 * 
	 * @return 成功返回 true
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
			db2Con.setAutoCommit(true); // 重新设置数据库为自动提交
		}
		catch (Exception e) {
			db2Con = null;
			System.out.println("{获取连接:}==== >失败<==== ");
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
	 * 暂时
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
	 * 获取数据库是否自动提交
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
	 * 设置数据库是否自动提交
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
	 * 数据库语句提交
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
	 * 数据库语句回滚
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
	 * 执行数据库查询功能，返回查询结果
	 * 
	 *  @ param 标准查询sql语句
	 * 
	 *  @ return 查询结果集ResultSet
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
	 * 执行数据库查询功能(用第二个句柄来执行，和sqlQuery不冲突)，返回查询结果
	 * 
	 *  @ param 标准查询sql语句
	 * 
	 *  @ return 查询结果集ResultSet
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
	 * 执行数据库查询功能，返回满足条件的记录数（使用时请注意）
	 * 
	 *  @ param 标准查询sql语句
	 * 
	 *  @ return 记录数Int
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
	 * 执行数据库查询功能，返回某单个字段唯一记录的字符串（使用时请注意）
	 * 
	 *  @ param 标准查询sql语句
	 * 
	 *  @ return 查询结果String
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
	 * 执行数据库删除、插入和修改功能，返回执行结果
	 * 
	 *  @ param 标准sql语句(除查询sql语句)
	 * 
	 *  @ return 执行结果，几行记录
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
	 * 存贮过程调用
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
	 * 获得错误信息.
	 * 
	 *  @ 无参数
	 * 
	 *  @ return 错误信息
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
	 * 获得错误信息.
	 * 
	 *  @ 无参数
	 * 
	 *  @ return 错误信息
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
	 * 扫尾工作.
	 * 
	 *  @ 无参数
	 * 
	 *  @ return 无
	 * 
	 * 
	 */
	public void finalize() {
		close();
	}
	/**
	 * 
	 * 
	 * 重新连接数据库.
	 * 
	 *  @ 无参数
	 * 
	 * 
	 * @return 连接成功与否标志
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
	 * 关闭数据库连接，释放资源.
	 * 
	 *  @ 无参数
	 * 
	 *  @ return 无
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
	 * 执行数据库查询功能，并返回翻页查询结果.
	 * 
	 * 
	 * Pagisql :要分页的Query字符串。[形式为select * from tablename where ...]
	 * 
	 * 
	 * request :参数传递过程中的变量。[用来控制翻页时的pages变量]
	 * 
	 * 
	 */
	/**
	 * 
	 * 
	 * 执行数据库查询功能，并返回翻页查询结果.
	 * 
	 * 
	 * Pagisql :要分页的Query字符串。[形式为select * from tablename where ...]
	 * 
	 * 
	 * request :参数传递过程中的变量。[用来控制翻页时的pages变量]
	 * 
	 * 
	 */
	// 获取当前页码
	/**
	 * 
	 * 
	 * 输出下拉框,返回结果自动定位到指定选项
	 * 
	 * 
	 * 参数意义
	 * 
	 * 
	 * zdmc:代表是<select>标签的名字
	 * 
	 * 
	 * zdval:传入参数，代表是<select>标签的值，返回结果自动定位到该选项
	 * 
	 * 
	 * sqlStr:SQL查询语句
	 * 
	 * 
	 * dh:1代表输出带有<select name = 'zdmc'> </select>标签
	 * 
	 * 
	 * dh:2代表输出带有<select> </select>标签
	 * 
	 * 
	 * dh:其他情况则只输出Option字符串
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
	 * 下拉选项数据库操作
	 * 
	 * 
	 * 参数:tab 表名字
	 * 
	 * 
	 * 参数:defval 缺省值
	 * 
	 * 
	 * 参数:dmzd 表代码字段
	 * 
	 * 
	 * 参数:mczd 表名称字段
	 * 
	 * 
	 * 条件:cond 数据库条件
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
	 * 根据两字符串数组生成SELECT的OPTION选项
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
	 * 根据数据库生成SELECT的OPTION选项(缺省值为空)
	 * 
	 * 
	 */
	public String seloption(String sql) {
		return seloption(sql, "");
	}
	/**
	 * 
	 * 
	 * 根据数据库生成SELECT的OPTION选项
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
	 * 字典相关系统数据库操作。
	 * 
	 * 
	 * 输出下拉框
	 * 
	 * 
	 * 参数意义
	 * 
	 * 
	 * zdmc:代表是<select>标签的名字
	 * 
	 * 
	 * sql:SQL查询语句
	 * 
	 * 
	 * dh:1代表输出带有<select name = 'zdmc'> </select>标签
	 * 
	 * 
	 * dh:2代表输出带有<select> </select>标签
	 * 
	 * 
	 * dh:其他情况则只输出Option字符串
	 * 
	 * 
	 */
	public String seloption(String zdmc, String sql, int dh) {
		return getSelect(zdmc, sql, "", dh);
	}
	public static void main(String args[]){
//		DBManage dbmng = new DBManage();
		System.out.println("测试成功");
	}
}
/*
 * 
 * 
 * The End *
 * 
 * 
 */
