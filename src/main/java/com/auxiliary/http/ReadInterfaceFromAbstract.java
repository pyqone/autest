package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：ReadInterfaceFromAbstract.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义接口信息读取类的基本方法
 * </p>
 * <p>
 * <b>编码时间：2022年4月12日 上午9:00:12
 * </p>
 * <p>
 * <b>修改时间：2022年4月12日 上午9:00:12
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public abstract class ReadInterfaceFromAbstract {
    /**
     * 定义断言json串中断言内容的键名称
     */
    public static final String JSON_ASSERT_ASSERT_VALUE = "assertValue";
    /**
     * 定义断言json串中搜索范围的键名称
     */
    public static final String JSON_ASSERT_SEARCH = "search";
    /**
     * 定义断言json串中断言参数名称的键名称
     */
    public static final String JSON_ASSERT_PARAM_NAME = "paramName";
    /**
     * 定义断言json串中剪切内容的左边界的键名称
     */
    public static final String JSON_ASSERT_LB = "lb";
    /**
     * 定义断言json串中剪切内容的右边界的键名称
     */
    public static final String JSON_ASSERT_RB = "rb";
    /**
     * 定义断言json串中返回的元素下标的键名称
     */
    public static final String JSON_ASSERT_ORD = "ord";

    /**
     * 定义提词json串中存储变量名称的键名称
     */
    public static final String JSON_EXTRACT_SAVE_NAME = "saveName";
    /**
     * 定义提词json串中搜索范围的键名称
     */
    public static final String JSON_EXTRACT_SEARCH = JSON_ASSERT_SEARCH;
    /**
     * 定义提词json串中断言参数名称的键名称
     */
    public static final String JSON_EXTRACT_PARAM_NAME = JSON_ASSERT_PARAM_NAME;
    /**
     * 定义提词json串中剪切内容的左边界的键名称
     */
    public static final String JSON_EXTRACT_LB = JSON_ASSERT_LB;
    /**
     * 定义提词json串中剪切内容的右边界的键名称
     */
    public static final String JSON_EXTRACT_RB = JSON_ASSERT_RB;
    /**
     * 定义提词json串中返回的元素下标的键名称
     */
    public static final String JSON_EXTRACT_ORD = JSON_ASSERT_ORD;

    // TODO 自动识别请求体格式，并设置相应的请求头
    /**
     * 该方法用于根据接口名称，返回接口的信息类对象
     *
     * @param interName 接口名称
     * @return 接口信息类对象
     * @since autest 3.3.0
     */
    public abstract InterfaceInfo getInterface(String interName);
}