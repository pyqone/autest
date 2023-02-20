package com.auxiliary.testcase.templet;

import java.util.HashMap;
import java.util.Map;

import com.auxiliary.datadriven.DataFunction;
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
 * <b>修改时间：2023年1月13日 上午8:03:08
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public abstract class AbstractCaseTemplet {
    /**
     * 标记用于需要替换的词语
     */
    protected final String REPLACE_WORD_SIGN = WriteTempletFile.WORD_SIGN;

    /**
     * 存储xml文件中其需要替换的词语
     */
    protected Map<String, DataFunction> replaceWordMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 该方法用于添加需要替换的内容
     * 
     * @param word        需要替换的内容
     * @param replaceWord 替换后的内容
     * @since autest 4.0.0
     */
    public void addReplaceWord(String word, String replaceWord) {
        if (word != null && !word.isEmpty()) {
            // 存储替换的词语
            replaceWordMap.put(word, text -> replaceWord);
        }
    }

    protected AbstractCaseTemplet() {
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
