package pres.auxiliary.work.old.testcase.templet;

import java.io.File;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public enum ZentaoExcel {
	/**
	 * 指向id为case的sheet
	 */
	SHEET_CASE("/templet/sheet[@id='case']"),
	/**
	 * 指向id为case的sheet中id为case_id的列
	 */
	CASE_COLUMN_CASE_ID(SHEET_CASE.getXpath() + "/column[@id='case_id']"),
	/**
	 * 指向id为case的sheet中id为module的列
	 */
	CASE_COLUMN_MODULE(SHEET_CASE.getXpath() + "/column[@id='module']"),
	/**
	 * 指向id为case的sheet中id为story的列
	 */
	CASE_COLUMN_STORY(SHEET_CASE.getXpath() + "/column[@id='story']"),
	/**
	 * 指向id为case的sheet中id为title的列
	 */
	CASE_COLUMN_TITLE(SHEET_CASE.getXpath() + "/column[@id='title']"),
	/**
	 * 指向id为case的sheet中id为step的列
	 */
	CASE_COLUMN_STEP(SHEET_CASE.getXpath() + "/column[@id='step']"),
	/**
	 * 指向id为case的sheet中id为expect的列
	 */
	CASE_COLUMN_EXPECT(SHEET_CASE.getXpath() + "/column[@id='expect']"),
	/**
	 * 指向id为case的sheet中id为keys的列
	 */
	CASE_COLUMN_KEYS(SHEET_CASE.getXpath() + "/column[@id='keys']"),
	/**
	 * 指向id为case的sheet中id为type的列
	 */
	CASE_COLUMN_TYPE(SHEET_CASE.getXpath() + "/column[@id='type']"),
	/**
	 * 指向id为case的sheet中id为priority的列
	 */
	CASE_COLUMN_RANK(SHEET_CASE.getXpath() + "/column[@id='rank']"),
	/**
	 * 指向id为case的sheet中id为state的列
	 */
	CASE_COLUMN_STATE(SHEET_CASE.getXpath() + "/column[@id='state']"),
	/**
	 * 指向id为case的sheet中id为apply的列
	 */
	CASE_COLUMN_STAGE(SHEET_CASE.getXpath() + "/column[@id='stage']"),
	/**
	 * 指向id为case的sheet中id为condition的列
	 */
	CASE_COLUMN_CONDITION(SHEET_CASE.getXpath() + "/column[@id='condition']"),
	/**
	 * 指向id为case的sheet中id为flow的列
	 */
	CASE_COLUMN_FLOW(SHEET_CASE.getXpath() + "/column[@id='flow']");

	// 用于存储xml文件位置
	private final String CONFIG_FILE_PATH = "ConfigurationFiles/CaseConfigurationFile/CaseTemplet.xml";

	// 存储xpath
	private String xpath;
	// 存储节点的标记
	private short type;
	// 存储节点的id属性
	private String id;
	// 用于存储父节点的id
	private String parentId;

	private ZentaoExcel(String xpath) {
		this.xpath = xpath;

		// 判断其xptah是否含有column，若包含，则表示其为column节点，否则为sheet节点
		if (this.xpath.indexOf("column") > -1) {
			this.type = 1;
		} else {
			this.type = 0;
		}

		// 获取其id属性
		this.id = xpath.substring(xpath.lastIndexOf("@id='") + "@id='".length(),
				xpath.lastIndexOf("'"));

		// 获取该节点的父节点
		if (this.type == 1) {
			this.parentId = this.xpath.substring(
					this.xpath.indexOf("@id='") + "@id='".length(),
					this.xpath.indexOf("']/column"));
		} else {
			this.parentId = "";
		}
	}

	/**
	 * 返回枚举对应的Elemen对象
	 * 
	 * @return Elemen对象
	 */
	public Element getElement() {
		try {
			return (Element) (new SAXReader().read(new File(CONFIG_FILE_PATH))
					.selectSingleNode(xpath));
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据Column元素的id查找元素，并将其枚举常量返回，若id查找不到，则返回null。根据调用的节点不同，其返回方式也有一些不同，
	 * 具体的方式如下：
	 * <ol>
	 * <li>若参数本身为sheet节点（type值为0），则判断每个元素的父节点id是否为当前节点的id，再判断子节点id（元素column节点的id
	 * ）是否为传入的id</li>
	 * <li>若参数本身为column节点（type值为1），则判断每个元素的父节点id是否为当前节点父节点的id，再判断子节点id（
	 * 元素column节点的id）是否为传入的id</li>
	 * </ol>
	 * 
	 * @param sonId
	 *            节点的id
	 * @return id对应的枚举常量
	 */
	public ZentaoExcel getColumnExcel(String sonId) {
		// 遍历枚举类中的所有枚举常量
		for (ZentaoExcel excel : ZentaoExcel.values()) {
			// 返回依据：
			// 1.若参数本身为sheet节点（type值为0），则判断每个元素的父节点id是否为当前节点的id，再判断子节点id（元素column节点的id）是否为传入的id
			// 2.若参数本身为column节点（type值为1），则判断每个元素的父节点id是否为当前节点父节点的id，再判断子节点id（元素column节点的id）是否为传入的id
			// 简单来说，若节点为sheet节点，则直接获取子节点；若节点为colnum节点，则先获取起sheet节点，再获取column节点
			if (type == (short) 0 && excel.parentId.equals(id)
					&& excel.id.equals(sonId)) {
				return excel;
			} else if (type == (short) 1 && excel.parentId.equals(parentId)
					&& excel.id.equals(sonId)) {
				return excel;
			}
		}

		return null;
	}

	/**
	 * 根据传入id来返回sheet节点
	 * 
	 * @param id
	 *            sheet节点id
	 * @return id对应的sheet节点
	 */
	public ZentaoExcel getSheetExcel(String id) {
		// 遍历枚举类中的所有枚举常量
		for (ZentaoExcel excel : ZentaoExcel.values()) {
			// 判断参数是否为sheet节点，再判断其id是否一致，都一致，则返回起枚举常量
			if (excel.type == (short) 0 && excel.id.equals(id)) {
				return excel;
			}
		}

		return null;
	}

	/**
	 * 用于返回当前column节点所在的sheet节点。若传入的是sheet节点，则直接返回本身
	 * 
	 * @return sheet节点
	 */
	public ZentaoExcel getSheetExcel() {
		// 判断当前节点是否为sheet节点，若是，则直接返回其本身
		if (type == (short) 0) {
			return this;
		}

		// 遍历枚举类中的所有枚举常量
		for (ZentaoExcel excel : ZentaoExcel.values()) {
			// 判断参数是否为sheet节点，再判断其id是否一致，都一致，则返回起枚举常量
			if (excel.type == (short) 0 && excel.id.equals(parentId)) {
				return excel;
			}
		}

		return null;
	}

	/**
	 * 用于返回元素对应的xpath路径
	 * 
	 * @return xpath路径
	 */
	public String getXpath() {
		return xpath;
	}

	/**
	 * 用于返回元素在xml文件中的位置，以达到确定其在Excel文件中所在的列（元素的下标从0开始）。
	 * 注意，该方法无论是sheet节点还是column节点，其返回的方法都是按照通过父节点来查找子节点的
	 * 方式进行，所以其返回值在不同的节点下返回的结果可能相同。
	 * 
	 * @return 元素位置
	 */
	public int getValue() {
		// 记录其元素的位置
		int value = -1;
		// 获取元素
		Element element = getElement();
		// 获取元素的父节点
		Element fElement = element.getParent();
		// 获取父节点下的所有节点的迭代器
		List<?> elements = fElement.elements();

		// 循环，遍历所有节点，找到与需要查找的元素匹配的元素，返回其位置
		for (int i = 0; i < elements.size(); i++) {
			if (((Element) (elements.get(i))).attributeValue("id")
					.equals(getElement().attributeValue("id"))) {
				value = i;
				break;
			}
		}
		return value;
	}

	/**
	 * 用于返回其元素对应的表格列标记，即在Excel中，第一列的标记为A，以此类推。
	 * 注意，若元素为sheet标签，则同样以其value值加上65的形式，以字符返回。
	 * 
	 * @return 元素在Excel文件中列的标记
	 */
	public char getCell() {
		return (char) (65 + getValue());
	}

	/**
	 * 用于返回节点的id属性
	 * 
	 * @return 节点的id属性
	 */
	public String getId() {
		return id;
	}

	/**
	 * 返回父节点的id属性
	 * 
	 * @return 父节点id属性
	 */
	public String getParentId() {
		return parentId;
	}
}
