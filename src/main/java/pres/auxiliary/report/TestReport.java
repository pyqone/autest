package pres.auxiliary.report;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pres.auxiliary.directory.exception.UndefinedDirectoryException;
import pres.auxiliary.work.selenium.xml.ByType;
import pres.auxiliary.work.selenium.xml.ReadXml;

/**
 * 该类用于从禅道上导出的BUG列表，使用项目路径下Templet的模版文件（TestReportTemplet.docx），来生成测试报告，
 * 支持压缩测试报告文件以及发送邮件到项目相关的人员（邮件功能需要配置项目路径下的ConfigurationFiles/
 * ReportConfigurationFile/TestReportConfiguration.xml文件）
 * 
 * @author 彭宇琦
 */
public class TestReport extends AbstractReport {
	/**
	 * 用于存储邮件内容
	 */
	private StringBuilder mail = new StringBuilder("");
	/**
	 * 设置配置文件存放的位置
	 */
	public final String xml = "ConfigurationFiles/ReportConfigurationFile/TestReportConfiguration.xml";
	/**
	 * 设置禅道元素xpath文件存放位置
	 */
	public final String xpath = "ConfigurationFiles/ReportConfigurationFile/ZendaoElementXpath.xml";
	/**
	 * 设置模版文件存放位置
	 */
	public final String templet = "Templet\\TestReportTemplet.docx";

	/**
	 * 存储项目的名称
	 */
	private String projectName;

	/**
	 * 存储项目的版本（带项目名称）
	 */
	private String projectVersion;

	/**
	 * 该构造只用于创建对象
	 */
	public TestReport() {
	}

	/**
	 * 该构造方法用于设置测试报告保存位置
	 * 
	 * @param savePath
	 *            测试报告保存位置
	 */
	public TestReport(String savePath) {
		setSavePath(savePath);
	}

	/**
	 * 该方法用于返回在发送测试报告邮件中正文的内容，不包括项目经理的名字等信息
	 * 
	 * @return 测试报告邮件中正文的内容
	 */
	/*
	public String getMailContent() {
		return mail.toString();
	}
	*/

	/**
	 * 用于根据项目名称或项目名的关键词来查询其项目的邮件发送人及收信人，注意，返回值是二维字符串数组，
	 * 其第一维表示收件人，第二维表示抄送人；收件人或抄送人返回的格式为：“姓名:邮箱”，如“张三:XX@qq.com”
	 * 
	 * @param project
	 *            项目名称或项目名称关键词
	 * @return 项目对应的邮件发送人及收信人
	 */
	@SuppressWarnings("unchecked")
	public String[][] getMailToAndCc(String project) {
		// TODO 还是要添加空标签到返回的列表中，否则第一次的项目无法通过该方法获取收件人与抄送人
		// 用于存储收件人及抄送人
		String[][] ss = new String[2][];

		// 读取配置好的XML文件
		Document dom = null;
		try {
			dom = new SAXReader().read(new File(xml));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 获取根节点
		Element root = dom.getRootElement();

		// 存储合适的收件人标签对象
		Element to = null;

		// 循环，遍历所有的收件人标签中项目的名称，查看是否有与之对应的项目收件人，若查不到，则查找一个无“project”属性的收件人标签，若仍没有，则报错
		for (Element toTem : (List<Element>) root.element("mail")
				.elements("to")) {
			int i = 0;
			// 判断当前遍历的标签是否包含project属性，不包含则暂时存储该标签，若整个遍历结束未找到对应的project，则该标签即为收件人标签
			if (toTem.attribute("project") == null) {
				// 判断XML文件是否只存在一个project属性为空的cc标签，若存在多个，则抛出异常
				if (++i <= 1) {
					to = toTem;
					continue;
				} else {
					throw new IncorrectGrammarException("存在多个没有project属性的to标签");
				}
			}
			// 判断当前遍历标签中的project属性的值是否与传入的project参数对应，若对应，则存储该标签
			if (toTem.attributeValue("project").indexOf(project) > -1) {
				to = toTem;
				break;
			}
		}

		// 判断遍历后是否存在合适的to标签，不存在则抛出异常
		if (to == null) {
			throw new IncorrectGrammarException("未找到相应项目的邮件接收人和抄送人");
		}

		// 用于拼接收件人及抄送人邮箱，由于收件人与抄送人都存在多个，所以不能直接使用使用setRecipient()方法
		StringBuilder toStr = new StringBuilder();
		StringBuilder ccStr = new StringBuilder();

		// 遍历to标签下的所有收件人，写入到邮件，并存储该收件人的名字到mailContent中
		for (Element preson : (List<Element>) (to.elements())) {
			// 判断当前收件人是否存在，不存在则添加收件人
			if (toStr.indexOf(preson.attributeValue("mail")) == -1) {
				// 拼接收件人名字及邮箱
				toStr.append(preson.attributeValue("name") + ":"
						+ preson.attributeValue("mail") + ",");
			}
		}

		// 遍历cc标签，设置抄送人
		for (Element preson : (List<Element>) root.element("mail").element("cc")
				.elements()) {
			// 判断抄送人是否存在或者存在于收件人之中，不存在，则添加该抄送
			if (ccStr.indexOf(preson.attributeValue("mail")) == -1
					&& toStr.indexOf(preson.attributeValue("mail")) == -1) {
				ccStr.append(preson.attributeValue("name") + ":"
						+ preson.attributeValue("mail") + ",");
			}
		}

		// 再将项目组的测试人员邮箱设置为抄送人
		for (Element testPreson : (List<Element>) root.element("report")
				.element("testperson").elements()) {
			// 判断测试组成员是否参与测试，参与测试则设置其邮箱为抄送
			if (testPreson.attributeValue("participation")
					.equalsIgnoreCase("true")) {
				// 判断抄送人是否存在或者存在于收件人之中，不存在，则添加该抄送
				if (ccStr.indexOf(testPreson.attributeValue("mail")) == -1
						&& toStr.indexOf(
								testPreson.attributeValue("mail")) == -1) {
					ccStr.append(testPreson.attributeValue("name") + ":"
							+ testPreson.attributeValue("mail") + ",");
				}
			}
		}
		// 添加自己
		ccStr.append(root.element("mail").attributeValue("name") + ":"
				+ root.element("mail").attributeValue("username"));

		ss[0] = toStr.toString().split("\\,");
		ss[1] = ccStr.toString().split("\\,");

		return ss;
	}

	/**
	 * 该方法用于完整创建一个测试报告，输入测试报告特有，自动化测试报告不能直接使用该方法
	 * 
	 * @param name
	 *            项目名称
	 * @param version
	 *            版本号（注意，此处的版本号是包括项目名称的）
	 * @param time
	 *            测试开始时间
	 * @param person
	 *            测试组参与人员
	 * @param range
	 *            测试范围
	 * @param purpose
	 *            测试目的
	 * @param gist
	 *            测试依据
	 * @param content
	 *            测试报告内容
	 * @param accessory
	 *            附件名称
	 * @return 邮件内容
	 * @throws IOException
	 */
	public String createReport(String name, String version, String time,
			String person, String range, String purpose, String gist,
			String content, String accessory) throws IOException {

		// 将测试结果写入模版
		// 用于存储待替换的文本及文本的定位
		Map<String, String> params = new HashMap<String, String>();
		// 存储替换的标签以及替换的文本
		params.put("name", name);
		params.put("version", version);
		params.put("time", time);
		params.put("purpose", purpose);
		params.put("gist", gist);
		params.put("person", person);
		params.put("range", range);
		params.put("accessory", accessory);
		params.put("result", content);

		// 读取模版文件
		FileInputStream fip = new FileInputStream(templet);
		// 使用POI中的读取word文件的方法
		XWPFDocument xd = new XWPFDocument(fip);
		// 关闭读出流
		fip.close();

		// 读取模版
		readeTemplet(xd, params);

		// 拼接邮件内容
		mail.append(content);

		// 判断是否打开文件夹，存在就自动弹出文件
		if (isOpenFolder()) {
			java.awt.Desktop.getDesktop().open(new File(getSavePath()));
		}

		// 打包测试报告
		compressReportFile(new File(getSavePath() + version + "版本测试报告"));

		// 删除从禅道上导出的BUG列表文件,参见Report类中bugListFile属性
		bugListFile.delete();

		return mail.toString();
	}

	@Override
	public String createReport(String name, String version, int testDay,
			String person, String range, int activeOne, int activeTwo,
			int activeThree, int activeFour, int one, int two, int three,
			int four, int noSulveOne, int noSulveTwo, int delayOne, int delayTwo, String accessory)
					throws IOException {
		// 读取配置文件的内容
		Document dom = null;
		try {
			dom = new SAXReader().read(new File(xml));
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
		// 获取测试报告中的内容
		Element content = dom.getRootElement().element("report")
				.element("content");

		// 用于存储测试输出及测试结果
		StringBuilder testResult = new StringBuilder("");

		// 判断测试结果文件保存路径和测试结果文件名是否存在，若不存在则抛出UndefinedDirectoryException异常
		if ("".equals(getSavePath()) || "".equals(getFileName())) {
			throw new UndefinedDirectoryException("未定义文件路径或者文件名，文件路径:"
					+ getSavePath() + "，文件名：" + getFileName());
		}

		// 用于存储测试时间
		StringBuilder time = new StringBuilder();
		// 判断是否需要修改测试时间，若不需要则自动计算，需要则直接读取
		if (content.element("time").attributeValue("is_change")
				.equalsIgnoreCase("false")) {
			// 存储测试结束时间，即当前的时间
			Date d = new Date();
			// 设置测试开始时间，调用Calendar类进行处理
			Calendar calendar = Calendar.getInstance();
			// 设置当前时间
			calendar.setTime(d);
			// 设置当前的天数减去测试时间，得到测试开始时间
			calendar.add(Calendar.DAY_OF_MONTH, (1 - testDay));
			// 将得到的时间转换为Date进行返回
			d = calendar.getTime();
			// 存储测试开始时间
			time.append(new SimpleDateFormat("yyyy.MM.dd").format(d));

			time.append("-");

			d = new Date();
			time.append(new SimpleDateFormat("yyyy.MM.dd").format(d));
			// d.setDate(d.getDate() - testDay);//方法过时
		} else {
			time.append(content.element("time").element("starttime").getText());
			time.append("-");
			time.append(content.element("time").element("endtime").getText());
		}

		// 读取配置文件中的测试目的
		StringBuilder purpose = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Element> purposes = content.element("purpose")
				.elements();
		// 获取测试目的
		for (int i = 0; i < purposes.size(); i++) {
			purpose.append((i + 1) + "." + purposes.get(i).getText() + "\n");
		}
		purpose.delete(purpose.lastIndexOf("\n"), purpose.length());

		// 读取配置文件中的测试依据
		StringBuilder gist = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Element> gists = content.element("gist")
				.elements();
		// 获取测试目的
		for (int i = 0; i < gists.size(); i++) {
			// 修改带${name}的
			if (gists.get(i).getText().indexOf("${name}") > -1) {
				gist.append((i + 1) + "." + gists.get(i).getText() + "\n");
				gist.replace(gist.indexOf("$"), gist.indexOf("}") + 1, name);
			} else {
				gist.append((i + 1) + "." + gists.get(i).getText() + "\n");
			}
		}
		gist.delete(gist.lastIndexOf("\n"), gist.length());

		// 编辑测试报告邮件中的内容
		mail.delete(0, mail.length());

		mail.append("附件是");
		mail.append(name + version + "版本测试报告，");

		if (activeOne != 0 || activeTwo != 0 || activeThree != 0
				|| activeFour != 0) {
			testResult.append("回归测试发现并激活"
					+ String.valueOf(
							activeOne + activeTwo + activeThree + activeFour)
					+ "个Bug，其中");
			if (activeOne != 0) {
				testResult.append("一级Bug有" + activeOne + "个，");
			}
			if (activeTwo != 0) {
				testResult.append("二级Bug有" + activeTwo + "个，");
			}
			if (activeThree != 0) {
				testResult.append("三级Bug有" + activeThree + "个，");
			}
			if (activeFour != 0) {
				testResult.append("四级Bug有" + activeFour + "个，");
			}
			testResult.replace(testResult.lastIndexOf("，"),
					testResult.lastIndexOf("，") + 1, "；");
		}
		testResult.append("新发现并提交" + String.valueOf(one + two + three + four)
				+ "个Bug，其中，");
		testResult.append("一级Bug有" + one + "个，");
		testResult.append("二级Bug有" + two + "个，");
		testResult.append("三级Bug有" + three + "个，");
		testResult.append("四级Bug有" + four + "个，");
		testResult.append("详情请查看禅道系统或者问题汇总中的Bug记录详情。");
		// 判断是否存在一级或二级的BUG，根据判断给出相应的文字结果
		if (one != 0 || two != 0 || activeOne != 0 || activeTwo != 0) {
			testResult.append("由于该版本存在二级以上的Bug，根据测试依据软件测试通过准则，判定该版本测试结果为：不通过。\n");
		} else {
			testResult.append("由于该版本未发现二级以上的Bug，根据测试依据软件测试通过准则，判定该版本测试结果为：通过。\n");
		}
		
		//若有严重的BUG存在，则获取其信息列表，之后将严重的BUG信息放入报告中
		if ( one != 0 || two != 0) {
			List<BugInformation> bugInformation = getBugInformations();
			testResult.append("其中，严重的BUG基本信息如下：\n");
			for (int i = 0; i < bugInformation.size(); i++) {
				testResult.append("●");
				testResult.append(bugInformation.get(i).bugTitle);
				testResult.append("（"+ bugInformation.get(i).bugLv + "级BUG，禅道编号为：" + bugInformation.get(i).bugID + "）\n");
			}
		}
		
		//添加未处理的BUG数量
		if ( noSulveOne != 0 || noSulveTwo != 0 || delayOne != 0 || delayTwo != 0 ) {
			testResult.append("在往期版本中，发现存在未解决、延期处理的一、二级Bug，总数为" + (noSulveOne + noSulveTwo + delayOne + delayTwo) + "个，其中，");
			if ( noSulveOne != 0 ) {
				testResult.append("未解决的一级Bug有" + noSulveOne + "个，");
			}
			if ( noSulveTwo != 0 ) {
				testResult.append("未解决二级Bug有" + noSulveTwo + "个，");
			}
			if ( delayOne != 0 ) {
				testResult.append("延期解决的一级Bug有" + delayOne + "个，");
			}
			if ( delayTwo != 0 ) {
				testResult.append("延期解决的二级Bug有" + delayTwo + "个，");
			}
			testResult.append("望技术经理、开发人员及项目经理重视！\n");
			
		}
		
		return createReport(name, (name + version), time.toString(), person,
				range, purpose.toString(), gist.toString(),
				testResult.toString(), accessory);
	}

	@Override
	public String createReport(File bugListFile, int testDay, String person,
			String range) throws IOException {
		// 判断测试结果文件保存路径和测试结果文件名是否存在，若不存在则抛出UndefinedDirectoryException异常
		if ("".equals(getSavePath())) {
			throw new UndefinedDirectoryException("未定义文件路径或者文件名，文件路径:"
					+ getSavePath() + "，文件名：" + getFileName());
		}

		// 调用读取Bug列表的方法，并将存储其返回值
		String[] s = readFile(bugListFile, testDay);

		setFileName(s[0] + "Ver" + s[1] + "版本测试报告");

		// bugListFile.delete();

		// 存储项目名称及版本
		projectName = s[0];
		projectVersion = s[0] + "Ver" + s[1];

		// 调用重载的方法，将得到的数组作为参数传入
		return createReport(s[0], ("Ver" + s[1]), testDay, person, range,
				Integer.valueOf(s[6]), Integer.valueOf(s[7]),
				Integer.valueOf(s[8]), Integer.valueOf(s[9]),
				Integer.valueOf(s[2]), Integer.valueOf(s[3]),
				Integer.valueOf(s[4]), Integer.valueOf(s[5]),
				Integer.valueOf(s[10]), Integer.valueOf(s[11]),Integer.valueOf(s[12]),Integer.valueOf(s[13]),
				("附：" + s[0] + "Ver" + s[1] + "版本Bug汇总表.xlsx"));
	}

	/**
	 * 该方法通过ConfigurationFiles/ReportConfigurationFile/ZendaoBasicInformation.
	 * xml配置文件中的信息来自动编写测试报告
	 * 
	 * @param isSandEMail
	 *            是否自动发送邮件
	 * 
	 * @param isPrintReport
	 *            是否打印测试报告
	 * 
	 * @throws DocumentException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public String AutoWriteReport(boolean isSandEMail, boolean isPrintReport)
			throws DocumentException, InterruptedException, IOException {
		// 定义读取xml文件的对象，用于取出基本配置
		Document dom = new SAXReader().read(new File(xml));

		// 获取基本信息并存储
		String url = dom.getRootElement().element("url").attributeValue("url");
		String firefoxDirectory = dom.getRootElement().element("firefox")
				.attributeValue("directory");
		String firefoxProfiles = dom.getRootElement().element("firefox")
				.attributeValue("profiles");
		String username = dom.getRootElement().element("user")
				.attributeValue("username");
		String password = dom.getRootElement().element("user")
				.attributeValue("password");
		String search = dom.getRootElement().element("project")
				.attributeValue("name");
		String downloadSaveDirectory = dom.getRootElement().element("firefox")
				.attributeValue("download_save_directory");
		String downloadFileName = dom.getRootElement().element("report")
				.attributeValue("download_file_name");
		String range = dom.getRootElement().element("report")
				.attributeValue("range");
		int testDay = Integer.valueOf(
				dom.getRootElement().element("report").attributeValue("days"));
		StringBuilder person = new StringBuilder();

		// 对禅道进行操作
		operationZendao(firefoxDirectory, firefoxProfiles, url, username,
				password, search, downloadFileName);

		// 添加参与测试的人员
		@SuppressWarnings("unchecked")
		List<Element> l = dom.getRootElement().element("report")
				.element("testperson").elements();
		for (Element e : l) {
			if (e.attributeValue("participation").equalsIgnoreCase("true")) {
				person.append(e.attributeValue("name") + "、");
			}
		}
		// 清除多余的符号
		person.delete(person.lastIndexOf("、"), person.lastIndexOf("、") + 1);

		// 进行读取csv文件的操作
		createReport(
				new File(downloadSaveDirectory + downloadFileName + ".csv"),
				testDay, person.toString(), range);

		// 发送邮件（若用户允许自动发送，则进行发送邮件的操作，其是否允许只能在调用AutoWriteReport()方法中传参设置，直接调用本方法时，默认为不发送）
		String content = mail.toString();
		if (isSandEMail) {
			try {
				content = sandMail(mail.toString(), projectName,
						(projectVersion + "版本测试报告"), range, new File(
								getSavePath() + projectVersion + "版本测试报告.zip"));

				// 发送完邮件后压缩文件已无用处，则自动删除
				new File(getSavePath() + projectVersion + "版本测试报告.zip")
						.delete();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}

		// 根据传入的参数栏判断是否打印报告，若传入的打印机名称不对或者网络不通时会打印失败，此时不做处理
		if (isPrintReport) {
			try {
				printReport(
						new File(getSavePath() + projectVersion + "版本测试报告\\"
								+ projectVersion + "版本测试报告.docx"),
						dom.getRootElement().element("report")
								.attributeValue("print_name"));
			} catch (Exception e) {
			}
		}

		return content;
	}

	/**
	 * 该方法用于发送邮件到相关的人员
	 * 
	 * @param reportContent
	 *            报告的内容
	 * @param project
	 *            项目名称或关键词
	 * @param title
	 *            邮件标题
	 * @param attch
	 *            附件
	 * @throws DocumentException
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked" })
	public String sandMail(String reportContent, String project, String title, String range, 
			File attch) throws DocumentException, UnsupportedEncodingException,
					MessagingException, IOException {
		// 读取配置好的XML文件
		Document dom = new SAXReader().read(new File(xml));
		// 获取根节点
		Element root = dom.getRootElement();

		// 获取发件邮箱的信息
		String mailUsername = root.element("mail").attributeValue("username");
		String mailPassword = root.element("mail").attributeValue("password");
		String mailSmtp = root.element("mail").attributeValue("smtp");

		// 定义邮件的内容
		String mailContent = getMailContent(reportContent, project, title, range);

		// 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();
		// 设置使用的协议（JavaMail规范要求）
		props.setProperty("mail.transport.protocol", "smtp");
		// 设置发件人的邮箱的 SMTP服务器地址
		props.setProperty("mail.smtp.host", mailSmtp);
		// 需要请求认证
		props.setProperty("mail.smtp.auth", "true");

		// 设置邮箱端口
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);

		// 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getInstance(props);
		// 设置为debug模式, 可以查看详细的发送 log，即在控制台打印日志，感觉用处不大
		// session.setDebug(true);

		// 创建邮件
		MimeMessage message = new MimeMessage(session);

		// 配置发件人及昵称（昵称为发件人姓名，以降低被认为是垃圾邮件的概率）
		message.setFrom(new InternetAddress(mailUsername,
				root.element("mail").attributeValue("name")));

		// 存储合适的收件人标签对象
		Element to = null;

		// 循环，遍历所有的收件人标签中项目的名称，查看是否有与之对应的项目收件人，若查不到，则查找一个无“project”属性的收件人标签，若仍没有，则报错
		for (Element toTem : (List<Element>) root.element("mail")
				.elements("to")) {
			int i = 0;
			// 判断当前遍历的标签是否包含project属性，不包含则暂时存储该标签，若整个遍历结束未找到对应的project，则该标签即为收件人标签
			if (toTem.attribute("project") == null) {
				// 判断XML文件是否只存在一个project属性为空的cc标签，若存在多个，则抛出异常
				if (++i <= 1) {
					to = toTem;
					continue;
				} else {
					throw new IncorrectGrammarException("存在多个没有project属性的to标签");
				}
			}

			// 判断当前遍历标签中的project属性的值是否与传入的project参数对应，若对应，则存储该标签
			if (toTem.attributeValue("project").indexOf(project) > -1) {
				to = toTem;
				break;
			}
		}

		// 判断遍历后是否存在合适的to标签，不存在则抛出异常
		if (to == null) {
			throw new IncorrectGrammarException("未查找到合适的to标签");
		}

		// 判断选择的标签是否为无project属性的标签，若是，则赋予其project属性，以便下一次快捷访问
		if (to.attribute("project") == null) {
			to.addAttribute("project", project);
			// 使用dom4j的漂亮格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置xml文件的编码方式
			format.setEncoding("GBK");
			// 写入xml
			XMLWriter xmlWriter = new XMLWriter(
					new FileOutputStream(new File(xml)), format);

			xmlWriter.write(dom);
			xmlWriter.close();
		}

		// 用于拼接收件人及抄送人邮箱，由于收件人与抄送人都存在多个，所以不能直接使用使用setRecipient()方法
		StringBuilder toStr = new StringBuilder();
		StringBuilder ccStr = new StringBuilder();

		// 设置多个收件人
		message.setRecipients(RecipientType.TO,
				InternetAddress.parse(toStr.toString()));

		// 遍历cc标签，设置抄送人
		for (Element preson : (List<Element>) root.element("mail").element("cc")
				.elements()) {
			/*
			 * // 设置抄送人 message.setRecipient(MimeMessage.RecipientType.CC, new
			 * InternetAddress(preson.attributeValue("mail"), "", "UTF-8"));
			 */
			// 判断抄送人是否存在或者存在于收件人之中，不存在，则添加该抄送
			if (ccStr.indexOf(preson.attributeValue("mail")) == -1
					&& toStr.indexOf(preson.attributeValue("mail")) == -1) {
				ccStr.append(preson.attributeValue("mail") + ",");
			}
		}
		// 再将项目组的测试人员邮箱设置为抄送人
		for (Element testPreson : (List<Element>) root.element("report")
				.element("testperson").elements()) {
			// 判断测试组成员是否参与测试，参与测试则设置其邮箱为抄送
			if (testPreson.attributeValue("participation")
					.equalsIgnoreCase("true")) {
				/*
				 * message.setRecipient(MimeMessage.RecipientType.CC, new
				 * InternetAddress(testPreson.attributeValue("mail"), "",
				 * "UTF-8"));
				 */
				// 判断抄送人是否存在或者存在于收件人之中，不存在，则添加该抄送
				if (ccStr.indexOf(testPreson.attributeValue("mail")) == -1
						&& toStr.indexOf(
								testPreson.attributeValue("mail")) == -1) {
					ccStr.append(testPreson.attributeValue("mail") + ",");
				}
			}
		}
		// 再将自己拼接入抄送人中，以避免邮件无法发送
		ccStr.append(root.element("mail").attributeValue("username"));
		// 设置多个抄送人
		message.setRecipients(RecipientType.CC,
				InternetAddress.parse(ccStr.toString()));

		// 设置邮件主题
		message.setSubject(title);

		// 设置邮件正文，注意，其附件也属于正文内容，需要添加一个MimeMultipart对象才能将整个邮件内容添加进去，使用混合关系
		MimeMultipart msgMultipart = new MimeMultipart("mixed");

		// 添加附件
		MimeBodyPart mailAttch = new MimeBodyPart();
		// 把文件，添加到附件中，DataHandler类表示源文件，FileDataSource类为文件处理器
		mailAttch.setDataHandler(new DataHandler(new FileDataSource(attch)));
		// 设置附件名称，注意，文件名称不能太长，否则文件无法上传，甚至被退回的可能，所以只能写死为“report.zip”
		mailAttch.setFileName("report.zip");
		// 将附件添加至msgMultipart中
		msgMultipart.addBodyPart(mailAttch);

		// 添加邮件正文
		MimeBodyPart mailText = new MimeBodyPart();

		// 将正文添加至mailText中
		mailText.setContent(mailContent, "text/html;charset=GBK");
		// 将邮件正文添加至msgMultipart中
		msgMultipart.addBodyPart(mailText);

		// 将msgMultipart添加至邮件内容中
		message.setContent(msgMultipart);

		// 设置发件时间
		message.setSentDate(new Date());

		// 保存设置
		message.saveChanges();

		// 根据 Session 获取邮件传输对象
		Transport transport = session.getTransport();

		// 使用邮箱账号和密码连接邮件服务器，这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
		transport.connect(mailUsername, mailPassword);

		// 发送邮件
		transport.sendMessage(message, message.getAllRecipients());

		// 关闭连接
		transport.close();
		
		return mailContent;
	}
	
	/**
	 * 该方法用于返回在发送测试报告邮件中正文的内容，不包括项目经理的名字等信息
	 * 
	 * @param reportContent
	 *            报告的内容
	 * @param project
	 *            项目名称或关键词
	 * @param title
	 *            邮件标题
	 * @param range
	 *            测试范围
	 * @return 测试报告邮件中正文的内容
	 * 
	 * @throws DocumentException
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String getMailContent(String reportContent, String project, String title, String range) throws DocumentException, UnsupportedEncodingException,
					MessagingException, IOException {
		// 读取配置好的XML文件
		Document dom = new SAXReader().read(new File(xml));
		// 获取根节点
		Element root = dom.getRootElement();

		// 定义邮件的内容
		StringBuilder mailContent = new StringBuilder();

		// 存储合适的收件人标签对象
		Element to = null;

		// 循环，遍历所有的收件人标签中项目的名称，查看是否有与之对应的项目收件人，若查不到，则查找一个无“project”属性的收件人标签，若仍没有，则报错
		for (Element toTem : (List<Element>) root.element("mail")
				.elements("to")) {
			int i = 0;
			// 判断当前遍历的标签是否包含project属性，不包含则暂时存储该标签，若整个遍历结束未找到对应的project，则该标签即为收件人标签
			if (toTem.attribute("project") == null) {
				// 判断XML文件是否只存在一个project属性为空的cc标签，若存在多个，则抛出异常
				if (++i <= 1) {
					to = toTem;
					continue;
				} else {
					throw new IncorrectGrammarException("存在多个没有project属性的to标签");
				}
			}

			// 判断当前遍历标签中的project属性的值是否与传入的project参数对应，若对应，则存储该标签
			if (toTem.attributeValue("project").indexOf(project) > -1) {
				to = toTem;
				break;
			}
		}

		// 判断遍历后是否存在合适的to标签，不存在则抛出异常
		if (to == null) {
			throw new IncorrectGrammarException("未查找到合适的to标签");
		}

		// 判断选择的标签是否为无project属性的标签，若是，则赋予其project属性，以便下一次快捷访问
		if (to.attribute("project") == null) {
			to.addAttribute("project", project);
			// 使用dom4j的漂亮格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置xml文件的编码方式
			format.setEncoding("GBK");
			// 写入xml
			XMLWriter xmlWriter = new XMLWriter(
					new FileOutputStream(new File(xml)), format);

			xmlWriter.write(dom);
			xmlWriter.close();
		}

		// 用于拼接收件人及抄送人邮箱，由于收件人与抄送人都存在多个，所以不能直接使用使用setRecipient()方法
		StringBuilder toStr = new StringBuilder();
		StringBuilder ccStr = new StringBuilder();

		mailContent.append("<p>");
		// 遍历to标签下的所有收件人，写入到邮件，并存储该收件人的名字到mailContent中
		for (Element preson : (List<Element>) (to.elements())) {
			// 判断当前收件人是否存在，不存在则添加收件人
			if (toStr.indexOf(preson.attributeValue("mail")) == -1) {
				// 拼接邮箱地址
				toStr.append(preson.attributeValue("mail") + ",");
				// 拼接收件人名称到邮件内容中
				mailContent.append(preson.attributeValue("name") + "、");
			}
		}

		// 将邮件内容的最后一个顿号改为冒号
		mailContent.replace(mailContent.lastIndexOf("、"),
				mailContent.lastIndexOf("、") + 1, "：");
		mailContent.append("</p>");

		// 遍历cc标签，设置抄送人
		for (Element preson : (List<Element>) root.element("mail").element("cc")
				.elements()) {
			/*
			 * // 设置抄送人 message.setRecipient(MimeMessage.RecipientType.CC, new
			 * InternetAddress(preson.attributeValue("mail"), "", "UTF-8"));
			 */
			// 判断抄送人是否存在或者存在于收件人之中，不存在，则添加该抄送
			if (ccStr.indexOf(preson.attributeValue("mail")) == -1
					&& toStr.indexOf(preson.attributeValue("mail")) == -1) {
				ccStr.append(preson.attributeValue("mail") + ",");
			}
		}
		// 再将项目组的测试人员邮箱设置为抄送人
		for (Element testPreson : (List<Element>) root.element("report")
				.element("testperson").elements()) {
			// 判断测试组成员是否参与测试，参与测试则设置其邮箱为抄送
			if (testPreson.attributeValue("participation")
					.equalsIgnoreCase("true")) {
				/*
				 * message.setRecipient(MimeMessage.RecipientType.CC, new
				 * InternetAddress(testPreson.attributeValue("mail"), "",
				 * "UTF-8"));
				 */
				// 判断抄送人是否存在或者存在于收件人之中，不存在，则添加该抄送
				if (ccStr.indexOf(testPreson.attributeValue("mail")) == -1
						&& toStr.indexOf(
								testPreson.attributeValue("mail")) == -1) {
					ccStr.append(testPreson.attributeValue("mail") + ",");
				}
			}
		}
		// 再将自己拼接入抄送人中，以避免邮件无法发送
		ccStr.append(root.element("mail").attributeValue("username"));

		// 添加测试范围至邮箱正文中
		mailContent.append("<p style='text-indent:2em;'><b>该版本的测试范围为：" + range + "</b></p>");
		
		// 判断测试报告是否有换行内容，若有，则按照换行符分割
		if (reportContent.indexOf("\n") > -1) {
			boolean createTable = false;
			// 分割字符串
			String[] ss = reportContent.toString().split("\\n");
			for (int i = 0; i < ss.length; i++) {
				if ( ss[i].indexOf("●") > -1 ) {
					if ( !createTable ) {
						createTable = true;
						mailContent.append("<table border='1' align='center'");
						mailContent.append("<tr>");
						mailContent.append("<td style='text-align: center;'>");
						mailContent.append("<b>Bug标题</b>");
						mailContent.append("</td>");
						mailContent.append("<td style='text-align: center;'>");
						mailContent.append("<b>Bug等级</b>");
						mailContent.append("</td>");
						mailContent.append("<td style='text-align: center;'>");
						mailContent.append("<b>Bug编号</b>");
						mailContent.append("</td>");
						mailContent.append("</tr>");
					}
					
					if ( createTable ) {
						mailContent.append("<tr>");
						mailContent.append("<td>");
						mailContent.append(ss[i].substring(ss[i].indexOf("●") + "●".length(), ss[i].indexOf("（")));
						mailContent.append("</td>");
						mailContent.append("<td style='text-align: center;'>");
						mailContent.append(ss[i].substring(ss[i].indexOf("（") + "（".length(), ss[i].indexOf("（") + "（".length() + 1));
						mailContent.append("</td>");
						mailContent.append("<td style='text-align: center;'>");
						mailContent.append(ss[i].substring(ss[i].lastIndexOf("：") + "：".length(), ss[i].length() - 1));
						mailContent.append("</td>");
						mailContent.append("</tr>");
						continue;
					}
				} else {
					if ( createTable ) { 
						mailContent.append("</table>");
						createTable = false;
					}
				}
				
				// 添加剩余的邮件正文到mailContent中
				mailContent.append("<p style='text-indent:2em;'>");
				mailContent.append(ss[i] + "</p>");
			}
		} else {
			// 添加剩余的邮件正文到mailContent中
			mailContent.append("<p style='text-indent:2em;'>");
			mailContent.append(reportContent + "</p>");
		}

		// 判断是否有邮件的追加内容，若有则添加邮件追加内容
		if (root.element("mail").element("addcontent") != null || !root
				.element("mail").element("addcontent").getText().equals("")) {
			mailContent.append("<p style='text-indent:2em;'>");
			mailContent
					.append(root.element("mail").element("addcontent").getText()
							+ "</p>");
		}

		// 读取写信人的名字
		mailContent.append("<p align='right'>");
		mailContent.append(
				"软件部品控科：" + root.element("mail").attributeValue("name"));
		mailContent.append("</p>");
		
		return mailContent.toString();
	}

	/**
	 * 该方法用于将测试报告文件夹及其所有的文件压缩成压缩
	 * 
	 * @param reportFolder
	 *            待压缩的文件夹对象
	 * @throws IOException
	 */
	public void compressReportFile(File reportFolder) throws IOException {
		// 获取测试报告文件夹中所有的文件
		File[] fileList = reportFolder.listFiles();
		// 创建压缩文件流对象
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(new File(reportFolder.getParent() + "\\"
						+ reportFolder.getName() + ".zip"))));
		// 定义缓存变量
		byte[] bufs = new byte[1024 * 10];
		// 循环，将文件夹中的文件全部写入到压缩文件中
		for (int i = -1; i < fileList.length; i++) {
			// -1的作用：用于在压缩文件中添加根目录文件夹
			if (i == -1) {
				// 压缩文件夹的方法
				zos.putNextEntry(new ZipEntry(reportFolder.getName() + "/"));
				continue;
			}

			// 创建ZIP实体，并添加进压缩包
			zos.putNextEntry(new ZipEntry(
					reportFolder.getName() + "/" + fileList[i].getName()));
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
	 * 用于自动在禅道上导出BUG汇总表
	 * 
	 * @param firefoxDirectory
	 * @param firefoxProfiles
	 * @param url
	 * @param username
	 * @param password
	 * @param search
	 * @param downloadFileName
	 * @throws InterruptedException
	 */
	public void operationZendao(String firefoxDirectory, String firefoxProfiles,
			String url, String username, String password, String search,
			String downloadFileName) throws InterruptedException {
		// 定义浏览器对象，自动登录禅道进行导出BUG汇总表的工作
		WebDriver d = new FirefoxBrower(firefoxDirectory, firefoxProfiles, url)
				.getDriver();
		// 定义Control对象，读取禅道元素定位信息配置文件
		Event event = Event.newInstance(d);
		event.setXmlFile(new File(xpath));
		ReadXml r = new ReadXml(new File(xpath));
		// 操作浏览器
		event.getTextEvent().input("用户名", username);
		event.getTextEvent().input("密码", password);
		event.getClickEvent().click("登录");
		event.getClickEvent().click("测试");
		event.getClickEvent().click("Bug");
		event.getClickEvent().click("项目下拉框");

		// 由于禅道的项目搜索机制与其他页面不同，即使搜索了其<ul>标签下也显示所有的<li>，故不能通过搜索后获取第一个元素来对搜索到的项目进行定位
		List<WebElement> l = null;
		while (true) {
			l = d.findElements(r.getBy("项目选项", ByType.XPATH));
			if (l.size() != 0) {
				break;
			} else {
				Thread.sleep(100);
			}
		}
		for (WebElement e : l) {
			if (e.getText().indexOf(search) > -1) {
				e.click();
				break;
			}
		}
		event.getClickEvent().click("所有");

		event.getClickEvent().click("ID");
		// 获取点击ID排序前后第一条BUG的ID号，以便判断当前是按照什么规则排序
		int before = Integer.valueOf(event.getTextEvent().getText("第一条Bug的ID").getStringValve());
		event.getClickEvent().click("ID");
		int after = Integer.valueOf(event.getTextEvent().getText("第一条Bug的ID").getStringValve());
		// 判断按ID排序前后，其第一条BUG的ID值是否是前比后大，若前比后大，则说明当前是升序，则应再点击一次ID，按降序排
		if (before > after) {
			event.getClickEvent().click("ID");
		}
		event.getClickEvent().click("导出");
		event.getClickEvent().click("导出数据");
		event.switchFrame("导出数据窗口");
		event.getTextEvent().input("文件名", downloadFileName);
		event.getSelectEvent().select("文件格式下拉框", 0);
		event.getSelectEvent().select("编码格式下拉框", 1);
		event.getSelectEvent().select("记录下拉框", 0);
		// 禅道的导出按钮无法点击，故需要通过提交表单的方法来导出
		d.findElement(r.getBy("导出数据表单", ByType.XPATH)).submit();
		d.close();
	}
}
