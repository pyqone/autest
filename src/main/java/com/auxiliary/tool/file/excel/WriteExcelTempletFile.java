package com.auxiliary.tool.file.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
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
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.data.TableData;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.MarkColorsType;
import com.auxiliary.tool.file.MarkComment;
import com.auxiliary.tool.file.MarkFieldBackground;
import com.auxiliary.tool.file.MarkFieldLink;
import com.auxiliary.tool.file.MarkTextColor;
import com.auxiliary.tool.file.MarkTextFont;
import com.auxiliary.tool.file.TableFileReadUtil;
import com.auxiliary.tool.file.WriteFileException;
import com.auxiliary.tool.file.WriteMultipleTempletFile;
import com.auxiliary.tool.regex.RegexType;

/**
 * <p>
 * <b>文件名：</b>WriteExcelTempletFile.java
 * </p>
 * <p>
 * <b>用途：</b> 提供用于对excel数据型的内容进行编写的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月27日上午8:09:29
 * </p>
 * <p>
 * <b>修改时间：</b>2022年10月19日 上午8:13:53
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public abstract class WriteExcelTempletFile<T extends WriteExcelTempletFile<T>> extends WriteMultipleTempletFile<T>
		implements MarkComment<T>, MarkFieldBackground<T>, MarkTextColor<T>, MarkTextFont<T>, MarkFieldLink<T> {
    /**
     * 标记json中的wrapText字段
     */
	public static final String KEY_WRAP_TEXT = "wrapText";
    /**
     * 标记json中的background字段
     */
	public static final String KEY_BACKGROUND = "background";
    /**
     * 标记json中的work字段
     */
	public static final String KEY_WORK = "work";
    /**
     * 标记json中的type字段
     */
	public static final String KEY_TYPE = "type";
    /**
     * 标记json中的linkContent字段
     */
	public static final String KEY_LINK_CONTENT = "linkContent";
    /**
     * 标记json中的border字段
     */
    public static final String KEY_BORDER = "border";

	/**
	 * 单元格坐标分隔符
	 */
	protected final String COLUMN_SPLIT_SIGN = ",";

	/**
	 * 指定excel中，读取数据有效性的sheet页名称
	 */
	public static final String DATA_SHEET_NAME = "数据有效性";
	/**
	 * 指定数据有效性标题的格式
	 */
	public static final String DATA_TITLE_FORMAT = "%s-%s";

	/**
	 * 用于存储样式（key使用样式的json来编写）
	 */
	protected HashMap<JSONObject, XSSFCellStyle> styleMap = new HashMap<>();

	/**
	 * 构造Excel写入类，并设置一个Sheet页的模板及相应的名称
	 *
	 * @param templetName 模板名称
	 * @param templet     模板类
	 */
	public WriteExcelTempletFile(String templetName, FileTemplet templet) {
		super(templetName, new ExcelFileTemplet(templet.getTempletJson()));
	}

	/**
	 * 通过模板配置xml文件对文件写入类进行构造
	 * <p>
	 * 通过该方法构造的写入类为包含模板的写入类，可直接按照字段编写文件内容
	 * </p>
	 *
	 * @param templetXml 模板配置文件
	 * @param saveFile   文件保存路径
	 */
	public WriteExcelTempletFile(Document templetXml, File saveFile) {
		// 获取所有的sheet
		List<Element> sheetElementList = templetXml.getRootElement().elements();
		// 遍历元素，将sheet元素转换为模板（使用普通for循环，以方便默认命名）
		for (int index = 0; index < sheetElementList.size(); index++) {
			Element sheetElement = sheetElementList.get(index);

			ExcelFileTemplet excel = new ExcelFileTemplet(saveFile);
			// 获取并存储name属性，若无该属性，则以默认方式命名
			String sheetName = Optional.ofNullable(sheetElement.attributeValue(KEY_NAME))
					.orElse(DEFAULT_NAME + (index + 1));
			// 获取需要冻结的单元格（原模板默认首行冻结，数值控制列冻结数，为空则不冻结列）
			excel.setFreeze(1, Integer.valueOf(Optional.ofNullable(sheetElement.attributeValue("freeze")).orElse("0")));

			// 获取模板中的字段信息
			List<Element> fieldElementList = sheetElement.elements("column");
			// 记录模板位置内容
			AtomicInteger fieldIndex = new AtomicInteger(1);
			for (Element fieldElement : fieldElementList) {
				// 获取字段id
				String fieldId = Optional.ofNullable(fieldElement.attributeValue("id")).filter(id -> !id.isEmpty())
						.orElseThrow(() -> new WriteFileException(
								String.format("未指定模板字段ID，位于“%s”中的第%d行", sheetName, fieldIndex.getAndAdd(1))));
				// 存储字段名称
				excel.addTitle(fieldId, fieldElement.attributeValue("name"));

				// 存储字段对应单元格的宽度
				Optional<String> wide = Optional.ofNullable(fieldElement.attributeValue("wide"));
				if (wide.filter(f -> !f.isEmpty()).filter(f -> f.matches("\\d+(\\.\\d+)?")).isPresent()) {
                    excel.setWide(Double.valueOf(wide.get()), fieldId);
				}

				// 存储字段的水平对齐方式
                excel.setAlignment(Optional.ofNullable(fieldElement.attributeValue("align"))
                        .map(AlignmentType::getAlignmentType).orElse(AlignmentType.HORIZONTAL_LEFT), fieldId);

				// 存储字段是否分行写入
                excel.setContentBranch(Integer.valueOf(Optional.ofNullable(fieldElement.attributeValue("row_text"))
                        .filter(f -> !f.isEmpty()).filter(f -> f.matches("\\d+")).orElse("0")), fieldId);

				// 存储字段是否自动编号
				excel.setAutoSerialNumber(Boolean.valueOf(Optional.ofNullable(fieldElement.attributeValue("index"))
                        .filter(f -> !f.isEmpty()).filter(f -> f.matches("(true)|(false)")).orElse("false")), fieldId);
			}

			// 获取模板中的数据有效性信息
			List<Element> datasElementList = sheetElement.elements("datas");
			for (Element datasElement : datasElementList) {
				List<String> dataTextList = new ArrayList<>();
				String dataListName = datasElement.attributeValue("id");
				datasElement.elements().forEach(e -> {
					if ("data".equals(e.getName())) {
						// 若数据为单一元素，则直接获取相应的属性值
						dataTextList.add(e.attributeValue("name"));
					} else {
						// 若数据为文件数据，则获取文件中的内容
						TableData<String> tableData = TableFileReadUtil
								.readExcel(
										new File(Optional.ofNullable(e.attributeValue("path"))
												.orElseThrow(() -> new WriteFileException(
														String.format("无法读取文件中的数据选项：%s", dataListName)))),
										Optional.ofNullable(e.attributeValue("regex"))
												.orElseThrow(() -> new WriteFileException(
														String.format("未指定读取的Sheet：%s", dataListName))),
										true);
						// 获取列名
						String columnName = Optional.ofNullable(e.attributeValue("column"))
								.filter(s -> s.matches("\\d+")).map(Integer::valueOf).map(tableData::getFieldName)
								.orElseThrow(() -> new WriteFileException(
										String.format("无效的列下标：%s", e.attributeValue("column"))));
						// 获取列数据
                        List<Optional<String>> dataList = tableData.getColumnList(columnName);
						// 根据列数据以及开始下标，生成序号
						IntStream
								.range(Optional.ofNullable(e.attributeValue("start_row"))
										.filter(content -> content.matches("\\d+")).map(Integer::valueOf).orElse(0),
										dataList.size())
								// 转换序号为集合值，并将内容为null的数据转换为空串，最后过滤空串
								.mapToObj(dataList::get).map(data -> data.orElse("")).filter(data -> !data.isEmpty())
								// 存储最终获得的值
								.forEach(dataTextList::add);
					}
				});

				// 存储获得的数据选项
				excel.addDataOption(dataListName, dataTextList);
			}
			// 添加模板
			addTemplet(sheetName, excel);
		}
	}

	/**
	 * 用于在字段上添加文本内超链
	 * <p>
	 * 若超链接的字段在当前指向的模板中，则只需要传入字段名称；反之，则需要传入“模板名称|字段名称”。
	 * 例如，类中存在“测试用例”、“测试数据”两个模板，当前正在向“测试用例”模板写入内容，则：
	 * <ol>
	 * <li>需要在“步骤”字段上链接到“测试用例”模板“标题”字段的第2行，则传参为{@code linkField("步骤", "标题", 2)}</li>
	 * <li>需要在“步骤”字段上链接到“测试数据”模板“数据”字段的第2行，则传参为{@code linkField("步骤", "测试数据|数据", 2)}</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>字段行数可传入负数，表示从后向前遍历</li>
	 * <li>字段行标为真实的行标，当传入的下标为“2”时，则表示链接到文本的2行，而不是第1行</li>
	 * </ol>
	 * </p>
	 *
	 * @param field     字段
	 * @param linkField 需要链接的字段
	 * @param index     字段指定的下标
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T linkField(String field, String linkField, int index) {
		// 判断字段是否存在
		if (!data.getTemplet().containsField(field)) {
			return (T) this;
		}

		FileTemplet templet = this.data.getTemplet();

		// 判断当前是否存在切分符，并确定指定列的下标
		if (linkField.contains("|")) {
			// 存在分隔符，则按照分隔符进行拆分
			String[] linkTexts = linkField.split("\\|");

			// 判断模板是否存在
			if (!dataMap.containsKey(linkTexts[0])) {
				return (T) this;
			}

			// 获取得到的模板，以及切分后剩余的字段名称
			templet = dataMap.get(linkTexts[0]).getTemplet();
			linkField = linkTexts[1];
		}

		// 判断当前模板中是否存在指定的字段
		if (!templet.containsField(linkField)) {
			return (T) this;
		}

		// 获取字段指向的列下标，并计算得到excel中，数字列下标对应的字母
		Optional<String> columnChar = Optional.ofNullable(templet.getFieldAttribute(linkField, FileTemplet.KEY_INDEX))
				.map(Object::toString).map(Integer::valueOf).map(this::num2CharIndex);
		if (columnChar.isPresent()) {
			String templetName = templet.getTempletAttribute(FileTemplet.KEY_NAME).toString();
			// 拼接文本链接内容
			String linkText = String.format("'%s'!%s%d", templetName, columnChar.get(), index);

			// 记录当前链接json
			JSONObject linkJson = new JSONObject();
			linkJson.put(KEY_TYPE, LinkType.DOMCUMENT.getCode());
			linkJson.put(KEY_LINK_CONTENT, linkText);

			if (!data.getCaseJson().containsKey(field)) {
				data.getCaseJson().put(field, new JSONObject());
			}
			data.getCaseJson().getJSONObject(field).put(KEY_LINK, linkJson);
		}

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T linkUrl(String field, URL url) {
		try {
			// 记录当前链接json
			JSONObject linkJson = new JSONObject();
			linkJson.put(KEY_TYPE, LinkType.URL.getCode());
			linkJson.put(KEY_LINK_CONTENT, url.toURI().toASCIIString());

			data.getCaseJson().getJSONObject(field).put(KEY_LINK, linkJson);
		} catch (URISyntaxException e) {
			throw new WriteFileException("域名url错误", e);
		}

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T linkEmail(String field, String email) {
		try {
			if (!RegexType.EMAIL.judgeString(email)) {
				throw new WriteFileException("邮箱格式错误：" + email);
			}

			// 记录当前链接json
			JSONObject linkJson = new JSONObject();
			linkJson.put(KEY_TYPE, LinkType.EMAIL.getCode());

			linkJson.put(KEY_LINK_CONTENT, new URI("mailto:" + email).toASCIIString());
			data.getCaseJson().getJSONObject(field).put(KEY_LINK, linkJson);
		} catch (URISyntaxException e) {
			throw new WriteFileException("邮箱格式错误：" + email, e);
		}

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T linkFile(String field, File file) {
		// 记录当前链接json
		JSONObject linkJson = new JSONObject();
		linkJson.put(KEY_TYPE, LinkType.FILE.getCode());
		linkJson.put(KEY_LINK_CONTENT, file.toURI().toString());

		data.getCaseJson().getJSONObject(field).put(KEY_LINK, linkJson);

		return (T) this;
	}
	
    /**
     * 该方法用于指定字段的当前行单元格上添加边框
     * 
     * @param borderStyle 边框样式枚举
     * @param fields      字段组
     * @return 类本身
     * @since autest 3.7.0
     */
    @SuppressWarnings("unchecked")
    public T border(BorderStyle borderStyle, String... fields) {
        // 将字段数组转换为Set集合，若未传入数组，则默认使用当前
        Set<String> fieldSet = Optional.ofNullable(fields).filter(arr -> arr.length != 0).map(Arrays::asList)
                .map(HashSet::new).orElseGet(() -> new HashSet<>(getWriteData().getTemplet().getFieldList()));
        for (String field : fieldSet) {
            data.getCaseJson().getJSONObject(field).put(KEY_BORDER, borderStyle.toString());
        }

        return (T) this;
    }

	@SuppressWarnings("unchecked")
	@Override
	public T bold(String field, int... textIndexs) {
		// 判断下标集是否为空
		Arrays.stream(Optional.ofNullable(textIndexs).filter(arr -> arr.length != 0).orElse(new int[] {}))
				// 将下标转换为文本json，并过滤为null的对象
				.mapToObj(index -> getTextJson(field, index)).map(Optional::ofNullable).filter(Optional::isPresent)
				// 向字段json中添加相应的内容
				.map(Optional::get).forEach(json -> {
					json.put(KEY_BOLD, true);
				});

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T italic(String field, int... textIndexs) {
		// 判断下标集是否为空
		Arrays.stream(Optional.ofNullable(textIndexs).filter(arr -> arr.length != 0).orElse(new int[] {}))
				// 将下标转换为文本json，并过滤为null的对象
				.mapToObj(index -> getTextJson(field, index)).map(Optional::ofNullable).filter(Optional::isPresent)
				// 向字段json中添加相应的内容
				.map(Optional::get).forEach(json -> {
					json.put(KEY_ITALIC, true);
				});
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T underline(String field, int... textIndexs) {
		// 判断下标集是否为空
		Arrays.stream(Optional.ofNullable(textIndexs).filter(arr -> arr.length != 0).orElse(new int[] {}))
				// 将下标转换为文本json，并过滤为null的对象
				.mapToObj(index -> getTextJson(field, index)).map(Optional::ofNullable).filter(Optional::isPresent)
				// 向字段json中添加相应的内容
				.map(Optional::get).forEach(json -> {
					json.put(KEY_UNDERLINE, true);
				});
		return (T) this;
	}

    @SuppressWarnings({ "unchecked", "deprecation" })
	@Override
    @Deprecated
	public T changeTextColor(MarkColorsType markColorsType, String field, int... textIndexs) {
		// 判断下标集是否为空
		Arrays.stream(Optional.ofNullable(textIndexs).filter(arr -> arr.length != 0).orElse(new int[] {}))
				// 将下标转换为文本json，并过滤为null的对象
				.mapToObj(index -> getTextJson(field, index)).map(Optional::ofNullable).filter(Optional::isPresent)
				// 向字段json中添加相应的内容
				.map(Optional::get).forEach(json -> {
					json.put(KEY_COLOR, markColorsType.getColorsValue());
				});
		return (T) this;
	}

    @SuppressWarnings("unchecked")
    @Override
    public T changeTextColor(IndexedColors indexedColors, String field, int... textIndexs) {
        // 判断下标集是否为空
        Arrays.stream(Optional.ofNullable(textIndexs).filter(arr -> arr.length != 0).orElse(new int[] {}))
                // 将下标转换为文本json，并过滤为null的对象
                .mapToObj(index -> getTextJson(field, index)).map(Optional::ofNullable).filter(Optional::isPresent)
                // 向字段json中添加相应的内容
                .map(Optional::get).forEach(json -> {
                    json.put(KEY_COLOR, indexedColors.getIndex());
                });
        return (T) this;
    }

	@Override
    @SuppressWarnings("unchecked")
    @Deprecated
	public T changeCaseBackground(MarkColorsType markColorsType) {
        data.getCaseJson().put(KEY_BACKGROUND, markColorsType.getColorsValue());
		return (T) this;
	}

	@Override
    @SuppressWarnings("unchecked")
    @Deprecated
	public T changeFieldBackground(String field, MarkColorsType markColorsType) {
        if (data.getCaseJson().containsKey(field)) {
            data.getCaseJson().getJSONObject(field).put(KEY_BACKGROUND, markColorsType.getColorsValue());
		}
		return (T) this;
	}

    @SuppressWarnings("unchecked")
    @Override
    public T changeCaseBackground(IndexedColors indexedColors) {
        data.getCaseJson().put(KEY_BACKGROUND, indexedColors.getIndex());
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T changeFieldBackground(String field, IndexedColors indexedColors) {
        if (data.getCaseJson().containsKey(field)) {
            data.getCaseJson().getJSONObject(field).put(KEY_BACKGROUND, indexedColors.getIndex());
        }
        return (T) this;
    }

	@SuppressWarnings("unchecked")
	@Override
	public T fieldComment(String field, String commentText) {
		if (data.getCaseJson().containsKey(field)) {
			data.getCaseJson().getJSONObject(field).put(KEY_COMMENT, commentText);
		}
		return (T) this;
	}

	@Override
	public T addContent(String field, int index, String... contents) {
		String[] newContents = new String[contents.length];
		// 遍历当前的传参，若为数字，则将其转换为读取相关的数据有效性数据
		for (int i = 0; i < contents.length; i++) {
			try {
				// 尝试将传入的内容转换为数字，以获取数据有效性的内容
				int optionIndex = Integer.valueOf(contents[i]);
				// 获取模板中的数据有效性内容
				newContents[i] = Optional.ofNullable(data.getTemplet().getTempletAttribute(ExcelFileTemplet.KEY_DATA))
						.map(o -> (JSONObject) o).map(json -> json.getJSONArray(field))
						.map(list -> list.getString(analysisIndex(list.size(), optionIndex, true))).orElse(contents[i]);
			} catch (Exception e) {
				newContents[i] = contents[i];
			}
		}

		return super.addContent(field, index, newContents);
	}

	@Override
	protected void createTempletFile(FileTemplet templet) {
		JSONObject tempJson = JSONObject.parseObject(templet.getTempletJson());
		// 判断文件是否已经存在，若文件存在，则读取原文件，若文件不存在，则添加一个新的对象
		File templetFile = new File(tempJson.getString(ExcelFileTemplet.KEY_SAVE));
		XSSFWorkbook excel;
		if (templetFile.exists()) {
			try (FileInputStream fip = new FileInputStream(templetFile)) {
				// 通过输入流，使XSSFWorkbook对象指向模版文件
				excel = new XSSFWorkbook(fip);
			} catch (IOException e) {
				throw new WriteFileException("文件异常，无法创建模板：" + tempJson.getString(ExcelFileTemplet.KEY_SAVE), e);
			}
		} else {
			// 创建文件存储的路径文件夹
			templetFile.getParentFile().mkdirs();
			excel = new XSSFWorkbook();
		}

		// 读取指定的sheet，若sheet不存在，则根据名称创建一个新的sheet
		XSSFSheet sheet = Optional.ofNullable(excel.getSheet(tempJson.getString(ExcelFileTemplet.KEY_NAME)))
				.orElseGet(() -> excel.createSheet(tempJson.getString(ExcelFileTemplet.KEY_NAME)));
		// 冻结表格
		int freezeLeft = tempJson.getIntValue(ExcelFileTemplet.KEY_FREEZE_LEFT);
		int freezeTop = tempJson.getIntValue(ExcelFileTemplet.KEY_FREEZE_TOP);
		sheet.createFreezePane(freezeLeft, freezeTop, freezeLeft, freezeTop);

		// 创建标题json
		JSONObject styleJson = new JSONObject();
		styleJson.put(ExcelFileTemplet.KEY_HORIZONTAL, (short) 2);
		styleJson.put(ExcelFileTemplet.KEY_VERTICAL, (short) 5);
		styleJson.put(KEY_WRAP_TEXT, true);
		styleJson.put(KEY_FONT_NAME, "宋体");
		styleJson.put(KEY_FONT_SIZE, (short) 12);
		styleJson.put(KEY_BOLD, true);
		styleJson.put(KEY_ITALIC, false);
		styleJson.put(KEY_UNDERLINE, false);
		styleJson.put(KEY_WORK, tempJson.getString(KEY_NAME));

		// 根据字段内容，创建单元格，写入单元格标题
		JSONObject fieldJson = tempJson.getJSONObject(ExcelFileTemplet.KEY_FIELD);
		fieldJson.keySet().stream().map(fieldJson::getJSONObject).forEach(json -> {
            // 若当前字段存在边框属性，则在样式json中添加相应的属性
            if (json.containsKey(ExcelFileTemplet.KEY_BORDER)) {
                styleJson.put(KEY_BORDER, json.getString(ExcelFileTemplet.KEY_BORDER));
            }

            // 设置字段的背景色
            if (json.containsKey(ExcelFileTemplet.KEY_BACKGROUND)) {
                styleJson.put(KEY_BACKGROUND, json.getShort(ExcelFileTemplet.KEY_BACKGROUND));
            }

			XSSFCell cell = setCellContent(getCell(sheet, 0, json.getIntValue(ExcelFileTemplet.KEY_INDEX)),
					new XSSFRichTextString(json.getString(KEY_NAME)), getStyle(excel, styleJson));
			// 设置列宽，若不存在设置，则默认列宽
			if (json.containsKey(ExcelFileTemplet.KEY_WIDE)) {
				// 获取单元格坐标的列数，并转换为数字
				sheet.setColumnWidth(cell.getAddress().getColumn(),
						(int) (json.getDouble(ExcelFileTemplet.KEY_WIDE) * 256));
			}
		});

		// 添加数据有效性（不存在则跳过添加）
		JSONObject dataJson = tempJson.getJSONObject(ExcelFileTemplet.KEY_DATA);
		Optional.ofNullable(dataJson).filter(json -> !json.keySet().isEmpty()).ifPresent(json -> {
			XSSFSheet dataSheet = Optional.ofNullable(excel.getSheet(DATA_SHEET_NAME))
					.orElseGet(() -> excel.createSheet(DATA_SHEET_NAME));
			dataJson.keySet().forEach(key -> {
				JSONArray dataArrayJson = dataJson.getJSONArray(key);
				if (dataArrayJson.size() != 0) {
					// 添加标题
					XSSFCell cell = setCellContent(getCell(dataSheet, 0, -1),
							new XSSFRichTextString(String.format(DATA_TITLE_FORMAT, tempJson.getString(KEY_NAME), key)),
							null);

					// 写入数据有效性内容
					for (int i = 0; i < dataArrayJson.size(); i++) {
						setCellContent(getCell(dataSheet, i + 1, cell.getAddress().getColumn()),
								new XSSFRichTextString(dataArrayJson.getString(i)), null);
					}
				}
			});
		});

		// 添加数据筛选按钮
        Optional.ofNullable(tempJson.getBoolean(ExcelFileTemplet.KEY_FILTRATE)).filter(is -> is == true).ifPresent(is -> {
            sheet.setAutoFilter(CellRangeAddress.valueOf(String.format("A1:%s1", num2CharIndex(sheet.getRow(0).getLastCellNum() - 1))));
        });

		// 若存在数据有效性sheet页，则设置该页展示在文档最后
		if (excel.getSheet(DATA_SHEET_NAME) != null) {
			excel.setSheetOrder(DATA_SHEET_NAME, excel.getNumberOfSheets() - 1);
		}

		// 定义输出流，用于向指定的Excel文件中写入内容
		try (FileOutputStream fop = new FileOutputStream(templetFile)) {
			// 写入文件
			excel.write(fop);

		} catch (Exception e) {
			throw new WriteFileException("文件异常，无法创建模板：" + tempJson.getString(ExcelFileTemplet.KEY_SAVE), e);
		} finally {
			try {
				excel.close();
			} catch (IOException e) {
				throw new WriteFileException("文件异常，无法创建模板：" + tempJson.getString(ExcelFileTemplet.KEY_SAVE), e);
			}
		}
	}

	@Override
	protected void contentWriteTemplet(FileTemplet templet, int caseStartIndex, int caseEndIndex) {
		File templetFile = new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString());
		if (!templetFile.exists()) {
			throw new WriteFileException("文件模板路径不存在，无法写入数据：" + templetFile.getAbsolutePath());
		}
		XSSFWorkbook excel;
		try (FileInputStream fip = new FileInputStream(templetFile)) {
			// 通过输入流，使XSSFWorkbook对象指向模版文件
			excel = new XSSFWorkbook(fip);
		} catch (IOException e) {
			throw new WriteFileException("文件异常，无法写入数据：" + templetFile.getAbsolutePath(), e);
		}

		// 获取模板json
		JSONObject templetJson = JSONObject.parseObject(templet.getTempletJson());
		// 获取模板名称，并根据模板名称，获取相应的sheet
		String templetName = templetJson.getString(ExcelFileTemplet.KEY_NAME);
		XSSFSheet templetSheet = Optional.ofNullable(excel.getSheet(templetJson.getString(ExcelFileTemplet.KEY_NAME)))
				.orElseThrow(() -> new WriteFileException(String.format("Sheet名称在文件中不存在，无法写入内容。\nSheet名称：%s\n文件路径：%s",
						templetName, templetFile.getAbsolutePath())));

		// 根据模板名称，获取内容json
		JSONArray contentListJson = dataMap.get(templetName).getContentJson().getJSONArray(KEY_CASE);
		// 循环，遍历所有需要写入的内容
		for (int index = caseStartIndex; index < caseEndIndex + 1; index++) {
			// 获取内容json
			JSONObject contentJson = contentListJson.getJSONObject(index);
			// 计算当前需要写入的行
			int lastRowIndex = templetSheet.getLastRowNum() + 1;

			// 遍历所有的字段
			for (String field : templetJson.getJSONObject(ExcelFileTemplet.KEY_FIELD).keySet()) {
				// 判断字段是否存在内容，不存在，则不进行处理
				if (!contentJson.containsKey(field)) {
					continue;
				}

				// 获取当前字段的模板json
				JSONObject fieldTempletJson = templetJson.getJSONObject(ExcelFileTemplet.KEY_FIELD)
						.getJSONObject(field);
				JSONObject fieldContentJson = contentJson.getJSONObject(field);

				// 存储创建的最后一个单元格，用于添加与文本块相关的内容
				XSSFCell cell = null;
				// 根据当前字段是否分表格写入内容，计算当前写入单元格的行号
				if (fieldTempletJson.containsKey(ExcelFileTemplet.KEY_ROW_TEXT)) {
					cell = writeMultipleCellContent(excel, templetSheet, fieldContentJson, fieldTempletJson,
                            contentJson, lastRowIndex);
				} else {
					cell = writeSingleCellContent(excel, templetSheet, fieldContentJson, fieldTempletJson,
                            contentJson, lastRowIndex);
				}

				// 记录当前内容json的位置 TODO 用于超链接
//				contentJson.put(KEY_RELATIVE_ROW, lastRowIndex);

				// 判断当前是否存在注解
				if (fieldContentJson.containsKey(KEY_COMMENT)) {
					addComment(excel, templetSheet, cell, fieldContentJson.getString(KEY_COMMENT));
				}

				// 判断当前是否存在超链接
				if (fieldContentJson.containsKey(KEY_LINK)) {
					addLink(excel, cell, fieldContentJson.getJSONObject(KEY_LINK));
				}
			}

			// 获取数据有效性标题
			List<String> dataTitleList = readDataOptionTitle(excel);
			addDataOption(templetSheet, templetJson, dataTitleList, lastRowIndex);
		}

		// 定义输出流，用于向指定的Excel文件中写入内容
		try (FileOutputStream fop = new FileOutputStream(templetFile)) {
			// 写入文件
			excel.write(fop);
		} catch (Exception e) {
			throw new WriteFileException("文件异常，无法写入：" + templet.getTempletAttribute(FileTemplet.KEY_SAVE), e);
		} finally {
			try {
				excel.close();
			} catch (IOException e) {
				throw new WriteFileException("文件异常，无法写入：" + templet.getTempletAttribute(FileTemplet.KEY_SAVE), e);
			}

			// 完成编写后，清理当前存储的样式，以便于后续编写
			styleMap.clear();
		}
	}

	/**
     * 用于写入单个单元格内容
     *
     * @param excel            excel文件对象
     * @param templetSheet     工作页
     * @param fieldContentJson 字段内容json
     * @param fieldTempletJson 字段模板json
     * @param contentJson
     * @param lastRowIndex     需要写入的行号
     * @return 写入数据的单元格
     */
	protected XSSFCell writeSingleCellContent(XSSFWorkbook excel, XSSFSheet templetSheet, JSONObject fieldContentJson,
            JSONObject fieldTempletJson, JSONObject contentJson, int lastRowIndex) {
		XSSFRichTextString content = new XSSFRichTextString("");
		XSSFCellStyle style = null;
		int columnIndex = fieldTempletJson.getIntValue(FileTemplet.KEY_INDEX);

		// 获取段落内容
		JSONArray textListJson = Optional.ofNullable(fieldContentJson.getJSONArray(KEY_DATA)).orElse(new JSONArray());
		// 遍历内容串，拼接需要写入到单元格的内容
		for (int textIndex = 0; textIndex < textListJson.size(); textIndex++) {
			JSONObject textJson = textListJson.getJSONObject(textIndex);

			// 根据当前字段的json，获取样式
			style = getStyle(excel,
                    fieldJson2StyleJson(templetSheet.getSheetName(), fieldTempletJson, textJson, fieldContentJson,
                            contentJson));

			// 根据样式与内容，拼接文本
			appendContent(content, style, textJson.getString(KEY_TEXT),
					fieldTempletJson.getBooleanValue(ExcelFileTemplet.KEY_AUTO_NUMBER), textIndex);
		}

		// 由于统一字段单元格样式一致，故此处取最后一个样式作为单元格的样式
		return setCellContent(getCell(templetSheet, lastRowIndex, columnIndex), content, style);

	}

	/**
     * 用于写入多个单元格内容
     *
     * @param excel            excel文件对象
     * @param templetSheet     工作页
     * @param fieldContentJson 字段内容json
     * @param fieldTempletJson 字段模板json
     * @param contentJson
     * @param lastRowIndex     需要写入的行号
     * @return 写入数据的第一个单元格
     */
	protected XSSFCell writeMultipleCellContent(XSSFWorkbook excel, XSSFSheet templetSheet, JSONObject fieldContentJson,
            JSONObject fieldTempletJson, JSONObject contentJson, int lastRowIndex) {
		ArrayList<XSSFRichTextString> contentList = new ArrayList<>();
		XSSFCellStyle style = null;
		int columnIndex = fieldTempletJson.getIntValue(FileTemplet.KEY_INDEX);

		// 获取段落内容
		JSONArray textListJson = Optional.ofNullable(fieldContentJson.getJSONArray(KEY_DATA)).orElse(new JSONArray());
		// 遍历内容串，拼接需要写入到单元格的内容
		for (int textIndex = 0; textIndex < textListJson.size(); textIndex++) {
			JSONObject textJson = textListJson.getJSONObject(textIndex);

			// 根据当前字段是否分表格写入内容，计算当前写入单元格的行号
			int rowText = fieldTempletJson.getIntValue(ExcelFileTemplet.KEY_ROW_TEXT);
			// 判断当前内容集合的元素个数，若个数与(textIndex / rowText)一致，则表示当前需要换行
			/*
			 * 思路：当textIndex正好被rowText整除时，表示当前的内容需要重新开启一个单元格进行编写，
			 * 而由于下标是从0开始计算，使得第一个单元格开始写入的内容为 0 / rowText = 0 = contentList.size()
			 * 故可以推算出，当contentList集合元素个数正好等于(textIndex / rowText)整除时的商时，表示其需要换行
			 */
			if (contentList.size() == (textIndex / rowText)) {
				contentList.add(new XSSFRichTextString(""));
			}

			// 获取最后一位数据
			XSSFRichTextString content = contentList.get(contentList.size() - 1);
			// 根据当前字段的json，获取样式
			style = getStyle(excel,
                    fieldJson2StyleJson(templetSheet.getSheetName(), fieldTempletJson, textJson, fieldContentJson,
                            contentJson));

			// 根据样式与内容，拼接文本
			appendContent(content, style, textJson.getString(KEY_TEXT),
					fieldTempletJson.getBooleanValue(ExcelFileTemplet.KEY_AUTO_NUMBER), textIndex);
		}

		// 遍历需要写入的数据内容，根据内容下标，创建相应的单元格
		int rowIndex = lastRowIndex;
		for (int contentIndex = 0; contentIndex < contentList.size(); contentIndex++) {
			// 由于统一字段单元格样式一致，故此处取最后一个样式作为单元格的样式
			setCellContent(getCell(templetSheet, (rowIndex + contentIndex), columnIndex), contentList.get(contentIndex),
					style);
		}

		// 返回创建的第一个单元格
		return getCell(templetSheet, rowIndex, columnIndex);
	}

	/**
	 * 用于对需要写入单元格的文本进行拼接
	 *
	 * @param content      需要拼接的富文本内容
	 * @param style        样式
	 * @param text         文本内容
	 * @param isAutoNumber 是否自动编号
	 * @param textIndex    文本下标
	 * @return 拼接后的富文本对象
	 */
	protected XSSFRichTextString appendContent(XSSFRichTextString content, XSSFCellStyle style, String text,
			boolean isAutoNumber, int textIndex) {
		// 获取单元格内容，判断内容是否需要自动标号
		if (isAutoNumber) {
			text = String.format("%d.%s", (textIndex + 1), text);
		}

		// 判断当前文本前是否包含了文本，若存在文本，则添加换行
		text = (content.length() == 0 ? "" : "\n") + text;
		content.append(text, style.getFont());

		return content;
	}

	/**
	 * 用于获取指定的单元格对象，若对象不存在，则创建单元格
	 *
	 * @param sheet       工作页
	 * @param rowIndex    单元格所在下标
	 * @param columnIndex 单元格所在列下标
	 * @return 单元格对象
	 */
	protected XSSFCell getCell(XSSFSheet sheet, int rowIndex, int columnIndex) {
		// 获取行，若行不存在，则创建行对象
		XSSFRow row = Optional.ofNullable(sheet.getRow(rowIndex)).orElseGet(() -> sheet.createRow(rowIndex));
		// 若列下标为-1，则表示插入到最后一列上
		int newColumnIndex = (columnIndex == -1 ? (row.getLastCellNum() == -1 ? 0 : row.getLastCellNum())
				: columnIndex);
		return Optional.ofNullable(row.getCell(newColumnIndex)).orElseGet(() -> row.createCell(newColumnIndex));
	}

	/**
	 * 用于在单元格设置样式，并写入内容
	 *
	 * @param cell    单元格类
	 * @param content 需要写入单元格的内容
	 * @param style   需要写入单元格的样式，为空则不设置样式
	 * @return 单元格对象
	 */
	protected XSSFCell setCellContent(XSSFCell cell, XSSFRichTextString content, XSSFCellStyle style) {
		// 若当前传入单元格样式，则对单元格样式进行设置
		if (style != null) {
			cell.setCellStyle(style);
		}

		cell.setCellValue(content);

		return cell;
	}

	/**
	 * 用于拼接一个单元格中带样式的内容
	 *
	 * @param content 富文本
	 * @param text    文本内容
	 * @param style   样式
	 * @return 拼接后带样式的内容
	 */
	protected XSSFRichTextString setContent(XSSFRichTextString content, String text, XSSFCellStyle style) {
		// 判断当前是否存储有内容，若无内容，则不添加换行
		text = (content.length() == 0 ? "" : "\n") + text;

		// 判断当前是否需要设置样式，若不存在样式，则只设置内容
		if (style == null) {
			content.append(text);
		} else {
			content.append(text, style.getFont());
		}

		return content;
	}

	/**
     * 用于将字段的json转换为字段样式json
     *
     * @param templetName      模板名称
     * @param templetFieldJson 模板字段json
     * @param textJson         文本json
     * @param contentJson
     * @param background       背景颜色
     * @return 字段样式json
     */
	protected JSONObject fieldJson2StyleJson(String templetName, JSONObject templetFieldJson, JSONObject textJson,
            JSONObject fieldContentJson, JSONObject contentJson) {
		JSONObject styleJson = new JSONObject();
		styleJson.put(ExcelFileTemplet.KEY_HORIZONTAL,
				Optional.ofNullable(templetFieldJson.getInteger(ExcelFileTemplet.KEY_HORIZONTAL)).orElse(0));
		styleJson.put(KEY_FONT_NAME, "宋体");
		styleJson.put(KEY_UNDERLINE, Optional.ofNullable(textJson.getBoolean(KEY_UNDERLINE)).orElse(false));
		styleJson.put(KEY_BOLD, Optional.ofNullable(textJson.getBoolean(KEY_BOLD)).orElse(false));
		styleJson.put(KEY_ITALIC, Optional.ofNullable(textJson.getBoolean(KEY_ITALIC)).orElse(false));
		styleJson.put(ExcelFileTemplet.KEY_VERTICAL,
				Optional.ofNullable(templetFieldJson.getInteger(ExcelFileTemplet.KEY_VERTICAL)).orElse(5));
		styleJson.put(KEY_FONT_SIZE, 12);
		styleJson.put(KEY_WORK, templetName);
		styleJson.put(KEY_WRAP_TEXT, true);

		// 判断是否需要设置字体颜色
		if (textJson.containsKey(KEY_COLOR)) {
			styleJson.put(KEY_COLOR, textJson.getShort(KEY_COLOR));
		}

		// 判断是否需要背景颜色
        short background = fieldContentJson.containsKey(KEY_BACKGROUND) ? fieldContentJson.getShortValue(KEY_BACKGROUND)
                : (contentJson.containsKey(KEY_BACKGROUND) ? contentJson.getShortValue(KEY_BACKGROUND) : -1);
		if (background != -1) {
			styleJson.put(KEY_BACKGROUND, background);
		}

        // 判断是会否需要边框
        Optional.ofNullable(fieldContentJson.getString(KEY_BORDER)).filter(text -> !text.isEmpty())
                .ifPresent(text -> styleJson.put(KEY_BORDER, text));

		return styleJson;
	}

	/**
	 * 用于获取指定的单元格样式
	 * <p>
	 * 根据样式json，来判断该样式是否存在，若不存在，则创建样式；若存在，则获取该样式
	 * </p>
	 *
	 * @param styleJson 样式json
	 * @return 指定内容的单元格样式
	 */
	protected XSSFCellStyle getStyle(XSSFWorkbook excel, JSONObject styleJson) {
		// 判断样式json是否存在
		if (!styleMap.containsKey(styleJson)) {
			// 创建样式
			XSSFCellStyle style = excel.createCellStyle();
			// 设置单元格水平居中
			style.setAlignment(AlignmentType.getAlignmentType(styleJson.getShortValue(ExcelFileTemplet.KEY_HORIZONTAL))
					.getHorizontal());
			// 设置单元格垂直居中
			style.setVerticalAlignment(AlignmentType
					.getAlignmentType(styleJson.getShortValue(ExcelFileTemplet.KEY_VERTICAL)).getVertical());

			// 创建字体样式
			XSSFFont font = excel.createFont();
			// 设置字体名称
			font.setFontName(styleJson.getString(KEY_FONT_NAME));
			// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
			font.setFontHeightInPoints(styleJson.getShortValue(KEY_FONT_SIZE));
			// 设置字体加粗
			font.setBold(styleJson.getBooleanValue(KEY_BOLD));
			// 设置字体倾斜
			font.setItalic(styleJson.getBooleanValue(KEY_ITALIC));
			// 设置字体下划线
			if (styleJson.getBooleanValue(KEY_UNDERLINE)) {
				font.setUnderline(FontUnderline.SINGLE);
			}
			// 设置字体颜色
			if (styleJson.containsKey(KEY_COLOR)) {
				font.setColor(styleJson.getShortValue(KEY_COLOR));
			}

			// 设置背景颜色
			if (styleJson.containsKey(KEY_BACKGROUND)) {
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				style.setFillForegroundColor(styleJson.getShortValue(KEY_BACKGROUND));
			}

			// 设置字体的样式
			style.setFont(font);
			// 设置单元格自动换行
			style.setWrapText(styleJson.getBooleanValue(KEY_WRAP_TEXT));

            // 设置单元格添加边框
            if (styleJson.containsKey(KEY_BORDER)) {
                BorderStyle border = BorderStyle.valueOf(styleJson.getString(KEY_BORDER));
                style.setBorderBottom(border);
                style.setBorderLeft(border);
                style.setBorderRight(border);
                style.setBorderTop(border);
            }

			styleMap.put(styleJson, style);

			return style;
		} else {
			return styleMap.get(styleJson);
		}
	}

	@Override
	protected boolean isExistTemplet(File templetFile, FileTemplet templet) {
		// 判断文件是否存在，不存在则返回false
		if (!templetFile.exists()) {
			return false;
		}

		// 读取文件，若文件中存在与模板名称相同的Sheet，则表示模板存在
		try (FileInputStream fip = new FileInputStream(templetFile); XSSFWorkbook excel = new XSSFWorkbook(fip)) {
			return Optional.ofNullable(excel.getSheet(templet.getTempletAttribute(KEY_NAME).toString())).isPresent();
		} catch (IOException e) {
			return false;
		}
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
	 * 用于读取数据有效性sheet页中所有数据有效性列的标题
	 *
	 * @param excel 模板类对象
	 * @return 数据有效性相关的标题
	 */
	protected List<String> readDataOptionTitle(XSSFWorkbook excel) {
		List<String> dataOptionTitleList = new ArrayList<>();

		Optional.ofNullable(excel.getSheet(DATA_SHEET_NAME)).map(sheet -> sheet.getRow(0)).ifPresent(row -> {
			IntStream.range(0, row.getLastCellNum() + 1).mapToObj(row::getCell).filter(cell -> cell != null)
					.map(XSSFCell::getStringCellValue).filter(text -> !text.isEmpty())
					.forEach(dataOptionTitleList::add);
		});

		return dataOptionTitleList;
	}

	/**
	 * 用于向单元格上添加Comment标注
	 *
	 * @param excel          excel对象
	 * @param sheet          sheet对象
	 * @param cell           相应单元格对象
	 * @param commentContent 字段元素
	 */
	protected void addComment(XSSFWorkbook excel, XSSFSheet sheet, XSSFCell cell, String commentContent) {
		// 判断内容是否存在，若不存在，则结束方法运行，不添加标注
		if (commentContent == null) {
			return;
		}

		// 创建一个标注，并确定其在一个单元格的左上角和右下角
		Comment com = sheet.createDrawingPatriarch().createCellComment(excel.getCreationHelper().createClientAnchor());
		// 创建标注的内容
		com.setString(excel.getCreationHelper().createRichTextString(commentContent));
		// 创建标注的作者(作者为计算机名称)
		com.setAuthor(System.getenv().get("COMPUTERNAME"));
		// 将标注附加到单元格上
		cell.setCellComment(com);
	}

	/**
	 * 用于向单元格上添加超链接
	 *
	 * @param excel    excel对象
	 * @param cell     相应单元格对象
	 * @param linkJson 链接内容json
	 */
	protected void addLink(XSSFWorkbook excel, XSSFCell cell, JSONObject linkJson) {
		// 添加本地文件链接
		Hyperlink link = excel.getCreationHelper()
				.createHyperlink(LinkType.getLinkType(linkJson.getShortValue(KEY_TYPE)).getHyperlinkType());
		// 添加超链接
		link.setAddress(linkJson.getString(KEY_LINK_CONTENT));

		// 在单元格上添加超链接
		cell.setHyperlink(link);
	}

	/**
	 * 用于在单元格上附加数据有效性下拉选项
	 *
	 * @param sheet         工作页
	 * @param templetJson   模板json
	 * @param dataTitleList 数据有效性选项标题集合
	 * @param rowIndex      行下标
	 */
	protected void addDataOption(XSSFSheet sheet, JSONObject templetJson, List<String> dataTitleList, int rowIndex) {
		// 若模板不存在数据有效性选项，则不添加
		if (!templetJson.containsKey(ExcelFileTemplet.KEY_DATA)) {
			return;
		}

		// 获取字段json以及数据选项json
		JSONObject dataJson = templetJson.getJSONObject(ExcelFileTemplet.KEY_DATA);
		JSONObject fieldsJson = templetJson.getJSONObject(ExcelFileTemplet.KEY_FIELD);

		// 遍历dataJson的内容
		for (String field : dataJson.keySet()) {
			if (!fieldsJson.containsKey(field)) {
				continue;
			}

			JSONObject fieldJson = fieldsJson.getJSONObject(field);
			// 拼接数据有效性标题
			String dataTitle = String.format(DATA_TITLE_FORMAT, templetJson.getString(ExcelFileTemplet.KEY_NAME),
					field);
			// 判断当前标题是否在数据选项标题集合中
			if (!dataTitleList.contains(dataTitle)) {
				continue;
			}

			// 指定数据有效性选项公式
			String dataChar = num2CharIndex(dataTitleList.indexOf(dataTitle));
			String dataConstraint = String.format("=%s!$%s$2:$%s$%d", DATA_SHEET_NAME, dataChar, dataChar,
					(dataJson.getJSONArray(field).size() + 1));

			// 根据字段位置，获取指定行的单元格
			int cellIndex = fieldJson.getIntValue(ExcelFileTemplet.KEY_INDEX);
			// 创建公式约束
			DataValidationConstraint constraint = new XSSFDataValidationHelper(sheet)
					.createFormulaListConstraint(dataConstraint);

			// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
			CellRangeAddressList regions = new CellRangeAddressList(rowIndex, rowIndex, cellIndex, cellIndex);

			// 数据有效性对象
			sheet.addValidationData(new XSSFDataValidationHelper(sheet).createValidation(constraint, regions));
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>WriteExcelTempletFile.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 枚举单元格的对齐方式（水平对齐方式和垂直对齐方式），并可通过get方法，返回在poi中对应的对齐方式枚举
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年5月22日下午3:48:50
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年5月22日下午3:48:50
	 * </p>
	 *
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum AlignmentType {
		/**
		 * 水平左对齐
		 */
		HORIZONTAL_LEFT(HorizontalAlignment.LEFT, null, (short) 0, "left"),
		/**
		 * 水平右对齐
		 */
		HORIZONTAL_RIGHT(HorizontalAlignment.RIGHT, null, (short) 1, "right"),
		/**
		 * 水平居中对齐
		 */
		HORIZONTAL_MIDDLE(HorizontalAlignment.CENTER, null, (short) 2, "center"),
		/**
		 * 垂直顶部对齐
		 */
		VERTICAL_TOP(null, VerticalAlignment.TOP, (short) 3, "top"),
		/**
		 * 垂直底部对齐
		 */
		VERTICAL_BOTTOM(null, VerticalAlignment.BOTTOM, (short) 4, "bottom"),
		/**
		 * 垂直居中对齐
		 */
		VERTICAL_CENTER(null, VerticalAlignment.CENTER, (short) 5, "middle");

		/**
		 * 水平对齐方式枚举
		 */
		HorizontalAlignment horizontal;
		/**
		 * 垂直对齐方式枚举
		 */
		VerticalAlignment vertical;
		/**
		 * 存储当前枚举的编号，用于识别当前枚举
		 */
		short code;
		/**
		 * 存储当前枚举的名称
		 */
		String name;

		/**
		 * 初始化枚举内容，若非相应的对齐方式，则其存储该方式为null
		 *
		 * @param type       对齐方式的名称
		 * @param horizontal 水平对齐方式枚举
		 * @param vertical   垂直对齐方式枚举
		 */
		private AlignmentType(HorizontalAlignment horizontal, VerticalAlignment vertical, short code, String name) {
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.code = code;
			this.name = name;
		}

		/**
		 * 返回在POI中的水平对齐方式{@link HorizontalAlignment}枚举
		 * <p>
		 * 若枚举为设置垂直对齐方式，则该方法返回null
		 * </p>
		 *
		 * @return 水平对齐方式枚举
		 */
		public HorizontalAlignment getHorizontal() {
			return horizontal;
		}

		/**
		 * 用于返回当前枚举的编码值
		 *
		 * @return 枚举的编码值
		 */
		public short getCode() {
			return code;
		}

		/**
		 * 返回在POI中的垂直对齐方式{@link VerticalAlignment}枚举
		 * <p>
		 * 若枚举为设置水平对齐方式，则该方法返回null
		 * </p>
		 *
		 * @return 垂直对齐方式枚举
		 */
		public VerticalAlignment getVertical() {
			return vertical;
		}

		/**
		 * 返回枚举的名称
		 *
		 * @return 枚举名称
		 */
		public String getName() {
			return name;
		}

		/**
		 * 根据枚举的编码，识别枚举，并进行返回
		 * <p>
		 * 若无法查到与之匹配的编码，则返回null
		 * </p>
		 *
		 * @param code 枚举编码
		 * @return 对齐方式枚举
		 */
		public static AlignmentType getAlignmentType(short code) {
			for (AlignmentType type : values()) {
				if (code == type.code) {
					return type;
				}
			}

			return null;
		}

		/**
		 * 用于根据枚举名称，识别枚举，并进行返回
		 * <p>
		 * 若无法查到与之匹配的编码，则返回null
		 * </p>
		 *
		 * @param name 枚举名称
		 * @return 对齐方式枚举
		 */
		public static AlignmentType getAlignmentType(String name) {
			for (AlignmentType type : values()) {
				if (type.name.equals(name)) {
					return type;
				}
			}

			return null;
		}
	}
}
