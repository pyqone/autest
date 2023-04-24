package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：VideoCaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>生成与视频播放相关的测试用例
 * </p>
 * <p>
 * <b>编码时间：2023年4月24日 上午8:12:04
 * </p>
 * <p>
 * <b>修改时间：2023年4月24日 上午8:12:04
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.1.0
 */
public class VideoCaseTemplet extends AbstractPresetCaseTemplet implements StepDetailTemplet {
    /**
     * 构造对象，并指定读取的模板xml文件
     * 
     * @param xmlTempletFile 用例模板文件类对象
     * @param videoName      视频名称
     * @since autest 4.1.0
     */
    public VideoCaseTemplet(File xmlTempletFile, String videoName) {
        super(xmlTempletFile);
        addReplaceWord(VideoCaseTempletReplaceWord.VIDEO_NAME, videoName);
    }

    /**
     * 构造对象，通过包内的默认模板，对类进行构造
     * 
     * @param videoName 视频名称
     * @since autest 4.1.0
     */
    public VideoCaseTemplet(String videoName) {
        super("VideoCaseTemplet");
        addReplaceWord(VideoCaseTempletReplaceWord.VIDEO_NAME, videoName);
    }

    @Override
    public void setReadStepDetail(boolean isStepDetail, boolean isStepIndependentCase) {
        this.isStepDetail = isStepDetail;
        this.isStepIndependentCase = isStepIndependentCase;
    }

    /**
     * 该方法用于生成视频播放相关的测试用例
     * 
     * @param isSpace       是否可通过键盘空格键操作视频暂停/播放
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> playVideoCase(boolean isSpace) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { VideoCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_PLAY_VIDEO_CASE, new String[] { "1" })));

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_PLAY_VIDEO_CASE, new String[] { "1" })));

        // 添加默认步骤
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_PLAY_VIDEO_CASE,
                        new String[] { "1", "2", "3", "4", "5" })));

        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_PLAY_VIDEO_CASE,
                        new String[] { "1", "2", "3", "4", "5" })));

        // 添加按住空格切换视频暂停或播放的用例
        if (isSpace) {
            // 步骤
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_PLAY_VIDEO_CASE, new String[] { "6" })));
            // 预期
            addContent(allContentMap, LabelType.EXPECT,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_PLAY_VIDEO_CASE, new String[] { "6" })));
        }

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成对视频进行截图相关的测试用例
     * 
     * @param isNoPalyScreeenshot 是否允许未播放时截图
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> videoScreenshotCase(boolean isNoPalyScreeenshot) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(
                        new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SCREENSHOT_CASE, new String[] { "1" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { VideoCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SCREENSHOT_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SCREENSHOT_CASE, new String[] { "1", "2", "3" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SCREENSHOT_CASE,
                        new String[] { "1", "1", isNoPalyScreeenshot ? "1" : "2" })));
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成生成视频快进、快退相关的测试用例
     * 
     * @param isInputSec 是否允许输入快进或快退秒数
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> videoAdvanceCase(boolean isInputSec) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_ADVANCE_CASE, new String[] { "1" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { VideoCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_ADVANCE_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_ADVANCE_CASE,
                        new String[] { "1", "2", "3", "4", "5" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_ADVANCE_CASE,
                        new String[] { "1", "2", "3", "4", "5" })));
        
        // 添加允许输入快进或快退秒数的用例
        if (isInputSec) {
            // 步骤
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_ADVANCE_CASE,
                            new String[] { "6", "7", "8", "9" })));

            // 预期
            addContent(allContentMap, LabelType.EXPECT,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_ADVANCE_CASE,
                            new String[] { "6", "7", "8", "9" })));
        }

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成生成视频快放、慢放相关的测试用例
     * 
     * @param isSelect 是否允许用户选择倍率
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> videoSpeedCase(boolean isSelect) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SPEED_CASE, new String[] { "1" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { VideoCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SPEED_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SPEED_CASE,
                        new String[] { "1", "2", "3", "4", "5" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SPEED_CASE,
                        new String[] { "1", "1", "1", "2", "2" })));

        // 添加允许选择播放速度用例
        if (isSelect) {
            // 步骤
            addContent(allContentMap, LabelType.STEP,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SPEED_CASE, new String[] { "6" })));
            // 预期
            addContent(allContentMap, LabelType.EXPECT,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SPEED_CASE, new String[] { "3" })));
        }
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成对视频进度条操作相关的测试用例
     * 
     * @param isOperate 进度条是否允许操作
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> videoProgressBarCase(boolean isOperate) {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 关键词
        addContent(allContentMap, LabelType.KEY,
                Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_SPEED_CASE, new String[] { "1" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { VideoCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_PROGRESS_BAR_CASE, new String[] { "1" })));

        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_PROGRESS_BAR_CASE,
                        new String[] { "1", "2", "3", "5" })));

        // 添加进度条是否允许操作时对应的步骤与预期
        if (isOperate) {
            addContent(allContentMap, LabelType.STEP, Arrays.asList(
                    new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_PROGRESS_BAR_CASE, new String[] { "4" })));
            // 预期
            addContent(allContentMap, LabelType.EXPECT,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_PROGRESS_BAR_CASE,
                            new String[] { "1", "2", "3", "1", "4" })));
        } else {
            // 预期
            addContent(allContentMap, LabelType.EXPECT,
                    Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_VIDEO_PROGRESS_BAR_CASE,
                            new String[] { "5", "5", "5", "5" })));
        }

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成视频全屏播放和退出全屏播放操作相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> videoFullScreenPlayCase() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 关键词
        addContent(allContentMap, LabelType.KEY, Arrays
                .asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_FULL_SCREEN_PLAY_CASE, new String[] { "1" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(VideoCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { VideoCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_FULL_SCREEN_PLAY_CASE, new String[] { "1" })));

        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_FULL_SCREEN_PLAY_CASE,
                        new String[] { "1", "2", "3", "4" })));

        // 预期
        addContent(allContentMap, LabelType.EXPECT, Arrays
                .asList(new Entry<>(VideoCaseTempletField.GROUP_ADD_FULL_SCREEN_PLAY_CASE,
                        new String[] { "1", "2", "1", "2" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * <p>
     * <b>文件名：VideoCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记“VideoCaseTemplet”用例模板中存在的用例组名称
     * </p>
     * <p>
     * <b>编码时间：2023年4月24日 上午8:19:17
     * </p>
     * <p>
     * <b>修改时间：2023年4月24日 上午8:19:17
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    public class VideoCaseTempletField {
        public static final String COMMON_CONTENT_RANK_1 = "1";
        public static final String COMMON_CONTENT_RANK_2 = "2";
        public static final String COMMON_CONTENT_RANK_3 = "3";
        public static final String COMMON_CONTENT_RANK_4 = "4";

        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String GROUP_ADD_PLAY_VIDEO_CASE = "addPlayVideoCase";
        public static final String GROUP_ADD_VIDEO_SCREENSHOT_CASE = "addVideoScreenshotCase";
        public static final String GROUP_ADD_VIDEO_ADVANCE_CASE = "addVideoAdvanceCase";
        public static final String GROUP_ADD_VIDEO_SPEED_CASE = "addVideoSpeedCase";
        public static final String GROUP_ADD_VIDEO_PROGRESS_BAR_CASE = "addVideoProgressBarCase";
        public static final String GROUP_ADD_FULL_SCREEN_PLAY_CASE = "addFullScreenPlayCase";
    }

    /**
     * <p>
     * <b>文件名：VideoCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义在默认模板中所有的替换词语
     * </p>
     * <p>
     * <b>编码时间：2023年4月24日 上午8:18:58
     * </p>
     * <p>
     * <b>修改时间：2023年4月24日 上午8:18:58
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    protected class VideoCaseTempletReplaceWord {
        public static final String VIDEO_NAME = "视频名称";
    }
}
