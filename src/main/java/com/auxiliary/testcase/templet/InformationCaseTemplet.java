package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：InformationTempletCase.java</b>
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * <b>编码时间：2023年1月19日 上午9:56:13
 * </p>
 * <p>
 * <b>修改时间：2023年1月19日 上午9:56:13
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public class InformationCaseTemplet extends AbstractPresetCaseTemplet {
    /**
     * 编辑操作名称
     */
    public final String OPERAT_EDIT = "编辑";
    /**
     * 新增操作名称
     */
    public final String OPERAT_ADD = "新增";

    /**
     * 最小数字限制
     */
    public static final int MIN_NUMBER = Integer.MIN_VALUE;
    /**
     * 最大数字限制
     */
    public static final int MAX_NUMBER = Integer.MAX_VALUE;

    /**
     * 构造对象，并指定读取的模板xml文件
     * 
     * @param xmlTempletFile 用例模板文件类对象
     * @since autest 4.0.0
     */
    public InformationCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    /**
     * 构造对象，通过包内的默认模板，对类进行构造
     * 
     * @since autest 4.0.0
     */
    public InformationCaseTemplet() {
        super("AddInformation");
    }

    /**
     * 该方法用于替换“#成功预期前文#”和“#成功预期后文#”两个占位符的内容
     * 
     * @param beforeSuccessText 替换“#成功预期前文#”占位符的内容
     * @param afterSuccessText  替换“#成功预期后文#”占位符的内容
     * @return 类本身
     * @since autest 4.0.0
     */
    public InformationCaseTemplet setSuccessExceptContent(String beforeSuccessText, String afterSuccessText) {
        addReplaceWord(ReplaceWord.SUCCESS_EXCEPT_TEXT_START, Optional.ofNullable(beforeSuccessText).orElse(""));
        addReplaceWord(ReplaceWord.SUCCESS_EXCEPT_TEXT_END, Optional.ofNullable(afterSuccessText).orElse(""));
        return this;
    }

    /**
     * 该方法用于替换“#失败预期前文#”和“#失败预期后文#”两个占位符的内容
     * 
     * @param beforeFailText 替换“#失败预期前文#”占位符的内容
     * @param afterFailText  替换“#失败预期后文#”占位符的内容
     * @return 类本身
     * @since autest 4.0.0
     */
    public InformationCaseTemplet setFailExceptContent(String beforeFailText, String afterFailText) {
        addReplaceWord(ReplaceWord.FAIL_EXCEPT_TEXT_START, Optional.ofNullable(beforeFailText).orElse(""));
        addReplaceWord(ReplaceWord.FAIL_EXCEPT_TEXT_END, Optional.ofNullable(afterFailText).orElse(""));
        return this;
    }

    /**
     * 该方法用于替换“#保存按钮名称#”占位符的
     * 
     * @param saveButtonName 替换“#保存按钮名称#”占位符的内容
     * @return 类本身
     * @since autest 4.0.0
     */
    public InformationCaseTemplet setSaveButtonName(String saveButtonName) {
        addReplaceWord(ReplaceWord.SAVE_BUTTON_NAME, Optional.ofNullable(saveButtonName).orElse(""));
        return this;
    }

    /**
     * 该方法用于替换“#信息#”占位符的
     * 
     * @param informationName 替换“#信息#”占位符的内容
     * @return 类本身
     * @since autest 4.0.0
     */
    public InformationCaseTemplet setInformationName(String informationName) {
        addReplaceWord(ReplaceWord.ADD_INFORMATION, Optional.ofNullable(informationName).orElse(""));
        return this;
    }

    /**
     * 该方法用于生成正确填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addWholeInformationCase() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT,
                        new String[] { "输入成功预期" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "1" })));
        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays.asList(
                        new Entry<>(AddInformationTemplet.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "1" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成不完全填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addUnWholeInformationCase() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_UNWHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_UNWHOLE_INFORMATION_CASE,
                        new String[] { "1", "2", "3" })));
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT,
                        new String[] { "失败预期", "输入成功预期", "失败预期" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "2" })));
        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "1" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于添加文本框相关的基本测试用例
     * 
     * @param isMust         是否必填
     * @param isRepeat       是否允许重复提交
     * @param isClear        是否允许清空
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）组
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> textboxCommonCase(String name, String operationName,
            boolean isMust, boolean isRepeat, boolean isClear, InputRuleType... inputRuleTypes) {
        // 添加替换词语
        addReplaceWord(ReplaceWord.OPERATION_TYPE, operationName);
        addReplaceWord(ReplaceWord.CONTROL_NAME, name);

        // 转换输入限制
        Set<InputRuleType> inputRuleTypeSet = new HashSet<>();
        if (inputRuleTypes != null && inputRuleTypes.length != 0) {
            for (InputRuleType inputRuleType : inputRuleTypes) {
                inputRuleTypeSet.add(inputRuleType);
            }
        }

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "1" })));

        // 步骤与预期
        List<Entry<String, String[]>> stepList = new ArrayList<>();
        List<Entry<String, String[]>> exceptList = new ArrayList<>();
        stepList.add(new Entry<>(AddInformationTemplet.GROUP_TEXTBOX_BASIC_CASE, new String[] { "1", "2" }));
        if (isMust) {
            exceptList.add(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "失败预期", "失败预期" }));
        } else {
            exceptList
                    .add(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "空成功预期", "空成功预期" }));
        }

        // 判断是否没有输入限制，或者允许输入大、小写字母和特殊字符，以添加SQL注入漏洞与XSS攻击漏洞用例
        if (inputRuleTypeSet.isEmpty()
                || ((inputRuleTypeSet.contains(InputRuleType.LOW) || inputRuleTypeSet.contains(InputRuleType.CAP))
                        && inputRuleTypeSet.contains(InputRuleType.SPE))) {
            stepList.add(new Entry<>(AddInformationTemplet.GROUP_TEXTBOX_BASIC_CASE, new String[] { "3", "8" }));
            exceptList
                    .add(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "输入成功预期", "输入成功预期" }));
        }
        // 判断是否允许输入emoji表情
        if (inputRuleTypeSet.size() == 0 || inputRuleTypeSet.contains(InputRuleType.EMOJI)) {
            stepList.add(new Entry<>(AddInformationTemplet.GROUP_TEXTBOX_BASIC_CASE, new String[] { "7" }));
            exceptList.add(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "输入成功预期" }));
        }
        // 判断是否有输入限制，以添加输入限制相关的用例
        if (inputRuleTypeSet.size() != 0) {
            // 拼接输入限制
            StringJoiner inputRule = new StringJoiner("、");
            inputRuleTypeSet.stream().map(InputRuleType::getName).forEach(inputRule::add);
            // 添加替换词以及用例
            addReplaceWord(ReplaceWord.INPUT_RULE, inputRule.toString());
            stepList.add(new Entry<>(AddInformationTemplet.GROUP_TEXTBOX_BASIC_CASE, new String[] { "4" }));
            exceptList.add(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "失败预期" }));
        }
        // 判断是否能清空内容，添加清空后提交的用例
        if (isClear) {
            stepList.add(new Entry<>(AddInformationTemplet.GROUP_TEXTBOX_BASIC_CASE, new String[] { "6" }));
            exceptList.add(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT,
                    new String[] { isMust ? "失败预期" : "输入成功预期" }));
        }
        // 添加重复提交相关的用例
        stepList.add(new Entry<>(AddInformationTemplet.GROUP_TEXTBOX_BASIC_CASE, new String[] { "5" }));
        exceptList.add(
                new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { isRepeat ? "输入成功预期" : "失败预期" }));

        // 添加步骤与预期
        addContent(allContentMap, LabelType.STEP, stepList);
        addContent(allContentMap, LabelType.EXCEPT, exceptList);
        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "1" })));

        // 根据操作名称，添加前置条件
        if (Objects.equals(OPERAT_ADD, operationName)) {
            addContent(allContentMap, LabelType.PRECONDITION,
                    Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "1" })));
            addContent(allContentMap, LabelType.PRECONDITION,
                    Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "3" })));
        } else {
            addContent(allContentMap, LabelType.PRECONDITION,
                    Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "2" })));
            addContent(allContentMap, LabelType.PRECONDITION,
                    Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "4" })));
        }

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中对普通文本测试的用例
     * 
     * @param name           控件名称
     * @param isMust         是否必填
     * @param isRepeat       是否允许保存相同内容
     * @param isClear        是否可以通过按钮清除内容
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）组
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addBasicTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            InputRuleType... inputRuleTypes) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(name, OPERAT_ADD, isMust,
                isRepeat, isClear, inputRuleTypes);
        
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "2" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成编辑信息中对普通文本测试的用例
     * 
     * @param name           控件名称
     * @param isMust         是否必填
     * @param isRepeat       是否允许保存相同内容
     * @param isClear        是否可以通过按钮清除内容
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）组
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editBasicTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            InputRuleType... inputRuleTypes) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(name, OPERAT_EDIT, isMust,
                isRepeat, isClear, inputRuleTypes);

        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "2" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成带长度限制的文本框测试用例的基本内容
     * 
     * @param operationName  操作类型名称
     * @param name           控件名称
     * @param isMust         是否必填
     * @param isRepeat       是否可以与存在的内容重复
     * @param isClear        是否有按钮可以清空文本框
     * @param minLen         最小输入长度限制
     * @param maxLen         最大输入长度限制
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）组
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> lengthRuleTextboxCase(String operationName, String name,
            boolean isMust, boolean isRepeat, boolean isClear, int minLen, int maxLen,
            InputRuleType... inputRuleTypes) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(name, operationName, isMust,
                isRepeat, isClear, inputRuleTypes);

        // 添加输入长度限制相关的测试用例
        // 判断传入的限制参数是否有小于0的参数，参数小于0则直接转换为0
        minLen = minLen < 0 ? 0 : minLen;
        maxLen = maxLen < 0 ? 0 : maxLen;
        // 判断是否存在最小长度限制
        if (minLen != 0) {
            addReplaceWord(ReplaceWord.INPUT_MIN_LENGTH, String.valueOf(minLen));
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_TEXTBOX_CASE, new String[] { "1", "2" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "失败预期", "输入成功预期" })));
        }
        // 判断是否存在最大长度限制
        if (maxLen != 0) {
            addReplaceWord(ReplaceWord.INPUT_MAX_LENGTH, String.valueOf(maxLen));
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_TEXTBOX_CASE, new String[] { "3", "4" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "失败预期", "输入成功预期" })));
        }

        // 根据是否必填，添加其文本框的优先级
        addContent(allContentMap, LabelType.RANK, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { isMust ? "1" : "2" })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中带长度限制的文本框测试用例
     * <p>
     * 传入长度限制的方法如下：
     * <ol>
     * <li>输入长度限制为2~10个字符时：addLengthRuleTextboxCase(..., 2, 10, ...)</li>
     * <li>输入长度限制为最多输入10个字符时：addLengthRuleTextboxCase(..., 0, 10, ...)</li>
     * <li>输入长度限制为最少输入2个字符时：addLengthRuleTextboxCase(..., 2, 0, ...)</li>
     * <li>输入长度限制仅能输入2个字符时：addLengthRuleTextboxCase(..., 2, 2, ...)</li>
     * </ol>
     * </p>
     * <p>
     * <b>注意：</b>若最长、最短限制均传入小于等于0的数值时，则当成无任何长度限制的文本框用例进行返回
     * </p>
     * 
     * @param name           控件名称
     * @param isMust         是否必填
     * @param isRepeat       是否可以与存在的内容重复
     * @param isClear        是否有按钮可以清空文本框
     * @param minLen         最小输入长度限制
     * @param maxLen         最大输入长度限制
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addLengthRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            int minLen, int maxLen, InputRuleType... inputRuleTypes) {
        return createCaseDataList(this,
                lengthRuleTextboxCase(OPERAT_ADD, name, isMust, isRepeat, isClear, minLen, maxLen, inputRuleTypes));
    }

    /**
     * 该方法用于生成编辑信息中带长度限制的文本框测试用例
     * <p>
     * 传入长度限制的方法可参考生成新增用例的方法{@link #addLengthRuleTextboxCase(String, boolean, boolean, boolean, int, int, InputRuleType...)}
     * </p>
     * 
     * @param name           控件名称
     * @param isMust         是否必填
     * @param isRepeat       是否可以与存在的内容重复
     * @param isClear        是否有按钮可以清空文本框
     * @param minLen         最小输入长度限制
     * @param maxLen         最大输入长度限制
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editLengthRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            int minLen, int maxLen, InputRuleType... inputRuleTypes) {
        return createCaseDataList(this,
                lengthRuleTextboxCase(OPERAT_EDIT, name, isMust, isRepeat, isClear, minLen, maxLen, inputRuleTypes));
    }

    protected Map<LabelType, List<Entry<String, String[]>>> numberRuleTextboxCase(String operationName, String name,
            boolean isMust, boolean isRepeat, boolean isClear, int decimals, int minNum, int maxNum) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(name, operationName, isMust,
                isRepeat, isClear, InputRuleType.NUM);

        // 添加输入数字限制相关的测试用例
        // 判断最大最小值是否一致，若一致，则按照只包含最大限制生成用例
        if (minNum == maxNum) {
            minNum = MIN_NUMBER;
        }
        // 判断最大与最小值是否需要调换，存储正确最大最小值
        if (minNum > maxNum) {
            int temp = minNum;
            minNum = maxNum;
            maxNum = temp;
        }

        // 存储数字限制
        addReplaceWord(ReplaceWord.INPUT_MIN_NUMBER, String.valueOf(minNum));
        addReplaceWord(ReplaceWord.INPUT_MAX_NUMBER, String.valueOf(maxNum));

        // 判断是否有最小数字限制
        if (minNum != MIN_NUMBER) {
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_TEXTBOX_CASE, new String[] { "6", "7" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "失败预期", "输入成功预期" })));
        }
        // 判断是否有最大数字限制
        if (maxNum != MAX_NUMBER) {
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_TEXTBOX_CASE, new String[] { "8", "9" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "失败预期", "输入成功预期" })));
        }

        // 若传入的小数位数大于0，则添加小数位相关的用例
        if (decimals > 0) {
            // 存储小数位数
            addReplaceWord(ReplaceWord.INPUT_DECIMALS, String.valueOf(decimals));
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_TEXTBOX_CASE,
                            new String[] { "10", "11", "12" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT,
                            new String[] { "失败预期", "输入成功预期", "输入成功预期" })));
        }

        return allContentMap;
    }

    /**
     * <p>
     * <b>文件名：InformationTempletCase.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记当前用例模板中存在的用例组名称
     * </p>
     * <p>
     * <b>编码时间：2023年1月31日 上午8:14:52
     * </p>
     * <p>
     * <b>修改时间：2023年1月31日 上午8:14:52
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.0.0
     */
    public class AddInformationTemplet {
        // 分组名称
        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String GROUP_TEXTBOX_BASIC_CASE = "textboxBasicCase";
        public static final String GROUP_ADD_TEXTBOX_CASE = "addTextboxCase";
        public static final String GROUP_ADD_SELECTBOX_CASE = "addSelectboxCase";
        public static final String GROUP_ADD_RADIO_BUTTON_CASE = "addRadioButtonCase";
        public static final String GROUP_ADD_CHECKBOX_CASE = "addCheckboxCase";
        public static final String GROUP_ADD_DATE_CASE = "addDateCase";
        public static final String GROUP_ADD_PHONE_CASE = "addPhoneCase";
        public static final String GROUP_ADD_ID_CARD_CASE = "addIDCardCase";
        public static final String GROUP_ADD_UPLOAD_IMAGE_CASE = "addUploadImageCase";
        public static final String GROUP_ADD_UPLOAD_FILE_CASE = "addUploadFileCase";
        public static final String GROUP_ADD_WHOLE_INFORMATION_CASE = "addWholeInformationCase";
        public static final String GROUP_EXAMINE_UI = "examineUI";
        public static final String GROUP_ADD_UNWHOLE_INFORMATION_CASE = "addUnWholeInformationCase";
        public static final String GROUP_OPEN_EDIT_PAGE = "openEditPage";
        public static final String GROUP_CANCEL_SAVE_DATA = "cancelSaveData";

        // TODO 后续补充每个分组中id的名称
    }
    
    /**
     * <p>
     * <b>文件名：InformationCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义在默认模板中所有的替换词语
     * </p>
     * <p>
     * <b>编码时间：2023年3月2日 上午8:25:57
     * </p>
     * <p>
     * <b>修改时间：2023年3月2日 上午8:25:57
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.0.0
     */
    protected class ReplaceWord {
        public static final String SAVE_BUTTON_NAME = "保存按钮名称";
        public static final String CENCEL_BUTTON_NAME = "取消按钮名称";
        public static final String ADD_INFORMATION = "信息";
        public static final String SUCCESS_EXCEPT_TEXT_START = "成功预期前文";
        public static final String SUCCESS_EXCEPT_TEXT_END = "成功预期后文";
        public static final String FAIL_EXCEPT_TEXT_START = "失败预期前文";
        public static final String FAIL_EXCEPT_TEXT_END = "失败预期后文";
        public static final String CONTROL_NAME = "控件名称";
        public static final String INPUT_RULE = "输入限制";
        public static final String INPUT_MAX_LENGTH = "最长长度限制";
        public static final String INPUT_MIN_LENGTH = "最短长度限制";
        public static final String INPUT_MAX_NUMBER = "数字最大限制";
        public static final String INPUT_MIN_NUMBER = "数字最小限制";
        public static final String INPUT_DECIMALS = "小数位数";
        public static final String END_DATE = "结束日期";
        public static final String START_DATE = "开始日期";
        public static final String FILE_TYPE = "文件类型";
        public static final String UPLOAD_MAX_LENGTH = "文件最大个数";
        public static final String UPLOAD_MIN_LENGTH = "文件最小个数";
        public static final String FILE_SIZE = "文件大小";
        public static final String FILE_RULE = "文件格式";
        public static final String OPERATION_TYPE = "操作类型";
    }

    /**
     * <p>
     * <b>文件名：</b>InputRuleType.java
     * </p>
     * <p>
     * <b>用途：</b>用于枚举控件中可输入字符类型的限制
     * </p>
     * <p>
     * <b>编码时间：</b>2020年3月14日 下午9:14:30
     * </p>
     * <p>
     * <b>修改时间：</b>2023年3月8日 上午8:13:53
     * </p>
     * 
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 2.6.0
     */
    public enum InputRuleType {
        /**
         * 中文
         */
        CH("中文"),
        /**
         * 数字
         */
        NUM("数字"),
        /**
         * 特殊字符
         */
        SPE("特殊字符"),
        /**
         * 小写字母
         */
        LOW("小写字母"),
        /**
         * 大写字母
         */
        CAP("大写字母"),
        /**
         * emoji表情
         * 
         * @since autest 4.0.0
         */
        EMOJI("emoji表情");

        /**
         * 枚举名称
         */
        private String name;

        /**
         * 初始化枚举名称
         * 
         * @param name 枚举名称
         */
        private InputRuleType(String name) {
            this.name = name;
        }

        /**
         * 返回枚举名称
         * 
         * @return 枚举名称
         */
        public String getName() {
            return name;
        }
    }

    /**
     * <p>
     * <b>文件名：</b>InputRuleType.java
     * </p>
     * <p>
     * <b>用途：</b>用于枚举号码限制
     * </p>
     * <p>
     * <b>编码时间：</b>2020年3月14日 下午9:14:30
     * </p>
     * <p>
     * <b>修改时间：</b>2020年3月14日 下午9:14:30
     * </p>
     * 
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     */
    public enum PhoneType {
        /**
         * 设置类型为移动电话（手机）
         */
        MOBLE,
        /**
         * 设置类型为固定电话（座机）
         */
        FIXED;
    }

    /**
     * <p>
     * <b>文件名：</b>InformationCase.java
     * </p>
     * <p>
     * <b>用途：</b>用于枚举上传文件的格式
     * </p>
     * <p>
     * <b>编码时间：</b>2020年3月18日上午9:30:34
     * </p>
     * <p>
     * <b>修改时间：</b>2020年3月18日上午9:30:34
     * </p>
     * 
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     *
     */
    public enum FileRuleType {
        /**
         * jpg格式
         */
        JPG("jpg"),
        /**
         * gif格式
         */
        GIF("gif"),
        /**
         * png格式
         */
        PNG("png"),
        /**
         * bmp格式
         */
        BMP("bmp"),
        /**
         * doc格式
         */
        DOC("doc"),
        /**
         * docx格式
         */
        DOCX("docx"),
        /**
         * txt格式
         */
        TXT("txt"),
        /**
         * xls格式
         */
        XLS("xls"),
        /**
         * xlsx格式
         */
        XLSX("xlsx"),
        /**
         * pdf格式
         */
        PDF("pdf"),
        /**
         * csv格式
         */
        CSV("csv"),;

        /**
         * 文件格式名称
         */
        private String name;

        /**
         * 初始化枚举名称
         * 
         * @param name 枚举名称
         */
        private FileRuleType(String name) {
            this.name = name;
        }

        /**
         * 返回格式名称
         */
        public String getName() {
            return name;
        }
    }

    /**
     * <p>
     * <b>文件名：</b>InformationCase.java
     * </p>
     * <p>
     * <b>用途：</b>用于枚举上传文件的类型
     * </p>
     * <p>
     * <b>编码时间：</b>2020年3月18日上午9:56:51
     * </p>
     * <p>
     * <b>修改时间：</b>2020年3月18日上午9:56:51
     * </p>
     * 
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     *
     */
    public enum UploadFileType {
        /**
         * 上传文件
         */
        FILE("文件"),
        /**
         * 上传图片
         */
        IMAGE("图片");

        /**
         * 上传文件类型名称
         */
        private String name;

        /**
         * 返回枚举名称
         * 
         * @return 枚举名称
         */
        public String getName() {
            return name;
        }

        /**
         * 初始化枚举值名称
         * 
         * @param name 枚举值名称
         */
        private UploadFileType(String name) {
            this.name = name;
        }
    }
}
