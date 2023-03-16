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
	 * 用于列表名称
	 */
	public static final String DATA_NAME = WordType.INFORMATION.getName();
	
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
	 * <p><b>文件名：</b>DataListCase.java</p>
	 * <p><b>用途：</b>枚举在预设测试用例中需要被替换的词语</p>
	 * <p><b>编码时间：</b>2020年3月15日下午5:50:28</p>
	 * <p><b>修改时间：</b>2020年3月15日下午5:50:28</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	private enum WordType {
		/**
		 * 提交按钮名称
		 */
		INFORMATION("信息"), 
		/**
		 * 取消按钮名称
		 */
		SEARCH_CONDITION("搜索条件"), 
		/**
		 * 开始时间
		 */
		START_TIME("开始时间"), 
		/**
		 * 结束时间
		 */
		END_TIME("结束时间"), 
		/**
		 * 导出按钮
		 */
		EXPORT_BUTTON_NAME("导出按钮"), 
		/**
		 * 下级选项
		 */
		DONW_CONDITION("下级选项"), 
		/**
		 * 删除按钮
		 */
		DELECT_BUTTON("删除按钮"), 
		;
		/**
		 * 存储需要替换的单词名称
		 */
		private String name;

		/**
		 * 初始化枚举值
		 * @param name 枚举的名称
		 */
		private WordType(String name) {
			this.name = name;
		}

		/**
		 * 返回需要替换的单词的名称
		 * @return 需要替换的单词的名称
		 */
		public String getName() {
			return name;
		}
	}
}
