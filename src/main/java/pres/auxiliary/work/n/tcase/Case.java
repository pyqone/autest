package pres.auxiliary.work.n.tcase;

import java.io.File;
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
	HashMap<String, String> textMap = new HashMap<String, String>();
	
	/**
	 * 根据用例xml文件来构造Case类
	 * @param configXmlFile xml配置文件
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public Case(File configXmlFile) {
		// 判断传入的configurationFile是否为一个文件类对象，若非文件类对象，则抛出异常
		try {
			configXml = new SAXReader().read(configXmlFile);
		} catch (DocumentException e) {
			throw new IncorrectFileException("用例xml文件有误" );
		}
		
		//获取xml中包含value的元素，并将其中包含需要替换的词语存储至textMap\
		List<Element> textElement = configXml.selectNodes("//*[@value]");
		textElement.stream().
		//获取元素的value属性，将其转换为文本对象	
		map(e -> e.attributeValue("value")).
		//筛选包含*{的文本
		filter(e -> e.indexOf("*{") > -1).forEach(e -> {
			//对文本按照*{切割，并筛选包含}*的文本
			Arrays.asList(e.split("\\*\\{")).stream().filter(s -> s.indexOf("}*") > -1).
			forEach(s -> {
				//将需要存储的替换词语存入textMap中
				textMap.put(s.substring(0, s.indexOf("}*")), "");
			});
		});
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
	void setStep(String[] step) {
		this.step = step;
	}

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
	void setExcept(String[] except) {
		this.except = except;
	}

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
	void setTitle(String title) {
		this.title = title;
	}

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
	void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

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
	void setPrecondition(String[] precondition) {
		this.precondition = precondition;
	}

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
	void setRank(int rank) {
		this.rank = rank;
	}
	
	/**
	 * 用于设置需要替换的词语
	 * @param word 测试用例xml库中需要替换的词语
	 * @param value 被替换的词语
	 */
	public void setReplaceWord(String word, String value) {
		//判断该词语是否存在于textMap中，若不存在，则抛出异常
		if (!textMap.containsKey(word)) {
			throw new IncorrectFileException("未找到需要替换的词语：" + word);
		}
		//存储替换的词语
		textMap.put(word, value);
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
			sb.replace(index, sb.indexOf(END_SIGN) + END_SIGN.length(), textMap.get(var));
		}
		
		return sb.toString();
	}
}
