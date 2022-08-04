package com.auxiliary.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.file.TextFileReadUtil;

/**
 * <p>
 * <b>文件名：HttpUtil.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义接口调试常用的工具方法
 * </p>
 * <p>
 * <b>编码时间：2022年8月4日 下午4:53:15
 * </p>
 * <p>
 * <b>修改时间：2022年8月4日 下午4:53:15
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.6.0
 */
public class HttpUtil {
    /**
     * 该方法用于读取Har文件中的接口信息，并将其转换为接口信息类对象的方法
     * 
     * @param harFile       Har文件
     * @param resourceTypes 需要过滤的接口类型
     * @return 转换后的接口信息类集合
     * @since autest 3.6.0
     */
    public static List<InterfaceInfo> HarFile2InterfaceInfo(File harFile, ResourceType... resourceTypes) {
        List<InterfaceInfo> interList = new ArrayList<>();

        // 读取HAR文件，并将其转换为JSON类对象
        JSONArray interJosnArray = JSONObject.parseObject(TextFileReadUtil.megerTextToTxt(harFile, false))
                .getJSONObject("log").getJSONArray("entries");

        return interList;
    }
}
