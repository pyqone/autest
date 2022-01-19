package com.auxiliary.sikuli.event;

import java.util.Optional;

import com.auxiliary.selenium.tool.ActionLogRecord;
import com.auxiliary.sikuli.element.FindSikuliElement;

/**
 * <p>
 * <b>文件名：</b>SikuliAbstractEvent.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 定义所有sikuli事件类包含的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月19日 上午8:02:37
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月19日 上午8:02:37
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public abstract class SikuliAbstractEvent {
    /**
     * 指向元素查找类，用于根据名称查找元素
     */
    protected FindSikuliElement find;
    /**
     * 指向日志记录类，用于自动记录操作的日志
     */
    protected ActionLogRecord log;

    /**
     * 构造对象
     * @since autest 3.0.0
     */
    public SikuliAbstractEvent() {
        log = new ActionLogRecord();
    }

    /**
     * 该方法用于指定元素查找类对象
     *
     * @param find 元素查找类{@link FindSikuliElement}对象
     * @since autest 3.0.0
     */
    public void setFindElementTool(FindSikuliElement find) {
        this.find = find;
    }

    /**
     * 该方法用于设置记录操作日志的工具，并根据参数，决定是否保留当前已有的日志
     *
     * @param log 日志记录工具类{@link ActionLogRecord}对象
     * @param isSaveOldLog 是否保存原有日志
     * @since autest 3.0.0
     */
    public void setLogRecordTool(ActionLogRecord log, boolean isSaveOldLog) {
        // 判断传入的log对象是否为空
        if (Optional.ofNullable(log).isPresent()) {
            // 判断是否需要保留当前已有的日志
            if (isSaveOldLog && !this.log.isEmpty()) {
                log.recordLog(this.log.getNoSignLogArray());
            }
            this.log = log;
        }
    }

    /**
     * 该方法用于记录日志，并根据指定的数字，在记录前删除多余的日志
     * <p><b>注意：</b>当指定的数字小于等于0时，表示不删除日志</p>
     *
     * @param logText 日志文本
     * @param removeLogNum 需要移除的日志数量
     * @since autest 3.0.0
     */
    public void recordLog(String logText, int removeLogNum) {
        if (removeLogNum > 0) {
            log.removeLog(removeLogNum);
        }

        log.recordLog(logText);
    }
}
