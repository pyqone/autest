package com.auxiliary.tool.file.excel;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.auxiliary.tool.file.FileSuffixType;

/**
 * <p>
 * <b>文件名：ExcelUtil.java</b>
 * </p>
 * <p>
 * <b>用途：</b>对Excel文件后期处理的工具类
 * </p>
 * <p>
 * <b>编码时间：2022年12月5日 上午8:18:07
 * </p>
 * <p>
 * <b>修改时间：2022年12月5日 上午8:18:07
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.8.0
 */
public class ExcelUtil {
    /**
     * 指向xlsx文件后缀名
     */
    public static final String FILE_SUFFIX_XLSX = ".xlsx";
    /**
     * 指向xls文件后缀名
     */
    public static final String FILE_SUFFIX_XLS = ".xls";

    /**
     * 该方法用于读取excel文件，并返回{@link Workbook}类对象
     * 
     * @param excelFile excel文件类对象
     * @return {@link Workbook}类对象
     * @since autest 3.8.0
     */
    public static Workbook readExcelFile(File excelFile) {
        // 定义excel类对象
        Workbook excel = null;
        // 读取excel文件，将其转换为excel对象
        try (FileInputStream fis = new FileInputStream(excelFile)) {
            // 判断文件后缀，并根据后缀判断excel文件的版本
            switch (FileSuffixType.typeText2Type(excelFile.getName())) {
            case XLS:
                excel = new HSSFWorkbook(fis);
                break;
            case XLSX:
                excel = new XSSFWorkbook(fis);
                break;
            default:
                break;
            }
        } catch (Exception e) {
        }

        return excel;
    }
}
