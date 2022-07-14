package com.auxiliary.tool.common;

import com.auxiliary.AuxiliaryToolsException;
import com.auxiliary.tool.common.enums.MathematicsSymbolType;

/**
 * <p>
 * <b>文件名：IndexException.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 当数组下标处理出错时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2022年7月7日 上午8:21:07
 * </p>
 * <p>
 * <b>修改时间：2022年7月7日 上午8:21:07
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.5.0
 */
public class DisposeNumberException extends AuxiliaryToolsException {
    private static final long serialVersionUID = 1L;

    public DisposeNumberException(String message) {
        super(message);
    }
    
    /**
     * 该方法用于对两数字比较，并在比较失败时抛出异常
     * 
     * @param numName               比较数字的名称
     * @param num                   待比较数字
     * @param compareName           被比较数字的名称
     * @param conpareNum            被比较的数据
     * @param mathematicsSymbolType 数学符号枚举
     * @since autest 3.5.0
     */
    public static void compareNumber(String numName, int num, String compareName,
            int conpareNum, MathematicsSymbolType mathematicsSymbolType) {
        // 根据逻辑表达式枚举，存储判断结果
        boolean isThrowException = false;
        switch (mathematicsSymbolType) {
        case LESS:
            isThrowException = num < conpareNum;
            break;
        case LESS_AND_EQUALS:
            isThrowException = num <= conpareNum;
            break;
        case GREATER:
            isThrowException = num > conpareNum;
            break;
        case GREATER_AND_EQUALS:
            isThrowException = num >= conpareNum;
            break;
        case EQUALS:
            isThrowException = num == conpareNum;
            break;
        case NOT_EQUALS:
            isThrowException = num != conpareNum;
            break;
        default:
            break;
        }

        // 若判断不通过，则抛出该异常
        if (isThrowException) {
            throw new DisposeNumberException(String.format("%s“%d”不能%s%s“%d”", numName, num,
                    mathematicsSymbolType.getName(), compareName, conpareNum));
        }
    }
}
