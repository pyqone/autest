package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private Map<LabelType, List<Entry<String, String[]>>> textboxCommonCase(boolean isMust, boolean isRepeat,
            boolean isClear, InputRuleType... inputRuleTypes) {
        // 添加测试用例
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        // 步骤
        allContentMap.put(LabelType.STEP, Arrays.asList(
                new Entry<>(AddInformationTemplet.GROUP_ADD_UNWHOLE_INFORMATION_CASE, new String[] { "1", "2", "3" })));

        // 预期
        allContentMap.put(LabelType.EXCEPT, Arrays.asList(
                new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "失败预期", "输入成功预期", "失败预期" })));
        // 优先级
        allContentMap.put(LabelType.RANK,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "2" })));
        // 关键词
        allContentMap.put(LabelType.KEY, Arrays
                .asList(new Entry<>(AddInformationTemplet.GROUP_ADD_WHOLE_INFORMATION_CASE, new String[] { "1" })));
        // 前置条件
        allContentMap.put(LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(AddInformationTemplet.GROUP_COMMON_CONTENT, new String[] { "1" })));

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
     * <b>修改时间：</b>2020年3月14日 下午9:14:30
     * </p>
     * 
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     */
    public enum InputRuleType {
        /**
         * 中文
         */
        CH("中文"),
        /**
         * 英文
         */
        EN("英文"),
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
        CAP("大写字母");

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
