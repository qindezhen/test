
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
 * @deprecated Ŀ¼�򵥸��ļ��ĵ���
 * @author: ljm
 * @create date 2011-9-15
 */
public class ReportFile {

	String sourceDir="I:\\DEMo";//Դ�ļ���ַ
	String destinationDir="E:/ab"; //Ŀ���ļ���ַ
	String filePath="E:/ab/abc.txt"; //�����ļ��б��ļ�·��txt��ʽ
	private void copyFileMain(){ 
			List<String> ls = this.getDirList(filePath);
			//ȥ��list���ظ���¼start
			HashSet<String> set = new HashSet<String>(ls); //ȥ��ls�����е��ظ���¼
			ls.clear();
			ls.addAll(set);
			//ȥ��list���ظ���¼end
			System.out.println("����ظ��ļ��� ����"+ls.size()+"���ļ���");
			int i=0;
			for(i=0;i<ls.size();i++){
				File fileDirectory = new File(sourceDir+ls.get(i)); //Դ�ļ���ַ
				if(fileDirectory.isDirectory()){ //�����Ŀ¼
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
				System.out.println("�ɹ�����:"+i+"�ļ���");
				System.out.println("���Ƴɹ�!");
			}else{
				System.out.println("����ʧ�ܣ�");
			}
			
	}
	
	
	private List<String> getDirList(String filePath){ //�õ�Ҫ�����ļ��ļ���
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
	
	//Ŀ¼����srcDirNameԴĿ¼ destDirNameĿ��Ŀ¼
	 private boolean copyDirectory(String srcDirName, String destDirName) {  
	        File srcDir = new File(srcDirName);  
	        if(!srcDir.exists()) //���Դ�ļ������� 
	        {  
	            System.out.println("����Ŀ¼ʧ�ܣ�ԭ��"+srcDir+"������");  
	            return false;  
	        }else if(!srcDir.isDirectory())   //�������Ŀ¼
	        {  
	            System.out.println("����Ŀ¼ʧ�ܣ�ԭ��"+srcDir+"����һ��Ŀ¼");  
	             return false;  
	        }
	        File destDir = new File(destDirName);
	        if(!destDir.exists()){
	        	if(!destDir.mkdirs()){ //����Ŀ¼
	        		System.out.println("����Ŀ¼ʧ�ܣ�"+destDirName);
	        		return false;
	        	}
	        }
	        //����ļ����������ļ��ָ�����β���Զ�����ļ��ָ���   
	        if(!destDirName.endsWith(File.separator)){  
	            destDirName = destDirName + File.separator;  
	        }  
	          
	        File[] fileList = srcDir.listFiles();
	        boolean flag =true;
	        for(File fileTemp:fileList){
	        	if(fileTemp.isFile()){ //������ļ�
	        		flag = this.copyFile(fileTemp.getAbsolutePath(), destDirName+fileTemp.getName()); 
	        	}
	        	if(fileTemp.isDirectory()){ //�����Ŀ¼
	        		flag = copyDirectory(fileTemp.getAbsolutePath(),destDirName+fileTemp.getName());
	        	}
	        	if(!flag){
	        		break;
	        	}
	        }
	        return true;
	 }
	//�ļ�����
	 private boolean copyFile(String sourceDir,String destDir){
		boolean flag = true;
		FileInputStream fi=null;
		FileOutputStream fo=null;
		File file = new File(destDir);
		try{
			if(!file.getParentFile().exists()){ //��������ڸ�Ŀ¼
				if(!file.getParentFile().mkdirs()){ //����Ŀ¼ʧ��
					System.out.println("����Ŀ¼ʧ�ܣ�"+file.getParent());
					return false;
				}
			}
			if(file.isFile() && file.exists()){ //Ŀ���ļ�����
				System.out.println("Ŀ���ļ����ڽ�Ҫɾ���ļ���"+ destDir);
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
			System.out.println("�ļ�������");
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
