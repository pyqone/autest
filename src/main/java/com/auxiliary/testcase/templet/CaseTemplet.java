package com.auxiliary.testcase.templet;

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
 * <b>修改时间：2023年1月13日 上午8:03:08
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public abstract class CaseTemplet<T extends CaseTemplet<T>> {
    /**
     * 标记用于需要替换的词语
     */
    protected final String REPLACE_WORD_SIGN = WriteTempletFile.WORD_SIGN;
}
