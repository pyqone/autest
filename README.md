# autest简介

autest为Auxiliary Test的英文缩写，译为辅助测试，旨在通过代码的形式，辅助日常的测试工作。工具中包括测试用例编写工具、自动化测试二次封装工具以及接口请求工具等等。开发这个项目的目的在于使用简单的代码来简化我们测试日常中较为繁杂的操作，使测试的效率得到一定的提升。

目前工具已打包到maven中央仓库，可按以下代码添加依赖：

```XML
<dependency>
    <groupId>com.gitee.pyqone</groupId>
    <artifactId>autest</artifactId>
    <version>${NEW_VERSION}</version>
</dependency>
```

## autest设计初衷

autest比起说是一个工具，不如说是我在工作中的一个总结，在我看来，测试工作就是一个机械式的工作，既然是机械式的工作就应该用机械来代替，抱着这个想法，于是我就启动了这个项目。工具第一版实际上是我自己为简化Web自动化脚本而做的一个对`selenium`代码简单的封装，但随着工作经验的累积，我在工作中越发地发现测试工作中很多的地方都是重复的工作，但这个重复的工作又不得不去做，并且做这件重复的事情花的时间还不是一般的长，比如编写测试用例、编写测试报告，于是我便尝试着去把这些工具整合到代码中，通过代码的调用来辅助测试工作。

另一方面，随着自动化测试这个名词逐渐的流行，很多的公司跟风，要求测试部门也要弄一个自动化出来。对于熟练使用代码的测试人员而言，其实问题并不大，但对于不会使用代码的测试人员而言，一上手就要写大量的代码，那这将是一件很难的事情。面对这一痛点，许多的公司纷纷选择使用自动化软件来代替编码，但使用过软件的测试人员应该也发现了，软件满足基本的使用问题并不大，但一旦需要进行一些特殊的操作时，那软件便很难实现了。此时，一般有两个方案可以选择，要么手动进行特殊操作，要么就抛弃已有的软件，走自主研发的道路。手动执行便不必说了，若进行自主研发软件，虽然能解决绝大部分的特殊问题，但自行开发软件也是需要成本的，一般也很少考虑走这条路。基于这个问题，我便不断地去尝试使用一种简单的编码方式去尽可能多的完成大量的编码工作，封装各个工具的代码，尽可能多地去减少代码，于是，便诞生了autest工具。

就我个人而言，我并不是开发转测试，在大学研习的也并非计算机专业，所以编写代码会有许多与开发规范不符的地方，关于这点，希望大家能在使用时多多海涵，同时也希望大家能对工具多多批评和指点，我会尽可能地做出改正，使工具更加地完善。

## 工具概要

autest包含如下工具：
  - selenium/appium二次封装工具：用于快速进行web/app的自动化测试，实现代码简化及自动化测试中各个操作的独立
  - sikuli二次封装工具：用于快速进行基于OCR的自动化测试，实现代码简化及自动化测试中各个操作的独立
  - TestNG数据驱动工具：用于在使用TestNG的数据驱动时，进行快速调用，且实现各个参数化文件的读取，方便对数据驱动的定义
  - OKHttp二次封装工具：用于快速进行接口请求和测试，实现代码简化及接口信息模板化的定义，方便对接口进行调试
  - 测试用例生成工具：基于测试用例模板，以快速批量生成测试用例的工具
  - 模板文件写入工具：基于文件模板的文件写入工具，用于快速写入文件的操作工具，特别是对excel文件的写入
  - 随机字符串：用于快速生成随机字符串，以及个性化字符串相关的工具，以及部分预设工具
  - 日期工具：用于快速定义日期及对日期进行操作，满足日常工作对日期内容的计算及输出
  - 其他小工具：包括表格工具、文件读取与写入工具、随机对象生成工具等，用于简化代码，降低使用代码解决测试问题的难度，提高编码效率

# 工具介绍

## 1 selenium/appium二次封装工具

以`selenium`工具为例，`appium`也是基于`selenium`进行实现的，故除了启动代码上有些区别，其他的代码与`selenium`类似。我使用的`selenium`工具是`3.141.59`版本（`appium`使用的是`7.5.1`版本），在使用`selenium`进行自动化脚本编写的实践过程中，我发现`selenium`有以下的问题：
  1. `WebDriver`类构造比较麻烦，特别是指向浏览器驱动的代码，不容易记住，每次编写脚本时都需要去复制之前的代码
  2. 浏览器配置不方便，需要记住浏览器配置相关的代码，该代码没有智能提示
  3. 元素定位方式无法专门维护（`selenium`提供了一种维护元素定位方式的代码，也是现在流行一时的PO模式，但该模式我并未对其深究，因为我仍发现该模式在调用时仍不方便，后面我会提到）
  4. 遇到存在`iframe`标签的元素时，需要不断地切换窗体，在切换频繁时容易出错
  5. 进行元素操作时会因为各种各样的问题而报错，由于元素操作是独立的，所以需要为每一个元素操作都做一次判断，导致代码冗余
  6. 等待机制使用便捷，同样需要进行封装
  7. 元素操作分布在各个类中，其方法调用不统一，导致同一个操作可能出现不同的代码，例如点击元素

以上我仅仅也只是大致总结了一些使用`selenium`代码时常见的问题，但事实上，在编写的过程还存在各式各样的小问题。针对这些不便的地方，结合平时的脚本事件，我便对`selenium`代码进行了一个二次封装，把经常出现，但又不得不写的代码进行了封装，做成单一的方法进行调用，以提高脚本编写的速度。

### 1.1 脚本编写需求

指定待测页面为Selenium API网站：

[<center>https://seleniumhq.github.io/selenium/docs/api/java/index.html</center>](https://seleniumhq.github.io/selenium/docs/api/java/index.html)

之后我们按照以下步骤进行操作：
  1. 点击页面左上角的“FRAMES”
  2. 点击“Packages”窗口中的“org.openqa.selenium.chrome”
  3. 点击下方窗口的“ChromeDriver”
  4. 获取类中所有的构造方法（从“Constructor Summary”表格中获取）

![步骤截图](https://images.gitee.com/uploads/images/2021/0106/195720_fd44ce3a_1776234.png)

**注意：** 除初次进入页面点击的“FRAMES”控件外，其他的控件均在一个`iframe`下。

### 1.2 录制元素

采用xml文件的形式记录所需要操作的元素，内容为：

```XML
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

### 1.3 脚本编写

1. 启动浏览器并加载页面

    ```Java
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
    ```
2. 定义元素文件读取类对象以及元素操作类对象

    ```Java
    //用于查找普通元素
    FindCommonElement common = new FindCommonElement(chrome);
    //用于查找列表型元素
    FindDataListElement data = new FindDataListElement(chrome);
    
    //定义查找元素时读取的元素定位方式存储文件
    File LOCATION_FILE = new File ("api页面元素.xml");
    common.setReadMode(new XmlLocation(LOCATION_FILE), true);
    
    //定义点击事件
    ClickEvent click = new ClickEvent(chrome);
    //定义扩展事件中的列表型事件
    DataTableEvent table = new DataTableEvent(chrome);
    
    ```
3. 对页面元素进行操作

    ```Java
    //点击元素
    click.click(common.getElement("展开窗口"));
    click.click(common.getElement("包名"));
    click.click(common.getElement("类名"));
    //获取构造方法
    table.addList(data.find("构造方法列表"));
    //获取列表的所有元素文本，并进行输出
    table.getListText("构造方法列表").stream().map(text -> text.orElse("空值")).forEach(System.out::println);
    ```
4. 关闭浏览器

    ```Java
    chrome.closeBrower();
    ```

## 2 sikuli二次封装工具

`sikuli`是基于图片识别的原理进行的自动化操作，其优点在于可无视软件底层的前端框架结构，直接对符合录制的元素内容进行操作；但缺点在于，其比较依赖截图时的图片，若元素的样式改变或屏幕上有多个与其相似的内容时，则可能出现操作元素识别不到或不准确的情况。在特殊情况下，`sikuli`工具可以与`selenium/appium`工具结合使用，以弥补`selenium/appium`工具的不足。关于`sikuli`的简介，可参考Wiki中的“[Sikuli（图形自动化工具）](https://gitee.com/pyqone/autest/wikis/%E5%B7%A5%E5%85%B7%E4%BB%8B%E7%BB%8D/sikuli%E5%B7%A5%E5%85%B7%E4%BB%8B%E7%BB%8D/Sikuli%EF%BC%88%E5%9B%BE%E5%BD%A2%E8%87%AA%E5%8A%A8%E5%8C%96%E5%B7%A5%E5%85%B7%EF%BC%89)”一文，工具中使用的`sikuli`版本号为`2.0.5`。

### 2.1 脚本录制需求

以从桌面打开浏览器并进入Selenium API页面为例，其操作步骤为：
  1. 双击桌面上的“Microsoft Edge”图标，打开Edge浏览器

      ![](https://foruda.gitee.com/images/1694132100727211033/c910671e_1776234.png)
  2. 在浏览器的地址框中输入“[https://seleniumhq.github.io/selenium/docs/api/java/index.html](https://seleniumhq.github.io/selenium/docs/api/java/index.html)”

      ![](https://foruda.gitee.com/images/1694132249846131040/17c60c59_1776234.png)
  3. 按下回车，进入Selenium API页面

由于`sikuli`是基于图片识别的原理进行的操作，此处的示例只能以我桌面上的内容进行举例，其他机器上的桌面背景，浏览器图片和浏览器主题、配置等都会对相应的截图有影响，故下面元素录制文件与代码仅做参考。

### 2.2 录制元素

由于`sikuli`的元素是以图片的形式进行存储，故可参考以上内容，图片将保存至测试项目路径下。与`selenium`的元素类似，将元素的路径及其参数存放至`xml`文档中：

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project path="src/main/resources/com/test/sikuli" suffix='png'>
  <element name='浏览器' wait='3'>
    <file similar='0.9' />
  </element>
  
  <element name='地址栏' wait='3'>
    <file similar='0.8' />
  </element>
</project>
```

### 2.3 脚本编写

1. 定义元素文件类对象

    ```Java
    // 定义元素文件类对象
    File sikulElementFile = new File("src/main/resources/com/test/sikuli/element.xml");
    ```
2. 初始化sikuli屏幕类对象、文件读取类对象以及操作类对象

    ```Java
    // 初始化sikuli工具
    Screen s = new Screen();
    // 初始化元素查找类对象
    FindSikuliElement fse = new FindSikuliElement(s);
    fse.setReadMode(new XmlSikuliLocation(sikulElementFile));
    
    // 初始化元素操作类对象
    SikuliMouseEvent sme = new SikuliMouseEvent();
    SikuliKeyboardEvent ske = new SikuliKeyboardEvent();
    sme.setFindElementTool(fse);
    ske.setFindElementTool(fse);
    ```
3. 对元素进行操作

    ```Java
    // 双击浏览器图标
    sme.doubleClick("浏览器");
    // 在地址栏中输入URL
    ske.input("地址栏", "https://seleniumhq.github.io/selenium/docs/api/java/index.html");
    // 按下回车
    ske.type(KeyType.ENTER);
    ```

**注意：** 为简化演示代码，此处是直接通过Screen 对象对屏幕中的浏览器图片双击打开，由于未经过App对象进行打开，导致无法关闭应用， 故在执行以上脚本后，程序无法自动停止（一直在监听屏幕），需要手动停止程序的运行。

## 3 TestNG数据驱动工具

数据驱动在自动化测试过程中使用得还是比较广泛的，无论是在UI自动化还是接口自动化，都有数据驱动的身影。在工具中，数据可存放在文本文件（doc/docx/txt格式文件）、excel文件（xls/xlsx格式文件）或csv文件（csv格式文件）中，亦可自定义数据源及数据的读取方式，只需要将读取到的表数据转换为[TableData](https://apidoc.gitee.com/pyqone/autest/com/auxiliary/tool/data/TableData.html)类即可。

由于目前工具只对`TestNG`框架的数据驱动进行了封装，故后面将以`TestNG`的框架进行演示。

### 3.1 数据准备

测试数据如下表所示，将以下数据存储至excel文件中，文件命名为“数据驱动.xlsx”。

|姓名|手机号码|更改时间|
|-|-|-|
|${rs(CH,2, 3)}|13000000000|${time()}|
|${rs(CH,2, 3)}|13000000001|${time(-1d)}|
|${rs(CH,2, 3)}|13000000002|${time(-2d)}|


其中使用“`${}`”括起来的内容表示使用公式，该公式可自定义。

### 3.2 脚本编写

1. 定义TestNG框架中的@DataProvider方法，构造数据的读取：

    ```Java
    @DataProvider(name = "data")
    public Object[][] initDataDriver() {
        // 构造对象并指向数据驱动文件
        TestNGDataDriver dataDriver = new TestNGDataDriver();
        // 读取数据驱动文件，并将其转换为TableData类对象
        TableData<String> dataTable = TableFileReadUtil.readExcel(new File("src/main/resources/com/test/datadriver/数据驱动.xlsx"), "TestNG", true);
        // 将字符串类表格数据转换为Object类，并将其添加至数据驱动类中
        dataDriver.addDataDriver(ListUtil.changeTable(dataTable, data -> (Object) data));
    
        // 加载公式
        dataDriver.addFunction(Functions.randomString());
        dataDriver.addFunction(Functions.getNowTime());
    
        // 返回TestNG框架识别的数据驱动
        return dataDriver.getDataDriver();
    }
    ```
2. 定义TsetNG框架中的@Test方法，对数据驱动进行读取：

    ```Java
    int count = 1;
    @Test(dataProvider = "data")
    public void outPutData(Data data) {
      System.out.println("第" + count++ + "次执行输出结果：");
      System.out.println("姓名：" + data.getString("姓名"));
      System.out.println("手机号码：" + data.getString("手机号码"));
      System.out.println("更改时间：" + data.getString("更改时间"));
      System.out.println("===========================");
    }
    ```

    由于“姓名”列使用了随机方法生成内容以及“更改时间”列使用了根据当前时间生成数据的方法，故每次运行脚本时都可能不同，以下将是某次运行时得到的结果：
    
    ```Bash
    第1次执行输出结果：
    姓名：步佳
    手机号码：13000000000
    更改时间：2023-09-13 08:37:52
    ===========================
    第2次执行输出结果：
    姓名：个伴
    手机号码：13000000001
    更改时间：2023-09-12 08:37:52
    ===========================
    第3次执行输出结果：
    姓名：跳赞怎
    手机号码：13000000002
    更改时间：2023-09-11 08:37:52
    ===========================
    ```

# 后记

作为一个没有系统学习过开发的我而言，能力实在有限，代码质量也不高，对于设计模式也是一知半解的，工具中很多的地方都是只追求实现。但有件事情希望在使用的您能明白：作为测试工程师，我们的代码不用上服务器运行、不用考虑多线程（大多数情况下）、不用考虑那0.01秒的响应。对于软件测试而言，最重要的是得到一个结果，即便代码的效率再不堪，也比我们人工测试来得快。我们追求代码应该是使用简单，快速得到数据，能简化测试工作就好，而不是一味地追求与开发工程师一样，使用各式各样的框架、设计模式，这样的代码首先使用就不方便，其次就是大多数人都看不懂，这就失去意义了。

当然，以上只是我个人的感觉，若与您的想法相未被，还请您见谅。若您在使用上有什么的建议，或在代码上有什么优化的方法，还请您不吝教授于我，我将万分感谢您的批评。若您也有兴趣维护该项目，可拉取分支进行开发，当然，提交时还望您完整写好注释，以便于使用和生成文档。若您在工作上使用后有其他的功能需要封装，可与我联系，将需求告知于我，我会抽时间出来开发您需要的功能。我的邮箱是：465615774@qq.com。