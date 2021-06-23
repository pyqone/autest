package com.auxiliary.tool.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
 * <p>
 * <b>文件名：</b>WriteSingleTempletFile.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对单模板内容数据写入的方法，详细内容可参见父类{@link WriteTempletFile}说明
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月31日下午8:50:42
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月31日下午8:50:42
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public abstract class WriteSingleTempletFile<T extends WriteSingleTempletFile<T>> extends WriteTempletFile<T> {

	public WriteSingleTempletFile(FileTemplet templet) {
		super(templet);
	}

	public WriteSingleTempletFile(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
	}

	protected WriteSingleTempletFile() {
		super();
	}

	
	@Override
	public void write() {
		// 若分页行数不为0，则获取当前行数作为编写的起始行数
		int startIndex = 0;
		if (writeRowNum != 0) {
			startIndex = nowRowNum;
		}
		
		write(templet, startIndex, -1);
	}

	@Override
	public void write(FileTemplet templet, int caseStartIndex, int caseEndIndex) {
		// 判断模板文件是否存在，若不存在，则创建模板文件
		if (!new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString()).exists()) {
			createTempletFile(templet);
		}
		
		// 计算真实的起始下标与结束下标
		JSONArray contentListJson = contentJson.getJSONArray(KEY_CASE);
		// 判断内容json是否为空，为空则不进行处理
		if (contentListJson.isEmpty()) {
			return;
		}

		// 计算真实的起始下标与结束下标
		int newCaseEndIndex = analysisIndex(contentListJson.size(), caseEndIndex, true);
		int newCaseStartIndex = analysisIndex(newCaseEndIndex, caseStartIndex, true);

		// 将文件内容写入模板文件
		contentWriteTemplet(templet, newCaseStartIndex, newCaseEndIndex);
	}

	@Override
	protected List<String> getAllTempletJson() {
		List<String> tempList = new ArrayList<>();
		tempList.add(templet.getTempletJson());
		return tempList;
	}
}
