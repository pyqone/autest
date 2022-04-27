package com.auxiliary.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <p>
 * <b>文件名：ReadInterfaceFromXml.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于读取xml文件中的接口信息
 * </p>
 * <p>
 * <b>编码时间：2022年4月27日 上午8:10:25
 * </p>
 * <p>
 * <b>修改时间：2022年4月27日 上午8:10:25
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class ReadInterfaceFromXml extends ReadInterfaceFromAbstract
        implements ActionEnvironment, AssertResponse, ExtractResponse {
    /**
     * 定义接口默认请求环境
     */
    private final String DEFAULT_HOST = InterfaceInfo.DEFAULT_PROTOCOL + InterfaceInfo.DEFAULT_HOST;

    /**
     * 定义environments标签名称
     */
    private final String XML_LABEL_ENVIRONMENTS = "environments";
    /**
     * 定义default属性名称
     */
    private final String XML_ATTRI_DEFAULT = "default";
    /**
     * 定义name属性名称
     */
    private final String XML_ATTRI_NAME = "name";

    /**
     * xml文件根节点
     */
    private Element rootElement;

    /**
     * 环境集合
     */
    private HashMap<String, String> environmentMap = new HashMap<>();
    /**
     * 当前执行接口的环境
     */
    private String actionEnvironment = DEFAULT_HOST;

    /**
     * 根据xml文件对象，解析接口信息xml文件，并设置接口执行环境及接口默认执行环境
     *
     * @param xmlFile xml文件类对象
     */
    public ReadInterfaceFromXml(File xmlFile) {
        try {
            rootElement = new SAXReader().read(xmlFile).getRootElement();
        } catch (DocumentException e) {
            throw new InterfaceReadToolsException(String.format("xml文件存在异常，无法解析。文件路径为：%s", xmlFile.getAbsolutePath()),
                    e);
        } catch (NullPointerException e) {
            throw new InterfaceReadToolsException("文件类对象为null");
        }

        // 读取环境集合
        Element environmentsElement = rootElement.element(XML_LABEL_ENVIRONMENTS);
        List<Element> environmentElementList = environmentsElement.elements();
        // 判断集合是否为空，若为不为空，则存储所有的环境，并设置默认环境
        if (!environmentElementList.isEmpty()) {
            // 获取所有得到环境，并存储所有的环境
            for (Element environmentElement : environmentElementList) {
                String name = Optional.ofNullable(environmentElement.attributeValue(XML_ATTRI_NAME)).orElseGet(() -> "");
                // 判断当前环境名称是否为空，为空，则不存储
                if (!name.isEmpty()) {
                    // 若当前标签不存在文本节点，则存储默认内容
                    environmentMap.put(name, Optional.ofNullable(environmentElement.getText())
                            .orElseGet(() -> DEFAULT_HOST));
                }
            }

            // 判断环境集合标签中是否指定默认环境，若存在，则将执行环境指向为默认环境；若不存在，则取环境集合的第一个元素
            if (environmentMap.containsKey(environmentsElement.attributeValue(XML_ATTRI_DEFAULT))) {
                actionEnvironment = environmentMap.get(environmentsElement.attributeValue(XML_ATTRI_DEFAULT));
            } else {
                actionEnvironment = environmentElementList.get(0).getText();
            }
        }
    }

    @Override
    public InterfaceInfo getInterface(String interName) {
        return null;
    }

    @Override
    public String getExtractContent(String interName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAssertContent(String interName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setActionEnvironment(String environmentName) {
        if (environmentMap.containsKey(environmentName)) {
            actionEnvironment = environmentMap.get(environmentName);
        }
    }

    @Override
    public HashMap<String, String> getActionEnvironment() {
        return environmentMap;
    }

    @Override
    public InterfaceInfo getInterface(String interName, String environmentName) {
        // TODO Auto-generated method stub
        return null;
    }

}
