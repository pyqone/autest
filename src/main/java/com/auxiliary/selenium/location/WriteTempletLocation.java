package com.auxiliary.selenium.location;

/**
 * <p><b>文件名：</b>WriteTempletLocation.java</p>
 * <p><b>用途：</b>
 * 定义写入元素模板内容必须实现的方法
 * </p>
 * <p><b>编码时间：</b>2020年10月30日上午8:11:45</p>
 * <p><b>修改时间：</b>2020年10月30日上午8:11:45</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public interface WriteTempletLocation {
	/**
	 * 用于设置元素定位模板内容，将根据对模板的id来区分模板，多次设置将覆盖上一次设置的内容
	 * 
	 * @param templetId    模板id
	 * @param templetValue 模板内容
	 */
	public abstract void putTemplet(String templetId, String templetValue);
	
	/**
	 * 用于设置元素调用模板时，该内容允许多次设置，将存储多个替换模板的词语。若元素不存在相应的模板，则抛出异常
	 * @param name 元素名称
	 * @param templetId 模板id
	 * @param key 被替换的关键词
	 * @param value 替换的内容
	 * @throws UndefinedElementException 模板id不存在时抛出的异常
	 */
	public abstract void putTempletReplaceKey(String name, String templetId, String key, String value);
	
	/**
	 * 用于设置元素调用模板的id，该内容允许多次设置，将存储多个元素定位方式模板。若元素名称不存在，则创建元素
	 * @param name 元素名称
	 * @param byType 元素定位类型{@link ByType}枚举
	 * @param templetId 模板id
	 */
	public abstract void putElementTempletLocation(String name, ByType byType, String templetId);
}
