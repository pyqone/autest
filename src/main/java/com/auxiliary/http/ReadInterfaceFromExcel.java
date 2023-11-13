package com.auxiliary.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.AuxiliaryToolsException;
import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.file.TextFileReadUtil;
import com.auxiliary.tool.regex.ConstType;
import com.auxiliary.tool.regex.RegexType;

/**
 * <p>
 * <b>文件名：ReadInterfaceFromExcel.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于读取excel文件中的接口信息，文件支持“.xls”和“.xlsx”格式，但在读取完后，需要调用{@link #close()}方法关闭文件的读取
 * </p>
 * <p>
 * <b>编码时间：2022年9月8日 上午10:48:30
 * </p>
 * <p>
 * <b>修改时间：2022年9月8日 上午10:48:30
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.7.0
 */
public class ReadInterfaceFromExcel extends ReadInterfaceFromAbstract
        implements ActionEnvironment, AssertResponse, ExtractResponse, BeforeOperation {
    /**
     * 用于对特殊内容进行定义的符号
     */
    protected final String SPECIAL_CONTENT_SYMBOL = "#";

    /**
     * 定义模板文件
     */
    protected Workbook excel = null;
    /**
     * 用于对单元格内容进行格式化输出
     */
    protected DataFormatter format = new DataFormatter();

    /**
     * 存储环境数据
     */
    protected HashMap<String, String> envMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口数据及接口所在的行号
     */
    protected HashMap<String, Entry<Integer, Integer>> interMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口参数
     */
    protected HashMap<String, Entry<Integer, Integer>> interParamMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储前置接口操作
     */
    protected HashMap<String, Entry<Integer, Integer>> beforeInterMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储普通类型请求体
     */
    protected HashMap<String, Entry<Integer, Integer>> normalBodyMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储文件类型请求体
     */
    protected HashMap<String, Entry<Integer, Integer>> fileBodyMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储表单类型请求体
     */
    protected HashMap<String, Entry<Integer, Integer>> formBodyMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储请求头参数
     */
    protected HashMap<String, Entry<Integer, Integer>> requestHeaderMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储cookie参数
     */
    protected HashMap<String, Entry<Integer, Integer>> cookieMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储相应内容参数
     */
    protected HashMap<String, Entry<Integer, Integer>> responseMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口断言
     */
    protected HashMap<String, Entry<Integer, Integer>> assertMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口提词
     */
    protected HashMap<String, Entry<Integer, Integer>> extractMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 当前执行接口的环境名称
     */
    private String actionEnvironmentName = "";

    /**
     * 构造对象，读取模板文件，并初始化各个参数
     *
     * @param excelFile 接口模板文件
     */
    public ReadInterfaceFromExcel(File excelFile) {
        // 根据文件后缀格式，对文件内容进行读取
        try (FileInputStream fis = new FileInputStream(excelFile)) {
            if (excelFile.getName().matches(".*\\.xlsx")) {
                excel = new XSSFWorkbook(fis);
            } else if (excelFile.getName().matches(".*\\.xls")) {
                excel = new HSSFWorkbook(fis);
            } else {
                throw new InterfaceReadToolsException(String.format("文件“%s”非excel文件格式，无法读取", excelFile.getName()));
            }
        } catch (Exception e) {
            throw new InterfaceReadToolsException(String.format("文件读取异常，请检查文件：%s", excelFile.getAbsolutePath()), e);
        }

        // 读取接口数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_INTER.getKey()))
                .ifPresent(dataSheet -> addInterNameRowIndex(interMap, ExcelField.INTER_INTER_NAME, dataSheet));
        // 读取接口参数数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_INTER_PARAM.getKey()))
                .ifPresent(dataSheet -> addInterNameRowIndex(interParamMap,
                        ExcelField.INTER_PARAM_INTER_NAME, dataSheet));
        // 读取前置接口操作数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_BEFORE_INTER_OPEARTE.getKey()))
                .ifPresent(dataSheet -> addInterNameRowIndex(beforeInterMap,
                        ExcelField.BEFORE_INTER_OPEARTE_INTER_NAME, dataSheet));
        // 读取普通类型请求体数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_NORMAL_BODY.getKey()))
                .ifPresent(dataSheet -> addInterNameRowIndex(normalBodyMap,
                        ExcelField.NORMAL_BODY_INTER_NAME, dataSheet));
        // 读取文件类型请求体数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_FILE_BODY.getKey())).ifPresent(
                dataSheet -> addInterNameRowIndex(fileBodyMap, ExcelField.FILE_BODY_INTER_NAME, dataSheet));
        // 读取表单类型请求体数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_FORM_BODY.getKey())).ifPresent(
                dataSheet -> addInterNameRowIndex(formBodyMap, ExcelField.FORM_BODY_INTER_NAME, dataSheet));
        // 读取请求头数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_REQUEST_HEADER.getKey()))
                .ifPresent(dataSheet -> addInterNameRowIndex(requestHeaderMap,
                        ExcelField.REQUEST_HEADER_INTER_NAME, dataSheet));
        // 读取cookie数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_COOKIE.getKey())).ifPresent(
                dataSheet -> addInterNameRowIndex(cookieMap, ExcelField.COOKIE_INTER_NAME, dataSheet));
        // 读取接口响应数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_RESPONSE.getKey())).ifPresent(
                dataSheet -> addInterNameRowIndex(responseMap, ExcelField.RESPONSE_INTER_NAME, dataSheet));
        // 读取接口断言数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_ASSERT.getKey())).ifPresent(
                dataSheet -> addInterNameRowIndex(assertMap, ExcelField.ASSERT_INTER_NAME, dataSheet));
        // 读取接口提词数据
        Optional.ofNullable(excel.getSheet(ExcelField.SHEET_EXTRACT.getKey())).ifPresent(
                dataSheet -> addInterNameRowIndex(extractMap, ExcelField.EXTRACT_INTER_NAME, dataSheet));

        // 获取环境sheet页，若不存在，则不存储执行环境及环境集合
        Sheet envSheet = excel.getSheet(ExcelField.SHEET_ENV.getKey());
        if (envSheet != null) {
            // 获取当前sheet的所有行
            List<Row> rowList = getSheetAllRow(envSheet);
            // 判断当前是否存在行数据，若不存在，则跳过获取单元格
            if (!rowList.isEmpty()) {
                // 定义是否已存储默认执行环境的标记
                boolean isSaveDefaultEnv = false;
                // 反向遍历所有的行，以方便存储默认的执行环境（当未标记执行环境时，则将第一个环境数据作为默认环境）
                for (int rowIndex = rowList.size() - 1; rowIndex > -1; rowIndex--) {
                    // 获取当前行中的所有单元格
                    List<Optional<Cell>> cellList = getRowAllCell(rowList.get(rowIndex),
                            ExcelField.SHEET_ENV.getValue());
                    if (cellList.isEmpty()) {
                        continue;
                    }
                    // 获取环境名称单元格内容，若不存在该单元格，或名称为空串，则跳过该行内容的获取
                    String envName = cellList.get(ExcelField.ENV_ENV_NAME).map(format::formatCellValue)
                            .orElse("");
                    if (envName.isEmpty()) {
                        continue;
                    }

                    // 存储当前环境内容，并获取其url
                    envMap.put(envName, cellList.get(ExcelField.ENV_URL).map(format::formatCellValue)
                            .orElse(InterfaceInfo.DEFAULT_PROTOCOL + InterfaceInfo.DEFAULT_HOST));

                    // 判断当前是否已存储执行环境
                    if (!isSaveDefaultEnv) {
                        // 判断环境名称第一位字符是否为“#”符号，若存在该符号，则设置已存储默认环境
                        if (envName.indexOf(SPECIAL_CONTENT_SYMBOL) == 0) {
                            isSaveDefaultEnv = true;
                        }
                        // 存储当前环境
                        actionEnvironmentName = envName;
                    }
                }
            }
        }
    }

    @Override
    public void setActionEnvironment(String environmentName) {
        // 若环境集合中不存在该环境，则不进行存储
        if (envMap.containsKey(environmentName)) {
            actionEnvironmentName = environmentName;
        }
    }

    @Override
    public HashMap<String, String> getActionEnvironment() {
        return envMap;
    }

    @Override
    public List<BeforeInterfaceOperation> getBeforeOperation(String interName) {
        return getInterface(interName).getBeforeOperationList();
    }

    @Override
    public List<String> getParentInterfaceName(String interName) {
        return getBeforeOperation(interName).stream().map(before -> before.getName()).collect(Collectors.toList());
    }

    @Override
    public Set<String> getExtractContent(String interName) {
        return getInterface(interName).getAssertRuleJson();
    }

    @Override
    public Set<String> getAssertContent(String interName) {
        return getInterface(interName).getExtractRuleJson();
    }

    @Override
    public InterfaceInfo getInterface(String interName) {
        if (interName == null || interName.isEmpty()) {
            throw new InterfaceReadToolsException("指定的接口名称为空或未指定接口名称：" + interName);
        }
        // 判断接口名称在接口表中是否存在数据，若不存在，则抛出异常
        if (!interMap.containsKey(interName)) {
            throw new InterfaceReadToolsException(String.format("接口元素“%s”在文件中不存在", interName));
        }

        // 根据接口sheet页内容，获取接口的环境信息
        String envName = Optional
                .ofNullable(excel.getSheet(ExcelField.SHEET_INTER.getKey()).getRow(interMap.get(interName).getKey()))
                .map(row -> row.getCell(ExcelField.INTER_ENV)).map(format::formatCellValue).orElse("");
        return getInterface(interName, envName);
    }

    @Override
    public InterfaceInfo getInterface(String interName, String environmentName) {
        if (interName == null || interName.isEmpty()) {
            throw new InterfaceReadToolsException("指定的接口名称为空或未指定接口名称：" + interName);
        }

        String env = Optional.ofNullable(envMap.get(environmentName)).orElse("");

        // 判断接口是否缓存，若已缓存，则直接返回
        if (interfaceCacheMap.containsKey(interName)) {
            InterfaceInfo inter = interfaceCacheMap.get(interName).clone();
            if (!env.isEmpty()) {
                inter.setHost(env);
            }
            return inter;
        }

        // 判断接口名称在接口表中是否存在数据，若不存在，则抛出异常
        if (!interMap.containsKey(interName)) {
            throw new InterfaceReadToolsException(
                    String.format("接口元素“%s”在文件中不存在", interName));
        }

        InterfaceInfo inter = new InterfaceInfo();
        // 判断当前是否获取到坏境，若不存在，则根据默认环境获取，若默认也为空，则设置为空串
        inter.setHost(!env.isEmpty() ? env : Optional.ofNullable(envMap.get(actionEnvironmentName)).orElse(""));
        // 读取接口的url、请求方式及超时时间信息
        readInterSheetContent(interName, inter);
        // 读取接口参数sheet数据
        readMultirowSheetContent(interName, ExcelField.SHEET_INTER_PARAM.getKey(), inter, interParamMap);
        // 读取请求头sheet数据
        readMultirowSheetContent(interName, ExcelField.SHEET_REQUEST_HEADER.getKey(), inter, requestHeaderMap);
        // 读取cookie sheet数据
        readMultirowSheetContent(interName, ExcelField.SHEET_COOKIE.getKey(), inter, cookieMap);
        // 读取前置接口sheet数据
        readMultirowSheetContent(interName, ExcelField.SHEET_BEFORE_INTER_OPEARTE.getKey(), inter, beforeInterMap);
        // 设置响应字符集
        readMultirowSheetContent(interName, ExcelField.SHEET_RESPONSE.getKey(), inter, responseMap);
        // 设置响应断言
        readMultirowSheetContent(interName, ExcelField.SHEET_ASSERT.getKey(), inter, assertMap);
        // 设置提词
        readMultirowSheetContent(interName, ExcelField.SHEET_EXTRACT.getKey(), inter, extractMap);
        // 设置请求体
        readBodySheetContent(interName, inter);

        // 缓存读取的接口
        interfaceCacheMap.put(interName, inter);
        return inter;
    }

    protected void readBodySheetContent(String interName, InterfaceInfo inter) {
        // 若模板中指定了普通请求体文本，则对接口添加普通请求体文本
        if (normalBodyMap.containsKey(interName)) {
            // 获取普通请求体sheet页
            Sheet sheet = excel.getSheet(ExcelField.SHEET_NORMAL_BODY.getKey());
            if (sheet != null) {
                // 获取请求体的起始行与结束行，由于普通请求体只获取单一一行，故只获取其起始行下标
                Row row = sheet.getRow(normalBodyMap.get(interName).getKey());
                // 获取类型文本
                String typeCellText = Optional.ofNullable(row.getCell(ExcelField.NORMAL_BODY_TYPE)).map(format::formatCellValue).orElse("");
                // 获取请求体内容文本，若请求体内容在文本中，则读取文本中的内容
                String contentCellText = Optional.ofNullable(row.getCell(ExcelField.NORMAL_BODY_CONTENT))
                        .map(format::formatCellValue).map(content -> {
                            // 若读取到的内容是以特殊符号开头，以特殊符号结尾，则认为其为内容在文件中，则读取其相应路径下的内容
                            if (content.indexOf(SPECIAL_CONTENT_SYMBOL) == 0
                                    && content.lastIndexOf(SPECIAL_CONTENT_SYMBOL) == content.length() - 1) {
                                String filePath = content.substring(
                                        content.indexOf(SPECIAL_CONTENT_SYMBOL) + SPECIAL_CONTENT_SYMBOL.length(),
                                        content.lastIndexOf(SPECIAL_CONTENT_SYMBOL));

                                if (!filePath.isEmpty()) {
                                    try {
                                        return TextFileReadUtil.megerTextToTxt(new File(filePath), true);
                                    } catch (AuxiliaryToolsException e) {
                                    }
                                }
                            }
                            return content;
                        }).orElse("");

                // 若请求体内容不为空，则按照指定的方式设置请求体内容
                if (!contentCellText.isEmpty()) {
                    try {
                        inter.setBodyContent(MessageType.typeText2Type(typeCellText), contentCellText);
                    } catch (Exception e) {
                        inter.setBody(contentCellText);
                    }
                    // 设置请求体字符集编码
                    inter.setRequestCharsetname(Optional.ofNullable(row.getCell(ExcelField.NORMAL_BODY_CHARSET))
                            .map(format::formatCellValue).orElse(""));

                    return;
                }
            }
        }

        // 若不存在普通请求体方法，则读取表单类型请求体
        if (formBodyMap.containsKey(interName)) {
            // 获取表单请求体sheet页
            Sheet sheet = excel.getSheet(ExcelField.SHEET_FORM_BODY.getKey());
            if (sheet != null) {
                // 获取接口所在行
                Entry<Integer, Integer> indexEntry = formBodyMap.get(interName);
                int startRowIndex = indexEntry.getKey();
                int endRowIndex = indexEntry.getValue();
                // 获取首行的封装类对象
                Optional<Row> startRow = Optional.ofNullable(sheet.getRow(startRowIndex));

                // 获取首行中指定的表单请求类型内容
                MessageType type = startRow.map(row -> row.getCell(ExcelField.FORM_BODY_TYPE))
                        .map(format::formatCellValue).map(typeText -> {
                            try {
                                return MessageType.typeText2Type(typeText);
                            } catch (Exception e) {
                                throw new InterfaceReadToolsException(
                                        String.format("接口“%s”的表单格式文本“%s”无法进行转换", interName, typeText), e);
                            }
                        }).orElseThrow(
                                () -> new InterfaceReadToolsException(String.format("接口“%s”必须指定表单请求类型", interName)));

                // 存储遍历的键值对
                List<Entry<String, Object>> dataList = new ArrayList<>();
                // 遍历所有的行
                for (int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++) {
                    // 获取行对象
                    Row row = sheet.getRow(rowIndex);
                    // 若该行对象为空，则跳过
                    if (row == null) {
                        continue;
                    }

                    // 获取key值
                    String keyContent = Optional.ofNullable(row.getCell(ExcelField.FORM_BODY_KEY))
                            .map(format::formatCellValue).orElse("");
                    // 若key值为空，则跳过该行
                    if (keyContent.isEmpty()) {
                        continue;
                    }

                    // 获取value值
                    Object valueContent = Optional.ofNullable(row.getCell(ExcelField.FORM_BODY_VALUE))
                            .map(format::formatCellValue).map(content -> {
                                // 判断value是否为文件类型，即是否开头与结尾是特殊符号，若是，则按照文件类型进行返回
                                if (content.indexOf(SPECIAL_CONTENT_SYMBOL) == 0
                                        && content.lastIndexOf(SPECIAL_CONTENT_SYMBOL) == content.length() - 1) {
                                    String filePath = content.substring(
                                            content.indexOf(SPECIAL_CONTENT_SYMBOL) + SPECIAL_CONTENT_SYMBOL.length(),
                                            content.lastIndexOf(SPECIAL_CONTENT_SYMBOL));

                                    return new File(filePath);
                                }

                                return content;
                            }).orElse("");

                    dataList.add(new Entry<>(keyContent, valueContent));
                }

                // 读取所有行后，若集合不为空，则对接口进行设置
                if (!dataList.isEmpty()) {
                    inter.setFormBody(type, dataList);
                    // 设置请求体字符集编码
                    inter.setRequestCharsetname(startRow.map(row -> row.getCell(ExcelField.NORMAL_BODY_CHARSET))
                            .map(format::formatCellValue).orElse(""));
                    return;
                }
            }
        }

        // 若不存在表单类型请求体，则按照文件类型请求体进行读取
        if (fileBodyMap.containsKey(interName)) {
            // 获取文件请求体sheet页
            Sheet sheet = excel.getSheet(ExcelField.SHEET_FILE_BODY.getKey());
            if (sheet != null) {
                // 获取文件路径，由于文件请求体只获取单一一行，故只获取其起始行下标
                Optional<File> file = Optional.ofNullable(sheet.getRow(fileBodyMap.get(interName).getKey()))
                        .map(row -> row.getCell(ExcelField.FILE_BODY_PATH)).map(format::formatCellValue).map(File::new);
                // 若能转换为文件类对象，则对接口设置文件类型请求体
                if (file.isPresent()) {
                    inter.setBodyContent(MessageType.FILE, file.get());
                    return;
                }
            }
        }

        // 若所有请求体内容未通过判断，则添加请求体为none类型
        inter.setBodyContent(MessageType.NONE, "");
    }

    /**
     * 该方法用于读取一个接口占多行的sheet页内容
     *
     * @param interName 接口名称
     * @param sheetName sheet页名称
     * @param inter     接口信息类对象
     * @since autest 3.7.0
     */
    protected void readMultirowSheetContent(String interName, String sheetName, InterfaceInfo inter,
            HashMap<String, Entry<Integer, Integer>> map) {
        // 若接口名称不在索引集合中，则不进行获取操作
        if (!map.containsKey(interName)) {
            return;
        }

        // 获取sheet类对象
        Sheet sheet = excel.getSheet(sheetName);

        // 获取接口所在行
        Entry<Integer, Integer> indexEntry = map.get(interName);
        int startRowIndex = indexEntry.getKey();
        int endRowIndex = indexEntry.getValue();

        // 遍历所有的行
        for (int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++) {
            // 获取行对象
            Row row = sheet.getRow(rowIndex);
            // 根据sheet名称，将其分配到不同的方法中
            if (sheetName.equals(ExcelField.SHEET_INTER_PARAM.getKey())) {
                readInterParamSheetContent(row, inter);
                continue;
            }
            if (sheetName.equals(ExcelField.SHEET_REQUEST_HEADER.getKey())) {
                readRequestHeaderSheetContent(row, inter);
                continue;
            }
            if (sheetName.equals(ExcelField.SHEET_COOKIE.getKey())) {
                readCookieSheetContent(row, inter);
                continue;
            }
            if (sheetName.equals(ExcelField.SHEET_BEFORE_INTER_OPEARTE.getKey())) {
                readBeforeInterOpeateSheetContent(row, inter);
                continue;
            }
            if (sheetName.equals(ExcelField.SHEET_RESPONSE.getKey())) {
                // 若当前行为首行，则设置其响应字符集
                if (rowIndex == startRowIndex) {
                    inter.setResponseCharsetname(Optional.ofNullable(row.getCell(ExcelField.RESPONSE_RESPONSE_CHARSET))
                            .map(format::formatCellValue).orElse(""));
                }
                readResponseSheetContent(row, inter);
                continue;
            }
            if (sheetName.equals(ExcelField.SHEET_ASSERT.getKey())) {
                readAssertSheetContent(row, inter);
                continue;
            }
            if (sheetName.equals(ExcelField.SHEET_EXTRACT.getKey())) {
                readExtractSheetContent(row, inter);
                continue;
            }
        }
    }

    /**
     * 该方法用于读取接口提词sheet页内容
     *
     * @param row   行对象
     * @param inter 接口信息类对象
     * @since autest 3.7.0
     */
    private void readExtractSheetContent(Row row, InterfaceInfo inter) {
        // 获取提词保存名称
        String saveName = Optional.ofNullable(row).map(r -> r.getCell(ExcelField.EXTRACT_SAVE_NAME))
                .map(format::formatCellValue)
                .orElse("");
        // 若提词保存名称为空，则直接返回
        if (saveName.isEmpty()) {
            return;
        }

        // 通过判断后，则对断言json串进行存储
        JSONObject extractJson = new JSONObject();
        extractJson.put(ExtractResponse.JSON_EXTRACT_SAVE_NAME, saveName);
        extractJson.put(ExtractResponse.JSON_EXTRACT_SEARCH,
                Optional.ofNullable(row.getCell(ExcelField.EXTRACT_SEARCH)).map(format::formatCellValue).orElse(""));
        extractJson.put(ExtractResponse.JSON_EXTRACT_LB,
                Optional.ofNullable(row.getCell(ExcelField.EXTRACT_LB)).map(format::formatCellValue).orElse(""));
        extractJson.put(ExtractResponse.JSON_EXTRACT_RB,
                Optional.ofNullable(row.getCell(ExcelField.EXTRACT_RB)).map(format::formatCellValue).orElse(""));
        extractJson.put(ExtractResponse.JSON_EXTRACT_PARAM_NAME, Optional
                .ofNullable(row.getCell(ExcelField.EXTRACT_PARAM_NAME)).map(format::formatCellValue).orElse(""));
        extractJson.put(ExtractResponse.JSON_EXTRACT_XPATH,
                Optional.ofNullable(row.getCell(ExcelField.EXTRACT_XPATH)).map(format::formatCellValue).orElse(""));
        extractJson.put(ExtractResponse.JSON_EXTRACT_ORD,
                Optional.ofNullable(row.getCell(ExcelField.EXTRACT_ORD)).map(format::formatCellValue).orElse(""));

        inter.addExtractRule(extractJson.toJSONString());

    }

    /**
     * 该方法用于读取接口断言sheet页内容
     *
     * @param row   行对象
     * @param inter 接口信息类对象
     * @since autest 3.7.0
     */
    private void readAssertSheetContent(Row row, InterfaceInfo inter) {
        // 获取断言规则内容
        String assertRegex = Optional.ofNullable(row).map(r -> r.getCell(ExcelField.ASSERT_REGEX))
                .map(format::formatCellValue).orElse("");
        // 若断言规则为空，则直接返回
        if (assertRegex.isEmpty()) {
            return;
        }

        // 通过判断后，则对断言json串进行存储
        JSONObject assertJson = new JSONObject();
        assertJson.put(AssertResponse.JSON_ASSERT_ASSERT_REGEX, assertRegex);
        assertJson.put(AssertResponse.JSON_ASSERT_SEARCH,
                Optional.ofNullable(row.getCell(ExcelField.ASSERT_SEARCH))
                        .map(format::formatCellValue).orElse(""));
        assertJson.put(AssertResponse.JSON_ASSERT_LB,
                Optional.ofNullable(row.getCell(ExcelField.ASSERT_LB))
                .map(format::formatCellValue).orElse(""));
        assertJson.put(AssertResponse.JSON_ASSERT_RB,
                Optional.ofNullable(row.getCell(ExcelField.ASSERT_RB))
                .map(format::formatCellValue).orElse(""));
        assertJson.put(AssertResponse.JSON_ASSERT_PARAM_NAME, Optional.ofNullable(row.getCell(ExcelField.ASSERT_PARAM_NAME))
                .map(format::formatCellValue).orElse(""));
        assertJson.put(AssertResponse.JSON_ASSERT_XPATH,
                Optional.ofNullable(row.getCell(ExcelField.ASSERT_XPATH))
                .map(format::formatCellValue).orElse(""));
        assertJson.put(AssertResponse.JSON_ASSERT_ORD, Optional.ofNullable(row.getCell(ExcelField.ASSERT_ORD))
                .map(format::formatCellValue).orElse(""));

        inter.addAssertRule(assertJson.toJSONString());
    }

    /**
     * 该方法用于读取接口响应sheet页内容
     *
     * @param row   行对象
     * @param inter 接口信息类对象
     * @since autest 3.7.0
     */
    private void readResponseSheetContent(Row row, InterfaceInfo inter) {
        // 获取当前状态码，若状态码为空或不能转换为数字，则返回-1
        int state = Optional.ofNullable(row).map(r -> r.getCell(ExcelField.RESPONSE_STATE)).map(format::formatCellValue)
                .map(num -> {
                    try {
                        return Integer.valueOf(num);
                    } catch (Exception e) {
                        return -1;
                    }
                }).orElse(-1);
        // 若状态码为-1，则不进行存储操作
        if (state == -1) {
            return;
        }

        // 存储接口响应数据
        inter.addResponseContentTypeSet(state,
                MessageType.typeText2Type(format.formatCellValue(row.getCell(ExcelField.RESPONSE_TYPE))));
    }

    /**
     * 该方法用于读取前置接口sheet页的接口信息
     *
     * @param row   行对象
     * @param inter 接口信息类对象
     * @since autest 3.7.0
     */
    private void readBeforeInterOpeateSheetContent(Row row, InterfaceInfo inter) {
        Optional<Row> ro = Optional.ofNullable(row);
        // 获取单元格中存储的父层接口名称
        String parentName = ro.map(r -> r.getCell(ExcelField.BEFORE_INTER_OPEARTE_BEFORE_INTER_NAME))
                .map(format::formatCellValue).orElse("");
        // 若名称不为空，则进行获取接口的操作
        if (parentName.isEmpty()) {
            return;
        }
        BeforeInterfaceOperation beforeInterfaceOperation = new BeforeInterfaceOperation(parentName,
                getInterface(parentName));

        // 获取并设置前置接口的执行次数
        int count = ro.map(r -> r.getCell(ExcelField.BEFORE_INTER_OPEARTE_ACTION_COUNT))
                .map(format::formatCellValue).filter(num -> num.matches(RegexType.INTEGER.getRegex()))
                .map(Integer::valueOf).orElse(0);
        beforeInterfaceOperation.setActionCount(count);

        inter.addBeforeOperation(beforeInterfaceOperation);
    }

    /**
     * 该方法用于接口cookie sheet页内容
     *
     * @param row   行对象
     * @param inter 接口信息类对象
     * @since autest 3.7.0
     */
    private void readCookieSheetContent(Row row, InterfaceInfo inter) {
        // 获取键内容
        String key = Optional.ofNullable(row).map(r -> r.getCell(ExcelField.COOKIE_KEY)).map(format::formatCellValue)
                .orElse("");
        // 若键为空，则直接返回
        if (key.isEmpty()) {
            return;
        }

        // 获取值内容，并进行存储
        inter.addCookie(key,
                Optional.ofNullable(row.getCell(ExcelField.COOKIE_VALUE)).map(format::formatCellValue).orElse(""));
    }

    /**
     * 该方法用于读取接口请求头sheet页内容
     *
     * @param row   行对象
     * @param inter 接口信息类对象
     * @since autest 3.7.0
     */
    private void readRequestHeaderSheetContent(Row row, InterfaceInfo inter) {
        // 获取键内容
        String key = Optional.ofNullable(row).map(r -> r.getCell(ExcelField.REQUEST_HEADER_KEY))
                .map(format::formatCellValue)
                .orElse("");
        // 若键为空，则直接返回
        if (key.isEmpty()) {
            return;
        }

        // 获取值内容，并进行存储
        inter.addRequestHeader(key, Optional.ofNullable(row.getCell(ExcelField.REQUEST_HEADER_VALUE))
                .map(format::formatCellValue).orElse(""));
    }

    /**
     * 该方法用于读取接口参数sheet页内容
     *
     * @param row   行对象
     * @param inter 接口信息对象
     * @since autest 3.7.0
     */
    private void readInterParamSheetContent(Row row, InterfaceInfo inter) {
        // 获取键内容
        String key = Optional.ofNullable(row).map(r -> r.getCell(ExcelField.INTER_PARAM_KEY))
                .map(format::formatCellValue)
                .orElse("");
        // 若键为空，则直接返回
        if (key.isEmpty()) {
            return;
        }

        // 获取值内容，并进行存储
        inter.addParam(key,
                Optional.ofNullable(row.getCell(ExcelField.INTER_PARAM_VALUE)).map(format::formatCellValue).orElse(""));
    }

    /**
     * 该方法用于读取“接口”sheet页中接口相关的基本信息
     *
     * @param interName 接口名称
     * @param inter     接口信息类对象
     * @since autest 3.7.0
     */
    protected void readInterSheetContent(String interName, InterfaceInfo inter) {
        // 获取接口所在行
        Row interRow = excel.getSheet(ExcelField.SHEET_INTER.getKey()).getRow(interMap.get(interName).getValue());

        // 读取url数据
        inter.analysisUrl(format.formatCellValue(interRow.getCell(ExcelField.INTER_URL)));
        // 读取请求方式数据
        String requestTypeText = format.formatCellValue(interRow.getCell(ExcelField.INTER_TYPE)).toUpperCase();
        if (!requestTypeText.isEmpty()) {
            inter.setRequestType(RequestType.typeText2Type(requestTypeText));
        }

        // 读取接口超时时间
        inter.setConnectTime(format.formatCellValue(interRow.getCell(ExcelField.INTER_CONNECT_TIME)));
    }

    /**
     * 该方法用于关闭对接口模板的读取
     *
     * @since autest 3.7.0
     */
    public void close() {
        try {
            excel.close();
        } catch (IOException e) {
            throw new InterfaceReadToolsException("Excel文件读取异常，无法关闭", e);
        }
    }

    /**
     * 该方法用于获取当前sheet中所有行对象的集合
     *
     * @param sheet sheet对象
     * @return 当前sheet所有行对象集合
     * @since autest 3.7.0
     */
    protected List<Row> getSheetAllRow(Sheet sheet) {
        int rowLastIndex = sheet.getLastRowNum();

        // 若当前sheet中的最后一行下标为0时，则表示其只存在一行（标题行）或无内容，则直接返回空集合
        if (rowLastIndex == 0) {
            return new ArrayList<>();
        }

        // 遍历所有的行，并将其存储至集合中
        return IntStream.range(1, rowLastIndex + 1)
                // 转换下标为行对象，并进行存储
                .mapToObj(index -> sheet.getRow(index)).collect(Collectors.toList());
    }

    /**
     * 该方法用于获取当前行中所有单元格对象的集合
     *
     * @param row         行对象
     * @param columnIndex 当前行下的列数
     * @return 当前行中所有单元格对象的集合
     * @since autest 3.7.0
     */
    protected List<Optional<Cell>> getRowAllCell(Row row, int columnIndex) {
        if (row == null) {
            return new ArrayList<>();
        }

        // 遍历所有的行，并将其存储至集合中
        return IntStream.range(0, columnIndex)
                // 转换下标为行对象，并进行存储
                .mapToObj(index -> Optional.ofNullable(row.getCell(index))).collect(Collectors.toList());
    }

    /**
     * 该方法用于将sheet所有行中指定下标单元格中不为空的内容进行存储，并记录其内容及行号至指定的map集合中
     *
     * @param map        存储内容及下标的集合
     * @param fieldIndex 指定字段所在的列的下标
     * @param dataSheet  数据sheet页
     * @since autest 3.7.0
     */
    protected void addInterNameRowIndex(HashMap<String, Entry<Integer, Integer>> map, int fieldIndex, Sheet dataSheet) {
        // 获取当前sheet的所有行，并遍历
        List<Row> rowList = getSheetAllRow(dataSheet);

        int startIndex = 0;
        String saveContent = "";
        for (int index = 0; index < rowList.size(); index++) {
            // 获取指定该行指定单元格中的内容
            String content = Optional.ofNullable(rowList.get(index)).map(row -> row.getCell(fieldIndex))
                    .map(Cell::getStringCellValue).orElse("");
            // 若内容不为空串，则在指定的map中进行存储
            if (!content.isEmpty()) {
                int rowIndex = rowList.get(index).getRowNum();

                // 判断当前是否有存储内容，若存在需要存储的内容，则记录其结束下标
                if (!saveContent.isEmpty()) {
                    map.put(saveContent, new Entry<>(startIndex, rowIndex - 1));
                }

                // 存储当前内容，并将开始下标置为当前行所在下标
                saveContent = content;
                startIndex = rowIndex;
            }
        }

        // 若存在需要保存的内容，则将最后一行的下标进行存储
        if (!saveContent.isEmpty()) {
            map.put(saveContent, new Entry<>(startIndex, dataSheet.getLastRowNum()));
        }
    }

    /**
     * <p>
     * <b>文件名：ReadInterfaceFromExcel.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义excel模板中需要使用的字段及其所在位置
     * </p>
     * <p>
     * <b>编码时间：2022年9月13日 上午8:15:49
     * </p>
     * <p>
     * <b>修改时间：2022年9月13日 上午8:15:49
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.6.0
     */
    protected static class ExcelField {
        /**
         * 环境sheet
         */
        public static final Entry<String, Integer> SHEET_ENV = new Entry<>("环境", 2);
        /**
         * 接口sheet
         */
        public static final Entry<String, Integer> SHEET_INTER = new Entry<>("接口", 5);
        /**
         * 接口参数sheet
         */
        public static final Entry<String, Integer> SHEET_INTER_PARAM = new Entry<>("接口参数", 3);
        /**
         * 前置接口操作sheet
         */
        public static final Entry<String, Integer> SHEET_BEFORE_INTER_OPEARTE = new Entry<>("前置接口操作", 2);
        /**
         * 普通类型请求体sheet
         */
        public static final Entry<String, Integer> SHEET_NORMAL_BODY = new Entry<>("普通类型请求体", 4);
        /**
         * 文件类型请求体sheet
         */
        public static final Entry<String, Integer> SHEET_FILE_BODY = new Entry<>("文件类型请求体", 2);
        /**
         * 表单类型请求体sheet
         */
        public static final Entry<String, Integer> SHEET_FORM_BODY = new Entry<>("表单类型请求体", 5);
        /**
         * 接口请求头sheet
         */
        public static final Entry<String, Integer> SHEET_REQUEST_HEADER = new Entry<>("接口请求头", 3);
        /**
         * 接口cookiesheet
         */
        public static final Entry<String, Integer> SHEET_COOKIE = new Entry<>("接口cookie", 3);
        /**
         * 接口响应sheet
         */
        public static final Entry<String, Integer> SHEET_RESPONSE = new Entry<>("接口响应", 4);
        /**
         * 接口断言sheet
         */
        public static final Entry<String, Integer> SHEET_ASSERT = new Entry<>("接口断言", 8);
        /**
         * 接口提词sheet
         */
        public static final Entry<String, Integer> SHEET_EXTRACT = new Entry<>("接口提词", 8);

        /**
         * 环境sheet中的环境名称
         */
        public static final int ENV_ENV_NAME = 0;
        /**
         * 环境sheet中的环境url
         */
        public static final int ENV_URL = 1;

        /**
         * 接口sheet中的接口名称
         */
        public static final int INTER_INTER_NAME = 0;
        /**
         * 接口sheet中的接口url
         */
        public static final int INTER_URL = 1;
        /**
         * 接口sheet中的接口请求方式
         */
        public static final int INTER_TYPE = 2;
        /**
         * 接口sheet中的接口连接时间
         */
        public static final int INTER_CONNECT_TIME = 3;
        /**
         * 接口sheet中的接口环境
         */
        public static final int INTER_ENV = 4;

        /**
         * 接口参数sheet中的接口名称
         */
        public static final int INTER_PARAM_INTER_NAME = 0;
        /**
         * 接口参数sheet中的键
         */
        public static final int INTER_PARAM_KEY = 1;
        /**
         * 接口参数sheet中的值
         */
        public static final int INTER_PARAM_VALUE = 2;

        /**
         * 前置接口操作sheet中的接口名称
         */
        public static final int BEFORE_INTER_OPEARTE_INTER_NAME = 0;
        /**
         * 前置接口操作sheet中的前置接口名称
         */
        public static final int BEFORE_INTER_OPEARTE_BEFORE_INTER_NAME = 1;
        /**
         * 前置接口操作sheet中的执行次数名称
         *
         * @since 4.3.0
         */
        public static final int BEFORE_INTER_OPEARTE_ACTION_COUNT = 2;

        /**
         * 普通请求体sheet中的接口名称
         */
        public static final int NORMAL_BODY_INTER_NAME = 0;
        /**
         * 普通请求体sheet中的请求体格式
         */
        public static final int NORMAL_BODY_TYPE = 1;
        /**
         * 普通类型请求体sheet中的字符集编码
         */
        public static final int NORMAL_BODY_CHARSET = 2;
        /**
         * 普通类型请求体sheet中的请求体内容
         */
        public static final int NORMAL_BODY_CONTENT = 3;

        /**
         * 文件类型体sheet中的接口名称
         */
        public static final int FILE_BODY_INTER_NAME = 0;
        /**
         * 文件类型体sheet中的文件路径
         */
        public static final int FILE_BODY_PATH = 1;

        /**
         * 表单类型体sheet中的接口名称
         */
        public static final int FORM_BODY_INTER_NAME = 0;
        /**
         * 表单类型体sheet中的表单请求类型
         */
        public static final int FORM_BODY_TYPE = 1;
        /**
         * 普通类型请求体sheet中的字符集编码
         */
        public static final int FORM_BODY_CHARSET = 2;
        /**
         * 表单类型体sheet中的键
         */
        public static final int FORM_BODY_KEY = 3;
        /**
         * 表单类型体sheet中的值
         */
        public static final int FORM_BODY_VALUE = 4;

        /**
         * 请求头sheet中的接口名称
         */
        public static final int REQUEST_HEADER_INTER_NAME = 0;
        /**
         * 请求头sheet中的键
         */
        public static final int REQUEST_HEADER_KEY = 1;
        /**
         * 请求头sheet中的值
         */
        public static final int REQUEST_HEADER_VALUE = 2;

        /**
         * Cookie sheet中的接口名称
         */
        public static final int COOKIE_INTER_NAME = 0;
        /**
         * Cookie sheet中的键
         */
        public static final int COOKIE_KEY = 1;
        /**
         * Cookie sheet中的值
         */
        public static final int COOKIE_VALUE = 2;

        /**
         * 接口响应sheet中的接口名称
         */
        public static final int RESPONSE_INTER_NAME = 0;
        /**
         * 接口响应sheet中的响应字符集
         */
        public static final int RESPONSE_RESPONSE_CHARSET = 1;
        /**
         * 接口响应sheet中的响应状态
         */
        public static final int RESPONSE_STATE = 2;
        /**
         * 接口响应sheet中的响应体格式
         */
        public static final int RESPONSE_TYPE = 3;

        /**
         * 接口断言sheet中的接口名称
         */
        public static final int ASSERT_INTER_NAME = 0;
        /**
         * 接口断言sheet中的断言规则
         */
        public static final int ASSERT_REGEX = 1;
        /**
         * 接口断言sheet中的搜索范围
         */
        public static final int ASSERT_SEARCH = 2;
        /**
         * 接口断言sheet中的搜索参数名称
         */
        public static final int ASSERT_PARAM_NAME = 3;
        /**
         * 接口断言sheet中的xpath
         */
        public static final int ASSERT_XPATH = 4;
        /**
         * 接口断言sheet中的搜索左边界
         */
        public static final int ASSERT_LB = 5;
        /**
         * 接口断言sheet中的搜索右边界
         */
        public static final int ASSERT_RB = 6;
        /**
         * 接口断言sheet中的读取下标
         */
        public static final int ASSERT_ORD = 7;

        /**
         * 接口提词sheet中的接口名称
         */
        public static final int EXTRACT_INTER_NAME = 0;
        /**
         * 接口提词sheet中的提词保存名称
         */
        public static final int EXTRACT_SAVE_NAME = 1;
        /**
         * 接口提词sheet中的搜索范围
         */
        public static final int EXTRACT_SEARCH = 2;
        /**
         * 接口提词sheet中的搜索参数名称
         */
        public static final int EXTRACT_PARAM_NAME = 3;
        /**
         * 接口提词sheet中的xpath
         */
        public static final int EXTRACT_XPATH = 4;
        /**
         * 接口提词sheet中的搜索左边界
         */
        public static final int EXTRACT_LB = 5;
        /**
         * 接口提词sheet中的搜索右边界
         */
        public static final int EXTRACT_RB = 6;
        /**
         * 接口提词sheet中的读取下标
         */
        public static final int EXTRACT_ORD = 7;
    }
}
