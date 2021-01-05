package com.auxiliary.testcase.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.dom4j.DocumentException;

/**
 * <p><b>文件名：</b>CommonTestCaseWrite.java</p>
 * <p><b>用途：</b>定义了测试用例中一些基本方法的添加，如添加步骤、预期方法等，使编码时更加直观</p>
 * <p><b>编码时间：</b>2020年4月5日 下午2:22:20</p>
 * <p><b>修改时间：</b>2020年4月5日 下午2:22:20</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 */
public abstract class CommonTestCaseWrite<T extends CommonTestCaseWrite<T>> extends AbstractTestCaseWrite<CommonTestCaseWrite<T>> {
	/**
	 * 通过测试文件模板xml配置文件和测试用例文件来构造JiraTestCaseWrite类。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws DocumentException 
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public CommonTestCaseWrite(File configFile, File caseFile) throws DocumentException {
		super(configFile, caseFile);
	}
	
	/**
	 * 用于写入标题信息
	 * @param title 标题
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T addTitle(String title) {
		//清除标题原有的内容
		clearContent(getTitleName());
		//写入标题
		return (T) addContent(getTitleName(), title);
	}
	
	/**
	 * 用于写入步骤信息
	 * @param steps 步骤
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T addStep(String... steps) {
		//写入步骤信息
		addContent(getStepName(), steps);
		
		return (T) this;
	}
	
	/**
	 * 用于写入预期信息
	 * @param excepts 预期
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T addExcept(String... excepts) {
		//写入预期信息
		addContent(getExceptName(), excepts);
		
		return (T) this;
	}
	
	/**
	 * 由于步骤与预期是对应的，故可使用该方法写入一条步骤与预期信息
	 * @param step 步骤
	 * @param except 预期
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T addStepAndExcept(String step, String except) {
		//写入步骤信息
		addContent(getStepName(), step);
		//写入预期信息
		addContent(getExceptName(), except);
		
		return (T) this;
	}
	
	/**
	 * 根据关键词，匹配相应的模块信息，若未传入信息，则不写入信息，若能匹配信息，则会有以下三种情况：
	 * <ol>
	 * <li>匹配一个结果，则直接存入结果</li>
	 * <li>匹配多个结果，则以“key1/key2/key3/.../keyN/”的形式拼接字符串</li>
	 * <li>无匹配结果，则以“key1/key2/key3/.../keyN/”的形式拼接字符串</li>
	 * </ol>
	 * 重复调用该方法时将覆盖原写入的内容。
	 * @param keys 关键词组
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T addModule(String... keys) {
		//清除原有的内容
		clearContent(getModuleName());
				
		//若未传入关键词，则不填写信息
		if (keys == null) {
			return (T) this;
		}
		
		//若只有一个关键词，并且能转换成数字的情况下，则无需做任何处理，直接调用addContent方法
		if (keys.length == 1) {
			try {
				Integer.valueOf(keys[0]);
				return (T) addContent(getModuleName(), keys);
			} catch (NumberFormatException e) {
			}
		}
		
		//若关键词不止一个，则对关键词进行匹配（调用匹配方法）
		//匹配模块信息，分为两种情况：
		//1.命中一个结果，则直接存入结果
		//2.命中多个结果或未命中结果，则以“/key1/key2/key3/.../keyN”的形式拼接字符串
		//获取数据有效性
		ArrayList<String> dataList = fieldMap.get(nowSheetName).get(getModuleName()).matchDataValidation(keys);
		//存储最终得到的模块信息
		StringBuilder dataText = new StringBuilder();
		if (dataList.size() == 1) {
			dataText.append(dataList.get(0));
		} else {
			Arrays.stream(keys).forEach(text -> {
				//拼接关键词
				dataText.append("/" + text);
			});
		}
		
		//写入得到的关键词
		addContent(getModuleName(), String.valueOf(dataText.toString()));
		return (T) this;
	}
	
	/**
	 * 用于返回步骤在xml文件中的id，在{@link #addStep(String...)}中使用
	 * @return 步骤在xml文件中的id
	 */
	abstract String getStepName();
	
	/**
	 * 用于返回预期在xml文件中的id，在{@link #addExcept(String...)}中使用
	 * @return 预期在xml文件中的id
	 */
	abstract String getExceptName();
	
	/**
	 * 用于返回指向模块在xml文件中的id，在{@link #addModule(String...)}中使用
	 * @return 模块在xml文件中的id
	 */
	abstract String getModuleName();
	
	/**
	 * 用于返回指向标题在xml文件中的id，在{@link #addModule(String...)}中使用
	 * @return 标题在xml文件中的id
	 */
	abstract String getTitleName();
}
