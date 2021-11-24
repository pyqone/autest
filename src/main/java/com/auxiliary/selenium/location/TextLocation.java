package com.auxiliary.selenium.location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.auxiliary.selenium.element.ElementType;

/**
 * <p>
 * <b>文件名：</b>TextLcation.java
 * </p>
 * <p>
 * <b>用途：</b> 用于读取以文本形式存储的元素定位信息。其中，文本中每一行为一条元素信息，元素信息
 * 之间与元素层级的标识，可以通过方法进行设置，之后将按照设置的方式对元素信息与层级
 * 进行计算。默认情况下使用空格作为元素信息分隔的符号，制表符作为元素层级的标识。<br>
 * </p>
 * <p>
 * 元素前三个信息必须是“元素名称（分隔符）元素定位类型（分隔符）元素定位内容”，例如：
 * “用户名==xpath==\\*[@name='username']”，缺失信息或位置错误其元素均无法识别，则抛出
 * {@link UndefinedElementException}异常。后两个元素信息分别为元素等待时间与元素类型，
 * 两者均为整形数字，同样也存在顺序，例如：
 * <ul>
 * <li>若只存在一个数字，且数字在{@link ElementType}枚举范围内，则表示数字为元素枚举，如
 * “用户名==xpath==\\*[@name='username']==2”，则表示元素等待时间为2秒</li>
 * <li>若只存在两个个数字，则按照顺序读取元素信息，如
 * “用户名==xpath==\\*[@name='username']==2==0”，则表示元素类型为{@link ElementType#COMMON_ELEMENT}
 * ，且元素等待时间为2秒</li>
 * </ul>
 * </p>
 * <p>
 * <b>编码时间：</b>2020年11月3日下午8:45:25
 * </p>
 * <p>
 * <b>修改时间：</b>2021年3月9日上午8:08:45
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class TextLocation extends AbstractLocation {
    /**
     * 默认元素信息分隔符号
     */
    public static final String DEFAULT_INFO_SPLIT_SIGN = "==";
    /**
     * 默认元素层级标识符
     */
    public static final String DEFAULT_LEVEL_SPLIT_SIGN = "\t";
    /**
     * 默认元素间分隔符号
     */
    public static final String ELEMENT_SPLIT_SIGN = "\\n";

    /**
     * 存储设置的元素信息分隔符号
     */
    protected String infoSplitSign = DEFAULT_INFO_SPLIT_SIGN;
    /**
     * 存储设置的元素层级标识符
     */
    protected String levelSplitSign = DEFAULT_LEVEL_SPLIT_SIGN;

    /**
     * 用于存储当前窗体的层级结构
     */
    protected List<String> iframeNameList = new ArrayList<>();
    /**
     * 用于存储当前读取的元素名称
     */
    protected String nowElementName = "";

    /**
     * 用于当前文本转换后的读取元素信息方式
     */
    private NoFileLocation noFileLocation;

    /**
     * 通过默认元素信息及层级信息分隔符对文本中元素信息进行分割，构造相应元素信息对象。默认
     * 元素信息分隔符为“==”（{@link #DEFAULT_INFO_SPLIT_SIGN}），默认层级分隔符为制表符
     * （{@link #DEFAULT_LEVEL_SPLIT_SIGN}）
     * 
     * @param text 元素信息文本
     */
    public TextLocation(String text) {
        this(text, DEFAULT_INFO_SPLIT_SIGN, DEFAULT_LEVEL_SPLIT_SIGN);
    }

    /**
     * <p>
     * 通过自定义的元素信息与元素层级分隔符号对文本中元素信息进行分隔，构造元素信息对象。
     * </p>
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>由于字符串的切分按照正则表达式进行切分，若使用特殊字符（如制表符“\t”），则需要按照正则的 写法（如制表符应写为“\\t”）。</li>
     * <li><i><b>请勿在元素信息中使用元素信息分隔符，否则会导致元素信息读取异常</b></i></li>
     * </ol>
     * </p>
     * 
     * @param text           元素信息文本
     * @param infoSplitSign  元素信息分隔符
     * @param levelSplitSign 元素层级分隔符
     */
    public TextLocation(String text, String infoSplitSign, String levelSplitSign) {
        Optional.ofNullable(text).filter(t -> !t.isEmpty()).orElseThrow(() -> new UndefinedElementException("元素信息为空"));

        this.infoSplitSign = Optional.ofNullable(infoSplitSign).filter(t -> !t.isEmpty())
                .orElseThrow(() -> new UndefinedElementException("元素信息切分符为空"));

        this.levelSplitSign = Optional.ofNullable(levelSplitSign).filter(t -> !t.isEmpty())
                .orElseThrow(() -> new UndefinedElementException("元素层级切分符为空"));

        noFileLocation = new NoFileLocation();

        Arrays.asList(text.split(ELEMENT_SPLIT_SIGN)).stream().filter(elementText -> !elementText.isEmpty())
        .forEach(this::analysisElement);
    }

    /**
     * 用于设置文本中元素信息切分符号与元素层级标识符号。由于字符串的切分按照 正则表达式进行切分，若使用特殊字符（如制表符“\t”），则需要按照正则的
     * 写法（如制表符应写为“\\t”）
     * 
     * @param infoSign  元素信息分隔符号
     * @param levelSign 元素层级标识符号
     */
    public void setSplitSign(String infoSign, String levelSign) {
        this.infoSplitSign = infoSign;
        this.levelSplitSign = levelSign;
    }

    @Override
    public ArrayList<ElementLocationInfo> getElementLocation() {
        return noFileLocation.getElementLocation();
    }

    @Override
    public ElementType getElementType() {
        return noFileLocation.getElementType();
    }

    @Override
    public ArrayList<String> getIframeNameList() {
        return noFileLocation.getIframeNameList();
    }

    @Override
    public long getWaitTime() {
        return noFileLocation.getWaitTime();
    }

    /**
     * 用于对文本元素信息进行切分，将元素信息分别存储
     * 
     * @param elementText 元素信息
     * @throws UndefinedElementException 元素信息不足或元素缺少关键信息时抛出的异常
     */
    protected void analysisElement(String elementText) {
        // 若当前元素为空，则直接结束
        if (elementText == null || elementText.isEmpty()) {
            return;
        }

        // 分析元素层级
        String name = analysisElementInformation(elementText);
        // 分析元素信息
        analysisElementLevel(name, elementText);

        nowElementName = name;
    }

    /**
     * 用于分析元素的层级结构
     * 
     * @param name        元素名称
     * @param elementText 元素信息
     */
    protected void analysisElementLevel(String name, String elementText) {
        // 循环，从第一位元素开始判断，直到当前元素不存在层级标志为止，通过循环，可计算出当前的层级
        int index = 0;
        while (levelSplitSign.equals(elementText.substring(index, index + levelSplitSign.length()))) {
            index++;
        }

        // 若窗体集合为空，且层级为0，则可直接返回
        if (iframeNameList.isEmpty() && index == 0) {
            return;
        }

        // 根据层级，结合iframeNameList计算是否存储窗体名称，思路：
        // 1.若当前层级大于iframeNameList中的总数，则将当前元素的窗体配置为上一个元素，并存储上一个元素名称
        // 2.若当前层级与iframeNameList中的总数一致，则将当前元素的窗体配置为iframeNameList中最后一个元素
        // 3.若当前层级小于iframeNameList中的总数，则需要分情况：
        // I.若层级为0，则记录当前元素无父窗体，并且清空iframeNameList
        // II.若层级大于0且小于iframeNameList中的总数，则根据层级，逐个移除窗体名称，并记录当前元素为移除后iframeNameList中最后一个元素
        if (index > iframeNameList.size()) {
            iframeNameList.add(nowElementName);
            noFileLocation.putIframeName(name, nowElementName);
        } else if (index == iframeNameList.size()) {
            noFileLocation.putIframeName(name, iframeNameList.get(iframeNameList.size() - 1));
        } else {
            if (index == 0) {
                iframeNameList.clear();
            } else {
                noFileLocation.putIframeName(name, iframeNameList.get(index - 1));
                iframeNameList = iframeNameList.subList(0, index);
            }
        }
    }

    /**
     * 用于分析元素信息
     * 
     * @param elementText 元素信息
     * @throws UndefinedElementException 元素信息不足或元素缺少关键信息时抛出的异常
     * @return 元素名称
     */
    protected String analysisElementInformation(String elementText) {
        // 去除层级标识符
        elementText = elementText.replaceAll("\\" + levelSplitSign, "");

        // 对元素内容按照元素信息切分符号进行切分
        String[] elementInfoTexts = elementText.split(infoSplitSign);
        int length = elementInfoTexts.length;
        // 若切分得到的数组其个数少于3个，则其必定缺少某个必要信息，则抛出异常
        if (length < 3) {
            throw new UndefinedElementException("元素信息不足，缺少必要信息！元素分隔符号：" + infoSplitSign + "；元素内容：" + elementText);
        }

        switch(length) {
        case 6:
            noFileLocation.putBeforeTime(elementInfoTexts[0], toWaitTime(elementInfoTexts[5]));
        case 5:
            noFileLocation.putElementType(elementInfoTexts[0], toElementType(elementInfoTexts[4]));
        case 4:
            noFileLocation.putWaitTime(elementInfoTexts[0], toWaitTime(elementInfoTexts[3]));
        case 3:
            // 存储元素名称
            noFileLocation.putElementLocation(elementInfoTexts[0], toByType(elementInfoTexts[1]), elementInfoTexts[2]);
            break;
        default:
            throw new UndefinedElementException("元素信息不足，缺少必要信息！元素分隔符号：" + infoSplitSign + "；元素内容：" + elementText);
        }

        return elementInfoTexts[0];
    }

    @Override
    public ReadLocation find(String name) {
        return noFileLocation.find(name);
    }

    @Override
    public long getBeforeTime() {
        return noFileLocation.getBeforeTime();
    }
}
