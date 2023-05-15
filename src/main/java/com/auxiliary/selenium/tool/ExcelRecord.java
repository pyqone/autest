package com.auxiliary.selenium.tool;

import java.io.File;
import java.util.Optional;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.tool.date.Time;
import com.auxiliary.tool.file.excel.WriteExcelTempletFile;

/**
 * <p>
 * <b>文件名：</b>ExcelRecord.java
 * </p>
 * <p>
 * <b>用途：</b> 用于记录自动化测试的运行过程，可记录用例步骤、结果、浏览器信息等，并在记录失败时提供自动截图。
 * </p>
 * <p>
 * <b>编码时间：</b>2020年8月12日下午2:16:55
 * </p>
 * <p>
 * <b>修改时间：</b>2021年10月1日 上午10:51:09
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class ExcelRecord extends WriteExcelTempletFile<ExcelRecord> {
    /**
     * 用于记录当前出现BUG的结果位置
     */
    private int bugNum = 0;
    /**
     * 用于指向当前是否存在异常
     */
    private boolean isError = false;
    /**
     * 用于记录开始执行的时间
     */
    private long startTime = 0L;
    /**
     * 记录写入到文件的基础数据
     */
    private RecordData data = new RecordData();

    /**
     * 记录运行类名称
     */
    private String className = "";
    /**
     * 记录运行方法名称
     */
    private String methodName = "";

    private final String TEMP_TEST_CASE = "测试用例";
    private final String TEMP_RUN_STEP = "运行记录";
    private final String TEMP_EXCEPT_RECORD = "错误记录";

    /**
     * 根据默认的模板，将记录文件生成到指定的目录下
     * 
     * @param saveFile 记录文件保存路径
     * @throws DocumentException
     */
    public ExcelRecord(File saveFile) throws DocumentException {
        super(new SAXReader().read(new File(
                ExcelRecord.class.getClassLoader().getResource("com/auxiliary/file/ExcelRecordTemplet.xml").getFile())),
                saveFile);
    }

    /**
     * 用于添加浏览器信息。该方法不能单独调用，必须配合其他 其他方法一起使用，否则在生成文件时，其写入的内容不会被填写到文件中
     * 
     * @param brower 继承自{@link AbstractBrower}类的浏览器类对象
     * @return 类本身
     */
    public ExcelRecord setBrowerInformation(AbstractBrower brower) {
        // 获取浏览器信息，将其转换为JSONObject对象
        JSONObject json = JSON.parseObject(brower.getAllInformation());
        switchPage(TEMP_RUN_STEP);
        addContent("brower", json.get("浏览器名称").toString()).addContent("version", json.get("浏览器版本").toString())
                .addContent("system", json.get("操作系统版本").toString());

        return this;
    }

    /**
     * 用于添加执行者。该方法不能单独调用，必须配合的其他其他方法一起使用， 否则在生成文件时，其写入的内容不会被填写到文件中
     * 
     * @param actionName 执行者
     * @return 类本身
     */
    public ExcelRecord setActionName(String actionName) {
        switchPage(TEMP_RUN_STEP);
        setFieldValue("active_person", actionName);

        return this;
    }

    /**
     * 用于添加运行类名以及运行方法名。多次调用时会清空上一次记录的内容
     * 
     * @param className  类名
     * @param methodName 方法名
     * @return 类本身
     */
    public ExcelRecord runMethod(String className, String methodName) {
        switchPage(TEMP_RUN_STEP);
        setFieldValue("class_name", className);
        setFieldValue("method_name", methodName);

        this.className = className;
        this.methodName = methodName;

        return this;
    }

    /**
     * 用于记录执行开始时间，记录结束时，会写入相应的结束时间
     */
    public ExcelRecord reckonByTime() {
        // 若设定当前状态为开始记录状态，则存储当前的时间戳
        startTime = System.currentTimeMillis();

        return this;
    }

    /**
     * 用于添加实际运行步骤，支持传入多条，每条数据将自动编号以及换行
     * 
     * @param text 实际运行步骤
     * @return 类本身
     */
    public ExcelRecord runStep(String... text) {
        switchPage(TEMP_RUN_STEP);
        data.isRecordRun = true;
        return addContent("step", text);
    }

    /**
     * 用于添加执行结果，根据传入的Bug判定来记录当前执行的Bug数量。该方法可调用多次， 将多个结果，结果将自动编号。
     * 
     * @param text 结果
     * @param bug  标记是否为Bug
     * @return 类本身
     */
    public ExcelRecord runResult(String text, boolean bug) {
        switchPage(TEMP_RUN_STEP);
        data.isRecordRun = true;
        addContent("result", text);
        // 若当前结果为bug，则bug数量+1，且将当前字体颜色标记为红色
        if (bug) {
            bugNum++;
            changeTextColor(IndexedColors.RED, "result", -1);
        }

        return this;
    }

    /**
     * 用于添加运行备注
     * 
     * @param text 备注文本
     * @return 类本身
     */
    public ExcelRecord runMark(String text) {
        switchPage(TEMP_RUN_STEP);
        data.isRecordRun = true;
        return addContent("mark", text);
    }

    /**
     * 用于添加运行时截图
     * 
     * @param screenshotFile 截图文件对象
     * @return 类本身
     */
    public ExcelRecord runScreenshot(File screenshotFile) {
        switchPage(TEMP_RUN_STEP);
        data.isRecordRun = true;
        return addContent("screenshot_position", screenshotFile.getPath()).linkFile("screenshot_position",
                screenshotFile);
    }

    /**
     * 用于添加运行记录编号
     * 
     * @param text 编号文本
     * @return 类本身
     */
    public ExcelRecord runId(String text) {
        switchPage(TEMP_RUN_STEP);
        data.isRecordRun = true;
        return addContent("id", text);
    }

    /**
     * 用于添加异常步骤的信息，传入异常类后，将会自动记录异常步骤
     * 
     * @param exception 异常类
     * @return 类本身
     */
    public ExcelRecord exception(Exception exception) {
        isError = true;
        // 获取运行记录模板的json串
        JSONObject caseJson = dataMap.get(TEMP_RUN_STEP).getCaseJson();
        switchPage(TEMP_EXCEPT_RECORD);
        data.isRecordEx = true;

        // 将运行记录中错误的步骤信息放置于此
        addContent("id", Optional.ofNullable(caseJson.getJSONObject("id")).map(json -> json.getJSONArray(KEY_DATA))
                .map(arr -> arr.getJSONObject(0)).map(json -> json.getString(KEY_TEXT)).orElse(""));
        addContent("error_step",
                Optional.ofNullable(caseJson.getJSONObject("step")).map(json -> json.getJSONArray(KEY_DATA))
                        .map(arr -> arr.getJSONObject(0)).map(json -> json.getString(KEY_TEXT)).orElse(""));
        addContent("class_name", className);
        addContent("method_name", methodName);

        // 创建超链接
        linkField("id", TEMP_RUN_STEP + "|id", data.getRunLinkIndex());
        switchPage(TEMP_RUN_STEP);
        linkField("state", TEMP_EXCEPT_RECORD + "|id", data.getExLinkIndex());

        // 记录错误信息
        switchPage(TEMP_EXCEPT_RECORD);
        return addContent("error_class", exception.getClass().getName()).addContent("error_information",
                exception.getMessage());
    }

    /**
     * 用于添加异常步骤信息，并记录截图，可参见{@link #exception(Exception)}
     * 
     * @param exception      异常类
     * @param screenshotFile 截图文件对象
     * @return 类本身
     */
    public ExcelRecord exception(Exception exception, File screenshotFile) {
        return exception(exception).addContent("screenshot_position", screenshotFile.getPath())
                .linkFile("screenshot_position", screenshotFile);
    }

    /**
     * 用于添加测试用例的前置条件，支持传入多条，每条数据将自动编号以及换行
     * 
     * @param text 前置条件
     * @return 类本身
     */
    public ExcelRecord caseCondition(String... text) {
        switchPage(TEMP_TEST_CASE);
        data.isRecordCase = true;
        return addContent("condition", text);
    }

    /**
     * 用于添加测试用例的用例编号，不支持传入多个用例编号，每次调用方法后将覆盖之前传入的内容
     * 
     * @param text 用例编号
     * @return 类本身
     */
    public ExcelRecord caseId(String text) {
        switchPage(TEMP_TEST_CASE);
        data.isRecordCase = true;
        addContent("case_id", text);
        switchPage(TEMP_RUN_STEP);
        data.isRecordRun = true;
        addContent("case_id", text);
        linkField("case_id", TEMP_TEST_CASE + "|case_id", data.getCaseLinkIndex());
        switchPage(TEMP_TEST_CASE);
        return linkField("case_id", TEMP_RUN_STEP + "|case_id", data.getRunLinkIndex());
    }

    /**
     * 用于添加测试用例的标题，不支持传入多个标题，每次调用方法后将覆盖之前传入的内容
     * 
     * @param text 标题
     * @return 类本身
     */
    public ExcelRecord caseTitle(String text) {
        switchPage(TEMP_TEST_CASE);
        data.isRecordCase = true;
        return addContent("title", text);
    }

    /**
     * 用于添加测试用例的步骤，支持传入多条，每条数据将自动编号以及换行
     * 
     * @param text 标题
     * @return 类本身
     */
    public ExcelRecord caseStep(String... text) {
        switchPage(TEMP_TEST_CASE);
        data.isRecordCase = true;
        return addContent("step", text);

    }

    /**
     * 用于添加测试用例的预期，支持传入多条，每条数据将自动编号以及换行
     * 
     * @param text 标题
     * @return 类本身
     */
    public ExcelRecord caseExpect(String... text) {
        switchPage(TEMP_TEST_CASE);
        data.isRecordCase = true;
        return addContent("expect", text);
    }

    @Override
    public ExcelRecord end(int index) {
        // 记录运行状态，Bug数量，当前时间以及运行时间
        switchPage(TEMP_RUN_STEP);
        data.isRecordRun = true;
        addContent("state", isError ? "1" : "0").addContent("bug_number", String.valueOf(bugNum))
                .addContent("active_time", Time.parse().getFormatTime()).addContent("use_time", startTime == 0L ? ""
                        : (String.valueOf((System.currentTimeMillis() - startTime) / 1000.0) + "s"));
        if (isError) {
            changeTextColor(IndexedColors.YELLOW, "state", -1);
        }

        // 初始化数据
        data.reckonIndex();
        bugNum = 0;
        startTime = 0L;
        isError = false;
        className = "";
        methodName = "";
        return super.end(index);
    }

    /**
     * <p>
     * <b>文件名：</b>ExcelRecord.java
     * </p>
     * <p>
     * <b>用途：</b>用于记录当前编写的数据内容数量，以便于超链接数据的传参
     * </p>
     * <p>
     * <b>编码时间：</b>2021年10月3日 上午10:59:39
     * </p>
     * <p>
     * <b>修改时间：</b>2021年10月3日 上午10:59:39
     * </p>
     * 
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     */
    private class RecordData {
        /**
         * 存储当前是否记录用例数据
         */
        boolean isRecordCase = false;
        /**
         * 存储当前是否记录运行数据
         */
        boolean isRecordRun = false;
        /**
         * 存储当前是否记录异常数据
         */
        boolean isRecordEx = false;

        /**
         * 存储用例数据的超链接位置
         */
        int caseLinkIndex = 2;
        /**
         * 存储运行数据的超链接位置
         */
        int runLinkIndex = 2;
        /**
         * 存储异常数据的超链接位置
         */
        int exLinkIndex = 2;

        /**
         * 返回用例数据的超链接位置
         * 
         * @return 用例数据的超链接位置
         */
        public int getCaseLinkIndex() {
            return caseLinkIndex;
        }

        /**
         * 返回运行数据的超链接位置
         * 
         * @return 运行数据的超链接位置
         */
        public int getRunLinkIndex() {
            return runLinkIndex;
        }

        /**
         * 返回异常数据的超链接位置
         * 
         * @return 异常数据的超链接位置
         */
        public int getExLinkIndex() {
            return exLinkIndex;
        }

        /**
         * 计算所有数据的下标，并初始化记录内容
         */
        public void reckonIndex() {
            if (isRecordCase) {
                caseLinkIndex++;
                isRecordCase = false;
            }

            if (isRecordRun) {
                runLinkIndex++;
                isRecordRun = false;
            }

            if (isRecordEx) {
                exLinkIndex++;
                isRecordEx = false;
            }
        }
    }
}
