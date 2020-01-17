package test.javase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class Test111 {
	public static void main(String[] args) throws Exception {
		File f1 = new File("D:\\1.txt");
		File f2 = new File("D:\\2.txt");
		
//		System.out.println(f1.getName());
		
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		
//		BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\1.txt"), "UTF-8"));
//		BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\2.txt"), "UTF-8"));
		
		String text1 = "";
		String text2 = "";
		
		String s = "";
		while((s = br1.readLine()) != null) {
			text1 += s;
			text1 += "\n";
		}
		
		s = "";
		while((s = br2.readLine()) != null) {
			text2 += s;
			text2 += "\n";
		}
		
//		System.out.println(text1);
//		System.out.println("-----------------");
//		System.out.println(text2);
		
		String[] texts1 = text1.split("\\n");
		String[] texts2 = text2.split("\\n");
		
		for (String t1 : texts1) {
			for (String t2 : texts2) {
//				String tt1 = new String(t1.getBytes("ISO-8859-1"),"UTF-8");
//				String tt2 = new String(t2.getBytes("ISO-8859-1"), "UTF-8");
				System.out.println(t1.length());
				System.out.println(t2.length());
				System.out.println("字符串“" + t1 + "”与字符串“" + t2 + "”的比较结果为：" + t1.equals(t2));
			}
		}
		
		/*
		System.out.println("t1:" + texts1.length);
		System.out.println("t2:" + texts2.length);
		
		for (String t1 : texts1) {
			boolean b = true;
			for (String t2 : texts2) {
				if (t2.equals(t1)) {
					b = false;
					break;
				}
				
				b = true;
			}
			
			if(b) {
				System.out.println(t1);
			}
			
		}*/
		
		br1.close();
		br2.close();
	}
}
