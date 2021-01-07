# autest
## 简介
autest为Auxiliary Test的英文缩写意为辅助测试，其中包括日常测试工作中能用到的工具，包括测试用例编写工具，简化Web UI自动化测试工具以及测试报告生成工具和日常工作中使用的小工具等。开发这个项目的目的在于使用简单的代码来简化我们测试日常中较为繁杂的操作，使测试的效率得到一定的提升。

### autest设计初衷
autest比起说是一个工具，不如说是我在工作中的一个总结，在我看来，测试工作就是一个机械式的工作，既然是机械式的工作就应该用机械来代替，抱着这个想法，于是我就启动了这个项目。工具第一版实际上是我自己为简化Web UI自动化脚本而做的一个对selenium代码简单的封装，但随着工作经验的累积，我在工作中越发地发现测试工作中很多的地方都是重复的工作，但这个重复的工作又不得不去做，并且做这件重复的事情花的时间还不是一般的长，比如编写测试用例、编写测试报告，于是我便下定决心要把这些工具整合到我的代码中。

另一方面，随着自动化测试这个名词逐渐的流行，很多的公司跟风，要求测试部门也要弄一个自动化出来。当时我在一家公司当测试主管，经理也希望我能弄一个自动化。对我而言还好，我对java不算精通，但也是到熟练的地步了，但我的同事就并非都会代码了。面对这一痛点，许多的公司都选择使用软件来代替编码，我也曾用过这样的软件，但对我而言，那就是一种痛苦，一种有力却使不上的憋屈。很多的操作，我通过编码的方式是分分钟能解决的，但搬到软件上，别人没有提供给你方法，你只能干瞪眼。基于这个问题，于是我便不断地探寻一种简单的方法来编写脚本，并不断地培训同事，告诉他们怎么去编写，遇到什么样的事件调什么方法，在这一方面，我还是取得了不错的实践效果的。

但我并不是开发转测试，在大学学习的也是化学工程专业，并未系统地学习过软件工程，所以在编写代码时会有许多与开发规范不符合的地方，关于这点，希望大家能在使用时多多海涵，同时也希望大家能对工具多多批评和指点，我会尽可能地做出改正，使工具更加地完善。

### 目标
autest的目标是使用代码来简化繁杂的测试工作，让测试工作变得轻松，同时，也让不会写代码的测试工程师开始熟悉代码，通过代码编写自动化测试脚本，脱离被自动化测试软件束缚，让自动化测试变得更加自由。

## 工具概要
|       模块      |    介绍     |
|---------------------|----------------|
|com.auxiliary.tool|包含日常测试工作中可以使用的测试工具，例如表格处理工具、文本处理工具、日期处理工具等|
|com.auxiliary.selenium|对Web UI自动化工具selenium的二次封装，简化了selenium中比较复杂的代码|
|com.auxiliary.testcase|用于编写测试用例的工具|
|com.auxiliary.http|用于做接口测试时使用的工具|
|com.auxiliary.db|用于对数据库操作的工具|

## 工具介绍

### 1 测试用例编写工具
该工具是通过预先写好的测试用例文件模板，调用其中添加内容的方法，对测试用例进行编写，之后再生成一个Excel文件，以方便测试用例阅读与上传。当然，使用代码的方式编写测试用例，适用于存在大量替换和模板式测试用例的场景。

#### 1.1 创建模板配置文件
首先，我们需要创建一个测试用例文件模板xml配置文件，文件的创建方法可参考Wiki中[《测试用例文件创建》](https://gitee.com/pyqone/autest/wikis/%E6%B5%8B%E8%AF%95%E7%94%A8%E4%BE%8B%E6%96%87%E4%BB%B6%E5%88%9B%E5%BB%BA?sort_id=3351622)一文，此处直接引用项目路径下的jira文件模板（可参考：[/ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml](https://gitee.com/pyqone/autest/blob/master/ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira%E6%B5%8B%E8%AF%95%E7%94%A8%E4%BE%8B%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xml)）。

#### 1.2 构造测试用例编写类
获得文件后，我们便可使用测试用例类来编写测试用例。这里我使用项目中已提供的jira用例编写类（JiraTestCaseWrite类）来进行讲解，详细的用例创建方法可参考Wiki中[《测试用例编写》](https://gitee.com/pyqone/autest/wikis/%E6%B5%8B%E8%AF%95%E7%94%A8%E4%BE%8B%E7%BC%96%E5%86%99?sort_id=3354705)和[《测试用例编写类扩展》](https://gitee.com/pyqone/autest/wikis/%E6%B5%8B%E8%AF%95%E7%94%A8%E4%BE%8B%E7%BC%96%E5%86%99%E7%B1%BB%E6%89%A9%E5%B1%95?sort_id=3354709)两篇文章。

#### 1.3 用例编写需求
假设，我们需要编写姓名、身份证号码和手机号码的测试用例，限制如下：
* 定姓名可随意输入，不设限制
* 身份证为中国公民身份证的校验规则
* 手机号码为一般11位号码
* 表单提交需要点击保存按钮
* 创建的信息为“账号”。
![用例截图](https://images.gitee.com/uploads/images/2021/0107/143719_65b90c76_1776234.png "用例截图")

#### 1.4 Demo
根据以上用例编写需求，结合TestNG框架，则具体的代码为：
```java
/**
 * 指向配置文件
 */
final File CONFIG_FILE = new File("ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
/**
 * 指向用例文件创建路径
 */
final File CASE_FILE = new File("src/main/java/com/auxiliary/testcase/file/测试用例.xlsx");
/**
 * 指向用例模板文件
 */
final File CASE_TEMP_CONFIG_FILE = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml");

/**
 * 测试用例模板类
 */
InformationCase infoCase;
/**
 * jira用例编写类
 */
JiraTestCaseWrite jira;

@BeforeClass
public void initData() throws DocumentException {
	jira = new JiraTestCaseWrite(CONFIG_FILE, CASE_FILE);
	infoCase = new InformationCase(CASE_TEMP_CONFIG_FILE);
	
	//添加需要替换的词语
	infoCase.setReplaceWord(InformationCase.ADD_INFORMATION, "账号");
	infoCase.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
}

@AfterClass
public void openFolder() throws IOException {
	//写入数据到文件中
	jira.writeFile();
}

@Test
public void addCase() {
	jira.setReplactWord("保存", "点击“保存”按钮");
	jira.setReplactWord("成功", "#成功#");
	jira.setReplactWord("失败", "#失败#");
	
	jira.addTitle("通过不同的姓名创建账号")
	.addStep("“姓名”文本框不输入任何信息，#保存#", 
			"在“姓名”文本框中只输入空格，#保存#", 
			"在“姓名”文本框中输入HTML代码，#保存#")
	.addExcept("#成功#", 
			"#成功#", 
			"#成功#，且HTML代码不会被转义")
	.addModule("/测试项目/账号管理/创建账号")
	.addContent(JiraFieldIdType.PRECONDITION.getName(), "已在创建账号界面", "除姓名字段外，其他信息均正确填写")
	.addContent(JiraFieldIdType.OBJECTIVE.getName(), "验证创建账号界面各个控件输入是否有效")
	.end();
	
	jira.addTitle("通过不同的身份证创建账号")
	.addStepAndExcept("“身份证”文本框不输入任何信息，#保存#", "#失败#")
	.addStepAndExcept("在“身份证”文本框中只输入空格，#保存#", "#失败#")
	.addStepAndExcept("输入15位的证件信息，#保存#", "#成功#")
	.addStepAndExcept("输入18位的证件信息，#保存#", "#成功#")
	.addStepAndExcept("输入末尾带“X”或“x”的证件信息，#保存#", "#成功#")
	.addStepAndExcept("输入大于18位的数字，#保存#", "#失败#")
	.addStepAndExcept("输入小于18位但大于15位的数字，#保存#", "#失败#")
	.addStepAndExcept("输入小于15位的数字，#保存#", "#失败#")
	.addStepAndExcept("输入不符合证件规则但长度符合规则的数字（如123456789012345678），#保存#", "#失败#")
	.addStepAndExcept("输入非数字字符，#保存#", "#失败#")
	.addModule("/测试项目/账号管理/创建账号")
	.addContent(JiraFieldIdType.PRECONDITION.getName(), "已在创建账号界面", "除身份证字段外，其他信息均正确填写")
	.addContent(JiraFieldIdType.OBJECTIVE.getName(), "验证创建账号界面各个控件输入是否有效")
	.end();
	
	jira.addCase(infoCase.addPhoneCase("手机号码", true, false, false, PhoneType.MOBLE)).end();
}
```
这里使用了一个模板类来生成身份证的测试用例（其实以上的用例都可以用模板来生成），模板的使用可以参考Wiki文档的[《测试用例模板》](https://gitee.com/pyqone/autest/wikis/%E6%B5%8B%E8%AF%95%E7%94%A8%E4%BE%8B%E6%A8%A1%E6%9D%BF?sort_id=3354706)。若完全引用模板，则可将代码写成：

```java
final File CONFIG_FILE = new File("ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
final File CASE_FILE = new File("src/main/java/com/auxiliary/testcase/file/测试用例.xlsx");
final File CASE_TEMP_CONFIG_FILE = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml");

InformationCase infoCase;
JiraTestCaseWrite jira;

@BeforeClass
public void initData() throws DocumentException {
	jira = new JiraTestCaseWrite(CONFIG_FILE, CASE_FILE);
	infoCase = new InformationCase(CASE_TEMP_CONFIG_FILE);
	
	infoCase.setReplaceWord(InformationCase.ADD_INFORMATION, "账号");
	infoCase.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
}

@AfterClass
public void openFolder() throws IOException {
	jira.writeFile();
}

@Test
public void addCase() {
	jira.addCase(infoCase.addBasicTextboxCase("姓名", true, false, false)).end();
	jira.addCase(infoCase.addIdCardCase("身份证号码", true, false, false)).end();
	jira.addCase(infoCase.addPhoneCase("手机号码", true, false, false, PhoneType.MOBLE)).end();
}
```

### 2 Web-UI自动化测试工具
在自动化脚本编写的实践过程中，我发现selenium有以下的问题：
1. WebDriver类构造比较麻烦，特别是指向浏览器驱动的代码，不容易记住，每次编写脚本时都需要去复制之前的代码
2. 浏览器配置不方便，需要记住浏览器配置相关的代码，该代码没有智能提示
3. 元素定位方式无法专门维护（selenium提供了一种维护元素定位方式的代码，也是现在流行一时的PO模式，但该模式我并未对其深究，因为我仍发现该模式在调用时仍不方便，后面我会提到）
4. 遇到存在iframe标签的元素时，需要不断地切换窗体，在切换频繁时容易出错
5. 进行元素操作时会因为各种各样的问题而报错，由于元素操作是独立的，所以需要为每一个元素操作都做一次判断，导致代码冗余
6. 等待机制使用便捷，同样需要进行封装
7. 元素操作分布在各个类中，不统一，导致同一个操作可能出现不同的代码，例如点击元素

以上我仅仅也只是大致总结了一些使用selenium代码时常见的问题，但事实上，在编写的过程还存在各式各样的小问题。针对这些不便的地方，结合平时的脚本事件，我便对selenium代码进行了一个二次封装，把经常出现，但又不得不写的代码进行了封装，做成单一的方法进行调用，以提高脚本编写的速度。代码详细的使用方法，我会在Wiki的“Web UI自动化测试”文件夹中放入详细说明，这里简单说明一下工具的使用步骤。

#### 2.1 脚本编写需求
指定待测页面为Selenium API网站：<br>
[<center>https://seleniumhq.github.io/selenium/docs/api/java/index.html</center>](https://seleniumhq.github.io/selenium/docs/api/java/index.html)

之后我们按照以下步骤进行操作：
1. 点击页面左上角的“FRAMES”
2. 点击“Packages”窗口中的“org.openqa.selenium.chrome”
3. 点击下方窗口的“ChromeDriver”
4. 获取类中所有的构造方法（从“Constructor Summary”表格中获取）
![步骤截图](https://images.gitee.com/uploads/images/2021/0106/195720_fd44ce3a_1776234.png "步骤截图")
**注意：** 除初次进入页面点击的“FRAMES”控件外，其他的控件均在一个iframe下。

#### 2.2 录制元素
采用xml文件的形式记录所需要操作的元素，内容为：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="">
	<templet>
		<xpath id='元素'>//*[text()='${text}']</xpath>
		<xpath id='窗体'>//iframe[@name='${name}']</xpath>
	</templet>

	<element name='展开窗口'>
		<xpath text='Frames' temp_id='元素' />
	</element>

	<iframe name='packageListFrame'>
		<xpath temp_id='窗体' />

		<element name='包名'>
			<xpath text='org.openqa.selenium.chrome' temp_id='元素' />
		</element>
	</iframe>

	<iframe name='packageFrame'>
		<xpath temp_id='窗体' />

		<element name='类名'>
			<xpath text='ChromeDriver' temp_id='元素' />
		</element>
	</iframe>

	<iframe name='classFrame'>
		<xpath temp_id='窗体' />

		<element name='构造方法列表'>
			<xpath>//tr//th[@class='colConstructorName']/code</xpath>
		</element>
	</iframe>
</project>
```

#### 2.3 启动浏览器并加载页面
要进行页面的自动化，首先需要启动浏览器并加载待测页面。以谷歌浏览器为例，谷歌浏览器驱动定义在项目路径根目录下，启动浏览器，并加载Selenium API页面的代码可写为：
```java
//指向谷歌驱动位置
File driverFile = new File("chromedriver.exe");
//待测页面名称
String pageName = "selenium";
//待测站点
String url = "https://seleniumhq.github.io/selenium/docs/api/java/index.html";
//构造浏览器对象
ChromeBrower chrome = new ChromeBrower(driverFile, url, pageName);
//设置浏览器全屏
chrome.addConfig(ChromeOptionType.SET_WINDOW_MAX_SIZE);
//启动浏览器（可返回WebDriver对象）
chrome.getDriver();
//关闭浏览器
chrome.closeBrower();
```

#### 2.4 操作元素
获取到浏览器对象后，首先构造元素查找类对象，再设置需要读取的元素定位方式存储文件（这里我将xml文件放在项目路径下）：
```java
//用于查找普通元素
FindCommonElement common = new FindCommonElement(chrome);
//用于查找列表型元素
FindDataListElement data = new FindDataListElement(chrome);

//定义查找元素时读取的元素定位方式存储文件
File LOCATION_FILE = new File ("api页面元素.xml");
common.setReadMode(new XmlLocation(LOCATION_FILE), true);
```
**注意：** 为简化元素查找类切换xml的过程，我将指向的定位文件设置为了静态，即以上代码中，只需要对common对象进行切换，data对象共享common对象切换的元素定位方式存储文件，此处在多线程和频繁切换时可能会出现问题。

获得元素查找对象后，再构造事件类对象，这里使用到点击事件类以及列表事件类，代码如下：
```java
//指向点击事件
ClickEvent click = new ClickEvent(chrome);
//指向扩展事件中的列表型事件
DataTableEvent table = new DataTableEvent(chrome);
```
最后便可操作元素：
```java
//点击元素
click.click(common.getElement("展开窗口"));
click.click(common.getElement("包名"));
click.click(common.getElement("类名"));
//获取构造方法
table.addList(data.find("构造方法列表"));
//获取列表的所有元素文本，并进行输出
table.getListText("构造方法列表").stream().map(text -> text.orElse("空值")).forEach(System.out::println);
```

#### 2.5 Demo
根据以上的说明，将代码放入TestNG框架中，完整的代码为：
```java
/**
 * 指向谷歌驱动位置
 */
private final File DRIVER_FILE = new File("chromedriver.exe");
/**
 * 待测站点
 */
private final String URL = "https://seleniumhq.github.io/selenium/docs/api/java/index.html";
/**
 * 待测页面名称
 */
private final String PAGE_NAME = "selenium";
/**
 * 指向元素定位方式存储文件
 */
private final File LOCATION_FILE = new File ("src/main/java/pres/auxiliary/selenium/brower/api页面元素.xml");

/**
 * 指向浏览器
 */
private ChromeBrower chrome;

/**
 * 初始化浏览器
 */
@BeforeClass
public void initBrower() {
	//构造浏览器对象
	chrome = new ChromeBrower(DRIVER_FILE, URL, PAGE_NAME);
	//设置浏览器全屏
	chrome.addConfig(ChromeOptionType.SET_WINDOW_MAX_SIZE);
}

/**
 * 关闭浏览器
 */
@AfterClass
public void closeBrower() {
	//关闭浏览器
	chrome.closeBrower();
}

/**
 * 对元素进行操作
 */
@Test
public void oprateElement() {
	//用于查找普通元素
	FindCommonElement common = new FindCommonElement(chrome);
	//用于查找列表型元素
	FindDataListElement data = new FindDataListElement(chrome);
	
	//指向点击事件
	ClickEvent click = new ClickEvent(chrome);
	//指向扩展事件中的列表型事件
	DataTableEvent table = new DataTableEvent(chrome);
	
	//定义查找元素时读取的元素定位方式存储文件
	common.setReadMode(new XmlLocation(LOCATION_FILE), true);
	
	//点击元素
	click.click(common.getElement("展开窗口"));
	click.click(common.getElement("包名"));
	click.click(common.getElement("类名"));
	//获取构造方法
	table.addList(data.find("构造方法列表"));
	//获取列表的所有元素文本，并进行输出
	table.getListText("构造方法列表").stream().map(text -> text.orElse("空值")).forEach(System.out::println);
}
```
