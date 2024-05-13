package com.auxiliary.testcase.templet;

import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.tool.common.AddPlaceholder;
import com.auxiliary.tool.common.Placeholder;
import com.auxiliary.tool.file.WriteTempletFile;

/**
 * <p>
 * <b>文件名：CaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义用例模板读取类的基础方法
 * </p>
 * <p>
 * <b>编码时间：2023年1月13日 上午8:03:08
 * </p>
 * <p>
 * <b>修改时间：2023年5月23日 下午3:05:00
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public abstract class AbstractCaseTemplet implements AddPlaceholder {
    /**
     * 标记用于需要替换的词语
     * 
     * @since autest 4.0.0
     */
    protected final String REPLACE_WORD_SIGN = WriteTempletFile.WORD_SIGN;

    /**
     * 存储xml文件中所有的占位符
     * 
     * @since 4.2.0
     */
    protected Placeholder placeholder = new Placeholder(REPLACE_WORD_SIGN, REPLACE_WORD_SIGN);

    /**
     * 定义无参构造，方便子类使用
     * 
     * @since autest 4.0.0
     */
    protected AbstractCaseTemplet() {
    }

    @Override
    public void addReplaceWord(String word, String replaceWord) {
        placeholder.addReplaceWord(word, replaceWord);
    }

    @Override
    public void addReplaceFunction(String regex, DataFunction function) {
        placeholder.addReplaceFunction(regex, function);
    }

    /**
     * 该方法用于返回当前存储的占位符类对象
     * 
     * @return 占位符类对象
     * @since autest 4.2.0
     */
    public Placeholder getPlaceholder() {
        return placeholder;
    }
}
