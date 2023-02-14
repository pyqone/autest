package com.auxiliary.testcase.templet;

/**
 * <p>
 * <b>文件名：StepDetailTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义读取步骤详情的基本方法，并标识模板读取类可以读取详细的用例步骤
 * </p>
 * <p>
 * <b>编码时间：2023年1月17日 上午8:25:25
 * </p>
 * <p>
 * <b>修改时间：2023年1月17日 上午8:25:25
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public interface StepDetailTemplet {
    /**
     * 该方法用于设置返回的用例步骤样式，并设置每组步骤是否作为独立的用例生成
     * <p>
     * 例如，当前用例有2个步骤，每个步骤有3条步骤详情，则：
     * <ul>
     * <li>当两个参数均为true时：输出2条测试用例，每条测试用例包含3个步骤；</li>
     * <li>当两个参数均为false时：输出1条测试用例，每条测试用例包含2个步骤；</li>
     * <li>当isStepDetail为true，isStepIndependentCase为false时：输出1条测试用例，每条测试用例包含6个步骤</li>
     * <li>当isStepDetail为false，isStepIndependentCase为true时：输出2条测试用例，每条测试用例包含1个步骤</li>
     * </ul>
     * </p>
     * <p>
     * 
     * @param isStepDetail          读取的步骤样式
     * @param isStepIndependentCase 每组步骤详情是否单独作为一条用例
     * @since autest 4.0.0
     */
    public void setReadStepDetail(boolean isStepDetail, boolean isStepIndependentCase);
}
