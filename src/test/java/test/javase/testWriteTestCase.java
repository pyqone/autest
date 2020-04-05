package test.javase;

import java.io.File;

import pres.auxiliary.work.old.testcase.change.WriteTestCase;

public class testWriteTestCase {
	public static void main(String[] args) throws Exception {
		WriteTestCase wtc = new WriteTestCase(new File("E:\\test.xml"));
		wtc.write();
		System.out.println("The End");
	}
}
