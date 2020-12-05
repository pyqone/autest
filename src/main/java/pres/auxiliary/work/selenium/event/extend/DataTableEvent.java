package pres.auxiliary.work.selenium.event.extend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.element.DataListBy;
import pres.auxiliary.work.selenium.element.Element;
import pres.auxiliary.work.selenium.event.AbstractEvent;
import pres.auxiliary.work.selenium.event.AssertEvent;
import pres.auxiliary.work.selenium.event.ClickEvent;
import pres.auxiliary.work.selenium.event.TextEvent;
import pres.auxiliary.work.selenium.event.WaitEvent;

/**
 * <p>
 * <b>文件名：</b>OperateDataTable.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对数据表格进行基本操作的事件，包括对数据列表的翻页、跳页、获取等操作，以简化部分操作的代码
 * </p>
 * <p>
 * <b>编码时间：</b>2020年11月17日上午7:58:40
 * </p>
 * <p>
 * <b>修改时间：</b>2020年11月17日上午7:58:40
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public final class DataTableEvent extends AbstractEvent {
	/**
	 * 用于进行点击事件
	 */
	private ClickEvent clickEvent;
	/**
	 * 用于进行文本事件
	 */
	private TextEvent textEvent;
	/**
	 * 用于进行断言事件
	 */
	private AssertEvent assertEvent;
	/**
	 * 用于进行等待事件
	 */
	private WaitEvent waitEvent;

	/**
	 * 用于存储当前的列表的一列元素
	 */
//	protected ArrayList<DataListBy> tableList = new ArrayList<>();
	protected LinkedHashMap<String, List<Element>> tableMap = new LinkedHashMap<>(16);
	/**
	 * 用于存储列表相应的操作元素映射
	 */
	private HashMap<DataTableKeywordType, Element> controlMap = new HashMap<>(16);
	/**
	 * 指向列表加载等待控件
	 */
	private Element waitElement;

	/**
	 * 用于存储当前列表的长度
	 */
	protected int listSize = -1;

	/**
	 * 标记是否进行元素个数长度校验
	 */
	protected boolean isExamine = true;

	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public DataTableEvent(AbstractBrower brower) {
		super(brower);

		clickEvent = new ClickEvent(brower);
		textEvent = new TextEvent(brower);
		assertEvent = new AssertEvent(brower);
		waitEvent = new WaitEvent(brower);
	}

	/**
	 * 用于设置是否对传入的元素列表的个数进行严格校验，即在调用{@link #add(DataListBy)}方法时，
	 * 若元素个数与初次传入的个数不符且需要严格校验，则抛出异常；反之，则直接进行存储
	 * 
	 * @param isExamine 是否严格校验元素个数
	 */
	public void setExamine(boolean isExamine) {
		this.isExamine = isExamine;
	}

	/**
	 * 用于设置列表加载等待元素，通过该元素，将应用与列表操作后，等待该控件消失后再进行断言的操作
	 * 
	 * @param waitElement 列表加载等待控件
	 */
	public void setWaitElement(Element waitElement) {
		this.waitElement = waitElement;
	}

	/**
	 * 用于添加一列元素，若启用严格校验（即通过{@link #setExamine(boolean)}方法设置为true），
	 * 则调用该方法时将对存储的数据个数进行校验，若传入的列元素个数与当前存储的列表元素个数
	 * 不一致时，则抛出{@link InvalidDataListException}异常。
	 * 
	 * <p>
	 * <b>注意：</b>传入的{@link DataListBy}类对象中元素的名称请勿与其他元素名称一致，否则会覆盖原有的元素列。
	 * 其元素名称将作为列表名称，可通过该名称获取当前列
	 * </p>
	 * 
	 * @param dataListBy 元素列查找对象
	 * @throws InvalidDataListException 启用严格校验且元素个数与存储列表元素个数不一致时抛出的异常
	 */
	public void addList(DataListBy dataListBy) {
		// 判断当前是否存储元素，若未存储元素，则不进行元素个数判断
		if (!tableMap.isEmpty()) {
			// 判断传入的列的元素个数是否与当前存储的元素个数一致，若不一致，则进行个数判定校验
			int nowSize = dataListBy.size();
			if (nowSize != listSize()) {
				// 若当前需要严格校验列表元素个数，则抛出异常
				if (isExamine) {
					throw new InvalidDataListException(
							"当前传入的元素列个数与存储的元素列个数不一致！" + "（当前元素列个数：" + listSize() + "，传入的元素列元素个数：" + nowSize + "）");
				} else {
					// 若无需校验元素个数，则判断传入的元素个数与存储的元素列个数，存储较小的数字
					listSize = listSize < nowSize ? listSize : nowSize;
				}
			}

		} else {
			listSize = dataListBy.size();
		}

		tableMap.put(dataListBy.getElementData().getName(), dataListBy.getAllElement());
	}

	/**
	 * 用于添加列表控件的枚举，在调用部分列表操作方法时会使用在此处添加的映射
	 * 
	 * @param dataTableKeywordType 列表可映射的控件枚举{@link DataTableKeywordType}
	 * @param by                   控件相应的元素对象{@link Element}
	 */
	public void putControl(DataTableKeywordType dataTableKeywordType, Element elemenet) {
		controlMap.put(dataTableKeywordType, elemenet);
	}

	/**
	 * 返回元素列中元素的个数，若设置了严格判断，则该数值为所有列的元素个数；反之，则该 数值表示列表集合中，最短元素列的元素个数
	 * 
	 * @return 元素列的元素个数
	 */
	public int listSize() {
		return listSize;
	}

	/**
	 * 获取列表名称指向列的元素个数
	 * @param name 列表名称
	 * @return 指向的列表元素个数
	 */
	public int listSize(String name) {
		return tableMap.get(name).size();
	}

	/**
	 * 用于返回元素表中的列数
	 * 
	 * @return
	 */
	public int rowSize() {
		return tableMap.size();
	}

	/**
	 * 用于点击多次上一页按钮，并返回实际点击次数（实际点击次数）。若设置的翻页次数小于0 ，则持续翻页至无法翻页为止
	 * 
	 * @param count 点击次数
	 * @return 实际点击次数
	 */
	public int previousPage(int count) {
		return pageTurning(DataTableKeywordType.PREVIOUS_PAGE_BUTTON, count);
	}

	/**
	 * 用于点击多次下一页按钮，并返回实际点击次数（实际点击次数）。若设置的翻页次数小于0 ，则持续翻页至无法翻页为止
	 * 
	 * @param count 点击次数
	 * @return 实际点击次数
	 */
	public int nextPage(int count) {
		return pageTurning(DataTableKeywordType.NEXT_PAGE_BUTTON, count);
	}

	/**
	 * 用于对列表进行翻页操作
	 * 
	 * @param dataTableKeywordType 翻页按钮类型
	 * @param count                指定的翻页次数
	 * @return 实际翻页次数
	 */
	private int pageTurning(DataTableKeywordType dataTableKeywordType, int count) {
		// 判断当前按钮是否存在映射
		if (!controlMap.containsKey(dataTableKeywordType)) {
			throw new ControlException(dataTableKeywordType.getName(), dataTableKeywordType.toString());
		}

		// 根据设置的点击次数循环点击翻页按钮
		int nowCount = 0;
		while (true) {
			// 判断翻页数，若当前翻页数大于指定翻页数时，则结束循环
			// 若指定的翻页数小于0，则持续翻页，直到翻页失败为止
			if (nowCount >= count && count >= 0) {
				break;
			}

			Element controlElement = controlMap.get(dataTableKeywordType);

			boolean result = assertData(() -> {
				// 判断按钮是否可以点击
				if (!controlElement.getWebElement().isEnabled()) {
					return false;
				}

				try {
					clickEvent.click(controlElement);
					// 等待控件消失
					if (waitElement != null) {
						waitEvent.disappear(waitElement);
					}
					return true;
				} catch (Exception e) {
					return false;
				}
			});

			// 若点击成功，则nowCount自增，若点击失败，则退出循环
			if (!result) {
				break;
			}

			nowCount++;
		}

		logText = "点击“" + controlMap.get(DataTableKeywordType.PREVIOUS_PAGE_BUTTON).getElementData().getName()
				+ "”元素，使列表返回至" + (dataTableKeywordType == DataTableKeywordType.PREVIOUS_PAGE_BUTTON ? "上" : "下")
				+ "页，其实际翻页数为：" + nowCount;
		resultText = String.valueOf(nowCount);

		// 返回实际点击次数
		return nowCount;
	}

	/**
	 * 用于对列表进行点击跳页按钮后的跳页操作。若当前存储过元素列表，则对元素列表进行断言，
	 * 即取存储的列表的第一行元素，若操作前后，该行元素不变，则判定为跳页失败
	 * 
	 * @param pageTextbox    跳页文本框元素
	 * @param jumpPageButton 跳页按钮
	 * @param pageCountText  页码文本
	 */
	public boolean jumpPage(String pageCount) {
		if (!controlMap.containsKey(DataTableKeywordType.PAGE_INPUT_TEXTBOX)) {
			throw new ControlException(DataTableKeywordType.PAGE_INPUT_TEXTBOX.getName(),
					DataTableKeywordType.PAGE_INPUT_TEXTBOX.toString());
		}

		boolean result = assertData(() -> {
			// 输入页码
			textEvent.input(controlMap.get(DataTableKeywordType.PAGE_INPUT_TEXTBOX), pageCount);
			// 判断是否存在跳页按钮的映射，若不存在，则使用回车进行跳页
			if (controlMap.containsKey(DataTableKeywordType.JUMP_PAGE_BUTTON)) {
				// 点击跳页
				clickEvent.click(controlMap.get(DataTableKeywordType.JUMP_PAGE_BUTTON));
			} else {
				textEvent.keyToSend(controlMap.get(DataTableKeywordType.PAGE_INPUT_TEXTBOX), Keys.ENTER);
			}

			// 清空输入框
			textEvent.clear(controlMap.get(DataTableKeywordType.PAGE_INPUT_TEXTBOX));

			// 等待控件消失
			if (waitElement != null) {
				waitEvent.disappear(waitElement);
			}

			return true;
		});

		logText = "在“" + controlMap.get(DataTableKeywordType.PAGE_INPUT_TEXTBOX).getElementData().getName() + "”元素中输入"
				+ pageCount + "，使列表跳转到相应的页码，其翻页" + (result ? "" : "不") + "成功";
		resultText = String.valueOf(result);

		return result;
	}

	/**
	 * 通过条件，点击{@link DataTableKeywordType#SEARCH_BUTTON}映射的按钮，对列表进行搜索。方法中需要接收一个
	 * 返回值为boolean类型的操作，若操作的返回值为false时，则不会点击按钮，可参考以下写法：
	 * 
	 * <pre>
	 * <code>
	 * DataTableEvent test = new DataTableEvent(brower);
	 * test.searchList(() -> {
	 * 	te.input(cb.getElement("账号搜索文本框"), "13000000000");
	 * 	return true;
	 * });
	 * </code>
	 * </pre>
	 * 
	 * @param action 返回值为boolean类型的操作
	 * @return 列表是否有变化
	 */
	public boolean searchList(BooleanSupplier action) {
		// 判断控件是否存在
		if (!controlMap.containsKey(DataTableKeywordType.SEARCH_BUTTON)) {
			throw new ControlException(DataTableKeywordType.SEARCH_BUTTON.getName(),
					DataTableKeywordType.SEARCH_BUTTON.toString());
		}

		boolean result = assertData(() -> {
			// 若操作成功，则点击搜索按钮
			if (action.getAsBoolean()) {
				clickEvent.click(controlMap.get(DataTableKeywordType.SEARCH_BUTTON));

				// 等待控件消失
				if (waitElement != null) {
					waitEvent.disappear(waitElement);
				}

				return true;
			} else {
				return false;
			}
		});

		logText = "通过搜索条件，点击“" + controlMap.get(DataTableKeywordType.SEARCH_BUTTON).getElementData().getName()
				+ "”元素，对列表进行搜索";
		resultText = String.valueOf(result);

		return result;
	}

	/**
	 * <p>
	 * 获取指定的一行元素，下标允许传入负数，表示从后向前遍历，具体遍历逻辑可参考{@link DataListBy#getElement(int)}方法。
	 * </p>
	 * <p>
	 * <b>注意：</b>下标将按照元素列长度进行计算，若下标的绝对值大于元素列长度，且下标为正数，则
	 * 获取最后一行元素；反之，则获取第一行元素。元素列个数可参考{@link #listSize()}方法
	 * </p>
	 * 
	 * @param rowIndex 需要获取的行下标
	 * @return 指定行的元素集合
	 */
	public ArrayList<Element> getRowElement(int rowIndex) {
		// 根据下标，获取元素，并进行存储
		ArrayList<Element> elementList = new ArrayList<>();
		tableMap.forEach((key, value) -> {
			// 存储元素
			elementList.add(value.get(toElementIndex(listSize(key), rowIndex)));
		});

		return elementList;
	}

	/**
	 * 获取指定行的文本，其行号可传入负数，具体规则可参考{@link DataListBy#getElement(int)}方法
	 * @param rowIndex 指定的行号
	 * @return 该行元素的文本
	 */
	public ArrayList<String> getRowText(int rowIndex) {
		//重新获取列表元素
		againFindDataList();
		
		ArrayList<String> rowTextList = new ArrayList<>();
		// 遍历元素，将其转换为文本后进行存储
		getRowElement(rowIndex).stream().map(textEvent::getText).forEach(rowTextList :: add);
		
		//添加日志
		resultText = rowTextList.toString();
		logText = "获取列表第" + rowIndex + "行元素内容的文本，其获取到的文本内容为：" + resultText;
		
		return rowTextList;
	}
	
	/**
	 * 获取指定列的文本，若该列元素异常时，则抛出异常
	 * @param listName 列表名称
	 * @return 指定列的文本内容
	 * @throws ControlException 该列不存在或该列元素为空时抛出的异常
	 */
	public ArrayList<String> getListText(String listName) {
		//重新获取列表元素
		againFindDataList();
		
		//判断列元素是否存在，若不存在，则抛出异常
		if (!tableMap.containsKey(listName)) {
			throw new ControlException("“" + listName + "”指向的列元素不存在");
		}
		//判断当前列是否包含元素
		if (listSize(listName) == 0) {
			throw new ControlException("“" + listName + "”指向的列无元素");
		}
		
		ArrayList<String> listTextList = new ArrayList<>();
		// 遍历元素，将其转换为文本后进行存储
		tableMap.get(listName).stream().map(textEvent::getText).forEach(listTextList :: add);
		
		//添加日志
		resultText = listTextList.toString();
		logText = "获取列表的" + listName + "列元素内容的文本，其获取到的文本内容为：" + resultText;
		
		return listTextList;
	}
	
	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始， 故可通过该方法对下标的含义进行转义，得到java能识别的下标
	 * 
	 * @param length 元素的个数
	 * @param index  传入的下标
	 * @return 可识别的下标
	 */
	protected int toElementIndex(int length, int index) {
		// 判断元素下标是否超出范围，由于可以传入负数，故需要使用绝对值
		if (Math.abs(index) > length) {
			if (index > 0) {
				return length;
			} else {
				return 0;
			}
		}

		// 判断index的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
		if (index > 0) {
			// 选择元素，正数的选项值从1开始，故需要减小1
			return index - 1;
		} else if (index < 0) {
			// 选择元素，由于index为负数，则长度加上选项值即可得到需要选择的选项
			return length + index;
		} else {
			return new Random().nextInt(length);
		}
	}

	/**
	 * 用于执行需要断言页面元素的列表操作，在其操作方法前后添加了断言操作
	 * 
	 * @param action 需要执行的内容
	 * @return 是否翻页成功
	 */
	protected boolean assertData(BooleanSupplier action) {
		// 若元素列表非空，则获取第一行元素，用于进行断言
		ArrayList<String> oldTextList = new ArrayList<>();
		if (!tableMap.isEmpty()) {
			// 获取第一行元素，并将其转换为文本后存储
			getRowElement(1).stream().map(textEvent::getText).forEach(oldTextList::add);
		}
		// 获取当前集合的长度
		int oldListSize = listSize();

		// 执行操作，并获取操作的返回结果；若返回值为true，则需要进行元素断言操作
		if (action.getAsBoolean()) {
			// 若当前未获取原元素的内容，则不进行列表断言
			if (oldTextList.size() != 0) {
				// 断言元素，并返回结果
				return assertDataChange(oldTextList, oldListSize);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 断言数据是否有改变，若数据改变，则返回true；反之，返回false
	 * 
	 * @param oldTextList 原始数据文本集合
	 * @param oldElement  原始数据第一个元素的{@link WebElement}对象
	 * @return 元素是否存在改变
	 */
	protected boolean assertDataChange(ArrayList<String> oldTextList, int oldListSize) {
		// 重新获取集合元素
		againFindDataList();

		// 获取操作后的第一行元素
		ArrayList<Element> newElementList = getRowElement(1);

		// 若集合的长度发生改变，则表示集合存在变化
		if (oldListSize != listSize) {
			return true;
		}

		// 为避免不进行翻页时，列表也会进行一次刷新，则获取信息，对每个文本数据进行比对
		for (int index = 0; index < oldTextList.size(); index++) {
			if (assertEvent.assertNotEqualsText(newElementList.get(index), oldTextList.get(index))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 用于重新获取元素信息
	 */
	private void againFindDataList() {
		// 用于判断当前数列元素的个数
		AtomicInteger nowListSize = new AtomicInteger(-1);
		tableMap.forEach((key, value) -> {
			// 获取原集合个数
			int oldSize = value.size();

			// 对列表第一个元素进行重新获取
			Element element = value.get(0);
			// 重新获取当前元素，并存储当前列表长度
			int elementListSize = element.againFindElement();

			// 判断当前size是否为初始化的状态，若为初始化的状态，则直接存储重新获取后的集合元素个数
			if (nowListSize.get() == -1) {
				nowListSize.set(elementListSize);
			} else {
				// 若当前size已被初始化，则进行重获后的元素个数判断
				if (nowListSize.get() != elementListSize) {
					// 若当前需要严格校验列表元素个数，则抛出异常
					if (isExamine) {
						throw new InvalidDataListException("“" + key + "”元素列的元素个数与其他元素列的元素个数不一致！" + "（ “" + key
								+ "”元素列元素列个数：" + elementListSize + "，" + "其他元素列的元素个数：" + nowListSize.get() + "）");
					} else {
						// 若无需校验元素个数，则判断传入的元素个数与存储的元素列个数，存储较小的数字
						nowListSize.set(nowListSize.get() > elementListSize ? elementListSize : nowListSize.get());
					}
				}
			}

			// 判断当前元素个数与重新获取前元素个数是否一致，不一致，则需要对数组进行处理
			int nowSize = nowListSize.get();
			if (nowSize != oldSize) {
				// 根据元素返回的元素查找对象，强转为DataListBy后，再重新获取所有元素
				value = ((DataListBy) (element.getBy())).getAllElement();

				// 根据是否进行严格检查，来对listSize进行赋值，若无需严格检查，则取两者之间最小者
				listSize = isExamine ? nowSize : Math.min(nowSize, listSize);
			}
		});
	}

	/**
	 * <p>
	 * <b>文件名：</b>DataTableEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 枚举列表中可操作的控件，如上一页、下一页按钮等
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年11月30日上午8:03:59
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年11月30日上午8:03:59
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public enum DataTableKeywordType {
		/**
		 * 上一页按钮
		 */
		PREVIOUS_PAGE_BUTTON("上一页按钮"),
		/**
		 * 下一页按钮
		 */
		NEXT_PAGE_BUTTON("下一页按钮"),
		/**
		 * 首页按钮
		 */
		FIRST_PAGE_BUTTON("首页按钮"),
		/**
		 * 尾页按钮
		 */
		LAST_PAGE_BUTTON("尾页按钮"),
		/**
		 * 跳页按钮
		 */
		JUMP_PAGE_BUTTON("跳页按钮"),
		/**
		 * 页码输入文本框（用于跳页的输入）
		 */
		PAGE_INPUT_TEXTBOX("页码输入文本框"),
		/**
		 * 搜索按钮
		 */
		SEARCH_BUTTON("搜索按钮");

		/**
		 * 存储枚举名称
		 */
		String name;

		/**
		 * 初始化枚举名称
		 * 
		 * @param name
		 */
		private DataTableKeywordType(String name) {
			this.name = name;
		}

		/**
		 * 返回枚举指向的控件名称
		 * 
		 * @return 控件名称
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>DataTableEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 若元素列表无法添加时抛出的异常
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年11月19日下午8:26:49
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年11月19日下午8:26:49
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public class InvalidDataListException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public InvalidDataListException() {
			super();
		}

		public InvalidDataListException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public InvalidDataListException(String message, Throwable cause) {
			super(message, cause);
		}

		public InvalidDataListException(String message) {
			super(message);
		}

		public InvalidDataListException(Throwable cause) {
			super(cause);
		}

	}
}
