package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：BeforeInterfaceOperation.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义接口前置操作的存储，并提供相应操作的执行方法
 * </p>
 * <p>
 * <b>编码时间：2022年8月1日 上午8:08:54
 * </p>
 * <p>
 * <b>修改时间：2023年6月21日 上午8:21:14
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.6.0
 */
public class BeforeInterfaceOperation {
    /**
     * 前置操作名称
     * 
     * @since autest 3.6.0
     */
    private String name = "";

    /**
     * 接口前置操作相应的类对象
     * 
     * @since autest 3.6.0
     */
    private Object operation;
    /**
     * 前置操作对应的枚举
     * 
     * @since autest 3.6.0
     */
    private BeforeInterfaceOperationType operationType;
    /**
     * 当前操作需要执行的次数
     * 
     * @since autest 4.3.0
     */
    private int actionTotalCount = 0;
    /**
     * 当前操作已执行的次数
     * 
     * @since autest 4.3.0
     */
    private int actionCount = 0;

    /**
     * 指定前置操作执行接口
     * 
     * @param inter 接口信息
     * @since autest 3.6.0
     */
    public BeforeInterfaceOperation(InterfaceInfo inter) {
        operation = inter;
        operationType = BeforeInterfaceOperationType.INTERFACE;
    }

    /**
     * 指定前置操作执行接口
     * 
     * @param name  前置操作名称
     * @param inter 接口信息
     * @since autest 3.7.0
     */
    public BeforeInterfaceOperation(String name, InterfaceInfo inter) {
        this(inter);
        this.name = name;
    }

    /**
     * 该方法用于返回前置操作名称
     * 
     * @return 前置操作名称
     * @since autest 3.7.0
     */
    public String getName() {
        return name;
    }

    /**
     * 该方法用于对前置操作为接口的执行方式进行执行
     * 
     * @param action 接口请求类{@link EasyHttp}
     * @return 接口响应类
     * @since autest 3.6.0
     */
    public EasyResponse actionInterface(EasyHttp action) {
        actionCount++;
        return action.requst((InterfaceInfo) operation);
    }

    /**
     * 该方法用于返回前置操作的类型
     * 
     * @return 前置操作类型枚举
     * @since autest 3.6.0
     */
    public BeforeInterfaceOperationType getOperationType() {
        return operationType;
    }

    /**
     * 该方法用于设置当前前置操作在整个接口请求周期中可执行的次数，用于在{@code #isExecutedOperation()}方法中的判断。
     * 若设置的次数小于1，则表示不限制执行次数
     * 
     * @param actionCount 操作总执行次数
     * @since autest 4.3.0
     */
    public void setActionCount(int actionCount) {
        actionTotalCount = actionCount;
    }

    /**
     * 该方法用于返回当前操作是否还有可用的执行次数
     * <p>
     * 由于执行方法中不可控制，故需要使用该方法自行判断当前操作是否还需要继续调用，若无视该方法的返回，其相应的执行方法依然能正常执行
     * </p>
     * 
     * @return 是否还有可用的执行次数
     * @since autest 4.3.0
     */
    public boolean isExecutedOperation() {
        return actionTotalCount < 1 || actionTotalCount > actionCount;
    }

    /**
     * <p>
     * <b>文件名：BeforeInterfaceOperation.java</b>
     * </p>
     * <p>
     * <b>用途：</b> 定义接口前置操作类型枚举
     * </p>
     * <p>
     * <b>编码时间：2022年8月1日 上午8:11:19
     * </p>
     * <p>
     * <b>修改时间：2022年8月1日 上午8:11:19
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.6.0
     */
    public enum BeforeInterfaceOperationType {
        /**
         * 执行接口
         */
        INTERFACE,
        /**
         * 执行SQL
         */
        SQL,
        /**
         * 执行自定义方法
         */
        FUNCTION;
    }
}
