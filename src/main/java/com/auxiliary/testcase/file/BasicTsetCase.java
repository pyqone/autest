package com.auxiliary.testcase.file;

import java.util.HashSet;
import java.util.Set;

import com.auxiliary.testcase.templet.LabelType;

/**
 * <p>
 * <b>文件名：</b>BasicTsetCase.java
 * </p>
 * <p>
 * <b>用途：</b> 定义测试用例基本的方法以及相关的字段
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月18日上午7:49:59
 * </p>
 * <p>
 * <b>修改时间：</b>2021年6月18日上午7:49:59
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public interface BasicTsetCase<T extends BasicTsetCase<T>> {
    /**
     * 用例标题字段
     */
    public String CASE_TITLE = LabelType.TITLE.getName();
    /**
     * 用例前置条件字段
     */
    public String CASE_PRECONDITION = LabelType.PRECONDITION.getName();
    /**
     * 用例步骤字段
     */
    public String CASE_STEP = LabelType.STEP.getName();
    /**
     * 用例预期字段
     */
    public String CASE_EXCEPT = LabelType.EXCEPT.getName();
    /**
     * 用例模块字段
     */
    public String CASE_MODULE = "module";
    /**
     * 用例优先级字段
     */
    public String CASE_RANK = LabelType.RANK.getName();

    /**
     * 用于返回当前定义的测试用例字段
     *
     * @return 测试用例字段集合
     */
    default Set<String> getField() {
        Set<String> fieldSet = new HashSet<>();
        fieldSet.add(CASE_EXCEPT);
        fieldSet.add(CASE_TITLE);
        fieldSet.add(CASE_PRECONDITION);
        fieldSet.add(CASE_STEP);
        fieldSet.add(CASE_MODULE);
        fieldSet.add(CASE_RANK);

        return fieldSet;
    }

    /**
     * 用于添加测试用例步骤
     * <p>
     * <b>注意：</b>多次调用该方法时，会在内容下方继续添加写入的内容
     * </p>
     *
     * @param stepTexts 用例步骤组
     * @return 类本身
     */
    T addStep(String... stepTexts);

    /**
     * 用于添加测试用例标题
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     *
     * @param titleText 用例标题
     * @return 类本身
     */
    T addTitle(String titleText);

    /**
     * 用于添加测试用例预期
     * <p>
     * <b>注意：</b>多次调用该方法时，会在内容下方继续添加写入的内容
     * </p>
     *
     * @param exceptTexts 用例预期组
     * @return 类本身
     */
    T addExcept(String... exceptTexts);

    /**
     * 用于添加测试用例所属模块
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     *
     * @param module 模块
     * @return 类本身
     */
    T addModule(String module);

    /**
     * 用于添加测试用例前置条件
     * <p>
     * <b>注意：</b>多次调用该方法时，会在内容下方继续添加写入的内容
     * </p>
     *
     * @param preconditions 前置条件组
     * @return 类本身
     */
    T addPrecondition(String... preconditions);

    /**
     * 用于添加测试用例优先级
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     *
     * @param priority 优先级
     * @return 类本身
     */
    T addPriority(String priority);

    /**
     * 用于添加测试用例的一条步骤及预期
     * <p>
     * <b>注意：</b>多次调用该方法时，会在内容下方继续添加写入的内容
     * </p>
     *
     * @param step   步骤
     * @param except 预期
     * @return 类本身
     */
    T addStepAndExcept(String step, String except);
}
