package com.auxiliary.tool.file;

import com.auxiliary.tool.common.enums.OrderedListSignType;

/**
 * <p>
 * <b>文件名：AutoAddListSign.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于标记当前模板能自动添加列表标志，并定义基本的添加标志的方法与相应的模板属性
 * </p>
 * <p>
 * <b>编码时间：2023年5月11日 上午8:54:07
 * </p>
 * <p>
 * <b>修改时间：2023年5月11日 上午8:54:07
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.2.0
 */
public interface TempletAutoAddListSign<T extends TempletAutoAddListSign<T>> {
    /**
     * 标记模板json中的autoListSign字段，表示需要对指定的字段进行自动标记列表标记
     * 
     * @since autest 4.2.0
     */
    public static final String KEY_AUTO_LIST_SIGN = "autoListSign";
    /**
     * 标记模板json中的signType字段，表示列表标记的类型，有序标记则读取{@link OrderedListSignType}中枚举的code值，无序枚举则用“-1”表示
     * 
     * @since autest 4.2.0
     */
    public static final String KEY_SIGN_TYPE = "signType";
    /**
     * 标记模板json中的signContent字段，表示无序标记中，自定义的标记样式
     * 
     * @since autest 4.2.0
     */
    public static final String KEY_SIGN_CONTENT = "signContent";

    /**
     * 该方法用于对指定的一组字段设置有序列表标记
     * 
     * @param orderedListSignType 有序列表标记类型
     * @param fields              需要添加标记的字段组
     * @return 类本身
     * @since autest 4.2.0
     */
    public T setOrderedListSign(OrderedListSignType orderedListSignType, String... fields);

    /**
     * 该方法用于对指定的一组字段设置无序列表标记
     * 
     * @param signText 自定义的无序列表标记
     * @param fields   需要添加标记的字段组
     * @return 类本身
     * @since autest 4.2.0
     */
    public T setUnorderedList(String signText, String... fields);

    /**
     * 该方法用于清除指定字段的自动添加列表标记
     * 
     * @param fields 需要删除自动标记的字段组
     * @return 类本身
     * @since autest 4.2.0
     */
    public T clearAutoListSign(String... fields);
}
