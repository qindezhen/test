
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * 
 * @deprecated 目录或单个文件的导出
 * @author: ljm
 * @create date 2011-9-15
 */
public class ReportFile {

	String sourceDir="I:\\DEMo";//源文件地址
	String destinationDir="E:/ab"; //目标文件地址
	String filePath="E:/ab/abc.txt"; //复制文件列表文件路径txt格式
	private void copyFileMain(){ 
			List<String> ls = this.getDirList(filePath);
			//去除list中重复记录start
			HashSet<String> set = new HashSet<String>(ls); //去掉ls集合中的重复记录
			ls.clear();
			ls.addAll(set);
			//去除list中重复记录end
			System.out.println("清除重复文件后 共："+ls.size()+"个文件！");
			int i=0;
			for(i=0;i<ls.size();i++){
				File fileDirectory = new File(sourceDir+ls.get(i)); //源文件地址
				if(fileDirectory.isDirectory()){ //如果是目录
					if(!copyDirectory(sourceDir+ls.get(i),destinationDir+ls.get(i))){
						break;
					}
				}else{
					if(!copyFile(sourceDir+ls.get(i),destinationDir+ls.get(i))){
						break;
					}
				}
			}
			if(i==ls.size()){
				System.out.println("成功复制:"+i+"文件！");
				System.out.println("复制成功!");
			}else{
				System.out.println("复制失败！");
			}
			
	}
	
	
	private List<String> getDirList(String filePath){ //得到要复制文件的集合
		List<String> dirLs = new ArrayList<String>();
		FileReader openFile;
		try {
			openFile = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(openFile);
			String destinationDir =bufferedReader.readLine().trim();
			while(destinationDir!=null){
				dirLs.add(destinationDir);
				destinationDir=bufferedReader.readLine();
				if(destinationDir!=null)destinationDir=destinationDir.trim();
			}
			openFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return dirLs;
	}
	
	//目录复制srcDirName源目录 destDirName目的目录
	 private boolean copyDirectory(String srcDirName, String destDirName) {  
	        File srcDir = new File(srcDirName);  
	        if(!srcDir.exists()) //如果源文件不存在 
	        {  
	            System.out.println("复制目录失败：原因"+srcDir+"不存在");  
	            return false;  
	        }else if(!srcDir.isDirectory())   //如果不是目录
	        {  
	            System.out.println("复制目录失败：原因"+srcDir+"不是一个目录");  
	             return false;  
	        }
	        File destDir = new File(destDirName);
	        if(!destDir.exists()){
	        	if(!destDir.mkdirs()){ //创建目录
	        		System.out.println("创建目录失败："+destDirName);
	        		return false;
	        	}
	        }
	        //如果文件名不是以文件分隔符结尾，自动添加文件分隔符   
	        if(!destDirName.endsWith(File.separator)){  
	            destDirName = destDirName + File.separator;  
	        }  
	          
	        File[] fileList = srcDir.listFiles();
	        boolean flag =true;
	        for(File fileTemp:fileList){
	        	if(fileTemp.isFile()){ //如果是文件
	        		flag = this.copyFile(fileTemp.getAbsolutePath(), destDirName+fileTemp.getName()); 
	        	}
	        	if(fileTemp.isDirectory()){ //如果是目录
	        		flag = copyDirectory(fileTemp.getAbsolutePath(),destDirName+fileTemp.getName());
	        	}
	        	if(!flag){
	        		break;
	        	}
	        }
	        return true;
	 }
	//文件复制
	 private boolean copyFile(String sourceDir,String destDir){
		boolean flag = true;
		FileInputStream fi=null;
		FileOutputStream fo=null;
		File file = new File(destDir);
		try{
			if(!file.getParentFile().exists()){ //如果不存在该目录
				if(!file.getParentFile().mkdirs()){ //创建目录失败
					System.out.println("创建目录失败："+file.getParent());
					return false;
				}
			}
			if(file.isFile() && file.exists()){ //目标文件存在
				System.out.println("目标文件存在将要删除文件："+ destDir);
				file.delete();
			}
			fi = new FileInputStream(sourceDir); 
			fo = new FileOutputStream(destDir);
			byte data[]=new byte[fi.available()];
			fi.read(data);
			fo.write(data);
			fi.close();
			fo.close();
		}catch(FileNotFoundException e){
			System.out.println("文件不存在");
			flag = false;
			e.printStackTrace();
		}catch(IOException e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	 
	 public static void main(String[] args){
		 ReportFile repFile = new ReportFile();
		 repFile.copyFileMain();
	 }
}
