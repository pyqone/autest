package com.auxiliary.selenium.event.extend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BooleanSupplier;

import org.openqa.selenium.Keys;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;
import com.auxiliary.selenium.element.FindDataListElement;
import com.auxiliary.selenium.event.AbstractEvent;
import com.auxiliary.selenium.event.AssertEvent;
import com.auxiliary.selenium.event.ClickEvent;
import com.auxiliary.selenium.event.TextEvent;
import com.auxiliary.selenium.event.WaitEvent;
import com.auxiliary.tool.data.ListUtil;
import com.auxiliary.tool.data.TableData;

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
	 * 用于存储当前的列表的元素
	 */
//	protected ArrayList<DataListBy> tableList = new ArrayList<>();
//	protected LinkedHashMap<String, List<Element>> tableMap = new LinkedHashMap<>(16);
	protected TableData<Element> elementTable = new TableData<>();
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
	 * 用于设置是否对传入的元素列表的个数进行严格校验，即在调用{@link #addList(FindDataListElement)}方法时，
	 * 若元素个数与初次传入的个数不符且需要严格校验，则抛出异常；反之，则直接进行存储
	 * 
	 * @param isExamine 是否严格校验元素个数
	 */
	public void setExamine(boolean isExamine) {
		elementTable.setExamine(isExamine);
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
	 * 用于添加一列元素
	 * <p>
	 * <b>注意：</b>传入的{@link FindDataListElement}类对象中元素的名称请勿与其他元素名称一致，否则会覆盖原有的元素列。
	 * 其元素名称将作为列表名称，可通过该名称获取当前列
	 * </p>
	 * 
	 * @param dataListBy 元素列查找对象
	 */
	public void addList(FindDataListElement dataListBy) {
		elementTable.addColumn(dataListBy.getElementData().getName(), new ArrayList<>(Optional.ofNullable(dataListBy)
				// 将By对象转换为元素集合
				.map(by -> by.getAllElement())
				// 若当前为空对象，则返回一个空元素集合
				.orElse(new ArrayList<Element>())));
	}

	/**
	 * 用于添加列表控件的枚举，在调用部分列表操作方法时会使用在此处添加的映射
	 * 
	 * @param dataTableKeywordType 列表可映射的控件枚举{@link DataTableKeywordType}
	 * @param elemenet             控件相应的元素对象{@link Element}
	 */
	public void putControl(DataTableKeywordType dataTableKeywordType, Element elemenet) {
		controlMap.put(dataTableKeywordType, elemenet);
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
	 * @param pageCount 页码数
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
	 * <code><pre>
	 * DataTableEvent test = new DataTableEvent(brower);
	 * test.searchList(() -&gt; {
	 * 	te.input(cb.getElement("账号搜索文本框"), "13000000000");
	 * 	return true;
	 * });
	 * </pre></code>
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
	 * 获取指定的一行元素，下标允许传入负数，表示从后向前遍历
	 * </p>
	 * <p>
	 * <b>注意：</b>下标从1开始计算，即传入1时表示获取第1行数据；若传入0，则以表中最长列的元素个数为基准，
	 * 返回一个随机的数字
	 * </p>
	 * 
	 * @param rowIndex 需要获取的行下标
	 * @return 指定行的元素集合
	 * @throws ControlException 元素集合为空时抛出的异常
	 */
	public List<Optional<Element>> getRowElement(int rowIndex) {
		//转换下标，若下标为0，则取随机数，若不为0，则使用原下标
		rowIndex = (rowIndex == 0 ? new Random().nextInt(elementTable.getLongColumnSize()) + 1 : rowIndex);
		return Optional.ofNullable(elementTable.getData(rowIndex, rowIndex, elementTable.getColumnName()))
				//将带标题的元素表转换为无标题元素表
				.map(ListUtil::toNoTitleTable)
				//将表转置，使第一列存储获取的行元素
				.map(table -> ListUtil.rowDataToList(table, 0))
				//判断当前元素集合是否为空
				.filter(table -> !table.isEmpty())
				//若当前为空集合，则抛出异常
				.orElseThrow(() -> new ControlException("当前行元素为空，无法获取"));

	}
	
	/**
	 * 获取指定行的文本，其行号可传入负数，具体规则可参考{@link FindDataListElement#getElement(int)}方法
	 * 
	 * @param rowIndex 指定的行号
	 * @return 该行元素的文本
	 */
	public ArrayList<Optional<String>> getRowText(int rowIndex) {
		// 重新获取列表元素
		againFindDataList();

		ArrayList<Optional<String>> rowTextList = new ArrayList<>(
				ListUtil.changeList(getRowElement(rowIndex), textEvent::getText));

		// 添加日志
		resultText = rowTextList.toString();
		logText = "获取列表第" + rowIndex + "行元素内容的文本，其获取到的文本内容为：" + resultText;

		return rowTextList;
	}

	/**
	 * 获取指定列的文本，若该列元素异常时，则抛出异常
	 * 
	 * @param listName 列表名称
	 * @return 指定列的文本内容
	 * @throws ControlException 该列不存在或该列元素为空时抛出的异常
	 */
	public  ArrayList<Optional<String>> getListText(String listName) {
		// 重新获取列表元素
		againFindDataList();

		ArrayList<Optional<String>> listTextList = new ArrayList<>(
				ListUtil.changeList(elementTable.getColumnList(listName), textEvent::getText));


		// 添加日志
		resultText = listTextList.toString();
		logText = "获取列表的" + listName + "列元素内容的文本，其获取到的文本内容为：" + resultText;

		return listTextList;
	}

	/**
	 * 用于执行需要断言页面元素的列表操作，在其操作方法前后添加了断言操作
	 * 
	 * @param action 需要执行的内容
	 * @return 是否翻页成功
	 */
	protected boolean assertData(BooleanSupplier action) {
		// 若元素列表非空，则获取第一行元素，用于进行断言
		ArrayList<Optional<String>> oldTextList = new ArrayList<>();
		if (elementTable.getLongColumnSize() > 0) {
			// 获取第一行元素，并将其转换为文本后存储
			oldTextList = getRowText(1);
		}
		// 获取当前集合的长度
		int oldListSize = elementTable.getShortColumnSize();

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
	 * @param oldListSize 原始数据个数
	 * @return 元素是否存在改变
	 */
	protected boolean assertDataChange(ArrayList<Optional<String>> oldTextList, int oldListSize) {
		// 重新获取集合元素
		againFindDataList();

		// 获取操作后的第一行元素
		List<Optional<Element>> newElementList = getRowElement(1);

		// 若集合的长度发生改变，则表示集合存在变化
		if (oldListSize != listSize) {
			return true;
		}

		// 为避免不进行翻页时，列表也会进行一次刷新，则获取信息，对每个文本数据进行比对
		for (int index = 0; index < oldTextList.size(); index++) {
			if (assertEvent.assertNotEqualsText(newElementList.get(index).orElse(null), oldTextList.get(index).orElse(""))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 用于重新获取元素信息
	 */
	private void againFindDataList() {
		elementTable.getFirstRowData().forEach(data -> {
			if (data.isPresent()) {
				Element element = data.get();
				element.againFindElement();
				
				String columnName = element.getElementData().getName();
				elementTable.clearColumn(columnName);
				addList((FindDataListElement) element.getBy());
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
