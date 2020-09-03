package pres.auxiliary.work.selenium.tool;

import java.io.File;

/**
 * <p><b>文件名：</b>RunLog.java</p>
 * <p><b>用途：</b>
 * 以纯文本的形式记录自动化运行过程
 * </p>
 * <p><b>编码时间：</b>2020年9月3日上午8:02:37</p>
 * <p><b>修改时间：</b>2020年9月3日上午8:02:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class RunLog {
	/**
	 * 指向日志文件类对象
	 */
	private File logFile = null;
	
	public RunLog() {
	}
	
	/**
	 * 构造对象，并指定日志存放位置
	 * @param logFile 日志文件类对象
	 */
	public RunLog(File logFile) {
		this.logFile = logFile;
	}
}
