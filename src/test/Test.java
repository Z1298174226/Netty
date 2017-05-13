package test;

import java.io.File;

public class Test {
	public static void main(String [] args){
		String path = System.getProperty("user.div")+"test.txt";
		//String path_1 = ".\\src\\test.txt";
		File file = new File(path);
		System.out.println(file.canRead());
		System.out.println(file.exists());
	}
	

}
