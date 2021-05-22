package com.auxiliary.tool.file.excel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.MarkColorsType;
import com.auxiliary.tool.file.MarkComment;
import com.auxiliary.tool.file.MarkFieldBackground;
import com.auxiliary.tool.file.MarkTextColor;
import com.auxiliary.tool.file.MarkTextFont;
import com.auxiliary.tool.file.MarkTextLink;
import com.auxiliary.tool.file.WriteFileException;
import com.auxiliary.tool.file.WriteFilePage;
import com.auxiliary.tool.file.WriteTempletFile;

public abstract class WriteExcelTempletFile<T extends WriteExcelTempletFile<T>>
		extends WriteTempletFile<WriteExcelTempletFile<T>> implements MarkComment<WriteExcelTempletFile<T>>,
		MarkFieldBackground<WriteExcelTempletFile<T>>, MarkTextColor<WriteExcelTempletFile<T>>,
		MarkTextFont<WriteExcelTempletFile<T>>, MarkTextLink<WriteExcelTempletFile<T>>, WriteFilePage {
	/**
	 * 存储每个Sheet对应的模板
	 */
	HashMap<String, FileTemplet> sheetTempletMap = new HashMap<>();

	/**
	 * 构造Excel写入类，并设置一个Sheet页的模板及相应的名称
	 * 
	 * @param templetName 模板名称
	 * @param templet     模板类
	 */
	public WriteExcelTempletFile(String templetName, FileTemplet templet) {
		super(templet);
		addTempletName(templetName, templet);
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
				String name = "Sheet" + (i + 1);
				temp.addTempletAttribute(KEY_NAME, name);
				sheetTempletMap.put(name, temp);
			} else {
				Object obj = temp.getTempletAttribute(KEY_NAME);
				if (obj instanceof String) {
					sheetTempletMap.put(temp.getTempletAttribute(KEY_NAME).toString(), temp);
				} else {
					String name = "Sheet" + (i + 1);
					temp.addTempletAttribute(KEY_NAME, name);
					sheetTempletMap.put(name, temp);
				}
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
	public void addTempletName(String name, FileTemplet templet) {
		WriteFilePage.super.addTempletName(name, templet);
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
		public static final String KEY_OPTION = "option";

		public ExcelFileTemplet(File saveFile) {
			super(saveFile);

			// 设置默认属性
			setFreeze(1, 0);
			setFiltrate(true);
		}

		public ExcelFileTemplet(String templetJsonText) {
			super(templetJsonText);
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
		public ExcelFileTemplet setHorizontalAlignment(String field, AlignmentType alignmentType) {
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
			addTempletAttribute(KEY_FREEZE_TOP, String.valueOf(topIndex > 0 ? topIndex : 0));
			addTempletAttribute(KEY_FREEZE_LEFT, String.valueOf(leftIndex > 0 ? leftIndex : 0));

			return this;
		}

		/**
		 * 用于设置是否在标题上加入筛选按钮，默认则存在筛选按钮
		 * @param isFiltrate 是否添加筛选
		 * @return 类本身
		 */
		public ExcelFileTemplet setFiltrate(boolean isFiltrate) {
			addTempletAttribute(KEY_FILTRATE, String.valueOf(isFiltrate));
			return this;
		}
		
		/**
		 * 用于添加数据选项
		 * @param optionList
		 * @return
		 */
		public ExcelFileTemplet addDataOption(List<String> optionList) {
			addTempletAttribute(KEY_OPTION, JSONArray.parseArray(optionList.toString()));
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
		HORIZONTAL_LEFT(HorizontalAlignment.LEFT, null, (short) 0),
		/**
		 * 水平右对齐
		 */
		HORIZONTAL_RIGHT(HorizontalAlignment.RIGHT, null, (short) 1),
		/**
		 * 水平居中对齐
		 */
		HORIZONTAL_CENTER(HorizontalAlignment.CENTER, null, (short) 2),
		/**
		 * 垂直顶部对齐
		 */
		VERTICAL_TOP(null, VerticalAlignment.TOP, (short) 3),
		/**
		 * 垂直底部对齐
		 */
		VERTICAL_BOTTOM(null, VerticalAlignment.BOTTOM, (short) 4),
		/**
		 * 垂直居中对齐
		 */
		VERTICAL_CENTER(null, VerticalAlignment.CENTER, (short) 5);

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
		 * 初始化枚举内容，若非相应的对齐方式，则其存储该方式为null
		 * 
		 * @param type       对齐方式的名称
		 * @param horizontal 水平对齐方式枚举
		 * @param vertical   垂直对齐方式枚举
		 */
		private AlignmentType(HorizontalAlignment horizontal, VerticalAlignment vertical, short code) {
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.code = code;
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
		 * 根据枚举的编码，识别枚举值，并进行返回
		 * <p>
		 * 若无法查到与之匹配的编码，则返回null
		 * </p>
		 * 
		 * @param code 枚举编码
		 * @return 对齐方式枚举
		 */
		public AlignmentType getAlignmentType(short code) {
			for (AlignmentType type : values()) {
				if (code == type.code) {
					return type;
				}
			}

			return null;
		}
	}
}
