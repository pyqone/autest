package pres.auxiliary.tool.file.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.testcase.file.IncorrectFileException;
import pres.auxiliary.testcase.templet.LabelNotFoundException;
import pres.auxiliary.tool.data.TableData;
import pres.auxiliary.tool.file.MarkColorsType;
import pres.auxiliary.tool.file.TableFileReadUtil;
import pres.auxiliary.tool.file.UnsupportedFileException;
import pres.auxiliary.tool.regex.RegexType;

/**
 * <p>
 * <b>文件名：</b>AbstractTestCaseWrite.java
 * </p>
 * <p>
 * <b>用途：</b>提供由xml配置文件生成的Excel文件中添加内容的基本方法。
 * </p>
 * <p>
 * <b>编码时间：</b>2020年2月17日下午9:36:00
 * </p>
 * <p>
 * <b>修改时间：</b>2020年8月22日 下午17:29:37
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since POI 3.15
 */
public abstract class AbstractWriteExcel<T extends AbstractWriteExcel<T>> {
	/**
	 * 用于指向用例的XSSFWorkbook对象
	 */
	protected XSSFWorkbook xw;

	/**
	 * 用于对待替换词语的标记
	 */
	protected final String WORD_SIGN = "\\#";

	/**
	 * 用于存储一条用例的信息，第一个参数指向配置文件中的字段id，第二个字段为xml文件中字段的相应信息
	 */
	protected HashMap<String, HashMap<String, Field>> fieldMap = new HashMap<>(16);

	/**
	 * 用于存储所有用例均使用的字段常值
	 */
	protected HashMap<String, HashMap<String, String>> constValueMap = new HashMap<>(16);

	/**
	 * 用于存储待替换的词语以及被替换的词语
	 */
	protected HashMap<String, ReplactFunction> replaceWordMap = new HashMap<>(16);

	/**
	 * 用于存储当前用例中正在编写的
	 */
	protected ArrayList<String> writeSheetNameList = new ArrayList<>();

	/**
	 * 用于存储当前对应的sheet名称
	 */
	protected String nowSheetName = "";

	/**
	 * 指向模板文件
	 */
	protected File tempFile;

	/**
	 * 指向配置文件的Document对象
	 */
	protected Document configXml;

	/**
	 * 指向存储测试用例的Document类，该类将不创建xml文件，但最终用于将doucument中的内容写入excel中
	 */
	protected Document contentXml;

	/**
	 * 通过测试文件模板xml配置文件和测试用例文件进行构造。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param tempFile   测试用例文件类对象
	 * @throws DocumentException      读取xml文件有误时抛出的异常
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public AbstractWriteExcel(File configFile, File tempFile) throws DocumentException {
		this(new SAXReader().read(configFile), tempFile);
	}

	/**
	 * 通过测试文件模板xml配置文件的{@link Document}类对象和测试用例文件进行构造。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configDocument 测试文件模板xml配置文件{@link Document}类对象
	 * @param tempFile       测试用例文件类对象
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public AbstractWriteExcel(Document configDocument, File tempFile) {
		this.configXml = configDocument;

		// 判断路径是否包含“.xlsx”（使用的方法为XSSFWork）
		if (tempFile.isFile() && tempFile.getAbsolutePath().indexOf(".xlsx") > -1) {
			this.tempFile = tempFile;
		} else {
			throw new IncorrectFileException("不正确的文件格式：" + tempFile.getAbsolutePath());
		}

		// 获取xml文件中的第一个sheet标签，则将该标签的name属性内容传入getColumnId中
//		switchSheet(configXml.getRootElement().element("sheet").attributeValue("name"));
		getAllColumnId();

		// 创建存储字段内容的document类对象
		contentXml = DocumentHelper.createDocument();
		// 写入根节点
		contentXml.addElement("cases");
	}

	/**
	 * 该方法用于切换sheet，以在另一个sheet中添加信息。需要注意的是，在切换sheet前，
	 * 若在上一个sheet中编写了内容，需要调用{@link AbstractWriteExcel#end()}方法 保存内容后再切换，否则会清空未保存的内容
	 * 
	 * @param sheetName xml配置文件中sheet的name字段内容
	 */
	@SuppressWarnings("unchecked")
	public T switchSheet(String sheetName) {
		// 将相应的sheet标签的name属性存储至sheetName中
		this.nowSheetName = sheetName;
		if (!writeSheetNameList.contains(sheetName)) {
			writeSheetNameList.add(sheetName);
		}

		return (T) this;
	}

	/**
	 * 用于在当前指向的sheet中设置字段名称的常值，通过该设置，则之后该字段将直接填入设置的值，无需再次写入字段的值
	 * 
	 * @param field   字段id
	 * @param content 相应字段的内容
	 * 
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public void setFieldValue(String field, String content) {
		setFieldValue(nowSheetName, field, content);
	}

	/**
	 * 用于在指定的sheet中设置字段名称的常值，通过该设置，则之后该字段将直接填入设置的值，无需再次写入字段的值
	 * 
	 * @param sheetName 指定的sheet名称
	 * @param field     字段id
	 * @param content   相应字段的内容
	 * 
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public void setFieldValue(String sheetName, String field, String content) {
		// 若当前sheetName中不存在常量，则先构造HashMap
		if (!constValueMap.containsKey(sheetName)) {
			constValueMap.put(sheetName, new HashMap<String, String>(16));
		}

		// 添加常量
		constValueMap.get(sheetName).put(field, content);
	}

	/**
	 * 用于设置需要被替换的词语。添加词语时无需添加特殊字符
	 * 
	 * @param word        需要替换的词语
	 * @param replactWord 被替换的词语
	 */
	public void setReplactWord(String word, String replactWord) {
		setReplactWord(word, (text) -> {
			return replactWord;
		});
	}

	/**
	 * 用于根据需要替换的词语，设置需要动态写入到文本的内容。添加词语时无需添加特殊字符
	 * 
	 * @param word            需要替换的词语
	 * @param replactFunction 替换规则
	 */
	public void setReplactWord(String word, ReplactFunction replactFunction) {
		replaceWordMap.put(word, replactFunction);
	}

	/**
	 * 通过传入的字段id，将对应的字段内容写入到用例最后的段落中，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#<br>
	 * 添加数据时，其亦可对存在数据有效性的数据进行转换，在传值时，只需要传入相应的字段值即可，例如：<br>
	 * 当字段存在两个数据有效性：“测试1”和“测试2”时，则，可传入addContent(..., "1")（注意，下标从1开始），
	 * 此时，文件中该字段的值将为“测试1”，若传入的值无法转换成数字，则直接填入传入的内容，具体说明可以参见{@link Field#getDataValidation(int)}。
	 * 
	 * @param field    字段id
	 * @param contents 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public T addContent(String field, String... contents) {
		return insertContent(field, -1, contents);
	}

	/**
	 * 用于对已存储的内容进行移除，若传入的下标超出当前的内容的段落数时，则不做处理
	 * 
	 * @param field  字段id
	 * @param indexs 需要删除的段落下标
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public T removeContent(String field, int... indexs) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.get(nowSheetName).containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}

		// 移除相关的内容，若输入的序号不存在，则不进行
		Arrays.stream(indexs).forEach(index -> {
			try {
				fieldMap.get(nowSheetName).get(field).content.remove(index);
			} catch (Exception e) {
			}
		});

		return (T) this;
	}

	/**
	 * 通过传入的字段id，将对应的字段内容写入到用例文本的相应段落中，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#<br>
	 * 添加数据时，其亦可对存在数据有效性的数据进行转换，在传值时，只需要传入相应的字段值即可，例如：<br>
	 * 当字段存在两个数据有效性：“测试1”和“测试2”时，则，可传入insertContent(..., "1")（注意，下标从1开始），
	 * 此时，文件中该字段的值将为“测试1”，若传入的值无法转换成数字，则直接填入传入的内容。具体说明可以参见{@link Field#getDataValidation(int)}
	 * 
	 * @param field    字段id
	 * @param index    需要插入的段落
	 * @param contents 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public T insertContent(String field, int index, String... contents) {
		// 若writeSheetNameList中未存储内容，则将当前的sheet名称存储到writeSheetNameList中，
		// 用于记录当前sheet有内容更新
		if (!writeSheetNameList.contains(nowSheetName)) {
			writeSheetNameList.add(nowSheetName);
		}

		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.get(nowSheetName).containsKey(field)) {
			throw new LabelNotFoundException("当前“" + nowSheetName + "”sheet中不存在标签id：" + field);
		}

		// 若未传值或传入null，则直接结束
		if (contents == null || contents.length == 0) {
			return (T) this;
		}

		// 还原下标
		/*
		 * System.out.print(nowSheetName + " = ");
		 * System.out.print(fieldMap.get(nowSheetName).get(field).name + " = ");
		 * System.out.println(fieldMap.get(nowSheetName).get(field).content.size());
		 */
		index = getPoiIndex(fieldMap.get(nowSheetName).get(field).content.size(), index);

		if (fieldMap.get(nowSheetName).get(field).datas.size() != 0) {
			// 查找数据有效性，若当前字段存在数据有效性，则将数据有效性转义，若添加的字段无法转义，则存储原内容
			contents = dataValidityChange(contents, fieldMap.get(nowSheetName).get(field));
		}
		// 查找特殊词语，并对词语进行替换
		contents = replaceWord(contents);

		// 将字段内容写入fieldMap，若插入的下标不正确，则不做任何处理
		try {
			fieldMap.get(nowSheetName).get(field).content.addAll(
					fieldMap.get(nowSheetName).get(field).content.size() == 0 ? 0 : index + 1, Arrays.asList(contents));
		} catch (Exception e) {
		}

		return (T) this;
	}

	/**
	 * 通过传入的字段id，将对应的字段相应段落的内容替换成传入的文本或文本组，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#
	 * 
	 * @param field    字段id
	 * @param index    需要插入的段落
	 * @param contents 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public T replaceContent(String field, int index, String... contents) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.get(nowSheetName).containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}

		// 若未传值或传入null，则直接结束
		if (contents == null || contents.length == 0) {
			return (T) this;
		}

		// 若传入的下标大于相应的最大段落数时，则直接结束
		if (index >= fieldMap.get(nowSheetName).get(field).content.size()) {
			return (T) this;
		}

		// 移除相应的段落
		removeContent(field, index);
		// 在原位置上插入相应的内容
		insertContent(field, index, contents);

		return (T) this;
	}

	/**
	 * 用于清空字段中存储的内容
	 * 
	 * @param field 字段id
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public T clearContent(String field) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.get(nowSheetName).containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}

		fieldMap.get(nowSheetName).get(field).content.clear();

		return (T) this;
	}

	/**
	 * 用于结束一条用例写作的标志，调用该方法后将结束一条用例的编写， 该方法以下的添加用例内容的方法均为编辑另一条用例
	 */
	public FieldMark end() {
		if (writeSheetNameList.isEmpty()) {
			throw new IncorrectIndexException("当前不存在需要写入的sheet内容");
		}

		// 生成case标签的uuid
		String caseUuid = UUID.randomUUID().toString();
		// 创建case标签
		writeSheetNameList.forEach(sheetName -> {
			// 写入到caseXml中
			// 通过xpath查询到相应的sheet标签
			String finSheetXpath = "//sheet[@name='" + sheetName + "']";
			Element sheetElement = (Element) (contentXml.selectSingleNode(finSheetXpath));

			// 判断是否存在sheet标签，若不存在，则创建相应的标签
			if (sheetElement == null) {
				sheetElement = contentXml.getRootElement().addElement("sheet").addAttribute("name", sheetName);
			}

			Element caseElement = sheetElement.addElement("case").addAttribute("id", caseUuid);
			// 为fieldMap中的所有key创建field标签，并记录相应的value
			fieldMap.get(sheetName).forEach((id, field) -> {
				// 判断字段是否需要进行编号，若需要编号，则调用编号方法
				if (field.serialNumber) {
					addSerialNumber(field);
				}

				// 添加field标签，并设置name属性（字段名称），mark属性（备注内容）
				// dom4j当属性值传入null时，则直接不会创建该属性，故此处无需做判断字段id是否在fieldMarkMap中
				Element fieldElement = caseElement.addElement("field").addAttribute("id", id).addAttribute("name",
						field.name);

				// 判断当前是否有添加内容，若未添加内容，则创建一个value属性为空的text标签
				if (field.content != null && !field.content.isEmpty()) {
					// 读取所有texts（字符串数组）的所有内容，并为每一元素创建一个text标签，将值加入属性中
					field.content.forEach(text -> {
						fieldElement.addElement("text").addAttribute("value", text);
					});
				} else {
					// 判断当前字段是否存在于constValueMap中，若存在，则填写constValueMap中的内容
					if (constValueMap != null && constValueMap.get(nowSheetName) != null
							&& constValueMap.get(nowSheetName).containsKey(field.id)) {
						// TODO 解决常值只能添加一个问题时需要修改此处代码
						fieldElement.addElement("text").addAttribute("value",
								dataValidityChange(new String[] { constValueMap.get(nowSheetName).get(field.id) },
										field)[0]);
					} else {
						fieldElement.addElement("text").addAttribute("value", "");
					}
				}
			});

			// 清空fieldMap中的内容
			clearFieldContent(sheetName);
			// 将字段常值设置入fieldMap中，若抛出异常，则不进行处理
			/*
			 * if (constValueMap != null && constValueMap.size() != 0) {
			 * constValueMap.get(nowSheetName).forEach((field, content) -> { try {
			 * addContent(field, content); } catch (LabelNotFoundException e) { } }); }
			 */
		});

		// 清空sheet写入记录
		writeSheetNameList.clear();
		return new FieldMark(caseUuid);
	}

	/**
	 * 当用例编写完成后，调用该方法，将编写的内容写入至模板文件中
	 * 
	 * @throws IOException            流异常时抛出的异常
	 * @throws IncorrectFileException 当模板文件内容异常时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public void writeFile() throws IOException {
		// 定义输入流，用于读取模版文件
		FileInputStream fip = new FileInputStream(tempFile);
		// 通过输入流，使XSSFWorkbook对象指向模版文件
		xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 读取contentXml中存储的所有sheet标签
		List<Element> sheetElements = contentXml.getRootElement().elements("sheet");
		for (Element sheetElement : sheetElements) {
			// 通过sheet标签的来读取excel中sheet的名称
			XSSFSheet xs = xw.getSheet(sheetElement.attributeValue("name"));
			// 判断是否获取到sheet，若未获取到sheet，则抛出异常
			if (xs == null) {
				throw new IncorrectFileException("不存在sheet名称：" + sheetElement.attributeValue("name"));
			}
			// 获取用例标签，将用例标签中的内容写入到文件中
			List<Element> caseElements = sheetElement.elements("case");
			for (Element caseElement : caseElements) {
				writeContent(xs.getLastRowNum() + 1, xs, caseElement);
			}
		}

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(tempFile);
		// 写入模版
		xw.write(fop);
		// 关闭流
		fop.close();
		xw.close();
	}

	/**
	 * 用于将case标签的内容写入到文本中
	 * 
	 * @param index       写入行号
	 * @param xs          sheet
	 * @param caseElement case标签对应的elemenet对象
	 * @return 当前行号
	 */
	@SuppressWarnings("unchecked")
	private void writeContent(int index, XSSFSheet xs, Element caseElement) {
		// 获取字段元素，需要获取配置xml文件中的以及用例xml文件中的字段
		List<Element> fieldElements = caseElement.elements("field");
		// 遍历所有的field标签，将标签的内容写入到文件中
		for (Element fieldElement : fieldElements) {
			// 获取相应的Field对象
			String fieldId = fieldElement.attributeValue("id");
			// 获取字段的field对象
			Field field = fieldMap.get(xs.getSheetName()).get(fieldId);
			// 获取text标签
			List<Element> textList = fieldElement.elements("text");

			// 判断当前字段是否需要分行编写内容
			if (field.rowText < 1) {
				writeCommonField(xs, fieldElement, fieldId, field, textList, index);
			} else {
				writeSpecificField(xs, fieldElement, fieldId, field, textList, index);
			}
		}
	}

	/**
	 * 写入无需多行编辑（每段分行）的字段信息
	 * 
	 * @param xs           sheet类对象
	 * @param fieldElement 字段元素
	 * @param fieldId      字段的id
	 * @param field        字段的Field对象
	 * @param textList     字段对应的内容
	 * @param index        写入的行下标
	 */
	private void writeCommonField(XSSFSheet xs, Element fieldElement, String fieldId, Field field,
			List<Element> textList, int index) {
		// 创建或读取测试用例所在的行
		XSSFRow xr;
		if ((xr = xs.getRow(index)) == null) {
			xr = xs.createRow(index);
		}

		// 创建字段所在的列相应的单元格
		XSSFCell xc = xr.createCell(field.index);

		// TODO 对单元格进行操作
		// 设置单元格的样式
		xc.setCellStyle(getFieldStyle(field, fieldElement));
		// 向单元格中添加数据有效性
		field.addDataValidation(xs, index, index);
		// 向单元格中添加Comment注解
		addComment(xs, xc, fieldElement);

		// 将字段内容写入单元格
		writeText(xc, textList);
		// 向单元格中添加超链接
		addLink(xc, fieldElement.attributeValue("link"));
	}

	/**
	 * 写入需要多行编辑（每段分行）的字段信息
	 * 
	 * @param xs           sheet类对象
	 * @param fieldElement 字段元素
	 * @param fieldId      字段的id
	 * @param field        字段的Field对象
	 * @param textList     字段对应的内容
	 * @param index        写入的行下标
	 */
	private void writeSpecificField(XSSFSheet xs, Element fieldElement, String fieldId, Field field,
			List<Element> textList, int index) {
		// 创建或读取测试用例所在的行
		XSSFRow xr;
		if ((xr = xs.getRow(index)) == null) {
			xr = xs.createRow(index);
		}

		// 用于控制当前斜土步骤的行位于标题行下降的行数
		int nowRowIndex = 0;
		// 若需要通过该方法写入用例，则必然有数据需要写入，则先写入数据，再做判断
		// 判断的方法为，以单元格较原位置下降的多少进行判断，例如有以下几个场景（设置的stepNum为2）：
		// 1.字段中存储了5个text标签
		// 2.字段中存储了3个text标签
		// 3.字段中存储了4个text标签
		// 由于在前面的代码已经运行并存储了2个text标签的内容，故
		// 针对场景1：先执行一次存储，此时表格的行较原来下降了1行，用例实际写入了4条，但写入的数据少于text数量，故需要继续循环
		// 针对场景2、3：先执行一次存储，此时表格的行较原来下降了1行，用例实际写入了3或4条，写入的数据等于text数量，故结束循环
		// 综合考虑，得到公式(stepNum * ++nowRowIndex)正好等于当前写入用例的条数，且nowRowIndex自增后可以作为下一次循环开始
		// 使用公式的值与用例总数判断，当公式值大于或等于text数量时，则结束循环
		do {
			// 判断当前行是否被创建，若未被创建，则读取相应的行号
			xr = xs.getRow(index + nowRowIndex) == null ? xs.createRow(index + nowRowIndex)
					: xs.getRow(index + nowRowIndex);
			// 创建字段所在的列相应的单元格
			XSSFCell xc = xr.createCell(field.index);

			// TODO 对单元格进行操作
			// 设置单元格的样式
			xc.setCellStyle(getFieldStyle(field, fieldElement));
			// 向单元格中添加数据有效性
			field.addDataValidation(xs, index, index);
			// 向单元格中添加Comment注解
			addComment(xs, xc, fieldElement);
			// 向单元格中添加超链接
			addLink(xc, fieldElement.attributeValue("link"));

			// 存储裁剪后的text元素
			ArrayList<Element> subTextList = new ArrayList<Element>();
			// 其中步骤数乘当前写入行的行数正好可以得到应该从哪个元素开始裁剪，例如
			// 字段中存储了5个text标签，此时设置的stepNum为2，在运行该代码前已经写入了2个text的内容，故循环从2开始（表示从第3个元素元素开始）
			// 当下一次循环时，nowRowIndex为2，此时2 * 2 = 4，正好可以得到从第5个元素开始，此时在写入时也只会写入一次
			for (int i = 0; i < field.rowText; i++) {
				// 若剩余内容数小于stepNum时，此时循环在读取textList会抛出数组越界异常，则捕捉抛出异常后直接结束循环
				try {
					subTextList.add(textList.get(field.rowText * nowRowIndex + i));
				} catch (IndexOutOfBoundsException e) {
					break;
				}
			}

			// 将字段内容写入单元格
			writeText(xc, subTextList);
		} while (field.rowText * ++nowRowIndex < textList.size());
	}

	/**
	 * 用于根据超链接内容，向单元格中添加超链接
	 * 
	 * @param xc          单元格对象
	 * @param linkContent 超链接内容（形式“超链接类型=超链接内容”）
	 */
	private void addLink(XSSFCell xc, String linkContent) {
		// 若不存在链接内容，则不添加链接
		if (linkContent == null || linkContent.isEmpty()) {
			return;
		}

		// 获取分隔超链接类型与内容的符号位置
		int splitIndex = linkContent.indexOf("=");
		// 拆分读取到的超链接文本，以此分为超链接类型与超链接内容
		String linkTypeName = linkContent.substring(0, splitIndex);
		String linkText = linkContent.substring(splitIndex + 1);

		CreationHelper createHelper = xw.getCreationHelper();
		XSSFHyperlink link = null;

		// 添加本地文件链接
		HyperlinkType hyperlinkType = getHyperlinkType(linkTypeName);
		link = (XSSFHyperlink) createHelper.createHyperlink(hyperlinkType);
		// 添加超链接
		link.setAddress(linkText);

		// 在单元格上添加超链接
		xc.setHyperlink(link);
		// 新增单元格的样式
//		CellStyleType.LINK_TEXT.getXSSFCellStyle(xc);
		// 获取字体信息
//		XSSFFont xf = xc.getCellStyle().getFont();
		// 添加下划线
//		xf.setUnderline((byte) 1);
		// 设置字体颜色为蓝色
//		xf.setColor(IndexedColors.BLUE.getIndex());
	}

	/**
	 * 用于根据链接类型名称，返回相应的超链接枚举（{@link HyperlinkType}）
	 * 
	 * @param linkTypeName 超链接类型名称
	 * @return 超链接枚举
	 */
	protected HyperlinkType getHyperlinkType(String linkTypeName) {
		LinkType linkType = null;
		for (LinkType type : LinkType.values()) {
			if (type.getName().equals(linkTypeName)) {
				linkType = type;
			}
		}

		if (linkType == null) {
			throw new NoSuchTypeException("错误的枚举名称：" + linkTypeName);
		}

		switch (linkType) {
		case DOMCUMENT:
			return HyperlinkType.DOCUMENT;
		case EMAIL:
			return HyperlinkType.EMAIL;
		case FILE:
			return HyperlinkType.FILE;
		case URL:
			return HyperlinkType.URL;
		default:
			return HyperlinkType.NONE;
		}
	}

	/**
	 * 返回文本中字段对应的样式，可在该方法中添加字段需要添加的相应样式
	 * 
	 * @param field        字段id
	 * @param fieldElement 字段元素
	 * @return 字段对应的样式
	 */
	private XSSFCellStyle getFieldStyle(Field field, Element fieldElement) {
		// 获取字段最基本的样式
		XSSFCellStyle xcs = field.getCellStyle();

		// 判断字段上是否需要添加背景色，若需要添加背景色，则将相应参数写入到xcs中
		String backgroundColorText;
		if ((backgroundColorText = fieldElement.attributeValue("background")) != null) {
			short backgroundcolor = Short.valueOf(backgroundColorText);

			// 为保证添加背景后仍能看清单元格中的文本，故背景采用细左斜线
			xcs.setFillPattern(FillPatternType.THIN_FORWARD_DIAG);
			xcs.setFillForegroundColor(backgroundcolor);
		}

		return xcs;
	}

	/**
	 * 用于向单元格上添加Comment标注
	 * 
	 * @param xs           sheet对象
	 * @param xc           相应单元格对象
	 * @param fieldElement 字段元素
	 */
	private void addComment(XSSFSheet xs, XSSFCell xc, Element fieldElement) {
		// 获取字段的comment标签内容
		String commentContent = fieldElement.attributeValue("comment");
		// 判断内容是否存在，若不存在，则结束方法运行，不添加标注
		if (commentContent == null) {
			return;
		}

		// 创建一个标注，并确定其在一个单元格的左上角和右下角
		Comment com = xs.createDrawingPatriarch().createCellComment(xw.getCreationHelper().createClientAnchor());
		// 创建标注的内容
		com.setString(xw.getCreationHelper().createRichTextString(commentContent));
		// 创建标注的作者(作者为计算机名称)
		com.setAuthor(System.getenv().get("COMPUTERNAME"));
		// 将标注附加到单元格上
		xc.setCellComment(com);
	}

	/**
	 * 获取并返回字段对应的内容
	 * 
	 * @param textList 字段的文本元素
	 * @return 字段对应的内容
	 */
	private void writeText(XSSFCell xc, List<Element> textList) {
		// 存储文本内容，由于文本可能带颜色，故使用富文本来存储文本内容
		XSSFRichTextString xrts = new XSSFRichTextString("");

		// 遍历text标签
		for (int index = 0; index < textList.size(); index++) {
			// 为每一行添加回车，若在第一行，则不加入回车
			if (index != 0) {
				xrts.append("\n");
			}

			// 创建字体
			XSSFFont xf = xw.createFont();
			// 设置字体名称
			xf.setFontName("宋体");
			// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
			xf.setFontHeightInPoints((short) 12);

			// 获取text标签的colors属性
			String colorsText = textList.get(index).attributeValue("colors");
			// 判断获取到的元素是否为null，即该属性是否存在，若存在，则加入相应的颜色到字体中
			if (colorsText != null) {
				// 设置颜色
				xf.setColor(Short.valueOf(colorsText));
			}

			// 拼接文本
			xrts.append(textList.get(index).attributeValue("value"), xf);
		}

		// 将文本值设置入单元格中
		xc.setCellValue(xrts);
	}

	/**
	 * 清空fieldMap内的存储字段信息，不清除字段
	 */
	private void clearFieldContent(String sheetName) {
		fieldMap.get(sheetName).forEach((fieldId, field) -> {
			field.clearContent();
		});
	}

	/**
	 * 该方法用于获取相应sheet下column字段的id内容，存储至相应的集合中
	 * 
	 * @param sheetName sheet的name属性
	 */
	@SuppressWarnings("unchecked")
	private void getAllColumnId() {
		// 清空fieldMap中的内容
//		fieldMap.clear();

		// 获取相应的sheet标签元素
//		Element sheetElement = (Element) (configXml.selectSingleNode("//sheet[@name='" + sheetName + "']"));
		configXml.getRootElement().elements("sheet").forEach(sheetElement -> {
			// 获取相应的sheet标签下的column标签
			List<Element> column = ((Element) sheetElement).elements("column");

			// 初始化fieldMap中的字段及其值
			for (int index = 0; index < column.size(); index++) {
				// 查找xml文件中数据有效性标签
				List<Element> datasList = ((Element) sheetElement).elements("datas");
				ArrayList<String> datas = new ArrayList<String>();
				// 遍历所有的数据有效性标签，若存在，则存储相应的数据有效性
				for (Element datasElemenet : datasList) {
					if (datasElemenet.attributeValue("id").equals(column.get(index).attributeValue("id"))) {
						// 存储当前的数据有效性的内容
						((List<Element>) (datasElemenet.elements())).forEach(textElement -> {
							// 判断数据有效性的存放位置，并按照相应的方法读取数据有效性
							if ("file".equalsIgnoreCase(textElement.getName())) {
								datas.addAll(getFileDataValidity(textElement));
							} else {
								datas.add(getLabelDataValidity(textElement));
							}
						});
						break;
					}
				}

				// 若fieldMap中不存在sheetName，则添加相应的内容
				String sheetName = ((Element) sheetElement).attributeValue("name");
				if (!fieldMap.containsKey(sheetName)) {
					fieldMap.put(sheetName, new HashMap<String, Field>(16));
				}

				nowSheetName = nowSheetName.isEmpty() ? sheetName : nowSheetName;

				// 存储字段信息
				fieldMap.get(sheetName).put(column.get(index).attributeValue("id"),
						new Field(column.get(index).attributeValue("id"), column.get(index).attributeValue("name"),
								column.get(index).attributeValue("align"), index,
								column.get(index).attributeValue("row_text"), datas,
								Boolean.valueOf(column.get(index).attributeValue("index"))));
			}
		});
	}

	/**
	 * 用于获取写在xml文件中的数据有效性
	 * 
	 * @param textElement 数据标签
	 * @return 标签内的数据
	 */
	private String getLabelDataValidity(Element textElement) {
		return textElement.attributeValue("name");
	}

	/**
	 * 用于获取写在外部文件中的数据有效性
	 * 
	 * @param textElement 数据标签
	 * @return 从外部文件中读取到的数据
	 */
	private ArrayList<String> getFileDataValidity(Element textElement) {
		ArrayList<String> dataList = new ArrayList<>();
		try {
			// 读取数据有效性文件内容
			File dataFile = new File(Optional.ofNullable(textElement.attributeValue("path"))
					.orElseThrow(() -> new NoSuchTypeException("未指定数据有效性文件路径（path属性）")));
			String sheet = Optional.ofNullable(textElement.attributeValue("regex"))
					.orElseThrow(() -> new NoSuchTypeException("未指定数据有效性文件读取规则（regex属性）"));
			// 读取文件数据
			TableData<String> dataTable = TableFileReadUtil.readExcel(dataFile, sheet, false);

			// 获取需要读取的列信息，若未指定列信息，则默认读取第一列
			String columnName = dataTable.getFieldName(Optional.ofNullable(textElement.attributeValue("column")).map(Integer::valueOf).filter(dataTable::isFieldIndex).orElse(0));
			// 读取起始行信息，若未指定起始行信息，则默认读取第一行
			int startRowIndex = Optional.ofNullable(textElement.attributeValue("start_row")).map(Integer::valueOf)
					.orElse(1);
			// 读取结束行信息，若未指定起始行信息，则默认读取最后一行
			int endRowIndex = Optional.ofNullable(textElement.attributeValue("end_row")).map(Integer::valueOf)
					.orElse(-1);
			
			// 获取数据信息
			dataList.addAll(dataTable.getData(startRowIndex, endRowIndex, columnName).get(columnName).stream()
					//将封装类转换为字符串类，空封装类则转译为空串
					.map(data -> data.orElse(""))
					//过滤掉空串
					.filter(text -> !text.isEmpty())
					//重组元素
					.collect(Collectors.toList()));
		} catch (Exception e) {
			throw new UnsupportedFileException("配置文件异常，无法读取数据", e);
		}

		return dataList;
	}

	/**
	 * 用于对需要进行替换的特殊词语进行替换
	 * 
	 * @param contents 文本内容
	 */
	protected String[] replaceWord(String[] contents) {
		// 查找特殊词语，并对词语进行替换
		for (String word : replaceWordMap.keySet()) {
			// 查找所有的0内容，并将特殊词语进行替换
			for (int i = 0; i < contents.length; i++) {
				String regex = WORD_SIGN + word + WORD_SIGN;
				contents[i] = contents[i].replaceAll(regex, replaceWordMap.get(word).replact(word));
			}
		}

		return contents;
	}

	/**
	 * 将数据转化为相应的数据有效性中的内容，当不存在数据有效性或传入的数据无法转换时，则返回原串
	 * 
	 * @param contents 文本内容
	 * @param field    字段相应的对象
	 * @return 转换后的文本内容
	 */
	private String[] dataValidityChange(String[] contents, Field field) {
		// 判断字段是否有数据有效性，若没有，则直接返回原串
		if (field.datas.size() == 0) {
			return contents;
		}

		// 查找所有的内容，并将特殊词语进行替换
		for (int i = 0; i < contents.length; i++) {
			try {
				// 转换内容为
				int index = Integer.valueOf(contents[i]);
				// 若需要添加序号，则先获取当前列表中的字段个数，以便于继续编号
				contents[i] = field.getDataValidation(index);
			} catch (NumberFormatException e) {
				// 若传入的内容无法转换，则继续循环
				continue;
			}
		}

		return contents;
	}

	/**
	 * 对段落的文本进行编号
	 * 
	 * @param field 段落类对象
	 */
	private void addSerialNumber(Field field) {
		for (int index = 0; index < field.content.size(); index++) {
			field.content.set(index, String.valueOf(index + 1) + "." + field.content.get(index));
		}
	}

	/**
	 * 用于根据xml文件中columenId字段所在的位置，推出字段在excel中的数字下标
	 * 
	 * @param sheetName 字段所在的sheet名称
	 * @param columenId xml文件 中字段的id
	 * @return 字段在excel中的下标
	 * @throws IncorrectIndexException 当传入的sheet名称或列id不正确时抛出的异常
	 */
	protected int getColumnNumIndex(String sheetName, String columenId) {
		// 根据sheet名称获取相应的sheet元素
		Element sheetElement = (Element) configXml.selectSingleNode("//sheet[@name='" + sheetName + "']");

		if (sheetElement == null) {
			throw new IncorrectIndexException("不存在的sheet名称：" + sheetName);
		}

		// 查找元素下所有column标签
		List<?> columnElementList = sheetElement.elements("column");
		// 遍历columnElementList，根据columenId反推对应的元素所在的下标
		for (int index = 0; index < columnElementList.size(); index++) {
			if (((Element) columnElementList.get(index)).attributeValue("id").equals(columenId)) {
				return index;
			}
		}

		throw new IncorrectIndexException("在“" + sheetName + "”中无法找到列id：" + columenId);
	}

	/**
	 * 用于将列数字下标转换为英文下标，数字下标计数从0开始，即0为第一列
	 * 
	 * @param numberIndex 列数字下标
	 * @return 列英文下标
	 */
	protected String num2CharIndex(int numberIndex) {
		// 存储列文本信息
		String indexText = "";
		// 转换下标，使下标变为可计算的下标
		numberIndex += 1;

		// 循环，直至cellIndex被减为0为止
		while (numberIndex != 0) {
			// 计算当前字母的个数，用于计算幂数以及需要进行除法的次数
			int textLength = indexText.length();

			// 存储当前转换的下标，并根据textLength值计算最后可以求余的数字
			int index = (int) (numberIndex / Math.pow(26, textLength));

			// 计算数据的余数，若余数为0，则转换为26（英文字母比较特殊，其对应数字从1开始，故需要处理0）
			int remainder = (index % 26 == 0) ? 26 : index % 26;
			// 将余数转换为字母，并拼接至indexText上
			indexText = String.valueOf((char) (65 + remainder - 1)) + indexText;
			// cellIndex按照计算公式减去相应的数值
			numberIndex -= remainder * Math.pow(26, textLength);
		}

		// 返回下标的文本
		return indexText;
	}

	/**
	 * 用于将英文下标转换为数字下标，转换的数字下标从0开始，即0为第一列，英文下标允许传入小写、大写字母
	 * 
	 * @param charIndex 列英文下标
	 * @return 列数字下标
	 * @throws IncorrectIndexException 当英文下标不正确时抛出的异常
	 */
	protected int char2NumIndex(String charIndex) {
		// 判断传入的内容是否符合正则
		if (!charIndex.matches("[a-zA-Z]+")) {
			throw new IncorrectIndexException("错误的英文下标：" + charIndex);
		}

		// 将所有的字母转换为大写
		charIndex = charIndex.toUpperCase();

		// 将字符串下标转换为字符数组
		char[] indexs = charIndex.toCharArray();
		// 初始化数字下标
		int numberIndex = 0;
		// 遍历所有字符，计算相应的值
		for (int i = 0; i < indexs.length; i++) {
			// 按照“(字母对应数字) * 26 ^ (字母位下标)”的公式对计算的数字进行累加，得到对应的数字下标
			numberIndex += ((indexs[i] - 'A' + 1) * Math.pow(26, indexs.length - i - 1));
		}

		return numberIndex;
	}

	/**
	 * 用于将传入的下标转换为poi识别的下标，主要用于处理负数下标问题，以方便其他的方法的调用。
	 * 若传入的下标大于总长度，则输出总长度-1，若负数的绝对值超过总长度，则输出0
	 * 
	 * @param length 总行数或总列数
	 * @param index  传入的下标
	 * @return 能被poi识别的下标
	 */
	protected int getPoiIndex(int length, int index) {
		// 判断下标的绝对值是否大于总长度，若大于总长度，则根据index的正负来判断返回值
		if (Math.abs(index) >= length) {
			return index > 0 ? length - 1 : 0;
		}

		// 若下标绝对值小于length，则判断下标正负，正数则直接返回，负数，则通过总长度
		// 减去下标（由于下标是负数，故计算时用总长度加上下标）
		return index < 0 ? length + index : index;
	}

	/**
	 * 用于测试fieldMap中的内容，编码结束后删除
	 */
	public HashMap<String, HashMap<String, AbstractWriteExcel<T>.Field>> getCharMap() {
		return fieldMap;
	}

	/**
	 * 用于测试rank中的内容，编码结束后删除
	 */
	/*
	 * public String[] getRank() { return rank; }
	 */

	/**
	 * 用于测试caseXml文件，编码结束后删除
	 */
	public File getCaseXml() throws IOException {
		File file = new File("src/test/java/pres/auxiliary/work/testcase/用例xml文件.xml");
		FileWriter fw = new FileWriter(file);
		contentXml.write(fw);
		fw.close();

		return file;
	}

	/**
	 * <p>
	 * <b>文件名：</b>WriteTestCase.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于对测试用例进行标记，提供基本的标记方法
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年2月25日下午3:43:36
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年2月25日下午3:43:36
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	public class FieldMark {
		// 用于存储case标签元素
//		Element caseElement;
		protected String uuid;

		/**
		 * 构造类，但不允许外部进行构造
		 * 
		 * @param uuid 用例的uuid
		 */
		private FieldMark(String uuid) {
			this.uuid = uuid;
		}

		/**
		 * 向当前指向的sheet中的单元格（字段）上添加一个标记（备注），以记录内容
		 * 
		 * @param field   字段id
		 * @param content 标记中记录的内容
		 * @return 类本身
		 */
		public FieldMark fieldComment(String field, String content) {
			return fieldComment(nowSheetName, field, content);
		}

		/**
		 * 向sheet中的单元格（字段）上添加一个标记（备注），以记录内容
		 * 
		 * @param field   字段id
		 * @param content 标记中记录的内容
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public FieldMark fieldComment(String sheetName, String field, String content) {
			// 查找nowSheetName指向的sheet中的与uuid一致的单元格
			Element caseElement = getCaseElement(sheetName);

			// 判断字段是否存在，若不存在，则抛出异常
			if (!fieldMap.get(nowSheetName).containsKey(field)) {
				throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
			}

			// 向包含uuid的标签下添加相应的属性
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("id").equals(field))
					.forEach(e -> e.addAttribute("comment", content));

			return this;
		}

		/**
		 * 用于对当前指向的sheet中字段所在的单元格的背景色进行修改
		 * 
		 * @param field          字段id
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public FieldMark changeFieldBackground(String field, MarkColorsType markColorsType) {
			return changeFieldBackground(nowSheetName, field, markColorsType);
		}

		/**
		 * 用于对指定sheet中字段所在的单元格的背景色进行修改
		 * 
		 * @param field          字段id
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public FieldMark changeFieldBackground(String sheetName, String field, MarkColorsType markColorsType) {
			// 查找nowSheetName指向的sheet中的与uuid一致的单元格
			Element caseElement = getCaseElement(sheetName);

			// 判断字段是否存在，若不存在，则抛出异常
			if (!fieldMap.get(nowSheetName).containsKey(field)) {
				throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
			}

			// 向包含uuid的标签下添加相应的属性
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("id").equals(field))
					.forEach(e -> e.addAttribute("background", String.valueOf(markColorsType.getColorsValue())));

			return this;
		}

		/**
		 * 对当前指向的sheet的整行用例的背景色进行更改（标记）
		 * 
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public FieldMark changeRowBackground(MarkColorsType markColorsType) {
			return changeRowBackground(nowSheetName, markColorsType);
		}

		/**
		 * 对指定sheet的整行用例的背景色进行更改（标记）
		 * 
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public FieldMark changeRowBackground(String sheetName, MarkColorsType markColorsType) {
			// 查找nowSheetName指向的sheet中的与uuid一致的单元格
			Element caseElement = getCaseElement(sheetName);

			// 将case下所有标签的name属性传至fieldBackground方法
			((List<Element>) (caseElement.elements())).stream()
					.forEach(e -> changeFieldBackground(e.attributeValue("id"), markColorsType));

			return this;
		}

		/**
		 * 该方法用于对当前指向的sheet的整行用例文本的颜色进行标记
		 * 
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public FieldMark changeRowTextColor(MarkColorsType markColorsType) {
			return changeRowTextColor(nowSheetName, markColorsType);
		}

		/**
		 * 该方法用于对指定的sheet中整行用例文本的颜色进行标记
		 * 
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public FieldMark changeRowTextColor(String sheetName, MarkColorsType markColorsType) {
			// 查找nowSheetName指向的sheet中的与uuid一致的单元格
			Element caseElement = getCaseElement(sheetName);

			// 将case下所有标签的name属性传至fieldBackground方法
			((List<Element>) (caseElement.elements())).forEach(fieldElement -> {
				List<Element> textElements = fieldElement.elements();
				changeTextColor(fieldElement.attributeValue("id"), 0, textElements.size(), markColorsType);
			});

			return this;
		}

		/**
		 * 用于对当前指向的sheet字段的文本进行颜色标记，下标从0开始计算，若下标小于0时，则标记第一段；
		 * 若下标大于最大段落数时，则编辑最后一段。若所传字段下不存在文本标签，则不进行标记
		 * 
		 * @param field          字段id
		 * @param index          字段文本的段标（段落）
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public FieldMark changeTextColor(String field, int index, MarkColorsType markColorsType) {
			return changeTextColor(nowSheetName, field, index, index, markColorsType);
		}

		/**
		 * 用于对指定的sheet字段的文本进行颜色标记，下标从0开始计算，若下标小于0时，则标记第一段；
		 * 若下标大于最大段落数时，则编辑最后一段。若所传字段下不存在文本标签，则不进行标记
		 * 
		 * @param field          字段id
		 * @param index          字段文本的段标（段落）
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public FieldMark changeTextColor(String sheetName, String field, int index, MarkColorsType markColorsType) {
			return changeTextColor(sheetName, field, index, index, markColorsType);
		}

		/**
		 * 用于对当前指向的sheet中字段的多段文本进行颜色标记，下标从0开始计算，若下标小于0时，则标记第一段；
		 * 若下标大于最大段落数时，则编辑最后一段。若所传字段下不存在文本标签，则不进行标记。
		 * 注意，标记的段落包括开始段落，但不包括结束段落；若开始与结束的段落数相同，则标记对应的一行
		 * 
		 * @param field          字段id
		 * @param startIndex     字段文本的开始段标（段落）
		 * @param endIndex       字段文本的结束段标（段落）
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public FieldMark changeTextColor(String field, int startIndex, int endIndex, MarkColorsType markColorsType) {
			return changeTextColor(nowSheetName, field, startIndex, endIndex, markColorsType);
		}

		/**
		 * 用于对指定的sheet字段的多段文本进行颜色标记，下标从0开始计算，若下标小于0时，则标记第一段；
		 * 若下标大于最大段落数时，则编辑最后一段。若所传字段下不存在文本标签，则不进行标记。
		 * 注意，标记的段落包括开始段落，但不包括结束段落；若开始与结束的段落数相同，则标记对应的一行
		 * 
		 * @param field          字段id
		 * @param startIndex     字段文本的开始段标（段落）
		 * @param endIndex       字段文本的结束段标（段落）
		 * @param markColorsType {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public FieldMark changeTextColor(String sheetName, String field, int startIndex, int endIndex,
				MarkColorsType markColorsType) {
			// 查找nowSheetName指向的sheet中的与uuid一致的单元格
			Element caseElement = getCaseElement(sheetName);
			// 获取case下的name属性与所传参数相同的field标签
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("id").equals(field))
					.forEach(fieldElement -> {
						// 获取其下的所有field标签
						List<Element> textElements = (fieldElement.elements());
						// 判断是否存在text标签，若不存在，则结束
						if (textElements.size() != 0) {
							// 转换下标
							int changeStartIndex = getPoiIndex(textElements.size(), startIndex);
							int changeEndIndex = getPoiIndex(textElements.size(), endIndex);

							// 处理最大与最小值，保证数据不会错误
							boolean endIndexBig = changeStartIndex < changeEndIndex;
							int smallIndex = endIndexBig ? changeStartIndex : changeEndIndex;
							int bigIndex = endIndexBig ? changeEndIndex : changeStartIndex;
							// 判断最大最小值是否相同，相同则最大值+1
							bigIndex = bigIndex == smallIndex ? (bigIndex + 1) : bigIndex;

							for (int index = smallIndex; index < bigIndex; index++) {
								setTextColor(textElements, index, markColorsType);
							}
						}
					});
			return this;
		}

		/**
		 * 用于对文本标签加上颜色属性
		 * 
		 * @param textElements   字段标签下的文本标签
		 * @param index          标签的位置
		 * @param markColorsType 颜色
		 * @return 颜色是否正常进行设置，即传入的index是否正常
		 */
		private void setTextColor(List<Element> textElements, int index, MarkColorsType markColorsType) {
			// 判断传入的index参数：
			// 若参数小于0，则标记第一个标签
			// 若参数大于0且小于text最大标签数，则标记相应的标签
			// 若参数大于text最大标签数，则标记最后一个标签
			/*
			 * Element textElement; if (index < 0) { textElement = textElements.get(0); }
			 * else if (index >= 0 && index < textElements.size()) { textElement =
			 * textElements.get(index); } else { textElement =
			 * textElements.get(textElements.size() - 1); }
			 */

			Element textElement = textElements.get(getPoiIndex(textElements.size(), index));
			textElement.addAttribute("colors", String.valueOf(markColorsType.getColorsValue()));
		}

		/**
		 * 用于向当前指向的sheet下的指定字段添加超链接。超链接文本可传入excel允许的超链接形式
		 * 
		 * @param field       字段id
		 * @param linkContent 需要链接的内容
		 * @return 类本身
		 */
		public FieldMark fieldLink(String field, String linkContent) {
			return fieldLink(nowSheetName, field, linkContent);
		}

		/**
		 * 用于向指定的的sheet下的指定字段添加超链接。超链接文本可传入excel允许的超链接形式
		 * 
		 * @param field       字段id
		 * @param linkContent 需要链接的内容
		 * @return 类本身
		 */
		public FieldMark fieldLink(String sheetName, String field, String linkContent) {
			// 查找sheetName指向的sheet中的与uuid一致的单元格
			Element fieldElement = getFieldElement(getCaseElement(sheetName), field);

			fieldElement.addAttribute("link", judgelinkText(linkContent));

			return this;
		}

		/**
		 * 用于根据链接内容，返回相应的超链接类型名称，返回的形式为“超链接类型=超链接内容”
		 * 
		 * @param linkContent 标签中link属性的内容
		 * @return 超链接类型名称
		 */
		private String judgelinkText(String linkContent) {
			// 文件超链接判定规则
			String fileRegex = "(([a-zA-Z]:(\\/|\\\\))|(\\.(\\/|\\\\)))([^/:*?<>\\\"|\\\\]+(\\/|\\\\)?)+[^/:*?<>\\\"|\\\\].[^/:*?<>\\\"|\\\\]";
			// 文档内超链接判定规则
			String domRegex = "'[^\\\\\\/\\?\\*\\[\\]]{1,31}'![A-Za-z]+\\d+";

			// 根据链接内容，与相应的正则进行判断，根据结果，返回相应的HyperlinkType类
			if (RegexType.URL.judgeString(linkContent)) {
				return LinkType.URL.getName() + "=" + linkContent;
			} else if (RegexType.EMAIL.judgeString(linkContent)) {
				return LinkType.EMAIL.getName() + "=" + linkContent;
			} else if (linkContent.matches(fileRegex)) {
				return LinkType.FILE.getName() + "=" + linkContent;
			} else if (linkContent.matches(domRegex)) {
				return LinkType.DOMCUMENT.getName() + "=" + linkContent;
			} else {
				throw new IncorrectIndexException("无法识别的超链接：" + linkContent);
			}
		}

		/**
		 * 用于根据指定的sheet名称获取其下相应的case标签，并返回其元素，若查不到元素，则返回null
		 * 
		 * @param sheetName sheet名称
		 * @return 指定sheet名称下的case标签元素
		 */
		public Element getCaseElement(String sheetName) {
			/*
			 * if (!writeSheetNameList.contains(sheetName)) { return null; }
			 */
			return ((Element) (contentXml
					.selectSingleNode("//sheet[@name='" + sheetName + "']/case[@id='" + uuid + "']")));
		}

		/**
		 * 用于获取在指定的case标签元素中的字段元素
		 * 
		 * @param caseElement case标签元素
		 * @param field       case标签下的字段id
		 * @return
		 */
		private Element getFieldElement(Element caseElement, String field) {
			for (Object fieldElement : caseElement.elements("field")) {
				if (((Element) fieldElement).attributeValue("id").equals(field)) {
					return (Element) fieldElement;
				}
			}

			throw new IncorrectIndexException(
					"当前sheet（" + caseElement.getParent().attributeValue("name") + "）中不存在字段：" + field);
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>WriteTestCase.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于对配置文件中字段信息的存储
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年2月27日下午6:58:08
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年4月3日 16:49:08
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	public class Field {
		/**
		 * 用于存储字段在配置文件中的id
		 */
		public String id;
		/**
		 * 用于存储字段在配置文件中的name
		 */
		public String name;
		/**
		 * 用于存储字段对应单元格内文本的水平对齐方式
		 */
		public String align;
		/**
		 * 用于存储字段所在单元格的位置（xml文件中标签的位置）
		 */
		public int index;
		/**
		 * 用于标记每行写入的段落数，默认为0，当数字小于1时，则认为不分行
		 */
		public int rowText = 0;
		/**
		 * 用于标记是否对字段进行编号
		 */
		public boolean serialNumber = false;

		/**
		 * 用于存储字段在用例中对应的内容
		 */
		public ArrayList<String> content = new ArrayList<>();

		/**
		 * 存储字段是否包含数据有效性（由于在创建模板时数据有效性已被写入到模板文件中，故此处无需存储）
		 */
		public ArrayList<String> datas = new ArrayList<>();

		/**
		 * 用于构造Field
		 * 
		 * @param id         字段在配置文件中的id
		 * @param name       字段在配置文件中的name
		 * @param align      字段在配置文件中的水平对齐方式
		 * @param index      字段在单元格中的位置
		 * @param rowText    换行段落数
		 * @param datas      字段是否存在数据有效性
		 * @param numberSign 字段是否需要编号
		 */
		public Field(String id, String name, String align, int index, String rowText, ArrayList<String> datas,
				boolean numberSign) {
			this.id = id;
			this.name = name;
			this.align = align;
			this.index = index;
			this.datas = datas;
			this.rowText = rowText == null ? -1 : Integer.valueOf(rowText);
			this.serialNumber = numberSign;
		}

		/**
		 * 用于清空content中的内容
		 */
		public void clearContent() {
			content.clear();
		}

		/**
		 * 根据字段单元格的对齐方式，来创建XSSFCellStyle对象，如样式
		 * 属性值不是center、right和left中的一项，则设置样式为左对齐（left）
		 * 
		 * @return XSSFCellStyle对象
		 */
		public XSSFCellStyle getCellStyle() {
			// 创建样式
			XSSFCellStyle xcs = xw.createCellStyle();
			// 设置单元格水平对其方式
			switch (align) {
			case "center":
				xcs.setAlignment(HorizontalAlignment.CENTER);
				break;
			case "right":
				xcs.setAlignment(HorizontalAlignment.RIGHT);
				break;
			case "left":
			default:
				xcs.setAlignment(HorizontalAlignment.LEFT);
			}

			// 设置单元格垂直居中
			xcs.setVerticalAlignment(VerticalAlignment.CENTER);
			// 设置单元格自动换行
			xcs.setWrapText(true);

			return xcs;
		}

		/**
		 * 用于返回当前字段相应需要填入表格中的数据（数据有效性中的内容），该方法只能接收能转换为数字的字符串，
		 * 若字符串无法转换为数字，则返回原串；若可以转换，则将其转换为数字后，通过方法获取数据有效性中的数据。 存在以下三种情况：
		 * <ol>
		 * <li>当转换的数值小于0时，则获取数据有效性中的第一个数据</li>
		 * <li>当转换的数值大于数据有效性集合内容的最大数值时，则获取数据有效性中的最后一个数据</li>
		 * <li>当转换的数值大于0小于数据有效性集合内容的最大数值时，则获取对应的数据</li>
		 * </ol>
		 * <b>注意：</b>数据有效性的为与优先级接轨，故下标将从1开始，即传入1时表示获取第1个元素，而不是第2个
		 * 
		 * @param index 下标
		 * @return 数据有效性中对应的数据，无法转换则返回传入的字符串
		 */
		public String getDataValidation(int index) {
			// 若数据有效性字段内容为空，则直接返回传入的值
			if (datas.size() == 0) {
				return "";
			}

			// 若存在数据有效性，则将传入的数值字符串进行转换，若无法转换为数字，则直接返回所填字段
			index = index - 1;
			// 再次判断转换的数字是否符合要求，若小于0，则返回第一个数据，若大于集合长度，则返回最后一个数据
			if (index < 0) {
				return datas.get(0);
			} else if (index >= datas.size()) {
				return datas.get(datas.size() - 1);
			} else {
				return datas.get(index);
			}
		}

		/**
		 * 用于对数据有效性的数据进行模糊匹配，并返回匹配到的数据有效性
		 * 
		 * @param keys 需要进行匹配的关键词
		 * @return 被匹配到的数据
		 */
		public ArrayList<String> matchDataValidation(String... keys) {
			return (ArrayList<String>) datas.stream().filter(data -> {
				// 遍历关键词组，若所有的词语通过判断（即filter筛选后所有的词语都通过），则表示该数据通过模糊匹配
				return Arrays.stream(keys).filter(key -> data.indexOf(key) > -1).collect(Collectors.toList())
						.size() == keys.length;
			}).collect(Collectors.toList());
		}

		/**
		 * 用于创建表格的数据有效性，若当前字段无数据有效性或文件中无数据有效性 相应的内容时，则不创建数据有效性
		 * 
		 * @param caseSheet     用例所在sheet
		 * @param startRowIndex 起始行下标
		 * @param endRowIndex   结束行下标
		 */
		public void addDataValidation(XSSFSheet caseSheet, int startRowIndex, int endRowIndex) {
			// 判断当前是否存在数据有效性，不存在，则结束
			if (datas.size() == 0) {
				return;
			}

			// 判断数据有效性sheet页是否存在，若不存在，则结束
			XSSFSheet dataSheet;
			if ((dataSheet = xw.getSheet(CreateExcelFile.DATA_SHEET_NAME)) == null) {
				return;
			}

			// 获取当前sheet的第一行
			XSSFRow xr = dataSheet.getRow(0);
			// 若第一行未创建，则结束
			if (xr == null) {
				return;
			}

			// 拼接当前数据在数据有效性页中的名称（sheetName来自外层类）
			String dataCellTitle = nowSheetName + CreateExcelFile.SIGN + id;
			// 遍历第一行相应的单元格，查看是否存在与sheetName相同的
			int cellindex = 0;
			for (; cellindex < xr.getLastCellNum(); cellindex++) {
				if (xr.getCell(cellindex).getStringCellValue().equals(dataCellTitle)) {
					break;
				}

				// 若index达到最大单元格数，但仍无法得到与之相同的名称，则结束运行
				if (cellindex == xr.getLastCellNum() - 1) {
					return;
				}
			}

			// 获取当前有效性的最后一个元素的行号
			int rowNum = dataSheet.getLastRowNum();
			// 循环，判断该行是否能获取到数据，如果能，则证明需求从该行获取结束
			while (dataSheet.getRow(rowNum).getCell(cellindex) == null) {
				rowNum--;
			}

			// 数据有效性公式约束拼接
			String dataConstraint = "=" + CreateExcelFile.DATA_SHEET_NAME + "!$" + ((char) (65 + cellindex)) + "$2:$"
					+ ((char) (65 + cellindex)) + "$" + String.valueOf(rowNum + 1);

			// 创建公式约束
			DataValidationConstraint constraint = new XSSFDataValidationHelper(caseSheet)
					.createFormulaListConstraint(dataConstraint);

			// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
			CellRangeAddressList regions = new CellRangeAddressList(startRowIndex, endRowIndex, this.index, this.index);

			// 数据有效性对象
			DataValidation d = new XSSFDataValidationHelper(caseSheet).createValidation(constraint, regions);
			caseSheet.addValidationData(d);
		}

		/**
		 * 根据下标返回字段中存储的内容，下标允许传入负数，表示从后向前遍历
		 * 
		 * @param index 下标
		 * @return 下标对应的字段内容
		 */
		public String getContent(int index) {
			return content.get(getPoiIndex(content.size(), index));
		}
	}
}
