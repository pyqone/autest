package com.auxiliary.testcase.file;

import com.auxiliary.testcase.templet.Case;
import com.auxiliary.testcase.templet.CaseData;

/**
 * <p>
 * <b>文件名：</b>TestCase.java
 * </p>
 * <p>
 * <b>用途：</b> 定义关联测试用例模板时所必须实现的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月17日上午8:04:22
 * </p>
 * <p>
 * <b>修改时间：</b>2023年2月6日 上午8:44:20
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 * @since autest 2.4.0
 */
@SuppressWarnings("deprecation")
public interface RelevanceTestCaseTemplet<T extends RelevanceTestCaseTemplet<T>> {
	/**
     * 用于将测试用例文件模板中的字段名与测试用例生成类（继承自{@link Case}的测试用例生成类）中
     * 的字段进行关联，通过该方法设置关联字段后，可将生成的测试用例写入到测试用例文件中
     * <p>
     * <b>注意：</b>一个模板字段只能指向一个用例字段，但一个用例字段可被多个模板字段指向
     * </p>
     * 
     * @param caseField    测试用例文件模板字段
     * @param templetField 需要关联的测试用例字段
     * @since autest 2.4.0
     */
	void relevanceCase(String caseField, String templetField);
	
	/**
     * 用于将测试用例模板（继承自{@link Case}类的方法）所成的测试用例添加到测试用例文件中
     * 
     * @param testCase 测试用例生成方法
     * @return 类本身
     * @since autest 2.4.0
     * @deprecated 该方法已由{@link #addCase(CaseData)}方法代替，将在4.1.0或后续版本中删除
     */
    @Deprecated
	T addCase(Case testCase);

    /**
     * 该方法用于将测试信息类中所存储的用例内容添加到测试用例模板文件中
     * 
     * @param caseData 测试用例信息类对象
     * @return 类本身
     * @since autest 4.0.0
     */
    T addCase(CaseData caseData);
}
