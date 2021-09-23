package com.auxiliary.tool.file.excel;

import java.io.File;
import java.util.List;

import org.dom4j.Document;

import com.auxiliary.tool.data.TableData;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.WriteTempletFile;

/**
 * <p>
 * <b>文件名：</b>WriteBasicExcelTempletFile.java
 * </p>
 * <p>
 * <b>用途：</b> 提供用于对excel数据型的内容进行编写的方法，详细内容可参见父类{@link WriteTempletFile}说明
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月13日下午3:33:31
 * </p>
 * <p>
 * <b>修改时间：</b>2021年6月13日下午3:33:31
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class WriteBasicExcelTempletFile extends WriteExcelTempletFile<WriteBasicExcelTempletFile> {
	/**
	 * 通过模板配置xml文件对文件写入类进行构造
	 * <p>
	 * 通过该方法构造的写入类为包含模板的写入类，可直接按照字段编写文件内容
	 * </p>
	 * 
	 * @param templetXml 模板配置文件
	 * @param saveFile   文件保存路径
	 */
	public WriteBasicExcelTempletFile(Document templetXml, File saveFile) {
		super(templetXml, saveFile);
	}

	/**
	 * 构造Excel写入类，并设置一个Sheet页的模板及相应的名称
	 * 
	 * @param templetName 模板名称
	 * @param templet     模板类
	 */
	public WriteBasicExcelTempletFile(String templetName, FileTemplet templet) {
		super(templetName, templet);
	}
	
	/**
	 * 
	 * @param saveFile
	 * @param data
	 * @return
	 */
	public static File writeTabelData(File saveFile, TableData<String> data) {
		// 设置模板
		ExcelFileTemplet templet = new ExcelFileTemplet(saveFile);
		// 读取表格数据中的字段并写入到模板的字段中，作为模板中的标题
		List<String> titleList = data.getColumnName();
		titleList.forEach(templet::addField);
		
		// 构造模板
		WriteBasicExcelTempletFile excel = new WriteBasicExcelTempletFile("默认模板", templet);
		// 获取行下标
		data.setExamine(false);
		for (int index = 0; index < data.getLongColumnSize(); index++) {
			// 读取列名称，根据列名称，写入数据
			for (String name : titleList) {
				excel.addContent(name, data.getSingleData(name, index).orElse(""));
			}
			excel.end();
		}
		
		excel.write();
		return saveFile;
	}
}
