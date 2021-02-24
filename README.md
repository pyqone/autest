# autest
## 简介
autest为Auxiliary Test的英文缩写意为辅助测试，其中包括日常测试工作中能用到的工具，包括测试用例编写工具，简化Web UI自动化测试工具以及测试报告生成工具和日常工作中使用的小工具等。开发这个项目的目的在于使用简单的代码来简化我们测试日常中较为繁杂的操作，使测试的效率得到一定的提升。

目前工具已打包到maven中央仓库，可按以下代码添加依赖：
```xml
<dependency>
    <groupId>com.gitee.pyqone</groupId>
    <artifactId>autest</artifactId>
    <version>${NEW_VERSION}</version>
</dependency>
```

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
* 定姓名可随意输入，不设限制，必填，可重复
* 身份证为中国公民身份证的校验规则，非必填，不可重复
* 手机号码为一般11位号码，必填，不可重复
* 表单提交需要点击保存按钮
* 创建的信息为“账号”。

原型图如下：

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
	.addStepAndExcept("“身份证”文本框不输入任何信息，#保存#", "#成功#")
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
	jira.addCase(infoCase.addBasicTextboxCase("姓名", true, true, false)).end();
	jira.addCase(infoCase.addIdCardCase("身份证号码", false, false, false)).end();
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

以上我仅仅也只是大致总结了一些使用selenium代码时常见的问题，但事实上，在编写的过程还存在各式各样的小问题。针对这些不便的地方，结合平时的脚本事件，我便对selenium代码进行了一个二次封装，把经常出现，但又不得不写的代码进行了封装，做成单一的方法进行调用，以提高脚本编写的速度。

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
将上述代码写到xml文件中，存储到项目根路径下，命名为“api页面元素.xml”。

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
获取到浏览器对象后，首先构造元素查找类对象，再设置需要读取的元素定位方式存储文件：
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
private final File LOCATION_FILE = new File ("api页面元素.xml");

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
	table.getListText("构造方法列表").stream()
	        .map(text -> text.orElse("空值"))
	        .forEach(System.out::println);
}
```

### 3 数据驱动工具
数据驱动在自动化测试过程中使用得还是比较广泛的，无论是在UI自动化还是接口自动化，都有数据驱动的身影。由于我写自动化测试脚本使用的是TestNG框架，所以目前我只针对TestNG框架的数据驱动进行了封装。

#### 3.1 数据准备
数据驱动中的数据可存放在文本文件（doc/docx/txt格式文件）、excel文件（xls/xlsx格式文件）或csv文件（csv格式文件）中，当然，若您不使用默认的读取方式，亦可自行编写读取词语的方法，只需要将读取到的词语转换为[TableData](https://apidoc.gitee.com/pyqone/autest/com/auxiliary/tool/data/TableData.html)类即可。这里我将数据存储至excel文件中，文件命名为“数据驱动.xlsx”，文件放置在项目根路径下，文件内容为：

姓名 | 手机号码 | 更改时间 | 
---|---|---
${rs(CH,2, 3)} | 13000000000 | ${time()}
${rs(CH,2, 3)} | 13000000001 | ${time(-1d)}
${rs(CH,2, 3)} | 13000000002 | ${time(-2d)}
其中使用“${}”括起来的内容表示使用公式，该公式可自定义，具体可以参考Wiki中对数据驱动公式的解释。

#### 3.2 构造数据并使用
定义数据驱动文件后，便可结合TestNG框架，在@DataProvider中对数据驱动进行构造：
```java
@DataProvider(name = "data")
public Object[][] initDataDriver() {
	//构造对象并指向数据驱动文件
	TestNGDataDriver dataDriver = new TestNGDataDriver();
	dataDriver.addDataDriver(DATA_DRIVER_FILE, "数据驱动", true);
	
	//加载公式
	dataDriver.addFunction(Functions.randomString());
	dataDriver.addFunction(Functions.getNowTime());
	
	//返回TestNG框架识别的数据驱动
	return dataDriver.getDataDriver();
}
```
将数据驱动写入到框架中后，我们在测试方法传参中只需要写入一个参数，便可直接使用数据：
```java
@Test(dataProvider = "data")
public void outPutData(Data data) {
	System.out.println("第" + count++ + "次执行输出结果：");
	System.out.println("姓名：" + data.getString("姓名"));
	System.out.println("手机号码：" + data.getString("手机号码"));
	System.out.println("更改时间：" + data.getString("更改时间"));
	System.out.println("===========================");
}
```
在getString()方法中，其传参为我们写入到文件中的列表名称，其姓名和更改时间根据加载的公式转译后，得到随机的中文和按照当前时间计算的日期，故每次运行结果会不一样。在某次运行中得到以下结果：
```
第1次执行输出结果：
姓名：膜柔凉
手机号码：13000000000
更改时间：2021-01-08 08:37:25
===========================
第2次执行输出结果：
姓名：衣字冒
手机号码：13000000001
更改时间：2021-01-07 08:37:25
===========================
第3次执行输出结果：
姓名：靠己苦
手机号码：13000000002
更改时间：2021-01-06 08:37:25
===========================
```

#### 3.3 Demo
根据以上的说明，将代码放入TestNG框架中，完整的代码为：
```java
/**
 * 指向数据驱动文件
 */
private final File DATA_DRIVER_FILE = new File("数据驱动文件.xlsx");
private int count = 1;

/**
 * 加载数据驱动
 */
@DataProvider(name = "data")
public Object[][] initDataDriver() {
	//构造对象并指向数据驱动文件
	TestNGDataDriver dataDriver = new TestNGDataDriver();
	dataDriver.addDataDriver(DATA_DRIVER_FILE, "数据驱动", true);
	
	//加载公式
	dataDriver.addFunction(Functions.randomString());
	dataDriver.addFunction(Functions.getNowTime());
	
	//返回TestNG框架识别的数据驱动
	return dataDriver.getDataDriver();
}

/**
 * 输出数据驱动
 * @param data 数据内容
 */
@Test(dataProvider = "data")
public void outPutData(Data data) {
	System.out.println("第" + count++ + "次执行输出结果：");
	System.out.println("姓名：" + data.getString("姓名"));
	System.out.println("手机号码：" + data.getString("手机号码"));
	System.out.println("更改时间：" + data.getString("更改时间"));
	System.out.println("===========================");
}
```

### 4 接口工具
由于我对接口测试并不熟悉，所以在接口工具上，目前只是简单地对HttpClient进行了一个二次封装，简化了部分发送请求的代码，封装完后才发现自己封装的在功能上和“OkHttp”差不多。当然，后期我会根据工作中实际使用接口的情况，继续对工具进行封装，使工具更符合测试人员的使用习惯。

在请求接口方面，我将接口地址的传入做了一个简单的拆分，即分成协议、主机、端口、地址和参数等，这样做一来是为了方便整合数据驱动，二来也为了使代码更容易理解，

#### 4.1 接口定义
由于我未找到一个合适的接口可以测试，这里就假定在我本地定义了一个接口。

##### 4.1.1 接口说明
项目 | 值
---|---
接口地址 | /find/findpreson
接口标识 | 
请求方式 | POST
返回格式 | JSON
业务描述 | 查询工程下的人员姓名

##### 4.1.2 请求参数
字段名 | 类型 | 是否为空 | 字段说明 | 备注
---|---|---|---|---
projectId | String | N | 工程ID 
presonId | String | N | 人员ID 

##### 4.1.3 响应参数
字段名 | 类型 | 是否为空 | 字段说明 | 备注
---|---|---|---|---
presonName | String | N | 人员姓名

#### 4.2 Demo
根据以上定义的接口，假设请求体为：
```json
{
    "projectId":"000001", 
    "presonId":"100001"
}
```
返回的人员姓名为：张三。结合TestNG框架，则完整的代码为：
```java
/**
 * 用于发送请求
 */
EasyHttp http;

@BeforeClass
public void initData() {
	http = new EasyHttp();
}

@Test
public void sandRequest() throws ClientProtocolException, URISyntaxException, IOException {
	//定义接口参数
	http.agreement("http://")
		.host("127.0.0.1")
		.port(9000)
		.address("/find/findpreson")
		.requestType(RequestType.POST)
		.putHead(HeadType.CONTENT_TYPE_JSON)
		.encoding("UTF-8");
	
	//定义请求体
	JSONObject json = new JSONObject();
	json.put("projectId", "000001");
	json.put("presonId", "100001");
	
	//写入请求体
	http.body(json.toJSONString());
	
	//以格式化的形式输出接口返回值
	System.out.println(http.response().getFormatResponseText());
}
```
以上代码输出结果为：
```json
{
    "presonName":"张三"
}
```
当然，以上定义接口的代码也可以直接写成：
```java
http.url("http://127.0.0.1:9000/find/findpreson")
			.requestType(RequestType.POST)
			.putHead(HeadType.CONTENT_TYPE_JSON)
			.encoding("UTF-8");
```
具体用法可参考Wiki对接口工具的介绍。

### 5 数据库工具
同接口一样，我对数据的操作也涉猎不深，目前数据库工具只对jdbc执行SQL这一块进行了封装，简化了执行SQL的代码以及连接数据库的代码。

#### 5.1 数据库定义
数据库也不好做演示，就假设在本地存在一个oracle数据库，连接信息如下：
```
用户名：test
密码：Test123456
主机：127.0.0.1
端口：1521
数据库名称：TEST_DB
```
假设库中存在一张名叫“TEST_PROJECT2USER”的表，表中有如下内容：

ID | PROJECT_ID | USER_ID | USER_NAME
---|---|---|---
1 | 000001 | 100001 | 张三
2 | 000002 | 100002 | 李四
3 | 000003 | 100003 | 王五
4 | 000004 | 100004 | 赵六

#### 5.2 Demo
查询以上表中的所有信息，则可使用如下SQL：
```sql
SELECT * FROM TEST_PROJECT2USER
```
结合TestNG框架，则完整的代码如下：
```java
/**
 * 需要执行SQL语句
 */
private final String SQL_TEXT = "SELECT * FROM TEST_PROJECT2USER";
/**
 * 用于查询数据库
 */
SqlAction action;

@BeforeClass
public void initData() {
	//连接信息
	String username = "test";
	String password = "Test123456";
	String host = "127.0.0.1:1521";
	String dataBaseName = "TEST_DB";
	
	//定义执行类
	action = new SqlAction(DataBaseType.ORACLE, username, password, host, dataBaseName);
}

@Test
public void action() {
	//执行并获取所有数据
	TableData<String> table = action.run(SQL_TEXT)
			.getResult(1, -1, action.getColumnNames().toArray(new String[] {}));
	//输出结果到控制台
	table.rowStream().forEach(System.out::println);
}
```
以上代码输出结果为：
```
["1", "000001", "100001", "张三"]
["2", "000002", "100002", "李四"]
["3", "000003", "100003", "王五"]
["4", "000004", "100004", "赵六"]
```
在代码中，使用到了表数据类TableData，该类可用于存储同数据类型的表数据，具体用法可在Wiki中进行查找。

### 6 其他工具
除以上介绍到的主要工具外，在项目中还定义了一些小工具，这些工具比较简单，但也经常使用，故将这一部分的代码也进行了封装，使其与上面介绍的大工具联系更紧密。

这里对这些工具进行简单的介绍，具体的用法可参考Wiki的介绍。
* 日期加减工具（Time）：可根据日期单位，对设定的时间进行增减操作，并按照指定的格式进行输出。
* 随机字符串工具（RandomString）：用于根据指定的字符串池，随机从字符串池中抽取字符串，拼接指定长度的随机字符串并返回的工具
* 常用随机内容返回工具（PresetString）：用于返回常用的，需要随机返回的词语。包括随机姓名、随机手机号码和随机身份证号等。
* 随机词语返回工具（RandomWord）：类似与随机字符串，根据预设的词语池，从池中随机抽取指定数量词语的工具。
* 集合处理工具（ListUtil）：用于对List集合类型结合的元素进行简单的处理的工具。包括集合数据处理、Map转List集合、集合转置等
* 表型数据存储工具（TableData）：为简化表的存储而定义的一个工具，并提供多种返回数据的方式。目前已在涉及到与表相关的工具中，都改为由该工具代替，以便于更好的维护代码。
* 表类型数据读取工具（TableFileReadUtil）：用于读取表类型的文件数据，支持文本文件（doc/docx/txt格式文件）、excel文件（xls/xlsx格式文件）或csv文件（csv格式文件）的读取。

## 后记
作为一个没有系统学习过开发的我而言，能力实在有限，代码质量也不高，对于设计模式也是一知半解的，工具中很多的地方都是只追求实现。但有件事情希望在使用的您能明白：作为测试工程师，我们的代码不用上服务器运行、不用考虑多线程（大多数情况下）、不用考虑那0.01秒的响应。对于软件测试而言，最重要的是得到一个结果，即便代码的效率再不堪，也比我们人工测试来得快。我们追求代码应该是使用简单，快速得到数据，能简化测试工作就好，而不是一味地追求与开发工程师一样，使用各式各样的框架、设计模式，这样的代码首先使用就不方便，其次就是大多数人都看不懂，这就失去意义了。

当然，以上只是我个人的感觉，若与您的想法相未被，还请您见谅。若您在使用上有什么的建议，或在代码上有什么优化的方法，还请您不吝教授于我，我将万分感谢您的批评。若您也有兴趣维护该项目，可拉取分支进行开发，当然，提交时还望您完整写好注释，以便于使用和生成文档。若您在工作上使用后有其他的功能需要封装，可与我联系，将需求告知于我，我会抽时间出来开发您需要的功能。我的邮箱是：465615774@qq.com。