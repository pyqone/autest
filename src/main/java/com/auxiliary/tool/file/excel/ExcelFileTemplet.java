package com.auxiliary.tool.file.excel;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.excel.WriteExcelTempletFile.AlignmentType;

/**
 * <p>
 * <b>文件名：</b>ExcelFileTemplet.java
 * </p>
 * <p>
 * <b>用途：</b> 指定Excel相关的模板
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月29日下午8:32:09
 * </p>
 * <p>
 * <b>修改时间：</b>2022年10月19日 上午8:13:53
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class ExcelFileTemplet extends FileTemplet {
    /**
     * 标记json中的name字段
     */
	public static final String KEY_NAME = "name";
    /**
     * 标记json中的wide字段
     */
	public static final String KEY_WIDE = "wide";
    /**
     * 标记json中的high字段
     */
	public static final String KEY_HIGH = "high";
    /**
     * 标记json中的horizontal字段
     */
	public static final String KEY_HORIZONTAL = "horizontal";
    /**
     * 标记json中的vertical字段
     */
	public static final String KEY_VERTICAL = "vertical";
    /**
     * 标记json中的rowText字段
     */
	public static final String KEY_ROW_TEXT = "rowText";
    /**
     * 标记json中的autoIndex字段
     */
	public static final String KEY_AUTO_NUMBER = "autoIndex";
    /**
     * 标记json中的freezeTop字段
     */
	public static final String KEY_FREEZE_TOP = "freezeTop";
    /**
     * 标记json中的freezeLeft字段
     */
	public static final String KEY_FREEZE_LEFT = "freezeLeft";
    /**
     * 标记json中的filtrate字段
     */
	public static final String KEY_FILTRATE = "filtrate";
    /**
     * 标记json中的data字段
     */
	public static final String KEY_DATA = "data";

    /**
     * 用于缓存当前模板中字段集合转换成的字段数组
     */
    protected String[] fields = null;

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
		if (!containsAttribute(KEY_DATA)) {
			addTempletAttribute(KEY_DATA, new JSONObject());
		}

		// 检查字段是否有名称，不存在名称，则默认以字段名称命名
		JSONObject fieldJson = templetJson.getJSONObject(KEY_FIELD);
		fieldJson.keySet().forEach(key -> {
			JSONObject json = fieldJson.getJSONObject(key);
			if (!json.containsKey(KEY_NAME)) {
				json.put(KEY_NAME, key);
			}
		});
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
     * @deprecated 该方法已由{@link #setWide(double, String...)}方法代替，将在3.8.0或后续版本中删除
     */
    @Deprecated
    public ExcelFileTemplet setWide(String field, double wide) {
        return setWide(wide, field);
	}

    /**
     * 用于在模板中添加指定多个字段指向的单元格的宽度
     * <p>
     * <b>注意：</b>若不传入字段组或字段组为null时，则
     * </p>
     * 
     * @param wide   宽度
     * @param fields 字段组
     * @return 类本身
     * @since autest 3.7.0
     */
    public ExcelFileTemplet setWide(double wide, String... fields) {
        if (wide > 0) {
            // 遍历所有字段，为所有字段加上相应的属性，若传入的字段组为空或为null时，则将所有的字段均加上该属性
            fields = Optional.ofNullable(fields).filter(arr -> arr.length != 0).orElseGet(() -> getFields());
            for (String field : fields) {
                addFieldAttribute(field, KEY_WIDE, String.valueOf(wide));
            }
        }

        return this;
    }

	/**
	 * 用于设置全局单元格的高度
	 * 
	 * @param high 高度
	 * @return 类本身
	 */
	public ExcelFileTemplet setHigh(double high) {
		if (high > 0) {
			addTempletAttribute(KEY_HIGH, high);
		}

		return this;
	}

	/**
     * 用于存储字段指向的单元格的对齐方式
     * <p>
     * 对齐方式包括水平对齐与垂直对齐，通过{@link AlignmentType}枚举可兼容两者的设置，但相同的对齐方式只存在一种。例如：
     * <code><pre>
     * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_CENTER);
     * test.addHorizontalAlignment("title", AlignmentType.VERTICAL_CENTER);
     * </pre></code> 以上代码表示设置“title”字段的对齐方式为水平居中和垂直居中对齐；而以下代码： <code><pre>
     * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_CENTER);
     * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_LEFT);
     * </pre></code> 表示设置“title”字段的对齐方式为水平左对齐（取最后一次设置的内容）
     * </p>
     * 
     * @param field         字段
     * @param alignmentType 对齐方式枚举
     * @return 类本身
     * @deprecated 该方法已由{@link #setAlignment(AlignmentType, String...)}方法代替，将在3.8.0或后续版本中删除
     */
	@Deprecated
    public ExcelFileTemplet setAlignment(String field, AlignmentType alignmentType) {
        return setAlignment(alignmentType, field);
	}

    /**
     * 用于设置字段指向的单元格的对齐方式
     * <p>
     * 对齐方式包括水平对齐与垂直对齐，通过{@link AlignmentType}枚举可兼容两者的设置，但相同的对齐方式只存在一种。例如：
     * <code><pre>
     * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_CENTER);
     * test.addHorizontalAlignment("title", AlignmentType.VERTICAL_CENTER);
     * </pre></code> 以上代码表示设置“title”字段的对齐方式为水平居中和垂直居中对齐；而以下代码： <code><pre>
     * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_CENTER);
     * test.addHorizontalAlignment("title", AlignmentType.HORIZONTAL_LEFT);
     * </pre></code> 表示设置“title”字段的对齐方式为水平左对齐（取最后一次设置的内容）
     * </p>
     * <p>
     * <b>注意：</b>若不传入字段组或字段组为null时，则
     * </p>
     * 
     * @param alignmentType 对齐方式枚举
     * @param fields        字段组
     * @return 类本身
     * @since autest 3.7.0
     */
    public ExcelFileTemplet setAlignment(AlignmentType alignmentType, String... fields) {
        // 判断字段是否存在，且是否传入指定的枚举
        if (alignmentType == null) {
            return this;
        }

        // 遍历所有字段，为所有字段加上相应的属性，若传入的字段组为空或为null时，则将所有的字段均加上该属性
        fields = Optional.ofNullable(fields).filter(arr -> arr.length != 0).orElseGet(() -> getFields());
        for (String field : fields) {
            addFieldAttribute(field, (alignmentType.getHorizontal() != null ? KEY_HORIZONTAL : KEY_VERTICAL),
                    String.valueOf(alignmentType.code));
        }
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
     * @deprecated 该方法已由{@link #setContentBranch(int, String...)}方法代替，将在3.8.0或后续版本中删除
     */
	@Deprecated
    public ExcelFileTemplet setContentBranch(String field, int paragraphNum) {
        return setContentBranch(paragraphNum, field);
	}

    /**
     * 用于设置字段内容分行写入的段落数
     * <p>
     * 当添加的内容段落达到指定的段落数时，则分成多个单元格写入。
     * </p>
     * <p>
     * <b>注意：</b>若不传入字段组或字段组为null时，则
     * </p>
     * 
     * @param paragraphNum 分行段落数
     * @param fields       字段组
     * @return 类本身
     * @since autest 3.7.0
     */
    public ExcelFileTemplet setContentBranch(int paragraphNum, String... fields) {
        // 判断字段是否存在
        if (paragraphNum > 0) {
            // 遍历所有字段，为所有字段加上相应的属性，若传入的字段组为空或为null时，则将所有的字段均加上该属性
            fields = Optional.ofNullable(fields).filter(arr -> arr.length != 0).orElseGet(() -> getFields());
            for (String field : fields) {
                addFieldAttribute(field, KEY_ROW_TEXT, String.valueOf(paragraphNum));
            }
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
     * @deprecated {@link #setAutoSerialNumber(boolean, String...)}方法代替，将在3.8.0或后续版本中删除
     */
	@Deprecated
    public ExcelFileTemplet setAutoSerialNumber(String field, boolean isAuto) {
        return setAutoSerialNumber(isAuto, field);
	}

    /**
     * 用于设置字段段落内容是否自动编号
     * <p>
     * 设置自动编号后，当写入文件时，将在每段内容前，加上“序号 + .”的内容
     * </p>
     * <p>
     * <b>注意：</b>若不传入字段组或字段组为null时，则
     * </p>
     * 
     * @param isAuto 是否自动编号
     * @param fields 字段组
     * @return 类本身
     * @since autest 3.7.0
     */
    public ExcelFileTemplet setAutoSerialNumber(boolean isAuto, String... fields) {
        // 遍历所有字段，为所有字段加上相应的属性，若传入的字段组为空或为null时，则将所有的字段均加上该属性
        fields = Optional.ofNullable(fields).filter(arr -> arr.length != 0).orElseGet(() -> getFields());
        for (String field : fields) {
            addFieldAttribute(field, KEY_AUTO_NUMBER, String.valueOf(isAuto));
        }

        return this;
    }

    /**
     * 该方法用于对指定的标题行添加边框
     * <p>
     * <b>注意：</b>若不传入字段组或字段组为null时，则
     * </p>
     * 
     * @param borderStyle 边框样式枚举
     * @param fields      字段组
     * @return 类本身
     * @since autest 3.7.0
     */
    public ExcelFileTemplet setTitleBorder(BorderStyle borderStyle, String... fields) {
        // 当颜举不为null时则对标题进行添加
        if (borderStyle != null) {
            // 遍历所有字段，为所有字段加上相应的属性，若传入的字段组为空或为null时，则将所有的字段均加上该属性
            fields = Optional.ofNullable(fields).filter(arr -> arr.length != 0).orElseGet(() -> getFields());
            // 获取所有字段，并将所有字段加上边框
            for (String field : fields) {
                if (containsField(field)) {
                    addFieldAttribute(field, ExcelCommonJsonField.KEY_BORDER,
                            ExcelCommonJsonField.getBorderJson(borderStyle));
                }
            }
        }
        return this;
    }

    /**
     * 该方法用于对指定的标题行添加背景颜色
     * <p>
     * <b>注意：</b>若不传入字段组或字段组为null时，则
     * </p>
     * 
     * @param indexedColors 颜色枚举
     * @param fields        字段组
     * @return 类本身
     * @since autest 3.7.0
     */
    public ExcelFileTemplet setTitleBackground(IndexedColors indexedColors, String... fields) {
        if (indexedColors != null) {
            // 遍历所有字段，为所有字段加上相应的属性，若传入的字段组为空或为null时，则将所有的字段均加上该属性
            fields = Optional.ofNullable(fields).filter(arr -> arr.length != 0).orElseGet(() -> getFields());
            for (String field : fields) {
                if (containsField(field)) {
                    addFieldAttribute(field, ExcelCommonJsonField.KEY_BACKGROUND, indexedColors.index);
                }
            }
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
	 * 用于设置模板的名称
	 * 
	 * @param name 模板名称
	 * @return 类本身
	 */
	public ExcelFileTemplet setName(String name) {
		addTempletAttribute(KEY_NAME, name);
		return this;
	}

	/**
	 * 用于添加数据选项
	 * 
	 * @param field      字段名称
	 * @param optionList 数据集
	 * @return 类本身
	 */
	public ExcelFileTemplet addDataOption(String field, List<String> optionList) {
		// 判空
		if (!Optional.ofNullable(field).filter(n -> !n.isEmpty()).isPresent()) {
			return this;
		}
		if (!Optional.ofNullable(optionList).filter(l -> !l.isEmpty()).isPresent()) {
			return this;
		}

		// 获取指定名称的数据选项json，若不存在，则添加空json
		JSONArray optionListJson = Optional.ofNullable(templetJson.getJSONObject(KEY_DATA).getJSONArray(field))
				.orElse(new JSONArray());
		optionListJson.addAll(optionList);

		// 将数据选项附加到模板json上
		templetJson.getJSONObject(KEY_DATA).put(field, optionListJson);
		return this;
	}

    /**
     * 该方法用于判断当前模板的所有字段是否被转换成字段数组，若未转换，则将字段集合转化成字段数组进行存储，并将其返回
     * 
     * @return 字段数组
     * @since autest 3.7.0
     */
    protected String[] getFields() {
        // 若fields为null，则对当前模板所有字段进行一次获取，并转换成字段数组
        if (fields == null) {
            fields = getFieldList().toArray(new String[] {});
        }

        return fields;
    }
}