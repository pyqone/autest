package com.auxiliary.tool.file;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

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
	 * 存储每个Sheet对应的模板
	 */
	protected LinkedHashMap<String, FileTemplet> templetMap = new LinkedHashMap<>();

	public WriteMultipleTempletFile(FileTemplet templet) {
		super(templet);
	}

	public WriteMultipleTempletFile(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
	}

	protected WriteMultipleTempletFile() {
		super();
	}

	@Override
	public void write(int caseStartIndex, int caseEndIndex) {
		// 遍历模板，判断模板是否存在，不存在，则进行创建
		templetMap.forEach((name, templet) -> {
			if (!isExistTemplet(new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString()), templet)) {
				createTempletFile(templet);
			}
		});

		// 计算真实的起始下标与结束下标
		caseEndIndex = analysisIndex(contentJson.getJSONArray(KEY_CONTENT).size(), caseEndIndex, true);
		caseStartIndex = analysisIndex(caseEndIndex, caseStartIndex, true);

		// 将文件内容写入模板文件
		contentWriteTemplet(caseStartIndex, caseEndIndex);
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
