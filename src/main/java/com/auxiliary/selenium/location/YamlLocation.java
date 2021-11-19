package com.auxiliary.selenium.location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.auxiliary.selenium.element.ElementType;

public class YamlLocation extends AbstractLocation implements ReadElementLimit, AppElementLocation {
    /**
     * 指向json中的模板key值
     */
    public static final String KEY_TEMPLETE = "templet";
    /**
     * 指向json中的元素key值
     */
    public static final String KEY_ELEMENT = "element";
    /**
     * 指向json中的元素定位方式key值
     */
    public static final String KEY_LOCATION = "location";
    /**
     * 指向json中的元素定位模板key值
     */
    public static final String KEY_TEMP = "temp";
    /**
     * 指向json中的元素定位模板key值
     */
    public static final String KEY_VALUE = "value";
    /**
     * 指向json中的元素定位类型key值
     */
    public static final String KEY_TYPE = "type";
    /**
     * 指向json中的元素等待时间key值
     */
    public static final String KEY_WAIT = "wait";
    /**
     * 指向json中的元素默认值的key值
     */
    public static final String KEY_DEFAULT_VALUE = "value";
    /**
     * 指向json中的元素所在窗体key值
     */
    public static final String KEY_IFRAME = "iframe";
    /**
     * 指向json中的元素与app相关的上下文key值
     */
    public static final String KEY_CONTEXT_VALUE = "context";
    /**
     * 指向json中的元素前置等待时间key值
     */
    public static final String KEY_BEFORE_TIME_VALUE = "before_time"; 

    /**
     * 存储元素模板内容
     */
    private Map<String, String> tempLetMap; 
    /**
     * 存储元素信息
     */
    private Map<String, Object> elementsMap; 

    /**
     * 存储单个元素内容
     */
    private Map<String, Object> elementMap;
    /**
     * 存储元素定位信息
     */
    private List<Map<String, String>> locationList;

    public YamlLocation(File yamlFile) {
        try(BufferedReader br = new BufferedReader(new FileReader(yamlFile))) {
            Map<String, Object> locationFileMap = new Yaml().load(br);
            analysisYaml(locationFileMap);
        } catch (IOException e) {
            throw new IncorrectFileException("yaml文件异常，文件位置：" + yamlFile.getAbsolutePath());
        }
    }

    @Override
    public ArrayList<ElementLocationInfo> getElementLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ElementType getElementType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<String> getIframeNameList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getWaitTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ReadLocation find(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getBeforeTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isNative() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDefaultValue() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 用于解析读取到的yaml文件中的内容，将其转换为相应的map集合
     * @param locationMap 模板文件内容map集合
     */
    @SuppressWarnings("unchecked")
    protected void analysisYaml(Map<String, Object> locationMap) {
        // 解析模板信息
        tempLetMap = (Map<String, String>) locationMap.get(KEY_TEMPLETE);
        // 解析元素信息
        elementsMap = (Map<String, Object>) locationMap.get(KEY_ELEMENT);
    }

    protected void analysisElementInfo(String elementName) {
        elementMap = (Map<String, Object>) elementsMap.get(elementName);
    }
}
