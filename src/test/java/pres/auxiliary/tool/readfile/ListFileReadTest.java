package pres.auxiliary.tool.readfile;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ListFileReadTest {
	ListFileRead lfr;
	
//	@BeforeTest
//	public void start() throws IOException {
//		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.csv"));
//	}
	
	@AfterMethod
	public void showColumn() {
		lfr.getColumn(0).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Csv() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.csv"));
		lfr.getColumn(4).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xls() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.xls"));
		lfr.getColumn(4).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xls_String() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.xls"), "Sheet2");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xlsx() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.xlsx"));
		lfr.getColumn(4).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xlsx_String() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.xlsx"), "Sheet2");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Txt() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.txt"));
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Txt_String() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.txt"), "\\t");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Doc() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.doc"));
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Doc_String() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.doc"), "\\t");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_docx() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.docx"));
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_docx_String() throws IOException {
		lfr = new ListFileRead(new File("src/test/java/pres/auxiliary/tool/readfile/模块信息.docx"), "\\t");
	}

	@Test
	public void tableTranspositionTest() {
		lfr.tableTransposition();
		lfr.getColumn(1).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
}
