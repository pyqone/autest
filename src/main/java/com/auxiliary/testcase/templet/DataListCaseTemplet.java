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
 * <b>文件名：DataListCaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于根据数据列表页面相关的测试用例模板，生成与数据列表相关的用例。相关的测试用例模板可参考“<a href=
 * 'https://gitee.com/pyqone/autest/blob/master/src/main/resources/com/auxiliary/testcase/templet/BrowseList.xml'>BrowseList.xml</a>”文件
 * </p>
 * <p>
 * <b>编码时间：2023年3月16日 上午8:30:29
 * </p>
 * <p>
 * <b>修改时间：2023年3月16日 上午8:30:29
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.1.0
 */
public class DataListCaseTemplet extends AbstractPresetCaseTemplet {
	/**
     * 构造对象，并指定读取的模板xml文件
     * 
     * @param xmlTempletFile 用例模板文件类对象
     * @param dataListName   列表名称
     * @since autest 4.0.0
     */
    public DataListCaseTemplet(File xmlTempletFile, String dataListName) {
        super(xmlTempletFile);
        addReplaceWord(ReplaceWord.INFORMATION, dataListName);
    }

    /**
     * 构造对象，通过包内的默认模板，对类进行构造
     * 
     * @param dataListName 列表名称
     * @since autest 4.0.0
     */
    public DataListCaseTemplet(String dataListName) {
        super("BrowseList");
        addReplaceWord(ReplaceWord.INFORMATION, dataListName);
    }

    /**
     * 该方法用于生成检查列表界面相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> examineUI() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                        new String[] { BrowseListTemplet.COMMON_CONTENT_PRECONDITION_BASIC })));
        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                new String[] { BrowseListTemplet.COMMON_CONTENT_RANK_1 })));

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_EXAMINE_UI, new String[] { "1" })));

        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_EXAMINE_UI, new String[] { "1" })));
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_EXAMINE_UI, new String[] { "1" })));

        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_EXAMINE_UI, new String[] { "1" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成对指定类型的数据列表基本操作的测试用例
     * 
     * @param dataListType 数据列表类型枚举
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> browseListCase(DataListType dataListType) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                        new String[] { BrowseListTemplet.COMMON_CONTENT_PRECONDITION_BASIC })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                        new String[] { BrowseListTemplet.COMMON_CONTENT_RANK_1 })));

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE, new String[] { "1" })));

        // 添加基础步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE, new String[] { "com1" })));
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE, new String[] { "com1" })));

        // 根据浏览器的类型，添加剩余的步骤与预期
        switch (dataListType) {
        case APP_LIST:
            // 步骤
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE,
                            new String[] { "app1", "app2", "app3", "app4", "app5" })));
            // 预期
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE,
                            new String[] { "app1", "app2", "app3", "app4", "app4" })));
            // 关键词
            addContent(allContentMap, LabelType.KEY,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE, new String[] { "app1" })));
            break;
        case WEB_LIST:
            // 步骤
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE,
                            new String[] { "web1", "web2", "web3", "web4", "web5" })));
            // 预期
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE,
                            new String[] { "web1", "web2", "web2", "web2", "web2" })));
            // 关键词
            addContent(allContentMap, LabelType.KEY,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_BROWSE_LIST_CASE, new String[] { "web1" })));
            break;
        default:
            break;
        }

        return createCaseDataList(this, allContentMap);
    }
    
    /**
     * 该方法用于添加搜索条件相关的基本内容
     * 
     * @return 用例集合
     * @since autest 4.1.0
     */
    protected Map<LabelType, List<Entry<String, String[]>>> searchDataListCase(String conditionName) {
        addReplaceWord(ReplaceWord.SEARCH_CONDITION, conditionName);

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                        new String[] { BrowseListTemplet.COMMON_CONTENT_PRECONDITION_BASIC })));

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                        new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_TITLE_BASIC })));
        
        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_KEY_BASIC })));
        
        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                new String[] { BrowseListTemplet.COMMON_CONTENT_RANK_1 })));
        
        return allContentMap;
    }

    /**
     * 该方法用于生成输入类型搜索条件相关的测试用例
     * 
     * @param conditionName 搜索条件名称
     * @param isMust        是否必须输入
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> textboxSearchCase(String conditionName, boolean isMust) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = searchDataListCase(conditionName);
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_INPUT_SEARCH_CASE,
                        new String[] { "1", "2", "3", "4", "5", "6" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                        new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                isMust ? BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION
                                        : BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT,
                                BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_NO_RESULT,
                                isMust ? BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION
                                        : BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT })));
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成选择类型搜索条件相关的测试用例
     * 
     * @param conditionName         搜索条件名称
     * @param isAssociatedCondition 是否关联下级搜索条件
     * @param isDefaultOption       是否存在默认值
     * @param isMust                是否必须输入
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> selectSearchCase(String conditionName, boolean isAssociatedCondition, boolean isDefaultOption,
            boolean isMust) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = searchDataListCase(conditionName);
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_SELECT_SEARCH_CASE,
                        new String[] { "1", "2", "3", "4", "5", "6" })));

        // 预期
        if (!isAssociatedCondition) {
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                            new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                    BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                    BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                    BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_NO_RESULT })));
            if (isDefaultOption) {
                addContent(allContentMap, LabelType.EXCEPT,
                        Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                                new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                        BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT })));
            } else {
                if (isMust) {
                    addContent(allContentMap, LabelType.EXCEPT,
                            Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                                    new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION,
                                            BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION })));
                } else {
                    addContent(allContentMap, LabelType.EXCEPT,
                            Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                                    new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT,
                                            BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT })));
                }
            }
        } else {
            addReplaceWord(ReplaceWord.OPTION_CONTENT, isDefaultOption ? "默认内容" : "空");
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_SELECT_SEARCH_CASE,
                            new String[] { "1", "1", "1", "4" })));
            if (isDefaultOption) {
                addContent(allContentMap, LabelType.EXCEPT,
                        Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                                new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT })));
                addContent(allContentMap, LabelType.EXCEPT, Arrays
                        .asList(new Entry<>(BrowseListTemplet.GROUP_ADD_SELECT_SEARCH_CASE, new String[] { "2" })));
            } else {
                if (isMust) {
                    addContent(allContentMap, LabelType.EXCEPT,
                            Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                                    new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_NO_RESULT })));
                    addContent(allContentMap, LabelType.EXCEPT, Arrays
                            .asList(new Entry<>(BrowseListTemplet.GROUP_ADD_SELECT_SEARCH_CASE, new String[] { "3" })));
                } else {
                    addContent(allContentMap, LabelType.EXCEPT,
                            Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                                    new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT,
                                            BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT })));
                }
            }
        }

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成选择单个时间进行搜索相关的测试用例
     * 
     * @param conditionName 搜索条件名称
     * @param isMust        是否必须选择
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> dateSearchCase(String conditionName, boolean isMust) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = searchDataListCase(conditionName);
        
        addReplaceWord(ReplaceWord.TIME_CONTENT, "时间");
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_DATE_SEARCH_CASE, new String[] { "1", "2", "3" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                        new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_NO_RESULT,
                                isMust ? BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION
                                        : BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT })));
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成选择时间段进行搜索相关的测试用例
     * 
     * @param startDateName 开始时间条件名称
     * @param endDateName   结束时间条件名称
     * @param isMust        是否必须选择
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> dateQuantumSearchCase(String startDateName, String endDateName, boolean isMust) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = searchDataListCase(
                startDateName + "与" + endDateName);

        addReplaceWord(ReplaceWord.TIME_CONTENT, "时间段");
        addReplaceWord(ReplaceWord.START_TIME, startDateName);
        addReplaceWord(ReplaceWord.END_TIME, endDateName);
        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(BrowseListTemplet.GROUP_ADD_DATE_SEARCH_CASE,
                        new String[] { "1", "2", "3", "4", "5", "6" })));

        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                        new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT,
                                BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_NO_RESULT,
                                isMust ? BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION
                                        : BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT,
                                BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_NO_RESULT })));
        if (isMust) {
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_SEARCH_CASE,
                            new String[] { BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION,
                                    BrowseListTemplet.COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION })));
        } else {
            addContent(allContentMap, LabelType.EXCEPT,
                    Arrays.asList(
                            new Entry<>(BrowseListTemplet.GROUP_ADD_DATE_SEARCH_CASE, new String[] { "1", "2" })));
        }

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成列表排序相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> listSortCase(String field) {
        addReplaceWord(ReplaceWord.LIST_FIELD_NAME, field);
        
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                new String[] { BrowseListTemplet.COMMON_CONTENT_RANK_1 })));

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_LIST_SORT_CASE, new String[] { "1" })));

        // 添加基础步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_LIST_SORT_CASE, new String[] { "1", "2" })));

        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_LIST_SORT_CASE, new String[] { "1", "2" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成导出列表数据相关的的测试用例
     * 
     * @param exportButton 导出按钮名称
     * @param isCheck      是否允许勾选
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> exportListCase(String exportButton, boolean isCheck) {
        addReplaceWord(ReplaceWord.EXPORT_BUTTON_NAME, exportButton);
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                        new String[] { BrowseListTemplet.COMMON_CONTENT_PRECONDITION_BASIC })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_COMMON_CONTENT,
                        new String[] { BrowseListTemplet.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_EXPORT_LIST_CASE, new String[] { "1" })));
        
        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_EXPORT_LIST_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(BrowseListTemplet.GROUP_ADD_EXPORT_LIST_CASE,
                        new String[] { "1", "2", "6" })));
        // 预期
        addContent(allContentMap, LabelType.EXCEPT,
                Arrays.asList(
                        new Entry<>(BrowseListTemplet.GROUP_ADD_EXPORT_LIST_CASE, new String[] { "5", "1", "1" })));
        if (isCheck) {
            // 步骤
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(BrowseListTemplet.GROUP_ADD_EXPORT_LIST_CASE, new String[] { "3", "4", "5" })));
            // 预期
            addContent(allContentMap, LabelType.EXCEPT, Arrays
                    .asList(new Entry<>(BrowseListTemplet.GROUP_ADD_EXPORT_LIST_CASE, new String[] { "2", "2", "4" })));
        }
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * <p>
     * <b>文件名：DataListCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记“BrowseList”用例模板中存在的用例组名称
     * </p>
     * <p>
     * <b>编码时间：2023年3月23日 上午8:56:08
     * </p>
     * <p>
     * <b>修改时间：2023年3月23日 上午8:56:08
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    public class BrowseListTemplet {
        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String COMMON_CONTENT_RANK_1 = "1";
        public static final String COMMON_CONTENT_RANK_2 = "2";
        public static final String COMMON_CONTENT_RANK_3 = "3";
        public static final String COMMON_CONTENT_RANK_4 = "4";
        public static final String COMMON_CONTENT_PRECONDITION_BASIC = "basic";

        public static final String GROUP_COMMON_SEARCH_CASE = "commonSearchCase";
        public static final String COMMON_SEARCH_CASE_TITLE_BASIC = "basic";
        public static final String COMMON_SEARCH_CASE_KEY_BASIC = "basic";
        public static final String COMMON_SEARCH_CASE_EXCEPT_ALL_RESULT = "allResult";
        public static final String COMMON_SEARCH_CASE_EXCEPT_CONTAIN_RESULT = "containResult";
        public static final String COMMON_SEARCH_CASE_EXCEPT_NO_RESULT = "noResult";
        public static final String COMMON_SEARCH_CASE_EXCEPT_MUST_WRITE_CONTIDION = "mustWriteContidion";

        public static final String GROUP_EXAMINE_UI = "examineUI";
        public static final String GROUP_BROWSE_LIST_CASE = "BrowseListCase";
        public static final String GROUP_ADD_INPUT_SEARCH_CASE = "addInputSearchCase";
        public static final String GROUP_ADD_SELECT_SEARCH_CASE = "addSelectSearchCase";
        public static final String GROUP_ADD_DATE_SEARCH_CASE = "addDateSearchCase";
        public static final String GROUP_ADD_LIST_SORT_CASE = "addListSortCase";
        public static final String GROUP_ADD_EXPORT_LIST_CASE = "addExportListCase";
        public static final String GROUP_ADD_IMPORT_LIST_CASE = "addImportListCase";
        public static final String GROUP_ADD_RESET_SEARCH_CASE = "addResetSearchCase";
        public static final String GROUP_ADD_SWITCH_LIST_SHOW_DATA_CASE = "addSwitchListShowDataCase";
        public static final String GROUP_DELECT_DATA_CASE = "delectDataCase";

    }
	
    /**
     * <p>
     * <b>文件名：DataListCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义所有需要替换的词语
     * </p>
     * <p>
     * <b>编码时间：2023年3月22日 上午8:54:31
     * </p>
     * <p>
     * <b>修改时间：2023年3月22日 上午8:54:31
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    protected class ReplaceWord {
        public static final String INFORMATION = "信息";
        public static final String SEARCH_CONDITION = "搜索条件";
        public static final String OPTION_CONTENT = "选项内容";
        public static final String TIME_CONTENT = "时间内容";
        public static final String START_TIME = "开始时间";
        public static final String END_TIME = "结束时间";
        public static final String LIST_FIELD_NAME = "列表字段";
        public static final String EXPORT_BUTTON_NAME = "导出按钮";
        public static final String DONW_CONDITION = "下级选项";
        public static final String BATCHES_DELECT_BUTTON = "批量删除按钮";
        public static final String DELECT_BUTTON = "删除按钮";
	}

    /**
     * <p>
     * <b>文件名：DataListCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记数据列表的类型枚举
     * </p>
     * <p>
     * <b>编码时间：2023年3月24日 上午8:14:43
     * </p>
     * <p>
     * <b>修改时间：2023年3月24日 上午8:14:43
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    public enum DataListType {
        /**
         * app、H5类型页面列表
         */
        APP_LIST,
        /**
         * web端类型页面列表
         */
        WEB_LIST;
    }
}
