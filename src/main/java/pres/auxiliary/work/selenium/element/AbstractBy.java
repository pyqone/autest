package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.location.AbstractRead;
import pres.auxiliary.work.selenium.location.ByType;

/**
 * <p><b>文件名：</b>AbstractElement.java</p>
 * <p><b>用途：</b></p>
 * <p>对辅助化测试工具selenium的获取元素代码进行的二次封装，通过类中提供的方法以及配合相应存储元素的
 * xml文件，以更简便的方式对页面元素进行获取，减少编程时的代码量。
 * </p>
 * <p><b>编码时间：</b>2020年4月25日 下午4:18:37</p>
 * <p><b>修改时间：</b>2020年4月25日 下午4:18:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractBy {
	/**
	 * 存储通过信息获取到的页面元素对象
	 */
	protected List<WebElement> elementList;
	
	/**
	 * 用于存储元素信息
	 */
	protected ElementData elementData;
	/**
	 * 存储元素的定位方式读取类对象，由于多个子类之间需要互通，故此处标记为static
	 */
	protected static AbstractRead read;
	
	/**
	 * 用于存储浏览器对象
	 */
	protected AbstractBrower brower;
	/**
	 * 存储当前定位的窗体层级，由于多个子类之间需要互通，故此处标记为static
	 */
	private static ArrayList<ElementData> nowIframeList = new ArrayList<>();
	
	/**
	 * 用于当元素获取失败时执行的方法
	 */
	protected ExceptionAction action;
	
	/**
	 * 用于存储元素通用的等待时间，默认5秒
	 */
	private long globalWaitTime = 5;
	
	/**
	 * 控制是否自动切换窗体，由于通过Event类调用时会构造另一个事件类
	 */
	protected boolean isAutoSwitchIframe = true;
	
	/**
	 * 通过元素信息类对象（{@link ElementData}）构造对象
	 * @param brower {@link AbstractBrower}类对象
	 * @param elementData {@link ElementData}类对象
	 */
	public AbstractBy(AbstractBrower brower) {
		this.brower = brower;
	}
	
	/**
	 * 设置元素查找失败时需要执行的方法
	 * @param action 执行方法
	 */
	public void setAction(ExceptionAction action) {
		this.action = action;
	}
	
	/**
	 * 用于清除元素查找失败时执行的方法
	 */
	public void clearAction() {
		this.action = null;
	}
	
	/**
	 * 用于设置元素的默认等待时间
	 * 
	 * @param waitTime 元素的默认等待时间
	 */
	public void setWaitTime(long waitTime) {
		this.globalWaitTime = waitTime;
	}
	
	/**
	 * 设置是否自动切换窗体
	 * @param isAutoSwitchIframe 是否自动切换窗体
	 */
	public void setAutoSwitchIframe(boolean isAutoSwitchIframe) {
		this.isAutoSwitchIframe = isAutoSwitchIframe;
	}

	/**
	 * 用于设置指向存储元素定位方式的xml文件对象，并根据传参，判断窗体是否需要回到顶层
	 * @param xmlFile 存储元素定位方式的xml文件对象
	 * @param isBreakRootFrame 是否需要将窗体切回到顶层
	 */
	public void setReadMode(AbstractRead read, boolean isBreakRootFrame) {
		AbstractBy.read = read;
		
		if (isBreakRootFrame) {
			switchRootFrame();
		}
	}
	
	/**
	 * 该方法用于将窗体切回顶层，当本身是在最顶层时，则该方法将使用无效
	 */
	public void switchRootFrame() {
		//切换窗口至顶层
		brower.getDriver().switchTo().defaultContent();
		//清空nowIframeNameList中的内容
		nowIframeList.clear();
	}
	
	/**
	 * 该方法用于将窗体切换到上一层（父层）。若当前层只有一层，则调用方法后切回顶层；
	 * 若当前层为最顶层时，则该方法将使用无效
	 * 
	 * @param count 需要切换父层的次数
	 */
	public void switchParentFrame(int count) {
		//判断count是否小于等于0，若小于等于0，则不进行切换
        if (count <= 0) {
        	return;
        }
        
        //判断count是否大于iframeElementList中所有元素的个数，若大于，则切回至顶层
        if (count > nowIframeList.size()) {
        	switchRootFrame();
            return;
        }
            
        //循环，对窗体进行切换，并移除nowIframeNameList中最后一个元素
        for (int index = 0; index < count; index++) {
        	brower.getDriver().switchTo().parentFrame();
        	nowIframeList.remove(nowIframeList.size() - 1);
        }
	}

	/**
	 * 该方法用于根据iframe_id来切换窗体
	 * 
	 * @param name 窗体的名称
	 */
	public void switchFrame(ElementData iframeElementData) {
		brower.getDriver().switchTo().frame(recognitionElement(iframeElementData).get(0));
		nowIframeList.add(iframeElementData);
	}
	
	/**
	 * 用于根据元素的窗体层级关系列表，对元素的窗体进行切换，并存储当前切换的窗体
	 * @param iframeNameList 元素所有窗体名称集合
	 */
	protected void autoSwitchFrame(List<String> iframeNameList) {
		//若传参为空，则切回到顶层
        if (iframeNameList.size() == 0) {
        	//若当前窗体也为空，则直接返回
            if (nowIframeList.size() == 0) {
            	return;
            }
            
            //当前窗体不为空时，则切换到顶层
            switchRootFrame();
            return;
        }
        
        //判断nowIframeNameList是否为空，或iframeNameList的第一个元素是否存在于nowIframeNameList中，
        //若不存在，则切回到顶层，并根据iframeList切换窗体
        ElementData firstIframeElementData = new ElementData(iframeNameList.get(0), read);
        if (nowIframeList.size() == 0 || nowIframeList.contains(firstIframeElementData)) {
        	switchRootFrame();
        	for(String iframeName : iframeNameList) {
        		switchFrame(new ElementData(iframeName, read));
        	}
             return;
        }
        
        //判断iframeElementList中的最后一个元素是否与iframeList中的最后一个元素一致，若一致，则无需切换
        ElementData lastIframeElementData = new ElementData(iframeNameList.get(iframeNameList.size() - 1), read);
        if (nowIframeList.get(nowIframeList.size() - 1).equals(lastIframeElementData)) {
        	return;
        }
        
        //遍历iframeList，查看iframeList中的窗体元素是否存在于iframeElementList中
        int index = 0;
        for (; index < iframeNameList.size(); index++) {
        	if (!nowIframeList.contains(new ElementData(iframeNameList.get(index), read))) {
        		break;
        	}
        }
        
        //计算需要向上切换窗体的个数
        //当窗体不存在于iframeElementList中时，其index以比存在的窗体下标大1位，故
        //index可直接用于计算需要切换窗体的个数
        switchParentFrame(nowIframeList.size() - index);
        
        //切换到相应的父层后，再根据iframeList剩余的元素进行切换
        for (; index < iframeNameList.size(); index++) {
        	switchFrame(new ElementData(iframeNameList.get(index), read));
        }
	}
	
	/**
	 * 根据元素的参数，返回元素的By对象
	 * @return 元素的By对象
	 */
	protected By getBy(String value, ByType byType) {
		//根据元素的定位方式，对定位内容进行选择，返回相应的By对象
		switch (byType) {
		case XPATH: 
			return By.xpath(value);
		case CLASSNAME:
			return By.className(value);
		case CSS:
			return By.cssSelector(value);
		case ID:
			return By.id(value);
		case LINKTEXT:
			return By.linkText(value);
		case NAME:
			return By.name(value);
		case TAGNAME:
			return By.tagName(value);
		default:
			throw new UnrecognizableLocationModeException("无法识别的定位类型：" + byType);
		}
	}
	
	/**
	 * 用于根据元素信息，在页面上对数据进行查找
	 * @param elementData {@link ElementData}类对象
	 * @return {@link WebElement}类对象{@link List}集合
	 */
	protected List<WebElement> recognitionElement(ElementData elementData) {
		//获取元素的定位类型及定位内容
		ArrayList<ByType> elementByTypeList = elementData.getByTypeList();
		ArrayList<String> elementValueList = elementData.getValueList();
        
        //获取两个列表长度的最小者
        int minLength = Math.min(elementByTypeList.size(), elementValueList.size());
        //循环，遍历所有的定位方式，使用根据定位方式，判断页面是否存在定位方式指向的元素
        for (int index = 0; index < minLength; index++) {
        	//根据当前元素信息，在页面获取元素
        	List<WebElement> elementList = findElement(elementByTypeList.get(index), 
        			elementValueList.get(index), 
        			elementData.getWaitTime() == -1 ? globalWaitTime : elementData.getWaitTime());
        	
        	//若获取到的元素列表为空，则继续循环，调用下一个定位方式
        	if (elementList.size() != 0) {
        		return elementList;
        	}
        }
        
        throw new TimeoutException("页面上无相应定位方式的元素，元素名称：" + elementData.getName());
	}
	
	/**
	 * 根据传入的定位方式枚举，以及定位内容，在页面查找
	 * 元素，返回查到的元素列表，若查不到元素，则返回空列表
	 * @param byType {@link ByType}枚举类
	 * @param value 元素定位内容
	 * @param waitTime 元素查找超时时间
	 * @return 页面查找到的{@link WebElement}类对象{@link List}集合
	 */
	protected List<WebElement> findElement(ByType byType, String value, long waitTime) {
		try {
			return new WebDriverWait(brower.getDriver(), waitTime)
					.until(driver -> driver.findElements(getBy(value, byType)));
		} catch (TimeoutException e) {
			return new ArrayList<WebElement>();
		}
	}
	
	/**
	 * <p><b>文件名：</b>AbstractBy.java</p>
	 * <p><b>用途：</b>
	 * 用于存储通过元素信息查找到的元素内容
	 * </p>
	 * <p><b>编码时间：</b>2020年10月13日上午8:02:38</p>
	 * <p><b>修改时间：</b>2020年10月13日上午8:02:38</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 *
	 */
	public class Element {
		/**
		 * 存储元素的获取下标
		 */
		private int index;
		
		/**
		 * 构造对象。其传入的下标允许为-1，表示当前元素集合中不存在该元素
		 * @param element 通过信息获取到的页面元素对象
		 * @param index 元素的获取下标
		 */
		public Element(int index) {
			this.index = index;
		}
		
		/**
		 * 用于返回当前存储的{@link WebElement}对象，若该对象为空，则抛出元素查找超时异常
		 * @return {@link WebElement}对象
		 * @throws TimeoutException 元素在页面不存在时抛出的异常
		 * @throws NoSuchElementException 元素集合不存在指定的下标时抛出的异常
		 */
		public WebElement getWebElement() {
			//判断元素集合是否为空，若为空，则抛出查找超时异常
			if (elementList == null || elementList.size() == 0) {
				throw new TimeoutException("页面上无相应定位方式的元素，当前元素名称：" + elementData.getName());
			}
			
			//判断元素下标是否为-1，若为-1，则抛出元素不存在异常
			if (index == -1) {
				throw new NoSuchElementException("指定的元素下标值不存在，当前元素集合个数：" + elementList.size());
			}
			
			return elementList.get(index);
		}
		
		/**
		 * 重新根据元素信息，在页面查找元素
		 */
		public void againFindElement() {
			elementList = recognitionElement(elementData);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Element other = (Element) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (index != other.index)
				return false;
			return true;
		}

		private AbstractBy getEnclosingInstance() {
			return AbstractBy.this;
		}
	}
}
