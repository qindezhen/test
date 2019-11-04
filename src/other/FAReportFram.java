package other;
/**
 * @Title: FAReportFram.java
 * @Description:报表结构
 * @Copyright: Copyright (c) 2009
 * @Company: 舜德软件
 * 
 * @author: 耿继鹏
 * @create date 2009-7-22
 */
public class FAReportFram {

	   private String frepcode;//报表代码
	   private int serno;//序号
	   private int rowno;//行号
	   private int colno;//列号
	   private String subcode;//科目编号
	   private String subproperty;//科目属性
	   private String isnewcol;//是否新栏
	   private String showname;//展示名称
	   private String calexpress ;//计算公式
	   private String calexpressname;
	   private String isimportant ;//是否重要科目
	   
	public String getIsimportant() {
		return isimportant;
	}
	public void setIsimportant(String isimportant) {
		this.isimportant = isimportant;
	}
	public String getCalexpress() {
		return calexpress;
	}
	public void setCalexpress(String calexpress) {
		this.calexpress = calexpress;
	}
	public int getColno() {
		return colno;
	}
	public void setColno(int colno) {
		this.colno = colno;
	}
	public String getFrepcode() {
		return frepcode;
	}
	public void setFrepcode(String frepcode) {
		this.frepcode = frepcode;
	}
	public String getIsnewcol() {
		return isnewcol;
	}
	public void setIsnewcol(String isnewcol) {
		this.isnewcol = isnewcol;
	}
	public int getRowno() {
		return rowno;
	}
	public void setRowno(int rowno) {
		this.rowno = rowno;
	}
	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public String getShowname() {
		return showname;
	}
	public void setShowname(String showname) {
		this.showname = showname;
	}
	public String getSubcode() {
		return subcode;
	}
	public void setSubcode(String subcode) {
		this.subcode = subcode;
	}
	public String getSubproperty() {
		return subproperty;
	}
	public void setSubproperty(String subproperty) {
		this.subproperty = subproperty;
	}
	public String getCalexpressname() {
		return calexpressname;
	}
	public void setCalexpressname(String calexpressname) {
		this.calexpressname = calexpressname;
	}
	   
	   
}
