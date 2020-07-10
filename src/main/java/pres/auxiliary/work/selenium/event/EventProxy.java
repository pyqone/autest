package pres.auxiliary.work.selenium.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import pres.auxiliary.work.selenium.element.Element;

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

    /**
     * 构造代理类
     * @param target {@link AbstractEvent}类或其子类
     */
    public EventProxy(T target) {
        this.target = target;
    }

    /**
     * 创建{@link AbstractEvent}类或其子类的代理类对象，通过该类可调用
     * {@link AbstractEvent}类或子类的所有方法
     * @return {@link AbstractEvent}类或子类的代理类对象
     */
    @SuppressWarnings("unchecked")
	public T getProxyInstance(){
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return (T) en.create();
    }
    
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		//构造事件信息类对象
		EventInformation eventInformation = new EventInformation(method, args);
		
		//按照方法名或元素名，匹配到相应的通知后，运行通知，若通知运行出现异常，则直接跳过
		//运行方法前置通知
		functionBeforeActionMap.forEach((key, value) -> {
    		if (Pattern.compile(key).matcher(method.getName()).matches()) {
    			value.forEach(action -> {
    				//出现异常则不进行处理
    				try {
    					action.action(eventInformation);
					} catch (Exception e) {
					}
    			});
    		}
    	});
    	
		//运行元素前置通知
    	Arrays.stream(args).filter(arg -> arg instanceof Element).forEach(arg -> {
    		elementBeforeActionMap.forEach((key, value) -> {
    			if (Pattern.compile(key).matcher(((Element) arg).getName()).matches()) {
        			value.forEach(action -> {
        				try {
        					action.action(eventInformation);
						} catch (Exception e) {
						}
        			});
        		}
    		});
    	});
    	
        //执行目标对象的方法
    	try {
    		Object returnValue = method.invoke(target, args);
    		//运行方法成功执行通知
    		functionSuccessActionMap.forEach((key, value) -> {
        		if (Pattern.compile(key).matcher(method.getName()).matches()) {
        			value.forEach(action -> {
        				//出现异常则不进行处理
        				try {
        					action.action(eventInformation);
    					} catch (Exception e) {
    					}
        			});
        		}
        	});
    		return returnValue;
		} catch (Exception exception) {
			//运行方法失败执行通知
    		functionFailActionMap.forEach((key, value) -> {
        		if (Pattern.compile(key).matcher(method.getName()).matches()) {
        			value.forEach(action -> {
        				//出现异常则不进行处理
        				try {
        					action.action(eventInformation);
    					} catch (Exception e) {
    					}
        			});
        		}
        	});
			throw exception;
		} finally {
			//运行元素后置通知
	    	Arrays.stream(args).filter(arg -> arg instanceof Element).forEach(arg -> {
	    		elementAfterActionMap.forEach((key, value) -> {
	    			if (Pattern.compile(key).matcher(((Element) arg).getName()).matches()) {
	        			value.forEach(action -> {
	        				try {
	        					action.action(eventInformation);
							} catch (Exception e) {
							}
	        			});
	        		}
	    		});
	    	});
	    	
	    	//运行方法最终通知
    		functionFinalActionMap.forEach((key, value) -> {
        		if (Pattern.compile(key).matcher(method.getName()).matches()) {
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
	}
}
