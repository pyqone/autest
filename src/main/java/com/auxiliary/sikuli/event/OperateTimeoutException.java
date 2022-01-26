package com.auxiliary.sikuli.event;

/**
 * <p>
 * <b>文件名：</b>OperateTimeoutException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当元素操作超时时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月19日 下午8:17:53
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月19日 下午8:17:53
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class OperateTimeoutException extends ElementOperateException {
    private static final long serialVersionUID = 1L;

    /**
     * 根据指定的事件名称与操作超时时间，抛出相应文本的异常
     * @param eventName 事件名称
     * @param time 操作超时时间
     * @since autest 3.0.0
     */
    public OperateTimeoutException(String eventName, long time) {
        super(String.format("“%s”事件在%d秒执行失败，无法执行该事件", eventName, time));
    }
}
