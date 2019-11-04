package other;

public class FinCourseDic {

	 private String coursecode;//科目编码
	 private String tradetype;//报表类型
	 private String reporttype;//所属报表类别
	 private String coursename;//科目名称
	 private String remarks;//备注
	 
	 private String courseshort;//科目名称缩写
	 private String reportname;
	 
	public String getReportname() {
		return reportname;
	}
	public void setReportname(String reportname) {
		this.reportname = reportname;
	}
	public String getCourseshort() {
		return courseshort;
	}
	public void setCourseshort(String courseshort) {
		this.courseshort = courseshort;
	}
	public String getCoursecode() {
		return coursecode;
	}
	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReporttype() {
		return reporttype;
	}
	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}
	public String getTradetype() {
		return tradetype;
	}
	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}
	 
	 
}
