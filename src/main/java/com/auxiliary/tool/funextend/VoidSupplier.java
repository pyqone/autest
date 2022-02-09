package com.auxiliary.tool.funextend;

/**
 * <p>
 * <b>文件名：</b>VoidSupplier.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 定义一个包含不接受参数，也不进行返回任何内容的函数式接口
 * </p>
 * <p>
 * <b>编码时间：</b>2022年2月9日 上午8:24:15
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月9日 上午8:24:15
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.1.0
 */
@FunctionalInterface
public interface VoidSupplier {
    /**
     * 该方法既不接收参数，也不返回结果
     *
     * @since autest 3.1.0
     */
    void apply();
}
