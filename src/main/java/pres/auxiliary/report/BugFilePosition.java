package pres.auxiliary.report;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

class BugFilePosition {
	/**
	 * 设置配置文件存放的位置
	 */
	private final static String xml = "ConfigurationFiles/ReportConfigurationFile/TestReportConfiguration.xml";
	/**
	 * Bug编号
	 */
	public static int ID;
	/**
	 * 所属产品
	 */
	public static int PRODUCT;
	/**
	 * 所属模块
	 */
	public static int MODULE;
	/**
	 * 所属项目
	 */
	public static int PROJECT;
	/**
	 * Bug标题
	 */
	public static int TITLE;
	/**
	 * 严重程度
	 */
	public static int SIGNIFICANCE;
	/**
	 * Bug类型
	 */
	public static int TYPE;
	/**
	 * 步骤
	 */
	public static int STEP;
	/**
	 * 激活次数
	 */
	public static int ACTIVE_COUNT;
	/**
	 * 创建者
	 */
	public static int CREATE_PERSON;
	/**
	 * 创建时间
	 */
	public static int CREATE_TIME;
	/**
	 * 影响版本
	 */
	public static int VERSION;
	/**
	 * 指派给
	 */
	public static int ASSIGN_PERSON;
	/**
	 * 解决方案
	 */
	public static int SOULVE_WAY;

	//通过静态代码块的方式，在未构造类的情况下根据配置文件中的值来初始化其列数
	static {
		// 读取配置好的XML文件
		Document dom = null;
		try {
			dom = new SAXReader().read(new File(xml));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// 获取测试报告中的内容
		String type = dom.getRootElement().attributeValue("type");
		
		//判断测试由何处导出，之后初始化其值
		if ( type.equalsIgnoreCase("Zentao") || type.equalsIgnoreCase("") ) {
			ID = Zentao.ID.getCell();
			PRODUCT = Zentao.PRODUCT.getCell();
			MODULE = Zentao.MODULE.getCell();
			PROJECT = Zentao.PROJECT.getCell();
			TITLE = Zentao.TITLE.getCell();
			SIGNIFICANCE = Zentao.SIGNIFICANCE.getCell();
			TYPE = Zentao.TYPE.getCell();
			STEP = Zentao.STEP.getCell();
			ACTIVE_COUNT = Zentao.ACTIVE_COUNT.getCell();
			CREATE_PERSON = Zentao.CREATE_PERSON.getCell();
			CREATE_TIME = Zentao.CREATE_TIME.getCell();
			VERSION = Zentao.VERSION.getCell();
			VERSION = Zentao.VERSION.getCell();
			ASSIGN_PERSON = Zentao.ASSIGN_PERSON.getCell();
			SOULVE_WAY = Zentao.SOULVE_WAY.getCell();
		} else if ( type.equalsIgnoreCase("TFS") ) {
			ID = TFS.ID.getCell();
			PRODUCT = TFS.PRODUCT.getCell();
			MODULE = TFS.MODULE.getCell();
			PROJECT = TFS.PROJECT.getCell();
			TITLE = TFS.TITLE.getCell();
			SIGNIFICANCE = TFS.SIGNIFICANCE.getCell();
			TYPE = TFS.TYPE.getCell();
			STEP = TFS.STEP.getCell();
			ACTIVE_COUNT = TFS.ACTIVE_COUNT.getCell();
			CREATE_PERSON = TFS.CREATE_PERSON.getCell();
			CREATE_TIME = TFS.CREATE_TIME.getCell();
			VERSION = TFS.VERSION.getCell();
			VERSION = TFS.VERSION.getCell();
			ASSIGN_PERSON = TFS.ASSIGN_PERSON.getCell();
			SOULVE_WAY = TFS.SOULVE_WAY.getCell();
		} else {
			throw new InvalidBugListFileSignException("无效的Bug汇总表文件标识：" + type);
		}
	}

	/**
	 * 用于指向禅道导出的BUG汇总表文件中的各个信息
	 * 
	 * @author 彭宇琦
	 */
	private enum Zentao {
		/**
		 * Bug编号
		 */
		ID(0),
		/**
		 * 所属产品
		 */
		PRODUCT(1),
		/**
		 * 所属模块
		 */
		MODULE(2),
		/**
		 * 所属项目
		 */
		PROJECT(3),
		/**
		 * Bug标题
		 */
		TITLE(6),
		/**
		 * 严重程度
		 */
		SIGNIFICANCE(8),
		/**
		 * Bug类型
		 */
		TYPE(10),
		/**
		 * 步骤
		 */
		STEP(13),
		/**
		 * 激活次数
		 */
		ACTIVE_COUNT(16),
		/**
		 * 创建者
		 */
		CREATE_PERSON(19),
		/**
		 * 创建时间
		 */
		CREATE_TIME(20),
		/**
		 * 影响版本
		 */
		VERSION(21),
		/**
		 * 指派给
		 */
		ASSIGN_PERSON(22),
		/**
		 * 解决方案
		 */
		SOULVE_WAY(25);

		private int cell;

		private Zentao(int cell) {
			this.cell = cell;
		}

		public int getCell() {
			return cell;
		}
	}
	
	/**
	 * 用于指向TFS导出的BUG汇总表文件中的各个信息
	 * 
	 * @author 彭宇琦
	 */
	private enum TFS {
		/**
		 * Bug编号
		 */
		ID(-1),
		/**
		 * 所属产品
		 */
		PRODUCT(-1),
		/**
		 * 所属模块
		 */
		MODULE(-1),
		/**
		 * 所属项目
		 */
		PROJECT(-1),
		/**
		 * Bug标题
		 */
		TITLE(-1),
		/**
		 * 严重程度
		 */
		SIGNIFICANCE(-1),
		/**
		 * Bug类型
		 */
		TYPE(-1),
		/**
		 * 步骤
		 */
		STEP(-1),
		/**
		 * 激活次数
		 */
		ACTIVE_COUNT(-1),
		/**
		 * 创建者
		 */
		CREATE_PERSON(-1),
		/**
		 * 创建时间
		 */
		CREATE_TIME(-1),
		/**
		 * 影响版本
		 */
		VERSION(-1),
		/**
		 * 指派给
		 */
		ASSIGN_PERSON(-1),
		/**
		 * 解决方案
		 */
		SOULVE_WAY(-1);
		
		private int cell;

		private TFS(int cell) {
			this.cell = cell;
		}

		public int getCell() {
			return cell;
		}
	}
}
