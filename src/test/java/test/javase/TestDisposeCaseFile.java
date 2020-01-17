package test.javase;

import java.io.File;
import java.io.IOException;

import pres.auxiliary.work.testcase.templet.ZentaoTemplet;

/**
 * FileName: TestDisposeCaseFile.java
 * 
 * 用于测试处理测试用例文件方法
 * 
 * @author 彭宇琦
 * @Deta 2018年6月22日 下午5:44:01
 * @version ver1.0
 */
public class TestDisposeCaseFile {
	public static void main(String[] args) throws IOException {
		ZentaoTemplet.disposeCaseFile(new File("E:\\Case.xlsx"));
		System.out.println("The End");
	}
}
