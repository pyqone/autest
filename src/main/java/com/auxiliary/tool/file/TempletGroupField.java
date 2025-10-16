package com.auxiliary.tool.file;

/**
 * <p>
 * <b>用途：</b>该接口用于标记当前模板允许添加组字段，以实现多级标题的目的
 * </p>
 * <p>
 * <b>编码时间：2025年10月11日 08:20:27
 * </p>
 * <p>
 * <b>修改时间：2025年10月11日 08:20:27
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 5.1.0
 */
public interface TempletGroupField<T extends TempletGroupField<T>> {
    /**
     * 该方法用于向模板中添加组名称
     * 
     * @param groupName 组名称
     * @return 类本身
     * @author 彭宇琦
     * @since autest 5.1.0
     */
    public T addGroup(String groupName);

    /**
     * 该方法用于向字段组中添加字段
     * 
     * @param groupName 组名称
     * @param fieldName 字段名称
     * @return 类本身
     * @author 彭宇琦
     * @since autest 5.1.0
     */
    public T addGroupField(String groupName, String fieldName);
}
