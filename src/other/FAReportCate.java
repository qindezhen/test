package other;
/**
 * @Title: FAReportCate.java
 * @Description: ���񱨱�����
 * @Copyright: Copyright (c) 2009
 * @Company: ˴�����
 * 
 * @author: ������
 * @create date 2009-7-22
 */
public class FAReportCate {

	   private String typecode;//���ʹ���
	   private String freptype;//����
	   private String frepname;//��������
	   private String frepcode;//�������
	   private int haslastvalue;//�Ƿ����ڳ�
	   private String verexpress="";//У�鹫ʽ
	   private String verexpressname="";
	   private int rown;//����
	   private int coln;//����
	   private int sublen = 0;
	   private int firstcollen = 0;
	   private int secondcollen = 0;
	   private int titlecolspan = 2;
	   private String mandatory; //�Ƿ��¼
	   private String mandatory1="0";//��
	   private String mandatory2="0";//����
	   private String mandatory3="0";//����
	   private String mandatory4="0";//��
	   private String isexist; //�Ƿ����¼��ı���
	   private String frepmode = "0"; //���� 0һ�� 1����
	   
	public String getFrepmode() {
		return frepmode;
	}
	public void setFrepmode(String frepmode) {
		this.frepmode = frepmode;
	}
	public String getIsexist() {
		return isexist;
	}
	public void setIsexist(String isexist) {
		this.isexist = isexist;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public int getTitlecolspan() {
		return titlecolspan;
	}
	public void setTitlecolspan(int titlecolspan) {
		this.titlecolspan = titlecolspan;
	}
	public int getColn() {
		return coln;
	}
	public void setColn(int coln) {
		this.coln = coln;
	}
	public String getFrepcode() {
		return frepcode;
	}
	public void setFrepcode(String frepcode) {
		this.frepcode = frepcode;
	}
	public String getFrepname() {
		return frepname;
	}
	public void setFrepname(String frepname) {
		this.frepname = frepname;
	}
	public String getFreptype() {
		return freptype;
	}
	public void setFreptype(String freptype) {
		this.freptype = freptype;
	}
	public int getHaslastvalue() {
		return haslastvalue;
	}
	public void setHaslastvalue(int haslastvalue) {
		this.haslastvalue = haslastvalue;
	}
	public int getRown() {
		return rown;
	}
	public void setRown(int rown) {
		this.rown = rown;
	}
	public String getTypecode() {
		return typecode;
	}
	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
	public String getVerexpress() {
		return verexpress;
	}
	public void setVerexpress(String verexpress) {
		this.verexpress = verexpress;
	}
	public int getSublen() {
		return sublen;
	}
	public void setSublen(int sublen) {
		this.sublen = sublen;
	}
	public int getFirstcollen() {
		return firstcollen;
	}
	public void setFirstcollen(int firstcollen) {
		this.firstcollen = firstcollen;
	}
	public int getSecondcollen() {
		return secondcollen;
	}
	public void setSecondcollen(int secondcollen) {
		this.secondcollen = secondcollen;
	}
	public String getMandatory1() {
		return mandatory1;
	}
	public void setMandatory1(String mandatory1) {
		this.mandatory1 = mandatory1;
	}
	public String getMandatory2() {
		return mandatory2;
	}
	public void setMandatory2(String mandatory2) {
		this.mandatory2 = mandatory2;
	}
	public String getMandatory3() {
		return mandatory3;
	}
	public void setMandatory3(String mandatory3) {
		this.mandatory3 = mandatory3;
	}
	public String getMandatory4() {
		return mandatory4;
	}
	public void setMandatory4(String mandatory4) {
		this.mandatory4 = mandatory4;
	}
	public String getVerexpressname() {
		return verexpressname;
	}
	public void setVerexpressname(String verexpressname) {
		this.verexpressname = verexpressname;
	}
	   
	   
}
