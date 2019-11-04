package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class FileDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String destinationDir = "e:/ab";//目标目录
		String xgcxlb = "E:/class.txt";//修改程序列表
		
		ArrayList<String> list = new ArrayList<String>();
		String djpath = "";//登记的文件路径
		String filepath = "";//登记的文件路径
		FileReader openFile;
		try {
			File out=new File(destinationDir + "/程序列表.xls");
			FileOutputStream fileos = new FileOutputStream(out);
			
			openFile = new FileReader(xgcxlb);
			BufferedReader bufferedReader = new BufferedReader(openFile);

			djpath=bufferedReader.readLine();
			djpath = djpath.trim();
			
			while(djpath!=null){
				list.add(djpath);
				djpath=bufferedReader.readLine();
				if(djpath!=null)djpath=djpath.trim();
			}
			openFile.close();
			
			System.out.println("一共有" + list.size() + "个文件!");
			
			WritableWorkbook wwb = null;
			wwb=Workbook.createWorkbook(fileos);
			
			 Label label = null;
			 //格式
			WritableSheet baseSheet=wwb.createSheet("基本信息", 0);
			baseSheet.setColumnView(0, 23); //设置列的宽度
			baseSheet.setColumnView(1, 12); //设置列的宽度
			baseSheet.setColumnView(2, 12); //设置列的宽度
			baseSheet.setColumnView(3, 12); //设置列的宽度
			baseSheet.setColumnView(4, 12); //设置列的宽度
			baseSheet.setColumnView(5, 12); //设置列的宽度
			baseSheet.setColumnView(6, 12); //设置列的宽度
			baseSheet.setColumnView(7, 20); //设置列的宽度
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_80_PERCENT);
			cellFormat.setAlignment(Alignment.LEFT);
			
			WritableCellFormat cellFormat2 = new WritableCellFormat();
			cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_80_PERCENT);
			cellFormat2.setAlignment(Alignment.LEFT);
			cellFormat2.setWrap(true);//自动换行 
			
			//设置科目对期方式
			WritableCellFormat cellFormatRight = new WritableCellFormat();
			cellFormatRight.setBackground(Colour.ICE_BLUE);
			cellFormatRight.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_80_PERCENT);
			cellFormatRight.setAlignment(Alignment.RIGHT);
			
			label=new Label(0,0,"目标名",cellFormat2);
			baseSheet.addCell(label);
			baseSheet.mergeCells(0, 0, 2, 0);
			//label=new Label(1,0,"绝对路径",cellFormat2);
			//baseSheet.addCell(label);
			
			label=new Label(3,0,"类别",cellFormat);
			baseSheet.addCell(label);
			label=new Label(4,0,"中文描述",cellFormat);
			baseSheet.addCell(label);
			
			label=new Label(5,0,"目标大小",cellFormat);
			baseSheet.addCell(label);
			label=new Label(6,0,"修改时间",cellFormatRight);
			baseSheet.addCell(label);

			for(int i=0;i<list.size();i++){
				filepath = (String)list.get(i);
				File file = new File(destinationDir+ filepath);
				Date d = new Date(file.lastModified());
				SimpleDateFormat format22 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
			
				
				label=new Label(0,i+1,file.getPath().replace(file.getName(), "").replace("\\", "/").replace(destinationDir, "")+file.getName(),cellFormat2);
				baseSheet.addCell(label);
				baseSheet.mergeCells(0, i+1, 2, i+1);
				//label=new Label(1,i+1,file.getPath().replace(file.getName(), "").replace("\\", "/").replace(destinationDir, ""),cellFormat2);
			//	baseSheet.addCell(label);
				
				String houzhui = file.getName().substring(file.getName().lastIndexOf(".")+1);
				houzhui = houzhui.toLowerCase();
				String temp = "";
				
				if("class".equals(houzhui)){
					temp = "class文件";
				}else if("jsp".equals(houzhui) || "htm".equals(houzhui) || "html".equals(houzhui)) {
					temp = "页面文件";
				}else if("js".equals(houzhui)) {
					temp = "JS文件";
				}else if("xml".equals(houzhui) || "mex".equals(houzhui)) {
					temp = "配置文件";
				}else if("css".equals(houzhui)) {
					temp = "样式文件";
				}else if("jpg".equals(houzhui) || "gif".equals(houzhui)) {
					temp = "图片文件";
				}
				
				label=new Label(3,i+1,"新增或修改",cellFormat);
				baseSheet.addCell(label);
				label=new Label(4,i+1,temp,cellFormat);
				baseSheet.addCell(label);
				
				
				label=new Label(5,i+1,"" + file.length(),cellFormat);
				baseSheet.addCell(label);
				
				label=new Label(6,i+1,format22.format(d),cellFormatRight);
				baseSheet.addCell(label);
				
				if(! file.exists()){
				System.out.println(filepath  + ":" + file.length() + "====" + file.exists() );
				}
				
			}
			
			fileos.flush();
			 wwb.write();
			 wwb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
