# autest
## 简介
### autest设计初衷
autest为Auxiliary Test的英文缩写意为辅助测试，其中包括日常测试工作中能用到的工具，包括测试用例编写工具，简化Web UI自动化测试工具以及测试报告生成工具和日常工作中使用的小工具等。开发这个项目的目的在于使用简单的代码来简化我们测试日常中较为繁杂的操作，使测试的效率得到一定的提升。
autest比起说是一个工具，不如说是我在工作中的一个总结，在我看来，测试工作就是一个机械式的工作，既然是机械式的工作就应该用机械来代替，抱着这个想法，于是我就启动了这个项目。但我并不是开发转测试，在大学学习的也是化学工程专业，并未系统地学习过软件工程，所以在编写代码时会有许多与开发规范不符合的地方，关于这点，希望大家能在使用时多多海涵，同时也希望大家能对工具多多批评和指点，我会尽可能地做出改正，使工具更加地完善。
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
该工具是通过预先写好的测试用例文件模板，调用其中添加内容的方法，对测试用例进行编写，之后再生成一个Excel文件，以方便测试用例阅读与上传。当然，看到这许多人就有疑问了，既然最后要生成一个Excel文件，那编写测试用例直接在Excel文档里写就好，何必还要编写代码，然后再生成呢？的确，在office的Excel软件中，其可视化界面确实要比写代码要强很多，但Excel软件也存在上下滚动不方便的缺点，并且，大家也清楚，很多测试用例都可以复用，在编写过程中难免会有大量的复制和替换的工作，对于少量的用例还好，一旦用例较多时，复制用例后，就容易遗漏需要替换文本的用例，或者多复制用例，导致编写出错。为解决这一类的问题，所以我封装了一个测试用例编写工具，将测试用例的编写工作，由Excel向eclipse（不要问我为什么不用IDEA，有伤T_T）转移，当然，缺点就是可视化差了一些。
测试用例工具暂时做了Jira用例模板，故此处以Jira为例，讲解工具的使用，在最后，再讲解测试用例模板的扩展。
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
#### 1.3 测试用例模板
##### 1.3.1 测试用例模板使用
##### 1.3.2 测试用例模板扩展
#### 1.4 测试用例字段标记
#### 1.5 测试用例模板扩展
##### 1.5.1 测试用例字段xml文件的编写
##### 1.5.2 测试用例模板类基类的继承
##### 1.5.3 测试用例模板类字段枚举（可选）