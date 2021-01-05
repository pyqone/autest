package pres.auxiliary.selenium.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import pres.auxiliary.selenium.brower.AbstractBrower;
import pres.auxiliary.selenium.brower.ChromeBrower;
import pres.auxiliary.selenium.element.Element;

/**
 * <p><b>文件名：</b>EventProxy.java</p>
 * <p><b>用途：</b>
 * 用于对事件类进行代理，使其能在对应的事件方法执行前后能执行指定方法，以增强事件的执行。
 * 类中提供6种增强方法的方式，其名称与执行顺序为：方法前置通知、元素前置通知、方法成功/失败通知（两种）、
 * 元素后置通知、方法最终通知，解释如下：
 * <ol>
 * 	<li>方法前置通知：根据方法名，匹配到相应的方法后，在执行事件前执行的方法</li>
 * 	<li>元素前置通知：根据元素名称，匹配到传入方法的元素后，在执行事件前执行的方法</li>
 * 	<li>方法成功通知：根据方法名，匹配到相应的方法后，在事件成功执行（未抛出异常）后执行的方法</li>
 * 	<li>方法失败通知：根据方法名，匹配到相应的方法后，在事件失败执行（抛出异常）后执行的方法</li>
 * 	<li>元素后置通知：根据元素名称，匹配到传入方法的元素后，在执行事件后（无论是否抛出异常）执行的方法</li>
 * 	<li>方法最终通知：根据方法名，匹配到相应的方法后，在执行事件后（无论是否抛出异常）执行的方法</li>
 * </ol>
 * 可参考以下示例：
 * </p>
 * <p>
 * 假设存在元素xpah:元素1“//*[text()='登录']”、元素2“//*[@name='account']”、元素3“//*[@name='password']”，
 * 在点击元素1前，需要先在元素2和元素3中分别输入“admin”、“123456”，并且在此前定义了{@link ChromeBrower}浏览器对象，变量名为
 * chrome，此时，可以将代码写作<br>
 * <code><pre>
 * EventProxy &lt;ClickEvent&gt; clickEventProxy = new EventProxy(new ClickEvent(chrome.getDriver()));
 * 
 * clickProxy.addAcion(ActionType.ELEMENT_BEFORE, ".*登录.*", (info) -&gt; {
 * 			TextEvent text = inputProxy.getProxyInstance();
 * 			text.input(by.getElement("//*[@name='account']"), "admin");
 * 			text.input(by.getElement("//*[@name='password']"), "1111111");
 * 		});
 * clickEventProxy.getProxyInstance().click(new CommnBy(chrome).getElement("//*[text()='登录']"));
 * </pre></code>
 * </p>
 * <p><b>编码时间：</b>2020年7月12日 下午1:35:22</p>
 * <p><b>修改时间：</b>2020年10月20日下午7:54:15</p>
 * 
 * @version Ver1.0
 * @since JDK 8
 * @author 彭宇琦
 * 
 * @param <T> 继承自{@link AbstractEvent}的事件类
 */
public class EventProxy<T extends AbstractEvent> implements MethodInterceptor {
	/**
	 * 用于存储方法前置通知，key表示方法名称匹配规则，value表示调用的方法
	 */
	private LinkedHashMap<String, ArrayList<EventAction>> functionBeforeActionMap = new LinkedHashMap<>(16); 
	/**
	 * 用于存储元素前置通知，key表示元素名称匹配规则，value表示调用的方法
	 */
	private LinkedHashMap<String, ArrayList<EventAction>> elementBeforeActionMap = new LinkedHashMap<>(16);
	/**
	 * 用于存储元素后置通知
	 */
	private LinkedHashMap<String, ArrayList<EventAction>> elementAfterActionMap = new LinkedHashMap<>(16);
	/**
	 * 用于存储方法执行成功通知
	 */
	private LinkedHashMap<String, ArrayList<EventAction>> functionSuccessActionMap = new LinkedHashMap<>(16);
	/**
	 * 用于存储方法执行失败通知
	 */
	private LinkedHashMap<String, ArrayList<EventAction>> functionFailActionMap = new LinkedHashMap<>(16);
	/**
	 * 用于存储方法最终通知
	 */
	private LinkedHashMap<String, ArrayList<EventAction>> functionFinalActionMap = new LinkedHashMap<>(16);
	
	/**
     * 事件类对象
     */
    private T target;
    private AbstractBrower brower;

    /**
     * 构造代理类
     * @param target {@link AbstractEvent}类或其子类
     */
    public EventProxy(T target) {
        this.target = target;
        brower = ((AbstractEvent) target).getBrower();
    }

    /**
     * 创建{@link AbstractEvent}类或其子类的代理类对象，通过该类可调用
     * {@link AbstractEvent}类或子类的所有方法
     * @return {@link AbstractEvent}类或子类的代理类对象
     */
    @SuppressWarnings("unchecked")
	public T getProxyInstance(){
        //工具类
        Enhancer en = new Enhancer();
        //设置父类
        en.setSuperclass(target.getClass());
        //设置回调函数
        en.setCallback(this);
        //创建子类(代理对象)
        return (T) en.create(new Class[] {AbstractBrower.class}, new Object[] {brower});
//        return (T) en.create();
    }
    
    /**
     * 根据相应的执行的方法类型，添加指定的名称匹配规则及执行方法
     * @param actionType 通知类型枚举类{@link ActionType}
     * @param regex 名称匹配规则，元素名称或者方法名称，规则为正则表达式
     * @param action 需要执行的方法
     */
    public void addAcion(ActionType actionType, String regex, EventAction action) {
    	//指向需要添加通知的map
    	LinkedHashMap<String, ArrayList<EventAction>> actionMap = null;
    	//根据不同类型的通知，将actionMap指向相应的map
    	switch (actionType) {
		case FUNCTION_BEFORE:
			actionMap = functionBeforeActionMap;
			break;
		case ELEMENT_BEFORE:
			actionMap = elementBeforeActionMap;
			break;
		case FUNCTION_SUCCESS_AFTER:
			actionMap = functionSuccessActionMap;
			break;
		case FUNCTION_FAIL_AFTER:
			actionMap = functionFailActionMap;
			break;
		case ELEMENT_AFTER:
			actionMap = elementAfterActionMap;
			break;
		case FUNCTION_FINAL:
			actionMap = functionFinalActionMap;
			break;
		default:
			break;
		}
    	
    	//执行添加通知的方法
		mapAddAction(regex, action, actionMap);
    	
    }
    
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		//构造事件信息类对象
		EventInformation eventInformation = new EventInformation(method, args);
		
		//按照方法名或元素名，匹配到相应的通知后，运行通知，若通知运行出现异常，则直接跳过
		//运行方法前置通知
		runFuntionAction(eventInformation, functionBeforeActionMap);
    	
		//运行元素前置通知
		runElementAction(eventInformation, elementBeforeActionMap);
    	
        //执行目标对象的方法
    	try {
    		Object returnValue = method.invoke(target, args);
    		//运行方法成功执行通知
    		runFuntionAction(eventInformation, functionSuccessActionMap);
    		return returnValue;
		} catch (Exception exception) {
			//运行方法失败执行通知
			runFuntionAction(eventInformation, functionFailActionMap);
			throw exception;
		} finally {
			//运行元素后置通知
			runElementAction(eventInformation, elementAfterActionMap);
	    	
	    	//运行方法最终通知
	    	runFuntionAction(eventInformation, functionFinalActionMap);
		}
	}
	
	/**
	 * 运行方法通知
	 * @param eventInformation 事件信息类对象
	 * @param actionMap 需要执行的通知
	 */
	private void runFuntionAction(EventInformation eventInformation, LinkedHashMap<String, ArrayList<EventAction>> actionMap) {
		actionMap.forEach((key, value) -> {
    		if (Pattern.compile(key).matcher(eventInformation.getMethod().getName()).matches()) {
    			value.forEach(action -> {
    				//出现异常则不进行处理
    				try {
    					action.action(eventInformation);
					} catch (Exception e) {
					}
    			});
    		}
    	});
	}
	
	/**
	 * 执行元素通知
	 * @param eventInformation 事件类对象
	 * @param actionMap 需要执行的通知
	 */
	private void runElementAction(EventInformation eventInformation, LinkedHashMap<String, ArrayList<EventAction>> actionMap) {
    	Arrays.stream(eventInformation.getParam()).filter(arg -> arg instanceof Element).forEach(arg -> {
    		actionMap.forEach((key, value) -> {
    			if (Pattern.compile(key).matcher(((Element) arg).getElementData().getName()).matches()) {
        			value.forEach(action -> {
        				try {
        					action.action(eventInformation);
						} catch (Exception e) {
						}
        			});
        		}
    		});
    	});
	}
	
	/**
	 * 用于向相应的通知map中添加通知
	 * @param regex 匹配规则
	 * @param action 通知
	 * @param actionMap 存储通知的map
	 */
	private void mapAddAction(String regex, EventAction action, LinkedHashMap<String, ArrayList<EventAction>> actionMap) {
		//判断actionMap中是否存在regex，若不存在，则将regex加入到actionMap，并构造ArrayList类
		if (!actionMap.containsKey(regex)) {
			actionMap.put(regex, new ArrayList<EventAction>());
		}
		
		//在相应的regex中添加action
		actionMap.get(regex).add(action);
	}
	
	/**
	 * <p><b>文件名：</b>EventProxy.java</p>
	 * <p><b>用途：</b>用于标记通知的类型</p>
	 * <p><b>编码时间：</b>2020年7月12日 上午11:24:57</p>
	 * <p><b>修改时间：</b>2020年7月12日 上午11:24:57</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 8
	 */
	public enum ActionType {
		/**
		 * 表示方法前置通知
		 */
		FUNCTION_BEFORE, 
		/**
		 * 表示元素前置通知
		 */
		ELEMENT_BEFORE, 
		/**
		 * 表示方法执行成功通知
		 */
		FUNCTION_SUCCESS_AFTER, 
		/**
		 * 表示方法执行失败通知
		 */
		FUNCTION_FAIL_AFTER, 
		/**
		 * 表示元素后置通知
		 */
		ELEMENT_AFTER, 
		/**
		 * 表示方法最终通知
		 */
		FUNCTION_FINAL;
	}
}
