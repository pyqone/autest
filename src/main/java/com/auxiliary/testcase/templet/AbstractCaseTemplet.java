package com.auxiliary.testcase.templet;

import java.util.HashMap;
import java.util.Map;

import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.tool.common.AddPlaceholder;
import com.auxiliary.tool.common.Placeholder;
import com.auxiliary.tool.file.WriteTempletFile;
import com.auxiliary.tool.regex.ConstType;

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
     * 存储xml文件中其需要替换的词语
     * 
     * @since autest 4.0.0
     * @deprecated 占位符替换方法由{@link Placeholder}类代替，其原有的占位符替换方法已失效，将在4.3.0或后续版本中进行删除
     */
    @Deprecated
    protected Map<String, DataFunction> replaceWordMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
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

    /**
     * 该方法用于返回需要替换的占位符内容集合
     * 
     * @return 待替换的占位符内容集合
     * @since autest 4.0.0
     */
    public Map<String, DataFunction> getReplaceWordMap() {
        return replaceWordMap;
    }
}
