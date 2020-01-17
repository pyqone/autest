package pres.auxiliary.report;

import java.io.File;
import java.io.IOException;

/**
 * 该接口定义了测试报告类应实现的方法
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public interface ReportInter {
	/**
	 * 该方法用于生成测试报告的参数
	 * 
	 * @param name
	 *            项目名称
	 * @param version
	 *            项目版本号
	 * @param testDay
	 *            测试的天数
	 * @param person
	 *            测试人员
	 * @param range
	 *            测试的范围
	 * @param one
	 *            一级Bug的数量
	 * @param two
	 *            二级Bug的数量
	 * @param three
	 *            三级Bug的数量
	 * @param four
	 *            四级Bug的数量
	 * @param noSulveOne
	 *            原版本中未解决的一级Bug的数量
	 * @param noSulveTwo
	 *            原版本中未解决的二级Bug的数量
	 * @param delayOne 原版本中延期解决的一级Bug的数量
	 * @param delayTwo 原版本中延期解决的二级Bug的数量
	 * @param accessory
	 *            附件存放位置
	 * @param activeOne
	 *            激活的一级BUG的数量
	 * @param activeTwo
	 *            激活的二级BUG的数量
	 * @param activeThree
	 *            激活的三级BUG的数量
	 * @param activeFour
	 *            激活的四级BUG的数量
	 * @return 测试报告邮件中正文的内容
	 * 
	 * @see #createReport(File, int, String, String)
	 * 
	 * @throws IOException
	 */
	public abstract String createReport(String name, String version,
			int testDay, String person, String range, int activeOne,
			int activeTwo, int activeThree, int activeFour, int one, int two,
			int three, int four, int noSulveOne,
			int noSulveTwo, int delayOne, int delayTwo, String accessory)
					throws IOException;

	/**
	 * 该方法用于通过读取BUG汇总表中的信息来生成测试报告
	 * 
	 * @param excel
	 *            BUG汇总表文件
	 * @param testDay
	 *            测试的天数
	 * @param person
	 *            测试人员
	 * @param range
	 *            测试范围
	 * @return 测试报告邮件中正文的内容
	 * @see #createReport(String, String, int, String, String, int, int, int,
	 *      int, String)
	 * @throws IOException
	 */
	public abstract String createReport(File excel, int testDay, String person,
			String range) throws IOException;

	/**
	 * 该方法用于读取Bug汇总表，其返回的数组存储的信息如下表所示：<br/>
	 * <table border="1">
	 * <tr>
	 * <td>s[0]:项目名称</td>
	 * <td>s[1]:当前版本</td>
	 * <td>s[2]:1级BUG数量</td>
	 * <td>s[3]:2级BUG数量</td>
	 * </tr>
	 * <tr>
	 * <td>s[4]:3级BUG数量</td>
	 * <td>s[5]:4级BUG数量</td>
	 * <td>s[6]:激活1级BUG数量</td>
	 * <td>s[7]:激活2级BUG数量</td>
	 * </tr>
	 * <tr>
	 * <td>s[8]:激活3级BUG数量</td>
	 * <td>s[9]:激活4级BUG数量</td>
	 * <td>s[10]:未解决的1级BUG数量</td>
	 * <td>s[11]:未解决的2级BUG数量</td>
	 * </tr>
	 * <tr>
	 * <td>s[12]:延期解决1级BUG数量</td>
	 * <td>s[13]:延期解决2级BUG数量</td>
	 * </tr>
	 * </table>
	 * 
	 * @param excel
	 *            BUG汇总表
	 * @param testDay
	 *            测试的天数
	 * @return Bug汇总表中提取的信息
	 * @throws IOException
	 */
	public abstract String[] readFile(File excel, int testDay)
			throws IOException;
}