package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：UserCaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>生成与账号相关测试用例的方法，包括登录、忘记密码和修改密码相关的用例，如需生成注册相关的测试用例，需要调用{@link InformationCaseTemplet}相关的方法生成。
 * </p>
 * <p>
 * <b>编码时间：2023年4月17日 下午3:56:48
 * </p>
 * <p>
 * <b>修改时间：2023年4月17日 下午3:56:48
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.1.0
 */
public class AccountOperateCaseTemplet extends AbstractPresetCaseTemplet implements StepDetailTemplet {
    /**
     * 构造对象，并指定读取的模板xml文件
     * 
     * @param accountName    登录账号的名称，例如用户名、手机号、账号等字段，用于替换“#用户名名称#”占位符
     * @param xmlTempletFile 用例模板文件类对象
     * @since autest 4.1.0
     */
    public AccountOperateCaseTemplet(String accountName, File xmlTempletFile) {
        super(xmlTempletFile);
        addReplaceWord(AccountOperateCaseTempletReplaceWord.ACCOUNT_NAME, accountName);
    }

    /**
     * 构造对象，通过包内的默认模板，对类进行构造
     * 
     * @param accountName 登录账号的名称，例如用户名、手机号、账号等字段，用于替换“#用户名名称#”占位符
     * @since autest 4.1.0
     */
    public AccountOperateCaseTemplet(String accountName) {
        super("AccountOperateCaseTemplet");
        addReplaceWord(AccountOperateCaseTempletReplaceWord.ACCOUNT_NAME, accountName);
    }

    @Override
    public void setReadStepDetail(boolean isStepDetail, boolean isStepIndependentCase) {
        this.isStepDetail = isStepDetail;
        this.isStepIndependentCase = isStepIndependentCase;
    }

    /**
     * 该方法用于生成正常登录相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> rightLoginCase() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AccountOperateCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { AccountOperateCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_RIGHT_LOGIN_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_RIGHT_LOGIN_CASE, new String[] { "1", "2" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_RIGHT_LOGIN_CASE, new String[] { "1", "2" })));
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成非常规账号登录的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> errorAccountLoginCase() {
        // 替换登录信息
        addReplaceWord(AccountOperateCaseTempletReplaceWord.LOGIN_INFO,
                "#" + AccountOperateCaseTempletReplaceWord.ACCOUNT_NAME + "#");

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AccountOperateCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { AccountOperateCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1" })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1", "2" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1", "1" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成非常规密码登录的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> errorPasswordLoginCase() {
        // 替换登录信息
        addReplaceWord(AccountOperateCaseTempletReplaceWord.LOGIN_INFO, "密码");

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AccountOperateCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { AccountOperateCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays
                .asList(new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1" })));

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1" })));

        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "3", "4", "5" })));

        // 预期
        addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1", "1", "2" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成非常规验证码登录的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> errorCodeLoginCase(CodeType codeType) {
        // 替换登录信息
        addReplaceWord(AccountOperateCaseTempletReplaceWord.LOGIN_INFO, codeType.getCodeTypeName());
        // 替换验证码类型
        addReplaceWord(AccountOperateCaseTempletReplaceWord.CODE_TYPE, codeType.getCodeTypeName());
        // 替换验证码操作
        addReplaceWord(AccountOperateCaseTempletReplaceWord.CODE_OPERATE, codeType.getCodeOperateName());
        // 替换操作类型
        addReplaceWord(AccountOperateCaseTempletReplaceWord.OPRATE_TYPE, "登录");

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(AccountOperateCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { AccountOperateCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays
                .asList(new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1" })));

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE, new String[] { "1" })));

        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(AccountOperateCaseTempletField.GROUP_OPERATE_CODE,
                        new String[] { "1", "2", "3", "4", "5", "6" })));

        // 预期
        addContent(allContentMap, LabelType.EXCEPT, Arrays.asList(
                new Entry<>(AccountOperateCaseTempletField.GROUP_ADD_ERROR_LOGINCASE,
                        new String[] { "1", "1", "3", "1", "1", "1" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * <p>
     * <b>文件名：AccountOperateCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义验证码类型
     * </p>
     * <p>
     * <b>编码时间：2023年4月18日 下午5:13:41
     * </p>
     * <p>
     * <b>修改时间：2023年4月18日 下午5:13:41
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    public enum CodeType {
        /**
         * 图形验证码
         */
        IMAGE_CODE("图形验证码", "刷新"),
        /**
         * 手机验证码
         */
        PHONE_CODE("手机验证码", "发送"),
        /**
         * 邮件验证码
         */
        EMAIL_CODE("邮件验证码", "发送");

        /**
         * 验证码类型名称
         */
        private String codeTypeName = "";
        /**
         * 验证码操作名称
         */
        private String codeOperateName = "";

        /**
         * 初始化验证码类型及操作名称
         * 
         * @param codeTypeName    验证码类型名称
         * @param codeOperateName 验证码操作名称
         * @since autest 4.1.0
         */
        private CodeType(String codeTypeName, String codeOperateName) {
            this.codeTypeName = codeTypeName;
            this.codeOperateName = codeOperateName;
        }

        /**
         * 该方法用于返回验证码类型名称
         * 
         * @return 验证码类型名称
         * @since autest 4.1.0
         */
        public String getCodeTypeName() {
            return codeTypeName;
        }

        /**
         * 该方法用于验证码操作名称
         * 
         * @return 验证码操作名称
         * @since autest 4.1.0
         */
        public String getCodeOperateName() {
            return codeOperateName;
        }
    }

    /**
     * <p>
     * <b>文件名：AccountOperateCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记默认用例模板中存在的用例组名称
     * </p>
     * <p>
     * <b>编码时间：2023年4月18日 上午8:09:54
     * </p>
     * <p>
     * <b>修改时间：2023年4月18日 上午8:09:54
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    public class AccountOperateCaseTempletField {
        public static final String COMMON_CONTENT_RANK_1 = "1";
        public static final String COMMON_CONTENT_RANK_2 = "2";
        public static final String COMMON_CONTENT_RANK_3 = "3";
        public static final String COMMON_CONTENT_RANK_4 = "4";
        public static final String FORGET_AND_ALTER_PASSWORD_TITLE_BASIC = "basic";
        public static final String FORGET_AND_ALTER_PASSWORD_KEY_BASIC = "basic";
        public static final String FORGET_AND_ALTER_PASSWORD_PRECONDITION_STEP_1 = "step1";
        public static final String FORGET_AND_ALTER_PASSWORD_PRECONDITION_STEP_2 = "step2";
        public static final String FORGET_AND_ALTER_PASSWORD_EXCEPT_ALTER_SUCCESS = "alterSuccess";
        public static final String FORGET_AND_ALTER_PASSWORD_EXCEPT_ALTER_FAIL = "alterFail";

        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String GROUP_ADD_RIGHT_LOGIN_CASE = "addRightLoginCase";
        public static final String GROUP_ADD_ERROR_LOGINCASE = "addErrorLoginCase";
        public static final String GROUP_FORGET_AND_ALTER_PASSWORD = "forgetAndAlterPassword";
        public static final String GROUP_RIGHT_WRITE_FORGET_AND_ALTER_PASSWORD_INFO = "rightWriteForgetAndAlterPasswordInfo";
        public static final String GROUP_OPERATE_USERNAME = "operateUsername";
        public static final String GROUP_OPERATE_PASSWORD = "operatePassword";
        public static final String GROUP_OPERATE_CODE = "operateCode";
    }

    /**
     * <p>
     * <b>文件名：AccountOperateCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义在默认模板中所有的替换词语
     * </p>
     * <p>
     * <b>编码时间：2023年4月18日 上午8:10:28
     * </p>
     * <p>
     * <b>修改时间：2023年4月18日 上午8:10:28
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest
     */
    protected class AccountOperateCaseTempletReplaceWord {
        public static final String OPRATE_TYPE = "操作类型";
        public static final String CONTROL_NAME = "控件名称";
        public static final String ACCOUNT_NAME = "用户名名称";
        public static final String LOGIN_INFO = "登录信息";
        public static final String CODE_TYPE = "验证码类型";
        public static final String CODE_OPERATE = "验证码操作";
    }
}
