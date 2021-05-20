package com.auxiliary.tool.file.excel;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.MarkColorsType;
import com.auxiliary.tool.file.MarkComment;
import com.auxiliary.tool.file.MarkFieldBackground;
import com.auxiliary.tool.file.MarkTextColor;
import com.auxiliary.tool.file.MarkTextFont;
import com.auxiliary.tool.file.MarkTextLink;
import com.auxiliary.tool.file.WriteFileException;
import com.auxiliary.tool.file.WriteFilePage;
import com.auxiliary.tool.file.WriteTempletFile;

public class WriteExcelTempletFile<T extends WriteExcelTempletFile<T>>
		extends WriteTempletFile<WriteExcelTempletFile<T>> implements MarkComment<WriteExcelTempletFile<T>>,
		MarkFieldBackground<WriteExcelTempletFile<T>>, MarkTextColor<WriteExcelTempletFile<T>>,
		MarkTextFont<WriteExcelTempletFile<T>>, MarkTextLink<WriteExcelTempletFile<T>>, WriteFilePage {
	/**
	 * 存储每个Sheet对应的模板
	 */
	HashMap<String, FileTemplet> sheetTempletMap = new HashMap<>();

	/**
	 * 构造Excel写入类，并设置一个Sheet页的模板及相应的名称
	 * @param templetName 模板名称
	 * @param templet 模板类
	 */
	public WriteExcelTempletFile(String templetName, FileTemplet templet) {
		super(templet);
		addTempletName(templetName, templet);
	}

	/**
	 * 根据已有的写入类对象，构造新的写入类对象，并保存原写入类对象中的模板、内容、字段默认内容以及词语替换内容
	 * @param writeTempletFile 文件写入类对象
	 * @throws WriteFileException 文件写入类对象为空时，抛出的异常
	 */
	public WriteExcelTempletFile(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
		
		// 拉取WriteTempletFile类的模板内容
		JSONArray tempJsonList = JSONObject.parseObject(writeTempletFile.toTempletJson()).getJSONArray(KEY_TEMPLET);
		// 循环，读取所有的模板，并对模板进行转换
		for (int i = 0; i < tempJsonList.size(); i++) {
			FileTemplet temp = FileTemplet.parse(tempJsonList.getJSONObject(i).toJSONString());
			
			// 判断模板是否包含"name"属性，不包含，则加入默认名称
			if (!temp.containsAttribute(KEY_NAME)) {
				String name = "Sheet" + (i + 1);
				temp.addTempletAttribute(KEY_NAME, name);
				sheetTempletMap.put(name, temp);
			} else {
				sheetTempletMap.put(temp.getTempletAttribute(KEY_NAME).toString(), temp);
			}
		}
	}

	@Override
	public void write(int caseStartIndex, int caseEndIndex) {
	}

	@Override
	public void switchPage(String name) {
		
	}
	
	@Override
	public void renamePage(String oldName, String newName) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 添加Sheet页模板，并设置模板的名称
	 * <p>
	 * 每一个模板表示写入Excel时的一个Sheet页，其模板的名称即为Sheet页的名称。若重复添加同一个名称，则会覆盖上一次设置的模板。
	 * </p>
	 */
	@Override
	public void addTempletName(String name, FileTemplet templet) {
		WriteFilePage.super.addTempletName(name, templet);
		sheetTempletMap.put(name, templet);
	}

	@Override
	public WriteExcelTempletFile<T> textLink(String field, int textIndex, String likeContent) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> fieldLink(String field, String likeContent) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> bold(String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> italic(String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> underline(String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> changeTextColor(MarkColorsType markColorsType, String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> changeCaseBackground(MarkColorsType markColorsType) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> changeFieldBackground(String field, MarkColorsType markColorsType) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> fieldComment(String field, String commentText) {
		return null;
	}

	@Override
	protected List<String> getAllTempletJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
