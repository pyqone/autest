package pres.auxiliary.work.n.tcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
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
 * <b>修改时间：</b>2020年2月17日下午9:36:00
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class WriteTestCase {
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
//	private HashMap<String, String[]> fieldMap = new HashMap<>(16);
	private HashMap<String, Field> fieldMap = new HashMap<>(16);

	/**
	 * 用于存储所有用例均使用的字段常值
	 */
	private HashMap<String, String> caseValueMap = new HashMap<>(16);

	/**
	 * 用于存储待替换的词语以及被替换的词语
	 */
	private HashMap<String, String> replaceWordMap = new HashMap<>(16);

	/**
	 * 用于存储数据有效性的内容
	 */
	private String[] rank;

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
	 * 用于控制写入多少条步骤后换一新行来继续写入步骤
	 */
	private int stepNum = -1;

	/**
	 * 通过测试文件模板xml配置文件和测试用例文件来构造WriteTestCase类。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configurationFile 测试文件模板xml配置文件类对象
	 * @param caseFile          测试用例文件类对象
	 * @throws DocumentException      xml文件内容不正确时抛出的异常
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public WriteTestCase(File configurationFile, File caseFile) throws DocumentException {
		// 判断传入的configurationFile是否为一个文件类对象，若非文件类对象，则抛出异常（isFile()方法包含判断文件是否存在）
		// 再判断文件是否包含文件路径是否包含“.xml”
		if (configurationFile.isFile() && configurationFile.getAbsolutePath().indexOf(".xml") > -1) {
			// 读取xml文件的信息
			configXml = new SAXReader().read(configurationFile);
		} else {
			throw new IncorrectFileException("不正确的文件格式：" + configurationFile.getAbsolutePath());
		}

		// 获取xml文件中的第一个sheet标签，则将该标签的name属性内容传入getColumnId中
		switchSheet(configXml.getRootElement().element("sheet").attributeValue("name"));

		// 判断路径是否包含“.xlsx”（使用的方法为XSSFWork）
		if (caseFile.isFile() && caseFile.getAbsolutePath().indexOf(".xlsx") > -1) {
			this.caseFile = caseFile;
		} else {
			throw new IncorrectFileException("不正确的文件格式：" + caseFile.getAbsolutePath());
		}

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
	 * 用于控制一个单元格中写入多少条步骤（预期），当设置为0或以下时，则不生效。<br>
	 * 例如，设置为2，用例的步骤（预期）有4条时，则该用例占两行（每行写2条步骤）<br>
	 * <b><i>注意：</i></b>当设置值为0或以下时，则不会校验枚举值；若值大于0时，则必须要通过{@link #setPresupposeField(FieldType, String)}方法指
	 * 定<b>步骤</b>和<b>预期</b>在{@link FieldType}枚举的{@link FieldType#STEP}和{@link FieldType#EXPECT}
	 * 的指向，否则在调用{@link #writeFile()}方法时会抛出异常。
	 * 
	 * @param stepNum 步骤数量
	 */
	public void setStepNumber(int stepNum) {
		this.stepNum = stepNum;
	}

	/**
	 * 用于指定特殊字段在xml文件中对应的字段id，可预设的字段详见{@link FieldType}。
	 * 若设置的是优先级字段，则自动读取数据有效性标签中对优先级的数据内容，添加至优先级映射中，其顺序
	 * 按照标签的顺序，可参建方法{@link #setRank(String...)}
	 * 
	 * @param fieldType 预设字段枚举对象
	 * @param fieldId   xml文件中字段对应的id
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public void setPresupposeField(FieldType fieldType, String field) {
		// 当字段id在fieldMap中查不到时，则抛出异常
		if (!fieldMap.containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}

		fieldType.setValue(field);

		// 当设置的字段为优先级字段时，则直接查找字段id对应的数据有效性，若存在，则直接对其进行设置
		if (fieldType == FieldType.RANK) {
			Element datas = null;
			if ((datas = (Element) (configXml
					.selectSingleNode("//sheet[@name='" + sheetName + "']/datas[@id='" + field + "']"))) != null) {
				// 获取datas标签下所有的data标签
				List<Element> dataList = datas.elements();
				// 设置优先级
				setRank(dataList.stream().map(e -> e.attributeValue("name")).collect(Collectors.toList()));
			}
		}
	}

	/**
	 * 用于设置优先级的等级映射值，若未使用预设的优先级添加方法可无需进行设置。优先级映射根据传入的参数 的前后顺序进行排序，例如：<br>
	 * 传入的参数为：高、中、低<br>
	 * 则相应的映射关系为：1→高、2→中、1→低<br>
	 * 若未对其映射进行设置，则按照默认的优先级数字写入到文件中
	 * 
	 * @param ranks 优先级映射文本
	 */
	public void setRank(String... ranks) {
		rank = ranks;
	}

	/**
	 * 用于设置优先级的等级映射值，若未使用预设的优先级添加方法可无需进行设置。优先级映射根据传入的参数 的前后顺序进行排序，例如：<br>
	 * 传入的参数为：高、中、低<br>
	 * 则相应的映射关系为：1→高、2→中、1→低<br>
	 * 若未对其映射进行设置，则按照默认的优先级数字写入到文件中
	 * 
	 * @param ranks 优先级映射文本
	 */
	public void setRank(List<String> ranks) {
		rank = new String[ranks.size()];

		for (int i = 0; i < ranks.size(); i++) {
			rank[i] = ranks.get(i);
		}
	}

	/**
	 * 设置字段名称的常值，通过该设置，则之后该字段将直接填入设置的值，无需再次写入字段的值
	 * 
	 * @param field   字段id
	 * @param context 相应字段的内容
	 * 
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public void setFieldValue(String field, String context) {
		// 为保证在写用例的时候也能生效，故将值设置进入fieldMap
		addContext(field, context);

		// 先将值设置入fieldMap中可以保证field字段是存在于fieldMap中，以减少此处再做判断
		// 将字段内容写入caseValueMap
		caseValueMap.put(field, context);
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
	 * 向用例中添加步骤，可传入多个参数，每一个参数表示一个步骤，通过该方法传入后将自动对数据进行编号和换行。
	 * 若需要调用该方法，则需要对步骤对应的标签id调用{@link #setPresupposeField(FieldType, String)}方法
	 * 映射到{@link FieldType}枚举中。 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#
	 * 
	 * @param steps 步骤参数
	 * @return 类本身，以方便链式编码
	 */
	public WriteTestCase addStep(String... steps) {
		// 为每一段添加标号
		for (int i = 0; i < steps.length; i++) {
			steps[i] = ((i + 1) + "." + steps[i]);
		}

		return addContext(FieldType.STEP.getValue(), steps);
	}

	/**
	 * 向用例中添加预期，可传入多个参数，每一个参数表示一个预期，通过该方法传入后将自动对数据进行编号和换行。
	 * 若需要调用该方法，则需要对预期对应的标签id调用{@link #setPresupposeField(FieldType, String)}方法
	 * 映射到{@link FieldType}枚举中。 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#
	 * 
	 * @param excepts 预期参数
	 * @return 类本身，以方便链式编码
	 */
	public WriteTestCase addExcept(String... excepts) {
		// 为每一段添加标号
		for (int i = 0; i < excepts.length; i++) {
			excepts[i] = ((i + 1) + "." + excepts[i]);
		}

		return addContext(FieldType.EXPECT.getValue(), excepts);
	}

	/**
	 * 向用例中添加前置条件，可传入多个参数，每一个参数表示一个前置条件，通过该方法传入后将自动对数据进行编号和换行。
	 * 若需要调用该方法，则需要对预期对应的标签id调用{@link #setPresupposeField(FieldType, String)}方法
	 * 映射到{@link FieldType}枚举中。 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#
	 * 
	 * @param preconditions 前置条件参数
	 * @return 类本身，以方便链式编码
	 */
	public WriteTestCase addPrecondition(String... preconditions) {
		// 为每一段添加标号
		for (int i = 0; i < preconditions.length; i++) {
			preconditions[i] = ((i + 1) + "." + preconditions[i]);
		}

		return addContext(FieldType.PRECONDITION.getValue(), preconditions);
	}

	/**
	 * 向用例中添加标题。若需要调用该方法，则需要对预期对应的
	 * 标签id调用{@link #setPresupposeField(FieldType, String)}方法映射到{@link FieldType}枚举中。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#
	 * 
	 * @param title 标题参数
	 * @return 类本身，以方便链式编码
	 */
	public WriteTestCase addTitle(String title) {
		return addContext(FieldType.TITLE.getValue(), title);
	}

	/**
	 * 向用例中添加优先级，若有通过{@link #setRank(String...)}等方法设置了优先级文本映射时，则
	 * 将传入的数字转义成设置的文本，若未设置，则直接传入数字。若需要调用该方法，则需要对预期对应
	 * 的标签id调用{@link #setPresupposeField(FieldType, String)}方法映射到{@link FieldType}枚举中
	 * 
	 * @param titles 优先级参数
	 * @return 类本身，以方便链式编码
	 */
	public WriteTestCase addRank(int rank) {
		// 若有设置优先级文本映射，则将优先级对应到文本中，若未设置，则直接传入数字
		if (this.rank != null) {
			String text = "";
			// 转义优先级
			if (rank < 1) {
				// 若优先级小于1，则直接设置优先级第一个项目
				text = this.rank[0];
			} else if (rank >= 1 && rank <= this.rank.length) {
				// 若优先级大于1，且小于数组的长度时，则直接设置相应优先级文字（由于传入的数字从1开始，故实际值为传入的数字-1）
				text = this.rank[rank - 1];
			} else {
				// 若优先级大于数组长度，则直接设置最后一个项目
				text = this.rank[this.rank.length - 1];
			}

			return addContext(FieldType.RANK.getValue(), text);
		} else {
			return addContext(FieldType.RANK.getValue(), String.valueOf(rank));
		}
	}

	/**
	 * 通过传入的字段id，将对应的字段内容写入到用例中，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#
	 * 
	 * @param field   字段id
	 * @param context 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public WriteTestCase addContext(String field, String... contexts) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}

		// 查找特殊词语，并对词语进行替换
		for (String word : replaceWordMap.keySet()) {
			// 查找所有的内容，并将特殊词语进行替换
			for (int i = 0; i < contexts.length; i++) {
				contexts[i] = contexts[i].replaceAll(word, replaceWordMap.get(word));
			}
		}

		// 将字段内容写入fieldMap
		fieldMap.get(field).context = contexts;
//		fieldMap.put(field, contexts);

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
		String caseUUID = UUID.randomUUID().toString();
		Element caseElement = sheetElement.addElement("case").addAttribute("id", caseUUID);
		// 为fieldMap中的所有key创建field标签，并记录相应的value
		fieldMap.forEach((id, field) -> {
			// 添加field标签，并设置name属性（字段名称），mark属性（备注内容）
			// dom4j当属性值传入null时，则直接不会创建该属性，故此处无需做判断字段id是否在fieldMarkMap中
			Element fieldElement = caseElement.addElement("field").addAttribute("name", id);
			// 判断当前是否有添加内容，若未添加内容，则创建一个value属性为空的text标签
			if (field.context != null) {
				// 读取所有texts（字符串数组）的所有内容，并为每一元素创建一个text标签，将值加入属性中
				Arrays.stream(field.context).forEach(text -> {
					fieldElement.addElement("text").addAttribute("value", text);
				});
			} else {
				fieldElement.addElement("text").addAttribute("value", "");
			}
		});

		// 清空fieldMap中的内容
		clearFieldContext();
		// 将字段常值设置入fieldMap中，若抛出异常，则不进行处理
		if (caseValueMap != null && caseValueMap.size() != 0) {
			caseValueMap.forEach((field, context) -> {
				try {
					addContext(field, context);
				} catch (LabelNotFoundException e) {
				}
			});
		}

		return new CaseMark(caseUUID);
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
			// 获取当前sheet的最后一行的行号，并从其下一行开始编写
			int index = xs.getLastRowNum() + 1;

			// 获取用例标签，将用例标签中的内容写入到文件中
			List<Element> caseElements = sheetElement.elements("case");
			for (Element caseElement : caseElements) {
				index = writeCase(index, xs, caseElement) + 1;
//				// 创建行
//				XSSFRow xr = xs.createRow(index++);

				// 获取字段元素，需要获取配置xml文件中的以及用例xml文件中的字段
//				List<Element> fieldElements = caseElement.elements("field");
//				for (Element fieldElement : fieldElements) {
//					//获取相应的Field对象
//					String fieldId = fieldElement.attributeValue("name");
//					Field field = fieldMap.get(fieldId);
//					
//					//创建字段所在的列相应的单元格
//					XSSFCell xc = xr.createCell(field.index);
//					
//					//将字段内容写入单元格
//					writeCellContext(xc, fieldElement);
//					// 设置单元格格式	
//					xc.setCellStyle(field.getCellStyle());
//				}
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
	
	@SuppressWarnings("unchecked")
	private int writeCase(int index, XSSFSheet xs, Element caseElement) {
		//判断每行步骤数是否大于0，大于零则是否有设置步骤和预期的枚举值，若同时满足，则抛出异常
		boolean sepStep = stepNum > 0;
		if (sepStep && (FieldType.STEP.getValue().isEmpty() || FieldType.EXPECT.getValue().isEmpty())) {
			throw new LabelNotFoundException("步骤或预期未设置枚举值");
		}
		
		//创建一行，编写测试用例的第一行内容
		XSSFRow xr = xs.createRow(index);
		// 获取字段元素，需要获取配置xml文件中的以及用例xml文件中的字段
		List<Element> fieldElements = caseElement.elements("field");
		//存储读取到的步骤和预期
		ArrayList<Element> stepAndExceptList = new ArrayList<Element>();
		//遍历所有的field标签，若需要对步骤预期进行分行显示，则不读取步骤和预期
		for (Element fieldElement : fieldElements) {
			//获取相应的Field对象
			String fieldId = fieldElement.attributeValue("name");
			//获取字段的field对象
			Field field = fieldMap.get(fieldId);
			//创建字段所在的列相应的单元格
			XSSFCell xc = xr.createCell(field.index);
			List<Element> textList = fieldElement.elements("text");
			
			//判断标签是否为步骤或预期，若为该标签则跳过获取步骤
			if (sepStep && (FieldType.STEP.getValue().equals(fieldId) || FieldType.EXPECT.getValue().equals(fieldId))) {
				//若步骤与预期的数量小于stepNum，则无需后期处理，若大于相应的数量，则先将第一行的元素进行写入
				if (textList.size() > stepNum) {
					ArrayList<Element> tempList = new ArrayList<Element>();
					for (int i = 0; i < stepNum; i++) {
						//存储第一行需要写入的元素
						tempList.add(textList.get(i));
						
						//删除stepNum以后的元素，该方法调用后会把xml文件中的标签也一并删除，不能使用
						//textList.remove(i--);
					}
					//将临时集合赋给textList
					textList = tempList;
					stepAndExceptList.add(fieldElement);
				}
			}
			
			//将字段内容写入单元格
			writeCellContext(xc, textList);
			// 设置单元格格式	
			xc.setCellStyle(field.getCellStyle());
		}
		
		//判断stepAndExceptList是否有存储内容，若存储了内容，则对步骤和预期进行分别操作
		if (stepAndExceptList.size() != 0) {
			for (Element element : stepAndExceptList) {
				//获取相应的Field对象
				String fieldId = element.attributeValue("name");
				//获取字段的field对象
				Field field = fieldMap.get(fieldId);
				//获取text标签
				List<Element> textList = element.elements("text");
				
				//用于控制当前斜土步骤的行位于标题行下降的行数
				int nowRowIndex = 1;
				//若需要通过该方法写入用例，则必然有数据需要写入，则先写入数据，再做判断
				//判断的方法为，以单元格较原位置下降的多少进行判断，例如有以下几个场景（设置的stepNum为2）：
				//1.字段中存储了5个text标签
				//2.字段中存储了3个text标签
				//3.字段中存储了4个text标签
				//由于在前面的代码已经运行并存储了2个text标签的内容，故
				//针对场景1：先执行一次存储，此时表格的行较原来下降了1行，用例实际写入了4条，但写入的数据少于text数量，故需要继续循环
				//针对场景2、3：先执行一次存储，此时表格的行较原来下降了1行，用例实际写入了3或4条，写入的数据等于text数量，故结束循环
				//综合考虑，得到公式(stepNum * ++nowRowIndex)正好等于当前写入用例的条数，且nowRowIndex自增后可以作为下一次循环开始
				//使用公式的值与用例总数判断，当公式值大于或等于text数量时，则结束循环
				do {
					//判断当前行是否被创建，若未被创建，则读取相应的行号
					xr = xs.getRow(index + nowRowIndex) == null ? xs.createRow(index + nowRowIndex) : xs.getRow(index + nowRowIndex);
					//创建字段所在的列相应的单元格
					XSSFCell xc = xr.createCell(field.index);
					
					//存储裁剪后的text元素
					ArrayList<Element> subTextList = new ArrayList<Element>();
					//其中步骤数乘当前写入行的行数正好可以得到应该从哪个元素开始裁剪，例如
					//字段中存储了5个text标签，此时设置的stepNum为2，在运行该代码前已经写入了2个text的内容，故循环从2开始（表示从第3个元素元素开始）
					//当下一次循环时，nowRowIndex为2，此时2 * 2 = 4，正好可以得到从第5个元素开始，此时在写入时也只会写入一次
					for (int i = 0; i < stepNum; i++) {
						//若剩余内容数小于stepNum时，此时循环在读取textList会抛出数组越界异常，则捕捉抛出异常后直接结束循环
						try {
							subTextList.add(textList.get(stepNum * nowRowIndex + i));
						} catch (IndexOutOfBoundsException e) {
							break;
						}
					}
					
					//将字段内容写入单元格
					writeCellContext(xc, subTextList);
					// 设置单元格格式	
					xc.setCellStyle(field.getCellStyle());
				} while(stepNum * ++nowRowIndex < textList.size());
			}
		}
		
		//返回sheet最后一行的行号
		return xs.getLastRowNum();
	}

	/**
	 * 获取并返回字段对应的内容
	 * @param textList 字段的文本元素
	 * @return 字段对应的内容
	 */
	private void writeCellContext(XSSFCell xc, List<Element> textList) {
		//存储文本内容，由于文本可能带颜色，故使用富文本来存储文本内容
		XSSFRichTextString xrts = new XSSFRichTextString("");
		
		//遍历text标签
		for (int index = 0; index < textList.size(); index++) {
			//为每一行添加回车，若在第一行，则不加入回车
			if (index != 0) {
				xrts.append("\n");
			}
			
			//创建字体
			XSSFFont xf = xw.createFont();
			// 设置字体名称
			xf.setFontName("宋体");
			// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
			xf.setFontHeightInPoints((short) 12);
			
			//获取text标签的colors属性
			String colorsText = textList.get(index).attributeValue("colors");
			//判断获取到的元素是否为null，即该属性是否存在，若存在，则加入相应的颜色到字体中
			if (colorsText != null) {
				//设置颜色
				xf.setColor(Short.valueOf(colorsText));
			}
			//拼接文本
			xrts.append(textList.get(index).attributeValue("value"), xf);
		}
		
		//将文本值设置入单元格中
		xc.setCellValue(xrts);
	}

	/**
	 * 清空fieldMap内的存储字段信息，不清除字段
	 */
	private void clearFieldContext() {
		fieldMap.forEach((key, value) -> value.clearContext());
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
			boolean datas = false;
			// 遍历所有的数据有效性标签，若存在，则将datas设置为true
			for (Element datasElemenet : datasList) {
				if (datasElemenet.attributeValue("id").equals(column.get(index).attributeValue("id"))) {
					datas = true;
					break;
				}
			}

			// 存储字段信息
			fieldMap.put(column.get(index).attributeValue("id"),
					new Field(column.get(index).attributeValue("id"), column.get(index).attributeValue("align"), index, datas));
		}
//		column.forEach(e -> fieldMap.put(e.attributeValue("id"), null));
	}

	/**
	 * 用于测试fieldMap中的内容，编码结束后删除
	 */
	/*
	 * public HashMap<String, String> getCharMap() { return fieldMap; }
	 */

	/**
	 * TODO 用于测试rank中的内容，编码结束后删除
	 */
	public String[] getRank() {
		return rank;
	}

	/**
	 * TODO 用于测试caseXml文件，编码结束后删除
	 */
	public File getCaseXml() throws IOException {
		File file = new File("src/test/java/pres/auxiliary/work/testcase/用例xml文件.xml");
		FileWriter fw = new FileWriter(file);
		caseXml.write(fw);
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
		 * @param context 标记中记录的内容
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public CaseMark markField(String field, String context) {
			// 判断字段是否存在，若不存在，则抛出异常
			if (!fieldMap.containsKey(field)) {
				throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
			}

			// 向包含uuid的标签下添加相应的属性
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("name").equals(field))
					.forEach(e -> e.addAttribute("mark", context));

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
		public CaseMark fieldBackground(String field, MarkColorsType colors) {
			// 判断字段是否存在，若不存在，则抛出异常
			if (!fieldMap.containsKey(field)) {
				throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
			}

			// 向包含uuid的标签下添加相应的属性
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("name").equals(field))
					.forEach(e -> e.addAttribute("background", String.valueOf(colors.getColorsValue())));

			return this;
		}

		/**
		 * 对整行用例的背景色进行更改（标记）
		 * 
		 * @param colors {@link MarkColorsType}类枚举
		 * @return 类本身
		 */
		@SuppressWarnings("unchecked")
		public CaseMark rowBackground(MarkColorsType colors) {
			// 将case下所有标签的name属性传至fieldBackground方法
			((List<Element>) (caseElement.elements())).stream()
					.forEach(e -> fieldBackground(e.attributeValue("name"), colors));

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
		@SuppressWarnings("unchecked")
		public CaseMark markText(String field, int index, MarkColorsType colors) {
			// 获取case下的name属性与所传参数相同的field标签
			((List<Element>) (caseElement.elements())).stream().filter(e -> e.attributeValue("name").equals(field))
					.forEach(fieldElement -> {
						// 获取其下的所有field标签
						List<Element> textElements = (List<Element>) (fieldElement.elements());
						// 判断是否存在text标签，若不存在，则结束
						if (textElements.size() != 0) {
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
							textElement.addAttribute("colors", String.valueOf(colors.getColorsValue()));
						}
					});
			return this;
		}

		/**
		 * 用于对步骤和预期同时进行标记，使用该方法前需要调用{@link WriteTestCase#setPresupposeField(FieldType, String)}
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
			markText(FieldType.STEP.getValue(), stepIndex, colors);
			// 标记预期
			markText(FieldType.EXPECT.getValue(), stepIndex, colors);

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
	 * <b>修改时间：</b>2020年2月27日下午6:58:08
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	private class Field {
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
		 * 用于存储字段在用例中对应的内容
		 */
		public String[] context = null;

		/**
		 * 存储字段是否包含数据有效性（由于在创建模板时数据有效性已被写入到模板文件中，故此处无需存储）
		 */
		public boolean datas;

		/**
		 * 用于构造Field
		 * 
		 * @param id    字段id
		 * @param align 单元格对齐方式
		 * @param index 字段在单元格中的位置
		 * @param datas 字段是否存在数据有效性
		 */
		public Field(String id, String align, int index, boolean datas) {
			this.id = id;
			this.align = align;
			this.index = index;
			this.datas = datas;
		}
		
		/**
		 * 用于清空context中的内容
		 */
		public void clearContext() {
			context = null;
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
		 * 用于创建表格的数据有效性，若当前字段无数据有效性或文件中无数据有效性 相应的内容时，则不创建数据有效性
		 */
		public void addDataValidation(XSSFSheet caseSheet, int startRowIndex, int endRowIndex) {
			// 判断当前是否存在数据有效性，不存在，则结束
			if (!datas) {
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
