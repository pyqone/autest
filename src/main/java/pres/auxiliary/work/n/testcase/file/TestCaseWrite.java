package pres.auxiliary.work.n.testcase.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Comment;
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
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.tool.readfile.ListFileRead;
import pres.auxiliary.work.n.testcase.templet.Case;
import pres.auxiliary.work.n.testcase.templet.LabelNotFoundException;

/**
 * <p>
 * <b>文件名：</b>WriteTestCase.java
 * </p>
 * <p>
 * <b>用途：</b>用于向测试用例文件中添加用例，该工具类支持词语替换方法，但需要使用“#”符号对词语进行标记
 * </p>
 * <p>
 * <b>编码时间：</b>2020年2月17日下午9:36:00
 * </p>
 * <p>
 * <b>修改时间：</b>2020年3月22日 下午10:29:37
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class TestCaseWrite {
	/**
	 * 用于指向用例的XSSFWorkbook对象
	 */
	XSSFWorkbook xw;

	/**
	 * 用于对待替换词语的标记
	 */
	private final String WORD_SIGN = "\\#";

	/**
	 * 用于存储一条用例的信息，第一个参数指向配置文件中的字段id，第二个字段为xml文件中字段的相应信息
	 */
	HashMap<String, Field> fieldMap = new HashMap<>(16);

	/**
	 * 用于存储所有用例均使用的字段常值
	 */
	private HashMap<String, String> caseValueMap = new HashMap<>(16);

	/**
	 * 用于存储待替换的词语以及被替换的词语
	 */
	private HashMap<String, String> replaceWordMap = new HashMap<>(16);

	/**
	 * 用于存储与测试用例生成类关联的字段，参数1为用例文件中的字段，参数2为测试用例生成方法中的字段
	 */
	private HashMap<String, String> relevanceMap = new HashMap<>(16);

	/**
	 * 用于存储当前对应的sheet名称
	 */
	private String sheetName = "";

	/**
	 * 指向用例文件
	 */
	private File caseFile;

	/**
	 * 指向配置文件的Document对象
	 */
	private Document configXml;

	/**
	 * 指向存储测试用例的Document类，该类将不创建xml文件，但最终用于将doucument中的内容写入excel中
	 */
	private Document caseXml;

	/**
	 * 通过测试文件模板xml配置文件和测试用例文件来构造WriteTestCase类。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public TestCaseWrite(File configFile, File caseFile) {
		// 判断传入的configurationFile是否为一个文件类对象，若非文件类对象，则抛出异常
		try {
			configXml = new SAXReader().read(configFile);
		} catch (DocumentException e) {
			throw new IncorrectFileException("用例xml文件有误");
		}
		
		// 判断路径是否包含“.xlsx”（使用的方法为XSSFWork）
		if (caseFile.isFile() && caseFile.getAbsolutePath().indexOf(".xlsx") > -1) {
			this.caseFile = caseFile;
		} else {
			throw new IncorrectFileException("不正确的文件格式：" + caseFile.getAbsolutePath());
		}

		// 获取xml文件中的第一个sheet标签，则将该标签的name属性内容传入getColumnId中
		switchSheet(configXml.getRootElement().element("sheet").attributeValue("name"));

		// 创建存储测试用例的document类对象
		caseXml = DocumentHelper.createDocument();
		// 写入根节点
		caseXml.addElement("cases");
	}

	/**
	 * 该方法用于切换sheet，以在另一个sheet中添加信息
	 * 
	 * @param sheetName xml配置文件中sheet的name字段内容
	 */
	public void switchSheet(String sheetName) {
		// 每次切换sheet时，要重新获取一次sheet下的字段id。
		// 切换sheet后需要清空预设字段中的内容
		// 重新获取id，包括清空fieldMap
		getColumnId(sheetName);
		// 清空常值map
		caseValueMap.clear();
		// 清空标记map
//		fieldMarkMap.clear();
		// 清空预设字段枚举值
		Arrays.stream(FieldType.values()).forEach(e -> e.setValue(""));

		// 将相应的sheet标签的name属性存储至sheetName中
		this.sheetName = sheetName;
	}


	/**
	 * 设置字段名称的常值，通过该设置，则之后该字段将直接填入设置的值，无需再次写入字段的值
	 * 
	 * @param field   字段id
	 * @param content 相应字段的内容
	 * 
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public void setFieldValue(String field, String content) {
		// 为保证在写用例的时候也能生效，故将值设置进入fieldMap
		addContent(field, content);

		// 先将值设置入fieldMap中可以保证field字段是存在于fieldMap中，以减少此处再做判断
		// 将字段内容写入caseValueMap
		caseValueMap.put(field, content);
	}

	/**
	 * 用于设置需要被替换的词语。添加词语时无需添加特殊字符
	 * 
	 * @param word        需要替换的词语
	 * @param replactWord 被替换的词语
	 */
	public void setReplactWord(String word, String replactWord) {
		word = WORD_SIGN + word + WORD_SIGN;
		replaceWordMap.put(word, replactWord);
	}

	/**
	 * 用于将测试用例文件模板中的字段名与测试用例生成类（继承自{@link Case}的测试用例生成类）中
	 * 的字段进行关联，通过该方法设置关联字段后，可将生成的测试用例写入到测试用例文件中
	 * 
	 * @param field     测试用例文件字段
	 * @param caseLabel 测试用例生成方法的字段
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public void relevanceCase(String field, String caseLabel) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}

		// 添加字段
		relevanceMap.put(field, caseLabel);
	}


	/**
	 * 通过传入的字段id，将对应的字段内容写入到用例最后的段落中，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#<br>
	 * 添加数据时，其亦可对存在数据有效性的数据进行转换，在传值时，只需要传入相应的字段值即可，例如：<br>
	 * 当字段存在两个数据有效性：“测试1”和“测试2”时，则，可传入addContent(..., "1")（注意，下标从1开始），
	 * 此时，文件中该字段的值将为“测试1”，若传入的值无法转换成数字，则直接填入传入的内容。具体说明可以参见{@link Field#getDataValidation(String)}
	 * 
	 * @param field   字段id
	 * @param contents 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public TestCaseWrite addContent(String field, String... contents) {
		return insertContent(field, fieldMap.get(field).content.size(), contents);
	}
	
	/**
	 * 用于对已存储的内容进行移除，若传入的下标超出当前的内容的段落数时，则不做处理
	 * @param field 字段id
	 * @param indexs 需要删除的段落下标
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public TestCaseWrite removeContent(String field, int...indexs) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}
		
		//移除相关的内容，若输入的序号不存在，则不进行
		Arrays.stream(indexs).forEach(index -> {
			try {
				fieldMap.get(field).content.remove(index);
			} catch (Exception e) {
			}
		});
		
		return this;
	}
	
	/**
	 * 通过传入的字段id，将对应的字段内容写入到用例文本的相应段落中，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#<br>
	 * 添加数据时，其亦可对存在数据有效性的数据进行转换，在传值时，只需要传入相应的字段值即可，例如：<br>
	 * 当字段存在两个数据有效性：“测试1”和“测试2”时，则，可传入insertContent(..., "1")（注意，下标从1开始），
	 * 此时，文件中该字段的值将为“测试1”，若传入的值无法转换成数字，则直接填入传入的内容。具体说明可以参见{@link Field#getDataValidation(String)}
	 * 
	 * @param field   字段id
	 * @param index 需要插入的段落
	 * @param contents 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public TestCaseWrite insertContent(String field, int index, String... contents) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}
		
		//若未传值或传入null，则直接结束
		if (contents == null || contents.length == 0) {
			return this;
		}
		
		//若传入的下标大于相应的最大段落数时，则直接结束
		if (index > fieldMap.get(field).content.size()) {
			return this;
		}
		
		if (fieldMap.get(field).datas.size() != 0) {
			//查找数据有效性，若当前字段存在数据有效性，则将数据有效性转义，若添加的字段无法转义，则存储原内容
			contents = dataValidityChange(contents, fieldMap.get(field));
		}
		// 查找特殊词语，并对词语进行替换
		contents = replaceWord(contents);
		
		// 将字段内容写入fieldMap，若插入的下标不正确，则不做任何处理
		try {
			fieldMap.get(field).content.addAll(index, Arrays.asList(contents));
		} catch (Exception e) {
		}
		
		return this;
	}
	
	/**
	 * 通过传入的字段id，将对应的字段相应段落的内容替换成传入的文本或文本组，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#
	 * @param field   字段id
	 * @param index 需要插入的段落
	 * @param contents 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public TestCaseWrite replaceContent(String field, int index, String... contents) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}
		
		//若未传值或传入null，则直接结束
		if (contents == null || contents.length == 0) {
			return this;
		}
		
		//若传入的下标大于相应的最大段落数时，则直接结束
		if (index >= fieldMap.get(field).content.size()) {
			return this;
		}
		
		//移除相应的段落
		removeContent(field, index);
		//在原位置上插入相应的内容
		insertContent(field, index, contents);
		
		return this;
	}
	
	/**
	 * 用于清空字段中存储的内容
	 * @param field   字段id
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public TestCaseWrite clearContent(String field) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}
		
		fieldMap.get(field).content.clear();
		
		return this;
	}

	/**
	 * 用于将生成测试用例方法（继承自{@link Case}类的方法）所成成的测试用例添加到测试用例文件中
	 * 
	 * @param testCase 测试用例生成方法
	 * @return 类本身
	 */
	public TestCaseWrite addCase(Case testCase) {
		// 获取用例内容
		HashMap<String, ArrayList<String>> labelMap = testCase.getFieldTextMap();

		// 遍历relevanceMap，将用例字段内容写入到xml中
		relevanceMap.forEach((field, label) -> {
			addContent(field, labelMap.get(label).toArray(new String[] {}));
		});

		return this;
	}

	/**
	 * 用于结束一条用例写作的标志，调用该方法后将结束一条用例的编写， 该方法以下的添加用例内容的方法均为编辑另一条用例
	 */
	@SuppressWarnings("unchecked")
	public CaseMark end() {
		// 写入到caseXml中
		// 获取所有的sheet标签， 并筛选出name属性为sheetName的标签
		List<Element> sheetList = ((List<Element>) (caseXml.getRootElement().elements("sheet"))).stream()
				.filter(e -> e.attributeValue("name").equals(sheetName)).collect(Collectors.toList());
		Element sheetElement = null;
		// 判断是否存在sheet标签，若存在，则直接获筛选后的第一个数据，若不存在，则创建
		if (sheetList.size() != 0) {
			sheetElement = sheetList.get(0);
		} else {
			sheetElement = caseXml.getRootElement().addElement("sheet").addAttribute("name", sheetName);
		}
		
		// 创建case标签
		String caseUuid = UUID.randomUUID().toString();
		Element caseElement = sheetElement.addElement("case").addAttribute("id", caseUuid);
		// 为fieldMap中的所有key创建field标签，并记录相应的value
		fieldMap.forEach((id, field) -> {
			//判断字段是否需要进行编号，若需要编号，则调用编号方法
			if (field.serialNumber) {
				addSerialNumber(field);
			}
			
			// 添加field标签，并设置name属性（字段名称），mark属性（备注内容）
			// dom4j当属性值传入null时，则直接不会创建该属性，故此处无需做判断字段id是否在fieldMarkMap中
			Element fieldElement = caseElement.addElement("field").addAttribute("name", id);
			// 判断当前是否有添加内容，若未添加内容，则创建一个value属性为空的text标签
			if (field.content != null) {
				// 读取所有texts（字符串数组）的所有内容，并为每一元素创建一个text标签，将值加入属性中
				field.content.forEach(text -> {
					fieldElement.addElement("text").addAttribute("value", text);
				});
			} else {
				fieldElement.addElement("text").addAttribute("value", "");
			}
		});

		// 清空fieldMap中的内容
		clearFieldContent();
		// 将字段常值设置入fieldMap中，若抛出异常，则不进行处理
		if (caseValueMap != null && caseValueMap.size() != 0) {
			caseValueMap.forEach((field, content) -> {
				try {
					addContent(field, content);
				} catch (LabelNotFoundException e) {
				}
			});
		}

		return new CaseMark(caseUuid);
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
		FileInputStream fip = new FileInputStream(caseFile);
		// 通过输入流，使XSSFWorkbook对象指向模版文件
		xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 读取caseXml中存储的所有sheet标签
		List<Element> sheetElements = caseXml.getRootElement().elements("sheet");
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
				writeCase(xs.getLastRowNum() + 1, xs, caseElement);
			}
		}

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(caseFile);
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
	private void writeCase(int index, XSSFSheet xs, Element caseElement) {
		// 获取字段元素，需要获取配置xml文件中的以及用例xml文件中的字段
		List<Element> fieldElements = caseElement.elements("field");
		// 遍历所有的field标签，将标签的内容写入到文件中
		for (Element fieldElement : fieldElements) {
			// 获取相应的Field对象
			String fieldId = fieldElement.attributeValue("name");
			// 获取字段的field对象
			Field field = fieldMap.get(fieldId);
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
	private void clearFieldContent() {
		fieldMap.forEach((key, value) -> value.clearContent());
	}

	/**
	 * 该方法用于获取相应sheet下column字段的id内容，存储至charMap中
	 * 
	 * @param sheetName sheet的name属性
	 */
	@SuppressWarnings("unchecked")
	private void getColumnId(String sheetName) {
		// 清空fieldMap中的内容
		fieldMap.clear();

		// 获取相应的sheet标签元素
		Element sheetElement = (Element) (configXml.selectSingleNode("//sheet[@name='" + sheetName + "']"));
		// 获取相应的sheet标签下的column标签
		List<Element> column = sheetElement.elements("column");

		// 初始化fieldMap中的字段及其值
		for (int index = 0; index < column.size(); index++) {
			// 查找xml文件中数据有效性标签
			List<Element> datasList = sheetElement.elements("datas");
			ArrayList<String> datas = new ArrayList<String>();
			// 遍历所有的数据有效性标签，若存在，则存储相应的数据有效性
			for (Element datasElemenet : datasList) {
				if (datasElemenet.attributeValue("id").equals(column.get(index).attributeValue("id"))) {
					//存储当前的数据有效性的内容
					((List<Element>)(datasElemenet.elements())).forEach(textElement -> {
						//判断数据有效性的存放位置，并按照相应的方法读取数据有效性
						if ("file".equalsIgnoreCase(textElement.getName())) {
							datas.addAll(getFileDataValidity(textElement));
						} else {
							datas.add(getLabelDataValidity(textElement));
						}
					});
					break;
				}
			}
			
			// 存储字段信息
			fieldMap.put(column.get(index).attributeValue("id"),
					new Field(column.get(index).attributeValue("id"), column.get(index).attributeValue("align"), index,
							column.get(index).attributeValue("row_text"), datas,
							Boolean.valueOf(column.get(index).attributeValue("index"))));
		}
	}
	
	/**
	 * 用于获取写在xml文件中的数据有效性
	 * @param textElement 数据标签
	 * @return 标签内的数据
	 */
	private String getLabelDataValidity(Element textElement) {
		return textElement.attributeValue("name");
	}
	
	/**
	 * 用于获取写在外部文件中的数据有效性
	 * @param textElement 数据标签
	 * @return 从外部文件中读取到的数据
	 */
	private ArrayList<String> getFileDataValidity(Element textElement) {
		ArrayList<String> dataList = new ArrayList<>();
		try {
			//读取数据有效性文件内容
			File dataFile = new File(textElement.attributeValue("path"));
			String sheet = textElement.attributeValue("regex");
			ListFileRead lfr = new ListFileRead(dataFile, sheet);
			
			//获取需要读取的列信息，若未指定列信息，则默认读取第一列
			int columnIndex = textElement.attributeValue("column") == null ? 0 : Integer.valueOf(textElement.attributeValue("column"));
			//读取起始行信息，若未指定起始行信息，则默认读取第一行
			int startRowIndex = textElement.attributeValue("start_row") == null ? 0 : Integer.valueOf(textElement.attributeValue("start_row"));
			//读取结束行信息，若未指定起始行信息，则默认读取最后一行
			int endRowIndex = textElement.attributeValue("end_row") == null ? lfr.getCoulumnSize(columnIndex) : Integer.valueOf(textElement.attributeValue("end_row"));
			//获取数据信息
			dataList.addAll(lfr.getColumn(columnIndex, startRowIndex, endRowIndex));
		} catch (Exception e) {
			//若抛出任何异常，则说明xml配置不正确，故不进行操作
		}
		
		//去除空行
		for (int i = 0; i < dataList.size(); i++) {
			if ("".equals(dataList.get(i))) {
				dataList.remove(i);
				i--;
			}
		}
		
		return dataList;
	}
	
	/**
	 * 用于对需要进行替换的特殊词语进行替换
	 * @param contents 文本内容
	 */
	private String[] replaceWord(String[] contents) {
		// 查找特殊词语，并对词语进行替换
		for (String word : replaceWordMap.keySet()) {
			// 查找所有的内容，并将特殊词语进行替换
			for (int i = 0; i < contents.length; i++) {
				// 若需要添加序号，则先获取当前列表中的字段个数，以便于继续编号
				contents[i] = contents[i].replaceAll(word, replaceWordMap.get(word));
			}
		}
		
		return contents;
	}
	
	/**
	 * 将数据转化为相应的数据有效性中的内容，当不存在数据有效性或传入的数据无法转换时，则返回原串
	 * @param contents 文本内容
	 * @param field 字段相应的对象
	 * @return 转换后的文本内容
	 */
	private String[] dataValidityChange(String[] contents, Field field) {
		//判断字段是否有数据有效性，若没有，则直接返回原串
		if (field.datas.size() == 0) {
			return contents;
		}
		
		// 查找所有的内容，并将特殊词语进行替换
		for (int i = 0; i < contents.length; i++) {
			// 若需要添加序号，则先获取当前列表中的字段个数，以便于继续编号
			contents[i] = field.getDataValidation(contents[i]);
		}
		
		return contents;
	}
	
	/**
	 * 对段落的文本进行编号
	 * @param field 段落类对象
	 */
	private void addSerialNumber(Field field) {
		for (int index = 0; index < field.content.size(); index++) {
			field.content.set(index, String.valueOf(index + 1) + "." + field.content.get(index));
		}
	}
	
	/**
	 * 用于测试fieldMap中的内容，编码结束后删除
	 */
	/*
	 * public HashMap<String, String> getCharMap() { return fieldMap; }
	 */

	/**
	 * 用于测试rank中的内容，编码结束后删除
	 */
	/*
	 * public String[] getRank() { return rank; }
	 */

	/**
	 * 用于测试caseXml文件，编码结束后删除
	 */
//	public File getCaseXml() throws IOException {
//		File file = new File("src/test/java/pres/auxiliary/work/testcase/用例xml文件.xml");
//		FileWriter fw = new FileWriter(file);
//		caseXml.write(fw);
//		fw.close();
//
//		return file;
//	}

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
	public class CaseMark {
		// 用于存储case标签元素
		Element caseElement;

		/**
		 * 构造类，但不允许外部进行构造
		 * 
		 * @param uuid 用例的uuid
		 */
		private CaseMark(String uuid) {
			caseElement = ((Element) (caseXml.selectSingleNode("//*[@id='" + uuid + "']")));
		}

		/**
		 * 向单元格（字段）上添加一个标记（备注），以记录内容
		 * 
		 * @param field   字段id
		 * @param content 标记中记录的内容
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public CaseMark fieldComment(String field, String content) {
			// 判断字段是否存在，若不存在，则抛出异常
			if (!fieldMap.containsKey(field)) {
				throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
			}

			// 向包含uuid的标签下添加相应的属性
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("name").equals(field))
					.forEach(e -> e.addAttribute("comment", content));

			return this;
		}

		/**
		 * 用于对字段所在的单元格的背景色进行修改
		 * 
		 * @param field  字段id
		 * @param colors {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public CaseMark changeFieldBackground(String field, MarkColorsType color) {
			// 判断字段是否存在，若不存在，则抛出异常
			if (!fieldMap.containsKey(field)) {
				throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
			}

			// 向包含uuid的标签下添加相应的属性
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("name").equals(field))
					.forEach(e -> e.addAttribute("background", String.valueOf(color.getColorsValue())));

			return this;
		}

		/**
		 * 对整行用例的背景色进行更改（标记）
		 * 
		 * @param colors {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public CaseMark changeRowBackground(MarkColorsType color) {
			// 将case下所有标签的name属性传至fieldBackground方法
			((List<Element>) (caseElement.elements())).stream()
					.forEach(e -> changeFieldBackground(e.attributeValue("name"), color));

			return this;
		}

		/**
		 * 该方法用于对整行用例文本的颜色进行标记
		 * 
		 * @param color {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public CaseMark changeRowTextColor(MarkColorsType color) {
			// 将case下所有标签的name属性传至fieldBackground方法
			((List<Element>) (caseElement.elements())).forEach(fieldElement -> {
				List<Element> textElements = fieldElement.elements();
				changeTextColor(fieldElement.attributeValue("name"), 0, textElements.size(), color);
			});

			return this;
		}

		/**
		 * 用于对字段的文本进行颜色标记，下标从0开始计算，若下标小于0时，则标记第一段；
		 * 若下标大于最大段落数时，则编辑最后一段。若所传字段下不存在文本标签，则不进行标记
		 * 
		 * @param field  字段id
		 * @param index  字段文本的段标（段落）
		 * @param colors {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public CaseMark changeTextColor(String field, int index, MarkColorsType color) {
			return changeTextColor(field, index, index, color);
		}

		/**
		 * 用于对字段的多段文本进行颜色标记，下标从0开始计算，若下标小于0时，则标记第一段；
		 * 若下标大于最大段落数时，则编辑最后一段。若所传字段下不存在文本标签，则不进行标记。
		 * 注意，标记的段落包括开始段落，但不包括结束段落；若开始与结束的段落数相同，则标记对应的一行
		 * 
		 * @param field      字段id
		 * @param startIndex 字段文本的开始段标（段落）
		 * @param endIndex   字段文本的结束段标（段落）
		 * @param color      {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public CaseMark changeTextColor(String field, int startIndex, int endIndex, MarkColorsType color) {
			// 获取case下的name属性与所传参数相同的field标签
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("name").equals(field))
					.forEach(fieldElement -> {
						// 获取其下的所有field标签
						List<Element> textElements = (List<Element>) (fieldElement.elements());
						// 判断是否存在text标签，若不存在，则结束
						if (textElements.size() != 0) {
							// 处理最大与最小值，保证数据不会错误
							boolean endIndexBig = startIndex < endIndex;
							int smallIndex = endIndexBig ? startIndex : endIndex;
							int bigIndex = endIndexBig ? endIndex : startIndex;
							// 判断最大最小值是否相同，相同则最大值+1
							bigIndex = bigIndex == smallIndex ? (bigIndex + 1) : bigIndex;

							for (int index = smallIndex; index < bigIndex; index++) {
								setTextColor(textElements, index, color);
							}
						}
					});
			return this;
		}

		/**
		 * 用于对文本标签加上颜色属性
		 * 
		 * @param textElements 字段标签下的文本标签
		 * @param index        标签的位置
		 * @param color        颜色
		 * @return 颜色是否正常进行设置，即传入的index是否正常
		 */
		private void setTextColor(List<Element> textElements, int index, MarkColorsType color) {
			// 判断传入的index参数：
			// 若参数小于0，则标记第一个标签
			// 若参数大于0且小于text最大标签数，则标记相应的标签
			// 若参数大于text最大标签数，则标记最后一个标签
			Element textElement;
			if (index < 0) {
				textElement = textElements.get(0);
			} else if (index >= 0 && index < textElements.size()) {
				textElement = textElements.get(index);
			} else {
				textElement = textElements.get(textElements.size() - 1);
			}
			textElement.addAttribute("colors", String.valueOf(color.getColorsValue()));
		}

		/**
		 * 用于对步骤和预期同时进行标记，使用该方法前需要调用{@link TestCaseWrite#setPresupposeField(FieldType, String)}
		 * 方法对字段的步骤（{@link FieldType#STEP}枚举值）和预期（{@link FieldType#EXPECT}枚举值）进行标记。
		 * 若步骤和预期中一项未添加时，则只标记存在的文本；若均不存在，则不进行标记。下标从0开始计算，若下标小于0时，
		 * 则标记第一段；若下标大于最大段落数时，则编辑最后一段。
		 * 
		 * @param stepIndex 步骤或预期下标 colors {@link MarkColorsType}类枚举
		 * @param colors    {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		public CaseMark markStepAndExcept(int stepIndex, MarkColorsType colors) {
			// 标记步骤
			changeTextColor(FieldType.STEP.getValue(), stepIndex, colors);
			// 标记预期
			changeTextColor(FieldType.EXPECT.getValue(), stepIndex, colors);

			return this;
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
	class Field {
		/**
		 * 用于存储字段在配置文件中的id
		 */
		public String id;
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
		 * @param id    字段id
		 * @param align 单元格对齐方式
		 * @param index 字段在单元格中的位置
		 * @param datas 字段是否存在数据有效性
		 */
		/**
		 * 用于构造Field
		 * 
		 * @param id         字段id
		 * @param align      单元格对齐方式
		 * @param index      字段在单元格中的位置
		 * @param rowText    换行段落数
		 * @param datas      字段是否存在数据有效性
		 * @param numberSign 字段是否需要编号
		 */
		public Field(String id, String align, int index, String rowText, ArrayList<String> datas, boolean numberSign) {
			this.id = id;
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
		 * @param xw XSSFWorkbook对象
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
		 * 若字符串无法转换为数字，则返回原串；若可以转换，则将其转换为数字后，通过方法获取数据有效性中的数据。
		 * 存在以下三种情况：
		 * <ol> 
		 * 	<li>当转换的数值小于0时，则获取数据有效性中的第一个数据</li>
		 * 	<li>当转换的数值大于数据有效性集合内容的最大数值时，则获取数据有效性中的最后一个数据</li>
		 * 	<li>当转换的数值大于0小于数据有效性集合内容的最大数值时，则获取对应的数据</li>
		 * </ol>
		 * <b>注意：</b>数据有效性的为与优先级接轨，故下标将从1开始，即传入1时表示获取第1个元素，而不是第2个
		 * @param indexText 下标字符串
		 * @return 数据有效性中对应的数据，无法转换则返回传入的字符串
		 */
		public String getDataValidation(String indexText) {
			//若数据有效性字段内容为空，则直接返回传入的值
			if (datas.size() == 0) {
				return indexText;
			}
			
			//若存在数据有效性，则将传入的数值字符串进行转换，若无法转换为数字，则直接返回所填字段
			try {
				int index = Integer.valueOf(indexText) - 1;
				//再次判断转换的数字是否符合要求，若小于0，则返回第一个数据，若大于集合长度，则返回最后一个数据
				if (index < 0) {
					return datas.get(0);
				} else if (index > datas.size()) {
					return datas.get(datas.size() - 1);
				} else {
					return datas.get(index);
				}
			} catch (NumberFormatException e) {
				return indexText;
			}
		}
		
		/**
		 * 用于对数据有效性的数据进行模糊匹配，并返回匹配到的数据有效性
		 * @param keys 需要进行匹配的关键词
		 * @return 被匹配到的数据
		 */
		public ArrayList<String> matchDataValidation(String... keys) {
			return (ArrayList<String>) datas.stream().filter(data -> {
				//遍历关键词组，若所有的词语通过判断（即filter筛选后所有的词语都通过），则表示该数据通过模糊匹配
				return Arrays.stream(keys).filter(key -> data.indexOf(key) > -1)
						.collect(Collectors.toList()).size() == keys.length;
			}).collect(Collectors.toList());
		}
		
		/**
		 * 用于创建表格的数据有效性，若当前字段无数据有效性或文件中无数据有效性 相应的内容时，则不创建数据有效性
		 * @param caseSheet 用例所在sheet
		 * @param startRowIndex 起始行下标
		 * @param endRowIndex 结束行下标
		 */
		public void addDataValidation(XSSFSheet caseSheet, int startRowIndex, int endRowIndex) {
			// 判断当前是否存在数据有效性，不存在，则结束
			if (datas.size() == 0) {
				return;
			}

			// 判断数据有效性sheet页是否存在，若不存在，则结束
			XSSFSheet dataSheet;
			if ((dataSheet = xw.getSheet(TestCaseTemplet.DATA_SHEET_NAME)) == null) {
				return;
			}

			// 获取当前sheet的第一行
			XSSFRow xr = dataSheet.getRow(0);
			// 若第一行未创建，则结束
			if (xr == null) {
				return;
			}

			// 拼接当前数据在数据有效性页中的名称（sheetName来自外层类）
			String dataCellTitle = sheetName + TestCaseTemplet.SIGN + id;
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
			String dataConstraint = "=" + TestCaseTemplet.DATA_SHEET_NAME + "!$" + ((char) (65 + cellindex)) + "$2:$"
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
	}
}
