package pres.auxiliary.tool.readfile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ListFileReadTest {
	ListFileRead lfr;
	
	@BeforeTest
	public void start() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.txt"), "\\t");
	}
	
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
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.csv"));
		lfr.getColumn(4).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xls() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.xls"));
		lfr.getColumn(4).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xls_String() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.xls"), "Sheet2");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xlsx() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.xlsx"));
		lfr.getColumn(4).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Xlsx_String() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.xlsx"), "Sheet2");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Txt() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.txt"));
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Txt_String() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.txt"), "\\t");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Doc() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.doc"));
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_Doc_String() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.doc"), "\\t");
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_docx() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.docx"));
	}
	
	/**
	 * 测试构造方法
	 * @throws IOException 
	 */
	@Test
	public void listFileReadTest_docx_String() throws IOException {
		lfr = new ListFileRead(new File("Test/ListDataFile/模块信息.docx"), "\\t");
	}
	
	/**
	 * 测试{@link ListFileRead#getColumn(int, int, int)}
	 */
	@Test
	public void getColumnTest() {
		lfr.getColumn(2, 3, 6).forEach(System.out :: println);
		System.out.println("------------------------------------");
		lfr.getColumn(2, 4, 0).forEach(System.out :: println);
		System.out.println("------------------------------------");
		lfr.getColumn(2, 3, 3).forEach(System.out :: println);
		System.out.println("------------------------------------");
		lfr.getColumn(99, 99, 99).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
	
	/**
	 * 测试{@link ListFileRead#getTable()}
	 */
	@Test
	public void getTableTest() {
		AtomicInteger commitCount = new AtomicInteger(1);
		lfr.getTable().forEach(textList -> {
			System.out.println("第" + commitCount.getAndIncrement() + "列：");
			textList.forEach(System.out :: println);
		});
		System.out.println("------------------------------------");
	}

	/**
	 * 测试{@link ListFileRead#getTable(int, int, int, int)}
	 */
	@Test
	public void getTableTest_IntIntIntInt() {
		AtomicInteger commitCount = new AtomicInteger(1);
		lfr.getTable(0, 2, 3, 6).forEach(textList -> {
			System.out.println("第" + commitCount.getAndIncrement() + "列：");
			textList.forEach(System.out :: println);
		});
		System.out.println("------------------------------------");
		commitCount.set(1);
		lfr.getTable(2, 0, 4, 0).forEach(textList -> {
			System.out.println("第" + commitCount.getAndIncrement() + "列：");
			textList.forEach(System.out :: println);
		});
		System.out.println("------------------------------------");
		commitCount.set(1);
		lfr.getTable(2, 2, 4, 4).forEach(textList -> {
			System.out.println("第" + commitCount.getAndIncrement() + "列：");
			textList.forEach(System.out :: println);
		});
		System.out.println("------------------------------------");
		commitCount.set(1);
		lfr.getTable(99, 99, 99, 99).forEach(textList -> {
			System.out.println("第" + commitCount.getAndIncrement() + "列：");
			textList.forEach(System.out :: println);
		});
		System.out.println("------------------------------------");
	}

	@Test
	public void tableTranspositionTest() {
		lfr.tableTransposition();
		lfr.getColumn(1).forEach(System.out :: println);
		System.out.println("------------------------------------");
	}
}
