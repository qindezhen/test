import java.net.Socket;
import java.util.Arrays;



public class Test {
	Door door = new DoorImpl();
	public String test2() {
		String reval="1,2,3,4,5,6,7,8,9,11,22,33,44,55,66,77,88,99,111,222,333,444,555,666,777,888,999,1111,2222";
		try{
			String[] ruleStr=reval.split(",");
			String[] temp=new String[9];
			int num=ruleStr.length/9;
//			int mod=ruleStr.length%9;
			for(int i=0;i<num;i++){
				temp=new String[9];
				int value=i*9;
				System.arraycopy(ruleStr, value, temp, 0, 9);
				System.out.println(Arrays.asList(temp).toString());
			}
			if((ruleStr.length%9)>0){
				temp=new String[ruleStr.length%9];
				System.arraycopy(ruleStr, num*9, temp, 0, ruleStr.length%9);
				System.out.println(Arrays.asList(temp).toString());
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("========");
		}
		return "";
	}
	public static void main(String[] args){
		Socket toReilly=null;
		try{
			toReilly=new Socket("192.168.60.85",8000);
			if(toReilly.isConnected()){
				System.out.println("已连接");
			}else{
				System.out.println("未连接");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
