package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：MapCaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>生成与地图相关测试用例的方法
 * </p>
 * <p>
 * <b>编码时间：2023年4月14日 上午8:01:22
 * </p>
 * <p>
 * <b>修改时间：2023年4月14日 上午8:01:22
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.1.0
 */
public class MapCaseTemplet extends AbstractPresetCaseTemplet implements StepDetailTemplet {
    /**
     * 构造对象，并指定读取的模板xml文件
     * 
     * @param xmlTempletFile 用例模板文件类对象
     * @since autest 4.1.0
     */
    public MapCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    /**
     * 构造对象，通过包内的默认模板，对类进行构造
     * 
     * @since autest 4.1.0
     */
    public MapCaseTemplet() {
        super("MapCaseTemplet");
    }

    @Override
    public void setReadStepDetail(boolean isStepDetail, boolean isStepIndependentCase) {
        this.isStepDetail = isStepDetail;
        this.isStepIndependentCase = isStepIndependentCase;
    }

    /**
     * 该方法用于生成地图测距相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> rangeFindingCase() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { MapCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_RANGE_FINDING_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_RANGE_FINDING_CASE,
                        new String[] { "1", "2", "3", "4", "5", "6" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_RANGE_FINDING_CASE,
                        new String[] { "1", "2", "3", "4", "5", "6" })));
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成地图上单一标记点的测试用例
     * 
     * @param pointName 标记点名称
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> mapPointCase(String pointName) {
        // 替换标记点名称
        addReplaceWord(MapCaseTempletReplaceWord.POINT_NAME, pointName);

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_POINT_CASE, new String[] { "1" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { MapCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_POINT_CASE, new String[] { "1" })));

        // 步骤
        addContent(allContentMap, LabelType.STEP, Arrays
                .asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_POINT_CASE, new String[] { "1", "2", "3" })));

        // 预期
        addContent(allContentMap, LabelType.EXPECT, Arrays
                .asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_POINT_CASE, new String[] { "1", "2", "3" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成对地图定位点搜索相关的测试用例
     * 
     * @param condition   搜索条件
     * @param information 搜索信息
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> mapSearchInformationCase(String condition, String information) {
        // 替换搜索条件
        addReplaceWord(MapCaseTempletReplaceWord.SEARCH_CONDITION, condition);
        // 替换信息
        addReplaceWord(MapCaseTempletReplaceWord.INFORMATION, information);

        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION, Arrays
                .asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_SEARCH_INFORMATION_CASE, new String[] { "1" })));

        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { MapCaseTempletField.COMMON_CONTENT_RANK_1 })));

        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_SEARCH_INFORMATION_CASE, new String[] { "1" })));

        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_SEARCH_INFORMATION_CASE,
                        new String[] { "1", "2", "3", "4" })));

        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_MAP_SEARCH_INFORMATION_CASE,
                        new String[] { "1", "2", "3", "4" })));

        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成与车辆轨迹回放相关的测试用例
     * 
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> carLocusPlaybackCase() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 前置条件
        addContent(allContentMap, LabelType.PRECONDITION,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_CAR_LOCUS_PLAYBACK_CASE,
                        new String[] { "1" })));
        // 优先级
        addContent(allContentMap, LabelType.RANK,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_COMMON_CONTENT,
                        new String[] { MapCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE, Arrays
                .asList(new Entry<>(MapCaseTempletField.GROUP_ADD_CAR_LOCUS_PLAYBACK_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_CAR_LOCUS_PLAYBACK_CASE,
                        new String[] { "1", "2", "3", "4" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_ADD_CAR_LOCUS_PLAYBACK_CASE,
                        new String[] { "1", "2", "3", "4" })));
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * 该方法用于生成在地图绘制图案相关的测试用例
     * 
     * @param signName                     图形标记名称
     * @param isMultiple                   是否允许在地图绘制多个图形
     * @param isExistDoubleClickEndDrawing 是有存在有需要双击才结束绘制的图形
     * @param graphNames                   图形名称组（可传入多个名称，例如矩形、圆形、多边形等）
     * @return 用例数据对象集合
     * @since autest 4.1.0
     */
    public List<CaseData> mapGraphSignCase(String signName, boolean isMultiple, boolean isExistDoubleClickEndDrawing,
            String... graphNames) {
        // 替换图形类型
        addReplaceWord(MapCaseTempletReplaceWord.GRAPH,
                Optional.ofNullable(graphNames).filter(arr -> arr.length != 0).map(arr -> {
                    StringJoiner sj = new StringJoiner("、");
                    for (String name : arr) {
                        sj.add(name);
                    }
                    return sj.toString();
                }).orElse("所有图形"));
        // 替换标记名称
        addReplaceWord(MapCaseTempletReplaceWord.SIGN_NAME, signName);
        
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        
        // 优先级
        addContent(allContentMap, LabelType.RANK, Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_COMMON_CONTENT,
                new String[] { MapCaseTempletField.COMMON_CONTENT_RANK_1 })));
        
        // 标题
        addContent(allContentMap, LabelType.TITLE,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_MAP_GRAPH_SIGN_CASE, new String[] { "1" })));
        
        // 步骤
        addContent(allContentMap, LabelType.STEP,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_MAP_GRAPH_SIGN_CASE,
                        new String[] { "1", "4", "5", "6" })));
        
        // 预期
        addContent(allContentMap, LabelType.EXPECT,
                Arrays.asList(new Entry<>(MapCaseTempletField.GROUP_MAP_GRAPH_SIGN_CASE,
                        new String[] { "1", isMultiple ? "4" : "5", "6", "7" })));

        // 是否需要双击结束绘制图形
        if (isExistDoubleClickEndDrawing) {
            // 步骤
            addContent(allContentMap, LabelType.STEP, Arrays
                    .asList(new Entry<>(MapCaseTempletField.GROUP_MAP_GRAPH_SIGN_CASE, new String[] { "2", "3" })));

            // 预期
            addContent(allContentMap, LabelType.EXPECT, Arrays
                    .asList(new Entry<>(MapCaseTempletField.GROUP_MAP_GRAPH_SIGN_CASE, new String[] { "2", "3" })));
        }
        
        return createCaseDataList(this, allContentMap);
    }

    /**
     * <p>
     * <b>文件名：MapCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记“MapCaseTemplet”用例模板中存在的用例组名称
     * </p>
     * <p>
     * <b>编码时间：2023年4月14日 上午8:27:14
     * </p>
     * <p>
     * <b>修改时间：2023年4月14日 上午8:27:14
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    public class MapCaseTempletField {
        public static final String COMMON_CONTENT_RANK_1 = "1";
        public static final String COMMON_CONTENT_RANK_2 = "2";
        public static final String COMMON_CONTENT_RANK_3 = "3";
        public static final String COMMON_CONTENT_RANK_4 = "4";

        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String GROUP_ADD_RANGE_FINDING_CASE = "addRangeFindingCase";
        public static final String GROUP_ADD_MAP_POINT_CASE = "addMapPointCase";
        public static final String GROUP_ADD_MAP_SEARCH_INFORMATION_CASE = "addMapSearchInformationCase";
        public static final String GROUP_ADD_CAR_LOCUS_PLAYBACK_CASE = "addCarLocusPlaybackCase";
        public static final String GROUP_MAP_GRAPH_SIGN_CASE = "mapGraphSignCase";
    }

    /**
     * <p>
     * <b>文件名：MapCaseTemplet.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义在默认模板中所有的替换词语
     * </p>
     * <p>
     * <b>编码时间：2023年4月14日 上午9:01:46
     * </p>
     * <p>
     * <b>修改时间：2023年4月14日 上午9:01:46
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.1.0
     */
    protected class MapCaseTempletReplaceWord {
        public static final String POINT_NAME = "标记点名称";
        public static final String SEARCH_CONDITION = "搜索条件";
        public static final String INFORMATION = "信息";
        public static final String SIGN_NAME = "标记名称";
        public static final String GRAPH = "图形类型";
    }
}
