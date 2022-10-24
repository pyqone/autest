package com.auxiliary.tool.file.excel;

import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.common.enums.OrientationType;

/**
 * <p>
 * <b>文件名：ExcelCommonJsonField.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义excel模板类和excel写入工具中公用的字段及json定义方法，便于不同类之间的统一管理
 * </p>
 * <p>
 * <b>编码时间：2022年10月24日 上午10:24:48
 * </p>
 * <p>
 * <b>修改时间：2022年10月24日 上午10:24:48
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.7.0
 */
public class ExcelCommonJsonField {
    /**
     * 标记json中的background字段
     */
    public static final String KEY_BACKGROUND = "background";
    /**
     * 标记json中的type字段
     */
    public static final String KEY_TYPE = "type";
    /**
     * 标记json中的border字段
     */
    public static final String KEY_BORDER = "border";
    /**
     * 标记json中的orientation字段
     */
    public static final String KEY_ORIENTATION = "orientation";

    /**
     * 该方法用于根据单元格边框样式和边框的位置，组合并返回边框样式json
     * <p>
     * 该json的完整样式为： <code><pre>
     * {
     *  "type":1, 
     *  "orientation":[0, 1, 2, 3]
     * }
     * </pre></code>
     * </p>
     * 
     * @param borderStyle
     * @param orientationTypes
     * @return
     * @since autest
     */
    public static JSONObject getBorderJson(BorderStyle borderStyle, OrientationType... orientationTypes) {
        // 若未传入样式，或样式为NONE，则不进行转换
        if (borderStyle == null || borderStyle == BorderStyle.NONE) {
            return null;
        }
        
        JSONObject borderJson = new JSONObject();
        borderJson.put(KEY_TYPE, borderStyle.getCode());
        
        // 判断orientationTypes是否为空，若为空，则将所有的枚举项进行添加
        orientationTypes = Optional.ofNullable(orientationTypes).filter(arr -> arr.length != 0)
                .orElseGet(() -> new OrientationType[] { OrientationType.DOWN, OrientationType.LEFT,
                        OrientationType.RIGHT, OrientationType.UP });
        JSONArray oriArrJson = new JSONArray();
        for (OrientationType orientationType : orientationTypes) {
            oriArrJson.add(orientationType.getCode());
        }

        borderJson.put(KEY_ORIENTATION, oriArrJson);

        return borderJson;
    }
}
