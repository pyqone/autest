package pres.auxiliary.testcase.templet;

import org.junit.BeforeClass;
import org.junit.Test;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.work.testcase.templet.ZentaoTemplet;

public class TestZentaoTemplet {
	@BeforeClass
	public static void setPath() {
		ZentaoTemplet.setSavePath("E:\\Test\\");
		ZentaoTemplet.setFileName("测试用例测试");
		try {
			ZentaoTemplet.create();
		} catch( IncorrectDirectoryException e ) {
		}
	}
	
	@Test
	public void test1() {
		try {
			ZentaoTemplet.create();
			System.out.println("test1-------未抛出异常");
		} catch( IncorrectDirectoryException e ) {
			System.out.println("test1-------抛出异常");
		}
	}
	
	@Test 
	public void test2() {
		ZentaoTemplet.setCoverFile(false);
		try {
			ZentaoTemplet.create();
			System.out.println("test2-------未抛出异常");
		} catch( IncorrectDirectoryException e ) {
			System.out.println("test2-------抛出异常");
		}
	}
	
	@Test
	public void test3() {
		ZentaoTemplet.setCoverFile(true);
		try {
			ZentaoTemplet.create();
			System.out.println("test3-------未抛出异常");
		} catch( IncorrectDirectoryException e ) {
			System.out.println("test3-------抛出异常");
		}
	}
}
