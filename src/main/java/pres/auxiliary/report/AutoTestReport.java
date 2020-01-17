package pres.auxiliary.report;

import java.io.File;
import java.io.IOException;

public class AutoTestReport extends AbstractReport implements ReportInter {
	/**
	 * 该构造只用于创建对象
	 */
	public AutoTestReport() {
	}

	/**
	 * 该构造方法用于设置测试报告保存位置
	 * 
	 * @param savePath
	 *            测试报告保存位置
	 */
	public AutoTestReport(String savePath) {
		setSavePath(savePath);
	}

	@Override
	public String createReport(File excel, int testDay, String person,
			String range) throws IOException {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String createReport(String name, String version, int testDay, String person, String range, int activeOne,
			int activeTwo, int activeThree, int activeFour, int one, int two, int three, int four, int noSulveOne,
			int noSulveTwo, int delayOne, int delayTwo, String accessory) throws IOException {
		// TODO 自动生成的方法存根
		return null;
	}

	/*
	 * @Override public void createReport(File folder, int[] testDay, String[]
	 * person, String[] range) throws IOException { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 */
}
