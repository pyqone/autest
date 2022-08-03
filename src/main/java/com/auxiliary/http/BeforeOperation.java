package com.auxiliary.http;

import java.util.List;

/**
 * <p>
 * <b>文件名：BeforeOperation.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义编写接口前置操作的模板基本读取方法
 * </p>
 * <p>
 * <b>编码时间：2022年8月2日 上午10:10:43
 * </p>
 * <p>
 * <b>修改时间：2022年8月2日 上午10:10:43
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.6.0
 */
public interface BeforeOperation {
    /**
     * 该方法用于返回接口的所有前置操作方法
     * 
     * @return 前置操作封装类集合
     * @since autest 3.6.0
     */
    public List<BeforeInterfaceOperation> getBeforeOperation(String interName);

    /**
     * 该方法用于返回接口的所有父层接口名称（不包括当前接口名称）
     *
     * @param interName 接口名称
     * @return 父层接口名称集合
     * @since autest 3.3.0
     */
    public List<String> getParentInterfaceName(String interName);
}
