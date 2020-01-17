package pres.auxiliary.selenium.event;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.inter.ClickInter;
import pres.auxiliary.selenium.event.inter.DataListEventInter;
import pres.auxiliary.selenium.event.inter.DoubleClickInter;
import pres.auxiliary.selenium.event.inter.GetAttributeValueInter;
import pres.auxiliary.selenium.event.inter.GetTextInter;
import pres.auxiliary.selenium.event.inter.JudgeKeyInter;
import pres.auxiliary.selenium.event.inter.JudgeTextInter;
import pres.auxiliary.selenium.event.inter.RightClickInter;
import pres.auxiliary.selenium.tool.RecordTool;

/**
 * <p>
 * <b>文件名：</b>WebElementList.java
 * </p>
 * <p>
 * <b>用途：</b>提供对页面多个使用相近定位方式的元素进行获取的方法，并根据get方法返回获得每个 元素后可对其元素进行基本操作的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2019年7月19日下午3:23:50
 * </p>
 * <p>
 * <b>修改时间：</b>2019年11月29日上午9:53:37
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.3
 * @since JDK 12
 *
 */
public class DataListEvent extends ListElementEvent {
	/**
	 * 构造对象，存储储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public DataListEvent(WebDriver driver) {
		super(driver);
	}

	/**
	 * 该方法用于根据存入的元素名称或定位方式，对元素进行重新获取的操作。主要用于当列表数据翻页后，
	 * 其原存入的数据将会失效，必须重新获取。注意，调用该方法后会清空原存储的数据。
	 */
	public void againGetElement() {
		// 读取elements中的元素
		elements.forEach((name, list) -> {
			// 清空元素中的内容
			clear(name);
			// 对页面内容重新进行获取
			add(name);
		});
	}

	/**
	 * 用于根据下标移除下标所在行的整行元素，下标允许传入负数，意为由后向前遍历
	 * 
	 * @param index 下标
	 * 
	 * @throws NoSuchElementException 传入的元素列或元素不存在时抛出的异常
	 */
	public void removeLineElement(int index) {
		// 遍历整行元素，并调用removeElement方法删除对应列的元素，若存在异常，则跳过不处理
		// 由于当传入0时会出现问题，但lambda表达式不能为变量赋值，故只能使用普通循环
		for (String name : elements.keySet()) {
			// 若下标为0，则先进行下标获取操作，并赋予index真实下标
			index = index == 0 ? getIndex(name, index) : index;

			// 判断是否能获取到元素，若不能获取到元素，则使用第二种构造方法
			try {
				removeElement(name, index);
			} catch (NoSuchElementException e) {
				throw e;
			}
		}
	}

	/**
	 * 用于返回最短列的长度
	 * 
	 * @return 最短列的长度
	 */
	public String shortListName() {
		// 初始化minSize，为int的最大值
		int minSize = Integer.MAX_VALUE;
		String name = "";
		// 循环，遍历所有的key，获取其长度，查找最短的进行存储
		for (String key : elements.keySet()) {
			if (size(key) < minSize) {
				minSize = size(key);
				name = key;
			}
		}

		return name;
	}

	/**
	 * 用于根据根据指定的列下标返回相应列元素的个数，该方法根据存储时的顺序指定其列对应的下标，
	 * 若需要通过列表返回元素个数，可使用{@link #size(String)}进行返回
	 * 
	 * @param columnNum 存储时相应列的下标
	 * @return 相应列元素的个数
	 */
	public int columnLength(int columnNum) {
		//存储元素集合
		Set<String> elemensSet = elements.keySet();
		//存储最大下标
		int maxListIndex = elemensSet.size();
		
		//判断columnNum是否符合标准
		if (columnNum < 0 || columnNum > maxListIndex) {
			throw new ArrayIndexOutOfBoundsException("不存在的列下标：" + columnNum + "（列个数为：）" + maxListIndex);
		}
		
		//循环，遍历元素下标，返回其相应的列下标的元素个数
		int i = 0;
		for (String name : elements.keySet()) {
			//判断当前循环的下标是否与columnNum相同，若相同，则返回相应的name
			if (i == columnNum) {
				return size(name);
			}
			//若不同，则循环下标+1
			i++;
		}
		
		return -1;
	}
	
	/**
	 * 用于返回添加列的个数，即一行元素的个数，若某一列元素不存在，则每行元素个数同样将该行包含
	 * @return 添加列的个数
	 */
	public int rowLength() {
		//存储元素集合
		return elements.keySet().size();
	}

	/**
	 * 用于对列中元素的文本进行筛选，返回符合筛选项序号
	 * 
	 * @param name  列名
	 * @param index 筛选后所在的列下标（下标从1开始，1表示第一个元素）
	 */
	public int[] filterText(String name, String key, boolean keyFull) {
		ArrayList<Integer> indexList = new ArrayList<>();

		// 筛选文本
		// 记录当前是否开启自动记录步骤的状态，并设置关闭自动记录操作步骤，加快操作进程
		boolean recordStep = RecordTool.isRecordStep();
		RecordTool.setRecordStep(false);

		// 由于获取方法可能会出现异常，故使用finally，以保证无论获取方法是否有异常也能还原记录操作步骤的状态
		try {
			getEvents(name).forEach(event -> {
				// 调用judgeText()方法进行判断，若符合要求，则indexList进行存储
				if (event.judgeText(keyFull, key).getBooleanValue()) {
					indexList.add(event.getIndex());
				}
			});
		} catch (Exception e) {
			// 抛出异常
			throw e;
		} finally {
			// 还原自动记录操作步骤的状态
			RecordTool.setRecordStep(recordStep);
		}

		// 将ArrayList转换成数组
		int[] indexs = new int[indexList.size()];
		for (int i = 0; i < indexs.length; i++) {
			indexs[i] = indexList.get(i);
		}

		return indexs;
	}

	/**
	 * 根据指定的翻页按钮，以及翻页次数，对列表进行翻页操作。该方法会自动判定是否翻页成功，但以下场景下
	 * 将无法自动判定翻页是否成功：
	 * <ol>
	 * 	<li>
	 * 		当按钮的disable属性无法直接获取到（其disable卸载css中或style中时），
	 * 		且列表的最后一页元素量与前一页一致时无法判断
	 * 	</li> 
	 * 	<li>
	 * 		当按钮在最后一页时仍能继续点击且且列表的最后一页元素量与前一页一致时无法判断
	 * 	</li> 
	 * </ol>
	 * @param turnCount 翻页次数
	 * @param buttonName 翻页按钮
	 * @param sleepTime 翻页时间间隔，单位为毫秒
	 * 
	 * @throws TimeoutException 翻页按钮查找不到或数据加载时间过长导致翻页按钮无法点击时抛出
	 */
	public void pageTurning(int turnCount, String buttonName, long sleepTime) {
		//判断翻页数是否小于等于0，若小于等于0，则直接返回，不进行操作
		if (turnCount <= 0) {
			return;
		}
		
		//构造点击事件对象
		ClickEvent ce = new ClickEvent(getDriver());
		
		//记录当前步骤记录情况，为节约翻页事件，则先关闭自动记录步骤功能，在循环结束后再还原
		boolean recordStep = RecordTool.isRecordStep();
		RecordTool.setRecordStep(false);
		//记录实际翻页数
		int actualPageCount = 0;
		//循环，对列表元素进行翻页
		for (; actualPageCount < turnCount; actualPageCount++) {
			//获取翻页前第一列的元素个数
			int oldElementNum = columnLength(0);
			
			//点击翻页按钮
			//若抛出TimeoutException异常，则表示翻页按钮的指向有问题，则抛出该异常
			//若抛出其他异常，则表示下一页按钮可能无法点击，则结束循环
			//TODO 以上描述只表示已知的情况，若存在其他异常且不是按钮无法点击的情况下，则需要组织记录
			try {
				ce.click(buttonName);
			} catch (ElementClickInterceptedException elementClickInterceptedException) {
				RecordTool.recordException(new TimeoutException("页面未完全加载，翻页按钮点击超时"));
				throw new TimeoutException("页面未完全加载，翻页按钮点击超时");
			} catch (TimeoutException timeoutException) {
				throw timeoutException;
			} catch (Exception e) {
				break;
			}
			
			//等待一定时间后再运行翻页
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
			}
			
			//重新获取列表元素
			againGetElement();
			
			//获取翻页后第一列的元素个数
			int nowElementNum = columnLength(0);
			
			//对比翻页前后第一列元素的个数，若元素个数不同，则表示当前以翻到最后一页，则结束循环
			if (nowElementNum != oldElementNum) {
				break;
			}
		}
		//还原之前的自动记录情况
		RecordTool.setRecordStep(recordStep);
		
		RecordTool.recordStep("点击" + buttonName + "指向的按钮，对列表进行" + turnCount + "翻页（实际翻页为" + actualPageCount + "页）");
	}

	/**
	 * 该方法用于根据控件名称或定位方式，提供需要操作列表元素的下标，对相应的元素进行操作
	 * 
	 * @param name  控件的名称或xpath与css定位方式
	 * @param index 元素下标（即列表中对应的某一个元素）
	 * @return 元素的可执行的事件
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 */
	public ListEvent getEvent(String name, int index) {
		// 转义下标
		index = getIndex(name, index);

		// 判断元素是否存在
		if (elements.containsKey(name)) {
			// 转义下标后，返回对应的元素
			return new ListEvent(elements.get(name).get(index), name, index + 1);
		} else {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
	}

	/**
	 * 该方法用于根据控件名称或定位方式，获取该列下所有的元素
	 * 
	 * @param name 控件的名称或xpath与css定位方式
	 * @return 一组元素的可执行的事件
	 * @throws NoSuchElementException 当未对name列进行获取数据时抛出的异常
	 */
	public ArrayList<ListEvent> getEvents(String name) {
		if (elements.containsKey(name)) {
			ArrayList<ListEvent> events = new ArrayList<>();
			int index = 1;

			for (WebElement element : elements.get(name)) {
				events.add(new ListEvent(element, name, index++));
			}

			return events;
		} else {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
	}

	/**
	 * 用于返回多个指定的列表数据的事件
	 * 
	 * @param name   控件的名称或xpath与css定位方式
	 * @param indexs 一组元素下标（即列表中对应的某一个元素）
	 * @return 指定下标的事件组
	 */
	public ArrayList<ListEvent> getEvents(String name, int... indexs) {
		// 存储所有获取到的事件
		ArrayList<ListEvent> events = new ArrayList<>();

		// 循环，解析所有的下标，并调用getEvent()方法，存储至events
		for (int index : indexs) {
			events.add(getEvent(name, index));
		}

		return events;
	}

	/**
	 * 用于返回指定个数列表事件，其列表的行下标将随机生成
	 * 
	 * @param name   控件的名称或xpath与css定位方式
	 * @param length 需要返回列表事件的个数
	 * @return 列表事件组
	 */
	public ArrayList<ListEvent> getRandomCountEvent(String name, int length) {
		// 判断元素是否存在，若元素不存在抛出异常
		if (!elements.containsKey(name)) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}

		// 判断传入的长度是否大于等于当前
		if (length >= elements.get(name).size()) {
			return getEvents(name);
		}

		// 存储通过随机得到的数字
		ArrayList<Integer> indexsList = new ArrayList<Integer>();
		// 循环，随机获取下标数字
		for (int i = 0; i < length; i++) {
			int randomIndex = 0;
			// 循环，直到生成的随机数不在indexs中为止
			while (indexsList.contains(randomIndex = new Random().nextInt(elements.get(name).size()) + 1)) {
			}
			indexsList.add(randomIndex);
		}

		// 将indexsList转换成int[]
		int[] indexs = new int[indexsList.size()];
		for (int i = 0; i < indexsList.size(); i++) {
			indexs[i] = indexsList.get(i);
		}

		return getEvents(name, indexs);
	}

	/**
	 * 该方法用于根据指定的行数获取一行元素，当元素
	 * 
	 * @param index 行数（元素下标所在的行）
	 * @return 指定行的事件组
	 */
	public LinkedHashMap<String, ListEvent> getLineEvent(int index) {
		// 存储一行元素
		LinkedHashMap<String, ListEvent> events = new LinkedHashMap<>(16);
		// 由于当传入0时会出现问题，但lambda表达式不能为变量赋值，故只能使用普通循环
		for (String name : elements.keySet()) {
			// 若下标为0，则先进行下标获取操作，并赋予index真实下标
			index = index == 0 ? getIndex(name, index) : index;

			// 判断是否能获取到元素，若不能获取到元素，则使用第二种构造方法
			ListEvent event;
			try {
				event = getEvent(name, index);
			} catch (NoSuchElementException e) {
				event = new ListEvent(name, index);
			}
			// 存储元素
			events.put(name, event);
		}

		return events;
	}

	/**
	 * <p>
	 * <b>文件名：</b>DataListEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于定义列表元素的操作事件
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2019年10月8日下午7:08:16
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2019年10月8日下午7:08:16
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	public class ListEvent implements DataListEventInter {
		/**
		 * 用于存储当前获取的页面元素
		 */
		private WebElement element;

		/**
		 * 用于存储当前元素的下标
		 */
		private int index;

		/**
		 * 用于存储列表的名称
		 */
		private String name;

		/**
		 * 构造对象
		 * 
		 * @param elemnet WebElement对象
		 * @param name    列表的名称
		 * @param index   当前元素的下标
		 */
		public ListEvent(WebElement element, String name, int index) {
			this.element = element;
			this.index = index;
			this.name = name;
		}

		public ListEvent(String name, int index) {
			this.element = null;
			this.index = index;
			this.name = name;
		}

		/**
		 * 用于返回当前元素的下标
		 * 
		 * @return 当前元素的下标
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * 用于判断元素是否为空，若为空，则抛出元素为空的异常
		 */
		private void isEmptyElement() {
			if (element == null) {
				throw new NoSuchElementException("不存在的元素；列名：" + name + "，行数：" + index);
			}
		}

		@Override
		public Event click() {
			// 判断元素是否为空
			isEmptyElement();

			try {
				// 修饰元素
				elementHight(element);

				// 操作元素
				ClickInter.click(element);
			} catch (Exception exception) {
				// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
				RecordTool.recordException(exception);
				throw exception;
			} finally {
				RecordTool.recordStep("点击“" + name + "”对应列的第" + index + "条数据");
			}

			// 返回Event类
			return Event.newInstance(getDriver());
		}

		@Override
		public Event doubleClick() {
			// 判断元素是否为空
			isEmptyElement();

			try {
				// 修饰元素
				elementHight(element);

				// 操作元素
				DoubleClickInter.doubleClick(getDriver(), element);
			} catch (Exception exception) {
				// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
				RecordTool.recordException(exception);
				throw exception;
			} finally {
				RecordTool.recordStep("双击“" + name + "”对应列的第" + index + "条数据");
			}

			// 返回Event类
			return Event.newInstance(getDriver());
		}

		@Override
		public Event rightClick() {
			// 判断元素是否为空
			isEmptyElement();

			try {
				// 修饰元素
				elementHight(element);

				// 操作元素
				RightClickInter.rightClick(getDriver(), element);
			} catch (Exception exception) {
				// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
				RecordTool.recordException(exception);
				throw exception;
			} finally {
				RecordTool.recordStep("右击“" + name + "”对应列的第" + index + "条数据");
			}

			// 返回Event类
			return Event.newInstance(getDriver());
		}

		@Override
		public Event getAttributeValue(String attributeName) {
			// 判断元素是否为空
			isEmptyElement();

			// 自动记录异常
			try {
				// 修饰元素
				elementHight(element);

				// 操作元素
				// 调用GetAttributeValueInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
				GetAttributeValueInter.getAttributeValue(element, attributeName);
			} catch (Exception exception) {
				// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
				RecordTool.recordException(exception);
				throw exception;
			} finally {
				RecordTool.recordStep("获取“" + name + "”对应列的第" + index + "条数据" + attributeName + "属性的内容");
			}

			// 返回Event类
			return Event.newInstance(getDriver());
		}

		@Override
		public Event getText() {
			// 判断元素是否为空
			isEmptyElement();

			// 自动记录异常
			try {
				// 修饰元素
				elementHight(element);

				// 操作元素
				// 调用GetTextInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
				GetTextInter.getText(element);
			} catch (Exception exception) {
				// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
				RecordTool.recordException(exception);
				throw exception;
			} finally {
				RecordTool.recordStep("获取“" + name + "”对应列的第" + index + "条数据的内容");
			}

			// 返回Event类
			return Event.newInstance(getDriver());
		}

		@Override
		public Event judgeKey(boolean keyFull, String... keys) {
			// 判断元素是否为空
			isEmptyElement();

			// 自动记录异常
			try {
				// 修饰元素
				elementHight(element);

				// 操作元素
				// 调用JudgeKeyInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
				JudgeKeyInter.judgeKey(element, keyFull, keys);
			} catch (Exception exception) {
				// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
				RecordTool.recordException(exception);
				throw exception;
			} finally {
				// 记录步骤
				// 拼接传入的关键词
				String text = "";
				for (String key : keys) {
					text += (key + "、");
				}
				text = text.substring(0, text.length() - 1);

				// 根据keyFull的不同记录的文本将有改变
				if (keyFull) {
					RecordTool.recordStep("判断“" + name + "”对应列的第" + index + "条数据中的内容是否包含所有关键词（关键词为：" + text + "）");
				} else {
					RecordTool.recordStep("判断“" + name + "”对应列的第" + index + "条数据中的内容是否包含部分关键词（关键词为：" + text + "）");
				}
			}

			// 返回Event类
			return Event.newInstance(getDriver());
		}

		@Override
		public Event judgeText(boolean keyFull, String key) {
			// 判断元素是否为空
			isEmptyElement();

			// 自动记录异常
			try {
				// 修饰元素
				elementHight(element);

				// 操作元素
				// 调用JudgeTextInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
				JudgeTextInter.judgeText(element, keyFull, key);
			} catch (Exception exception) {
				// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
				RecordTool.recordException(exception);
				throw exception;
			} finally {
				// 根据keyFull的不同记录的文本将有改变
				if (keyFull) {
					RecordTool.recordStep("判断“" + name + "”对应列的第" + index + "条数据中的内容是否与所有关键词“" + key + "”一致");
				} else {
					RecordTool.recordStep("判断“" + name + "”对应列的第" + index + "条数据中的内容是否包含关键词“" + key + "”");
				}
			}

			// 返回Event类
			return Event.newInstance(getDriver());
		}
	}
}
