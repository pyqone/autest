package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：AuResponse.java</b>
 * </p>
 * <p>
 * <b>用途：</b>提供响应报文解析方法，并可对报文进行断言、提词等操作
 * </p>
 * <p>
 * <b>编码时间：2022年5月9日 下午2:19:58
 * </p>
 * <p>
 * <b>修改时间：2022年5月9日 下午2:19:58
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class AuResponse {
    /**
     * 接口响应状态码
     */
    private int state = -1;
    /**
     * 接口响应报文
     */
    private String message = "";

    // TODO 临时方法，用于返回初始化和返回响应报文，后续更改
    public AuResponse(int state, String message) {
        super();
        this.state = state;
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }
}
