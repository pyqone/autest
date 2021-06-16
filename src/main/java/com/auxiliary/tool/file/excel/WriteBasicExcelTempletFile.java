package com.auxiliary.tool.file.excel;

import java.io.File;

import org.dom4j.Document;

import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.WriteFileException;
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
	 * 根据已有的写入类对象，构造新的写入类对象，并保存原写入类对象中的模板、内容、字段默认内容以及词语替换内容
	 * <p>
	 * <b>注意：</b>在转换模板时，若模板的name字段为对象，则以默认名称“Sheet + 序号”来命名，并修改其中的name字段值
	 * </p>
	 * 
	 * @param writeTempletFile 文件写入类对象
	 * @throws WriteFileException 文件写入类对象为空时，抛出的异常
	 */
	public WriteBasicExcelTempletFile(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
	}

}
