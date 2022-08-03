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
 * <b>修改时间：2022年8月1日 上午8:08:54
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.6.0
 */
public class BeforeInterfaceOperation {
    /**
     * 接口前置操作相应的类对象
     */
    private Object operation;
    /**
     * 前置操作对应的枚举
     */
    private BeforeInterfaceOperationType operationType;

    /**
     * 指定前置操作执行接口
     * 
     * @param inter 接口信息
     */
    public BeforeInterfaceOperation(InterfaceInfo inter) {
        operation = inter;
        operationType = BeforeInterfaceOperationType.INTERFACE;
    }

    /**
     * 该方法用于对前置操作为接口的执行方式进行执行
     * 
     * @param action 接口请求类{@link EasyHttp}
     * @return 接口响应类
     * @since autest 3.6.0
     */
    public EasyResponse actionInterface(EasyHttp action) {
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
