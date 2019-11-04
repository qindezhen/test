package test;


import java.io.*;
import java.util.*;
/**
 * 打包程序
 * @author chyto
 *
 */
public class DBCX2 {
	private static String errMsg = "";
	private static String errFile = "";//出错的文件名
	
	public static void main(String[] args) {
		String xgcxlb = "F:/阜新银行/阜新押品系统/changeLog/changeLog.txt";//修改程序列表
		String sourceDir = "D:/MyEclipse2014Workspace/";//源目录（工作空间）
		String destinationDir = "F:/阜新银行/阜新押品系统/changeLog";//目标目录
		String projectName ="fxsc";//项目名称
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try{
			String str="";
			boolean srcFlag=false;
			boolean classFlag=false;
			Map<String,String> map = new HashMap<String, String>();
			while(true){
				System.out.println("请选择……");
				System.out.println("0、退出");
				System.out.println("1、生成源代码");
				System.out.println("2、生成.class文件");
				System.out.println("3、生成源文件及.class文件");
				
				str = stdin.readLine();//键盘输入

				if(str.equals("0")){
					System.out.println("程序退出！");
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
					System.out.println("输入错误！");	
				}
			}
			
			ArrayList<String> list = new ArrayList<String>();
			String djPath = "";//登记的文件路径
			RandomAccessFile openFile=new RandomAccessFile(xgcxlb,"r");
			djPath=openFile.readLine();
			djPath = djPath.trim();
			System.out.println(djPath);
			String filedir = "";//文件目录
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
								errMsg = javaDirFi.toString()+"目录创建失败";
								throw new RuntimeException();
							}
						}
						String soujavapath = sourceDir + djPath;
						String desjavapath = destinationDir + "/src" + djPath;
						copyFile(soujavapath,desjavapath);
					}
					
					if(classFlag){ //抽取class文件
						String soruceDjpath=djPath;
						djPath = djPath.replaceFirst(".java", ".class");
						if(djPath.indexOf("/"+projectName+"/")==0){
							source = sourceDir + djPath.replaceFirst(projectName+"/src",projectName+"/WebRoot/WEB-INF/classes");
							destination = destinationDir + djPath.replaceFirst(projectName+"/src",projectName+".war/WEB-INF/classes");
							if(source.indexOf(".class")!=-1){
								String curDir=source.substring(0, source.lastIndexOf("/")); //class文件地址
								String fileName = source.substring(source.lastIndexOf("/")+1,source.indexOf(".class"));
								File file =new File(curDir);//绝对路径
								String files[] = file.list();
								for(int k=0;k<files.length;k++){
									if(files[k].indexOf(fileName+"$")!=-1){ //存在内部类
										if(map.get(files[k])==null){
											list.add(soruceDjpath.substring(0, soruceDjpath.lastIndexOf("/")+1)+files[k]);
										}
									}
								}
							}
						}else{
							errMsg = djPath+"登记程序里面没有包括/Loan/或/CGS/";
							throw new RuntimeException();
						}
					}
				} else {
					if(srcFlag){
						String javaDir = destinationDir + "/src"+djPath.substring(0,djPath.lastIndexOf("/"));
						File javaDirFi = new File(javaDir);
						if(!javaDirFi.exists()){
							if(!javaDirFi.mkdirs()){
								errMsg = javaDirFi.toString()+"目录创建失败";
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
							errMsg = djPath+"登记程序里面没有包括/Loan/或/CGS/";
							throw new RuntimeException();
						}
						destination = destinationDir + djPath;
					}
				}
				
				//登记列表记录
				if(classFlag){	
					filedir = destination.substring(0,(destination.lastIndexOf("/")));
					File fi = new File(filedir);
					errFile = fi.toString();
					//打包的目录如果不存在，则需要创建
					if(!fi.exists()){
						if(!fi.mkdirs()){
							errMsg = fi.toString()+"目录创建失败";
							throw new RuntimeException();
						}
						
					}
					File a = new File(destination);
					if(a.exists()){
						System.out.println(destination+"已经存在");
						continue;
					}
					System.out.print(destination.substring(destination.lastIndexOf("/")+1));
					System.out.print("\t");
					System.out.println(destination.substring(destinationDir.length(),(destination.lastIndexOf("/")+1)));
					copyFile(source,destination);
				}
				
			}
			System.out.println("执行完成！");
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("出错文件");
			System.out.println(errFile);
			System.out.println(errMsg);
			System.out.println("执行不成功");
		}
	}
	
	private static void copyFile(String path1,String path2) throws IOException{
		long modifytime = 0;//上次修改时间		
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

