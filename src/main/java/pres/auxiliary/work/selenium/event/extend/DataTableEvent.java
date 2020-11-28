package pres.auxiliary.work.selenium.event.extend;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

import org.openqa.selenium.Keys;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.element.DataListBy;
import pres.auxiliary.work.selenium.element.Element;
import pres.auxiliary.work.selenium.event.AbstractEvent;
import pres.auxiliary.work.selenium.event.AssertEvent;
import pres.auxiliary.work.selenium.event.ClickEvent;
import pres.auxiliary.work.selenium.event.TextEvent;

/**
 * <p><b>文件名：</b>OperateDataTable.java</p>
 * <p><b>用途：</b>
 * 提供对数据表格进行基本操作的事件，包括对数据列表的翻页、跳页、获取等操作，以简化部分操作的代码
 * </p>
 * <p><b>编码时间：</b>2020年11月17日上午7:58:40</p>
 * <p><b>修改时间：</b>2020年11月17日上午7:58:40</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class DataTableEvent extends AbstractEvent {
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
	 * 用于存储当前的列表元素
	 */
//	protected ArrayList<DataListBy> tableList = new ArrayList<>();
	protected LinkedHashMap<String, List<Element>> tableMap = new LinkedHashMap<>(16);
	
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
	}
	
	/**
	 * 用于设置是否对传入的元素列表的个数进行严格校验，即在调用{@link #add(DataListBy)}方法时，
	 * 若元素个数与初次传入的个数不符且需要严格校验，则抛出异常；反之，则直接进行存储
	 * @param isExamine 是否严格校验元素个数
	 */
	public void setExamine(boolean isExamine) {
		this.isExamine = isExamine;
	}

	/**
	 * 用于添加一列元素，若启用严格校验（即通过{@link #setExamine(boolean)}方法设置为true），
	 * 则调用该方法时将对存储的数据个数进行校验，若传入的列元素个数与当前存储的列表元素个数
	 * 不一致时，则抛出{@link InvalidDataListException}异常
	 * 
	 * <p>
	 * 	<b>注意：</b>传入的{@link DataListBy}类对象中元素的名称请勿与其他元素名称一致，否则会
	 * 	覆盖原有的元素列
	 * </p>
	 * 
	 * @param dataListBy 元素列查找对象
	 * @throws InvalidDataListException 启用严格校验且元素个数与存储列表元素个数不一致时抛出的异常
	 */
	public void add(DataListBy dataListBy) {
		//判断当前是否存储元素，若未存储元素，则不进行元素个数判断
		if (!tableMap.isEmpty()) {
			//判断传入的列的元素个数是否与当前存储的元素个数一致，若不一致，则进行个数判定校验
			int nowSize = dataListBy.size();
			if (nowSize != listSize()) {
				//若当前需要严格校验列表元素个数，则抛出异常
				if (isExamine) {
					throw new InvalidDataListException("当前传入的元素列个数与存储的元素列个数不一致！"
							+ "（当前元素列个数：" + listSize() + "，传入的元素列元素个数：" + nowSize + "）");
				} else {
					//若无需校验元素个数，则判断传入的元素个数与存储的元素列个数，存储较小的数字
					listSize = listSize < nowSize ? listSize : nowSize;
				}
			}
			
		} else {
			listSize = dataListBy.size();
		}
		
		tableMap.put(dataListBy.getElement(1).getElementData().getName(), dataListBy.getAllElement());
	}
	
	/**
	 * 返回元素列中元素的个数，若设置了严格判断，则该数值为所有列的元素个数；反之，则该
	 * 数值表示列表集合中，最短元素列的元素个数
	 * 
	 * @return 元素列的元素个数
	 */
	public int listSize() {
		return listSize;
	}
	
	public int listSize(String name) {
		return tableMap.get(name).size();
	}
	
	/**
	 * 用于返回元素表中的列数
	 * @return
	 */
	public int rowSize() {
		return tableMap.size();
	}

	/**
	 * 用于点击一次上一页按钮，并返回点击成功结果
	 * @param pageTurningButton 翻页按钮元素
	 * @return 点击结果
	 */
	public boolean pageTurning(Element pageTurningButton) {
		boolean result = operateSuchAssertData(() -> {
			//判断按钮是否可以点击
			if (!pageTurningButton.getWebElement().isEnabled()) {
				return false;
			}
			
			try {
				clickEvent.click(pageTurningButton);
				return true;
			} catch (Exception e) {
				return false;
			}
		});
		
		logText = "点击“" + pageTurningButton.getElementData().getName() + "”元素，返回列表上一页，其翻页"
				+ (result ? "" : "不") + "成功";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 用于点击多次上一页按钮，并返回实际点击次数（实际点击次数）
	 * @param pageTurningButton 上一页按钮元素
	 * @param count 点击次数
	 * @return 实际点击次数
	 */
	public int pageTurning(Element pageTurningButton, int count) {
		int nowCount = 0;
		//根据设置的点击次数循环点击上一页
		while (nowCount < count) {
			//若点击成功，则nowCount自增，若点击失败，则退出循环
			if (pageTurning(pageTurningButton)) {
				nowCount++;
			} else {
				break;
			}
		}
		
		logText = "连续点击“" + pageTurningButton.getElementData().getName() + "”元素，使列表返回至上"
				+ count + "页，其实际翻页数为：" + nowCount;
		resultText = String.valueOf(nowCount);
		
		//返回实际点击次数
		return nowCount;
	}
	
	/**
	 * 用于持续点击上一页按钮，直到按钮不可点击为止，并返回实际点击次数（实际点击次数）
	 * @param pageTurningButton 上一页按钮元素
	 * @return 实际点击次数
	 */
	public int continuePageTurning(Element pageTurningButton) {
		int nowCount = 0;
		//根据设置的点击次数循环点击上一页
		while (true) {
			//若点击成功，则nowCount自增，若点击失败，则退出循环
			if (pageTurning(pageTurningButton)) {
				nowCount++;
			} else {
				break;
			}
		}
		
		logText = "持续点击“" + pageTurningButton.getElementData().getName() + "”元素，使列表返回至首页"
				+ "，其实际翻页数为：" + nowCount;
		resultText = String.valueOf(nowCount);
		
		//返回实际点击次数
		return nowCount;
	}
	
	/**
	 * 用于对列表进行点击跳页按钮后的跳页操作。若当前存储过元素列表，则对元素列表进行断言，
	 * 即取存储的列表的第一行元素，若操作前后，该行元素不变，则判定为跳页失败
	 * 
	 * @param pageTextbox 跳页文本框元素
	 * @param jumpPageButton 跳页按钮
	 * @param pageCountText 页码文本
	 */
	public boolean jumpPage(Element pageTextbox, Element jumpPageButton, String pageCount) {
		boolean result = operateSuchAssertData(() -> {
			//输入页码
			textEvent.input(pageTextbox, pageCount);
			//点击跳页
			clickEvent.click(jumpPageButton);
			
			return true;
		});
		
		logText = "在“" + pageTextbox.getElementData().getName() + "”元素中输入" + pageCount 
				+ "，点击“ + jumpPageButton.getElementData().getName() + ”按钮，跳到列表相应的页码，其翻页"
				+ (result ? "" : "不") + "成功";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 用于对列表按下回车后的跳页操作。若当前存储过元素列表，则对元素列表进行断言，
	 * 即取存储的列表的第一行元素，若操作前后，该行元素不变，则判定为跳页失败
	 * 
	 * @param pageTextbox 跳页文本框元素
	 * @param pageCountText 页码文本
	 */
	public boolean jumpPage(Element pageTextbox, String pageCount) {
		boolean result = operateSuchAssertData(() -> {
			//输入页码
			textEvent.input(pageTextbox, pageCount);
			//点击跳页
			textEvent.keyToSend(pageTextbox, Keys.ENTER);
			
			return true;
		});
		
		logText = "在“" + pageTextbox.getElementData().getName() + "”元素中输入" + pageCount 
				+ "，按下回车，跳到列表相应的页码，其翻页"
				+ (result ? "" : "不") + "成功";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * <p>
	 * 获取指定的一行元素，下标允许传入负数，表示从后向前遍历，具体遍历逻辑
	 * 可参考{@link DataListBy#getElement(int)}方法。
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
		//根据下标，获取元素，并进行存储
		ArrayList<Element> elementList = new ArrayList<>();
		tableMap.forEach((key, value) -> {
			elementList.add(value.get(toElementIndex(listSize(key), rowIndex)));
		});
		
		return elementList;
	}
	
	/**
	 * 断言数据是否有改变，若数据改变，则返回true；反之，返回false
	 * @param oldElementList 原始数据元素列表
	 * @param newElementList 新数据元素列表
	 * @return 元素是否存在改变
	 */
	protected boolean assertDataChange(ArrayList<Element> oldElementList, ArrayList<Element> newElementList) {
		//获取两数组的长度
		int oldSize = oldElementList.size();
		int newSize = newElementList.size();
		
		//若两数组长度不一致，说明元素有改变，则返回true
		if (oldSize != newSize) {
			return true;
		}
		
		//若列表第一个元素与新列表第一个元素相同，说明列表并未改变，则返回false
		if (!oldElementList.get(0).equals(newElementList.get(0))) {
			return false;
		}
		
		//为避免不进行翻页时，列表也会进行一次刷新，则获取信息，对每个文本数据进行比对
		for (int index = 0; index < oldSize; index++) {
			if (assertEvent.assertNotEqualsElementText(oldElementList.get(index), newElementList.get(index))) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始，
	 * 故可通过该方法对下标的含义进行转义，得到java能识别的下标
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
	 * @param action 需要执行的内容
	 * @return 是否翻页成功
	 */
	private boolean operateSuchAssertData(BooleanSupplier action) {
		//若元素列表非空，则获取第一行元素，用于进行断言
		ArrayList<Element> oldElementList = new ArrayList<>();
		if (!tableMap.isEmpty()) {
			//获取第一行元素
			oldElementList = getRowElement(1);
		}
		
		//执行操作，并获取操作的返回结果；若返回值为true，则需要进行元素断言操作
		if (action.getAsBoolean()) {
			//获取操作后的第一行元素
			ArrayList<Element> newElementList = new ArrayList<>();
			if (!tableMap.isEmpty()) {
				//获取第一行元素
				newElementList = getRowElement(1);
				//断言元素，并返回结果
				return assertDataChange(oldElementList, newElementList);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public enum DataTableKeywordType {
		/**
		 * 上一页按钮
		 */
		UP_PAGE_BUTTON, 
		/**
		 * 下一页按钮
		 */
		DOWN_PAGE_BUTTON, 
		/**
		 * 首页按钮
		 */
		FIRST_PAGE_BUTTON,
		/**
		 * 尾页按钮
		 */
		LAST_PAGE_BUTTON, 
		/**
		 * 跳页按钮
		 */
		JUMP_PAGE_BUTTON,
		;
	}
	
	/**
	 * <p><b>文件名：</b>DataTableEvent.java</p>
	 * <p><b>用途：</b>
	 * 若元素列表无法添加时抛出的异常
	 * </p>
	 * <p><b>编码时间：</b>2020年11月19日下午8:26:49</p>
	 * <p><b>修改时间：</b>2020年11月19日下午8:26:49</p>
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
