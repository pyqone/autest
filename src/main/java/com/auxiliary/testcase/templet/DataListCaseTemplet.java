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
		/**
		 * 提交按钮名称
		 */
        public static final String INFORMATION = "信息";
		/**
		 * 取消按钮名称
		 */
        public static final String SEARCH_CONDITION = "搜索条件";
		/**
		 * 开始时间
		 */
        public static final String START_TIME = "开始时间";
		/**
		 * 结束时间
		 */
        public static final String END_TIME = "结束时间";
		/**
		 * 导出按钮
		 */
        public static final String EXPORT_BUTTON_NAME = "导出按钮";
		/**
		 * 下级选项
		 */
        public static final String DONW_CONDITION = "下级选项";
        /**
         * 批量删除按钮
         */
        public static final String BATCHES_DELECT_BUTTON = "批量删除按钮";
		/**
		 * 删除按钮
		 */
        public static final String DELECT_BUTTON = "删除按钮";
	}
}
