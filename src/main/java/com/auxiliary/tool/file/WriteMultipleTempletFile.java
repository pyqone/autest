package com.auxiliary.tool.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.auxiliary.tool.file.excel.ExcelFileTemplet;

/**
 * <p>
 * <b>文件名：</b>WriteMultipleTempletFile.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对多模板内容数据写入的方法，详细内容可参见父类{@link WriteTempletFile}说明
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月31日下午8:50:35
 * </p>
 * <p>
 * <b>修改时间：</b>2021年8月27日下午7:43:03
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public abstract class WriteMultipleTempletFile<T extends WriteMultipleTempletFile<T>> extends WriteTempletFile<T>
		implements WriteFilePage {
	/**
	 * 存储每个模板的数据
	 */
	protected HashMap<String, WriteFileData> dataMap = new HashMap<>();

	/**
	 * 记录模板的默认名称
	 */
	protected final String DEFAULT_NAME = "Temp";

	/**
	 * 构造对象，初始化创建文件的模板
	 * 
	 * @param templetName 模板名称
	 * @param templet     模板类对象
	 */
	public WriteMultipleTempletFile(String templetName, FileTemplet templet) {
		addTemplet(templetName, templet);
	}

	protected WriteMultipleTempletFile() {
		super();
	}

	@Override
	public void switchPage(String name) {
		// 判断名称是否为空、存在
		if (Optional.ofNullable(name).filter(n -> !n.isEmpty()).filter(dataMap::containsKey).isPresent()) {
			this.data = dataMap.get(name);
		}
	}

	@Override
	public void setWriteData(WriteFileData data) {
		super.setWriteData(data);
	}

	@Override
	public FileTemplet getTemplet(String name) {
		return dataMap.get(name).getTemplet();
	}
	
	@Override
	public WriteFileData getWriteFileData(String name) {
		return dataMap.get(name);
	}

	/**
	 * 添加Sheet页模板，并设置模板的名称
	 * <p>
	 * 每一个模板表示写入Excel时的一个Sheet页，其模板的名称即为Sheet页的名称。若重复添加同一个名称，则会覆盖上一次设置的模板。
	 * </p>
	 */
	@Override
	public void addTemplet(String name, FileTemplet templet) {
		// 初始化模板信息
		WriteFilePage.super.addTemplet(name, templet);
		dataMap.put(name, new WriteFileData(templet));

		// 切换至当前模板
		switchPage(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T end(int contentIndex) {
        String nowTempName = data.getTempName();
		for (String tempName : dataMap.keySet()) {
			data = dataMap.get(tempName);
			super.end(contentIndex);
			// TODO 该行代码可能冗余，但我暂时无法测试
			dataMap.get(tempName).setNowCaseNum(data.getNowCaseNum());
		}
		
        data = dataMap.get(nowTempName);
		return (T) this;
	}

	@Override
	public void write() {
		dataMap.forEach((name, data) -> {
			// 若分页行数不为0，则获取当前行数作为编写的起始行数
			int startIndex = 0;
			if (writeRowNum != 0) {
				startIndex = data.getNowCaseNum();
			}

			write(data.getTemplet(), startIndex, -1);
		});
	}

	@Override
	public void write(FileTemplet templet, int caseStartIndex, int caseEndIndex) {
		if (!isExistTemplet(new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString()), templet)) {
			createTempletFile(templet);
		}

		// 计算真实的起始下标与结束下标
		JSONArray contentListJson = dataMap.get(templet.getTempletAttribute(ExcelFileTemplet.KEY_NAME)).getContentJson()
				.getJSONArray(KEY_CASE);

		// 判断内容json是否为空，为空则不进行处理
		if (contentListJson.isEmpty()) {
			return;
		}

		int newCaseEndIndex = analysisIndex(contentListJson.size(), caseEndIndex, true);
		int newCaseStartIndex = writeRowNum == 0 ? analysisIndex(newCaseEndIndex, caseStartIndex, true)
				: dataMap.get(templet.getTempletAttribute(ExcelFileTemplet.KEY_NAME)).getNowCaseNum();

		// 将文件内容写入模板文件
		contentWriteTemplet(templet, newCaseStartIndex, newCaseEndIndex);
	}

	@Override
	protected List<String> getAllTempletJson() {
		return dataMap.values().stream().map(WriteFileData::getTemplet).map(FileTemplet::getTempletJson)
				.collect(Collectors.toList());
	}

	/**
	 * 用于判断当前模板是否存在于模板文件中
	 * <p>
	 * 该方法用于作为是否需要创建模板文件的依据
	 * </p>
	 * 
	 * @param templetFile 模板文件对象
	 * @param templet     模板对象
	 * @return 模板对象是否存在于模板文件中
	 */
	protected abstract boolean isExistTemplet(File templetFile, FileTemplet templet);

	/**
	 * 用于返回当前的数据集合
	 * 
	 * @return 数据集合
	 */
	protected HashMap<String, WriteFileData> getDataMap() {
		return dataMap;
	}
}
