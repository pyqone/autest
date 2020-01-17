package pres.auxiliary.report;

/**
 * 该类用于创建报告文件对象的类型，传入“TestReport”表示测试报告，传入“AutoTestPath”表示自动化测试报告
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class ReportFactory {
	/**
	 * 该方法用于通过带参构造的方法创建报告类对象
	 * 
	 * @param report
	 *            报告的类型
	 * @param savePath
	 *            报告的输出位置
	 * @return 报告类对象
	 */
	public static AbstractReport newInstance(String report, String savePath) {
		if (report.equalsIgnoreCase("TestReport")) {
			return new TestReport(savePath);
		} else if (report.equalsIgnoreCase("AutoTestPath")) {
			return new AutoTestReport(savePath);
		} else {
			return null;
		}
	}

	/**
	 * 该方法用于通过无参构造的方法创建报告类对象
	 * 
	 * @param report
	 *            报告的类型
	 * @return 报告类对象
	 */
	public static AbstractReport newInstance(String report) {
		if (report.equalsIgnoreCase("TestReport")) {
			return new TestReport();
		} else if (report.equalsIgnoreCase("AutoTestPath")) {
			return new AutoTestReport();
		} else {
			return null;
		}
	}
}
