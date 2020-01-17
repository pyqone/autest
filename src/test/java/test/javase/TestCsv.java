package test.javase;

import java.io.File;
import java.io.FileReader;

import com.opencsv.CSVReader;

public class TestCsv {
	public static void main(String[] args) throws Exception {
		CSVReader csv = new CSVReader(new FileReader(new File("src/test/java/test/javase/resource/test.csv")));
		String[] s;
		while((s = csv.readNext()) != null) {
			for (String ss : s) {
				System.out.println(ss);
			}
		}
		
		csv.close();
	}
}
