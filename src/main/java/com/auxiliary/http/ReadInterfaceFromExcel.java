package com.auxiliary.http;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：ReadInterfaceFromExcel.java</b>
 * </p>
 * <p>
 * <b>用途：</b>
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
public class ReadInterfaceFromExcel extends ReadInterfaceFromAbstract implements ActionEnvironment {
    /**
     * 定义模板文件
     */
    private Workbook excel = null;

    /**
     * 存储环境数据
     */
    private HashMap<String, String> envMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口数据及接口所在的行号
     */
    private HashMap<String, Entry<Integer, Integer>> interMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口参数
     */
    private HashMap<String, Entry<Integer, Integer>> interParamMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储前置接口操作
     */
    private HashMap<String, Entry<Integer, Integer>> beforeInterMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储普通类型请求体
     */
    private HashMap<String, Entry<Integer, Integer>> normalBodyMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储文件类型请求体
     */
    private HashMap<String, Entry<Integer, Integer>> fileBodyMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储表单类型请求体
     */
    private HashMap<String, Entry<Integer, Integer>> formBodymMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储请求头参数
     */
    private HashMap<String, Entry<Integer, Integer>> requestHeaderMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储cookie参数
     */
    private HashMap<String, Entry<Integer, Integer>> cookieMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储相应内容参数
     */
    private HashMap<String, Entry<Integer, Integer>> reponseMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口断言
     */
    private HashMap<String, Entry<Integer, Integer>> assertMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口提词
     */
    private HashMap<String, Entry<Integer, Integer>> extractMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 当前执行接口的环境
     */
    private String actionEnvironment = "";

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
        addInterNameRowIndex(interMap, ExcelField.INTER_INTER_NAME.getValue(),
                Optional.ofNullable(excel.getSheet(ExcelField.SHEET_INTER.getKey()))
                .orElseThrow(() -> new InterfaceReadToolsException(
                                String.format("文件中不存在“%s”sheet页", ExcelField.SHEET_INTER.getKey()))));
        // 读取接口参数数据
        addInterNameRowIndex(interParamMap, fieldIndex, dataSheet);


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
                    String envName = cellList.get(ExcelField.ENV_ENV_NAME.getValue()).map(Cell::getStringCellValue)
                            .orElse("");
                    if (envName.isEmpty()) {
                        continue;
                    }
                    
                    // 存储当前环境内容，并获取其url
                    envMap.put(envName, cellList.get(ExcelField.ENV_URL.getValue()).map(Cell::getStringCellValue)
                            .orElse(InterfaceInfo.DEFAULT_PROTOCOL + InterfaceInfo.DEFAULT_HOST));

                    // 判断当前是否已存储执行环境
                    if (!isSaveDefaultEnv) {
                        // 判断环境名称第一位字符是否为“#”符号，若存在该符号，则设置已存储默认环境
                        if (envName.indexOf("#") == 0) {
                            isSaveDefaultEnv = true;
                        }
                        // 存储当前环境
                        actionEnvironment = envMap.get(envName);
                    }
                }
            }
        }
        
    }

    @Override
    public InterfaceInfo getInterface(String interName) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setActionEnvironment(String environmentName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getActionEnvironment() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceInfo getInterface(String interName, String environmentName) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 该方法用于获取当前sheet中所有行对象的集合
     * 
     * @param sheet sheet对象
     * @return 当前sheet所有行对象集合
     * @since autest 3.7.0
     */
    private List<Row> getSheetAllRow(Sheet sheet) {
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
    private List<Optional<Cell>> getRowAllCell(Row row, int columnIndex) {
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
    private void addInterNameRowIndex(HashMap<String, Entry<Integer, Integer>> map, int fieldIndex, Sheet dataSheet) {
        // 获取当前sheet的所有行，并遍历
        List<Row> rowList = getSheetAllRow(dataSheet);

        int startIndex = 0;
        String saveContent = "";
        for (int index = 0; index < rowList.size(); index++) {
            // 获取指定该行指定单元格中的内容
            String content = Optional.ofNullable(rowList.get(index).getCell(fieldIndex))
                    .map(Cell::getStringCellValue).orElse("");
            // 若内容不为空串，则在指定的map中进行存储
            if (!content.isEmpty()) {
                int rowIndex = rowList.get(index).getRowNum();
                
                // 判断当前是否有存储内容，若存在需要存储的内容，则记录其结束下标
                if (!saveContent.isEmpty()) {
                    map.put(saveContent, new Entry<>(startIndex, rowIndex));
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
    private static class ExcelField {
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
        public static final Entry<String, Integer> SHEET_NORMAL_BODY = new Entry<>("普通类型请求体", 3);
        /**
         * 文件类型请求体sheet
         */
        public static final Entry<String, Integer> SHEET_FILE_BODY = new Entry<>("文件类型请求体", 2);
        /**
         * 表单类型请求体sheet
         */
        public static final Entry<String, Integer> SHEET_FORM_BODY = new Entry<>("表单类型请求体", 4);
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
        public static final Entry<String, Integer> SHEET_REPONSE = new Entry<>("接口响应", 4);
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
        public static final Entry<String, Integer> ENV_ENV_NAME = new Entry<>(SHEET_ENV.getKey(), 0);
        /**
         * 环境sheet中的环境url
         */
        public static final Entry<String, Integer> ENV_URL = new Entry<>(SHEET_ENV.getKey(), 1);

        /**
         * 接口sheet中的接口名称
         */
        public static final Entry<String, Integer> INTER_INTER_NAME = new Entry<>(SHEET_INTER.getKey(), 0);
        /**
         * 接口sheet中的接口url
         */
        public static final Entry<String, Integer> INTER_URL = new Entry<>(SHEET_INTER.getKey(), 1);
        /**
         * 接口sheet中的接口请求方式
         */
        public static final Entry<String, Integer> INTER_TYPE = new Entry<>(SHEET_INTER.getKey(), 2);
        /**
         * 接口sheet中的接口连接时间
         */
        public static final Entry<String, Integer> INTER_CONNECT_TIME = new Entry<>(SHEET_INTER.getKey(), 3);
        /**
         * 接口sheet中的接口环境
         */
        public static final Entry<String, Integer> INTER_ENV = new Entry<>(SHEET_INTER.getKey(), 4);

        /**
         * 接口参数sheet中的接口名称
         */
        public static final Entry<String, Integer> INTER_PARAM_INTER_NAME = new Entry<>(SHEET_INTER_PARAM.getKey(), 0);
        /**
         * 接口参数sheet中的键
         */
        public static final Entry<String, Integer> INTER_PARAM_KEY = new Entry<>(SHEET_INTER_PARAM.getKey(), 1);
        /**
         * 接口参数sheet中的值
         */
        public static final Entry<String, Integer> INTER_PARAM_VALUE = new Entry<>(SHEET_INTER_PARAM.getKey(), 2);

        /**
         * 前置接口操作sheet中的接口名称
         */
        public static final Entry<String, Integer> BEFORE_INTER_OPEARTE_INTER_NAME = new Entry<>(
                SHEET_BEFORE_INTER_OPEARTE.getKey(), 0);
        /**
         * 前置接口操作sheet中的前置接口名称
         */
        public static final Entry<String, Integer> BEFORE_INTER_OPEARTE_BEFORE_INTER_NAME = new Entry<>(
                SHEET_BEFORE_INTER_OPEARTE.getKey(), 1);

        /**
         * 普通请求体sheet中的接口名称
         */
        public static final Entry<String, Integer> NORMAL_BODY_INTER_NAME = new Entry<>(SHEET_NORMAL_BODY.getKey(), 0);
        /**
         * 普通请求体sheet中的请求体格式
         */
        public static final Entry<String, Integer> NORMAL_BODY_TYPE = new Entry<>(SHEET_NORMAL_BODY.getKey(), 1);
        /**
         * 普通类型请求体sheet中的请求体内容
         */
        public static final Entry<String, Integer> NORMAL_BODY_CONTENT = new Entry<>(SHEET_NORMAL_BODY.getKey(), 2);

        /**
         * 文件类型体sheet中的接口名称
         */
        public static final Entry<String, Integer> FILE_BODY_INTER_NAME = new Entry<>(SHEET_FILE_BODY.getKey(), 0);
        /**
         * 文件类型体sheet中的文件路径
         */
        public static final Entry<String, Integer> FILE_BODY_PATH = new Entry<>(SHEET_FILE_BODY.getKey(), 1);

        /**
         * 表单类型体sheet中的接口名称
         */
        public static final Entry<String, Integer> FORM_BODY_INTER_NAME = new Entry<>(SHEET_FORM_BODY.getKey(), 0);
        /**
         * 表单类型体sheet中的表单请求类型
         */
        public static final Entry<String, Integer> FORM_BODY_TYPE = new Entry<>(SHEET_FORM_BODY.getKey(), 1);
        /**
         * 表单类型体sheet中的键
         */
        public static final Entry<String, Integer> FORM_BODY_KEY = new Entry<>(SHEET_FORM_BODY.getKey(), 2);
        /**
         * 表单类型体sheet中的值
         */
        public static final Entry<String, Integer> FORM_BODY_VALUE = new Entry<>(SHEET_FORM_BODY.getKey(), 3);

        /**
         * 请求头sheet中的接口名称
         */
        public static final Entry<String, Integer> REQUEST_HEADER_INTER_NAME = new Entry<>(
                SHEET_REQUEST_HEADER.getKey(), 0);
        /**
         * 请求头sheet中的键
         */
        public static final Entry<String, Integer> REQUEST_HEADER_KEY = new Entry<>(SHEET_REQUEST_HEADER.getKey(), 1);
        /**
         * 请求头sheet中的值
         */
        public static final Entry<String, Integer> REQUEST_HEADER_VALUE = new Entry<>(SHEET_REQUEST_HEADER.getKey(), 2);

        /**
         * Cookie sheet中的接口名称
         */
        public static final Entry<String, Integer> COOKIE_INTER_NAME = new Entry<>(SHEET_COOKIE.getKey(), 0);
        /**
         * Cookie sheet中的键
         */
        public static final Entry<String, Integer> COOKIE_KEY = new Entry<>(SHEET_COOKIE.getKey(), 1);
        /**
         * Cookie sheet中的值
         */
        public static final Entry<String, Integer> COOKIE_VALUE = new Entry<>(SHEET_COOKIE.getKey(), 2);

        /**
         * 接口响应sheet中的接口名称
         */
        public static final Entry<String, Integer> REPONSE_INTER_NAME = new Entry<>(SHEET_REPONSE.getKey(), 0);
        /**
         * 接口响应sheet中的响应字符集
         */
        public static final Entry<String, Integer> REPONSE_RESPONSE_CHARSET = new Entry<>(SHEET_REPONSE.getKey(), 1);
        /**
         * 接口响应sheet中的响应状态
         */
        public static final Entry<String, Integer> REPONSE_STATE = new Entry<>(SHEET_REPONSE.getKey(), 2);
        /**
         * 接口响应sheet中的响应体格式
         */
        public static final Entry<String, Integer> REPONSE_TYPE = new Entry<>(SHEET_REPONSE.getKey(), 3);

        /**
         * 接口断言sheet中的接口名称
         */
        public static final Entry<String, Integer> ASSERT_INTER_NAME = new Entry<>(SHEET_ASSERT.getKey(), 0);
        /**
         * 接口断言sheet中的断言规则
         */
        public static final Entry<String, Integer> ASSERT_REGEX = new Entry<>(SHEET_ASSERT.getKey(), 1);
        /**
         * 接口断言sheet中的搜索范围
         */
        public static final Entry<String, Integer> ASSERT_SEARCH = new Entry<>(SHEET_ASSERT.getKey(), 2);
        /**
         * 接口断言sheet中的搜索参数名称
         */
        public static final Entry<String, Integer> ASSERT_PARAM_NAME = new Entry<>(SHEET_ASSERT.getKey(), 3);
        /**
         * 接口断言sheet中的xpath
         */
        public static final Entry<String, Integer> ASSERT_XPATH = new Entry<>(SHEET_ASSERT.getKey(), 4);
        /**
         * 接口断言sheet中的搜索左边界
         */
        public static final Entry<String, Integer> ASSERT_LB = new Entry<>(SHEET_ASSERT.getKey(), 5);
        /**
         * 接口断言sheet中的搜索右边界
         */
        public static final Entry<String, Integer> ASSERT_RB = new Entry<>(SHEET_ASSERT.getKey(), 6);
        /**
         * 接口断言sheet中的读取下标
         */
        public static final Entry<String, Integer> ASSERT_ORD = new Entry<>(SHEET_ASSERT.getKey(), 7);
        
        /**
         * 接口提词sheet中的接口名称
         */
        public static final Entry<String, Integer> EXTRACT_INTER_NAME = new Entry<>(SHEET_EXTRACT.getKey(), 0);
        /**
         * 接口提词sheet中的提词规则
         */
        public static final Entry<String, Integer> EXTRACT_REGEX = new Entry<>(SHEET_EXTRACT.getKey(), 1);
        /**
         * 接口提词sheet中的搜索范围
         */
        public static final Entry<String, Integer> EXTRACT_SEARCH = new Entry<>(SHEET_EXTRACT.getKey(), 2);
        /**
         * 接口提词sheet中的搜索参数名称
         */
        public static final Entry<String, Integer> EXTRACT_PARAM_NAME = new Entry<>(SHEET_EXTRACT.getKey(), 3);
        /**
         * 接口提词sheet中的xpath
         */
        public static final Entry<String, Integer> EXTRACT_XPATH = new Entry<>(SHEET_EXTRACT.getKey(), 4);
        /**
         * 接口提词sheet中的搜索左边界
         */
        public static final Entry<String, Integer> EXTRACT_LB = new Entry<>(SHEET_EXTRACT.getKey(), 5);
        /**
         * 接口提词sheet中的搜索右边界
         */
        public static final Entry<String, Integer> EXTRACT_RB = new Entry<>(SHEET_EXTRACT.getKey(), 6);
        /**
         * 接口提词sheet中的读取下标
         */
        public static final Entry<String, Integer> EXTRACT_ORD = new Entry<>(SHEET_EXTRACT.getKey(), 7);
    }
}
