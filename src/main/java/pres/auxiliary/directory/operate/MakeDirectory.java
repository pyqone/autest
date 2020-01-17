package pres.auxiliary.directory.operate;

import java.io.File;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.directory.processor.directoryprocessor.impprocessorinterface.AddSeparatorProcessor;
import pres.auxiliary.directory.processor.directoryprocessor.impprocessorinterface.FormatSeparatorProcessor;
import pres.auxiliary.directory.processor.directoryprocessor.impprocessorinterface.RelativeToAbsoluteProcessor;
import pres.auxiliary.directory.processor.directoryprocessor.processorchain.ProcessorChain;

/**
 * 该类为创建文件工具类，通过该类可辅助创建文件
 * 
 * @author Administrator
 * 
 */
public class MakeDirectory {
	// 该属性用于存储文件的路径字符串
	private static StringBuilder savePath = new StringBuilder("C:\\AutoTest\\");

	/**
	 * 私有构造，禁止创建对象
	 */
	private MakeDirectory() {
	}

	/**
	 * 该方法用于返回文件定位的路径
	 * 
	 * @return 返回在类中存储的文件路径
	 */
	public static String getSavePath() {
		return savePath.toString();
	}

	/**
	 * 用于设置文件夹的根路径
	 * 
	 * @param savePath
	 *            传入的新文件夹目录
	 */
	public static void setSavePath(String savePath) {
		// 将传入的参数进行格式化
		StringBuilder sb = new StringBuilder(savePath);
		sb = formatPath(sb);

		// 判断格式化后的文件路径格式是否符合windonws下文件路径的命名规则,不符合则抛出异常
		if (!isPath(sb.toString())) {
			System.out.println(sb.toString());
			throw new IncorrectDirectoryException("不合理的文件夹路径，文件路径："
					+ sb.toString());
		}

		// 判断传入的路径是否只是硬盘的根目录，若是，则加上AutoTest文件夹
		if (isDisk(sb.toString())) {
			// 判断传入的硬盘根目录中是否包含文件夹分隔符“\\”，若不包含，则在后面加上
			if (sb.indexOf("\\") < 0) {
				sb.append("\\");
			}
			// 补上AutoTest文件夹
			sb.append("AutoTest\\");
		}

		// 将文件路径设置入属性中
		MakeDirectory.savePath = sb;
	}

	/**
	 * 该方法用于判断传入的路径输入是否正确
	 * 
	 * @param path
	 *            传入的文件路径
	 * @return 返回路径是否正确
	 */
	public static boolean isPath(String path) {
		// 判断path是否为空，为空则返回false
		if (path == null) {
			return false;
		}

		// 判断path的内容是否为空，为空则返回false
		if ("".equals(path)) {
			return false;
		}

		// 判断path中的内容是否为满足路径的要求，不满足则返回false
		// 可匹配相对路径和绝对路径
		// 正则中只检测windows下创建文件夹或文件时不允许输入的字符
		if (!path.matches("^([A-z]:\\\\)?([^ :\\*\\?<\">|\\/\\\\]+?(\\\\)?)*$")) {
			return false;
		}

		// 若能满足上述条件，则返回true
		return true;
	}

	/**
	 * 该方法用于判断传入的参数是否符合在windows下文件名的命名规则（不判断后缀名）
	 * 
	 * @param fileName
	 *            传入的待判断的文件夹名称
	 * @return 返回判断的结果
	 */
	public static boolean isFileName(String fileName) {
		// 判断path是否为空，为空则返回false
		if (fileName == null) {
			return false;
		}

		// 判断path的内容是否为空，为空则返回false
		if ("".equals(fileName)) {
			return false;
		}
		// 通过正则判断文件名中是否包含windows不允许输入的特殊字符，并匹配在传入的文件名中是否至少包含一个“.”，若符合正则，则返回true
		if (fileName.matches("^([^:\\*\\?<\">|\\/\\\\])+?$")) {
			return true;
		}

		// 若未通过判断，则返回false
		return false;
	}

	/**
	 * 该方法用于判断路径是否是硬盘的根目录
	 * 
	 * @param path
	 *            传入的路径
	 * @return 返回判断的结果
	 */
	public static boolean isDisk(String path) {
		// 判断path是否为空，为空则返回false
		if (path == null) {
			return false;
		}

		// 判断path的内容是否为空，为空则返回false
		if ("".equals(path)) {
			return false;
		}
		// 匹配只有盘符时的正则
		if (path.matches("^[A-z]:(\\\\)?$")) {
			return true;
		}

		// 若未通过判断，则返回false
		return false;
	}

	/**
	 * 该方法将对传入文件路径进行三种处理
	 * <ol>
	 * <li>将所有的分隔符“/”改为“\\”</li>
	 * <li>末尾未添加分隔符的加上“\\”</li>
	 * <li>将传入的相对路径改为绝对路径</li>
	 * </ol>
	 * 
	 * @param path
	 *            传入的文件路径，为StringBuilder对象
	 * @return 返回格式化后的路径，为StringBuilder对象
	 */
	public static StringBuilder formatPath(StringBuilder path) {
		// 创建处理链
		ProcessorChain pc = new ProcessorChain();
		// 添加处理器
		pc.add(new FormatSeparatorProcessor());
		pc.add(new RelativeToAbsoluteProcessor());
		pc.add(new AddSeparatorProcessor());

		// 返回格式化后的路径
		return pc.doProcessor(path, pc);
	}

	/**
	 * 该方法用于创建使用辅助包时需要创建的文件夹
	 */
	public static void createAllFolder() {
		// TODO 创建日志文件
		// 创建文件夹，先判断文件夹是否已被创建，未被创建，则创建文件夹
		// 创建根文件夹
		File f = new File(savePath.toString());
		if (!f.exists()) {
			f.mkdir();
		}

		// 创建存放测试报告的文件夹
		f = new File(savePath.toString() + "Report");
		if (!f.exists()) {
			f.mkdir();
		}

		// 创建存放Bug汇总表的文件夹
		f = new File(savePath.toString() + "BugSummarySheet");
		if (!f.exists()) {
			f.mkdir();
		}

		// 创建存放自动化运行结果的文件夹
		f = new File(savePath.toString() + "Selenium");
		if (!f.exists()) {
			f.mkdir();
		}

		// 创建存放自动化运行结果截图的文件夹
		f = new File(savePath.toString() + "Selenium\\Screenshot");
		if (!f.exists()) {
			f.mkdir();
		}

		// 创建存放自动化运行结果日志的文件夹
		f = new File(savePath.toString() + "Selenium\\Log");
		if (!f.exists()) {
			f.mkdir();
		}

		// 创建存放待测页面定位方式的xml文件的文件夹
		f = new File(savePath.toString() + "Xml");
		if (!f.exists()) {
			f.mkdir();
		}

		// 创建存放生成的测试用例文件的文件夹
		f = new File(savePath.toString() + "Case");
		if (!f.exists()) {
			f.mkdir();
		}
		
		// 创建存放浏览器下载的文件的文件夹
		f = new File(savePath.toString() + "BrowersDownload");
		if (!f.exists()) {
			f.mkdir();
		}
	}
}
