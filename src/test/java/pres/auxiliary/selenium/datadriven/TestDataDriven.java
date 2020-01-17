package pres.auxiliary.selenium.datadriven;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestDataDriven {
	DataDriven dataList = new DataDriven();
	
	@After
	public void after() {
		while(dataList.hasNext()) {
			System.out.println("*******************************");
			System.out.println("正在读取第" + (dataList.getNowIndex() + 1) + "行数据：");
			int i = 0;
			for (String data : dataList.next()) {
				System.out.println("第" + (i + 1) + "列（列名：" + dataList.getReadListName()[i++] + "）数据：" + data);
			}
		}
		System.out.println("*******************************");
		System.out.println("整体数据：");
		System.out.println("[" + dataList.toString() + "]");
	}
	
	@Before
	public void readFile() throws DataNotFoundException {
//		dataList.setEmptyCell(true);
//		test_04_ReadTxtFile();
//		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test2.txt"), true);
		
		/*
		for ( int i = 0; i < dataList.getColumnLength(); i++ ) {
			dataList.replaceListName(i, ("测试数据第" + (i + 1) + "列"));
		} 
		*/ 
	}
	
	@Ignore
	@Test
	public void test_01_ReadCsvFile() throws DataNotFoundException {
		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test.csv"), true);
	}
	
	@Ignore
	@Test
	public void test_02_ReadXlsxFile() throws DataNotFoundException {
		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test.xlsx"), true);
	}
	
	@Ignore
	@Test
	public void test_03_ReadXlsFile() throws DataNotFoundException {
		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test.xls"), true);
	}
	
	@Ignore
	@Test
	public void test_04_ReadTxtFile() throws DataNotFoundException {
		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test.txt"), true);
	}
	
	@Ignore
	@Test
	public void test_05_setEmptyCell() throws DataNotFoundException {
		dataList.setEmptyCell(false);
		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test.xls"), true);
	}
	
	@Ignore
	@Test
	public void test_06_next() {
		int index = 0;
		while(dataList.hasNext()) {
			System.out.println("*******************************");
			System.out.println("正在读取第" + (index + 1) + "行数据：");
			int i = 0;
			for (String data : dataList.next()) {
				System.out.println("第" + (i + 1) + "列（列名：" + dataList.getListName(i++) + "）数据：" + data);
			}
			
			index++;
		}
	}
	
	@Test
	public void test_07_setReadListName() {
		dataList.setReadListName("测试1", "测试3");
	}
	
	@Test
	public void test_08_setReadListId() {
		dataList.setReadListId(1, 2);
	}
	
	@Test
	public void test_09_readFiles() throws DataNotFoundException {
		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test2.txt"), true);
		test_01_ReadCsvFile();
		test_02_ReadXlsxFile();
		test_03_ReadXlsFile();
		test_04_ReadTxtFile();
		dataList.setReadListName("测试1", "xls1", "测试4", "csv1", "xlsx1");
	}
	
	@Test
	public void test_10_setNowIndex() {
		dataList.setNowIndex(3);
	}
	
	@Test
	public void test_11_getRowAndsetReadAllList() {
		dataList.setReadListId(1, 3, 5);
		int index = 1;
		for (String data : dataList.getRow(2)) {
			System.out.println("第" + (index++) + "行数据：" + data);
		}
		dataList.setReadAllList();
	}
	
	@Test
	public void test_12_getColumn() {
		int index = 1;
		for (String data : dataList.getColumn(3)) {
			System.out.println("第" + (index++) + "行数据：" + data);
		}
		System.out.println("******************************");
		index = 1;
		for (String data : dataList.getColumn("测试2")) {
			System.out.println("第" + (index++) + "行数据：" + data);
		}
		dataList.setReadAllList();
	}
	
	@Test
	public void test_13_saveData() throws Exception {
		dataList.setEmptyCell(false);
		dataList.addDataFile(new File("src/test/java/pres/auxiliary/selenium/datadriven/Test2.txt"), true);
		test_01_ReadCsvFile();
		test_02_ReadXlsxFile();
		test_03_ReadXlsFile();
		test_04_ReadTxtFile();
		
		test_01_ReadCsvFile();
		test_02_ReadXlsxFile();
		test_03_ReadXlsFile();
		test_04_ReadTxtFile();
		
		dataList.saveData(new File("D:\\测试\\test.txt"));
		dataList.saveData(new File("D:\\测试\\test.csv"));
		dataList.saveData(new File("D:\\测试\\test.xls"));
		dataList.saveData(new File("D:\\测试\\test.xlsx"));
	}
}
