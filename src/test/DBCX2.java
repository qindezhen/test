package test;


import java.io.*;
import java.util.*;
/**
 * �������
 * @author chyto
 *
 */
public class DBCX2 {
	private static String errMsg = "";
	private static String errFile = "";//������ļ���
	
	public static void main(String[] args) {
		String xgcxlb = "F:/��������/����ѺƷϵͳ/changeLog/changeLog.txt";//�޸ĳ����б�
		String sourceDir = "D:/MyEclipse2014Workspace/";//ԴĿ¼�������ռ䣩
		String destinationDir = "F:/��������/����ѺƷϵͳ/changeLog";//Ŀ��Ŀ¼
		String projectName ="fxsc";//��Ŀ����
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try{
			String str="";
			boolean srcFlag=false;
			boolean classFlag=false;
			Map<String,String> map = new HashMap<String, String>();
			while(true){
				System.out.println("��ѡ�񡭡�");
				System.out.println("0���˳�");
				System.out.println("1������Դ����");
				System.out.println("2������.class�ļ�");
				System.out.println("3������Դ�ļ���.class�ļ�");
				
				str = stdin.readLine();//��������

				if(str.equals("0")){
					System.out.println("�����˳���");
					return;					
				}else if(str.equals("1")){
					srcFlag=true;
					break;					
				}else if(str.equals("2")){
					classFlag=true;
					break;					
				}else if(str.equals("3")){
					srcFlag=true;
					classFlag=true;
					break;					
				}else{
					System.out.println("�������");	
				}
			}
			
			ArrayList<String> list = new ArrayList<String>();
			String djPath = "";//�Ǽǵ��ļ�·��
			RandomAccessFile openFile=new RandomAccessFile(xgcxlb,"r");
			djPath=openFile.readLine();
			djPath = djPath.trim();
			System.out.println(djPath);
			String filedir = "";//�ļ�Ŀ¼
			while(djPath!=null) {
				list.add(djPath);
				djPath=openFile.readLine();
				if(djPath!=null){
					djPath=djPath.trim();
				}
			}
			openFile.close();
			for(int i=0;i<list.size();i++){
				String source = "";
				String destination = "";
				djPath = (String)list.get(i);
				errFile = djPath;
				if(!djPath.contains("WebRoot")){
					if(srcFlag){
						String javaDir = destinationDir + "/src"+djPath.substring(0,djPath.lastIndexOf("/"));
						File javaDirFi = new File(javaDir);
						if(!javaDirFi.exists()){
							if(!javaDirFi.mkdirs()){
								errMsg = javaDirFi.toString()+"Ŀ¼����ʧ��";
								throw new RuntimeException();
							}
						}
						String soujavapath = sourceDir + djPath;
						String desjavapath = destinationDir + "/src" + djPath;
						copyFile(soujavapath,desjavapath);
					}
					
					if(classFlag){ //��ȡclass�ļ�
						String soruceDjpath=djPath;
						djPath = djPath.replaceFirst(".java", ".class");
						if(djPath.indexOf("/"+projectName+"/")==0){
							source = sourceDir + djPath.replaceFirst(projectName+"/src",projectName+"/WebRoot/WEB-INF/classes");
							destination = destinationDir + djPath.replaceFirst(projectName+"/src",projectName+".war/WEB-INF/classes");
							if(source.indexOf(".class")!=-1){
								String curDir=source.substring(0, source.lastIndexOf("/")); //class�ļ���ַ
								String fileName = source.substring(source.lastIndexOf("/")+1,source.indexOf(".class"));
								File file =new File(curDir);//����·��
								String files[] = file.list();
								for(int k=0;k<files.length;k++){
									if(files[k].indexOf(fileName+"$")!=-1){ //�����ڲ���
										if(map.get(files[k])==null){
											list.add(soruceDjpath.substring(0, soruceDjpath.lastIndexOf("/")+1)+files[k]);
										}
									}
								}
							}
						}else{
							errMsg = djPath+"�Ǽǳ�������û�а���/Loan/��/CGS/";
							throw new RuntimeException();
						}
					}
				} else {
					if(srcFlag){
						String javaDir = destinationDir + "/src"+djPath.substring(0,djPath.lastIndexOf("/"));
						File javaDirFi = new File(javaDir);
						if(!javaDirFi.exists()){
							if(!javaDirFi.mkdirs()){
								errMsg = javaDirFi.toString()+"Ŀ¼����ʧ��";
								throw new RuntimeException();
							}
						
						}
	
						String soujavapath = sourceDir + djPath;
						String desjavapath = destinationDir + "/src" + djPath;
						copyFile(soujavapath,desjavapath);
					}
					
					if(classFlag){ 
						source = sourceDir + djPath;
						if(djPath.indexOf("/"+projectName+"/")==0){
							djPath = djPath.replaceFirst(projectName+"/WebRoot",projectName+".war");
						}else{
							errMsg = djPath+"�Ǽǳ�������û�а���/Loan/��/CGS/";
							throw new RuntimeException();
						}
						destination = destinationDir + djPath;
					}
				}
				
				//�Ǽ��б��¼
				if(classFlag){	
					filedir = destination.substring(0,(destination.lastIndexOf("/")));
					File fi = new File(filedir);
					errFile = fi.toString();
					//�����Ŀ¼��������ڣ�����Ҫ����
					if(!fi.exists()){
						if(!fi.mkdirs()){
							errMsg = fi.toString()+"Ŀ¼����ʧ��";
							throw new RuntimeException();
						}
						
					}
					File a = new File(destination);
					if(a.exists()){
						System.out.println(destination+"�Ѿ�����");
						continue;
					}
					System.out.print(destination.substring(destination.lastIndexOf("/")+1));
					System.out.print("\t");
					System.out.println(destination.substring(destinationDir.length(),(destination.lastIndexOf("/")+1)));
					copyFile(source,destination);
				}
				
			}
			System.out.println("ִ����ɣ�");
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("�����ļ�");
			System.out.println(errFile);
			System.out.println(errMsg);
			System.out.println("ִ�в��ɹ�");
		}
	}
	
	private static void copyFile(String path1,String path2) throws IOException{
		long modifytime = 0;//�ϴ��޸�ʱ��		
		errFile = path1;
		FileInputStream fi=new FileInputStream(path1);
		errFile = path2; 
		FileOutputStream fo=new FileOutputStream(path2);
		byte data[]=new byte[fi.available()]; 
		fi.read(data); 
		fo.write(data); 
		fi.close(); 
		fo.close(); 
		File sourceFi = new File(path1);
		File destFi = new File(path2);
		modifytime = sourceFi.lastModified();
		destFi.setLastModified(modifytime);	
	}
}

