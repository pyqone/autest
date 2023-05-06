package com.auxiliary.testcase.file;

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
 * <b>修改时间：</b>2023年4月28日 上午10:36:42
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 * @param <T> 子类
 * @since autest 2.4.0
 */
public interface RelevanceTestCaseTemplet<T extends RelevanceTestCaseTemplet<T>> {
    /**
     * 用于将测试用例文件模板中的字段名与测试用例生成类（继承自{@link CaseData}的测试用例生成类）中
     * 的字段进行关联，通过该方法设置关联字段后，可将生成的测试用例写入到测试用例文件中
     * <p>
     * <b>注意：</b>一个文件字段只能指向一个用例模板字段，但一个用例模板字段可被多个文件字段指向
     * </p>
     * 
     * @param caseField    测试用例模板字段，即{@link LabelType}枚举类中列举的字段
     * @param templetField 需要关联的文件字段，即需要写入的测试用例文件模板中自定义的字段
     * @since autest 2.4.0
     */
    void relevanceCase(String caseField, String templetField);

    /**
     * 该方法用于将测试信息类中所存储的用例内容添加到测试用例模板文件中
     * 
     * @param caseData 测试用例信息类对象
     * @return 类本身
     * @since autest 4.0.0
     */
    T addCase(CaseData caseData);
}
