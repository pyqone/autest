package com.auxiliary.selenium.event.extend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

import org.openqa.selenium.TimeoutException;

import com.auxiliary.selenium.SeleniumToolsException;
import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;
import com.auxiliary.selenium.element.FindDataListElement;
import com.auxiliary.selenium.event.AbstractEvent;
import com.auxiliary.selenium.event.AssertEvent;
import com.auxiliary.selenium.event.ClickEvent;
import com.auxiliary.selenium.event.TextEvent;
import com.auxiliary.selenium.event.WaitEvent;
import com.auxiliary.tool.data.KeyType;
import com.auxiliary.tool.data.ListUtil;
import com.auxiliary.tool.data.TableData;
import com.auxiliary.tool.regex.ConstType;

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
 * <b>修改时间：</b>2021年3月10日上午8:05:36
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
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
	 * 用于进行等待事件
	 */
	private WaitEvent waitEvent;
	/**
	 * 用于进行断言事件
	 */
	private AssertEvent assertEvent;

	/**
	 * 用于存储当前的列表的元素
	 */
	protected LinkedHashMap<String, FindDataListElement> elementTableMap = new LinkedHashMap<>();
	/**
	 * 用于存储列表相应的操作元素映射
	 */
    private HashMap<DataTableKeywordType, Element> controlMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
	/**
	 * 指向列表加载等待控件
	 */
	private Element waitElement;

	/**
	 * 用于存储当前列表的长度
	 */
	protected int listSize = -1;

	/**
	 * 用于存储是否需要严格检查
	 */
	@Deprecated
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
		waitEvent = new WaitEvent(brower);
		assertEvent = new AssertEvent(brower);
	}

	/**
	 * 用于设置是否对传入的元素列表的个数进行严格校验，即在调用{@link #addList(FindDataListElement)}方法时，
	 * 若元素个数与初次传入的个数不符且需要严格校验，则抛出异常；反之，则直接进行存储
	 *
	 * @param isExamine 是否严格校验元素个数
	 * @deprecated 方法可在返回列表对象{@link TableData}中调用{@link TableData#setExamine(boolean)}进行设置
	 */
	@Deprecated
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
	 * 用于添加一列元素
	 * <p>
	 * <b>注意：</b>传入的{@link FindDataListElement}类对象中元素的名称请勿与其他元素名称一致，否则会覆盖原有的元素列。
	 * 其元素名称将作为列表名称，可通过该名称获取当前列
	 * </p>
	 *
	 * @param dataListBy 元素列查找对象
	 */
	public void addList(FindDataListElement dataListBy) {
		// 判定当前传入的元素是否为空，且是否查找过元素
		if (Optional.ofNullable(dataListBy).filter(d -> d.getElementData() != null).isPresent()) {
			elementTableMap.put(dataListBy.getElementData().getName(), dataListBy);
		} else {
			throw new InvalidDataListException("未指定的元素集合：" + dataListBy);
		}
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
	 * <p>
	 * 用于点击多次上一页按钮，并返回实际点击次数（实际点击次数）。
	 * </p>
	 * <p>
	 * 根据设置的翻页数，对列表进行翻页，若翻页数小于等于0，则不进行翻页。方法可设置需要断言的列，
	 * 每次翻页时，均会断言对列表设置的值进行断言，以此判断列表翻页是否有效。不传入断言列或断言列 为空时，则表示对存储的所有列断言
	 * </p>
	 * <p>
	 * <b>注意：</b>在不进行断言或断言列为空时，则翻页将一直返回成功，直到达到指定的翻页数量为止
	 * </p>
	 *
	 * @param count       点击次数
	 * @param columnNames 需要进行断言的列名称
	 * @return 实际点击次数
	 */
	public int previousPage(int count, String... columnNames) {
		return pageTurning(DataTableKeywordType.PREVIOUS_PAGE_BUTTON, count, columnNames);
	}

	/**
	 * <p>
	 * 用于点击多次下一页按钮，并返回实际点击次数（实际点击次数）。
	 * </p>
	 * <p>
	 * 根据设置的翻页数，对列表进行翻页，若翻页数小于等于0，则不进行翻页。方法可设置需要断言的列，
	 * 每次翻页时，均会断言对列表设置的值进行断言，以此判断列表翻页是否有效。不传入断言列或断言列 为空时，则表示对存储的所有列断言
	 * </p>
	 * <p>
	 * <b>注意：</b>在不进行断言或断言列为空时，则翻页将一直返回成功，直到达到指定的翻页数量为止
	 * </p>
	 *
	 * @param count       点击次数
	 * @param columnNames 需要进行断言的列名称
	 * @return 实际点击次数
	 */
	public int nextPage(int count, String... columnNames) {
		return pageTurning(DataTableKeywordType.NEXT_PAGE_BUTTON, count, columnNames);
	}

	/**
	 * 用于对列表进行翻页操作
	 *
	 * @param dataTableKeywordType 翻页按钮类型
	 * @param count                指定的翻页次数
	 * @param columnNames          需要进行断言的列名称
	 * @return 实际翻页次数
	 */
	protected int pageTurning(DataTableKeywordType dataTableKeywordType, int count, String... columnNames) {
		// 判断翻页数是否大于0，小于0，则直接结束
		if (count <= 0) {
			return 0;
		}

		// 判断当前按钮是否存在映射
		if (!controlMap.containsKey(dataTableKeywordType)) {
			throw new ControlException(dataTableKeywordType.getName(), dataTableKeywordType.toString());
		}

		// 根据设置的点击次数循环点击翻页按钮
		int nowCount = 0;
		while (true) {
			// 判断翻页数，若当前翻页数大于指定翻页数时，则结束循环
			// 若指定的翻页数小于0，则持续翻页，直到翻页失败为止
			if (nowCount >= count) {
				break;
			}

			Element controlElement = controlMap.get(dataTableKeywordType);

			boolean result = true;
			result = assertData(() -> {
				// 判断按钮是否可以点击
				if (!controlElement.getWebElement().isEnabled()) {
					return false;
				}

				clickEvent.click(controlElement);
				brower.getLogRecord().removeLog(1);
				// 等待控件消失
				if (waitElement != null) {
					waitEvent.disappear(waitElement);
					brower.getLogRecord().removeLog(1);
				}
				return true;
			}, columnNames);

			// 若点击成功，则nowCount自增，若点击失败，则退出循环
			if (!result) {
				break;
			}

			nowCount++;
		}

		String logText = "点击“%s”元素，使列表返回至%s页，其实际翻页数为：%d";
		brower.getLogRecord()
				.recordLog(String.format(logText, controlMap.get(dataTableKeywordType).getElementData().getName(),
						(dataTableKeywordType == DataTableKeywordType.PREVIOUS_PAGE_BUTTON ? "上" : "下"), nowCount));

		// 返回实际点击次数
		return nowCount;
	}

	/**
	 * 用于对列表进行点击跳页按钮后的跳页操作。若当前存储过元素列表，则对元素列表进行断言，
	 * 即取存储的列表的第一行元素，若操作前后，该行元素不变，则判定为跳页失败
	 *
	 * @param pageCount 页码数
	 * @param columnNames 需要断言的列名称
	 */
	public boolean jumpPage(String pageCount, String... columnNames) {
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
				textEvent.keyToSend(controlMap.get(DataTableKeywordType.PAGE_INPUT_TEXTBOX), KeyType.ENTER);
			}

			// 清空输入框
			textEvent.clear(controlMap.get(DataTableKeywordType.PAGE_INPUT_TEXTBOX));

			// 等待控件消失
			if (waitElement != null) {
				waitEvent.disappear(waitElement);
			}

			return true;
		}, columnNames);

		String logText = "在“%s”元素中输入%s，使列表跳转到相应的页码，其翻页%s成功";
		brower.getLogRecord()
				.recordLog(String.format(logText,
						controlMap.get(DataTableKeywordType.PAGE_INPUT_TEXTBOX).getElementData().getName(), pageCount,
						(result ? "" : "不")));

		return result;
	}

	/**
	 * 通过条件，点击{@link DataTableKeywordType#SEARCH_BUTTON}映射的按钮，对列表
	 * 进行搜索，并以默认的断言形式对列表变化进行断言，返回断言结果。
	 * <p>
	 * 方法中需要接收一个返回值为boolean类型的操作，若操作的返回值为false时， 则不会点击按钮，可参考以下写法： <code><pre>
	 * DataTableEvent test = new DataTableEvent(brower);
	 * test.searchList(() -&gt; {
	 * 	te.input(cb.getElement("账号搜索文本框"), "13000000000");
	 * 	return true;
	 * });
	 * </pre></code>
	 * </p>
	 * <p>
	 * <b>注意：</b>默认断言表示根据指定的断言列，取每列的第一条数据，判断该值搜索前后的变化，若存在变化，则 断言成功。不传入断言列时，则默认断言所有的列
	 * </p>
	 *
	 * @param action      返回值为boolean类型的操作
	 * @param columnNames 指定的断言列
	 * @return 列表是否有变化
	 * @throws ControlException 未指定搜索按钮的映射时抛出的异常
	 */
	public boolean searchList(BooleanSupplier action, String... columnNames) {
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
		}, columnNames);

		String logText = "通过搜索条件，点击“%s”元素，对列表进行搜索";
		brower.getLogRecord().recordLog(
				String.format(logText, controlMap.get(DataTableKeywordType.SEARCH_BUTTON).getElementData().getName()));

		return result;
	}

	/**
	 * 用于无条件点击{@link DataTableKeywordType#SEARCH_BUTTON}映射的按钮。
	 *
	 * @return 列表是否有变化
	 * @throws ControlException 未指定搜索按钮的映射时抛出的异常
	 */
	public boolean searchList(String... columnNames) {
		return searchList(() -> true, columnNames);
	}

	/**
	 * 用于通过输入型搜索条件对列表进行搜索，并对指定列进行包含关键词的断言，并返回列表每一列的断言结果
	 * <p>
	 * 例如，在页面上存在“姓名”的筛选条件，在对应的列表中存在“姓名”的字段，则在编写
	 * 脚本时，分别对“姓名”搜索框和“姓名”列的定位方式进行存储，调用如下代码后，便可
	 * 通过“姓名”对列表进行搜索，并断言搜索后在列表字段上，“姓名”列的每一行是否搜索 的关键词 <code><pre>
	 * DataTableEvent test = new DataTableEvent(brower);
	 * test.addList(new FindDataListElement("姓名列"))
	 * test.searchList("姓名列", "测试", common.getElement("姓名搜索框"));
	 * </pre></code>
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>当指定的断言列未存储或传入null时，则不进行断言，并返回空集合</li>
	 * <li>当指定的关键词为空或为null时，则不做任何处理，并返回空集合</li>
	 * </ol>
	 * </p>
	 *
	 * @param columnName     断言列名称
	 * @param key            关键词
	 * @param textboxElement 搜索条件控件的{@link Element}对象
	 * @return 搜索后每一行的断言结果
	 * @throws ControlException 未指定搜索按钮的映射或搜索条件控件未传入时抛出的异常
	 * @see #searchList(BooleanSupplier, String...)
	 */
	public List<Boolean> searchList(String columnName, String key, Element textboxElement) {
		// 判断控件是否存在
		if (!Optional.ofNullable(textboxElement).isPresent()) {
			throw new ControlException("未指定搜索条件元素");
		}

		return searchList(columnName, key, () -> {
			textEvent.input(textboxElement, key);
			return true;
		});
	}

	/**
	 * 用于通过点击搜索前进行一系列操作后，对列表进行搜索，并对指定列进行包含关键词的断言，并返回列表每一列的断言结果
	 * <p>
	 * 例如，在页面上存在“姓名”的筛选条件，在对应的列表中存在“姓名”的字段，则在编写
	 * 脚本时，分别对“姓名”搜索框和“姓名”列的定位方式进行存储，调用如下代码后，便可
	 * 通过“姓名”对列表进行搜索，并断言搜索后在列表字段上，“姓名”列的每一行是否搜索 的关键词 <code><pre>
	 * DataTableEvent test = new DataTableEvent(brower);
	 * test.addList(new FindDataListElement("姓名列"))
	 * test.searchList("姓名列", "测试", () -&gt; {
	 *		textEvent.input(common.getElement("姓名搜索框"), "测试");
	 *		return true;
	 *	});
	 * </pre></code>
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>当指定的断言列未存储或传入null时，则不进行断言，并返回空集合</li>
	 * <li>当指定的关键词为空或为null时，则不做任何处理，并返回空集合</li>
	 * </ol>
	 * </p>
	 *
	 * @param columnName 断言列名称
	 * @param key        关键词
	 * @param action     点击搜索按钮前的操作
	 * @return 搜索后每一行的断言结果
	 * @throws ControlException 未指定搜索按钮的映射或搜索条件控件未传入时抛出的异常
	 * @see #searchList(BooleanSupplier, String...)
	 */
	public List<Boolean> searchList(String columnName, String key, BooleanSupplier action) {
		List<Boolean> resultList = new ArrayList<>();
		// 判断关键词是否为空
		if (!Optional.ofNullable(key).filter(k -> !k.isEmpty()).isPresent()) {
			return resultList;
		}

		// 判断控件是否存在
		if (!controlMap.containsKey(DataTableKeywordType.SEARCH_BUTTON)) {
			throw new ControlException(DataTableKeywordType.SEARCH_BUTTON.getName(),
					DataTableKeywordType.SEARCH_BUTTON.toString());
		}

		if (action.getAsBoolean()) {
			clickEvent.click(controlMap.get(DataTableKeywordType.SEARCH_BUTTON));

			// 等待控件消失
			if (waitElement != null) {
				waitEvent.disappear(waitElement);
			}

			// 判断指定的列是否存在，存在则进行断言操作
			Optional.ofNullable(columnName).filter(name -> !name.isEmpty()).filter(elementTableMap::containsKey)
					.ifPresent(name -> {
						// 重新获取列元素，并对列所有的元素文本进行断言
						elementTableMap.get(name).find(name).getAllElement().stream()
								.map(e -> assertEvent.assertTextContainKey(e, true, key)).forEach(resultList::add);
					});
		}

		// 记录日志
		String logText = "通过在控件中，输入或选择“%s”搜索条件后，点击“%s”元素";
		brower.getLogRecord().recordLog(String.format(logText, key,
				controlMap.get(DataTableKeywordType.SEARCH_BUTTON).getElementData().getName()));

		return resultList;
	}

	/**
	 * <p>
	 * 获取指定的一行元素，下标允许传入负数，表示从后向前遍历
	 * </p>
	 * <p>
	 * <b>注意：</b>下标从1开始计算，即传入1时表示获取第1行数据；若传入0，则以表中最长列的元素个数为基准， 返回一个随机的数字
	 * </p>
	 *
	 * @param rowIndex 需要获取的行下标
	 * @return 指定行的元素集合
	 * @throws ControlException 元素集合为空时抛出的异常
	 */
	public List<Optional<Element>> getRowElement(int rowIndex) {
		TableData<Element> elementTable = getElementTable();

		// 转换下标，若下标为0，则取随机数，若不为0，则使用原下标
		rowIndex = (rowIndex == 0 ? new Random().nextInt(elementTable.getLongColumnSize()) + 1 : rowIndex);
		return Optional.ofNullable(elementTable.getData(rowIndex, rowIndex, elementTable.getColumnName()))
				// 将带标题的元素表转换为无标题元素表
				.map(ListUtil::toNoTitleTable)
				// 将表转置，使第一列存储获取的行元素
				.map(table -> ListUtil.rowDataToList(table, 0))
				// 判断当前元素集合是否为空
				.filter(table -> !table.isEmpty())
				// 若当前为空集合，则抛出异常
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
		elementTableMap.forEach((k, v) -> v.find(k));

		ArrayList<Optional<String>> rowTextList = new ArrayList<>(
				ListUtil.changeList(getRowElement(rowIndex), ele -> {
					String text = textEvent.getText(ele);
					brower.getLogRecord().removeLog(1);
					return text;
				}));

		// 添加日志
		String logText = "获取列表第%d行元素内容的文本，其获取到的文本内容为：%s";
		brower.getLogRecord().recordLog(String.format(logText, rowIndex, rowTextList.toString()));

		return rowTextList;
	}

	/**
	 * 获取指定列的文本，若该列元素异常时，则抛出异常
	 *
	 * @param listName 列表名称
	 * @return 指定列的文本内容
	 * @throws ControlException 该列不存在或该列元素为空时抛出的异常
	 */
	public ArrayList<Optional<String>> getListText(String listName) {
		// 重新获取列表元素
		elementTableMap.forEach((k, v) -> v.find(k));

		ArrayList<Optional<String>> listTextList = new ArrayList<>(
				ListUtil.changeList(getElementTable().getColumnList(listName), ele -> {
					String text = textEvent.getText(ele);
					brower.getLogRecord().removeLog(1);
					return text;
				}));

		// 添加日志
		String logText = "获取列表的%s列元素内容的文本，其获取到的文本内容为：%s";
		brower.getLogRecord().recordLog(String.format(logText, listName, listTextList.toString()));

		return listTextList;
	}

	/**
	 * 用于以{@link TableData}的形式返回元素列表
	 *
	 * @return 元素列表
	 */
	public TableData<Element> getElementTable() {
		TableData<Element> elementTable = new TableData<>();
		elementTableMap.forEach((key, value) -> {
			// 重新获取数据
			value.find(key);
			// 存储数据
			elementTable.addColumn(key, value.getAllElement());
		});
		return elementTable;
	}

	/**
	 * 用于随机返回指定列表的随机一个元素
	 *
	 * @param listName 列表名称
	 * @return 指定列表的随机元素
	 */
	public Element getRandomElement(String listName) {
		TableData<Element> elementTable = getElementTable();

		// 按列表长度获取随机数
		int listSize = elementTable.getListSize(listName);
		int randomIndex = new Random().nextInt(listSize);

		// 根据随机数返回元素；若当前随机的元素不存在，则返回列表第一个元素
		// 若列表第一个元素仍不存在，则跑出异常
		return elementTable.getColumnList(listName).get(randomIndex).orElse(
				elementTable.getColumnList(listName).get(0).orElseThrow(() -> new ControlException("当前列元素为空，无法获取")));
	}

	/**
	 * 用于执行需要断言页面元素的列表操作，在其操作方法前后添加了断言操作
	 *
	 * @param action 需要执行的内容
	 * @return 是否翻页成功
	 */
	protected boolean assertData(BooleanSupplier action, String... columnNames) {
		// 随机获取一个存在的断言列
		String name = "";
		if (Optional.ofNullable(columnNames).filter(c -> c.length != 0).isPresent()) {
			name = Arrays.stream(columnNames).filter(elementTableMap::containsKey).findAny().orElse("");
		} else {
			name = elementTableMap.keySet().stream().findAny().orElse("");
		}

		// 为保证数据不过期，故对页面元素进行一次重获
		againFindElement(columnNames);
		// 记录操作前的第一行文本
		List<String> oldTextList = getAssertRowText(1, columnNames);
		// 记录操作前列表元素个数
		int oldLength = getAssertColumnSize(name);

		// 执行操作，并获取操作的返回结果；若返回值为true，则需要进行元素断言操作
		if (action.getAsBoolean()) {
			// 再次重新获取元素
			againFindElement(columnNames);
			// 记录操作后的第一行文本
			List<String> newTextList = getAssertRowText(1, columnNames);
			// 记录操作后列表元素个数
			int newLength = getAssertColumnSize(name);

			// 对比两个元素的长度，若长度不一致，则返回true
			if (oldLength != newLength) {
				return true;
			}

			// 对比两集合的长度，若长度不一致，则返回true
			if (oldTextList.size() != newTextList.size()) {
				return true;
			} else {
				// 若两个集合都为空则返回true
				if (oldTextList.isEmpty()) {
					return true;
				}

				// 对文本一一比对，若存在不一致的数据，则返回true
				for (int i = 0; i < oldTextList.size(); i++) {
					if (!oldTextList.get(i).equals(newTextList.get(i))) {
						return true;
					}
				}

				// 若所有数据均一致，则返回false
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 用于返回断言所需指定列的指定行文本（下标从1开始）。
	 *
	 * @param columnNames 列名称数组
	 * @return 获取的文本集合
	 */
	protected List<String> getAssertRowText(int assertRowIndex, String... columnNames) {
		// 若断言列为空或存储的列为空，则记录需要直接返回true
		if (!elementTableMap.isEmpty()) {
			ArrayList<String> columnList = new ArrayList<>();
			// 判断传入的集合是否为空，为空则默认获取所有列的文本，否则只获取指定列的文本
			if (!Optional.ofNullable(columnNames).filter(c -> c.length != 0).isPresent()) {
				columnList.addAll(elementTableMap.keySet());
			} else {
				columnList.addAll(
						Arrays.stream(columnNames).filter(elementTableMap::containsKey).collect(Collectors.toList()));
			}

			// 若元素列表非空，则获取第一行元素，用于进行断言
			try {
				return columnList.stream().filter(name -> elementTableMap.get(name).size() > 0)
						.map(name -> elementTableMap.get(name).getElement(assertRowIndex)).map(ele -> {
							String text = textEvent.getText(ele);
							brower.getLogRecord().removeLog(1);
							return text;
						})
						.collect(Collectors.toList());
			} catch (TimeoutException e) {
				return new ArrayList<>();
			}

		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * 获取断言列元素个数。列名称只做空判断
	 *
	 * @param name 列名称
	 * @return 指定列的元素个数
	 */
	protected int getAssertColumnSize(String name) {
		if (Optional.ofNullable(name).filter(n -> !n.isEmpty()).isPresent()) {
			return elementTableMap.get(name).size();
		} else {
			return 0;
		}
	}

	/**
	 * 重新获取指定列名对应的元素数据，若未传入列名称，则对存储所有数据进行重获
	 *
	 * @param columnNames 存储的列名称
	 */
	protected void againFindElement(String... columnNames) {
		if (Optional.ofNullable(columnNames).filter(c -> c.length != 0).isPresent()) {
			Arrays.stream(columnNames).filter(elementTableMap::containsKey)
					.forEach(name -> elementTableMap.get(name).find(name));
		} else {
			elementTableMap.forEach((key, value) -> value.find(key));
		}
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
     * <b>修改时间：</b>2022年3月25日上午8:42:39
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     *
     */
    public class InvalidDataListException extends SeleniumToolsException {
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
