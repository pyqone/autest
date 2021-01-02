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
|auxiliary.tool|包含日常测试工作中可以使用的测试工具，例如表格处理工具、文本处理工具、日期处理工具等|
|auxiliary.work.selenium|对Web UI自动化工具selenium的二次封装，简化了selenium中比较复杂的代码|
|auxiliary.work.testcase|用于编写测试用例的工具|
|auxiliary.work.http|用于做接口测试时使用的工具|
|auxiliary.work.sql|用于对oralc数据库简单查询的工具|

## 工具介绍
目前，整个项目比较成熟的工具是测试用例编写工具和Web UI自动化辅助编码工具，下面我将主要介绍这两个工具的使用。

### 1 测试用例编写工具
该工具是通过预先写好的测试用例文件模板，调用其中添加内容的方法，对测试用例进行编写，之后再生成一个Excel文件，以方便测试用例阅读与上传。当然，看到这许多人就有疑问了，既然最后要生成一个Excel文件，那编写测试用例直接在Excel文档里写就好，何必还要编写代码，然后再生成呢？的确，在office的Excel软件中，其可视化界面确实要比写代码要强很多，但Excel软件也存在上下滚动不方便的缺点，并且，大家也清楚，很多测试用例都可以复用，在编写过程中难免会有大量的复制和替换的工作，对于少量的用例还好，一旦用例较多时，复制用例后，就容易遗漏需要替换文本的用例，或者多复制用例，导致编写出错。为解决这一类的问题，所以我封装了一个测试用例编写工具，将测试用例的编写工作，由Excel向eclipse（不要问我为什么不用IDEA，我是特别不喜欢IDEA）转移，当然，缺点就是可视化差了一些。

#### 1.1 测试用例文件创建
##### 1.1.1 测试用例模板配置文件创建
在编写测试用例时，我们需要有一个存放测试用例的文件，之后在文件中编写测试用例，程序中也不例外，首先我们需要创建测试用例模板文件，该模板文件可以是自行创建，也可以是根据配置文件中编写的内容，通过程序进行创建，个人建议选择后者，因为我们创建的配置文件模板是符合我们在类中定义的字段位置的，若是通过自行创建的模板，此时可能遗漏字段或字段位置有误，导致程序失效。下面将介绍如何使用配置文件来创建测试用例模板。

首先，配置文件采用xml的文件形式，其内容主要是对测试用例模板文件中一些基本的参数进行配置，例如行宽、字段名称、是否居中等等，其结构如下：
```xml
<?xml version='1.0' encoding='UTF-8'?>
<templet>
	<sheet name='测试用例' freeze='2'>
		<column id='标题' name='Name' wide='30.88' align='left'/>
		<column id='步骤' name='Test Script (Step-by-Step) - Step' wide='45.75' align='left' row_text='1' index='true'/>

		<datas id='优先级'>
			<data name='High' />
			<data name='Normal' />
			<data name='Low' />
		</datas>
		
		<datas id='项目'>
			<file path='JiraCaseFileTemplet/Jira数据有效性.xlsx' regex='Sheet1' column='0' start_row='1' />
		</datas>
	</sheet>
</templet>
```
配置文件中的标签解释如下：
- templet是根标签，其不包含属性，可包含多个sheet标签
- sheet标签表示其下的字段标签或数据有效性标签写入的工作簿位置，该标签下可有多个column和datas标签，其具有以下属性：

|     属性      |  介绍     |
|-------------|------------|
|name|工作簿的名称，该属性必须存在|
|freeze|需要冻结的列数，值为2表示冻结前两列，若不写，则表示不冻结|
- column标签表示需要生成到文件中的字段，其具有以下属性：

|     属性      |  介绍     |
|-------------|------------|
|id |程序中的标识符，在程序中填写内容时使用该字段作为传参，该属性必须存在|
|name|在测试用例模板中的字段名称，该名称不能作为程序中的传参，该属性必须存在|
|wide|字段在模板文件中所占的宽度，该属性必须存在|
|align|字段内容在模板文件中的对其方式，有三个可选值：left（左对齐）、center(居中对齐)、right（右对齐），该属性必须存在|
|row_text|字段每段内容在文件中所占的行数，值为1表示每段内容在文件中占1个单元格（一段一分行），若无该属性，则表示所有内容只写在一个单元格中|
|index|字段每段内容是否自动加上编号，有两个可选值：true（创建编号）、false（不创建编号），若无该属性，则默认为false|
- datas标签表示写在sheet标签中的数据有效性内容，可包含多个data和file标签，其具有以下属性：

|     属性      |  介绍     |
|-------------|------------|
|id|程序中的标识符，在程序中填写内容时使用该字段作为传参，该属性必须存在|
- data标签表示数据有效性的内容，是直接写在xml文件中的数据有效，具有以下属性：

|     属性      |  介绍     |
|-------------|------------|
|name|数据有效性的内容，该属性必须存在|
- file标签表示数据有效性的内容，该标签表示需要读取写在其他文件中的数据有效性，可与data标签同时存在，不会冲突，具有以下属性：

|     属性      |  介绍     |
|-------------|------------|
|path|文件所在路径，支持word文件、excel文件、txt文件以及csv文件，该属性必须存在|
|regex|内容切分规则，若为excel文件则需要写入需要读取sheet名称，若为文本文件，则传入切分文件的规则，对csv文件则无效，该属性必须存在|
|column|需要读取的列，该属性必须存在|
|start_row|需要读取的起始行，该属性必须存在|
|end_row|需要读取的结束行，该属性必须存在|

***注意：file标签所有属性完全参考 pres.auxiliary.work.selenium.datadriven.ListFileRead 类的内容，支持的文件及参数介绍可查询该类的api***

##### 1.1.2 通过配置文件并创建测试用例模板文件
当我们编辑完配置文件后，调用CreateCaseFile中的create()方法，即可完成测试用例模板文件的创建，具体方法如下（假设xml文件放在ConfigurationFiles/jira测试用例导入模板.xml路径下）：

```java
@Test
public void createCaseTemplate() {
    //模板文件类对象，指向模板文件生成的路径
    File tempFile = new File("Result/测试用例.xlsx");
    //配置文件类对象，指向xml配置文件的存放路径
    File conFile = new File("ConfigurationFiles/jira测试用例导入模板.xml");
    
    //构造对象
    CreateCaseFile ccf = new CreateCaseFile(conFile, tempFile);
    //创建文件
    ccf.create();
}
```
在类中，有提供一种文件的保护机制，在多次生成同一个文件时，是否允许直接覆盖文件，默认是不允许直接覆盖文件，及多次创建文件名相同的文件时，程序将抛出异常，从而保护之前已创建的文件，如有需要，可通过方法将其设置为允许覆盖。例如以下两个测试方法：
```java
@Test
public void createCaseTemplate() {
    //模板文件类对象，指向模板文件生成的路径
    File tempFile = new File("Result/测试用例.xlsx");
    //配置文件类对象，指向xml配置文件的存放路径
    File conFile = new File("ConfigurationFiles/jira测试用例导入模板.xml");
    
    //构造对象
    CreateCaseFile ccf = new CreateCaseFile(conFile, tempFile);
    
    //设置允许覆盖文件
	temp.setCoverFile(true);
	temp.create();
	temp.create();
}

@Test(expectedExceptions = IncorrectFileException.class)//捕捉IncorrectFileException异常
public void createCaseTemplate() {
    //模板文件类对象，指向模板文件生成的路径
    File tempFile = new File("Result/测试用例.xlsx");
    //配置文件类对象，指向xml配置文件的存放路径
    File conFile = new File("ConfigurationFiles/jira测试用例导入模板.xml");
    
    //构造对象
    CreateCaseFile ccf = new CreateCaseFile(conFile, tempFile);
    
    //设置不允许覆盖
	temp.setCoverFile(false);
	temp.create();
	temp.create();//此时再次调用 创建方法时将抛出IncorrectFileException异常
}
```
到此，我们查看生成测试用例文件的路径中，已经存在我们测试用例模板文件，可继续进行下一步操作。
#### 1.2 测试用例编写
##### 1.2.1 构造用例编写类对象
首先通过以下xml文件来生成一个测试用例文件模板：
```xml
<?xml version='1.0' encoding='UTF-8'?>
<templet>
	<sheet name='测试用例' freeze='2'>
		<column id='标题' name='Name' wide='30.88' align='left'/>
		<column id='目的' name='Objective' wide='18.25' align='left'/>
		<column id='前置条件' name='Precondition' wide='18.25' align='left' index='true'/>
		<column id='步骤' name='Test Script (Step-by-Step) - Step' wide='45.75' align='left' row_text='1' index='true'/>
		<column id='预期' name='Test Script (Step-by-Step) - Expected Result' wide='45.75' align='left' row_text='1' index='true'/>
		<column id='模块' name='Folder' wide='22.00' align='center'/>
		<column id='状态' name='Status' wide='10.00' align='center'/>
		<column id='优先级' name='Priority' wide='10.00' align='center'/>
		<column id='项目' name='Component' wide='10.00' align='center'/>
		<column id='设计者' name='Owner' wide='10.00' align='center'/>
		<column id='关键用例' name='关键用例' wide='10.00' align='center'/>
		<column id='关联需求' name='Coverage (Issues)' wide='20.00' align='center'/>
	
		<datas id='状态'>
			<data name='Approved' />
			<data name='Draft' />
			<data name='Deprecated' />
		</datas>
		
		<datas id='优先级'>
			<data name='High' />
			<data name='Normal' />
			<data name='Low' />
		</datas>
		<datas id='关键用例'>
			<data name='是' />
			<data name='否' />
		</datas
    <sheet/>
</templet>
```
得到测试用例模板文件后，我们则需要通过用例模板文件对象和创建模板使用的xml文件对象来构造编写测试用例的类对象，这里我们需要调用包中的BasicTestCaseWrite类，框架选用TestNG：
```java
/**
 * 用例编写类
 */
BasicTestCaseWrite wtc;
/**
 * 配置文件类对象
 */
File conFile = new File("ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
/**
 * 模板文件类对象
 */
File tempFile = new File("Result/测试用例.xlsx");

@BeforeClass
public void createTemplet() throws DocumentException, IOException {
	CreateCaseFile temp = new CreateCaseFile(conFile, tempFile);
	temp.setCoverFile(true);
	temp.create();

	wtc = new BasicTestCaseWrite(conFile, tempFile);
}

@AfterClass
public void openFolder() throws IOException {
	wtc.writeFile();
}
```

##### 1.2.2 添加测试用例
得到了测试用例类对象后，我们则需要对需求中的内容设计测试用例。假设有一个需求，需要对创建账号界面进行测试，界面上有一个姓名文本框（非必填，无输入限制）和一个身份证文本框（必填，校验是否为正确的身份证号），那么，我们需要在测试用例中添加以下用例（简单编写）：
* 对姓名文本框的测试用例：

```
标题:
    通过不同的姓名创建账号
目的:
    验证创建账号界面各个控件输入是否有效
前置条件:
    1.已在创建账号界面
    2.除姓名字段外，其他信息均正确填写
步骤:
    1.“姓名”文本框不输入任何信息，点击“保存”按钮
    2.在“姓名”文本框中只输入空格，点击“保存”按钮
    3.在“姓名”文本框中输入HTML代码，点击“保存”按钮
预期:
    1.账号创建成功
    2.账号创建成功
    3.账号创建成功，且HTML代码不会被转义
模块:
    /测试项目/账号管理/创建账号
状态:
    Approved
优先级:
    Normal
项目:
    
设计者:
    test
关键用例:
    否
关联需求:
    TEST-1
```
* 对身份证文本框的测试用例：

```
标题:
    通过不同的身份证创建账号
目的:
    验证创建账号界面各个控件输入是否有效
前置条件:
    1.已在创建账号界面
    2.除身份证字段外，其他信息均正确填写
步骤:
    1.“身份证”文本框不输入任何信息，点击“保存”按钮
    2.在“身份证”文本框中只输入空格，点击“保存”按钮
    3.输入15位的证件信息，点击“保存”按钮
    4.输入18位的证件信息，点击“保存”按钮
    5.输入末尾带“X”或“x”的证件信息，点击“保存”按钮
    6.输入大于18位的数字，点击“保存”按钮
    7.输入小于18位但大于15位的数字，点击“保存”按钮
    8.输入小于15位的数字，点击“保存”按钮
    9.输入不符合证件规则但长度符合规则的数字（如123456789012345678），点击“保存”按钮
    10.输入非数字字符，点击“保存”按钮
预期:
    1.账号创建失败，并给出相应的提示
    2.账号创建失败，并给出相应的提示
    3.账号创建成功
    4.账号创建成功
    5.账号创建成功
    6.账号创建失败，并给出相应的提示
    7.账号创建失败，并给出相应的提示
    8.账号创建失败，并给出相应的提示
    9.账号创建失败，并给出相应的提示
    10.账号创建失败，并给出相应的提示
模块:
    /测试项目/账号管理/创建账号
状态:
    Approved
优先级:
    Normal
项目:
    
设计者:
    test
关键用例:
    否
关联需求:
    TEST-1
```
设计好测试用例后，我们则需要将相应的字段，以及内容写入到代码中，此时，我们调用类中的
```java
addContent(String field, String... contents)
```
方法即可，方法第一个参数为xml文件中字段的id属性，contents参数表示需要在字段中填写的内容，该参数为可变参数，可传入多个值，每传入一个值，表示一行文本，在系统内部将自动进行换行；若字段值为数据有效性标签的内容，则可直接传入数据数据序号，需要注意的是，数据序号是从1开始。具体代码如下（测试方法上接章节开始时编写的代码，之后的测试均是如此）：
```java
@Test
public void addCase() {
    //添加姓名相关的用例
	wtc.addContent("标题", "通过不同的姓名创建账号")
			.addContent("目的", "验证创建账号界面各个控件输入是否有效")
			.addContent("前置条件", 
					"已在创建账号界面", 
					"除姓名字段外，其他信息均正确填写"
					)
			.addContent("步骤", 
					"“姓名”文本框不输入任何信息，点击“保存”按钮", 
					"在“姓名”文本框中只输入空格，点击“保存”按钮", 
					"在“姓名”文本框中输入HTML代码，点击“保存”按钮"
					)
			.addContent("预期", 
					"账号创建成功", 
					"账号创建成功", 
					"账号创建成功，且HTML代码不会被转义"
					)
			.addContent("模块", "/测试项目/账号管理/创建账号")
			.addContent("状态", "1")
			.addContent("优先级", "2")
			.addContent("项目", "")
			.addContent("设计者", "test")
			.addContent("关键用例", "2")
			.addContent("关联需求", "TEST-1")
			.end();
    
    //添加身份证相关的用例
	wtc.addContent("标题", "通过不同的身份证创建账号")
			.addContent("目的", "验证创建账号界面各个控件输入是否有效")
			.addContent("前置条件", 
					"已在创建账号界面", 
					"除姓身份证段外，其他信息均正确填写"
					)
			.addContent("步骤", 
					"“身份证”文本框不输入任何信息，点击“保存”按钮", 
					"在“身份证”文本框中只输入空格，点击“保存”按钮", 
					"输入15位的证件信息，点击“保存”按钮", 
					"输入18位的证件信息，点击“保存”按钮", 
					"输入末尾带“X”或“x”的证件信息，点击“保存”按钮", 
					"输入大于18位的数字，点击“保存”按钮", 
					"输入小于18位但大于15位的数字，点击“保存”按钮", 
					"输入小于15位的数字，点击“保存”按钮", 
					"输入不符合证件规则但长度符合规则的数字（如123456789012345678），点击“保存”按钮", 
					"输入非数字字符，点击“保存”按钮"
					)
			.addContent("预期", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建成功", 
					"账号创建成功", 
					"账号创建成功", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示"
					)
			.addContent("模块", "/测试项目/账号管理/创建账号")
			.addContent("状态", "1")
			.addContent("优先级", "1")
			.addContent("项目", "")
			.addContent("设计者", "test")
			.addContent("关键用例", "1")
			.addContent("关联需求", "TEST-1")
			.end();
}
```
运行测试方法后，可以生成如下图的效果：


![image](https://github.com/pyqone/autest/blob/master/Readme/Image/TestCase/TestCase1.png)


类中还提供了对字段内容进行删除、替换和插入的方法，用法基本与添加测试用例方法类似，这里就不过多赘述，您可参考工具的文档，选择性使用。


***需要注意的是，在编写完一条测试用例后，一定要在末尾加上end()方法，否则该条测试用例不会被记录。***

##### 1.2.3 添加字段常量
在上述测试用例中，可以很明显地看出，部分字段的内容其实是重复的，但每一条用例都必须要包含，故需要引入常量的概念，通过对字段设置一次内容，使其在每次执行时会自动将字段的内容填写至测试用例中。当然，通过该方法设置的字段值并非真正意义上的常量，其值是可以改变的，可根据需要，实时改变字段相应的值。调用的方法为：

```java
setFieldValue(String field, String content)
```
其方法第一个参数为需要写入的字段名，第二个参数为需要在该字段中写入的内容，需要注意的是，字段内容参数不是可变参数，只能传入一个值，需要自行换行。
于是，在上一节中，我们编写的代码则可以改为：

```java
/**
 * 用例编写类
 */
BasicTestCaseWrite wtc;
/**
 * 配置文件类对象
 */
File conFile = new File(
		"ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
/**
 * 模板文件类对象
 */
File tempFile = new File("Result/测试用例.xlsx");

@BeforeClass
public void createTemplet() throws DocumentException, IOException {
	//创建测试用例模板文件
	CreateCaseFile temp = new CreateCaseFile(conFile, tempFile);
	temp.setCoverFile(true);
	temp.create();

	//构造用例编写类对象
	wtc = new BasicTestCaseWrite(conFile, tempFile);
	
	//添加常量词语
	wtc.setFieldValue("模块", "/测试项目/账号管理/创建账号");
	wtc.setFieldValue("目的", "验证创建账号界面各个控件输入是否有效");
	wtc.setFieldValue("状态", "1");
	wtc.setFieldValue("设计者", "test");
	wtc.setFieldValue("关联需求", "TEST-1");
}

@AfterClass
public void openFolder() throws IOException {
	//将测试用例内容写入到文件中
	wtc.writeFile();
}

@Test
public void addCase() {
	//添加姓名相关的用例
	wtc.addContent("标题", "通过不同的姓名创建账号")
			.addContent("前置条件", 
					"已在创建账号界面", 
					"除姓名字段外，其他信息均正确填写"
					)
			.addContent("步骤", 
					"“姓名”文本框不输入任何信息，点击“保存”按钮", 
					"在“姓名”文本框中只输入空格，点击“保存”按钮", 
					"在“姓名”文本框中输入HTML代码，点击“保存”按钮"
					)
			.addContent("预期", 
					"账号创建成功", 
					"账号创建成功", 
					"账号创建成功，且HTML代码不会被转义"
					)
			.addContent("优先级", "2")
			.addContent("关键用例", "2")
			.end();
			
	//添加身份证相关的用例
	wtc.addContent("标题", "通过不同的身份证创建账号")
			.addContent("前置条件", 
					"已在创建账号界面", 
					"除姓身份证段外，其他信息均正确填写"
					)
			.addContent("步骤", 
					"“身份证”文本框不输入任何信息，点击“保存”按钮", 
					"在“身份证”文本框中只输入空格，点击“保存”按钮", 
					"输入15位的证件信息，点击“保存”按钮", 
					"输入18位的证件信息，点击“保存”按钮", 
					"输入末尾带“X”或“x”的证件信息，点击“保存”按钮", 
					"输入大于18位的数字，点击“保存”按钮", 
					"输入小于18位但大于15位的数字，点击“保存”按钮", 
					"输入小于15位的数字，点击“保存”按钮", 
					"输入不符合证件规则但长度符合规则的数字（如123456789012345678），点击“保存”按钮", 
					"输入非数字字符，点击“保存”按钮"
					)
			.addContent("预期", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建成功", 
					"账号创建成功", 
					"账号创建成功", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示", 
					"账号创建失败，并给出相应的提示"
					)
			.addContent("优先级", "1")
			.addContent("关键用例", "1")
			.end();
}
```
##### 1.2.4 添加词语替换
在上一节中，我们虽然用常量代替了部分内容，但在用例步骤与预期中仍然存在较多的重复内容，这些内容无法用常量代替。为解决这一问题，类中还提供了一个词语替换的方法，先在使用标记前，对需要替换的词语设置好以后，在文本中使用“#内容#”进行标记即可。替换词语的方法为：
```java
setReplactWord(String word, String replactWord)
```
方法中，第一个参数表示需要替换的词语，第二个参数表示替换的内容。在传入需要替换的词语时无需加上两个“#”符号。根据上一节编写的测试方法，加上替换词语的方法后，可以将测试方法改写为如下形式：
```java
@Test
public void addCase() {
	wtc.setFieldValue("界面", "已在创建账号界面");
	wtc.setFieldValue("填写", "其他信息均正确填写");
	wtc.setFieldValue("保存", "点击“保存”按钮");
	wtc.setFieldValue("成功", "账号创建成功");
	wtc.setFieldValue("失败", "账号创建失败，并给出相应的提示");
	
	//添加姓名相关的用例
	wtc.addContent("标题", "通过不同的姓名创建账号")
			.addContent("前置条件", "#界面#", "除姓名字段外，#填写#")
			.addContent("步骤", 
					"“姓名”文本框不输入任何信息，#保存#", 
					"在“姓名”文本框中只输入空格，#保存#", 
					"在“姓名”文本框中输入HTML代码，#保存#"
					)
			.addContent("预期", 
					"#成功#", 
					"#成功#", 
					"#成功#，且HTML代码不会被转义"
					)
			.addContent("优先级", "2")
			.addContent("关键用例", "2")
			.end();

	//添加身份证相关的用例
	wtc.addContent("标题", "通过不同的身份证创建账号")
			.addContent("前置条件", "#界面#", "除姓身份证段外，#填写#")
			.addContent("步骤", 
					"“身份证”文本框不输入任何信息，#保存#", 
					"在“身份证”文本框中只输入空格，#保存#", 
					"输入15位的证件信息，#保存#", 
					"输入18位的证件信息，#保存#", 
					"输入末尾带“X”或“x”的证件信息，#保存#", 
					"输入大于18位的数字，#保存#", 
					"输入小于18位但大于15位的数字，#保存#", 
					"输入小于15位的数字，#保存#", 
					"输入不符合证件规则但长度符合规则的数字（如123456789012345678），#保存#", 
					"输入非数字字符，#保存#"
					)
			.addContent("预期", 
					"#失败#", 
					"#失败#", 
					"#成功#", 
					"#成功#", 
					"#成功#", 
					"#失败#", 
					"#失败#", 
					"#失败#", 
					"#失败#", 
					"#失败#"
					)
			.addContent("优先级", "1")
			.addContent("关键用例", "1")
			.end();
}
```
**需要注意以下几点：**
1. 标记的词语被重复定义替换的内容时，其替换的内容以最后一次修改为准
2. 在文本中，若使用了未被定义的词语，则不会被替换，保留原始内容（包括标记符号）

#### 1.3 测试用例模板
一般情况下，我们编写的测试用例大多都是重复的，根据需求描述中相应的规则，对这一类的测试用例进行一定的修改，一条新的用例就写好了。同样的，在程序中，也同样可以使用模板的方式来添加测试用例。当前工具中已提供了部分测试用例模板，可直接调用使用。当然，工具中提供的测试用例模板是按照我的想法写的，可能不能保证您写的用例也是如此，所以在工具中有提供模板的扩展，可供使用。下面先简单介绍测试用例模板写入到测试用例中的方法。
##### 1.3.1 测试用例模板使用
在工具中，我简单编写了添加信息、浏览列表以及地图操作等相关的测试用例，这里就简单介绍如何使用模板。

要使用测试用例模板，首先需要对用例模板的内容进行存储，同样，存储模板也是用过xml文件的形式。由于模板是已经写好的，内容过长，就不在这里展示，可在项目下的“ConfigurationFiles/CaseConfigurationFile/CaseTemplet”文件夹中找到所有的模板。
在上一节中，我们编写的新增账号时对身份证和姓名的测试用例实际可以使用模板来进行代替，使用的模板就是添加信息模板，通过调用测试用例模板类中的方法，从而达到添加测试用例的目的。

首先，我们需要构造信息编辑用例模板类（InformationCase），并将测试用例模板中的字段与测试用例文件模板的字段进行一个关联，由于测试用例模板配置文件的写法是固定的，所以我们只需要定义一次用例模板字段即可。关联测试用例的方法为：
```java
relevanceCase(String field, String labelType)
```
其第一个参数为在测试用例文件模板xml文件中的字段id，第二个参数为测试用例模板中的标签名称，该名称可以通过LabelType枚举类进行获取，亦可直接填入。为便于今后的扩展，故没有把labelType参数作为枚举，故在获取到枚举值后再调用枚举中的getName()方法进行，返回相应的标签名称。

在得到类对象后，我们还需要对模板中部分内容进行替换，就如同您从其他位置将模板内容复制到excel中，然后逐个替换为所需要的内容一样。替换词语的方法为：
```java
setReplaceWord(String word, String text)
```
用法与用例编写类中的替换词语方法类似，需要替换的词语可以使用类名调用，您可选择性使用并进行替换。建议您在查看用例模板后，尽可能替换相应的内容。

了解上述两个方法后，于是我们需要修改上一节中的前置条件方法：

```java
@BeforeClass
public void createTemplet() throws DocumentException, IOException {
	//创建测试用例模板文件
	CreateCaseFile temp = new CreateCaseFile(conFile, tempFile);
	temp.setCoverFile(true);
	temp.create();

	//构造用例编写类对象
	wtc = new BasicTestCaseWrite(conFile, tempFile);
	
	//添加常量词语
	wtc.setFieldValue("模块", "/测试项目/账号管理/创建账号");
	wtc.setFieldValue("目的", "验证创建账号界面各个控件输入是否有效");
	wtc.setFieldValue("状态", "1");
	wtc.setFieldValue("设计者", "test");
	wtc.setFieldValue("关联需求", "TEST-1");
	
	//添加与测试用例模板的字段关联
	wtc.relevanceCase("步骤", LabelType.STEP.getName());
	wtc.relevanceCase("预期", LabelType.EXCEPT.getName());
	wtc.relevanceCase("前置条件", LabelType.PRECONDITION.getName());
	wtc.relevanceCase("优先级", LabelType.RANK.getName());
	wtc.relevanceCase("标题", LabelType.TITLE.getName());
	
	//构造测试用例模板类对象
	ic = new InformationCase(caseTempFile);
	//添加需要替换的词语
	ic.setReplaceWord(InformationCase.ADD_INFORMATION, "账号");
	ic.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
}
```
当我们完成以上构造后，我们只需要调用方法，写入测试用例模板即可，这里需要使用以下方法，首先测试用例编写类中写入测试用例模板文本的方法：
```java
addCase(Case testCase)
```
传入的参数为继承至Case类的测试用例模板类。在案例中，姓名与身份证分别用到测试用例模板的以下方法：

```java
//添加姓名
addBasicTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear, InputRuleType... inputRuleTypes)
//添加身份证
addIdCardCase(String name, boolean isMust, boolean isRepeat, boolean isClear)
```
在方法中，各个参数表示控件的个性化内容，由于参数过多，就不大篇幅介绍，您可以参考项目的API文档。于是，我们可以将上一节中的测试方法改为：

```java
@Test
public void addCase() {
	wtc.addCase(ic.addBasicTextboxCase("姓名", false, true, false)).end();
	wtc.addCase(ic.addIdCardCase("身份证", true, false, false)).end();
}
```

##### 1.3.2 测试用例模板文件扩展
同配置测试用例文件模板一样，测试用例模板也是通过xml文件进行配置，目的是为了方便后续的字段扩展以及用例的扩展，其xml文件相对与文件模板而言就简单一些，大致结构如下：
```xml
<?xml version='1.0' encoding='UTF-8'?>
<cases>
	<case name=''>
		<steps>
			<step id='' value=''/>
			<step id='' value=''/>
		</steps>
		<excepts>
			<except id='' value=''/>
		</excepts>
		<titles>
			<title id='' value='' />
		</titles>
		<preconditions>
			<precondition id='' value='' />
		</preconditions>
		<ranks>
			<rank id='' value='' />
		</ranks>
		<keys>
			<key id='' value='' />
		</keys>
	</case>
</cases>
```
当然，除了编写配置文件外，我们还需要编写一个类，这样才能实现测试用例的扩展，其测试用例类的编写将在下一节中讲解。先解释下xml配置文件的标签：
- cases是根标签，其不包含属性，可包含多个case标签
- case标签表示一条用例模板的开始，可包含测试用例所需要的字段内容标签，子标签可根据需要进行扩展，但扩展的标签需要按照一定的规则编写，其具有以下属性：

|     属性      |  介绍     |
|-------------|------------|
|name|标记用例在代码中的标识，代码中将根据该字段查找相应的用例模板，该属性必须存在|

--case下其他子标签：用例标签下的子标签为扩展的字段标签，表示用例下的字段的取值可以在其中找到，虽然该标签可以自定义，但还是需要遵循一定的规则：
1. 字段的名称必须是“扩展字段名+"s"”，例如其中的steps、titles等标签
2. 其标签下必须包含与其同名，但不包含s的标签，表示该字段的可选值，例如steps下的step。该标签可以同时存在多个，其应包含以下属性：

|     属性      |  介绍     |
|-------------|------------|
|id|标记该可选值在代码中的标识，在代码中使用其值时通过该属性值获取，该属性必须存在|
|value|表示需要填写至测试用例中的内容，该属性必须存在|

需要注意的是，标签的value属性允许编写可替换的词语，使用“\*{词语}\*”进行标记

例如，以下是一个比较完整的测试用例模板配置文件，截取至已有的文件：

```xml
<?xml version='1.0' encoding='UTF-8'?>
<cases project='BrowseList'>
	<case name='addAppBrowseListCase'>
		<titles>
			<title id='1' value='浏览*{信息}*列表' />
		</titles>
		<keys>
			<key id='1' value='*{信息}*列表,刷新,加载' />
		</keys>
		<ranks>
			<rank id='1' value='1' />
		</ranks>
		<preconditions>
			<precondition id='1' value='列表中有信息存在' />
		</preconditions>
		<steps>
			<step id='1' value='查看*{信息}*列表' />
			<step id='2' value='下拉刷新列表' />
			<step id='3' value='上拉加载列表' />
			<step id='4' value='在无剩余数据时，上拉加载列表' />
			<step id='5' value='加载或刷新列表时点击某一个信息或某一个按钮' />
		</steps>
		<excepts>
			<except id='1' value='能看到当前存在的所有信息，且信息按一定规则排序'/>
			<except id='2' value='列表被刷新，若有新增信息则被显示出来，且原有数据排序不会混乱'/>
			<except id='3' value='能加载剩下的信息'/>
			<except id='4' value='上拉有效，但不加载出数据或数据不会错乱'/>
			<except id='5' value='app不会闪退，且能正常显示'/>
		</excepts>
	</case>
	
	<case name='addWebBrowseListCase'>
		<titles>
			<title id='1' value='浏览*{信息}*列表' />
		</titles>
		<keys>
			<key id='1' value='*{信息}*列表,刷新,加载' />
		</keys>
		<ranks>
			<rank id='1' value='1' />
		</ranks>
		<preconditions>
			<precondition id='1' value='列表中有信息存在' />
		</preconditions>
		<steps>
			<step id='1' value='查看*{信息}*列表' />
			<step id='2' value='翻页查看*{信息}*列表' />
			<step id='3' value='在首页上点击“首页”按钮' />
			<step id='4' value='在首页上点击“上一页”按钮' />
			<step id='5' value='在尾页上点击“尾页”按钮' />
			<step id='6' value='在尾页上点击“下一页”按钮' />
		</steps>
		<excepts>
			<except id='1' value='能看到当前存在的所有信息，且信息按一定规则排序'/>
			<except id='2' value='能看到不同页上的列表信息，且切换页后列表的排序不变'/>
			<except id='3' value='按钮无法点击或点击无效，且当前页列表排序不变'/>
			<except id='4' value='按钮无法点击或点击无效，且当前页列表排序不变'/>
			<except id='5' value='按钮无法点击或点击无效，且当前页列表排序不变'/>
			<except id='6' value='按钮无法点击或点击无效，且当前页列表排序不变'/>
		</excepts>
	</case>
</cases>
```

##### 1.3.3 测试用例模板类代码扩展
由于公司不同或者说测试用例编写风格不同，其测试用例的字段也大相径庭，为避免模板过于死板，所以就没对模板中的字段做过多的指定，也正因如此，才需要编写一个与之匹配的测试用例模板类来支撑测试用例的获取。
首先我们先创建一个类，命名为“MyCacse”，并继承包中的“pres.auxiliary.work.testcase.templet.Case”类，添加两个父类的构造方法后可得到如下'代码：

```java
public class MyCase extends Case {
	public MyCase(File configXmlFile) {
		super(configXmlFile);
	}
}
```

之后便是获取测试用例模板xml文件中的内容，这里将要用到内容获取以及词语替换方法。<br>
**内容获取：** 即从测试用例模板xml文件中获取到相应的文本内容。在父类中提供了<br>

```java
protected String getLabelText(String caseName, String labelName, String id)
```
 
方法，用于获取到在xml文件中指定标签下的内容，其caseName参数与case标签的name属性对应，labelName属性与case标签下的字段标签名称对应（不带“s”的标签），id与相应字段标签中的id属性对应。例如，在上述xml文件中，我们要获取用例名称为"BrowseList"下的steps标签下的第四个子标签的内容：

```java
String text = getLabelText("addAppBrowseListCase", "step", "4");
```

**单词替换：** 在上一节介绍中，有提到关于词语的替换，需要使用“\*{单词}\*”来标记，在调用getLabelText方法后，系统会查找预设替换的词语，若存在与之匹配的词语后，则对文本进行词语替换。设置预设词语则需要使用父类的一个属性“wordMap”，该属性是一个HashMap类，直接调用其put方法，添加需要替换的词语即可。例如，我们需要替换上述例子中的“信息”词语：

```java
wordMap.put("信息", "用户");
```

**文本存储：** 当我们获取到写在xml文件内容并添加了相应需要替换的词语后，我们就需要将用例文本的最终内容写入到类中进行存储，以方便写入到测试用例文件中。存储测试用例文本的方法也写在父类中，可调用以下方法：

```java
protected void addFieldText(String labelName, String text)
```

其中，labelName为在需要写入的类中的字段名称，其必须与写在xml文件中的标签字段名称一致，否则在调用方式时将抛出异常；text即需要写入的内容。使用该方法不仅可以写入在xml文件获取的内容，也可以写入自定义的内容。该方法还有三个重载方法：

```java
protected void addFieldText(String labelName, List<String> texts)
protected void addFieldText(LabelType label, String text)
protected void addFieldText(LabelType label, List<String> texts)
```

对于第一个重载方法，即可根据字段内容，传入一组文本内容。对于第三、四个重载方法，其形参label表示标签枚举，即对于一些常用的标签进行了枚举，方便标签名称与关联字段能统一进行管理，具体枚举的内容可参考代码中的LabelType枚举类。
例如，对于上述xml中，我们需要获取并存储第一条用例的标题时，可以通过以下方法实现：

```java
addFieldText("title", getLabelText("addAppBrowseListCase", "title", "1"));
```

或者

```java
addFieldText(LabelType.TITLE, getLabelText("addAppBrowseListCase", LabelType.TITLE, "1"));
```

通过以上介绍的方法，并对需要获取的测试用例内容自由获取、组合，便可得到一个测试用例模板类。例如，上述的MyCase类，我们可以做如下的改造：

```java
public class MyCase extends Case {
	public MyCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	public Case appBrowseListCase() {
	    //必须调用该方法来清除类中上一次调用存储的内容，否则会重复
		clearFieldText();
		wordMap.put("信息", "用户");
		String caseName = "addAppBrowseListCase";
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		relevanceAddData(caseName, ALL, ALL);
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		return this;
	}
	
	public Case webBrowseListCase() {
		clearFieldText();
		wordMap.put("信息", "用户");
		String caseName = "addWebBrowseListCase";
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		relevanceAddData(caseName, ALL, ALL);
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		return this;
	}
}
```

之后，我们在写入测试用例用例类中，构造该类，并调用其中的方法，即可将模板中的内容写入到测试用例文件中

#### 1.4 测试用例字段标记
在编写用例后，有时我们需要在文件中对用例做一定的标记，目前，在工具中，允许对生成的excel进行注释标记、背景色标记、文本颜色标记以及超链接标记。所有的标记方法都需要在调用end()方法后，即end()方法返回的FieldMark类。类中所有的标记方法均返回类本身，便于链式调用。<br>
**注释标记**<br>
即excel中审阅菜单下的“新建批注”（或“注释”）。该注释允许添加在字段所在的单元格下。其方法为：

```java
public FieldMark fieldComment(String sheetName, String field, String content)
```

其中，sheetName表示当前标记所在的sheet名称，该参数需要与field（字段id）参数对应；content表示需要写入到注释中的内容。<br>
该方法存在一个重载方法：

```java
public FieldMark fieldComment(String field, String content)
```

显然，该方法无需指定sheet的名称，表示在当前指向的sheet名称中标记需要标记的字段，即当前最后一次调用切换sheet方法时所指向的sheet名称。对于其他的标记方法中，也存在该重载形式，此后将不再赘述。<br>
为方便介绍该功能，借用在1.3.1中编写的例子：

```java
@Test
public void addCase() {
	wtc.addCase(ic.addBasicTextboxCase("姓名", false, true, false)).end();
	wtc.addCase(ic.addIdCardCase("身份证", true, false, false)).end();
}
```

假设我们需要对第二段用例上字段id为“步骤”的字段上添加注释，则可将代码改为：

```java
@Test
public void addCase() {
	wtc.addCase(ic.addBasicTextboxCase("姓名", false, true, false)).end();
	wtc.addCase(ic.addIdCardCase("身份证", true, false, false)).end().fieldComment("步骤", "测试注解");
}
```

#### 1.5 测试用例编写类扩展
测试用例编写类是支持扩展的，使用者可根据所在公司的不同规定，自行扩展相应的模板。其用例编写子类需要继承CommonTestCaseWrite类，其泛型参数写子类名称即可。其配置文件可参考1.1节中所介绍的方式进行编写。在工具中，提供了jira上编写测试用例的方法，其配置文件见项目路径下“<a href='https://gitee.com/pyqone/autest/blob/master/ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira%E6%B5%8B%E8%AF%95%E7%94%A8%E4%BE%8B%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xml'>ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml</a>”
##### 1.5.1 jira测试用例编写类的使用
在jira模板中，由于在类中已指定相应的字段，故在编写用例时则无需再次指定相应的字段。以1.2节中的测试用例为例，使用jira模板可以按以下方式编写：

```java
@Test
public void addCase() throws DocumentException {
	JiraTestCaseWrite jira = new JiraTestCaseWrite(configurationFile, templetFile);
	jira.addTitle("通过不同的姓名创建账号")
		.addStep("“姓名”文本框不输入任何信息，点击“保存”按钮", 
				"在“姓名”文本框中只输入空格，点击“保存”按钮", 
				"在“姓名”文本框中输入HTML代码，点击“保存”按钮")
		.addExcept("账号创建成功", 
				"账号创建成功", 
				"账号创建成功，且HTML代码不会被转义")
		.addModule("/测试项目/账号管理/创建账号")
		.addContent(JiraFieldIdType.PRECONDITION.getName(), "已在创建账号界面", "除姓名字段外，其他信息均正确填写")
		.addContent(JiraFieldIdType.OBJECTIVE.getName(), "验证创建账号界面各个控件输入是否有效")
		.addContent(JiraFieldIdType.STATUS.getName(), "1")
		.addContent(JiraFieldIdType.PRIORITY.getName(), "2")
		.addContent(JiraFieldIdType.COMPONENT.getName(), "")
		.addContent(JiraFieldIdType.OWNER.getName(), "test")
		.addContent(JiraFieldIdType.CASE_KEY.getName(), "2")
		.addContent(JiraFieldIdType.ISSUES.getName(), "TEST-1")
		.end();
	
	jira.addTitle("通过不同的身份证创建账号")
		.addStepAndExcept("“身份证”文本框不输入任何信息，点击“保存”按钮", "账号创建失败，并给出相应的提示")
		.addStepAndExcept("在“身份证”文本框中只输入空格，点击“保存”按钮", "账号创建失败，并给出相应的提示")
		.addStepAndExcept("输入15位的证件信息，点击“保存”按钮", "账号创建成功")
		.addStepAndExcept("输入18位的证件信息，点击“保存”按钮", "账号创建成功")
		.addStepAndExcept("输入末尾带“X”或“x”的证件信息，点击“保存”按钮", "账号创建成功")
		.addStepAndExcept("输入大于18位的数字，点击“保存”按钮", "账号创建失败，并给出相应的提示")
		.addStepAndExcept("输入小于18位但大于15位的数字，点击“保存”按钮", "账号创建失败，并给出相应的提示")
		.addStepAndExcept("输入小于15位的数字，点击“保存”按钮", "账号创建失败，并给出相应的提示")
		.addStepAndExcept("输入不符合证件规则但长度符合规则的数字（如123456789012345678），点击“保存”按钮", "账号创建失败，并给出相应的提示")
		.addStepAndExcept("输入非数字字符，点击“保存”按钮", "账号创建失败，并给出相应的提示")
		.addModule("/测试项目/账号管理/创建账号")
		.addContent(JiraFieldIdType.PRECONDITION.getName(), "已在创建账号界面", "除姓名字段外，其他信息均正确填写")
		.addContent(JiraFieldIdType.OBJECTIVE.getName(), "验证创建账号界面各个控件输入是否有效")
		.addContent(JiraFieldIdType.STATUS.getName(), "1")
		.addContent(JiraFieldIdType.PRIORITY.getName(), "1")
		.addContent(JiraFieldIdType.COMPONENT.getName(), "")
		.addContent(JiraFieldIdType.OWNER.getName(), "test")
		.addContent(JiraFieldIdType.CASE_KEY.getName(), "1")
		.addContent(JiraFieldIdType.ISSUES.getName(), "TEST-1")
		.end();
}
```
##### 1.5.2 自定义测试用例编写类
自定义的测试用例编写类，只需要继承CommonTestCaseWrite类即可，其中可以添加与测试用例模板类的关联，可参考JiraTestCaseWrite类的写法。

### 2 Web UI自动化脚本工具
该工具是对selenium代码的一个二次封装，简化了部分代码的编写，使代码调用上更加符合中国人的语法习惯。除此之外，工具还对页面元素的定位方式的管理提供了解决方案，目的在于简化页面元素定位方式的存储，强化对元素定位方式的管理。下面将是工具的具体介绍。

#### 2.1 浏览器
浏览器的启动是Web UI自动化的基础，在工具中，提供了对谷歌、火狐和IE浏览器的支持，也可自行扩展浏览器，具体的扩展方法将在以下的章节中讲到。目前，封装较为完善的浏览器是谷歌浏览，故以下将以谷歌浏览器为例，介绍浏览器工具的使用，其他的浏览器调用类似。

##### 2.1.1 启动浏览器
工具提供三种方式对浏览器进行构造：
```java
public ChromeBrower(File driverFile, Page page)
public ChromeBrower(File driverFile, String url, String pageName)
public ChromeBrower(File driverFile)
```
其中，每种构造方式都必须传入浏览器驱动所在的位置，前两种构造方式比较类似，要求传入待测页面的信息，表示在启动浏览器后，预先加载相应的页面；第三种构造方式若未添加页面时，在打开浏览器后将不加载任何的页面。与selenium不同，为方便对浏览器进行配置，在构造浏览器时，其不会直接打开浏览器，在此期间，可自行对浏览器添加配置，当第一次调用getDriver()方法时，浏览器才会真正的启动。若浏览器已打开，在未关闭的情况下，再次调用getDriver()方法时只会返回当前浏览器的WebDriver类对象，不会再次启动浏览器。即：
```java
private final File CHROME_DRIVER_FILE = new File("Resource/BrowersDriver/Chrom/86.0.4240.22/chromedriver.exe");
private ChromeBrower chrome;

@BeforeClass
public void initBrower() {
	chrome = new ChromeBrower(CHROME_DRIVER_FILE);//不会启动浏览器
	chrome.getDriver();//浏览器被启动
}

@Test
public void getDriver() {
	chrome.getDriver();//再次调用时只返回当前启动浏览器的WebDriver类对象
}
```

##### 2.1.2 浏览器配置
目前只有谷歌浏览器支持在类中配置部分浏览器个性化设置，其他的浏览器类的个性化配置方法还未完成，只能通过selenium提供的相应浏览器的Options类进行配置。在谷歌浏览器中，有提供两个方法配置内置的个性化配置以及一个支持键值对配置个性化的方法：
```java
public void addConfig(ChromeOptionType chromeOptionType, Object value)
public void addConfig(ChromeOptionType chromeOptionType)

public void addPersonalityConfig(String key, Object value)
```
其ChromeOptionType枚举列举了可支持的个性化配置，包括控制已打开的浏览器、设置浏览器大小、是否加载图片等配置，根据是否需要传参，调用相应的addConfig方法即可。例如，在自动化测试中，我们需要配置浏览器以下的信息：
1. 配置浏览器下载文件的路径为：D:/download
2. 配置浏览器不加载图片
3. 配置浏览器不弹出窗口
则在启动浏览器前，我们可以添加如下代码：
```java
private final File CHROME_DRIVER_FILE = new File("Resource/BrowersDriver/Chrom/86.0.4240.22/chromedriver.exe");
private ChromeBrower chrome;

@BeforeClass
public void initBrower() {
	chrome = new ChromeBrower(CHROME_DRIVER_FILE);
	//配置浏览器下载文件的路径为：D:/download
	chrome.addConfig(ChromeOptionType.DOWNLOAD_FILE_PATH, "D:/download");
	//配置浏览器不加载图片
	chrome.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
	//配置浏览器不弹出窗口
	chrome.addPersonalityConfig("profile.managed_default_content_settings.popups", 2);
}

@Test
public void openBrower() {
	chrome.getDriver();
}
```

**特别注意：**当配置控制已打开的浏览器时，即使用CONTRAL_OPEN_BROWER配置，其后的所有个性化配置均不生效：
```java
//控制在9222端口启动的浏览器
chrome.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
//当前配置不会生效
chrome.addConfig(ChromeOptionType.DOWNLOAD_FILE_PATH, "D:/download");
```

##### 2.1.3 打开页面
启动浏览器后，我们则需要加载待测页面，每个待测页面是通过页面类进行控制，关于页面类的介绍，可以参考2.2节。在关于构造浏览器接收中，有提及到传入待测页面的内容，若在构造时，已传入了待测页面的信息，则在打开浏览器后，系统会自动加载相应的页面；若构造时并未传入页面信息，或需要打开新的页面时，则需要调用以下方法：
```java
public void openUrl(String url, String pageName, boolean openNewLabel)
public void openUrl(Page newPage, boolean openNewLabel)
```
其中，除页面信息外，其openNewLabel参数表示是否在新的标签中打开页面，若传入true，则会在当前浏览器中打开一个新的标签来加载传入的页面；若传入为false，则表示在当前标签中加载页面。需要注意的是，该方法意义在于打开页面，并不是在启动浏览器前加载页面，故当未打开浏览器，直接调用该方法时，则会抛出IncorrectPageException异常（后期可能会改进）。下面以打开selenium API页面和好123页面为例，在启动浏览器时，默认加载selenium API页面，在启动浏览器后，再加载好123页面：
```java
private final File CHROME_DRIVER_FILE = new File("Resource/BrowersDriver/Chrom/86.0.4240.22/chromedriver.exe");
private final String URL = "https://www.selenium.dev/selenium/docs/api/java/overview-summary.html";
private ChromeBrower chrome;

@BeforeClass
public void initBrower() {
	chrome = new ChromeBrower(CHROME_DRIVER_FILE, URL, "selenium");
}

@Test
public void openBrower() {
	//chrome.openUrl(new Page("https://www.hao123.com/", "好123"), true);//报错
	chrome.getDriver();
	chrome.openUrl(new Page("https://www.hao123.com/", "好123"), true);
}
```

##### 2.1.4 切换页面
在浏览器存在多个页面的情况下，工具中提供了四种方法，对窗口进行切换
```java
public void switchWindow(String pageName)
public void switchWindow(Page page)
public void switchNowPage()
public boolean switchPopuWindow()
```
其中：
* 前两种切换页面的方法类似，根据存储的页面名称或页面类对页面进行切换；
* 第三种方法是当用户调用selenium提供的切换方式切换了窗口时，调用该方法可将窗口切回用户最后一次通过浏览器对象切换的窗口；
* 第四种方法为，当页面弹出弹窗时，由于该弹窗未被记录在打开的页面中，故可调用该方法，切换到其他页面打开的弹窗上，但存在多个弹窗时，只调用一次该方法则不能保证绝对命中到预期的页面上

具体的调用如下：
```java

```

##### 2.1.5 关闭页面
##### 2.1.6 关闭浏览器
##### 2.1.7 浏览器的扩展

#### 2.2 待测页面
#### 2.3 元素定位方式存储
#### 2.4 元素查找
#### 2.5 事件
##### 2.5.1 基础事件
##### 2.5.2 扩展事件
#### 2.6 数据驱动
#### 2.7 其他工具