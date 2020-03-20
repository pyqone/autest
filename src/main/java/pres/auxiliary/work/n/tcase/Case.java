package pres.auxiliary.work.n.tcase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <p><b>文件名：</b>AbstractCase.java</p>
 * <p><b>用途：</b>定义测试用例模板类能返回的基本字段，提供其相应的get与set方法，但该方法不允许包外调用</p>
 * <p><b>编码时间：</b>2020年3月3日下午8:07:23</p>
 * <p><b>修改时间：</b>2020年3月4日 07:39:23</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public abstract class Case {
	/**
	 * 步骤
	 */
	private String[] step;
	
	/**
	 * 预期
	 */
	private String[] except;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 关键词
	 */
	private String keyWord;
	
	/**
	 * 前置条件
	 */
	private String[] precondition;
	
	/**
	 * 用于存储需要替换的词语的开始标记
	 */
	final String START_SIGN = "*{";
	/**
	 * 用于存储需要替换的词语的结束标记
	 */
	final String END_SIGN = "}*";
	
	/**
	 * 用于存储传入到正则表达式中的开始标记
	 */
	final String START_SIGN_REGIX = "\\*\\{";
	
	/**
	 * 优先级
	 */
	private int rank;
	
	/**
	 * 用于指向测试用例xml文件的Document对象
	 */
	Document configXml;	
	
	/**
	 * 存储xml文件中其需要替换的词语
	 */
	HashMap<String, String> wordMap = new HashMap<String, String>(16);
	
	/**
	 * 存储字段的文本内容
	 */
	HashMap<String, ArrayList<String>> fieldTextMap = new HashMap<String, ArrayList<String>>(16);
	
	/**
	 * 根据用例xml文件来构造Case类
	 * @param configXmlFile xml配置文件
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public Case(File configXmlFile) {
		// 判断传入的configurationFile是否为一个文件类对象，若非文件类对象，则抛出异常
		try {
			configXml = new SAXReader().read(configXmlFile);
		} catch (DocumentException e) {
			throw new IncorrectFileException("用例xml文件有误" );
		}
		
		//查找并存储替换的词语
		saveWord();
		//保存字段的词语
		saveField();
	}

	/**
	 * 返回步骤文本
	 * @return 步骤文本
	 */
	public String[] getStep() {
		return step;
	}

	/**
	 * 根据调用方法的不同设置相应的步骤文本
	 * @param step 步骤文本
	 */
//	void setStep(String[] step) {
//		this.step = step;
//	}

	/**
	 * 返回预期文本
	 * @return 预期文本
	 */
	public String[] getExcept() {
		return except;
	}

	/**
	 * 根据调用方法的不同设置相应的预期文本
	 * @param except 预期文本
	 */
//	void setExcept(String[] except) {
//		this.except = except;
//	}

	/**
	 * 返回标题文本
	 * @return 标题文本
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 根据调用方法的不同设置相应的标题文本
	 * @param title 标题文本
	 */
//	void setTitle(String title) {
//		this.title = title;
//	}

	/**
	 * 返回关键词文本
	 * @return 关键词文本
	 */
	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * 根据调用方法的不同设置相应的关键词文本
	 * @param keyWord 关键词文本
	 */
//	void setKeyWord(String keyWord) {
//		this.keyWord = keyWord;
//	}

	/**
	 * 返回关键词文本
	 * @return 关键词文本
	 */
	public String[] getPrecondition() {
		return precondition;
	}

	/**
	 * 根据调用方法的不同设置相应的前置条件文本
	 * @param keyWord 前置条件文本
	 */
//	void setPrecondition(String[] precondition) {
//		this.precondition = precondition;
//	}

	/**
	 * 返回优先级文本
	 * @return 优先级文本
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * 根据调用方法的不同设置相应的优先级文本
	 * @param keyWord 优先级文本
	 */
//	void setRank(int rank) {
//		this.rank = rank;
//	}
	
	/**
	 * 用于设置需要替换的词语
	 * @param word 测试用例xml库中需要替换的词语
	 * @param value 被替换的词语
	 */
	public void setReplaceWord(String word, String text) {
		//判断该词语是否存在于textMap中，若不存在，则抛出异常
		if (!wordMap.containsKey(word)) {
			throw new IncorrectFileException("未找到需要替换的词语：" + word);
		}
		//存储替换的词语
		wordMap.put(word, text);
	}
	
	/**
	 * 用于设置测试用例相应的所有信息
	 * @param title 标题
	 * @param step 步骤
	 * @param except 预期
	 * @param precondition 前置条件
	 * @param keyWord 关键词
	 * @param rank 优先级
	 */
	void setAllContent(String title, String[] step, String[] except, String[] precondition, String keyWord, int rank) {
		this.step = step;
		this.except = except;
		this.title = title;
		this.keyWord = keyWord;
		this.precondition = precondition;
		this.rank = rank;
	}
	
	/**
	 * 用于初始化所有的内容
	 */
	public void clearAllContent() {
		step = null;
		except = null;
		title = "";
		keyWord = "";
		precondition = null;
		rank = 1;
	}
	
	/**
	 * 用于替换文本中需要替换的单词，返回替换后的文本
	 * @param text 需要替换的文本
	 * @return 替换后的文本
	 */
	String replaceText(String text) {
		StringBuilder sb = new StringBuilder(text);
		//存储替换符的位置
		int index = 0;
		//循环，替换content中所有需要替换的信息
		while( (index = sb.indexOf(START_SIGN)) != -1 ) {
			//存储待替换的变量名
			String var = sb.substring(index + START_SIGN.length(), sb.indexOf(END_SIGN));
			//替换该变量名
			sb.replace(index, sb.indexOf(END_SIGN) + END_SIGN.length(), wordMap.get(var));
		}
		
		return sb.toString();
	}
	
	/**
	 * 用于获取用例xml中对应用例的标签内的文本，并返回替换词语后的文本
	 * @param caseName 用例名称
	 * @param label 标签枚举
	 * @param id 对应标签的id属性
	 * @return 标签中存储的文本，并进行处理
	 */
	String getLabelText(String caseName, LabelType label, String id) {
		//定位case标签的名称属性名
		String caseLabelNameAttribute = "name";
		//定位标签中能指向调用用例的属性（id）
		String labelIdAttribute = "id";
		//定位相应标签中存储用例内容的属性
		String labelValueAttribute = "value";
		
		//拼接xpath，规则"//case[@name='caseName']//标签名称[@id='id']"
		String xpath = "//" + LabelType.CASE.getName() + 
				"[@" + caseLabelNameAttribute + "='" + 
				caseName + "']//" + label.getName() + 
				"[@" + labelIdAttribute + "='" + id +"']";
		
		//获取相应的文本内容
		Element textElement = (Element)(configXml.selectSingleNode(xpath));
		//判断获取的内容是否为空，为空则跑出异常
		
		//判断集合是否存在元素，若不存在元素，则抛出异常
		if (textElement == null) {
			throw new LabelNotFoundException("不存在的标签：<" + label.getName() + " " + labelIdAttribute + "='" + id +"'...>");
		}
		
		//返回处理替换的单词后相应的文本
		return replaceText(textElement.attributeValue(labelValueAttribute));
		
	}
	
	/**
	 * 用于获取用例xml中对应用例的标签内所有的文本，并返回替换词语后的文本
	 * @param caseName 用例名称
	 * @param label 标签枚举
	 * @return 标签中存储的文本，并进行处理
	 */
	@SuppressWarnings("unchecked")
	ArrayList<String> getAllLabelText(String caseName, LabelType label) {
		//定位case标签的名称属性名
		String caseLabelNameAttribute = "name";
		//定位相应标签中存储用例内容的属性
		String labelValueAttribute = "value";
		
		//拼接xpath，规则"//case[@name='caseName']//标签名称[@id='id']"
		String xpath = "//" + LabelType.CASE.getName() + 
				"[@" + caseLabelNameAttribute + "='" + 
				caseName + "']//" + label.getName();

		//获取所有的节点
		List<Element> textElements = configXml.selectNodes(xpath);
		//存储节点中的value属性内的文本
		ArrayList<String> texts = new ArrayList<String>();
		//存储节点值
		for (int i = 0; i < textElements.size(); i++) {
			texts.add(replaceText(textElements.get(i).attributeValue(labelValueAttribute)));
		}
		
		return texts;
	}
	
	/**
	 * 用于获取并存储需要替换的词语
	 */
	@SuppressWarnings("unchecked")
	private void saveWord() {
		//定义能获取到文本的属性，以便于后续的调整
		String textAttribute = "value";
				
		//获取xml中包含value的元素，并将其中包含需要替换的词语存储至wordMap
		List<Element> textElement = configXml.selectNodes("//*[@" + textAttribute + "]");
		textElement.stream().
		//获取元素的value属性，将其转换为文本对象	
		map(e -> e.attributeValue(textAttribute)).
		//筛选包含*{的文本
		filter(e -> e.indexOf(START_SIGN) > -1).forEach(e -> {
			//对文本按照*{切割，并筛选包含}*的文本
			Arrays.asList(e.split(START_SIGN_REGIX)).stream().filter(s -> s.indexOf(END_SIGN) > -1).
			forEach(s -> {
				//将需要存储的替换词语存入textMap中
				wordMap.put(s.substring(0, s.indexOf(END_SIGN)), "");
			});
		});
	}
	
	/**
	 * 用于保存xml文件中的字段
	 */
	@SuppressWarnings("unchecked")
	void saveField() {
		//获取case标签下所有的标签，存储至fieldTextMap，以初始化所有的字段名称
		((List<Element>) (configXml.getRootElement().elements("case"))).forEach(caseElement -> {
			((List<Element>) caseElement.elements()).forEach(labelElement -> {
				//去掉末尾的s
				String name = labelElement.getName();
				fieldTextMap.put(name.substring(0, name.length() - 1), new ArrayList<String>());
			});
		});
	}
	
	/**
	 * 用于添加一行文本
	 * @param label 标签名称（枚举）
	 * @param text 相应内容
	 */
	void addFieldText(LabelType label, String text) {
		fieldTextMap.get(label.getName()).add(text);
	}
	
	/**
	 * 用于添加带序号的一行文本
	 * @param label 标签名称（枚举）
	 * @param text 相应内容
	 */
	void addFieldText(LabelType label, int index, String text) {
		fieldTextMap.get(label.getName()).add(index + "." + text);
	}
	
	/**
	 * 用于清空字段的内容，以避免存储上一次输入的用例
	 */
	void clearFieldText() {
		fieldTextMap.forEach((key, value) -> {
			fieldTextMap.get(key).clear();
		});
	}
	
	/**
	 * 返回字段内容Map，测试使用
	 * @return
	 */
	public HashMap<String, ArrayList<String>> getFieldTextMap() {
		return fieldTextMap;
	}
}
