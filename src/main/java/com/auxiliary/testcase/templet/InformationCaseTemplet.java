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
 * <b>用途：</b>用于根据相关的测试用例模板，输出与页面新增、编辑和注册信息相关的用例，亦可根据部分参数的不同，适配更多自定义的内容，但其生成的测试用例可能不符合预期。
 * 相关的测试用例模板可参考“<a href=
 * 'https://gitee.com/pyqone/autest/blob/master/src/main/resources/com/auxiliary/testcase/templet/AddInformation.xml'>AddInformation.xml</a>”文件
 * </p>
 * <p>
 * <b>编码时间：2023年1月19日 上午9:56:13
 * </p>
 * <p>
 * <b>修改时间：2023年3月31日 下午3:52:44
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public class InformationCaseTemplet extends AbstractPresetCaseTemplet implements StepDetailTemplet {
    /**
     * 编辑操作名称
     */
    public final String OPERATION_EDIT = "编辑";
    /**
     * 新增操作名称
     */
    public final String OPERATION_ADD = "新增";
    /**
     * 注册账号操作
     */
    public final String OPERATION_REGISTERED = "注册";

    /**
     * 新增或编辑返回页页面名称
     */
    protected final String PAGE_NAME_ADD_OR_EDIT = "列表";
    /**
     * 注册返回页页面名称
     */
    protected final String PAGE_NAME_REGISTERED = "登录";

    /**
     * 新增或编辑时，成功输入的返回文本
     */
    protected final String BREAK_TEXT_ADD_OR_EDIT = "其#信息#列表上或详情中显示";
    /**
     * 注册时，成功输入的返回文本
     */
    protected final String BREAK_TEXT_REGISTERED = "可使用注册的信息进行登录，登录后，其账号";

    /**
     * 构造对象，并指定读取的模板xml文件
     * 
     * @param xmlTempletFile 用例模板文件类对象
     * @since autest 4.0.0
     */
    public InformationCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);

        // 默认成功与失败预期前后文都为空
        setSuccessExceptContent("", "");
        setFailExceptContent("", "");
    }

    /**
     * 构造对象，通过包内的默认模板，对类进行构造
     * 
     * @since autest 4.0.0
     */
    public InformationCaseTemplet() {
        super("WriteInformationCaseTemplet");

        // 默认成功与失败预期前后文都为空
        setSuccessExceptContent("", "");
        setFailExceptContent("", "");
    }

    @Override
    public void setReadStepDetail(boolean isStepDetail, boolean isStepIndependentCase) {
        this.isStepDetail = isStepDetail;
        this.isStepIndependentCase = isStepIndependentCase;
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
        addReplaceWord(WriteInformationCaseTempletReplaceWord.SUCCESS_EXCEPT_TEXT_START, Optional.ofNullable(beforeSuccessText).orElse(""));
        addReplaceWord(WriteInformationCaseTempletReplaceWord.SUCCESS_EXCEPT_TEXT_END, Optional.ofNullable(afterSuccessText).orElse(""));
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
        addReplaceWord(WriteInformationCaseTempletReplaceWord.FAIL_EXCEPT_TEXT_START, Optional.ofNullable(beforeFailText).orElse(""));
        addReplaceWord(WriteInformationCaseTempletReplaceWord.FAIL_EXCEPT_TEXT_END, Optional.ofNullable(afterFailText).orElse(""));
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
        if (saveButtonName != null) {
            addReplaceWord(WriteInformationCaseTempletReplaceWord.SAVE_BUTTON_NAME, saveButtonName);
        }
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
        if (informationName != null) {
            addReplaceWord(WriteInformationCaseTempletReplaceWord.ADD_INFORMATION, informationName);
        }
        return this;
    }

    /**
     * 该方法用于生成正确填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> wholeInformationCase(String operationName) {
        addReplaceWord(WriteInformationCaseTempletReplaceWord.OPERATION_TYPE, operationName);
        switch (operationName) {
        case OPERATION_REGISTERED:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_REGISTERED);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_REGISTERED);
            break;
        case OPERATION_ADD:
        case OPERATION_EDIT:
        default:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_ADD_OR_EDIT);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_ADD_OR_EDIT);
            break;
        }

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_ALL_SUCCESS_EXCEPT })));

        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1 })));
        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_PRECONDITION_ALREADY_ON_THE_PAGE })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息时正确填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addWholeInformationCase() {
        return createCaseDataList(this, wholeInformationCase(OPERATION_ADD));
    }

    /**
     * 该方法用于生成注册信息时正确填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredWholeInformationCase() {
        return createCaseDataList(this, wholeInformationCase(OPERATION_REGISTERED));
    }

    /**
     * 该方法用于生成不完全填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> unWholeInformationCase(String operationName) {
        addReplaceWord(WriteInformationCaseTempletReplaceWord.OPERATION_TYPE, operationName);
        switch (operationName) {
        case OPERATION_REGISTERED:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_REGISTERED);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_REGISTERED);
            break;
        case OPERATION_ADD:
        case OPERATION_EDIT:
        default:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_ADD_OR_EDIT);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_ADD_OR_EDIT);
            break;
        }

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UNWHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays.asList(
                new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UNWHOLE_INFORMATION_CASE, new String[] { "1", "2", "3" })));
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_ALL_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));
        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));
        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_PRECONDITION_ALREADY_ON_THE_PAGE })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息时不完全填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addUnWholeInformationCase() {
        return createCaseDataList(this, unWholeInformationCase(OPERATION_ADD));
    }

    /**
     * 该方法用于生成注册信息时不完全填写所有信息的用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> registeredUnWholeInformationCase() {
        return createCaseDataList(this, unWholeInformationCase(OPERATION_REGISTERED));
    }

    /**
     * 该方法用于添加用例公共内容
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> addCommonData(String operationName, String name) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 添加替换词语
        addReplaceWord(WriteInformationCaseTempletReplaceWord.OPERATION_TYPE, operationName);
        addReplaceWord(WriteInformationCaseTempletReplaceWord.CONTROL_NAME, name);
        switch (operationName) {
        case OPERATION_REGISTERED:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_REGISTERED);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_REGISTERED);
            break;
        case OPERATION_ADD:
        case OPERATION_EDIT:
        default:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_ADD_OR_EDIT);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_ADD_OR_EDIT);
            break;
        }
        
        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_TITLE_BASIC })));
        
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_PRECONDITION_ALREADY_ON_THE_PAGE })));
        // 根据操作名称，添加前置条件
        switch(operationName) {
        case OPERATION_ADD:
        case OPERATION_REGISTERED:
            addContent(allContentMap, LabelType.PRECONDITION,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_PRECONDITION_SUCCESS_INPUT })));
            break;
        case OPERATION_EDIT:
            addContent(allContentMap, LabelType.PRECONDITION,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_PRECONDITION_NOT_MODIFY })));
            break;
        default:
            break;
        }

        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_KEY_BASIC })));

        return allContentMap;
    }

    /**
     * 该方法用于添加文本框相关的基本测试用例
     * 
     * @param operationName  操作类型名称
     * @param name           控件名称
     * @param isMust         是否必填
     * @param isRepeat       是否允许重复提交
     * @param isClear        是否允许清空
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）组
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> textboxCommonCase(String operationName, String name,
            boolean isMust, boolean isRepeat, boolean isClear, InputRuleType... inputRuleTypes) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = addCommonData(operationName, name);

        // 转换输入限制
        Set<InputRuleType> inputRuleTypeSet = new HashSet<>(
                Arrays.asList(Optional.ofNullable(inputRuleTypes).orElseGet(() -> new InputRuleType[] {})));

        // 步骤与预期
        List<Entry<String, String[]>> stepList = new ArrayList<>();
        List<Entry<String, String[]>> exceptList = new ArrayList<>();
        stepList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_TEXTBOX_BASIC_CASE, new String[] { "1", "2" }));
        if (isMust) {
            exceptList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                    new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                            WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT }));
        } else {
            exceptList
                    .add(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_EMPTY_SUCCESS_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_EMPTY_SUCCESS_EXCEPT }));
        }

        // 判断是否没有输入限制，或者允许输入大、小写字母和特殊字符，以添加SQL注入漏洞与XSS攻击漏洞用例
        if (inputRuleTypeSet.isEmpty()
                || ((inputRuleTypeSet.contains(InputRuleType.LOW) || inputRuleTypeSet.contains(InputRuleType.CAP))
                        && inputRuleTypeSet.contains(InputRuleType.SPE))) {
            stepList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_TEXTBOX_BASIC_CASE, new String[] { "3", "8" }));
            exceptList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                    new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT,
                            WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT }));
        }
        // 判断是否允许输入emoji表情
        if (inputRuleTypeSet.size() == 0 || inputRuleTypeSet.contains(InputRuleType.EMOJI)) {
            stepList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_TEXTBOX_BASIC_CASE, new String[] { "7" }));
            exceptList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                    new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT }));
        }
        // 判断是否有输入限制，以添加输入限制相关的用例
        if (!inputRuleTypeSet.isEmpty()) {
            // 拼接输入限制
            StringJoiner inputRule = new StringJoiner("、");
            inputRuleTypeSet.stream().map(InputRuleType::getName).forEach(inputRule::add);
            // 添加替换词以及用例
            addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_RULE, inputRule.toString());
            stepList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_TEXTBOX_BASIC_CASE, new String[] { "4" }));
            exceptList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                    new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT }));
        }
        // 判断是否能清空内容，添加清空后提交的用例
        if (isClear) {
            stepList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_TEXTBOX_BASIC_CASE, new String[] { "6" }));
            exceptList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                    new String[] { isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT
                            : WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_EMPTY_SUCCESS_EXCEPT }));
        }
        // 添加重复提交相关的用例
        stepList.add(new Entry<>(WriteInformationCaseTempletField.GROUP_TEXTBOX_BASIC_CASE, new String[] { "5" }));
        exceptList.add(
                new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isRepeat ? WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT
                                : WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT }));

        // 添加步骤与预期
        addContent(allContentMap, LabelType.STEP, stepList);
        addContent(allContentMap, LabelType.EXCEPT, exceptList);


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
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(OPERATION_ADD, name, isMust,
                isRepeat, isClear, inputRuleTypes);
        
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

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
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(OPERATION_EDIT, name, isMust,
                isRepeat, isClear, inputRuleTypes);

        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成注册信息中对普通文本测试的用例
     * 
     * @param name           控件名称
     * @param isMust         是否必填
     * @param isRepeat       是否允许保存相同内容
     * @param isClear        是否可以通过按钮清除内容
     * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）组
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredBasicTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            InputRuleType... inputRuleTypes) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(OPERATION_REGISTERED, name,
                isMust, isRepeat, isClear, inputRuleTypes);

        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

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
            boolean isMust, boolean isRepeat, boolean isClear, Integer minLen, Integer maxLen,
            InputRuleType... inputRuleTypes) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(operationName, name, isMust,
                isRepeat, isClear, inputRuleTypes);

        // 添加输入长度限制相关的测试用例
        // 判断最大最小值是否一致
        if (Objects.equals(minLen, maxLen) && minLen != null && minLen != 0) {
            addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_MIN_LENGTH, String.valueOf(minLen));
            addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_MAX_LENGTH, String.valueOf(maxLen));
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_TEXTBOX_CASE, new String[] { "1", "2", "3" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));
        } else {
            // 判断最大与最小值是否需要调换，存储正确最大最小值
            if (minLen != null && maxLen != null && minLen > maxLen) {
                int temp = minLen;
                minLen = maxLen;
                maxLen = temp;
            }
            // 判断是否存在最小长度限制
            if (Optional.ofNullable(minLen).filter(min -> min > 0).isPresent()) {
                addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_MIN_LENGTH, String.valueOf(minLen));
                addContent(allContentMap, LabelType.STEP, Arrays
                        .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_TEXTBOX_CASE, new String[] { "1", "2" })));
                addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                        WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
            }
            // 判断是否存在最大长度限制
            if (Optional.ofNullable(maxLen).filter(max -> max > 0).isPresent()) {
                addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_MAX_LENGTH, String.valueOf(maxLen));
                addContent(allContentMap, LabelType.STEP, Arrays
                        .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_TEXTBOX_CASE, new String[] { "3", "4" })));
                addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                        WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
            }
        }

        // 根据是否必填，添加其文本框的优先级
        addContent(allContentMap, LabelType.RANK, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1
                                : WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中带长度限制的文本框测试用例
     * <p>
     * 传入长度限制的方法如下：
     * <ol>
     * <li>输入长度限制为2~10个字符时：{@code addLengthRuleTextboxCase("姓名", true, true, true, 2, 10)}</li>
     * <li>输入长度限制为最多输入10个字符时：{@code addLengthRuleTextboxCase("姓名", true, true, true, 0, 10)}</li>
     * <li>输入长度限制为最少输入2个字符时：{@code addLengthRuleTextboxCase("姓名", true, true, true, 2, 0)}</li>
     * <li>输入长度限制仅能输入2个字符时：{@code addLengthRuleTextboxCase("姓名", true, true, true, 2, 2)}</li>
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
            Integer minLen, Integer maxLen, InputRuleType... inputRuleTypes) {
        return createCaseDataList(this,
                lengthRuleTextboxCase(OPERATION_ADD, name, isMust, isRepeat, isClear, minLen, maxLen, inputRuleTypes));
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
            Integer minLen, Integer maxLen, InputRuleType... inputRuleTypes) {
        return createCaseDataList(this,
                lengthRuleTextboxCase(OPERATION_EDIT, name, isMust, isRepeat, isClear, minLen, maxLen, inputRuleTypes));
    }

    /**
     * 该方法用于生成注册信息中带长度限制的文本框测试用例
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
     * @since autest 4.1.0
     */
    public List<CaseData> registeredLengthRuleTextboxCase(String name, boolean isMust, boolean isRepeat,
            boolean isClear, Integer minLen, Integer maxLen, InputRuleType... inputRuleTypes) {
        return createCaseDataList(this,
                lengthRuleTextboxCase(OPERATION_REGISTERED, name, isMust, isRepeat, isClear, minLen, maxLen,
                        inputRuleTypes));
    }

    /**
     * 该方法用于生成带数字限制的文本框测试用例
     * <p>
     * 该方法可以生成带数字大小限制的输入文本框，亦可生成带小数位限制的文本框，其传参方式如下：
     * </p>
     * <p>
     * 带数字大小限制的文本框：
     * <ul>
     * <li>数字最小输入限制为2：{@code addNumberRuleTextboxCase("成功率", true, true, true, 1, null, null)}</li>
     * <li>数字最大输入限制为10：{@code addNumberRuleTextboxCase("成功率", true, true, true, null, 10, null)}</li>
     * <li>数字输入限制为2~10：{@code addNumberRuleTextboxCase("成功率", true, true, true, 2, 10, null)}</li>
     * </ul>
     * </p>
     * <p>
     * 带小数位限制的文本框：
     * <ul>
     * <li>小数位限制为2位：{@code addNumberRuleTextboxCase("成功率", true, true, true, null, null, 2)}</li>
     * <li>无小数位限制：{@code addNumberRuleTextboxCase("成功率", true, true, true, null, null, null)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>以上案例中，小数位与数字限制可同时使用，例如，数字输入限制为2~10，小数位限制2位：{@code addNumberRuleTextboxCase("成功率", true, true, true, 2, 10, 2)}</li>
     * <li>当最大、最小数字限制与小数位限制都传入null时，则按照只限制输入数字的文本框处理</li>
     * <li></li>
     * </ol>
     * </p>
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isRepeat      是否可以与存在的内容重复
     * @param isClear       是否有按钮可以清空文本框
     * @param minNum        数字最大限制
     * @param maxNum        数字最小限制
     * @param decimals      小数位限制
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> numberRuleTextboxCase(String operationName, String name,
            boolean isMust, boolean isRepeat, boolean isClear, Integer minNum, Integer maxNum, Integer decimals) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(operationName, name, isMust,
                isRepeat, isClear, InputRuleType.NUM);

        // 添加输入数字限制相关的测试用例
        // 判断最大最小值是否一致，若一致，则按照只包含最大限制生成用例
        if (Objects.equals(minNum, maxNum)) {
            minNum = null;
        }
        // 判断最大与最小值是否需要调换，存储正确最大最小值
        if (minNum != null && maxNum != null && minNum > maxNum) {
            int temp = minNum;
            minNum = maxNum;
            maxNum = temp;
        }

        // 存储数字限制
        addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_MIN_NUMBER, String.valueOf(minNum));
        addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_MAX_NUMBER, String.valueOf(maxNum));

        // 判断是否有最小数字限制
        if (minNum != null) {
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_TEXTBOX_CASE, new String[] { "6", "7" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
        }
        // 判断是否有最大数字限制
        if (maxNum != null) {
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_TEXTBOX_CASE, new String[] { "8", "9" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
        }

        // 若传入的小数位数大于0，则添加小数位相关的用例
        if (Optional.ofNullable(decimals).filter(de -> de.compareTo(0) > 0).isPresent()) {
            // 存储小数位数
            addReplaceWord(WriteInformationCaseTempletReplaceWord.INPUT_DECIMALS, String.valueOf(decimals));
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_TEXTBOX_CASE,
                            new String[] { "10", "11", "12" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_TEXTBOX_CASE, new String[] { "2" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT, WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
        }

        // 根据是否必填，添加其文本框的优先级
        addContent(allContentMap, LabelType.RANK, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1
                                : WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中带数字限制的文本框测试用例
     * <p>
     * 该方法可以生成带数字大小限制的输入文本框，亦可生成带小数位限制的文本框，其传参方式如下：
     * </p>
     * <p>
     * 带数字大小限制的文本框：
     * <ul>
     * <li>数字最小输入限制为2：{@code addNumberRuleTextboxCase("成功率", true, true, true, 1, null, null)}</li>
     * <li>数字最大输入限制为10：{@code addNumberRuleTextboxCase("成功率", true, true, true, null, 10, null)}</li>
     * <li>数字输入限制为2~10：{@code addNumberRuleTextboxCase("成功率", true, true, true, 2, 10, null)}</li>
     * </ul>
     * </p>
     * <p>
     * 带小数位限制的文本框：
     * <ul>
     * <li>小数位限制为2位：{@code addNumberRuleTextboxCase("成功率", true, true, true, null, null, 2)}</li>
     * <li>无小数位限制：{@code addNumberRuleTextboxCase("成功率", true, true, true, null, null, null)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>以上案例中，小数位与数字限制可同时使用，例如，数字输入限制为2~10，小数位限制2位：{@code addNumberRuleTextboxCase("成功率", true, true, true, 2, 10, 2)}</li>
     * <li>当最大、最小数字限制与小数位限制都传入null时，则按照只限制输入数字的文本框处理</li>
     * <li></li>
     * </ol>
     * </p>
     * 
     * @param name     控件名称
     * @param isMust   是否必填
     * @param isRepeat 是否可以与存在的内容重复
     * @param isClear  是否有按钮可以清空文本框
     * @param minNum   数字最大限制
     * @param maxNum   数字最小限制
     * @param decimals 小数位限制
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addNumberRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            Integer minNum, Integer maxNum, Integer decimals) {
        return createCaseDataList(this,
                numberRuleTextboxCase(OPERATION_ADD, name, isMust, isRepeat, isClear, minNum, maxNum, decimals));
    }

    /**
     * 该方法用于生成编辑信息中带数字限制的文本框测试用例
     * <p>
     * 传入数字限制的方法可参考生成新增用例的方法：{@link #addNumberRuleTextboxCase(String, boolean, boolean, boolean, Integer, Integer, Integer)}
     * </p>
     * 
     * @param name     控件名称
     * @param isMust   是否必填
     * @param isRepeat 是否可以与存在的内容重复
     * @param isClear  是否有按钮可以清空文本框
     * @param minNum   数字最大限制
     * @param maxNum   数字最小限制
     * @param decimals 小数位限制
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editNumberRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            Integer minNum, Integer maxNum, Integer decimals) {
        return createCaseDataList(this,
                numberRuleTextboxCase(OPERATION_EDIT, name, isMust, isRepeat, isClear, minNum, maxNum, decimals));
    }

    /**
     * 该方法用于生成注册信息中带数字限制的文本框测试用例
     * <p>
     * 传入数字限制的方法可参考生成新增用例的方法：{@link #addNumberRuleTextboxCase(String, boolean, boolean, boolean, Integer, Integer, Integer)}
     * </p>
     * 
     * @param name     控件名称
     * @param isMust   是否必填
     * @param isRepeat 是否可以与存在的内容重复
     * @param isClear  是否有按钮可以清空文本框
     * @param minNum   数字最大限制
     * @param maxNum   数字最小限制
     * @param decimals 小数位限制
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredNumberRuleTextboxCase(String name, boolean isMust, boolean isRepeat,
            boolean isClear, Integer minNum, Integer maxNum, Integer decimals) {
        return createCaseDataList(this,
                numberRuleTextboxCase(OPERATION_REGISTERED, name, isMust, isRepeat, isClear, minNum, maxNum, decimals));
    }

    /**
     * 该方法用于生成电话号码相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isRepeat      是否可以与存在的内容重复
     * @param isClear       是否有按钮可以清空文本框
     * @param phoneTypes    号码类型
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> phoneCase(String operationName, String name, boolean isMust,
            boolean isRepeat, boolean isClear, PhoneType... phoneTypes) {
        if (phoneTypes.length == 0) {
            return lengthRuleTextboxCase(operationName, name, isMust, isRepeat, isClear, null, null, InputRuleType.NUM);
        }

        // 将号码限制组改为set集合
        Set<PhoneType> phoneTypeSet = new HashSet<>(
                Arrays.asList(Optional.ofNullable(phoneTypes).orElseGet(() -> new PhoneType[] {})));
        
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = null;
        if (phoneTypeSet.contains(PhoneType.FIXED)) {
            allContentMap = lengthRuleTextboxCase(operationName, name, isMust, isRepeat, isClear, 7,
                    phoneTypeSet.contains(PhoneType.MOBLE) ? 11 : 7, InputRuleType.NUM);
        } else {
            allContentMap = lengthRuleTextboxCase(operationName, name, isMust, isRepeat, isClear, null, 11,
                    InputRuleType.NUM);
        }

        // 添加移动电话用例
        if (phoneTypeSet.contains(PhoneType.MOBLE)) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_PHONE_CASE, new String[] { "1" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));
        }
        // 添加座机用例
        if (phoneTypeSet.contains(PhoneType.FIXED)) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_PHONE_CASE, new String[] { "2" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
        }

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中电话号码类型文本框相关的测试用例
     * 
     * @param name       控件名称
     * @param isMust     是否必填
     * @param isRepeat   是否可以与存在的内容重复
     * @param isClear    是否有按钮可以清空文本框
     * @param phoneTypes 号码类型
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addPhoneCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            PhoneType... phoneTypes) {
        return createCaseDataList(this, phoneCase(OPERATION_ADD, name, isMust, isRepeat, isClear, phoneTypes));
    }

    /**
     * 该方法用于生成编辑信息中电话号码类型文本框相关的测试用例
     * 
     * @param name       控件名称
     * @param isMust     是否必填
     * @param isRepeat   是否可以与存在的内容重复
     * @param isClear    是否有按钮可以清空文本框
     * @param phoneTypes 号码类型
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editPhoneCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            PhoneType... phoneTypes) {
        return createCaseDataList(this, phoneCase(OPERATION_EDIT, name, isMust, isRepeat, isClear, phoneTypes));
    }

    /**
     * 该方法用于生成注册信息中电话号码类型文本框相关的测试用例
     * 
     * @param name       控件名称
     * @param isMust     是否必填
     * @param isRepeat   是否可以与存在的内容重复
     * @param isClear    是否有按钮可以清空文本框
     * @param phoneTypes 号码类型
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredPhoneCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
            PhoneType... phoneTypes) {
        return createCaseDataList(this, phoneCase(OPERATION_REGISTERED, name, isMust, isRepeat, isClear, phoneTypes));
    }

    /**
     * 该方法用于生成身份证相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isRepeat      是否可以与存在的内容重复
     * @param isClear       是否有按钮可以清空文本框
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> idCardCase(String operationName, String name,
            boolean isMust, boolean isRepeat, boolean isClear) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = textboxCommonCase(operationName, name, isMust,
                isRepeat, isClear);

        addContent(allContentMap, LabelType.STEP, Arrays.asList(
                new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_ID_CARD_CASE, new String[] { "1", "2", "3", "4" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT, WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT, WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));

        // 根据是否必填，添加其文本框的优先级
        addContent(allContentMap, LabelType.RANK, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1
                                : WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中身份证号码类型文本框相关的测试用例
     * 
     * @param name     控件名称
     * @param isMust   是否必填
     * @param isRepeat 是否可以与存在的内容重复
     * @param isClear  是否有按钮可以清空文本框
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addIdCardCase(String name, boolean isMust, boolean isRepeat, boolean isClear) {
        return createCaseDataList(this, idCardCase(OPERATION_ADD, name, isMust, isRepeat, isClear));
    }

    /**
     * 该方法用于生成编辑信息中身份证号码类型文本框相关的测试用例
     * 
     * @param name     控件名称
     * @param isMust   是否必填
     * @param isRepeat 是否可以与存在的内容重复
     * @param isClear  是否有按钮可以清空文本框
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editIdCardCase(String name, boolean isMust, boolean isRepeat, boolean isClear) {
        return createCaseDataList(this, idCardCase(OPERATION_EDIT, name, isMust, isRepeat, isClear));
    }

    /**
     * 该方法用于生成注册信息中身份证号码类型文本框相关的测试用例
     * 
     * @param name     控件名称
     * @param isMust   是否必填
     * @param isRepeat 是否可以与存在的内容重复
     * @param isClear  是否有按钮可以清空文本框
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredIdCardCase(String name, boolean isMust, boolean isRepeat, boolean isClear) {
        return createCaseDataList(this, idCardCase(OPERATION_REGISTERED, name, isMust, isRepeat, isClear));
    }

    /**
     * 该方法用于生成下拉选项控件相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isEmptyOption 是否存在空选项
     * @param isClear       是否能清空
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> selectboxCase(String operationName, String name,
            boolean isMust, boolean isEmptyOption, boolean isClear) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = addCommonData(operationName, name);

        // 添加检查选项
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "2" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "2" })));
        // 添加基础步骤
        addContent(allContentMap, LabelType.STEP, Arrays.asList(
                new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_SELECTBOX_CASE, new String[] { "0", "2", "3" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isMust && isEmptyOption ? WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT
                                : WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT })));
        // 判断下拉框是否包含空选项
        if (isEmptyOption) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_SELECTBOX_CASE, new String[] { "1" })));
            if (isMust) {
                addContent(allContentMap, LabelType.EXCEPT, Arrays
                        .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_SELECTBOX_CASE, new String[] { "1" })));
            } else {
                addContent(allContentMap, LabelType.EXCEPT, Arrays
                        .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));
            }
        }
        // 判断是否可清空
        if (isClear) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_SELECTBOX_CASE, new String[] { "4" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_SELECTBOX_CASE, new String[] { "2" })));
        }

        // 根据是否必填，添加其文本框的优先级
        addContent(allContentMap, LabelType.RANK, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1
                                : WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中下拉选项控件相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isEmptyOption 是否存在空选项
     * @param isClear       是否能清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addSelectboxCase(String name, boolean isMust, boolean isEmptyOption, boolean isClear) {
        return createCaseDataList(this, selectboxCase(OPERATION_ADD, name, isMust, isEmptyOption, isClear));
    }

    /**
     * 该方法用于生成编辑信息中下拉选项控件相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isEmptyOption 是否存在空选项
     * @param isClear       是否能清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editSelectboxCase(String name, boolean isMust, boolean isEmptyOption, boolean isClear) {
        return createCaseDataList(this, selectboxCase(OPERATION_EDIT, name, isMust, isEmptyOption, isClear));
    }

    /**
     * 该方法用于生成注册信息中下拉选项控件相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isEmptyOption 是否存在空选项
     * @param isClear       是否能清空
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredSelectboxCase(String name, boolean isMust, boolean isEmptyOption, boolean isClear) {
        return createCaseDataList(this, selectboxCase(OPERATION_REGISTERED, name, isMust, isEmptyOption, isClear));
    }

    /**
     * 该方法用于生成单选控件相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> radioButtonCase(String operationName, String name) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = addCommonData(operationName, name);

        // 添加检查选项
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "2" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "2" })));
        // 添加基础步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_RADIO_BUTTON_CASE,
                        new String[] { "1", "2", "3", "4" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_RADIO_BUTTON_CASE, new String[] { "1" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT })));

        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中单选控件相关的测试用例
     * 
     * @param name          控件名称
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addRadioButtonCase(String name) {
        return createCaseDataList(this, radioButtonCase(OPERATION_ADD, name));
    }

    /**
     * 该方法用于生成编辑信息中单选控件相关的测试用例
     * 
     * @param name          控件名称
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editRadioButtonCase(String name) {
        return createCaseDataList(this, radioButtonCase(OPERATION_EDIT, name));
    }

    /**
     * 该方法用于生成注册信息中单选控件相关的测试用例
     * 
     * @param name 控件名称
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredRadioButtonCase(String name) {
        return createCaseDataList(this, radioButtonCase(OPERATION_REGISTERED, name));
    }

    /**
     * 该方法用于生成多选控件相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> checkboxCase(String operationName, String name,
            boolean isMust) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = addCommonData(operationName, name);

        // 添加检查选项
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "2" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "2" })));

        // 添加基础步骤
        addContent(allContentMap, LabelType.STEP, Arrays.asList(
                new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_CHECKBOX_CASE, new String[] { "1", "2", "3", "4", "5" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_CHECKBOX_CASE, new String[] { "1" })));

        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] {
                                isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT
                                        : WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT })));

        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中多选控件相关的测试用例
     * 
     * @param name   控件名称
     * @param isMust 是否必填
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addCheckboxCase(String name, boolean isMust) {
        return createCaseDataList(this, checkboxCase(OPERATION_ADD, name, isMust));
    }

    /**
     * 该方法用于生成编辑信息中多选控件相关的测试用例
     * 
     * @param name   控件名称
     * @param isMust 是否必填
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editCheckboxCase(String name, boolean isMust) {
        return createCaseDataList(this, checkboxCase(OPERATION_EDIT, name, isMust));
    }

    /**
     * 该方法用于生成注册信息中多选控件相关的测试用例
     * 
     * @param name   控件名称
     * @param isMust 是否必填
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredCheckboxCase(String name, boolean isMust) {
        return createCaseDataList(this, checkboxCase(OPERATION_REGISTERED, name, isMust));
    }

    /**
     * 该方法用于生成日期控件基本的方法
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param isMust        是否必填
     * @param isInput       是否可输入
     * @param isClear       是否可清空
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> commonDateCase(String operationName, String name,
            boolean isMust, boolean isInput, boolean isClear) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = addCommonData(operationName, name);

        // 添加检查选项
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_DATE_CASE, new String[] { "1", "2" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] {
                                isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT
                                        : WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT })));
        // 判断是否能清空
        if (isClear) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_DATE_CASE, new String[] { "3" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] {
                                    isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT
                                            : WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT })));
        }
        // 判断能输入
        if (isInput) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_DATE_CASE, new String[] { "4", "5" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));
        }

        // 根据是否必填，添加其文本框的优先级
        addContent(allContentMap, LabelType.RANK, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1
                                : WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中独立日期相关的测试用例
     * 
     * @param name    控件名称
     * @param isMust  是否必填
     * @param isInput 是否可输入
     * @param isClear 是否可清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addDateCase(String name, boolean isMust, boolean isInput, boolean isClear) {
        return createCaseDataList(this, commonDateCase(OPERATION_ADD, name, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成编辑信息中独立日期相关的测试用例
     * 
     * @param name    控件名称
     * @param isMust  是否必填
     * @param isInput 是否可输入
     * @param isClear 是否可清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editDateCase(String name, boolean isMust, boolean isInput, boolean isClear) {
        return createCaseDataList(this, commonDateCase(OPERATION_EDIT, name, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成注册信息中独立日期相关的测试用例
     * 
     * @param name    控件名称
     * @param isMust  是否必填
     * @param isInput 是否可输入
     * @param isClear 是否可清空
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredDateCase(String name, boolean isMust, boolean isInput, boolean isClear) {
        return createCaseDataList(this, commonDateCase(OPERATION_REGISTERED, name, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成开始时间相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param endDateName   结束时间控件名称
     * @param isMust        是否必填
     * @param isInput       是否可输入
     * @param isClear       是否可清空
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> startDateCase(String operationName, String name,
            String endDateName, boolean isMust, boolean isInput, boolean isClear) {
        // 添加替换词
        addReplaceWord(WriteInformationCaseTempletReplaceWord.END_DATE, endDateName);

        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = commonDateCase(operationName, name, isMust,
                isInput, isClear);

        // 添加检查选项
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_DATE_CASE, new String[] { "6", "7" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中开始日期类型相关的测试用例
     * 
     * @param name        控件名称
     * @param endDateName 结束时间控件名称
     * @param isMust      是否必填
     * @param isInput     是否可输入
     * @param isClear     是否可清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addStartDateCase(String name, String endDateName, boolean isMust, boolean isInput,
            boolean isClear) {
        return createCaseDataList(this, startDateCase(OPERATION_ADD, name, endDateName, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成编辑信息中开始日期类型相关的测试用例
     * 
     * @param name        控件名称
     * @param endDateName 结束时间控件名称
     * @param isMust      是否必填
     * @param isInput     是否可输入
     * @param isClear     是否可清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editStartDateCase(String name, String endDateName, boolean isMust, boolean isInput,
            boolean isClear) {
        return createCaseDataList(this, startDateCase(OPERATION_EDIT, name, endDateName, isMust, isInput, isClear));
    }
    
    /**
     * 该方法用于生成注册信息中开始日期类型相关的测试用例
     * 
     * @param name        控件名称
     * @param endDateName 结束时间控件名称
     * @param isMust      是否必填
     * @param isInput     是否可输入
     * @param isClear     是否可清空
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredStartDateCase(String name, String endDateName, boolean isMust, boolean isInput,
            boolean isClear) {
        return createCaseDataList(this,
                startDateCase(OPERATION_REGISTERED, name, endDateName, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成结束时间相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @param name          控件名称
     * @param endDateName   结束时间控件名称
     * @param isMust        是否必填
     * @param isInput       是否可输入
     * @param isClear       是否可清空
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> endDateCase(String operationName, String name,
            String startDateName, boolean isMust, boolean isInput, boolean isClear) {
        // 添加替换词
        addReplaceWord(WriteInformationCaseTempletReplaceWord.START_DATE, startDateName);

        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = commonDateCase(operationName, name, isMust,
                isInput, isClear);

        // 添加检查选项
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_DATE_CASE, new String[] { "8", "9" })));
        addContent(allContentMap, LabelType.EXCEPT, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中结束日期类型相关的测试用例
     * 
     * @param name          控件名称
     * @param startDateName 结束时间控件名称
     * @param isMust        是否必填
     * @param isInput       是否可输入
     * @param isClear       是否可清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addEndDateCase(String name, String startDateName, boolean isMust, boolean isInput,
            boolean isClear) {
        return createCaseDataList(this, endDateCase(OPERATION_ADD, name, startDateName, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成编辑信息中结束日期类型相关的测试用例
     * 
     * @param name          控件名称
     * @param startDateName 结束时间控件名称
     * @param isMust        是否必填
     * @param isInput       是否可输入
     * @param isClear       是否可清空
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editEndDateCase(String name, String startDateName, boolean isMust, boolean isInput,
            boolean isClear) {
        return createCaseDataList(this, endDateCase(OPERATION_EDIT, name, startDateName, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成注册信息中结束日期类型相关的测试用例
     * 
     * @param name          控件名称
     * @param startDateName 结束时间控件名称
     * @param isMust        是否必填
     * @param isInput       是否可输入
     * @param isClear       是否可清空
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredEndDateCase(String name, String startDateName, boolean isMust, boolean isInput,
            boolean isClear) {
        return createCaseDataList(this,
                endDateCase(OPERATION_REGISTERED, name, startDateName, isMust, isInput, isClear));
    }

    /**
     * 该方法用于生成上传文件相关的测试用例
     * 
     * @param operationName   操作类型名称
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param sizeLimitText   文件大小限制的文本
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param uploadFileType  上传的文件类型枚举
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> commonUpdataFileCase(String operationName, String name,
            boolean isMust, boolean isDelete, boolean isPreview, String sizeLimitText, Integer fileMinNum,
            Integer fileMaxNum, UploadFileType uploadFileType, String... fileFormatNames) {
        // 转换输入限制
        Set<String> fileFormatNameSet = new HashSet<>(
                Arrays.asList(Optional.ofNullable(fileFormatNames).orElseGet(() -> new String[] {})));

        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = addCommonData(operationName, name);
        
        // 添加替换词语
        addReplaceWord(WriteInformationCaseTempletReplaceWord.FILE_TYPE, uploadFileType.getName());
        addReplaceWord(WriteInformationCaseTempletReplaceWord.FILE_UNIT, uploadFileType.getUnitName());

        // 添加检查选项
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "2", "4" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] {
                                isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT
                                        : WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FILE_SUCCESS_EXCEPT,
                                WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FILE_SUCCESS_EXCEPT })));
        // 判断文件是否能进行预览
        if (isPreview) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "1" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "1" })));
        }
        // 判断是否能删除文件
        if (isDelete) {
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "3" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FILE_SUCCESS_EXCEPT })));
        }
        // 判断文件大小限制（当大小限制文本不为空时）
        if (!Objects.equals("", sizeLimitText)) {
            addReplaceWord(WriteInformationCaseTempletReplaceWord.FILE_SIZE, sizeLimitText);
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "5", "11" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FILE_SUCCESS_EXCEPT })));
        }
        // 判断文件个数限制
        // 判断最大最小值是否一致
        if (Objects.equals(fileMinNum, fileMaxNum) && fileMinNum != null && fileMinNum != 0) {
            addReplaceWord(WriteInformationCaseTempletReplaceWord.UPLOAD_MIN_LENGTH, String.valueOf(fileMinNum));
            addReplaceWord(WriteInformationCaseTempletReplaceWord.UPLOAD_MAX_LENGTH, String.valueOf(fileMaxNum));
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE,
                            new String[] { "7", "8", "9" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT,
                                    WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));
        } else {
            // 判断最大与最小值是否需要调换，存储正确最大最小值
            if (fileMinNum != null && fileMaxNum != null && fileMinNum > fileMaxNum) {
                int temp = fileMinNum;
                fileMinNum = fileMaxNum;
                fileMaxNum = temp;
            }
            // 判断是否存在最小长度限制
            if (Optional.ofNullable(fileMinNum).filter(min -> min > 0).isPresent()) {
                addReplaceWord(WriteInformationCaseTempletReplaceWord.UPLOAD_MIN_LENGTH, String.valueOf(fileMinNum));
                addContent(allContentMap, LabelType.STEP, Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "7", "8" })));
                addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                        WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
            }
            // 判断是否存在最大长度限制
            if (Optional.ofNullable(fileMaxNum).filter(max -> max > 0).isPresent()) {
                addReplaceWord(WriteInformationCaseTempletReplaceWord.UPLOAD_MAX_LENGTH, String.valueOf(fileMaxNum));
                addContent(allContentMap, LabelType.STEP, Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "9", "10" })));
                addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                        new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT,
                                        WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT })));
            }
        }
        // 判断是否存在格式限制
        if (!fileFormatNameSet.isEmpty()) {
            // 格式限制
            StringJoiner fileRule = new StringJoiner("、");
            fileFormatNameSet.forEach(fileRule::add);
            // 添加替换词以及用例
            addReplaceWord(WriteInformationCaseTempletReplaceWord.FILE_RULE, fileRule.toString());
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_FILE_CASE, new String[] { "6" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                    new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                            new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_EXCEPT_FAIL_EXCEPT })));
        }

        // 根据是否必填，添加其文本框的优先级
        addContent(allContentMap, LabelType.RANK, Arrays
                .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { isMust ? WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1
                                : WriteInformationCaseTempletField.COMMON_CONTENT_RANK_2 })));

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中，上传文件相关的测试用例
     * <p>
     * 传入文件个数限制的方法如下：
     * <ol>
     * <li>上传限制为2~10个文件时：{@code addUploadFileCase("文档", true, true, true, null, 2, 10)}</li>
     * <li>上传限制为最多上传10个文件时：{@code addUploadFileCase("文档", true, true, true, null, 0, 10)}</li>
     * <li>上传限制为最少上传2个文件时：{@code addUploadFileCase("文档", true, true, true, null, 2, 0)}</li>
     * <li>上传限制为仅能上传2个文件时：{@code addUploadFileCase("文档", true, true, true, null, 2, 2)}</li>
     * </ol>
     * </p>
     * <p>
     * <b>注意：</b>文件大小显示文本需自行传入限制的大小及单位，例如，限制上传50M的文件，则传入{@code addUploadFileCase("文档", true, true, true, "50M", 2, 2)}；
     * 若为空或传入null，则表示没有文件大小限制
     * </p>
     * 
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param sizeLimitText   文件大小限制的文本，例如，限制上传50M的文件，则传入“50M”
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addUploadFileCase(String name, boolean isMust, boolean isDelete, boolean isPreview,
            String sizeLimitText, Integer fileMinNum, Integer fileMaxNum, String... fileFormatNames) {
        return createCaseDataList(this, commonUpdataFileCase(OPERATION_ADD, name, isMust, isDelete, isPreview,
                sizeLimitText, fileMinNum, fileMaxNum, UploadFileType.FILE, fileFormatNames));
    }

    /**
     * 该方法用于生成编辑信息中，上传文件相关的测试用例
     * <p>
     * 关于文件大小、个数限制的传参，可参考{@link #addUploadFileCase(String, boolean, boolean, boolean, String, Integer, Integer, String...)}方法
     * </p>
     * 
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param sizeLimitText   文件大小限制的文本，例如，限制上传50M的文件，则传入“50M”
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editUploadFileCase(String name, boolean isMust, boolean isDelete, boolean isPreview,
            String sizeLimitText, Integer fileMinNum, Integer fileMaxNum, String... fileFormatNames) {
        return createCaseDataList(this, commonUpdataFileCase(OPERATION_EDIT, name, isMust, isDelete, isPreview,
                sizeLimitText, fileMinNum, fileMaxNum, UploadFileType.FILE, fileFormatNames));
    }

    /**
     * 该方法用于生成注册信息中，上传文件相关的测试用例
     * <p>
     * 关于文件大小、个数限制的传参，可参考{@link #addUploadFileCase(String, boolean, boolean, boolean, String, Integer, Integer, String...)}方法
     * </p>
     * 
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param sizeLimitText   文件大小限制的文本，例如，限制上传50M的文件，则传入“50M”
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredUploadFileCase(String name, boolean isMust, boolean isDelete, boolean isPreview,
            String sizeLimitText, Integer fileMinNum, Integer fileMaxNum, String... fileFormatNames) {
        return createCaseDataList(this, commonUpdataFileCase(OPERATION_REGISTERED, name, isMust, isDelete, isPreview,
                sizeLimitText, fileMinNum, fileMaxNum, UploadFileType.FILE, fileFormatNames));
    }

    /**
     * 该方法用于生成上传图片相关的测试用例
     * 
     * @param operationName   操作类型名称
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param isPhoto         是否允许拍照上传
     * @param sizeLimitText   文件大小限制的文本
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例集合
     * @since autest 4.0.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> uploadImageCase(String operationName, String name,
            boolean isMust, boolean isDelete, boolean isPreview, boolean isPhoto, String sizeLimitText,
            Integer fileMinNum, Integer fileMaxNum, String... fileFormatNames) {
        // 获取文本框的基础测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = commonUpdataFileCase(operationName, name, isMust,
                isDelete, isPreview, sizeLimitText, fileMinNum, fileMaxNum, UploadFileType.IMAGE, fileFormatNames);

        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_IMAGE_CASE, new String[] { "3" })));
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_IMAGE_CASE, new String[] { "3" })));
        // 判断是否允许拍照上传
        if (isPhoto) {
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_IMAGE_CASE, new String[] { "1", "2" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_ADD_UPLOAD_IMAGE_CASE, new String[] { "1", "2" })));
        }

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息中，上传图片相关的测试用例
     * <p>
     * 传入图片个数限制的方法如下：
     * <ol>
     * <li>上传限制为2~10张图片时：{@code addUploadImageCase("图片", true, true, true, true, null, 2, 10)}</li>
     * <li>上传限制为最多上传10张图片时：{@code addUploadImageCase("图片", true, true, true, true, null, 0, 10)}</li>
     * <li>上传限制为最少上传2张图片时：{@code addUploadImageCase("图片", true, true, true, true, null, 2, 0)}</li>
     * <li>上传限制为仅能上传2张图片时：{@code addUploadImageCase("图片", true, true, true, true, null, 2, 2)}</li>
     * </ol>
     * </p>
     * <p>
     * <b>注意：</b>文件大小显示文本需自行传入限制的大小及单位，例如，限制上传50M的文件，则传入{@code addUploadImageCase("图片", true, true, true, true, "50M", 2, 2)}；
     * 若为空或传入null，则表示没有文件大小限制
     * </p>
     * 
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param isPhoto         是否允许拍照上传
     * @param sizeLimitText   文件大小限制的文本，例如，限制上传50M的文件，则传入“50M”
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> addUploadImageCase(String name, boolean isMust, boolean isDelete, boolean isPreview,
            boolean isPhoto, String sizeLimitText, Integer fileMinNum, Integer fileMaxNum, String... fileFormatNames) {
        return createCaseDataList(this, uploadImageCase(OPERATION_ADD, name, isMust, isDelete, isPreview, isPhoto,
                sizeLimitText, fileMinNum, fileMaxNum, fileFormatNames));
    }

    /**
     * 该方法用于生成编辑信息中，上传图片相关的测试用例
     * <p>
     * 关于文件大小、个数限制的传参，可参考{@link #addUploadImageCase(String, boolean, boolean, boolean, boolean, String, Integer, Integer, String...)}方法
     * </p>
     * 
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param isPhoto         是否允许拍照上传
     * @param sizeLimitText   文件大小限制的文本，例如，限制上传50M的文件，则传入“50M”
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> editUploadImageCase(String name, boolean isMust, boolean isDelete, boolean isPreview,
            boolean isPhoto, String sizeLimitText, Integer fileMinNum, Integer fileMaxNum, String... fileFormatNames) {
        return createCaseDataList(this, uploadImageCase(OPERATION_EDIT, name, isMust, isDelete, isPreview, isPhoto,
                sizeLimitText, fileMinNum, fileMaxNum, fileFormatNames));
    }

    /**
     * 该方法用于生成注册信息中，上传图片相关的测试用例
     * <p>
     * 关于文件大小、个数限制的传参，可参考{@link #addUploadImageCase(String, boolean, boolean, boolean, boolean, String, Integer, Integer, String...)}方法
     * </p>
     * 
     * @param name            控件名称
     * @param isMust          是否必须上传
     * @param isDelete        是否允许删除
     * @param isPreview       是否允许预览
     * @param isPhoto         是否允许拍照上传
     * @param sizeLimitText   文件大小限制的文本，例如，限制上传50M的文件，则传入“50M”
     * @param fileMinNum      最少上传文件数量
     * @param fileMaxNum      最多上传文件数量
     * @param fileFormatNames 上传文件的限制格式名称组
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> registeredUploadImageCase(String name, boolean isMust, boolean isDelete, boolean isPreview,
            boolean isPhoto, String sizeLimitText, Integer fileMinNum, Integer fileMaxNum, String... fileFormatNames) {
        return createCaseDataList(this, uploadImageCase(OPERATION_REGISTERED, name, isMust, isDelete, isPreview,
                isPhoto, sizeLimitText, fileMinNum, fileMaxNum, fileFormatNames));
    }

    /**
     * 该方法用于生成检查页面相关的测试用例
     * 
     * @param operationName 操作类型名称
     * @return 用例集合
     * @since autest 4.1.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> examineUICase(String operationName) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 添加替换词语
        addReplaceWord(WriteInformationCaseTempletReplaceWord.OPERATION_TYPE, operationName);
        switch (operationName) {
        case OPERATION_REGISTERED:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_REGISTERED);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_REGISTERED);
            break;
        case OPERATION_ADD:
        case OPERATION_EDIT:
        default:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_ADD_OR_EDIT);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_ADD_OR_EDIT);
            break;
        }

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "1" })));

        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "1" })));

        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 步骤与预期
        switch (operationName) {
        case OPERATION_ADD:
        case OPERATION_REGISTERED:
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "1" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "1" })));
            break;
        case OPERATION_EDIT:
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "3" })));
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_EXAMINE_UI, new String[] { "3" })));
            break;
        default:
            break;
        }

        return allContentMap;
    }

    /**
     * 该方法用于生成检查新增信息页面相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> examineAddUICase() {
        return createCaseDataList(this, examineUICase(OPERATION_ADD));
    }

    /**
     * 该方法用于生成检查编辑信息页面相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> examineEditUICase() {
        return createCaseDataList(this, examineUICase(OPERATION_EDIT));
    }

    /**
     * 该方法用于生成检查注册信息页面相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> examineRegisteredUICase() {
        return createCaseDataList(this, examineUICase(OPERATION_REGISTERED));
    }

    /**
     * 该方法用于生成新增信息界面取消保存信息的用例
     * 
     * @param operationName    操作类型名称
     * @param cencelButtonName 取消保存按钮名称
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> cencelSaveDataCase(String operationName,
            String cencelButtonName) {

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 添加替换词语
        addReplaceWord(WriteInformationCaseTempletReplaceWord.OPERATION_TYPE, operationName);
        addReplaceWord(WriteInformationCaseTempletReplaceWord.CENCEL_BUTTON_NAME, cencelButtonName);
        switch (operationName) {
        case OPERATION_REGISTERED:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_REGISTERED);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_REGISTERED);
            break;
        case OPERATION_ADD:
        case OPERATION_EDIT:
        default:
            addReplaceWord(WriteInformationCaseTempletReplaceWord.PAGE_NAME, PAGE_NAME_ADD_OR_EDIT);
            addReplaceWord(WriteInformationCaseTempletReplaceWord.BREAK_TEXT, BREAK_TEXT_ADD_OR_EDIT);
            break;
        }

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_CANCEL_SAVE_DATA, new String[] { "1" })));

        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_CANCEL_SAVE_DATA, new String[] { "1" })));

        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(WriteInformationCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { WriteInformationCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 步骤与预期
        switch (operationName) {
        case OPERATION_ADD:
        case OPERATION_REGISTERED:
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_CANCEL_SAVE_DATA, new String[] { "1", "3" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_CANCEL_SAVE_DATA, new String[] { "1", "1" })));
            break;
        case OPERATION_EDIT:
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_CANCEL_SAVE_DATA, new String[] { "2", "4" })));
            addContent(allContentMap, LabelType.EXCEPT, Arrays
                    .asList(new Entry<>(WriteInformationCaseTempletField.GROUP_CANCEL_SAVE_DATA, new String[] { "2", "2" })));
            break;
        default:
            break;
        }

        return allContentMap;
    }

    /**
     * 该方法用于生成新增信息界面取消保存信息的用例
     * 
     * @param cencelButtonName 取消保存按钮名称
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> cencelSaveAddDataCase(String cencelButtonName) {
        return createCaseDataList(this, cencelSaveDataCase(OPERATION_ADD, cencelButtonName));
    }

    /**
     * 该方法用于生成编辑信息界面取消保存信息的用例
     * 
     * @param cencelButtonName 取消保存按钮名称
     * @return 用例数据对象集合
     * @since autest 4.0.0
     */
    public List<CaseData> cencelSaveEditDataCase(String cencelButtonName) {
        return createCaseDataList(this, cencelSaveDataCase(OPERATION_EDIT, cencelButtonName));
    }

    /**
     * 该方法用于生成注册信息界面取消保存信息的用例
     * 
     * @param cencelButtonName 取消保存按钮名称
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> cencelSaveRegisteredDataCase(String cencelButtonName) {
        return createCaseDataList(this, cencelSaveDataCase(OPERATION_REGISTERED, cencelButtonName));
    }

    /**
     * <p>
     * <b>文件名：InformationTempletCase.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记“WriteInformationCaseTemplet”用例模板中存在的用例组名称
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
    public class WriteInformationCaseTempletField {
        // 分组名称
        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String COMMON_CONTENT_EXCEPT_FAIL_EXCEPT = "failExcept";
        public static final String COMMON_CONTENT_EXCEPT_INPUT_SUCCESS_EXCEPT = "inputSuccessExcept";
        public static final String COMMON_CONTENT_EXCEPT_SELECT_SUCCESS_EXCEPT = "selectSuccessExcept";
        public static final String COMMON_CONTENT_EXCEPT_FILE_SUCCESS_EXCEPT = "fileSuccessExcept";
        public static final String COMMON_CONTENT_EXCEPT_EMPTY_SUCCESS_EXCEPT = "emptySuccessExcept";
        public static final String COMMON_CONTENT_EXCEPT_ALL_SUCCESS_EXCEPT = "allSuccessExcept";
        public static final String COMMON_CONTENT_TITLE_BASIC = "basic";
        public static final String COMMON_CONTENT_PRECONDITION_ALREADY_ON_THE_PAGE = "alreadyOnThePage";
        public static final String COMMON_CONTENT_PRECONDITION_SUCCESS_INPUT = "successInput";
        public static final String COMMON_CONTENT_PRECONDITION_NOT_MODIFY = "notModify";
        public static final String COMMON_CONTENT_PRECONDITION_CLEAR_CONTENT = "clearContent";
        public static final String COMMON_CONTENT_RANK_1 = "1";
        public static final String COMMON_CONTENT_RANK_2 = "2";
        public static final String COMMON_CONTENT_RANK_3 = "3";
        public static final String COMMON_CONTENT_RANK_4 = "4";
        public static final String COMMON_CONTENT_KEY_BASIC = "basic";

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
    protected class WriteInformationCaseTempletReplaceWord {
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
        public static final String FILE_UNIT = "文件单位";
        public static final String OPERATION_TYPE = "操作类型";
        public static final String PAGE_NAME = "页面名称";
        public static final String BREAK_TEXT = "返回文本";
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
     * <b>修改时间：</b>2023年3月15日 下午4:18:30
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
        FILE("文件", "个"),
        /**
         * 上传图片
         */
        IMAGE("图片", "张");

        /**
         * 上传文件类型名称
         */
        private String name;
        /**
         * 枚举单位
         */
        private String unitName;

        /**
         * 返回枚举名称
         * 
         * @return 枚举名称
         */
        public String getName() {
            return name;
        }

        /**
         * 该方法用于返回枚举对应的单位
         * 
         * @return 单位名称
         * @since autest 4.0.0
         */
        public String getUnitName() {
            return unitName;
        }

        /**
         * 初始化枚举值名称
         * 
         * @param name 枚举值名称
         */
        private UploadFileType(String name, String unitName) {
            this.name = name;
            this.unitName = unitName;
        }
    }
}
