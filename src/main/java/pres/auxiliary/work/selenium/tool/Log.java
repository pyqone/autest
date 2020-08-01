package pres.auxiliary.work.selenium.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p><b>文件名：</b>Log.java</p>
 * <p><b>用途：</b>用于在txt文件中生成自动化测试相关的日志，亦可指定输出的内容</p>
 * <p><b>编码时间：</b>2019年6月1日上午11:34:54</p>
 * <p><b>修改时间：</b>2019年10月6日下午6:03:39</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class Log {
	/**
	 * 用于存储文件的保存路径
	 */
	private StringBuilder savePath = new StringBuilder("C:\\AutoTest\\Log\\");
	/**
	 * 用于存储的文件名称
	 */
	private StringBuilder fileName = new StringBuilder("TestResults");
	
	/**
	 * 用于存储当前正在运行的类名
	 */
	private String className = "";
	
	/**
	 * 用于存储当前正在运行的测试方法名
	 */
	private String methodName = "";
	
	/**
	 * 用于标识是否已经开始进行了记录
	 */
	private boolean startRecord = false;

	/**
	 * 用于按默认方式建文件保存位置及文件名称<br/>
	 * 默认位置为：C:\\AutoTestting\\TestResults\\<br/>
	 * 默认文件名为（不带后缀）：TestResults
	 */
	public Log() {
	}

	/**
	 * 用于按指定的路径以及默认的文件名保存测试结果文件<br/>
	 * 默认文件名为（不带后缀）：Image<br/>
	 * 注意，传入的文件路径可为相对路径，也可为绝对路径，若路径不符合windows下文件夹名称的名称规则，
	 * 则抛出IncorrectDirectoryException异常
	 * 
	 * @param savePath
	 *            指定的测试结果文件保存路径
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
	 */
	public Log(String savePath) {
		setSavePath(savePath);
	}

	/**
	 * 用于按指定的路径以及指定的文件名保存测试结果文件
	 * 
	 * @param savePath
	 *            指定的测试结果文件保存路径
	 * @param imageName
	 *            指定的测试结果文件文件名
	 * @throws IncorrectDirectoryException
	 *             传入的路径不合法或者文件名不合法时抛出的异常
	 */
	public Log(String savePath, String fileName) {
		setSavePath(savePath);
		setFileName(fileName);
	}
	
	public String getSavePath() {
		return savePath.toString();
	}

	/**
	 * 该方法用于设置测试结果文件保存的位置，可传入相对路径，也可传入绝对路径，
	 * 若传入的路径不符合windows下文件夹名称的命名规则时，则抛出IncorrectDirectoryException异常
	 * 
	 * @param savePath
	 *            传入的测试结果文件保存路径
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
	 */
	public void setSavePath(String savePath) {
		// 将传入的路径封装成StringBuilder，以便格式化
		StringBuilder sb = new StringBuilder(savePath);
		// 格式化传入的路径
		MakeDirectory.formatPath(sb);

		// 判断传入的路径是否符合windows下对文件夹名称命名的规则，如果不符合则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isPath(sb.toString())) {
			throw new IncorrectDirectoryException("不合理的文件夹路径，文件路径：" + sb.toString());
		}

		// 将通过判断的sb赋给savePath属性
		this.savePath = sb;
	}

	/**
	 * 用于返回测试结果文件的文件名称（不含后缀）
	 * 
	 * @return 测试结果文件的文件名称
	 */
	public String getFileName() {
		return fileName.toString();
	}

	/**
	 * 该方法用于设置测试结果文件的文件名称，若传入的文件名不符合windows下文件的命名规则，
	 * 则抛出IncorrectDirectoryException异常
	 * 
	 * @param fileName
	 *            指定的测试结果文件名称
	 * @throws IncorrectDirectoryException
	 *             文件命名不正确时抛出的异常
	 */
	public void setFileName(String fileName) {
		// 判断传入的测试结果文件名称是否符合windows下的命名规则，若不符合，则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isFileName(fileName)) {
			throw new IncorrectDirectoryException("不合理的文件名称，文件名称：" + fileName);
		}

		// 通过判断后，则清空fileName存储的信息并将新的文件名称放入fileName种属性中
		this.fileName.delete(0, this.fileName.length());
		this.fileName.append(fileName);
	}

	/**
	 * 该方法用于创建测试结果文件并保存到相应的路径下,通过类中存储的测试结果文件保存路径和测试结果文件文件名来创写入文件。
	 * 
	 * @param text
	 *            待写入文件的内容
	 * @throws IOException
	 *             文件流状态不正确时抛出的异常
	 * @throws UndefinedDirectoryException
	 *             测试结果文件保存路径或测试结果文件名称未指定时抛出的异常
	 */
	public void write(String text) throws IOException {
		// 判断text中的内容是否为空，为空则直接返回
		if (text == null || "".equals(text)) {
			return;
		}

		// 判断测试结果文件保存路径和测试结果文件名是否存在，若不存在则抛出UndefinedDirectoryException异常
		if ("".equals(savePath.toString()) || "".equals(fileName.toString())) {
			throw new UndefinedDirectoryException(
					"未定义文件路径或者文件名，文件路径:" + savePath.toString() + "，文件名：" + fileName.toString());
		}

		// 调用writeFile()方法，将测试内容写入文件中
		writeFile(text);
		
	}

	/**
	 * 用于标识开始进行自动化测试运行记录
	 * @param className 测试类名
	 * @param methodName 测试方法名
	 * @throws IOException 文件流状态不正确时抛出的异常
	 * @throws RecordStateException 未调用结束记录的方法时调用了此方法后抛出的异常
	 */
	public void startWrite(String className, String methodName) throws IOException {
		//当运行状态为true时，说明此次调用前未调用endWrite()方法，则抛出异常
		if (startRecord) {
			throw new RecordStateException("当前运行状态为" + startRecord + "，需要调用结束记录的方法再调用该方法");
		}
		
		this.className = className;
		this.methodName = methodName;
		
		write("正在运行" + className + "类的" + methodName + "()方法");
		
		startRecord = true;
	}

	/**
	 * 用于标识结束进行自动化测试运行记录
	 * 
	 * @param bugNumber
	 *            指定的BUG数量
	 * @throws IOException
	 *             文件流状态不正确时抛出的异常
	 * @throws RecordStateException
	 *             未调用开始记录的方法时调用了此方法后抛出的异常
	 */
	public void endWrite(int bugNumber) throws IOException {
		if (!startRecord) {
			throw new RecordStateException("当前运行状态为" + startRecord + "，需要调用开始记录的方法再调用该方法");
		}
		
		write(className + "类的" + methodName + "方法运行结束，共发现BUG" + bugNumber + "个");
		startRecord = false;
	}
	
	/**
	 * 该方法用于将用户输入的内容写入指定的文件中
	 * 
	 * @param text
	 *            指定写入的内容
	 * @throws IOException
	 *             流异常时抛出的异常
	 */
	private void writeFile(String text) throws IOException {
		// 用于存储待写入文件的内容
		StringBuilder sb = new StringBuilder("");

		// 将savePath中保存的路径作为测试结果文件保存路径创建文件夹
		new File(savePath.toString()).mkdirs();
		// 定义文件写入流，设置为不覆盖当前文本
		FileWriter fw = new FileWriter(new File(getSavePath() + getFileName() + ".txt"), true);

		// 将待写入文件的内容放入sb中
		sb.append("[");
		sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		sb.append("]");
		sb.append(text);
		sb.append("\r\n");

		// 将sb中的内容写入文件
		fw.write(sb.toString());

		// 刷新文件并关闭流
		fw.flush();
		fw.close();
	}

}
