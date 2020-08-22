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
	
	/**
	 * 将xml文件中编写的文档内部超链接内容进行转换
	 * @param linkContent 链接内容
	 * @return 转换后的在poi使用的超链接代码
	 */
	private String getDocumentLinkPath(String linkContent) {
		String[] linkContents = linkContent.split("|");
		int length = linkContents.length;
		
		//获取超链接的sheet名称
		String linkSheetName = linkContents[0];
		//根据sheet名称以及字段id，获取该字段在sheet中的列数字下标，并将数字下标转换为英文下标
		String linkColumnIndex = num2CharIndex(getColumnNumIndex(linkSheetName, linkContents[1]));
		
		String linkRowIndex = "";
		//获取当前表格的最后一行元素个数
		int lastIndex = xw.getSheet(linkSheetName).getLastRowNum();
		//判断当前linkContents是否存在链接行数（即第三个元素）且链接的文本为数字
		//若符合规则，则将linkRowIndex设置为当前编写的内容
		//若不符合规则，则将linkRowIndex设置为当前sheet的最后一行
		if (length > 2) {
			linkRowIndex = String.valueOf(getPoiIndex(lastIndex + 1, Integer.valueOf(linkContents[2])));
		} else {
			linkRowIndex = String.valueOf(lastIndex);
		}
		
		//返回文档链接的内容
		return "'" + nowSheetName + "'!" + linkColumnIndex + linkRowIndex;
	}
}
