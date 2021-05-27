package com.auxiliary.tool.file.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.dom4j.Document;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.data.TableData;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.MarkColorsType;
import com.auxiliary.tool.file.MarkComment;
import com.auxiliary.tool.file.MarkFieldBackground;
import com.auxiliary.tool.file.MarkTextColor;
import com.auxiliary.tool.file.MarkTextFont;
import com.auxiliary.tool.file.MarkTextLink;
import com.auxiliary.tool.file.TableFileReadUtil;
import com.auxiliary.tool.file.WriteFileException;
import com.auxiliary.tool.file.WriteFilePage;
import com.auxiliary.tool.file.WriteTempletFile;

/**
 * <p><b>文件名：</b>WriteExcelTempletFile.java</p>
 * <p><b>用途：</b>
 * 提供用于对excel数据型的内容进行编写的方法
 * </p>
 * <p><b>编码时间：</b>2021年5月27日上午8:09:29</p>
 * <p><b>修改时间：</b>2021年5月27日上午8:09:29</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public abstract class WriteExcelTempletFile<T extends WriteExcelTempletFile<T>>
		extends WriteTempletFile<WriteExcelTempletFile<T>> implements MarkComment<WriteExcelTempletFile<T>>,
		MarkFieldBackground<WriteExcelTempletFile<T>>, MarkTextColor<WriteExcelTempletFile<T>>,
		MarkTextFont<WriteExcelTempletFile<T>>, MarkTextLink<WriteExcelTempletFile<T>>, WriteFilePage {
	protected final String DEFAULT_NAME = "Sheet";

	/**
	 * 存储每个Sheet对应的模板
	 */
	protected HashMap<String, FileTemplet> sheetTempletMap = new HashMap<>();

	/**
	 * 构造Excel写入类，并设置一个Sheet页的模板及相应的名称
	 * 
	 * @param templetName 模板名称
	 * @param templet     模板类
	 */
	public WriteExcelTempletFile(String templetName, FileTemplet templet) {
		super(templet);
		addTemplet(templetName, templet);
	}

	/**
	 * 根据已有的写入类对象，构造新的写入类对象，并保存原写入类对象中的模板、内容、字段默认内容以及词语替换内容
	 * <p>
	 * <b>注意：</b>在转换模板时，若模板的name字段为对象，则以默认名称“Sheet + 序号”来命名，并修改其中的name字段值
	 * </p>
	 * 
	 * @param writeTempletFile 文件写入类对象
	 * @throws WriteFileException 文件写入类对象为空时，抛出的异常
	 */
	public WriteExcelTempletFile(WriteTempletFile<? extends WriteTempletFile<?>> writeTempletFile) {
		super(writeTempletFile);

		// 拉取WriteTempletFile类的模板内容
		JSONArray tempJsonList = JSONObject.parseObject(writeTempletFile.toTempletJson()).getJSONArray(KEY_TEMPLET);
		// 循环，读取所有的模板，并对模板进行转换
		for (int i = 0; i < tempJsonList.size(); i++) {
			FileTemplet temp = new FileTemplet(tempJsonList.getJSONObject(i).toJSONString());

			// 判断模板是否包含"name"属性，不包含，则加入默认名称
			if (!temp.containsAttribute(KEY_NAME)) {
				String name = DEFAULT_NAME + (i + 1);
				temp.addTempletAttribute(KEY_NAME, name);
				sheetTempletMap.put(name, temp);
			} else {
				Object obj = temp.getTempletAttribute(KEY_NAME);
				if (obj instanceof String) {
					sheetTempletMap.put(temp.getTempletAttribute(KEY_NAME).toString(), temp);
				} else {
					String name = DEFAULT_NAME + (i + 1);
					temp.addTempletAttribute(KEY_NAME, name);
					sheetTempletMap.put(name, temp);
				}
			}
		}
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
			addTemplet(sheetName, excel);
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
					excel.setWide(fieldId, Double.valueOf(wide.get()));
				}

				// 存储字段的水平对齐方式
				excel.setAlignment(fieldId, Optional.ofNullable(fieldElement.attributeValue("align"))
						.map(AlignmentType::getAlignmentType).orElse(AlignmentType.HORIZONTAL_LEFT));

				// 存储字段是否分行写入
				excel.setContentBranch(fieldId,
						Integer.valueOf(Optional.ofNullable(fieldElement.attributeValue("row_text"))
								.filter(f -> !f.isEmpty()).filter(f -> f.matches("\\d+")).orElse("0")));

				// 存储字段是否自动编号
				excel.setAutoSerialNumber(fieldId,
						Boolean.valueOf(Optional.ofNullable(fieldElement.attributeValue("index"))
								.filter(f -> !f.isEmpty()).filter(f -> f.matches("(true)|(false)")).orElse("false")));
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
						ArrayList<Optional<String>> dataList = tableData.getColumnList(columnName);
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
		}
	}

	@Override
	public void write(int caseStartIndex, int caseEndIndex) {
	}

	@Override
	public void switchPage(String name) {
		// 判断名称是否为空、存在
		if (Optional.ofNullable(name).filter(n -> !n.isEmpty()).filter(sheetTempletMap::containsKey).isPresent()) {
			this.templet = sheetTempletMap.get(name);
		}
	}

	/**
	 * 添加Sheet页模板，并设置模板的名称
	 * <p>
	 * 每一个模板表示写入Excel时的一个Sheet页，其模板的名称即为Sheet页的名称。若重复添加同一个名称，则会覆盖上一次设置的模板。
	 * </p>
	 */
	@Override
	public void addTemplet(String name, FileTemplet templet) {
		WriteFilePage.super.addTemplet(name, templet);
		sheetTempletMap.put(name, templet);
	}

	@Override
	public WriteExcelTempletFile<T> textLink(String field, int textIndex, String likeContent) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> fieldLink(String field, String likeContent) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> bold(String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> italic(String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> underline(String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> changeTextColor(MarkColorsType markColorsType, String field, int... textIndexs) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> changeCaseBackground(MarkColorsType markColorsType) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> changeFieldBackground(String field, MarkColorsType markColorsType) {
		return null;
	}

	@Override
	public WriteExcelTempletFile<T> fieldComment(String field, String commentText) {
		return null;
	}

	@Override
	protected List<String> getAllTempletJson() {
		return sheetTempletMap.values().stream().map(FileTemplet::getTempletJson).collect(Collectors.toList());
	}

	@Override
	protected void createTempletFile() {
		// TODO 编写创建文件逻辑
		sheetTempletMap.forEach((name, templet) -> {

		});
	}

	/**
	 * <p>
	 * <b>文件名：</b>WriteExcelTempletFile.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 定义写入Excel专用的模板
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年5月22日下午3:02:44
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年5月22日下午3:02:44
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public class ExcelFileTemplet extends FileTemplet {
		public static final String KEY_NAME = "name";
		public static final String KEY_WIDE = "wide";
		public static final String KEY_HIGH = "high";
		public static final String KEY_HORIZONTAL = "horizontal";
		public static final String KEY_VERTICAL = "vertical";
		public static final String KEY_ROW_TEXT = "rowText";
		public static final String KEY_INDEX = "index";
		public static final String KEY_FREEZE_TOP = "freezeTop";
		public static final String KEY_FREEZE_LEFT = "freezeLeft";
		public static final String KEY_FILTRATE = "filtrate";
		public static final String KEY_DATA = "data";

		public ExcelFileTemplet(File saveFile) {
			super(saveFile);

			// 设置默认属性
			setFreeze(1, 0);
			// 设置打开筛选
			setFiltrate(true);

			// 初始化数据有效性内容
			addTempletAttribute(KEY_DATA, new JSONObject());
		}

		public ExcelFileTemplet(String templetJsonText) {
			super(templetJsonText);
			// 判断模板是否包含冻结表格信息
			setFreeze(Optional.ofNullable(templetJson.getInteger(KEY_FREEZE_TOP)).orElse(1),
					Optional.ofNullable(templetJson.getInteger(KEY_FREEZE_LEFT)).orElse(0));
			// 判断模板是否存在筛选信息
			setFiltrate(Optional.ofNullable(templetJson.getBooleanValue(KEY_FILTRATE)).orElse(true));
			// 判断模板是否包含存储数据有效性的字段
			if (containsAttribute(KEY_DATA)) {
				addTempletAttribute(KEY_DATA, new JSONObject());
			}
		}

		/**
		 * 用于在模板中添加标题以及写入到excel中标题的名称
		 * <p>
		 * <b>注意：</b>调用该方法后，若字段不存在，则向模板中添加指定的字段；若标题名称为空，则已字段名称进行命名
		 * </p>
		 * 
		 * @param field 字段
		 * @param name  对应字段写入到模板中的名称
		 * @return 类本身
		 */
		public ExcelFileTemplet addTitle(String field, String name) {
			if (!containsField(field)) {
				addField(field);
			}

			addFieldAttribute(field, KEY_NAME, Optional.ofNullable(name).filter(n -> !n.isEmpty()).orElse(field));
			return this;
		}

		/**
		 * 用于在模板中添加字段指向的单元格的宽度
		 * 
		 * @param field 字段
		 * @param wide  宽度
		 * @return 类本身
		 */
		public ExcelFileTemplet setWide(String field, double wide) {
			if (wide > 0 && containsField(field)) {
				addFieldAttribute(field, KEY_WIDE, String.valueOf(wide));
			}

			return this;
		}

		/**
		 * 用于添加字段指向的单元格的高度
		 * 
		 * @param field 字段
		 * @param wide  高度
		 * @return 类本身
		 */
		public ExcelFileTemplet setHigh(String field, double wide) {
			if (wide > 0 && containsField(field)) {
				addFieldAttribute(field, KEY_HIGH, String.valueOf(wide));
			}

			return this;
		}

		/**
		 * 用于存储字段指向的单元格的对齐方式
		 * <p>
		 * 对齐方式包括水平对齐与垂直对齐，通过{@link AlignmentType}枚举可兼容两者的设置，但相同的对齐方式只存在一种。例如：
		 * <code><pre>
		 * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_CENTER);
		 * test.addHorizontalAlignment("title", AlignmentType.VERTICAL_CENTER)
		 * </pre></code> 以上代码表示设置“title”字段的对齐方式为水平居中和垂直居中对齐；而以下代码： <code><pre>
		 * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_CENTER);
		 * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_LEFT)
		 * </pre></code> 表示设置“title”字段的对齐方式为水平左对齐（取最后一次设置的内容）
		 * </p>
		 * 
		 * @param field         字段
		 * @param alignmentType 对齐方式枚举
		 * @return 类本身
		 */
		public ExcelFileTemplet setAlignment(String field, AlignmentType alignmentType) {
			// 判断字段是否存在，且是否传入指定的枚举
			if (!containsField(field) || alignmentType == null) {
				return this;
			}

			// 判断当前传入的对齐方式
			String key = "";
			if (alignmentType.getHorizontal() != null) {
				key = KEY_HORIZONTAL;
			} else {
				key = KEY_VERTICAL;
			}

			addFieldAttribute(field, key, String.valueOf(alignmentType.code));
			return this;
		}

		/**
		 * 用于设置字段内容分行写入的段落数
		 * <p>
		 * 当添加的内容段落达到指定的段落数时，则分成多个单元格写入。
		 * </p>
		 * 
		 * @param field        字段
		 * @param paragraphNum 分行段落数
		 * @return 类本身
		 */
		public ExcelFileTemplet setContentBranch(String field, int paragraphNum) {
			// 判断字段是否存在
			if (paragraphNum > 0 && containsField(field)) {
				addFieldAttribute(field, KEY_ROW_TEXT, String.valueOf(paragraphNum));
			}

			return this;
		}

		/**
		 * 用于设置字段段落内容是否自动编号
		 * <p>
		 * 设置自动编号后，当写入文件时，将在每段内容前，加上“序号 + .”的内容
		 * </p>
		 * 
		 * @param field  字段
		 * @param isAuto 是否自动编号
		 * @return 类本身
		 */
		public ExcelFileTemplet setAutoSerialNumber(String field, boolean isAuto) {
			// 判断字段是否存在
			if (containsField(field)) {
				addFieldAttribute(field, KEY_INDEX, String.valueOf(isAuto));
			}

			return this;
		}

		/**
		 * 用于设置需要冻结的单元格
		 * <p>
		 * 可设置自顶部开始以及自左边开始需要冻结的表格行（列）数，当值设置为0时，则表示不冻结。默认情况下，顶部冻结1行，左边不冻结
		 * </p>
		 * 
		 * @param topIndex  顶部冻结行数
		 * @param leftIndex 左边冻结列数
		 * @return 类本身
		 */
		public ExcelFileTemplet setFreeze(int topIndex, int leftIndex) {
			addTempletAttribute(KEY_FREEZE_TOP, topIndex > 0 ? topIndex : 0);
			addTempletAttribute(KEY_FREEZE_LEFT, leftIndex > 0 ? leftIndex : 0);

			return this;
		}

		/**
		 * 用于设置是否在标题上加入筛选按钮，默认则存在筛选按钮
		 * 
		 * @param isFiltrate 是否添加筛选
		 * @return 类本身
		 */
		public ExcelFileTemplet setFiltrate(boolean isFiltrate) {
			addTempletAttribute(KEY_FILTRATE, isFiltrate);
			return this;
		}

		/**
		 * 用于添加数据选项
		 * 
		 * @param optionList 数据集
		 * @return 类本身
		 */
		public ExcelFileTemplet addDataOption(String name, List<String> optionList) {
			// 判空
			if (!Optional.ofNullable(name).filter(n -> !n.isEmpty()).isPresent()) {
				return this;
			}
			if (!Optional.ofNullable(optionList).filter(l -> !l.isEmpty()).isPresent()) {
				return this;
			}

			// 获取指定名称的数据选项json，若不存在，则添加空json
			JSONArray optionListJson = Optional.ofNullable(templetJson.getJSONObject(KEY_DATA).getJSONArray(name))
					.orElse(new JSONArray());
			optionListJson.addAll(optionList);

			// 将数据选项附加到模板json上
			templetJson.getJSONObject(KEY_DATA).put(name, optionListJson);
			return this;
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
		 * @param code 枚举名称
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
