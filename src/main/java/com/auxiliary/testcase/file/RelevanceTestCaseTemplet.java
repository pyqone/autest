package com.auxiliary.testcase.file;

import com.auxiliary.testcase.templet.Case;

/**
 * <p><b>文件名：</b>TestCase.java</p>
 * <p><b>用途：</b>
 * 定义关联测试用例模板时所必须实现的方法
 * </p>
 * <p><b>编码时间：</b>2021年6月17日上午8:04:22</p>
 * <p><b>修改时间：</b>2021年6月17日上午8:04:22</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public interface RelevanceTestCaseTemplet<T extends RelevanceTestCaseTemplet<T>> {
	/**
	 * 用于将测试用例文件模板中的字段名与测试用例生成类（继承自{@link Case}的测试用例生成类）中
	 * 的字段进行关联，通过该方法设置关联字段后，可将生成的测试用例写入到测试用例文件中
	 * <p>
	 * <b>注意：</b>若模板中的字段与测试用例中的字段内容一致时，可无需进行设置
	 * </p>
	 * 
	 * @param caseField 测试用例文件模板字段
	 * @param templetField 需要关联的测试用例字段
	 */
	void relevanceCase(String caseField, String templetField);
	
	/**
	 * 用于将测试用例模板（继承自{@link Case}类的方法）所成的测试用例添加到测试用例文件中
	 * 
	 * @param testCase 测试用例生成方法
	 * @return 类本身
	 */
	T addCase(Case testCase);
}
