package com.auxiliary.http;

import java.util.HashMap;

import com.auxiliary.tool.regex.ConstType;

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
     * 
     * @deprecated 已由{@link AssertResponse#JSON_ASSERT_ASSERT_REGEX}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_ASSERT_REGEX = "assertRegex";
    /**
     * 定义断言json串中断言内容的键名称
     * 
     * @deprecated 已由常量{@link AssertResponse#JSON_ASSERT_ASSERT_REGEX}常量代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_ASSERT_VALUE = JSON_ASSERT_ASSERT_REGEX;
    /**
     * 定义断言json串中搜索范围的键名称
     * 
     * @deprecated 已由{@link AssertResponse#JSON_ASSERT_SEARCH}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_SEARCH = "search";
    /**
     * 定义断言json串中参数名称的键名称
     * 
     * @deprecated 已由{@link AssertResponse#JSON_ASSERT_PARAM_NAME}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_PARAM_NAME = "paramName";
    /**
     * 定义断言json串中xpath的键名称
     * 
     * @deprecated 已由{@link AssertResponse#JSON_ASSERT_XPATH}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_XPATH = "xpath";
    /**
     * 定义断言json串中剪切内容的左边界的键名称
     * 
     * @deprecated 已由{@link AssertResponse#JSON_ASSERT_LB}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_LB = "lb";
    /**
     * 定义断言json串中剪切内容的右边界的键名称
     * 
     * @deprecated 已由{@link AssertResponse#JSON_ASSERT_RB}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_RB = "rb";
    /**
     * 定义断言json串中返回的元素下标的键名称
     * 
     * @deprecated 已由{@link AssertResponse#JSON_ASSERT_ORD}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_ASSERT_ORD = "ord";

    /**
     * 定义提词json串中存储变量名称的键名称
     * 
     * @deprecated 已由{@link ExtractResponse#JSON_EXTRACT_SAVE_NAME}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_EXTRACT_SAVE_NAME = "saveName";
    /**
     * 定义提词json串中搜索范围的键名称
     * 
     * @deprecated 已由{@link ExtractResponse#JSON_EXTRACT_SEARCH}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_EXTRACT_SEARCH = JSON_ASSERT_SEARCH;
    /**
     * 定义提词json串中参数名称的键名称
     * 
     * @deprecated 已由{@link ExtractResponse#JSON_EXTRACT_PARAM_NAME}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_EXTRACT_PARAM_NAME = JSON_ASSERT_PARAM_NAME;
    /**
     * 定义提词json串中参数名称的键名称
     * 
     * @deprecated 已由{@link ExtractResponse#JSON_EXTRACT_XPATH}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_EXTRACT_XPATH = JSON_ASSERT_XPATH;
    /**
     * 定义提词json串中剪切内容的左边界的键名称
     * 
     * @deprecated 已由{@link ExtractResponse#JSON_EXTRACT_LB}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_EXTRACT_LB = JSON_ASSERT_LB;
    /**
     * 定义提词json串中剪切内容的右边界的键名称
     * 
     * @deprecated 已由{@link ExtractResponse#JSON_EXTRACT_RB}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_EXTRACT_RB = JSON_ASSERT_RB;
    /**
     * 定义提词json串中返回的元素下标的键名称
     * 
     * @deprecated 已由{@link ExtractResponse#JSON_EXTRACT_ORD}代替，将在3.5.0版本中删除
     */
    @Deprecated
    public static final String JSON_EXTRACT_ORD = JSON_ASSERT_ORD;

    /**
     * 定义接口默认请求环境
     */
    protected final String DEFAULT_HOST = InterfaceInfo.DEFAULT_PROTOCOL + InterfaceInfo.DEFAULT_HOST;
    /**
     * 定义接口断言默认搜索范围
     */
    protected final String DEFAULT_SEARCH = "BODY";
    /**
     * 定义接口断言默认搜索结果下标
     */
    protected final String DEFAULT_ORD = "1";

    /**
     * 存储已读取的接口信息
     */
    protected HashMap<String, InterfaceInfo> interfaceMap = new HashMap<>(
            ConstType.DEFAULT_MAP_SIZE);

    /**
     * 该方法用于根据接口名称，返回接口的信息类对象
     *
     * @param interName 接口名称
     * @return 接口信息类对象
     * @since autest 3.3.0
     */
    public abstract InterfaceInfo getInterface(String interName);

    /**
     * 该方法用于清除已缓存的接口信息
     * <p>
     * 调用该方法后，若再次读取之前已读取的接口信息时，则将重新查找接口信息（只针对启用缓存的情况）
     * </p>
     *
     * @since autest 3.3.0
     */
    public void clearInterfaceInfoCache() {
        interfaceMap.clear();
    }
}