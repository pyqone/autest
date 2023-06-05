package com.auxiliary.selenium.element;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;

import com.auxiliary.selenium.location.AppElementLocation;
import com.auxiliary.selenium.location.ElementLocationInfo;
import com.auxiliary.selenium.location.ReadElementLimit;
import com.auxiliary.selenium.location.ReadLocation;
import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.common.Placeholder;

/**
 * <p>
 * <b>文件名：</b>ElementData.java
 * </p>
 * <p>
 * <b>用途：</b> 用于存储页面元素的基本信息，以便于在查找元素中进行使用
 * </p>
 * <p>
 * <b>编码时间：</b>2020年9月27日上午7:50:44
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月10日下午2:53:33
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.3
 */
public class ElementData {
    private final String REPLACE_LINK_SIGN = "=";
    private final String TRANSFERRED_EQUAL_SIGN = "\\=";

    /**
     * 存储元素的名称
     */
    private String name;
    /**
     * 存储元素读取的方式
     */
    private ReadLocation read;
    /**
     * 读取类的占位符类对象
     * 
     * @since autest 4.0.0
     */
    private Placeholder placeholder;

    /**
     * 存储外链的词语
     */
    private ArrayList<String> linkWordList = new ArrayList<>();

    /**
     * 根据元素名称，在配置文件中查找元素，将元素的信息进行存储
     * 
     * @param name 元素名称
     * @param read 配置文件类对象
     */
    public ElementData(String name, ReadLocation read) {
        // 若查找成功，则存储元素名称与元素信息读取类对象
        this.name = name;
        this.read = read;
        this.placeholder = new Placeholder(read.getStartElementPlaceholder(), read.getEndElementPlaceholder());
    }

    /**
     * 返回元素名称
     * 
     * @return 元素名称
     */
    public String getName() {
        return name;
    }

    /**
     * 返回元素定位信息集合
     * 
     * @return 元素定位信息集合
     */
    public ArrayList<ElementLocationInfo> getLocationList() {
        // 对元素进行查找
        read.find(name);

        // 获取元素定位信息
        ArrayList<ElementLocationInfo> locationList = new ArrayList<>(read.getElementLocation());

        // 若存储的外链词语不为空，则对需要外链的定位内容进行处理
        if (!linkWordList.isEmpty() || placeholder.isExistReplaceContent()) {
            for (int i = 0; i < locationList.size(); i++) {
                // 将元素的定位内容先用指定的替换词语进行一次替换，再对其进行顺序替换
                locationList.get(i).setLocationText(placeholder.sequentialReplaceText(
                        placeholder.replaceText(locationList.get(i).getLocationText()), false, linkWordList));

            }
        }

        return locationList;
    }

    /**
     * 返回元素类型
     * 
     * @return 元素
     */
    public ElementType getElementType() {
        // 对元素进行查找
        read.find(name);
        return read.getElementType();
    }

    /**
     * 返回元素父层窗体名称列表
     * 
     * @return 父层窗体名称列表
     */
    public ArrayList<String> getIframeNameList() {
        // 对元素进行查找
        read.find(name);
        ArrayList<String> iframeNameList = new ArrayList<>(read.getIframeNameList());
        return iframeNameList;
    }

    /**
     * 返回元素加载超时时间
     * 
     * @return 元素加载超时时间
     */
    public long getWaitTime() {
        // 对元素进行查找
        read.find(name);
        return read.getWaitTime();
    }

    /**
     * 用于返回元素的默认值。若元素不存在默认值，则返回空串
     * 
     * @return 元素的默认值
     */
    public String getDefaultValue() {
        // 对元素进行查找
        read.find(name);
        String defaultValue = "";
        // 判断元素读取类是否
        if (read instanceof ReadElementLimit) {
            defaultValue = ((ReadElementLimit) read).getDefaultValue();
        }

        return defaultValue;
    }

    /**
     * 用于返回当前元素是否为app原生元素
     * <p>
     * <b>注意：</b>若元素非app元素，则返回false
     * </p>
     * 
     * @return 元素是否为app原生元素
     */
    public boolean isNativeElement() {
        // 对元素进行查找
        read.find(name);
        if (read instanceof AppElementLocation) {
            return ((AppElementLocation) read).isNative();
        } else {
            return false;
        }
    }

    /**
     * 用于返回app元素所在WebView的上下文
     * <p>
     * <b>注意：</b>若元素非app元素或app的原生元素，则返回空串
     * </p>
     * 
     * @return app元素所在WebView的上下文
     */
    public String getWebViewContext() {
        // 对元素进行查找
        read.find(name);
        if (read instanceof AppElementLocation) {
            return ((AppElementLocation) read).getContext();
        } else {
            return "";
        }
    }

    /**
     * 返回元素前置等待时间
     * 
     * @return 元素前置等待时间
     */
    public long getBeforeTime() {
        // 对元素进行查找
        read.find(name);
        return read.getBeforeTime();
    }

    /**
     * 用于添加元素定位外链词语
     * <p>
     * 外链词语允许指定替换相应占位符的内容，使用“=”符号连接，例如“text=abc”，表示将所有“text”占位符词语替换为“abc”；
     * 若传入的内容不包含指定替换的内容，则将其加入到顺序替换集合中。特别的，若需要替换的内容包含“=”符号，则使用“\=”符号来代替“=”，
     * 例如传入“text\=abc”（传入方法时，反斜杠符号需要转义，实际应传入“\\=”），此时，“text=abc”将作为一个整体词语加入到顺序替换集合中，
     * 而不是作为指定替换的词语进行存储
     * </p>
     * 
     * @param linkWords 外链词语
     */
    public void addLinkWord(String... linkWords) {
        if (linkWords != null && linkWords.length != 0) {
            // 定义将转义的等于符号进行替换的内容，使用一个UUID进行代替，以便于替换
            String tempUUID = UUID.randomUUID().toString();
            // 遍历所有需要替换的词语
            for (String word : linkWords) {
                // 将需要转义的替换符号使用随机的uuid进行替换
                word = word.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(TRANSFERRED_EQUAL_SIGN), tempUUID);
                // 获取替换后的内容其替换符号的位置
                int signIndex = word.indexOf(REPLACE_LINK_SIGN);
                // 若等于符号存在且非在字符串开头时，则将字符串按照第一个等于符号进行裁剪；否则，则直接存储至顺序替换内容中
                if (signIndex > 0) {
                    // 按照替换符号的位置，对传入的内容进行分解，并将相应的uuid换成连接符号
                    String key = word.substring(0, signIndex).replaceAll(tempUUID,
                            Matcher.quoteReplacement(TRANSFERRED_EQUAL_SIGN));
                    String value = word.substring(signIndex + REPLACE_LINK_SIGN.length()).replaceAll(tempUUID,
                            REPLACE_LINK_SIGN);

                    // 将分解的内容存储至占位符类对象中
                    placeholder.addReplaceWord(key, value);
                } else {
                    linkWordList.add(word.replaceAll(tempUUID, REPLACE_LINK_SIGN));
                }
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((linkWordList == null) ? 0 : linkWordList.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ElementData other = (ElementData) obj;
        if (linkWordList == null) {
            if (other.linkWordList != null) {
                return false;
            }
        } else if (!linkWordList.equals(other.linkWordList)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
