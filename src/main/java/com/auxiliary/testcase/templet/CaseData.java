package com.auxiliary.testcase.templet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.common.Placeholder;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：CaseData.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 提供测试用例数据的写入和返回方法
 * </p>
 * <p>
 * <b>编码时间：2023年1月4日 下午2:24:26
 * </p>
 * <p>
 * <b>修改时间：2023年1月4日 下午2:24:26
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public class CaseData {
    /**
     * 用于存储测试用例的内容
     */
    private Map<String, List<String>> caseMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 用于存储当前生成用例的模板类对象，以方便调取模板类对象中部分所需的功能
     */
    private AbstractCaseTemplet caseTemplet;

    /**
     * 构造对象，并设置当前生成用例的模板类对象
     * <p>
     * 在写入用例时需要读取模板类中的内容，若通过用例模板子类生成的测试用例数据，建议使用该构造，在构造对象时使用如下形式进行构造
     *
     * <pre>
     * <code>CaseData caseData = new CaseData(this)</code>
     * </pre>
     * </p>
     * <p>
     * 若不存在模板，可传入null进行构造
     * </p>
     *
     * @param caseTemplet 用例模板类对象
     */
    public CaseData(AbstractCaseTemplet caseTemplet) {
        this.caseTemplet = caseTemplet;
    }

    /**
     * 该方法用于返回用例模板类对象
     *
     * @return 用例模板类对象
     * @since autest 4.0.0
     */
    public AbstractCaseTemplet getCaseTemplet() {
        return caseTemplet;
    }

    /**
     * 该方法用于向指定的字段中插入指定的内容，下标支持反序遍历
     * <p>
     * 下标为内容所在位置的真实下标，其逻辑如下：
     * <ul>
     * <li>正序插入内容（传入正数），则在指定的真实下标前插入内容：
     * <ul>
     * <li>当传入大于等于1且小于等于字段内容集合总个数（以下简称“总数”）时，则在指定的下标前插入指定的内容，例如，下标传入“2”，则在第2条内容前插入指定的内容</li>
     * <li>当传入大于总数时，则在最后一条内容前插入指定的内容</li>
     * <li>当传入0时，则在第1条内容前插入指定的内容</li>
     * </ul>
     * </li>
     * <li>反序插入内容（传入负数），则在指定的真实下标后插入内容：</li>
     * <ul>
     * <li>当传入小于-1且大于等于总数的相反数时，则在倒数指定的下标内容后插入指定的内容，例如，下标传入“-2”，则在倒数第2条内容后插入指定的内容</li>
     * <li>当传入小于总数的相反数时，则第1条内容后插入指定的内容</li>
     * </ul>
     * </ul>
     * </p>
     *
     * @param field       字段名称
     * @param insertIndex 插入的下标
     * @param contents    插入的内容字符串数组
     * @return 类本身
     * @since autest 4.0.0
     */
    public CaseData addContent(String field, int insertIndex, String... contents) {
        return addContent(field, insertIndex,
                Arrays.asList(Optional.ofNullable(contents).orElseGet(() -> new String[] {})));
    }

    /**
     * 该方法用于向指定的字段中插入指定的内容，下标支持反序遍历
     * <p>
     * 下标为内容所在位置的真实下标，其逻辑如下：
     * <ul>
     * <li>正序插入内容（传入正数）：
     * <ul>
     * <li>当传入大于等于1且小于等于字段内容集合总个数（以下简称“总数”）时，则在指定的下标前插入指定的内容，例如，下标传入“2”，则在第2条内容前插入指定的内容</li>
     * <li>当传入大于总数时，则在最后一条内容后插入指定的内容</li>
     * <li>当传入0时，则在第1条内容前插入指定的内容</li>
     * </ul>
     * </li>
     * <li>反序插入内容（传入负数）：</li>
     * <ul>
     * <li>当传入小于等于-1且大于等于总数的相反数时，则在倒数指定的下标内容后插入指定的内容，例如，下标传入“-2”，则在倒数第2条内容后插入指定的内容</li>
     * <li>当传入小于总数的相反数时，则第1条内容前插入指定的内容</li>
     * </ul>
     * </ul>
     * </p>
     *
     * @param field       字段名称
     * @param insertIndex 插入的下标
     * @param contentList 插入的内容字符串集合
     * @return 类本身
     * @since autest 4.0.0
     */
    public CaseData addContent(String field, int insertIndex, List<String> contentList) {
        // 判断字段与内容数组是否包含数据，若为null或为空，则不进行操作
        if (field == null || field.isEmpty()) {
            return this;
        }
        if (contentList == null || contentList.isEmpty()) {
            return this;
        }

        // 获取内容组
        List<String> fieldContentList = getFieldContentList(field);

        // 处理需要插入的下标数据，使其能正确进行插入
        if (fieldContentList.isEmpty()) {
            insertIndex = 0;
        } else {
            insertIndex = DisposeCodeUtils.customizedIndex2ArrayIndex(insertIndex, 1, fieldContentList.size() + 1, 1,
                    true, false, false, false);
        }

        // 插入指定的数据
        fieldContentList.addAll(insertIndex, contentList);

        return this;
    }

    /**
     * 该方法用于将测试用例类对象下下指定字段的内容拼接至当前测试用例类对象相应字段下
     *
     * @param caseDate 已生成的测试用例类对象
     * @param fields   指定的字段组
     * @return 当前用例类对象
     * @since autest 4.0.0
     */
    public CaseData addContent(CaseData caseDate, String... fields) {
        // 判断两个参数是否为null
        if (fields == null) {
            return this;
        }
        Optional<CaseData> caseDataOpt = Optional.ofNullable(caseDate);
        if (!caseDataOpt.isPresent()) {
            return this;
        }

        // 遍历传入的caseData类对象中的所有字段
        for (String field : fields) {
            // 获取待添加用例类的字段内容，并将其转换为字符串集合
            List<String> contentList = caseDate.getFieldContentList(field);
            addContent(field, -1, contentList);
        }
        return this;
    }

    /**
     * 该方法用于对指定下标的内容进行移除，下标支持反序遍历
     * <p>
     * 下标为内容所在位置的真实下标，其逻辑如下（默认指定的字段存在内容，且不超过5条）：
     * <ul>
     * <li>正序插入内容（传入正数）：
     * <ul>
     * <li>当传入大于等于1且小于等于字段内容集合总个数（以下简称“总数”）时，则删除指定的下标内容，例如，下标传入“2”，则删除第2条内容</li>
     * <li>当传入大于总数时，则删除最后一条内容</li>
     * <li>当传入0时，则删除第1条内容</li>
     * </ul>
     * </li>
     * <li>反序插入内容（传入负数），则在指定的真实下标后插入内容：</li>
     * <ul>
     * <li>当传入小于-1且大于等于总数的相反数时，则删除倒数指定下标的内容，例如，下标传入“-2”，则删除倒数第2条内容</li>
     * <li>当传入小于总数的相反数时，则删除第1条内容</li>
     * </ul>
     * </ul>
     * </p>
     *
     * @param removeIndex 需要移除内容的真实所在下标
     * @param fields      需要删除指定下标的字段组
     * @return 类本身
     * @since autest 4.0.0
     */
    public CaseData removeContent(int removeIndex, String...fields) {
        // 判断传入的字段组是否为空
        if (fields == null || fields.length == 0) {
            return this;
        }

        // 遍历整个字段组，并根据字段组的长度，对相应下标的内容进行删除
        for (String field : fields) {
            // 判断当前字段在用例集合中是否存在，若不存在，则不进行删除操作
            if (!caseMap.containsKey(field)) {
                continue;
            }

            List<String> contentList = caseMap.get(field);
            // 根据字段对应内容的数量，以及传入的下标内容，重新确认真实的删除下标
            removeIndex = DisposeCodeUtils.customizedIndex2ArrayIndex(removeIndex, 1, contentList.size(), 1,
                    true, false, false, false);
            // 对相应字段内容进行删除
            contentList.remove(removeIndex);
        }

        return this;
    }

    /**
     * 该方法用于对指定下标的内容进行替换，下标支持反序遍历
     * <p>
     * 下标为内容所在位置的真实下标，其逻辑如下（默认指定的字段存在内容，且不超过5条）：
     * <ul>
     * <li>正序插入内容（传入正数）：
     * <ul>
     * <li>当传入大于等于1且小于等于字段内容集合总个数（以下简称“总数”）时，则替换指定的下标内容，例如，下标传入“2”，则替换第2条内容</li>
     * <li>当传入大于总数时，则替换最后一条内容</li>
     * <li>当传入0时，则替换第1条内容</li>
     * </ul>
     * </li>
     * <li>反序插入内容（传入负数），则在指定的真实下标后插入内容：</li>
     * <ul>
     * <li>当传入小于-1且大于等于总数的相反数时，则替换倒数指定下标的内容，例如，下标传入“-2”，则替换倒数第2条内容</li>
     * <li>当传入小于总数的相反数时，则替换第1条内容</li>
     * </ul>
     * </ul>
     * </p>
     *
     * @param field        字段名称
     * @param replaceIndex 需要替换的下标
     * @param contents     替换的内容组
     * @return 类本身
     * @since autest 4.0.0
     */
    public CaseData replaceContent(String field, int replaceIndex, String... contents) {
        // 判断传入的字段组是否为空
        if (contents == null || contents.length == 0) {
            return this;
        }

        // 判断字段是否存在
        if (!caseMap.containsKey(field)) {
            return this;
        }

        // 删除制定下标的内容
        removeContent(replaceIndex, field);
        // 在下标位置添加内容
        addContent(field, replaceIndex, contents);

        return this;
    }

    /**
     * 该方法用于返回指定字段的内容集合，若字段在用例集合中不存在，则初始化字段的内容集合，并返回相应字段的空集合对象
     *
     * @param field 字段名称
     * @return 用例集合中字段对应的内容集合
     * @since autest 4.0.0
     */
    private List<String> getFieldContentList(String field) {
        // 判断当前字段在用例map集合中是否存在，若不存在，则进行初始化
        if (!caseMap.containsKey(field)) {
            List<String> contentList = new ArrayList<>();
            caseMap.put(field, contentList);
        }

        return caseMap.get(field);
    }

    /**
     * 该方法用于对指定字段中的内容进行返回，若字段不存在，则返回空集合
     *
     * @param field 字段
     * @return 字段对应的内容集合
     * @since autest 4.0.0
     */
    public List<String> getContent(String field) {
        return Optional.ofNullable(caseMap.get(field)).map(ArrayList::new).orElseGet(ArrayList::new);
    }

    /**
     * 该方法用于返回用例中包含的所有字段
     *
     * @return 用例字段集合
     * @since autest 4.0.0
     */
    public Set<String> getFields() {
        return caseMap.keySet();
    }

    /**
     * 该方法用于返回指定的模板类对象中存储的需要替换模板占位符的词语集合，若未指定模板类对象，则返回空集合
     *
     * @return 需要替换模板占位符的词语集合
     * @since autest 4.0.0
     * @deprecated 该方法已失效，已有{@link #getPlaceholder()}方法代替，将在5.1.0版本后删除
     */
    @Deprecated
    public Map<String, DataFunction> getReplaceWordMap() {
        return Optional.ofNullable(caseTemplet).map(ct -> ct.getPlaceholder().getReplaceFunctionMap())
                .orElseGet(() -> new HashMap<>(ConstType.DEFAULT_MAP_SIZE));
    }

    /**
     * 该方法用于返回模板的占位符类对象
     *
     * @return 占位符类对象
     * @since autest 5.0.0
     */
    public Placeholder getPlaceholder() {
        return caseTemplet.getPlaceholder();
    }

    /**
     * 该方法用于返回当前存储的用例内容，转换为json的形式进行返回
     *
     * @return 用例json形式
     * @since autest 4.0.0
     */
    @Override
    public String toString() {
        return JSONObject.toJSONString(caseMap);
    }
}
