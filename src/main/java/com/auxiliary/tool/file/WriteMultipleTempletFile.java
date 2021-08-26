package com.auxiliary.tool.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.auxiliary.tool.data.RandomString;
import com.auxiliary.tool.data.StringMode;
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
 * <b>修改时间：</b>2021年5月31日下午8:50:35
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
	 * 存储所有的模板
	 */
//	protected LinkedHashMap<String, FileTemplet> templetMap = new LinkedHashMap<>();
	/**
	 * 存储模板名称对应的内容json
	 */
//	protected LinkedHashMap<String, JSONObject> contentMap = new LinkedHashMap<>();
	/**
	 * 存储默认字段数据json
	 */
//	protected HashMap<String, JSONObject> defaultMap = new HashMap<>();
	/**
	 * 存储每个模板中自动写入文件的用例数
	 */
//	protected HashMap<String, Integer> nowRowNumMap = new HashMap<>();
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
		super(templet);
		addTemplet(templetName, templet);
	}

	/**
	 * 根据已有的写入类对象，构造新的写入类对象，并保存原写入类对象中的模板、内容、字段默认内容以及词语替换内容
	 * 
	 * @param writeTempletFile 文件写入类对象
	 * @throws WriteFileException 文件写入类对象为空时，抛出的异常
	 */
	public WriteMultipleTempletFile(WriteTempletFile<?> writeTempletFile) {
		// TODO 原有数据未被保留
		super(writeTempletFile);
		// 若模板写入类继承自多模板类，则再记录其多模板相关的内容
		if (writeTempletFile instanceof WriteMultipleTempletFile) {
//			((WriteMultipleTempletFile<?>) writeTempletFile).templetMap.forEach(this.templetMap::put);
//			((WriteMultipleTempletFile<?>) writeTempletFile).contentMap.forEach(this.contentMap::put);
//			((WriteMultipleTempletFile<?>) writeTempletFile).defaultMap.forEach(this.defaultMap::put);
			((WriteMultipleTempletFile<?>) writeTempletFile).getDataMap().forEach(dataMap::put);
		} else {
			// 获取模板
			FileTemplet temp = writeTempletFile.data.getTemplet();
			String name = "";
			// 判断模板是否包含"name"属性，不包含，则加入默认名称
			if (!temp.containsAttribute(KEY_NAME)) {
				name = DEFAULT_NAME + RandomString.randomString(3, 6, StringMode.ALL);
				temp.addTempletAttribute(KEY_NAME, name);
			} else {
				// 若模板包含name属性，则判断属性的值是否为字符串，若不是字符串，则同样随机命名
				Object obj = temp.getTempletAttribute(KEY_NAME);
				if (obj instanceof String) {
					name = temp.getTempletAttribute(KEY_NAME).toString();
				} else {
					name = DEFAULT_NAME + RandomString.randomString(3, 6, StringMode.ALL);
				}
			}
			dataMap.put(name, new WriteFileData(temp));
		}
	}

	protected WriteMultipleTempletFile() {
		super();
	}

	@Override
	public void switchPage(String name) {
		// 判断名称是否为空、存在
		if (Optional.ofNullable(name).filter(n -> !n.isEmpty()).filter(dataMap::containsKey).isPresent()) {
//			this.templet = templetMap.get(name);
//			this.contentJson = contentMap.get(name);
//			this.defaultCaseJson = defaultMap.get(name);
//			this.nowRowNum = nowRowNumMap.get(name);
			this.data = dataMap.get(name);
		}
	}

	@Override
	public void setContentJson(String writeJsonText) {
		super.setContentJson(writeJsonText);

		// 将数据回写到相应的内容中
		WriteFileData nowData = new WriteFileData(data.getTemplet());
		nowData.setContentJson(data.getContentJsonText());
		nowData.setDefaultCaseJson(data.getDefaultCaseJsonText());
		dataMap.put(data.getTempName(), nowData);
//		JSONObject writeJson = JSONObject.parseObject(writeJsonText);
//		contentMap.put(this.templet.getTempletAttribute(KEY_NAME).toString(), writeJson.getJSONObject(KEY_DEFAULT));
//		defaultMap.put(this.templet.getTempletAttribute(KEY_NAME).toString(), writeJson.getJSONObject(KEY_CONTENT));

	}

	@Override
	public FileTemplet getTemplet(String name) {
//		return templetMap.get(name);
		return dataMap.get(name).getTemplet();
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
//		templetMap.put(name, new FileTemplet(templet.getTempletJson()));
//
//		// 初始化内容json串
//		JSONObject contentJson = new JSONObject();
//		contentJson.put(KEY_CASE, new JSONArray());
//		contentMap.put(name, contentJson);
//
//		// 初始化默认内容串
//		defaultMap.put(name, new JSONObject());
//
//		// 初始化自动写入文件的数据
//		nowRowNumMap.put(name, 0);

		dataMap.put(name, new WriteFileData(templet));

		// 切换至当前模板
		switchPage(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T end(int contentIndex) {
		super.end(contentIndex);
//		nowRowNumMap.put(templet.getTempletAttribute(KEY_NAME).toString(), nowRowNum);
		// TODO 该行代码可能冗余，但我暂时无法测试
		dataMap.get(data.getTempName()).setNowCaseNum(data.getNowCaseNum());
		return (T) this;
	}

	@Override
	public void write() {
//		templetMap.forEach((name, templet) -> {
//			// 若分页行数不为0，则获取当前行数作为编写的起始行数
//			int startIndex = 0;
//			if (writeRowNum != 0) {
//				startIndex = nowRowNumMap.get(name);
//			}
//
//			write(templet, startIndex, -1);
//		});
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
//		JSONArray contentListJson = contentMap.get(templet.getTempletAttribute(ExcelFileTemplet.KEY_NAME))
//				.getJSONArray(KEY_CASE);
		JSONArray contentListJson = dataMap.get(templet.getTempletAttribute(ExcelFileTemplet.KEY_NAME)).getContentJson()
				.getJSONArray(KEY_CASE);

		// 判断内容json是否为空，为空则不进行处理
		if (contentListJson.isEmpty()) {
			return;
		}

		int newCaseEndIndex = analysisIndex(contentListJson.size(), caseEndIndex, true);
		int newCaseStartIndex = analysisIndex(newCaseEndIndex, caseStartIndex, true);

		// 将文件内容写入模板文件
		contentWriteTemplet(templet, newCaseStartIndex, newCaseEndIndex);
	}

	@Override
	protected List<String> getAllTempletJson() {
//		return templetMap.values().stream().map(FileTemplet::getTempletJson).collect(Collectors.toList());
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
	 * @return 数据集合
	 */
	protected HashMap<String, WriteFileData> getDataMap() {
		return dataMap;
	}
}
