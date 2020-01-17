package pres.auxiliary.tool.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.testng.annotations.Test;

public class DisposeTextTest {
	@Test
	public void useFunction() throws IOException {
		ArrayList<String> arr1 = DisposeText.compareFileWord(new File("src/test/java/pres/auxiliary/tool/file/统计数据名单.txt"), 
				new File("src/test/java/pres/auxiliary/tool/file/源数据名单.txt"));
		ArrayList<String> arr2 = DisposeText.compareFileWord(new File("src/test/java/pres/auxiliary/tool/file/源数据名单.txt"), 
				new File("src/test/java/pres/auxiliary/tool/file/统计数据名单.txt"));
		
		arr1.forEach(System.out :: println);
		System.out.println("--------------------------");
		arr2.forEach(System.out :: println); 
		
		System.out.println("The End");
	}
	
	@Test
	public void data() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src/test/java/pres/auxiliary/tool/file/统计数据名单2.txt")));
		for (String text : DisposeText.wordDelDuplication(new File("src/test/java/pres/auxiliary/tool/file/统计数据名单.txt"))) {
			bw.write(text);
			bw.newLine();
		}
		
		bw.close();
	}
}
