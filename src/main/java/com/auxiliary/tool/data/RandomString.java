package com.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.tool.common.DisposeCodeUtils;

/**
 * <p>
 * <b>文件名：</b>RandomString.java
 * </p>
 * <p>
 * <b>用途：</b> 提供根据字符串池（产生随机字符串的范围）中添加模型，返回相应的随机字符串的方法。
 * </p>
 * <p>
 * <b>编码时间：</b>2019年2月13日下午19:53:01
 * </p>
 * <p>
 * <b>修改时间：</b>2022年7月19日 上午8:16:26
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver 1.20
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class RandomString {
    /**
     * 定义种子扩展起始标识
     */
    private String SEED_EXPAND_START = "[";
    /**
     * 定义种子扩展结束标识
     */
    private String SEED_EXPAND_END = "]";
    /**
     * 定义种子扩展转义字符
     */
    private String SEED_EXPAND_TRANSFERRED_MEANING = "\\";

	/**
	 * 字符串池
	 */
	private StringBuilder stringSeed = new StringBuilder("");
	/**
	 * 控制产生的随机字符串是否允许有重复字符的开关
	 */
	private boolean repeat = true;

	/**
     * 指定通过{@link #toString()}方法默认输出的字符串长度
     * 
     * @deprecated 该属性以作废，默认属性将由
     */
	@Deprecated
    public static int defaultLength = 6;

	/**
	 * 控制在需要生成的字符串大于字符串池时的处理方式
	 */
	private RepeatDisposeType dispose = RepeatDisposeType.DISPOSE_REPEAT;

    /**
     * 用于存储扩展判定正则表达式
     */
    private ArrayList<DataDriverFunction> expandRegexList = new ArrayList<>();

    /**
     * 默认随机字符串最小长度
     */
    private int defaultMinLength = 6;
    /**
     * 默认随机字符串最大长度
     */
    private int defaultMaxLength = 6;

    /**
     * 初始化字符串池，使其不包含任何元素
     */
    public RandomString() {
        String str = "[0-9a-zA-Z]";
        // 添加按照字母或数字顺序生成模型的方法
        expandRegexList.add(new DataDriverFunction(String.format("%s-%s", str, str), text -> {
            String[] dataTexts = text.split("-");
            return PresetString.createOrderlyText(dataTexts[0], dataTexts[1], 1);
        }));

        // 添加按照字母顺序，根据步长生成模型的方法
        expandRegexList.add(new DataDriverFunction(String.format("%s~%s~%s", str, str, "\\d"), text -> {
            String[] dataTexts = text.split("~");
            return PresetString.createOrderlyText(dataTexts[0], dataTexts[1], Integer.valueOf(dataTexts[2]));
        }));

        // 添加生成指定个数的重复字符的方法
        expandRegexList.add(new DataDriverFunction(".\\*\\d+", text -> {
            String[] dataTexts = text.split("\\*");
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < Integer.valueOf(dataTexts[1]); i++) {
                newText.append(dataTexts[0]);
            }

            return newText.toString();
        }));

        // 添加生成指定个数的中文方法
        expandRegexList.add(new DataDriverFunction("(c|C)(h|H)\\*\\d+", text -> {
            // 从第4位开始截取，“CH*”直接抛弃
            return StringMode.CH.getSeed().substring(0, Integer.valueOf(text.substring(3, text.length())));
        }));
	}

	/**
	 * 通过预设模型模型对字符串池进行初始化
	 * 
	 * @param stringModes 需要加入字符串池中的模型（为{@link StringMode}枚举对象）
	 */
	public RandomString(StringMode... stringModes) {
        this();
		addMode(stringModes);
	}

    /**
     * 通过预设模型模型对字符串池进行初始化，并初始化默认输出的字符串长度
     * 
     * @param defaultMinLength 随机字符串默认最小范围
     * @param defaultMaxLength 随机字符串默认最大范围
     * @param stringModes      需要加入字符串池中的模型（为{@link StringMode}枚举对象）
     * @since autest 4.3.0
     */
    public RandomString(int defaultMinLength, int defaultMaxLength, StringMode... stringModes) {
        this(stringModes);
        setRandomStringDefaultLength(defaultMinLength, defaultMaxLength);
    }

	/**
	 * 通过自定义的字符串对字符串池进行初始化
	 * 
	 * @param mode 需要加入字符串池中的模型
	 */
	public RandomString(String mode) {
        this();
		addMode(mode);
	}

    /**
     * 通过自定义的字符串对字符串池进行初始化，并初始化默认输出的字符串长度
     * 
     * @param defaultMinLength 随机字符串默认最小范围
     * @param defaultMaxLength 随机字符串默认最大范围
     * @param mode             需要加入字符串池中的模型
     * @since autest 4.3.0
     */
    public RandomString(int defaultMinLength, int defaultMaxLength, String mode) {
        this(mode);
        setRandomStringDefaultLength(defaultMinLength, defaultMaxLength);
    }

    /**
     * 该方法用于设置默认输出的随机字符串的长度范围，生成的随机字符串长度在长度范围中随机取值，若两值一致，则每次生成同样长度的随机字符串
     * 
     * @param defaultMinLength 随机字符串默认最小范围
     * @param defaultMaxLength 随机字符串默认最大范围
     * @return 类本身
     * @since autest 4.3.0
     * @see #toString()
     */
    public RandomString setRandomStringDefaultLength(int defaultMinLength, int defaultMaxLength) {
        // 若最大、最小值存在小于1的情况，则将其赋予1
        if (defaultMinLength < 1) {
            defaultMinLength = 1;
        }
        if (defaultMaxLength < 1) {
            defaultMaxLength = 1;
        }
        // 若最大值小于最小值，则对其进行交换
        if (defaultMaxLength < defaultMinLength) {
            int temp = defaultMinLength;
            defaultMinLength = defaultMaxLength;
            defaultMaxLength = temp;
        }

        this.defaultMinLength = defaultMinLength;
        this.defaultMaxLength = defaultMaxLength;

        return this;
    }

    /**
     * 该方法用于返回默认生成随机字符串的最小长度
     * 
     * @return 默认生成随机字符串的最小长度
     * @since autest 4.3.0
     */
    public int getDefaultMinLength() {
        return defaultMinLength;
    }

    /**
     * 该方法用于返回默认生成随机字符串的最大长度
     * 
     * @return 默认生成随机字符串的最大长度
     * @since autest 4.3.0
     */
    public int getDefaultMaxLength() {
        return defaultMaxLength;
    }

    /**
     * 该方法用于添加自定义的扩展方法
     * 
     * @param function 扩展方法
     * @since autest 3.5.0
     */
    public void addExtend(DataDriverFunction function) {
        Optional.ofNullable(function).ifPresent(expandRegexList::add);
    }

	/**
	 * 设置产生的随机字符串中是否允许存在重复的字符，默认为允许重复。
	 * <p>
	 * <b>注意：</b>随机字符串字符的重复是相对于字符串池而言的，若字符串池中本身存在重复的字符，则
	 * 即便设置了不允许出现字符，在生成的随机字符串中也可能包含重复的字符。若要生成不包含重复字符的
	 * 字符串，可调用{@link #removeRepetition()}方法，去除字符串池中重复的字符后，再调用返回字符串 的方法。
	 * </p>
	 * 
	 * @param repeat 是否允许重复
	 */
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	/**
	 * 用于设置需要产生的随机字符串长度大于字符串池长度，且设置了不允许出现重复字符时的处理方式。
	 * <p>
	 * 若需要产生的随机字符串长度大于字符串池长度，则必定会出现重复的字符。若同时调用
	 * {@link #setRepeat(boolean)}方法设置不允许存在重复字符时，则需要对字符串的生成进行一定的处理。
	 * 具体的处理规则，可参照{@link RepeatDisposeType}枚举中的说明
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>若未设置，则按照默认的{@link RepeatDisposeType#DISPOSE_REPEAT}方式进行处理</li>
	 * <li>处理方式为{@link RepeatDisposeType#DISPOSE_REPEAT}时，在处理后，不影响{@link #setRepeat(boolean)}的设置</li>
	 * </ol>
	 * </p>
	 * 
	 * @param repeatDisposeType 处理方式{@link RepeatDisposeType}枚举
	 */
	public void setDispose(RepeatDisposeType repeatDisposeType) {
		this.dispose = repeatDisposeType;
	}

	/**
	 * 用于返回字符串池
	 * 
	 * @return 字符串池
	 */
	public String getStringSeed() {
		return stringSeed.toString();
	}

	/**
	 * 用于向字符串池中添加模型，通过该方法添加的模型将不检查字符串池中是否存在与之重复的元素
	 * 
	 * @param stringModes 字符串模型枚举{@link StringMode}
	 * @return 类本身
	 */
	public RandomString addMode(StringMode... stringModes) {
		return addMode(true, stringModes);
	}

	/**
	 * 用于向字符串池中添加模型，且通过isRepeat进行判断是否允许添加可重复的字符串进入字符串池
	 * 
	 * @param isRepeat    字符串池中的元素是否可重复
	 * @param stringModes 字符串模型枚举{@link StringMode}
	 * 
	 * @return 类本身
	 */
	public RandomString addMode(boolean isRepeat, StringMode... stringModes) {
		StringBuilder joinText = new StringBuilder("");
		Optional.ofNullable(stringModes)
				// 过滤掉无元素的数组
				.filter(arr -> arr.length != 0)
				// 将数组转换为集合，若数组有误，则返回空集合
				.map(Arrays::asList).orElse(new ArrayList<>())
				// 读取集合每一个元素，并返回相应的字符串
				.stream().map(StringMode::getSeed).forEach(joinText::append);

		return addMode(isRepeat, joinText.toString());
	}

	/**
     * 用于向字符串池中添加自定义的字符串，通过该方法添加的字符串将不检查字符串池中是否存在与之重复的元素
     * <p>
     * 字符串模型支持扩展方法，可通过{@link #addExtend(DataDriverFunction)}方法，对字符串扩展方法进行添加，亦可使用默认的扩展方法。其扩展需要使用中括号“[]”
     * 表示，其中括号中的内容为进行识别的扩展内容。默认的扩展方法包括：
     * <ul>
     * <li>按照{@link StringMode}中各个枚举定义的字符顺序，添加字符串模型，如传入“[a-c]”，则生成的字符串模型为“abc”</li>
     * <li>按照{@link StringMode}中各个枚举定义的字符顺序，根据步长生成字符串模型，如传入“[1~9~2]”，则生成的模型为“13579”</li>
     * <li>生成指定个数的字符模型，如传入“[a*3]”，则生成的模型为“aaa”</li>
     * <li>按照{@link StringMode#CH}中定义的汉字顺序，取指定个数的中文字符，如传入“[ch*3]”，则生成的模型为“早晨我”</li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>若
     * </p>
     * 
     * @param mode 需要加入到字符串池中的模型
     * 
     * @return 类本身
     */
	public RandomString addMode(String mode) {
		return addMode(true, mode);
	}

	/**
	 * 用于向字符串池中添加自定义的字符串，且通过isRepeat进行判断是否允许添加可重复的字符串进入字符串池
	 * 
	 * @param isRepeat 字符串池中的元素是否可重复
	 * @param mode     需要加入到字符串池中的模型
	 * 
	 * @return 类本身
	 */
	public RandomString addMode(boolean isRepeat, String mode) {
		joinStringSeed(isRepeat, mode);
		return this;
	}

	/**
	 * 用于清空字符串池的内容
	 */
	public void clear() {
		if (stringSeed.length() != 0) {
			stringSeed.delete(0, stringSeed.length());
		}
	}

	/**
	 * 用于返回字符串池的长度
	 * 
	 * @return 字符串池的长度
	 */
	public int length() {
		return stringSeed.length();
	}

	/**
	 * 用于删除字符串池中指定的字符串
	 * 
	 * @param str 需要删除的字符串
	 */
	public void remove(String str) {
		// 判断StringSeed是否为空，为空则返回false
		if (stringSeed.length() == 0) {
			return;
		}

		// 对待删除的字符串进行过滤
		Optional.ofNullable(str).filter(text -> !text.isEmpty())
				.filter(text -> stringSeed.indexOf(text) > -1)
				.ifPresent(text -> {
					int index = -1;
					// 循环，直到字符串被完全删除为止
					while ((index = stringSeed.indexOf(text)) > -1) {
						remove(index, index + text.length());
					}
				});
	}

	/**
	 * 用于删除字符串池中指定范围的字符串。若两个下标相等，则表示删除指定下标的字符串
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>指定的范围中，删除的元素包括起始位置的元素，但不包括结束位置的元素</li>
	 * <li>在指定的范围不正确时，将有如下的处理方式：
	 * <ul>
	 * <li>若起始下标大于结束下标时，则调换下标位置</li>
	 * <li>若起始下标小于0时：
	 * <ul>
	 * <li>结束下标大于0，则设置起始下标为0</li>
	 * <li>结束下标小于等于0，则返回空串，并不做任何删除</li>
	 * </ul>
	 * </li>
	 * <li>结束下标大于字符串池长度时：
	 * <ul>
	 * <li>起始下标小于字符串池长度，则设置结束下标为字符串池长度</li>
	 * <li>起始下标大于等于字符串池长度，则返回空串，并不做任何删除</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </li>
	 * </ol>
	 * </p>
	 * <p>
	 * 
	 * @param startIndex 表示用户指定范围的起始位置（包括该位置的元素）
	 * @param endIndex   表示用户指定范围的结束位置（不包括该位置的元素）
	 * @return 返回的字符串，若输入的位置不正确，则返回null
	 * @see #remove(int)
	 * @see #remove(String)
	 */
	public String remove(int startIndex, int endIndex) {
		// 判断StringSeed是否为空，为空则返回false
		if (stringSeed.length() == 0) {
			return "";
		}

		// 判断最大生成长度与最小生成长度的关系
		if (startIndex > endIndex) {
			int temp = startIndex;
			startIndex = endIndex;
			endIndex = temp;
		}

		// 判断最小下标是否小于0
		if (startIndex < 0) {
			// 再判断最大下标是否大于0，大于0则将最小下标为1；
			if (endIndex >= 0) {
				startIndex = 0;
			} else {
				return "";
			}
		}

		// 判断最大下标是否大于字符串池长度
		if (endIndex >= stringSeed.length()) {
			// 再判断最小下标是否小于字符串池长度
			if (startIndex < stringSeed.length()) {
				endIndex = stringSeed.length();
			} else {
				return "";
			}
		}
		
		//判断两个下标是否一致
		if (startIndex == endIndex) {
			endIndex += 1;
		}

		String delectStr = stringSeed.substring(startIndex, endIndex);
		// 删除指定位置的字符串
		stringSeed.delete(startIndex, endIndex);

		// 返回被删除的字符串
		return delectStr;
	}

	/**
	 * 用于删除字符串池中指定下标的字符串
	 * 
	 * @param index 表示用户指定位置
	 * @return 返回被删除的字符串
	 * @see #remove(String)
	 * @see #remove(int, int)
	 */
	public String remove(int index) {
		return remove(index, index);
	}

	/**
	 * 用于移除随机字符串生成范围中重复的字符，以达到范围中字符串不会重复的情况
	 */
	public void removeRepetition() {
		// 判断StringSeed是否为空，为空则返回false
		if (stringSeed.length() == 0) {
			return;
		}

		// 用于保存删除重复元素后的字符串
		StringBuilder newStringSeed = new StringBuilder();
		// 读取stringSeed中的内容，过滤掉已存在的字符串
		stringSeed.chars().mapToObj(ch -> String.valueOf((char) ch))
				.filter(text -> newStringSeed.indexOf(text) < 0)
				.forEach(newStringSeed::append);

		// 清空StringSeed
		clear();
		// 将去重后的字符串放入stringSeed中
		stringSeed.append(newStringSeed);
	}

	/**
     * 以默认方式返回生成的随机字符串
     * <p>
     * 默认长度为6个字符，可通过{@link #setRandomStringDefaultLength(int, int)}方法进行修改
     * </p>
     * 
     * @return 默认长度的随机字符串
     * @throws IllegalDataException 当产生字符串不允许重复，且字符串产生范围长度小于默认长度，
     *                              处理方式为{@link RepeatDisposeType#DISPOSE_THROW_EXCEPTION}时抛出的异常
     */
	@Override
	public String toString() {
        return toString(defaultMinLength, defaultMaxLength);
	}

	/**
	 * 生成随机长度随机字符串，其长度范围由用户指定
	 * 
	 * @param minLength 随机长度的最小值
	 * @param maxLength 随机长度的最大值
	 * @return 返回生成的随机字符串
	 * @see #toString()
	 * @see #toString(int)
	 * @throws IllegalDataException 当产生字符串不允许重复，且字符串产生范围长度小于默认长度，
	 *                              处理方式为{@link RepeatDisposeType#DISPOSE_THROW_EXCEPTION}时抛出的异常
	 */
	public String toString(int minLength, int maxLength) {
		// 判断最大生成长度与最小生成长度的关系
		if (minLength > maxLength) {
			int temp = minLength;
			minLength = maxLength;
			maxLength = temp;
		}
		// 判断最少个数是否小于1
		if (minLength < 1) {
			// 再判断最多个数是否大于1，大于1则将最少个数设为1；
			if (maxLength > 1) {
				minLength = 1;
			} else {
				// 若最少最多的数字都小于1，则返回空集合
				return "";
			}
		}
		
        // 若设置最小长度大于字符串池长度，且设置为不允许重复，则根据设置的处理方式进行处理
        if (minLength > stringSeed.length() && !repeat) {
            switch (dispose) {
            case DISPOSE_IGNORE:
                minLength = stringSeed.length();
                maxLength = stringSeed.length();
                break;
            case DISPOSE_THROW_EXCEPTION:
                throw new IllegalDataException(String.format("生成不重复的随机字符串最小范围超出字符串范围；最小范围：%d；最大范围：%d；字符串池模型长度：%d",
                        minLength, maxLength, stringSeed.length()));
            case DISPOSE_REPEAT:
            default:
                break;
            }
        }
        
        // 若字符串的最大长度大于字符串池长度，且设置为不允许重复，将字符串池的长度赋予为随机字符串的最大长度
        if (maxLength > stringSeed.length() && !repeat) {
            maxLength = stringSeed.length();
        }

		//判断两个数值是否一致
		if (minLength == maxLength) {
			return toString(minLength);
		} else {
			// 返回生成的字符串
            return toString(new Random().nextInt(maxLength - minLength + 1) + minLength);
		}
	}

	/**
	 * 生成固定长度的随机字符串，其长度由用户指定
	 * 
	 * @param stringLength 随机字符串的长度
	 * @return 返回生成的随机字符串
	 * @see #toString()
	 * @see #toString(int, int)
	 * @throws IllegalDataException 当产生字符串不允许重复，且字符串产生范围长度小于默认长度，
	 *                              处理方式为{@link RepeatDisposeType#DISPOSE_THROW_EXCEPTION}时抛出的异常
	 */
	public String toString(int stringLength) {
		return createRandomString(stringLength, stringSeed.toString());
	}

	/**
	 * 用于简单返回随机字符串。下标规则可参考{@link #toString(int, int)}方法
	 * 
	 * @param minLength 随机长度的最小值
	 * @param maxLength 随机长度的最大值
	 * @param seed      字符串产生范围
	 * @return 返回生成的随机字符串
	 */
	public static String randomString(int minLength, int maxLength, String seed) {
		RandomString rs = new RandomString(seed);
		return rs.toString(minLength, maxLength);
	}

	/**
	 * 用于简单返回随机字符串。下标规则可参考{@link #toString(int, int)}方法
	 * 
	 * @param minLength   随机长度的最小值
	 * @param maxLength   随机长度的最大值
	 * @param stringModes 字符串产生范围,{@link StringMode}枚举对象
	 * @return 返回生成的随机字符串
	 */
	public static String randomString(int minLength, int maxLength, StringMode... stringModes) {
		RandomString rs = new RandomString(stringModes);
		return rs.toString(minLength, maxLength);
	}

	/**
	 * 用于向字符串池中添加字符串模型
	 * 
	 * @param mode 字符串模型
	 */
	private void joinStringSeed(boolean isRepeat, String mode) {
		// 过滤掉null与空串后，若存在数据，则根据条件，向字符串池中添加数据
		Optional.ofNullable(mode).filter(text -> !text.isEmpty()).ifPresent(text -> {
            // 对字符串种子的扩展识别
            text = disponseModeExpand(text);
			// 判断传入的参数是否允许字符串池中元素重复，若为true，则等同于调用addMode(StringMode... modes)
			if (!isRepeat) {
				// 判断字符串整串是否都在stringSeed中
				if (stringSeed.indexOf(text) > -1) {
					return;
				}

				// 将字符串拆成单个字符串，分别在stringSeed中进行判断
				IntStream.range(0, text.length()).mapToObj(text::charAt)
						// 将char类型转为字符串
						.map(String::valueOf)
						// 过滤掉存在于stringSeed中的字符
						.filter(ch -> stringSeed.indexOf(ch) < 0)
						.forEach(stringSeed::append);
				
			} else {
				stringSeed.append(text);
			}
		});
	}

    /**
     * 该方法用于字符串模型中的扩展内容进行处理
     * 
     * @param text 字符串模型文本
     * @return 处理后的模型文本
     * @since autest 3.5.0
     */
    private String disponseModeExpand(String text) {
        StringBuilder newText = new StringBuilder();
        // 循环，对扩展中的内容进行处理，直到文本中不存在起始扩展位置为止
        while (true) {
            // 获取起始位置
            int expandStartIndex = DisposeCodeUtils.calcExtendStrIndex(text, SEED_EXPAND_START,
                    SEED_EXPAND_TRANSFERRED_MEANING);
            // 判断起始标志是否存在，若不存在，则拼接剩余文本，并结束循环
            if (expandStartIndex == -1) {
                newText.append(deleteTransferred(text));
                break;
            }

            // 将text文本中的起始位置到扩展初始标识符位置截取，拼接至newText上
            newText.append(deleteTransferred(text.substring(0, expandStartIndex)));
            // 将text文本从起始位置截取到结束位置，以便于获取扩展结束位置
            text = text.substring(expandStartIndex);

            // 获取截取后字符串中的扩展结束位置
            int expandEndIndex = DisposeCodeUtils.calcExtendStrIndex(text, SEED_EXPAND_END,
                    SEED_EXPAND_TRANSFERRED_MEANING);
            // 判断是否存在结束位置，若不存在结束位置，则拼接剩余文本，并结束循环
            if (expandEndIndex == -1) {
                newText.append(deleteTransferred(text));
                break;
            }

            // 若存在结束位置，则获取并处理
            String expandText = text.substring(1, expandEndIndex);
            // 循环，将扩展文本与扩展公式集合进行对比，若符合对比的正则，则对扩展文本进行处理
            for (DataDriverFunction function : expandRegexList) {
                if (function.matchRegex(expandText)) {
                    expandText = function.getFunction().apply(expandText);
                    break;
                }
            }
            // 拼接扩展文本
            newText.append(expandText);

            // 判断扩展终止位置是否为文本末尾，若为文本末尾，则结束循环
            if (expandEndIndex == text.length() - 1) {
                break;
            }
            // 若不为文本末尾，则从结束位置后一个单位截取生成新字符串
            text = text.substring(expandEndIndex + 1);
        }

        return newText.toString();
    }

    /**
     * 该方法用于对非扩展符号进行处理，去除其转义符号
     * 
     * @param text 原文本
     * @return 处理后的文本
     * @since autest 3.5.0
     */
    private String deleteTransferred(String text) {
        return text
                .replaceAll(
                        DisposeCodeUtils.disposeRegexSpecialSymbol(SEED_EXPAND_TRANSFERRED_MEANING + SEED_EXPAND_START),
                        SEED_EXPAND_START)
                .replaceAll(
                        DisposeCodeUtils.disposeRegexSpecialSymbol(SEED_EXPAND_TRANSFERRED_MEANING + SEED_EXPAND_END),
                        SEED_EXPAND_END);
    }

    /**
     * 用于根据字符串池，生成随机字符串
     * 
     * @param length     生成的字符串长度
     * @param stringSeed 字符串池
     * @return 生成的随机字符串
     * @throws IllegalDataException 当产生字符串不允许重复，且字符串产生范围长度小于默认长度，
     *                              处理方式为{@link RepeatDisposeType#DISPOSE_THROW_EXCEPTION}时抛出的异常
     * @throws IllegalDataException 当字符串池为空时抛出的异常
     */
	private String createRandomString(int length, String stringSeed) {
        // 若字符串池为空，则抛出异常
	    if (stringSeed.isEmpty()) {
            throw new IllegalDataException("当前字符串池为空，无法生成随机字符串");
	    }
	    
		// 判断需要生成的字符串长度是否小于1位长度
        if (length < 1) {
			return "";
		}

		// 记录当前是否允许重复
		boolean isRepeat = repeat;

		// 判断当前字符串长度是否大于字符串池的最大长度
		if (length > stringSeed.length() && !isRepeat) {
			// 若传入的长度大于字符串，且不能重复时，则根据不同的设置做出相应的处理
			switch (dispose) {
			case DISPOSE_IGNORE:
				length = stringSeed.length();
				break;
			case DISPOSE_THROW_EXCEPTION:
                throw new IllegalDataException(
                        String.format("生成不重复的随机字符串个数超出字符串范围；随机字符串长度：%d；字符串池模型长度：%d", length, stringSeed.length()));
			case DISPOSE_REPEAT:
			default:
				isRepeat = true;
				break;
			}
		}

		// 存储生成的随机字符串
		StringBuilder randomString = new StringBuilder();
		StringBuilder seed = new StringBuilder(stringSeed);
		Random random = new Random();

		// 循环，生成随机字符串
		while (randomString.length() < length) {
			// 根据字符串池的长度，生成随机数
			int randomNum = random.nextInt(seed.length());
			randomString.append(seed.charAt(randomNum));

			// 若当前不允许重复，则删除被获取的字符
			if (!isRepeat) {
				seed.deleteCharAt(randomNum);
			}
		}

		return randomString.toString();
	}

	/**
	 * <p>
	 * <b>文件名：</b>RandomString.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 定义在设置随机字符串不允许出现重复，但需要生成的字符串长度大于随机字符串池的长度时的处理方式
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年1月16日下午2:11:36
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年1月16日下午2:11:36
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public enum RepeatDisposeType {
		/**
		 * 忽略，只生成一个与随机字符串范围长度相同的随机字符串
		 */
		DISPOSE_IGNORE,
		/**
		 * 抛出异常，中断生成字符串的操作
		 */
		DISPOSE_THROW_EXCEPTION,
		/**
		 * 重复，将随机字符串的生成条件改为允许字符串重复
		 */
		DISPOSE_REPEAT;
	}
}
