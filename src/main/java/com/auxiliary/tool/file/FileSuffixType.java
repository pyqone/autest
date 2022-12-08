package com.auxiliary.tool.file;

import com.auxiliary.tool.common.DisposeCodeUtils;

/**
 * <p>
 * <b>文件名：FileSuffixType.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义文件格式枚举
 * </p>
 * <p>
 * <b>编码时间：2022年12月5日 上午8:46:18
 * </p>
 * <p>
 * <b>修改时间：2022年12月5日 上午8:46:18
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.8.0
 */
public enum FileSuffixType {
    /**
     * 旧版excel文件格式
     * @since autest 3.8.0
     */
    XLS, 
    /**
     * 新版excel文件格式
     * 
     * @since autest 3.8.0
     */
    XLSX, 
    /**
     * 旧版word文件格式
     * 
     * @since autest 3.8.0
     */
    DOC,
    /**
     * 新版word文件格式
     * 
     * @since autest 3.8.0
     */
    DOCX, 
    /**
     * 旧版ppt文件格式
     * 
     * @since autest 3.8.0
     */
    PPT, 
    /**
     * 新版ppt文件格式
     * 
     * @since autest 3.8.0
     */
    PPTX, 
    /**
     * 纯文本文件格式
     * 
     * @since autest 3.8.0
     */
    TXT, 
    /**
     * csv数据表格文件格式
     * 
     * @since autest 3.8.0
     */
    CSV, 
    /**
     * jpg图片文件格式
     * 
     * @since autest 3.8.0
     */
    JPG, 
    /**
     * jpeg图片文件格式
     * 
     * @since autest 3.8.0
     */
    JPEG, 
    /**
     * gif图片文件格式
     * 
     * @since autest 3.8.0
     */
    GIF, 
    /**
     * png图片文件格式
     * 
     * @since autest 3.8.0
     */
    PNG, 
    /**
     * bmp图片文件格式
     * 
     * @since autest 3.8.0
     */
    BMP;
    
    /**
     * 该方法用于将枚举文本转换为文件后缀枚举
     * <p>
     * 名称不区分大小写，允许直接传入整个文件名名称，当传入整个文件名时，以“.”符号进行判断，取“.”符号之后内容为后缀名的有效内容
     * </p>
     * 
     * @param type 枚举文本内容
     * @return 转换后的枚举
     * @since autest 3.8.0
     * @throws IllegalArgumentException 当枚举文本为空或不能转换成枚举时抛出的异常
     */
    public static FileSuffixType typeText2Type(String type) {
        return DisposeCodeUtils.disposeEnumTypeText(FileSuffixType.class, type, text -> {
            // 若传入的名称中包含“.”符号，则获取最后一个“.”符号后面的内容为有效内容
            if (text.contains(".")) {
                text = text.substring(text.lastIndexOf(".") + 1);
            }

            return text;
        }, true);
    }
}