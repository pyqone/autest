package com.auxiliary.testcase.templet;

import java.io.File;


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
     * @since autest 4.0.0
     */
    public DataListCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    /**
     * 构造对象，通过包内的默认模板，对类进行构造
     * 
     * @since autest 4.0.0
     */
    public DataListCaseTemplet() {
        super("BrowseList");
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
        public static final String START_TIME = "开始时间";
        public static final String END_TIME = "结束时间";
        public static final String EXPORT_BUTTON_NAME = "导出按钮";
        public static final String DONW_CONDITION = "下级选项";
        public static final String BATCHES_DELECT_BUTTON = "批量删除按钮";
        public static final String DELECT_BUTTON = "删除按钮";
	}
}
