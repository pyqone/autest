package com.auxiliary.tool.file.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
 * @since POI 3.17
 * @since autest 3.8.0
 */
public class ExcelUtil {
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

    /**
     * 该方法用于自上而下对所有单元格进行合并
     * <p>
     * 合并的规则：每列从存在的文本内容的单元格开始，到下次出现文本的单元格结束，合并中间无内容的单元格，例如，某列的第1行单元格存在文本，直到第5行单元格存在文本，则合并1~4行的空单元格
     * </p>
     * <p>
     * <b>注意：</b>当一列单元格无数据时，则不进行合并
     * </p>
     * 
     * @param sheet 工作表对象
     * @since autest 3.8.0
     */
    public static void up2DownMergeCell(Sheet sheet) {
        DataFormatter format = new DataFormatter();
        int rowLastIndex = sheet.getLastRowNum();
        // 定义每列的合并的初始下标
        HashMap<Integer, Integer> columnStartIndexMap = new HashMap<>(16);

        // 遍历所有的行
        for (int rowIndex = 0; rowIndex <= rowLastIndex; rowIndex++) {
            // 获取当前下标指向的行对象，若当前读取的行为空，则跳过读取
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            // 计算当前行包含的列数与map中存储内容的差距，若存在差距，则需要对map进行扩充
            int columnLastIndex = row.getLastCellNum();
            int columnDiff = columnLastIndex - columnStartIndexMap.size();
            if (columnDiff > 0) {
                for (int diffIndex = 0; diffIndex < columnDiff; diffIndex++) {
                    // 列下标均以从0开始的正常排序的数字，故可直接使用columnStartIndexMap.size()进行存储
                    columnStartIndexMap.put(columnStartIndexMap.size(), -1);
                }
            }

            // 遍历列，确认当前列对应行是否有内容
            for (int columnIndex = 0; columnIndex < columnLastIndex; columnIndex++) {
                // 获取当前单元格
                Cell cell = row.getCell(columnIndex);
                // 判断单元格中是否存在内容，若存在内容，则对当前存储的起始行进行对比
                if (!format.formatCellValue(cell).isEmpty()) {
                    // 判断map中是否存储起始行，且起始行是否为当前行减1，若通过判断，则进行合并操作，若未存储，则将当前行作为起始行存储
                    int mergeStartRowIndex = columnStartIndexMap.get(columnIndex);
                    if (mergeStartRowIndex != -1 && mergeStartRowIndex != rowIndex - 1) {
                        CellRangeAddress mergeragex = new CellRangeAddress(mergeStartRowIndex, rowIndex - 1,
                                columnIndex, columnIndex);
                        sheet.addMergedRegion(mergeragex);
                    }
                    // 将该列的当前行作为新的起始行进行存储
                    columnStartIndexMap.put(columnIndex, rowIndex);
                }
            }
        }

        // 处理最后一行数据，将最后一行未合并的数据进行合并
        columnStartIndexMap.forEach((columnIndex, rowStartIndex) -> {
            if (rowStartIndex != -1 && rowStartIndex != sheet.getLastRowNum()) {
                CellRangeAddress mergeragex = new CellRangeAddress(rowStartIndex, rowLastIndex, columnIndex,
                        columnIndex);
                sheet.addMergedRegion(mergeragex);
            }
        });
    }
}
