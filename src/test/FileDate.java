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
		String destinationDir = "e:/ab";//Ŀ��Ŀ¼
		String xgcxlb = "E:/class.txt";//�޸ĳ����б�
		
		ArrayList<String> list = new ArrayList<String>();
		String djpath = "";//�Ǽǵ��ļ�·��
		String filepath = "";//�Ǽǵ��ļ�·��
		FileReader openFile;
		try {
			File out=new File(destinationDir + "/�����б�.xls");
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
			
			System.out.println("һ����" + list.size() + "���ļ�!");
			
			WritableWorkbook wwb = null;
			wwb=Workbook.createWorkbook(fileos);
			
			 Label label = null;
			 //��ʽ
			WritableSheet baseSheet=wwb.createSheet("������Ϣ", 0);
			baseSheet.setColumnView(0, 23); //�����еĿ��
			baseSheet.setColumnView(1, 12); //�����еĿ��
			baseSheet.setColumnView(2, 12); //�����еĿ��
			baseSheet.setColumnView(3, 12); //�����еĿ��
			baseSheet.setColumnView(4, 12); //�����еĿ��
			baseSheet.setColumnView(5, 12); //�����еĿ��
			baseSheet.setColumnView(6, 12); //�����еĿ��
			baseSheet.setColumnView(7, 20); //�����еĿ��
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_80_PERCENT);
			cellFormat.setAlignment(Alignment.LEFT);
			
			WritableCellFormat cellFormat2 = new WritableCellFormat();
			cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_80_PERCENT);
			cellFormat2.setAlignment(Alignment.LEFT);
			cellFormat2.setWrap(true);//�Զ����� 
			
			//���ÿ�Ŀ���ڷ�ʽ
			WritableCellFormat cellFormatRight = new WritableCellFormat();
			cellFormatRight.setBackground(Colour.ICE_BLUE);
			cellFormatRight.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_80_PERCENT);
			cellFormatRight.setAlignment(Alignment.RIGHT);
			
			label=new Label(0,0,"Ŀ����",cellFormat2);
			baseSheet.addCell(label);
			baseSheet.mergeCells(0, 0, 2, 0);
			//label=new Label(1,0,"����·��",cellFormat2);
			//baseSheet.addCell(label);
			
			label=new Label(3,0,"���",cellFormat);
			baseSheet.addCell(label);
			label=new Label(4,0,"��������",cellFormat);
			baseSheet.addCell(label);
			
			label=new Label(5,0,"Ŀ���С",cellFormat);
			baseSheet.addCell(label);
			label=new Label(6,0,"�޸�ʱ��",cellFormatRight);
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
					temp = "class�ļ�";
				}else if("jsp".equals(houzhui) || "htm".equals(houzhui) || "html".equals(houzhui)) {
					temp = "ҳ���ļ�";
				}else if("js".equals(houzhui)) {
					temp = "JS�ļ�";
				}else if("xml".equals(houzhui) || "mex".equals(houzhui)) {
					temp = "�����ļ�";
				}else if("css".equals(houzhui)) {
					temp = "��ʽ�ļ�";
				}else if("jpg".equals(houzhui) || "gif".equals(houzhui)) {
					temp = "ͼƬ�ļ�";
				}
				
				label=new Label(3,i+1,"�������޸�",cellFormat);
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
