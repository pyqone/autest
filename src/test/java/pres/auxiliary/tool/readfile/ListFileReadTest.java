package pres.auxiliary.tool.readfile;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ListFileReadTest {
	ListFileRead lfr;
	
	@BeforeTest
	public void start() throws IOException {
		lfr = new ListFileRead(new File("F:\\5.测试文件\\1.用例生成程序测试文件\\模块信息.csv"));
	}
	
	@AfterMethod
	public void showColumn() {
		lfr.getColumn(0).forEach(System.out :: println);
		System.out.println("------------------------------------");
		lfr.getColumn(1).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}

	@Test
	public void tableTranspositionTest() {
		lfr.tableTransposition();
		lfr.getColumn(16).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
}
