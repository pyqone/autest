package com.auxiliary.tool.file;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * <b>文件名：</b>WriteMultipleTempletFile.java
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月31日下午8:50:35
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月31日下午8:50:35
 * </p>
 * 
 * @author
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T>
 */
public abstract class WriteMultipleTempletFile<T extends WriteMultipleTempletFile<T>> extends WriteTempletFile<T>
		implements WriteFilePage {
	/**
	 * 存储所有的模板
	 */
	protected LinkedHashMap<String, FileTemplet> templetMap = new LinkedHashMap<>();
	/**
	 * 存储模板名称对应的内容json
	 */
	protected LinkedHashMap<String, JSONObject> contentMap = new LinkedHashMap<>();
	/**
	 * 存储默认字段数据json
	 */
	protected LinkedHashMap<String, JSONObject> defaultMap = new LinkedHashMap<>();

	public WriteMultipleTempletFile(String templetName, FileTemplet templet) {
		super(templet);
		addTemplet(templetName, templet);
	}

	public WriteMultipleTempletFile(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
	}

	protected WriteMultipleTempletFile() {
		super();
	}
	
	@Override
	public void switchPage(String name) {
		// 判断名称是否为空、存在
		if (Optional.ofNullable(name).filter(n -> !n.isEmpty()).filter(templetMap::containsKey).isPresent()) {
			this.templet = templetMap.get(name);
			this.contentJson = contentMap.get(name);
			this.defaultCaseJson = defaultMap.get(name);
		}
	}

	@Override
	public FileTemplet getTemplet(String name) {
		return templetMap.get(name);
	}
	
	/**
	 * 添加Sheet页模板，并设置模板的名称
	 * <p>
	 * 每一个模板表示写入Excel时的一个Sheet页，其模板的名称即为Sheet页的名称。若重复添加同一个名称，则会覆盖上一次设置的模板。
	 * </p>
	 */
	@Override
	public void addTemplet(String name, FileTemplet templet) {
		WriteFilePage.super.addTemplet(name, templet);
		templetMap.put(name, new FileTemplet(templet.getTempletJson()));
		
		JSONObject contentJson = new JSONObject();
		contentJson.put(KEY_CASE, new JSONArray());
		contentMap.put(name, contentJson);
		
		switchPage(name);
	}

	@Override
	public void write(int caseStartIndex, int caseEndIndex) {
		// 遍历模板，判断模板是否存在，不存在，则进行创建
		for (Entry<String, FileTemplet> entry : templetMap.entrySet()) {
			FileTemplet templet = entry.getValue();
			if (!isExistTemplet(new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString()), templet)) {
				createTempletFile(templet);
			}
			
			// 计算真实的起始下标与结束下标
			caseEndIndex = analysisIndex(contentJson.getJSONArray(KEY_CASE).size(), caseEndIndex, true);
			caseStartIndex = analysisIndex(caseEndIndex, caseStartIndex, true);
			
			// 判断两个下标是否相等，相等，则不进行处理
			if (caseEndIndex == caseStartIndex) {
				continue;
			}
			//将文件内容写入模板文件
			contentWriteTemplet(templet, caseStartIndex, caseEndIndex);
		}
	}

	@Override
	protected List<String> getAllTempletJson() {
		return templetMap.values().stream().map(FileTemplet::getTempletJson).collect(Collectors.toList());
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
}
