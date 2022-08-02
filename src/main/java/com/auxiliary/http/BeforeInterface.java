package com.auxiliary.http;

import java.util.List;

/**
 * <p>
 * <b>文件名：BeforeInterface.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义可编写接口前置条件的模板基本读取方法
 * </p>
 * <p>
 * <b>编码时间：2022年5月6日 上午8:08:51
 * </p>
 * <p>
 * <b>修改时间：2022年5月6日 上午8:08:51
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 * @deprecated 该接口已由{@link BeforeOperation}接口替换，将在3.7.0或以上版本删除
 */
@Deprecated
public interface BeforeInterface {
    /**
     * 该方法用于返回接口的所有父层接口名称（不包括当前接口名称）
     *
     * @param interName 接口名称
     * @return 父层接口名称集合
     * @since autest 3.3.0
     * @deprecated 该方法已由{@link BeforeOperation#getParentInterfaceName(String)}方法替换，将在3.7.0或以上版本删除
     */
    @Deprecated
    public List<String> getParentInterfaceName(String interName);
}
