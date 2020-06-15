package pres.auxiliary.work.selenium.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.openqa.selenium.WebDriverException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.io.FileUtil;

/**
 * <p>
 * <b>文件名：</b>RecordTool.java
 * </p>
 * <p>
 * <b>用途：</b>用于在自动化测试中对每个事件（操作）进行自动记录的工具，类中提供默认的工具类对象，
 * 只需要传入日志存放文件夹，即可进行对步骤的说明进行自动记录。可通过类中提供的开关，以判断是否开始自动记录。
 * </p>
 * <p>
 * 需要注意的是，当我们需要对某个记录工具类进行详细设置的时候，可以使用类中的get方法，将记录工具类返回后后，对该类进行单独设置。
 * 例如，当使用截图工具Screenshot类时，需要对该类设置其WebDriver对象，否则报错，此时，可按照以下方法进行设置：<br>
 * RecordStep.getScreenshot().setDriver(driver);
 * </p>
 * <p>
 * <b>编码时间：</b>2019年9月7日下午3:30:39
 * </p>
 * <p>
 * <b>修改时间：</b>2019年9月7日下午3:30:39
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class RecordTool {
	//TODO 路径相关
	/**
	 * 在未设置文件保存路径的情况下，默认提供一个保存路径
	 */
	private static final String DEFAULT_FILE_PATH = "Result/RunRecord/";
	/**
	 * 指向Xmind相关的文件的模板位置
	 */
	private static final String XMIND_TEMJPLET_FILE_PATH = "Templet/XMindTemplate/";

	//TODO 记录类对象相关
	/**
	 * Log类对象，默认构造
	 */
	private static Log log = new Log(DEFAULT_FILE_PATH);
	/**
	 * Record类对象，默认构造
	 */
	private static ExcelRecord record = new ExcelRecord(DEFAULT_FILE_PATH);
	/**
	 * Screenshot类对象，默认构造
	 */
	private static Screenshot screenshot = new Screenshot(DEFAULT_FILE_PATH);
	
	/**
	 * 用于存储log原始的存放文件夹路径
	 */
	private static String logSavePath = "";
	/**
	 * 用于存储record原始的存放文件夹路径
	 */
	private static String recordSavePath = "";
	/**
	 * 用于存储screenshot原始的存放文件夹路径
	 */
	private static String screenshotSavePath = "";

	//TODO 容器相关
	/**
	 * 用于存储当前需要使用的记录工具类，初始化为将所有的记录工具都开启
	 */
	private static ArrayList<RecordType> recordType = new ArrayList<>(Arrays.asList(RecordType.values()));

	/**
	 * 用于存储模块信息，包含模块名称、模块步骤数量和模块bug数量，由模块名称作为key
	 */
	private static HashMap<String, ModuleInformation> module = new HashMap<String, ModuleInformation>(16);

	//TODO 记录相关
	/**
	 * 用于存储当前测试模块名称
	 */
	private static String moduleName = "";

	/**
	 * 用于存储当前测试类名称
	 */
	private static String testClassName = "";

	/**
	 * 用于存储当前测试方法名称
	 */
	private static String methodName = "";

	/**
	 * 用于控制是否启用自动记录
	 */
	private static boolean isStepRecord = true;

	/**
	 * 用于标识是否已经开始进行记录
	 */
	private static boolean startRecord = false;

	/**
	 * 返回{@link Log}类对象
	 * 
	 * @return {@link Log}类对象
	 */
	public static Log getLog() {
		return log;
	}

	/**
	 * 返回{@link ExcelRecord}类对象
	 * 
	 * @return {@link ExcelRecord}类对象
	 */
	public static ExcelRecord getRecord() {
		return record;
	}

	/**
	 * 用于返回{@link Screenshot}类对象
	 * 
	 * @return {@link Screenshot}类对象
	 */
	public static Screenshot getScreenshot() {
		return screenshot;
	}

	/**
	 * 用于设置需要使用的工具类，通过传入枚举类{@link RecordType}的值类决定使用哪些记录工具
	 * 
	 * @param recordTypes {@link RecordType}指定的枚举值，可传入多个
	 */
	public static void setRecordType(RecordType... recordTypes) {
		// 清空recordType中的记录，重新进行存储，避免上一次的设置导致无需使用的工具类在进行记录
		recordType.clear();
		// 将相应使用的记录工具进行存储至recordType中，并对存在对象的类进行构造
		for (RecordType type : recordTypes) {
			recordType.add(type);
		}
	}

	/**
	 * 设置模块信息，若bug数和步骤数中的一个未存储该模块信息时，则直接将两个数量初始化。
	 * 根据传入的createFolder参数，控制是否创建文件，注意，该方法会只获取一次当前的文件夹路径，
	 * 请第一次将createFolder设置为true时一定要保证所使用的记录工具的文件保存路径处于
	 * 根路径状态，否则文件夹创建的位置可能无法达到期望的效果
	 * 
	 * @param moduleName 模块名称
	 * @param createFolder 是否创建新的文件夹
	 * @throws RecordStateException 当已调用{@link #startRecord(String, String)}方法后再调用此方法是抛出
	 */
	public static void setModule(String moduleName, boolean createFolder) {
		// 当运行状态为true时，说明已经开始进行记录，则不能切换模块，否则抛出异常
		if (startRecord) {
			throw new RecordStateException("当前已开始进行运行记录，无法切换模块");
		}
		
		// 判断bugNumber和stepNumber是否都有存储该模块，若其中一个未存储，则将两个map初始化
		if (!module.containsKey(moduleName)) {
			module.put(moduleName, new ModuleInformation(moduleName));
			//判断是否需要冲洗创建新的文件夹
			if (createFolder) {
				if (logSavePath.isEmpty()) {
					logSavePath = log.getSavePath();
				}
				
				if (recordSavePath.isEmpty()) {
					recordSavePath = record.getSavePath();
				}
				
				if (screenshotSavePath.isEmpty()) {
					screenshotSavePath = screenshot.getSavePath();
				}
				
				log.setSavePath(logSavePath + moduleName + "/");
				record.setSavePath(logSavePath + moduleName + "/");
				screenshot.setSavePath(logSavePath + moduleName + "/RunScreenshot/");
			}
		}
		
		// 设置当前的模块
		RecordTool.moduleName = moduleName;
	}
	
	/**
	 * 用于返回存储在类中的所有模块的名称
	 * @return 所有模块的名称
	 */
	public static Set<String> getModuleName() {
		return module.keySet();
	}

	/**
	 * 返回当前模块的文本信息
	 * 
	 * @return 模块的文本信息
	 */
	public static String getModuleInformation() {
		return module.get(moduleName).getStringReport(0);
	}

	/**
	 * 返回指定模块的文本信息
	 * 
	 * @param module 模块名称
	 * @return 模块的文本信息
	 */
	public static String getModuleInformation(String moduleName) {
		return module.containsKey(moduleName) ? module.get(moduleName).getStringReport(0) : "";
	}

	/**
	 * 以Json格式返回当前模块的信息，详细字段见{@link ModuleInformation#getJsonInformation()}
	 * 
	 * @return 模块的Json格式信息
	 */
	public static String getModuleJsonInformation() {
		return module.get(moduleName).getJsonInformation().toString();
	}

	/**
	 * 以Json格式返回指定模块的信息
	 * 
	 * @param module 模块名称
	 * @return 模块的Json格式信息
	 */
	public static String getModuleJsonInformation(String moduleName) {
		return module.containsKey(moduleName) ? module.get(moduleName).getJsonInformation().toString() : "";
	}
	
	public static String getAllModuleInformation() {
		String text = "";
		//读取module中所有的信息
		for (String key : module.keySet()) {
			text += (getModuleInformation(key) + "\n");
		}
		
		return text;
	}
	
	/**
	 * 用于指定是否进行自动记录操作步骤
	 * 
	 * @param isRecord 指定是否启用自动记录操作步骤
	 */
	public static void setRecordStep(boolean isRecord) {
		RecordTool.isStepRecord = isRecord;
	}
	
	/**
	 * 用于返回是否启动自动记录操作步骤的状态
	 * 
	 * @return 是否开启自动记录操作步骤
	 */
	public static boolean isRecordStep() {
		return RecordTool.isStepRecord;
	}

	/**
	 * <p>
	 * 用于记录自动化测试中进行的操作步骤。根据{@link #setRecordType(RecordType...)}方法指定使用的工具进行记录，若未指定工具，
	 * 则默认使用所有的工具。在事件类中已写入该方法对步骤进行自动记录，亦可通过{@link #isRecord(boolean)}方法控制是否需要进行自动记录。
	 * </p>
	 * <p>
	 * <b>注意：当未调用{@link #startRecord(String, String)}方法时，使用该方法不会抛出异常，以避免自动化测试不正常的结束</b>
	 * </p>
	 * 
	 * @param stepText 步骤内容
	 */
	public static void recordStep(String stepText) {
		// 当运行状态为false时，说明未开始进行记录，则不能进行记录，但不抛出异常，以避免自动化的结束
		if (!startRecord) {
			return;
		}
				
		// 判断是否需要进行步骤记录，若isRecord为false，则直接结束
		if (!isStepRecord) {
			return;
		}

		// 判断模块内信息是否为，若为空，则加上一个“未命名模块”
		if (module.isEmpty()) {
			setModule("未命名模块", true);
		}

		// 记录前，步骤数+1
		module.get(moduleName).getTestClass().getMethod().addStep();
		// 循环，根据recordType中指定使用的记录工具进行记录
		recordType.forEach(e -> writeStep(e, stepText));
	}

	/**
	 * <p>
	 * 用于记录自动化运行的结果，根据传入的判断结果（形参judgeResult），当其为true时，
	 * 则记录形参successText中的内容，反之，则记录形参errorText中的内容，并将其列为Bug，其模块中的Bug数加1。
	 * </p>
	 * 
	 * <p>
	 * 例如，我们在页面上当存在“取消”按钮时，则表示测试不通过，则可用以下代码表示：
	 * RecordTool.recordResult(!event.getJudgeEvent().judgeControl("取消").getBooleanValue(),
	 * "成功", "失败");
	 * </p>
	 * 
	 * <p>
	 * 记录工具根据{@link #setRecordType(RecordType...)}方法指定使用的工具进行记录，若未指定工具，则默认使用所有的工具。
	 * 在事件类中已写入该方法对步骤进行自动记录，亦可通过{@link #isRecord(boolean)}方法控制是否需要进行自动记录。
	 * 使用该方法时即使未使用截图工具，也会自动进行截图，并将截图附在记录的内容中。 当截图类配置有误时，则不进行截图，并将配置错误的信息，
	 * 记录在其他记录工具中。
	 * </p>
	 * 
	 * @param judgeResult 判断结果
	 * @param successText judgeResult为true时进行记录的文本
	 * @param errorText   judgeResult为false时进行记录的文本
	 * @throws RecordToolConfigException 记录工具配置错误时抛出的异常
	 * @throws RecordStateException 当未调用{@link #startRecord(String, String)}方法是抛出的异常
	 */
	public static void recordResult(boolean judgeResult, String successText, String errorText) {
		// 当运行状态为false时，说明未开始进行记录，则不能进行记录，故抛出异常
		if (!startRecord) {
			throw new RecordStateException("当前未开始进行运行记录，无法进行运行记录");
		}
				
		// 判断模块内信息是否为，若为空，则加上一个“未命名模块”
		if (module.isEmpty()) {
			setModule("未命名模块", true);
		}

		// 记录截图文件原保存位置，以方便在当前路径下加上Result文件夹
		String screenshotPath = screenshot.getSavePath();
		// 将当前截图放到原路径下的Result文件夹下
		screenshot.setSavePath(screenshotPath + "Result/");
		// 根据传入的结果，判断使用哪一个记录语
		String resultText = judgeResult ? successText : errorText;

		// 根据传入的判断结果，为true时，则使用successText记录，并标记为非Bug，反之，则标记为Bug
		File screenshotFile = null;
		try {
			screenshotFile = screenshot.creatImage(resultText);
		} catch (WebDriverException | IOException e1) {
		}

		// 由于lambda中不能写screenshotFile，故只能使用原始的forEach
		for (RecordType type : recordType) {
			writeResult(screenshotFile, type, !judgeResult, resultText);
		}

		// 将截图存放位置返回到之前的存储路径下
		screenshot.setSavePath(screenshotPath);
		
		// 若结果为BUG则对当前测试方法对象中添加BUG数量
		if (!judgeResult) {
			module.get(moduleName).getTestClass().getMethod().addBug();
		}
	}

	/**
	 * 用于记录自动化运行的结果，当参数isBug为true时，将其列为Bug，模块中的Bug数加1，反之则不进行增加。
	 * <p>
	 * 记录工具根据{@link #setRecordType(RecordType...)}方法指定使用的工具进行记录，若未指定工具，则默认使用所有的工具。
	 * 在事件类中已写入该方法对步骤进行自动记录，亦可通过{@link #isRecord(boolean)}方法控制是否需要进行自动记录。
	 * 使用该方法时即使未使用截图工具，也会自动进行截图，并将截图附在记录的内容中。 当截图类配置有误时，则不进行截图，并将配置错误的信息，
	 * 记录在其他记录工具中。
	 * </p>
	 * 
	 * @param isBug 是否为Bug
	 * @param text  结果文本
	 * @throws RecordToolConfigException 记录工具配置错误时抛出的异常
	 * @throws RecordStateException 当未调用{@link #startRecord(String, String)}方法是抛出的异常
	 */
	public static void recordResult(boolean isBug, String text) {
		// 当运行状态为false时，说明未开始进行记录，则不能进行记录，故抛出异常
		if (!startRecord) {
			throw new RecordStateException("当前未开始进行运行记录，无法进行运行记录");
		}
		
		// 判断模块内信息是否为，若为空，则加上一个“未命名模块”
		if (module.isEmpty()) {
			setModule("未命名模块", true);
		}

		// 记录截图文件原保存位置，以方便在当前路径下加上Result文件夹
		String screenshotPath = screenshot.getSavePath();
		// 将当前截图放到原路径下的Result文件夹下
		screenshot.setSavePath(screenshotPath + "Result/");

		// 根据传入的判断结果，为true时，则使用successText记录，并标记为非Bug，反之，则标记为Bug
		File screenshotFile = null;
		try {
			screenshotFile = screenshot.creatImage(text);
		} catch (WebDriverException | IOException e1) {
		}

		// 由于lambda中不能写screenshotFile，故只能使用原始的forEach
		for (RecordType type : recordType) {
			writeResult(screenshotFile, type, isBug, text);
		}

		// 将截图存放位置返回到之前的存储路径下
		screenshot.setSavePath(screenshotPath);
		
		// 若结果为BUG则对当前测试方法对象中添加BUG数量
		if (isBug) {
			module.get(moduleName).getTestClass().getMethod().addBug();
		}
	}

	/**
	 * <p>
	 * 用于记录备注消息，记录工具根据{@link #setRecordType(RecordType...)}方法指定使用的工具进行记录，
	 * 若未指定工具，则默认使用所有的工具。在事件类中已写入该方法对步骤进行自动记录，亦可通过{@link #isRecord(boolean)}方法控制是否需要进行自动记录。
	 * </p>
	 * 
	 * @param markText 备注文本
	 * @throws RecordToolConfigException 记录工具配置错误时抛出的异常
	 * @throws RecordStateException 当未调用{@link #startRecord(String, String)}方法是抛出的异常
	 */
	public static void recordMark(String markText) {
		// 当运行状态为false时，说明未开始进行记录，则不能进行记录，故抛出异常
		if (!startRecord) {
			return;
		}
				
		// 判断模块内信息是否为，若为空，则加上一个“未命名模块”
		if (module.isEmpty()) {
			setModule("未命名模块", true);
		}

		// 循环，根据recordType中指定使用的记录工具进行记录
		recordType.forEach(type -> writeMark(type, markText));
	}

	/**
	 * <p>
	 * 用于记录异常消息，记录工具根据{@link #setRecordType(RecordType...)}方法指定使用的工具进行记录，
	 * 若未指定工具，则默认使用所有的工具。在事件类中已写入该方法对步骤进行自动记录，亦可通过{@link #isRecord(boolean)}方法控制是否需要进行自动记录。
	 * </p>
	 * @param exception 异常类
	 * @throws RecordToolConfigException 记录工具配置错误时抛出的异常
	 */
	public static void recordException(Exception exception) {
		// 当运行状态为false时，说明未开始进行记录，则不能进行记录，故抛出异常
		if (!startRecord) {
			return;
		}
				
		// 判断模块内信息是否为，若为空，则加上一个“未命名模块”
		if (module.isEmpty()) {
			setModule("未命名模块", true);
		}

		// 记录截图文件原保存位置，以方便在当前路径下加上Result文件夹
		String screenshotPath = screenshot.getSavePath();
		// 将当前截图放到原路径下的Result文件夹下
		screenshot.setSavePath(screenshotPath + "Exception/");

		// 根据传入的判断结果，为true时，则使用successText记录，并标记为非Bug，反之，则标记为Bug
		File screenshotFile = null;
		try {
			// 将异常类名作为截图文件名
			screenshotFile = screenshot.creatImage(exception.getClass().getName());
		} catch (WebDriverException | IOException e1) {
		}

		// 由于lambda中不能写screenshotFile，故只能使用原始的forEach
		for (RecordType type : recordType) {
			writeException(type, screenshotFile, exception);
		}

		// 将截图存放位置返回到之前的存储路径下
		screenshot.setSavePath(screenshotPath);
	}

	/**
	 * 用于控制开始使用记录工具进行运行记录，只有在使用该方法后以下方法才能生效：
	 * {@link #recordException(Exception)}、{@link #recordMark(String)}、
	 * {@link #recordResult(boolean, String)}、{@link #recordResult(boolean, String, String)}
	 * 
	 * @param className  测试类的名称
	 * @param methodName 测试方法的名称
	 * @throws RecordToolConfigException 记录工具配置错误时抛出的异常
	 * @throws RecordStateException 未调用结束记录的方法时再次调用了此方法后抛出的异常
	 */
	public static void startRecord(String className, String methodName) {
		// 当运行状态为true时，说明此次调用前未调用endWrite()方法，则抛出异常
		if (startRecord) {
			throw new RecordStateException("当前运行状态为" + startRecord + "，需要调用结束记录的方法再调用该方法");
		}
		
		// 判断模块内信息是否为，若为空，则加上一个“未命名模块”
		if (module.isEmpty()) {
			setModule("未命名模块", true);
		}

		// 记录类名与方法名
		testClassName = className;
		RecordTool.methodName = methodName;
		
		//向内部类中添加信息
		module.get(moduleName).addTestClass(className);
		module.get(moduleName).getTestClass().addMethod(methodName);

		// 设置运行记录状态为true
		startRecord = true;

		// 由于lambda中不能写screenshotFile，故只能使用原始的forEach
		for (RecordType type : recordType) {
			writeStartRecord(type, className, methodName);
		}
	}

	/**
	 * 用于标识结束进行自动化测试运行记录，运行该方法后，所有的记录将停止
	 * 
	 * @throws RecordToolConfigException 记录工具配置错误时抛出的异常
	 * @throws RecordStateException 未调用开始记录的方法时调用了此方法后抛出的异常
	 */
	public static void endRecord() {
		if (!startRecord) {
			throw new RecordStateException("当前运行状态为" + startRecord + "，需要调用开始记录的方法再调用该方法");
		}
		
		// 判断模块内信息是否为，若为空，则加上一个“未命名模块”
		if (module.isEmpty()) {
			setModule("未命名模块", true);
		}

		// 设置运行记录状态为false
		startRecord = false;

		// 由于lambda中不能写screenshotFile，故只能使用原始的forEach
		for (RecordType type : recordType) {
			writeEndRecord(type);
		}
	}
	
	/**
	 * 用于根据模块名称生成指向模块的xmind版本的测试报告
	 * @param projectedName 项目名称
	 * @throws IOException 当文件不存在或错误时抛出的异常
	 */
	public static void createXmindReport(String projectedName) throws IOException {
		//读取模板文件，将文件的内容写入到jsonScript中
		BufferedReader bf = new BufferedReader(new FileReader(new File("Templet\\XMindTemplate\\content.json")));
		String jsonScript = "";
		String line = "";
		while ((line = bf.readLine()) != null) {
			jsonScript += line;
		}
		bf.close();
		
		//将模板中的json转换为JSONObject类，并添加标题与id
		JSONObject json = JSON.parseObject(jsonScript);
		json.getJSONObject("rootTopic").put("title", projectedName);
		json.getJSONObject("rootTopic").put("id", UUID.randomUUID());
		
		//循环，读取所有的模块信息
		int moduleIndex = 0;
		for (String moduleName : module.keySet()) {
			//读取模块信息的json
			JSONObject moduleJson = JSON.parseObject(RecordTool.getModuleJsonInformation(moduleName));
			//编写模块信息
			addChildren(json.getJSONObject("rootTopic"), moduleName);
			
			//读取模块下testclass中所有的内容
			JSONArray testClassList = moduleJson.getJSONArray("testClass");
			//循环，读取所有的测试类信息
			for (int testClassIndex = 0; testClassIndex < testClassList.size(); testClassIndex++) {
				//读取每一个测试类json
				JSONObject testClassJson = JSON.parseObject(testClassList.get(testClassIndex).toString());
				//编写测试类信息
				addChildren(json.getJSONObject("rootTopic").
						getJSONObject("children").getJSONArray("attached").
						getJSONObject(moduleIndex), testClassJson.getString("name"));
				
				//读取模块下method中所有的内容
				JSONArray methodList = testClassJson.getJSONArray("method");
				//循环，读取测试类下所有的测试方法
				for (int methodIndex = 0; methodIndex < methodList.size(); methodIndex++) {
					//读取每一个测试方法的json
					JSONObject methodJson = JSON.parseObject(methodList.get(methodIndex).toString());
					//写入测试方法信息
					String methodInformation = "方法名：";
					methodInformation += (methodJson.getString("name") + "\n");
					methodInformation += ("步骤数量：" + methodJson.getString("step") + "\n");
					methodInformation += ("Bug数量：" + methodJson.getString("bug") + "\n");
					//编写测试方法信息
					addChildren(json.getJSONObject("rootTopic").
							getJSONObject("children").getJSONArray("attached").
							getJSONObject(moduleIndex).
							getJSONObject("children").getJSONArray("attached").
							getJSONObject(testClassIndex), methodInformation);
				}
				
				//添加出现BUG的方法文本，若无出现BUG的方法，则不添加该分支
				if (!testClassJson.getJSONArray("bugMethod").isEmpty()) {
					addChildren(json.getJSONObject("rootTopic").
							getJSONObject("children").getJSONArray("attached").
							getJSONObject(moduleIndex).
							getJSONObject("children").getJSONArray("attached").
							getJSONObject(testClassIndex), "出现bug的方法有：" + testClassJson.getString("bugMethod"));
				}	
			}
			moduleIndex++;
		}
		
		//在json代码前后加上中括号，以适应xmind的读取方式
		jsonScript = "[";
		jsonScript += json.toString();
		jsonScript += "]";
		
		//添加文件夹
		new File(DEFAULT_FILE_PATH + projectedName + "自动化测试报告/").mkdirs();
		//存储测试报告相关的json文件生成至相应的位置
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(DEFAULT_FILE_PATH + projectedName + "自动化测试报告/" + "content.json")));
		bw.write(jsonScript);
		bw.close();
		
		//将模板中的文件复制到默认的位置，由于使用FileUtil指向的相对路径并非当前的工程路径，故需要先将绝对路径转为相对路径后再添加
		//由于生成的绝对路径数据末尾不带“/”，故需要在文件名前补上
		FileUtil.copy(new File(XMIND_TEMJPLET_FILE_PATH).getAbsoluteFile() + "/content.xml", new File(DEFAULT_FILE_PATH + projectedName + "自动化测试报告/").getAbsolutePath(), true);
		FileUtil.copy(new File(XMIND_TEMJPLET_FILE_PATH).getAbsoluteFile() + "/manifest.json", new File(DEFAULT_FILE_PATH + projectedName + "自动化测试报告/").getAbsolutePath(), true);
		FileUtil.copy(new File(XMIND_TEMJPLET_FILE_PATH).getAbsoluteFile() + "/metadata.json", new File(DEFAULT_FILE_PATH + projectedName + "自动化测试报告/").getAbsolutePath(), true);
		//压缩文件，将文件打包成xmind格式的文件
		compressReportFile(new File(DEFAULT_FILE_PATH + projectedName + "自动化测试报告/"));
		
		//删除复制到目录中的文件，只保留生成的xmind文件
		Arrays.asList(new File(DEFAULT_FILE_PATH + projectedName + "自动化测试报告/").listFiles()).stream().
			filter(file -> file.getName().indexOf(".xmind") == -1).forEach(File :: delete);
		
	}
	
	/**
	 * 压缩文件，并打包成xmind的格式
	 * @param reportFolder xmind文件存放文件夹
	 * @throws IOException 文件错误时抛出的异常
	 */
	private static void compressReportFile(File reportFolder) throws IOException {
		// 获取测试报告文件夹中所有的文件
		File[] fileList = reportFolder.listFiles();
		// 创建压缩文件流对象
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(new File(reportFolder, reportFolder.getName() + ".xmind"))));
		// 定义缓存变量
		byte[] bufs = new byte[1024 * 10];
		// 循环，将文件夹中的文件全部写入到压缩文件中
		for (int i = 0; i < fileList.length; i++) {
			// 创建ZIP实体，并添加进压缩包
			zos.putNextEntry(new ZipEntry(fileList[i].getName()));
			FileInputStream fis = new FileInputStream(fileList[i]);
			BufferedInputStream bis = new BufferedInputStream(fis, 1024 * 10);
			int read = 0;
			while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
				zos.write(bufs, 0, read);
			}

			bis.close();
		}
		zos.close();
	}
	
	/**
	 * 用于生成xmind测试报告中添加下级标题的方法
	 * @param parentJson json父节点
	 * @param title 写入到文件中的内容
	 */
	private static void addChildren(JSONObject parentJson, String title) {
		JSONObject childrenJson = new JSONObject();
		
		childrenJson.put("id", UUID.randomUUID());
		childrenJson.put("title", title);
		
		if (!parentJson.containsKey("children")) {
			parentJson.put("children", new JSONObject());
		}
		
		if (!parentJson.getJSONObject("children").containsKey("attached")) {
			parentJson.getJSONObject("children").put("attached", new ArrayList<>());
		}
		
		parentJson.getJSONObject("children").getJSONArray("attached").add(childrenJson);
	}

	/**
	 * 用于对测试方法运行结束时的记录
	 * 
	 * @param type 记录工具类型
	 */
	private static void writeEndRecord(RecordType type) {
		// 根据RecordType选择记录工具，若截图文件对象为null，则不记录截图文件
		switch (type) {
		case LOG:
			try {
				log.endWrite(module.get(moduleName).getTestClass().getMethod().getBug());
			} catch (IOException e) {
				throw new RecordToolConfigException("Log类配置错误，无法写入结果");
			}
			break;
		case RECORD:
			try {
				record.endRecord();
			} catch (Exception e) {
				throw new RecordToolConfigException("Record类配置错误，无法写入结果");
			}
			break;
		case SCREENSHOT:
			// 此处无需截图
			break;
		case SYSTEM:
			System.out.println(testClassName + "类的" + methodName + "方法运行结束，共发现BUG"
					+ module.get(moduleName).getTestClass().getMethod().getBug() + "个");
			break;
		default:
			throw new IllegalArgumentException("未定义的枚举值: " + type);
		}
	}

	/**
	 * 用于对测试方法开始运行时进行记录
	 * 
	 * @param type       记录工具类型
	 * @param className  运行类名
	 * @param methodName 运行方法名
	 */
	private static void writeStartRecord(RecordType type, String className, String methodName) {
		// 根据RecordType选择记录工具，若截图文件对象为null，则不记录截图文件
		switch (type) {
		case LOG:
			try {
				log.startWrite(className, methodName);
			} catch (IOException e) {
				throw new RecordToolConfigException("Log类配置错误，无法写入结果");
			}
			break;
		case RECORD:
			try {
				record.startRecord(className, methodName);
			} catch (Exception e) {
				throw new RecordToolConfigException("Record类配置错误，无法写入结果");
			}
			break;
		case SCREENSHOT:
			// 此处无需截图
			break;
		case SYSTEM:
			System.out.println("正在运行" + className + "类的" + methodName + "()方法");
			break;
		default:
			throw new IllegalArgumentException("未定义的枚举值: " + type);
		}
	}

	/**
	 * 用于对异常进行记录
	 * 
	 * @param type           记录工具类型
	 * @param screenshotFile 截图文件类对象
	 * @param exception      异常类对象
	 */
	private static void writeException(RecordType type, File screenshotFile, Exception exception) {
		// 根据RecordType选择记录工具，若截图文件对象为null，则不记录截图文件
		switch (type) {
		case LOG:
			try {
				log.write("出现异常，其异常信息如下：\n" + "异常名称：" + exception.getClass().getName() + "\n异常信息："
						+ exception.getMessage() + "\n"
						+ (screenshotFile != null ? ("详见截图：" + screenshotFile.getAbsolutePath())
								: "Screenshot类配置有误，无法进行截图"));
			} catch (IOException e) {
				throw new RecordToolConfigException("Log类配置错误，无法写入结果");
			}
		case RECORD:
			try {
				record.setException(exception);
				if (screenshotFile != null) {
					record.errorScreenshot(screenshotFile);
				} else {
					record.mark("Screenshot类配置有误，无法进行截图");
				}
			} catch (Exception e) {
				throw new RecordToolConfigException("Record类配置错误，无法写入结果");
			}
			break;
		case SCREENSHOT:
			// 由于在recordResult()方法中采用直接截图的方式，故此处可不做操作
			break;
		case SYSTEM:
			System.out.println(
					"出现异常，其异常信息如下：\n" + "异常名称：" + exception.getClass().getName() + "\n异常信息：" + exception.getMessage()
							+ "\n" + (screenshotFile != null ? ("详见截图：" + screenshotFile.getAbsolutePath())
									: "Screenshot类配置有误，无法进行截图"));
			break;
		default:
			throw new IllegalArgumentException("未定义的枚举值: " + type);
		}
	}

	/**
	 * 用于选择工具将备注文本写入到相应的工具中
	 * 
	 * @param type     记录工具类型
	 * @param markText 需要记录的备注消息
	 */
	private static void writeMark(RecordType type, String markText) {
		// 根据RecordType选择记录工具
		switch (type) {
		case LOG:
			try {
				log.write("备注：" + markText);
			} catch (IOException e) {
				throw new RecordToolConfigException("Log类配置错误，无法写入步骤");
			}
			break;
		case RECORD:
			try {
				record.mark(markText);
			} catch (Exception e) {
				throw new RecordToolConfigException("Record类配置错误，无法写入步骤");
			}
			break;
		case SCREENSHOT:
			try {
				String path = screenshot.getSavePath();
				screenshot.setSavePath(path + "mark/");
				screenshot.creatImage(markText);
				screenshot.setSavePath(path);
			} catch (WebDriverException | IOException e) {
				throw new RecordToolConfigException("Scerrnshot类配置错误，无法进行截图");
			}
			break;
		case SYSTEM:
			System.out.println("备注：" + markText);
			break;
		default:
			throw new IllegalArgumentException("未定义的值: " + type);
		}
	}

	/**
	 * 用于根据使用的记录工具类型，对测试结果进行记录
	 * 
	 * @param screenshotFile 截图文件类对象
	 * @param type           记录工具类型
	 * @param isBug          是否为Bug
	 * @param resultText     需要记录的内容
	 */
	private static void writeResult(File screenshotFile, RecordType type, boolean isBug, String resultText) {
		// 根据RecordType选择记录工具，若截图文件对象为null，则不记录截图文件
		switch (type) {
		case LOG:
			try {
				log.write("结果：" + resultText + "\n"
						+ (screenshotFile != null ? ("详见截图：" + screenshotFile.getAbsolutePath())
								: "Screenshot类配置有误，无法进行截图"));
			} catch (IOException e) {
				throw new RecordToolConfigException("Log类配置错误，无法写入结果");
			}
			break;
		case RECORD:
			try {
				record.result(resultText, isBug);
				if (screenshotFile != null) {
					record.runScreenshot(screenshotFile);
				} else {
					record.mark("Screenshot类配置有误，无法进行截图");
				}
			} catch (Exception e) {
				throw new RecordToolConfigException("Record类配置错误，无法写入结果");
			}
			break;
		case SCREENSHOT:
			// 由于在recordResult()方法中采用直接截图的方式，故此处可不做操作
			break;
		case SYSTEM:
			System.out.println(
					"结果：" + resultText + "\n" + (screenshotFile != null ? ("详见截图：" + screenshotFile.getAbsolutePath())
							: "Screenshot类配置有误，无法进行截图"));
			break;
		default:
			throw new IllegalArgumentException("未定义的枚举值: " + type);
		}
	}

	/**
	 * 用于根据指定的记录工具类型，对步骤进行记录
	 * 
	 * @param type     记录工具的
	 * @param stepText 当前的步骤的内容
	 */
	private static void writeStep(RecordType type, String stepText) {
		// 根据RecordType选择记录工具
		switch (type) {
		case LOG:
			try {
				log.write("步骤：" + module.get(moduleName).getTestClass().getMethod().getStep() + "." + stepText);
			} catch (IOException e) {
				throw new RecordToolConfigException("Log类配置错误，无法写入步骤");
			}
			break;
		case RECORD:
			try {
				record.step(stepText);
			} catch (Exception e) {
				throw new RecordToolConfigException("Record类配置错误，无法写入步骤");
			}
			break;
		case SCREENSHOT:
			try {
				String path = screenshot.getSavePath();
				screenshot.setSavePath(path + "step/");
				screenshot.creatImage(module.get(moduleName).getTestClass().getMethod().getStep() + "");
				screenshot.setSavePath(path);
			} catch (WebDriverException | IOException e) {
				throw new RecordToolConfigException("Scerrnshot类配置错误，无法进行截图");
			}
			break;
		case SYSTEM:
			System.out.println("步骤：" + module.get(moduleName).getTestClass().getMethod().getStep() + "." + stepText);
			break;
		default:
			throw new IllegalArgumentException("未定义的值: " + type);
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>RecordTool.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于存储、输出模块信息
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2019年9月21日下午4:28:30
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2019年10月6日下午5:19:30
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.1
	 * @since JDK 12
	 *
	 */
	private static class ModuleInformation implements AutoTestReport {
		/**
		 * 用于存储模块包含的用例类信息
		 */
		private HashMap<String, TestClassInformation> moduleInformation = new HashMap<String, TestClassInformation>(
				16);

		/**
		 * 用于记录模块的名称
		 */
		private String moduleName = "";

		/**
		 * 初始化模块信息
		 */
		public ModuleInformation(String moduleName) {
			this.moduleName = moduleName;
		}

		/**
		 * 用于记录并初始化测试用例类的信息，若信息已存在，则不进行存储
		 * 
		 * @param testClassName 测试用例类名称
		 */
		public void addTestClass(String testClassName) {
			// 判断当前测试类名称是否存在，存在则不进行初始化
			if (!moduleInformation.containsKey(testClassName)) {
				moduleInformation.put(testClassName, new TestClassInformation(testClassName));
			}
		}
		
		/**
		 * 用于返回当前指向的测试类对象
		 * 
		 * @return 当前指向的测试类对象
		 */
		public TestClassInformation getTestClass() {
			return moduleInformation.get(testClassName);
		}

		/**
		 * 以json格式，输出测试模块信息，json包含字段及类型如下：<br>
		 * name：模块名称，为String类型<br>
		 * testClass：模块中包含的所有测试类的信息，为List类型（该字段调用{@link TestClassInformation#getJsonInformation()}）<br>
		 * 
		 * @return 测试类信息，json格式
		 */
		@Override
		public JSONObject getJsonInformation() {
			JSONObject json = new JSONObject();
			json.put("name", moduleName);
			// 添加模块下所有的方法信息
			ArrayList<JSONObject> testClassList = new ArrayList<>();
			moduleInformation.forEach((key, value) -> {
				testClassList.add(value.getJsonInformation());
			});
			json.put("testClass", testClassList);

			return json;
		}

		@Override
		public String getStringReport(int tabCount) {
			// 记录测试模块名称
			String text = "\t".repeat(tabCount) + "测试模块名:" + moduleName + "\n";

			// 记录每个测试方法中的信息
			text += "\t".repeat(tabCount) + "以下是测试模块中所有的测试类的信息：\n";
			for (String key : moduleInformation.keySet()) {
				text += moduleInformation.get(key).getStringReport(tabCount + 1);
			}
			text += "\n";

			return text;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>RecordTool.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于存储、输出测试类的信息
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2019年10月6日下午5:10:51
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2019年10月6日下午5:10:51
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	private static class TestClassInformation implements AutoTestReport {
		/**
		 * 用于存储用例类包含的用例信息
		 */
		private HashMap<String, MethodInformation> testClassInforemation = new HashMap<String, MethodInformation>(
				16);

		/**
		 * 用于记录用例类的名称
		 */
		private String testClassName = "";

		/**
		 * 用于初始化测试类的名称
		 * 
		 * @param testClassName 测试类的名称
		 */
		public TestClassInformation(String testClassName) {
			this.testClassName = testClassName;
		}
		
		/**
		 * 用于记录并初始化测试用例类的信息，若信息已存在，则不进行存储
		 * 
		 * @param testClassName 测试用例类名称
		 */
		public void addMethod(String methodName) {
			// 判断当前测试类名称是否存在，存在则不进行初始化
			if (!testClassInforemation.containsKey(methodName)) {
				testClassInforemation.put(methodName, new MethodInformation(methodName));
			}
		}

		/**
		 * 用于返回当前指向的测试方法对象
		 * 
		 * @return 当前指向的测试方法对象
		 */
		public MethodInformation getMethod() {
			return testClassInforemation.get(methodName);
		}

		/**
		 * 以json格式，输出测试类信息，json包含字段及类型如下：<br>
		 * name：测试类名称，为String类型<br>
		 * method：测试类中包含的所有方法的信息，为List类型（该字段调用{@link MethodInformation#getJsonInformation()}）<br>
		 * bugMethod：测试类中出现BUG的方法信息
		 * 
		 * @return 测试类信息，json格式
		 */
		@Override
		public JSONObject getJsonInformation() {
			JSONObject json = new JSONObject();
			json.put("name", testClassName);
			// 添加测试类下所有的方法信息
			ArrayList<JSONObject> methodList = new ArrayList<>();
			testClassInforemation.forEach((key, value) -> {
				methodList.add(value.getJsonInformation());
			});
			json.put("method", methodList);

			// 添加出现Bug的测试方法的方法名
			ArrayList<String> bugMethodList = new ArrayList<>();
			testClassInforemation.forEach((key, value) -> {
				// 若该方法为出现BUG的测试方法，则进行存储其方法名（key则为方法名）
				if (value.isBugMethod()) {
					bugMethodList.add(key);
				}
			});
			json.put("bugMethod", bugMethodList);

			return json;
		}

		@Override
		public String getStringReport(int tabCount) {
			// 记录测试类名称
			String text = "\t".repeat(tabCount) + "测试类名:" + testClassName + "\n";
			// 记录测试类中出现BUG的方法名
			text += "\t".repeat(tabCount) + "出现Bug的方法有：";
			for (String key : testClassInforemation.keySet()) {
				// 若该方法为出现BUG的测试方法，则进行存储其方法名（key则为方法名）
				if (testClassInforemation.get(key).isBugMethod()) {
					text += key;
				}
			}
			text += "\n";

			// 记录每个测试方法中的信息
			text += "\t".repeat(tabCount) + "以下是测试类中所有的测试方法的信息：\n";
			for (String key : testClassInforemation.keySet()) {
				text += testClassInforemation.get(key).getStringReport(tabCount + 1);
			}
			text += "\n";

			return text;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>RecordTool.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于存储、输出测试方法的信息
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2019年10月6日下午4:47:33
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2019年10月6日下午4:47:33
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	private static class MethodInformation implements AutoTestReport {
		/**
		 * 用于记录步骤数
		 */
		private int step = 0;

		/**
		 * 用于记录Bug数
		 */
		private int bug = 0;

		/**
		 * 用于标记该没方法是否出现Bug
		 */
		private boolean bugMethod = false;

		/**
		 * 用于记录方法的名称
		 */
		private String methodName = "";

		/**
		 * 初始化用例信息
		 */
		public MethodInformation(String methodName) {
			this.methodName = methodName;
		}

		/**
		 * 用于增加步骤的数量
		 */
		public void addStep() {
			step++;
		}

		/**
		 * 用于增加Bug的数量，并设置当前方法为出现Bug方法
		 */
		public void addBug() {
			bug++;
			bugMethod = true;
		}

		/**
		 * 用于返回当前方法的步骤数量
		 * 
		 * @return 步骤数量
		 */
		public int getStep() {
			return step;
		}

		/**
		 * 用于返回当前方法的Bug数量
		 * 
		 * @return Bug数量
		 */
		public int getBug() {
			return bug;
		}

		/**
		 * 返回方法是否为出现Bug的方法
		 * 
		 * @return 是否为出现Bug的方法
		 */
		public boolean isBugMethod() {
			return bugMethod;
		}

		/**
		 * 以json格式，输出方法信息，json包含字段及类型如下：<br>
		 * name：方法名称，为String类型<br>
		 * bug：方法的Bug数量，为int类型<br>
		 * step：方法的步骤数量，为int类型<br>
		 * isBugMethod：标记方法是否为出现Bug的方法，为boolean类型
		 * 
		 * @return 方法信息，json格式
		 */
		@Override
		public JSONObject getJsonInformation() {
			JSONObject json = new JSONObject();
			json.put("name", methodName);
			json.put("bug", bug);
			json.put("step", step);
			json.put("isBugMethod", bugMethod);
			return json;
		}

		@Override
		public String getStringReport(int tabCount) {
			// 记录方法名称
			String text = "\t".repeat(tabCount) + "测试方法名:" + methodName + "\n";
			// 记录步骤数量
			text += "\t".repeat(tabCount) + "步骤数量:" + step + "\n";
			// 记录Bug数量
			text += "\t".repeat(tabCount) + "Bug数量:" + bug + "\n";

			// 返回文本
			return text;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>RecordTool.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于定义每个与自动化测试报告记录相关的接口
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2019年10月6日下午3:07:11
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2019年10月6日下午3:07:11
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 */
	private interface AutoTestReport {
		/**
		 * 以json格式输出自动化测试运行的结果
		 * 
		 * @return json格式，自动化测试运行的结果
		 */
		JSONObject getJsonInformation();

		/**
		 * 以文本的形式输出自动化测试的结果
		 * 
		 * @param tabCount 文本前tab的个数，调整格式使用
		 * @return 文本格式，自动化测试的结果
		 */
		String getStringReport(int tabCount);
	}
}