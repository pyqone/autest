package pres.auxiliary.work.selenium.brower;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.alibaba.fastjson.JSONArray;

import pres.auxiliary.work.selenium.page.Page;

/**
 * <p><b>文件名：</b>ChromeBrower.java</p>
 * <p><b>用途：</b>用于启动谷歌浏览器，并加载相应的待测页面，支持对浏览器进行部分个性化的配置，
 * 以达到相应的测试效果。</p>
 * <p>启动浏览器需要调用{@link #getDriver()}方法启动浏览器，若在构造方法中定义了{@link Page}类，则
 * 启动浏览器时会自动对页面进行加载，若未定义，则只打开浏览器，如:</p>
 * <p>
 * 若调用方法：
 * <pre><code>
 * ChromeBrower brower = new {@link #ChromeBrower(File)}
 * brower.{@link #getDriver()}
 * </code></pre>
 * 后将只全屏打开浏览器，不会加载页面；若调用方法：
 * <pre><code>
 * ChromeBrower brower = new {@link #ChromeBrower(File, Page)}
 * //或ChromeBrower brower = new {@link #ChromeBrower(File, String, String)}
 * brower.{@link #getDriver()}
 * </code></pre>
 * 后将全屏打开浏览器，并加载相应的页面
 * </p>
 * <p>对于个性化配置，在调用{@link #getDriver()}方法前调用{@link #addConfig(ChromeOptionType)}
 * 或{@link #addConfig(ChromeOptionType, Object)}方法，通过枚举类{@link ChromeOptionType}
 * 向浏览器添加相应的配置，在启动浏览器时，添加的配置会自动加载。</p>
 * <p>若添加配置在调用{@link #getDriver()}方法之后，则设置的配置不会生效。</p>
 * <p><b>编码时间：</b>2020年4月17日下午2:56:08</p>
 * <p><b>修改时间：</b>2020年11月7日下午7:12:48</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since Selenium 3.14.0
 * @since JDK 1.8
 *
 */
public class ChromeBrower extends AbstractBrower {
	/**
	 * 用于存储需要对浏览器进行配置的参数
	 */
//	HashSet<ChromeOptionType> chromeConfigSet = new HashSet<ChromeOptionType>();
	/**
	 * 用于存储谷歌浏览器个性化配置
	 */
	HashMap<String, Object> chromePrefsMap = new HashMap<String, Object>(16);
	/**
	 * 用于存储谷歌浏览器对手机的配置
	 */
	HashMap<String, Object> mobileEmulationMap = new HashMap<String, Object>(16);
	/**
	 * 用于指向当前配置中是否已打开控制当前浏览器模式
	 */
	boolean isContralOpenBrower = false;
			
	/**
	 * 用于对谷歌浏览器的配置
	 */
	ChromeOptions chromeOption = new ChromeOptions();
	
	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driverFile 驱动文件对象
	 * @param page       {@link Page}类对象
	 */
	public ChromeBrower(File driverFile, Page page) {
		super(driverFile, page);
	}

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driverFile 驱动文件对象
	 * @param url        待测站点
	 * @param pageName   待测站点名称，用于切换页面
	 */
	public ChromeBrower(File driverFile, String url, String pageName) {
		super(driverFile, url, pageName);
	}

	/**
	 * 指定驱动文件所在路径
	 * 
	 * @param driverFile 驱动文件对象
	 */
	public ChromeBrower(File driverFile) {
		super(driverFile);
	}

	/**
	 * 用于添加浏览器需要带参数的配置，参数需要严格按照枚举中的说明进行配置，否则配置不会生效。 若添加的配置是不需要传入参数的，
	 * 则将忽略传入的参数，无参配置可以使用{@link #addConfig(ChromeOptionType)} 方法进行添加
	 * 
	 * @param chromeOptionType 浏览器配置枚举（{@link ChromeOptionType}枚举类）
	 * @param value      需要设置的值
	 */
	public void addConfig(ChromeOptionType chromeOptionType, Object value) {
		chromeOptionType.setValue(value);
		addConfig(chromeOptionType);
	}
	
	/**
	 * 用于向浏览器中添加谷歌浏览器允许设置的个性化配置。如若设置启动的浏览器不弹出窗口，则可以
	 * 按照以下配置：<br>
	 * key = profile.managed_default_content_settings.popups<br>
	 * value = 2
	 * 
	 * @param key 配置在浏览器对应的键值
	 * @param value 其配置对应的传参
	 */
	public void addPersonalityConfig(String key, Object value) {
		chromePrefsMap.put(key, value);
		chromeOption.setExperimentalOption("prefs", chromePrefsMap);
		getConfigJsonArray().add(key);
	}
	
	/**
	 * 用于添加浏览器无需带参数的配置
	 * 
	 * @param chromeOptionType 浏览器配置枚举（{@link ChromeOptionType}枚举类）
	 */
	public void addConfig(ChromeOptionType chromeOptionType) {
		browerConfig(chromeOptionType);
	}
	
	/**
	 * <p>
	 * 用于设置谷歌浏览器可执行文件路径，在启动浏览器时，将打开该路径下的浏览器。
	 * 通过该方法可用于打开为谷歌浏览器内核的浏览器
	 * </p>
	 * <p>
	 * 	<b>注意：</b>若浏览器启动后再设置浏览器启动路径时，会重新打开一个新的浏览器，且
	 * 	{@link WebDriver}对象将重新构造，指向新打开的浏览器
	 * </p>
	 * @param path 浏览器执行路径
	 */
	public void setBinary(File path) {
		chromeOption.setBinary(path);
		driver = null;
	}

	/**
	 * 清空所有的配置，调用该方法后等同于重新构造{@link ChromeOptions}类对象
	 */
	public void clearConfig() {
		chromePrefsMap.clear();
		mobileEmulationMap.clear();
		chromeOption = new ChromeOptions();
	}
	
	/**
	 * 用于返回谷歌浏览器的配置，可通过此方法，获取到{@link ChromeOptions}对象后，在
	 * 外部对浏览器进行配置
	 * @return 谷歌浏览器的配置
	 */
	public ChromeOptions getConfig() {
		return chromeOption;
	}

	@Override
	void openBrower() {
		driver = new ChromeDriver(chromeOption);
	}
	
	@Override
	String getBrowerDriverSetName() {
		return "webdriver.chrome.driver";
	}

	/**
	 * 用于将配置添加至浏览器中
	 * @param configType
	 */
	void browerConfig(ChromeOptionType configType) {
		//若已打开控制启动的浏览器配置，则所有配置将不再生效
		if (isContralOpenBrower) {
			return;
		}
				
		JSONArray configJsonArray = getConfigJsonArray();
		
		//若当前配置为控制已开启的浏览器，则清空之前的配置，以避免调用报错
		if (configType == ChromeOptionType.CONTRAL_OPEN_BROWER) {
			clearConfig();
			chromeOption.setExperimentalOption(ChromeOptionType.CONTRAL_OPEN_BROWER.getKey(), String.valueOf(ChromeOptionType.CONTRAL_OPEN_BROWER.getValue()));
			isContralOpenBrower = true;
		}
		
		// 遍历所有的chromeConfigSet，对浏览器进行相应的设置
		// 根据设置的类型来指定使用哪种方法
		// 0表示使用addArguments（启动配置项）配置
		// 1表示setExperimentalOption（个性化配置）需要存储至map的配置
		// 2表示setExperimentalOption（个性化配置）不需要存储至map的配置
		// 3表示使用addArguments（启动配置项）配置，但需要拼接参数
		switch (configType.getOptionType()) {
		case 0:
			chromeOption.addArguments(configType.getKey());
			//添加信息
			configJsonArray.add(configType.getName());
			break;
		case 1:
			chromePrefsMap.put(configType.getKey(), configType.getValue());
			chromeOption.setExperimentalOption("prefs", chromePrefsMap);
			configJsonArray.add(configType.getName());
			break;
		case 2:
			chromeOption.setExperimentalOption(configType.getKey(), String.valueOf(configType.getValue()));
			configJsonArray.add(configType.getName() + (configType.getValue() == null ? "" : String.valueOf(configType.getValue())));
			break;
		case 3:
			// 拼接内容
			chromeOption.addArguments(configType.getKey() + String.valueOf(configType.getValue()));
			configJsonArray.add(configType.getName() + (configType.getValue() == null ? "" : String.valueOf(configType.getValue())));
			break;
		default:
			throw new IllegalArgumentException("错误的类型: " + configType.getOptionType());
		}

		// 添加配置
//		chromeOption.setExperimentalOption("mobileEmulation", mobileEmulationMap);

		return;
	}
	
	/**
	 * 用于返回配置信息集合
	 * @return 配置信息集合
	 */
	JSONArray getConfigJsonArray() {
		// 添加配置信息
		if (!informationJson.containsKey("浏览器配置")) {
			informationJson.put("浏览器配置", new JSONArray());
		}
		return informationJson.getJSONArray("浏览器配置");
	}

	/**
	 * <p>
	 * <b>文件名：</b>ChromeBrower.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于枚举出谷歌浏览器可以配置的参数
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年4月14日下午7:57:36
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年4月14日下午7:57:36
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public enum ChromeOptionType {
		/**
		 * 用于指定控制在本地或网络上已启动的浏览器，可以指定一个字符串参数，
		 * 表示被开启的浏览器的位置，如传入“127.0.0.1:9222”，则表示控制在"127.0.0.1:9222"上的浏览器<br>
		 * <b>注意：控制已打开的浏览器时，其他所有的配置均不会生效</b>
		 */
		CONTRAL_OPEN_BROWER("debuggerAddress", "启动已开启的浏览器", (short) 2),
		/**
		 * 用于指定浏览器加载的页面不允许弹窗，无需指定参数
		 */
		DONOT_POPUPS("profile.managed_default_content_settings.popups", "禁止弹窗", (short) 1, 2),
		/**
		 * 用于指定浏览器下载的文件路径，可以指定一个字符串参数，表示文件下载的位置，当指定有误时则下载至默认路径
		 */
		DOWNLOAD_FILE_PATH("download.default_directory", "文件下载路径", (short) 1),
		/**
		 * 用于指定浏览器加载的页面不允许加载图片，无需指定参数
		 */
		DONOT_LOAD_IMAGE("profile.managed_default_content_settings.images", "禁止加载图片", (short) 1, 2),
		/**
		 * 用于指定浏览器加载的页面不允许加载javascript，无需指定参数
		 */
		DONOT_LOAD_JS("profile.managed_default_content_settings.javascript", "禁止加载js", (short) 1, 2),
		/**
		 * 用于指定使用无头浏览器运行，无需指定参数
		 */
		HEADLESS("-headless", "启动无头浏览器", (short) 0),
		/**
		 * 用于设置浏览器以何种分辨率启动，需要指定一个字符串参数，以“宽, 长”的格式传入， 如需要使用“1024*768”的分辨率，则传入“"1027,
		 * 768"”，否则设置不会生效。<br>
		 * 注意，使用该参数设置时，会与浏览器全屏操作相矛盾，selenium会以全屏为主
		 */
		SET_WINDOW_SIZE("--window-size=", "设置浏览器分辨率", (short) 3),
		/**
		 * 设置浏览器全屏启动
		 */
		SET_WINDOW_MAX_SIZE("--start-maximized", "设置浏览器全屏", (short) 0);
		// TODO 添加忽略证书错误、隐藏“正在受自动测试....”提示、模拟手机等
		;
		/**
		 * 用于指向谷歌设置的key值
		 */
		private String key;

		/**
		 * 用于指向对操作的解释（即存储在information中的名称）
		 */
		private String name;

		/**
		 * 用于指向该设置操作的类型
		 * <ol>
		 * <li>0表示使用addArguments（启动配置项）配置</li>
		 * <li>1表示setExperimentalOption（个性化配置）需要存储至map的配置</li>
		 * <li>2表示setExperimentalOption（个性化配置）不需要存储至map的配置</li>
		 * <li>3表示使用addArguments（启动配置项）配置，但需要拼接参数</li>
		 * </ol>
		 */
		private short optionType;

		/**
		 * 用于存储对操作设置的值
		 */
		private Object value;

		/**
		 * 初始化配置
		 * 
		 * @param key        谷歌设置的key值
		 * @param name       操作的解释
		 * @param optionType 操作的类型
		 */
		private ChromeOptionType(String key, String name, short optionType) {
			this.key = key;
			this.name = name;
			this.optionType = optionType;
		}

		/**
		 * 初始化配置
		 * 
		 * @param key        谷歌设置的key值
		 * @param name       操作的解释
		 * @param optionType 操作的类型
		 * @param value      对枚举设置的值
		 */
		private ChromeOptionType(String key, String name, short optionType, Object value) {
			this.key = key;
			this.name = name;
			this.optionType = optionType;
			this.value = value;
		}

		/**
		 * 返回谷歌浏览器配置的key值
		 * 
		 * @return 谷歌浏览器配置的key值
		 */
		public String getKey() {
			return key;
		}

		/**
		 * 返回谷歌浏览器配置的解释，用于存储至information中使用
		 * 
		 * @return 谷歌浏览器配置的解释
		 */
		public String getName() {
			return name;
		}

		/**
		 * 返回对浏览器配置的操作类型
		 * <ol>
		 * <li>0表示使用addArguments（启动配置项）配置</li>
		 * <li>1表示setExperimentalOption（个性化配置）需要存储至map的配置</li>
		 * <li>2表示setExperimentalOption（个性化配置）不需要存储至map的配置</li>
		 * <li>3表示使用addArguments（启动配置项）配置，但需要拼接参数</li>
		 * </ol>
		 * 
		 * @return 浏览器配置的操作类型
		 */
		public short getOptionType() {
			return optionType;
		}

		/**
		 * 用于返回存储在枚举中需要对浏览器设置的值
		 * 
		 * @return 需要对浏览器设置的值
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * 用于对枚举对应的操作设置的值。注意，设置浏览器参数值的时候要传入正确的参数，否则设置无效
		 * 
		 * @param value 对浏览器设置的值
		 */
		void setValue(Object value) {
			this.value = value;
		}
	}
}
