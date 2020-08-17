package pres.auxiliary.work.selenium.tool;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import pres.auxiliary.tool.file.excel.AbstractWriteExcel;
import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>ExcelRecord.java</p>
 * <p><b>用途：</b>
 * 用于记录自动化测试的运行过程，可记录用例步骤、结果、浏览器信息等，并在记录失败时提供自动截图。
 * </p>
 * <p><b>编码时间：</b>2020年8月12日下午2:16:55</p>
 * <p><b>修改时间：</b>2020年8月12日下午2:16:55</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class ExcelRecord extends AbstractWriteExcel<ExcelRecord> {
	/**
	 * 存储浏览器对象
	 */
	AbstractBrower brower;
	
	/**
	 * 通过
	 * @param configDocument
	 * @param tempFile
	 * @param brower
	 */
	public ExcelRecord(Document configDocument, File tempFile, AbstractBrower brower) {
		super(configDocument, tempFile);
		this.brower = brower;
	}

	/**
	 * @param configFile
	 * @param tempFile
	 * @param brower
	 * @throws DocumentException
	 */
	public ExcelRecord(File configFile, File tempFile, AbstractBrower brower) throws DocumentException {
		super(configFile, tempFile);
		this.brower = brower;
	}
	
}
