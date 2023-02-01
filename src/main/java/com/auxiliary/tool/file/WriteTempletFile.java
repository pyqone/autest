package com.auxiliary.tool.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataDriverFunction.FunctionExceptional;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.tool.common.VoidSupplier;
import com.google.common.base.Objects;

/**
 * <p>
 * <b>文件名：</b>WriteTempletFile.java
 * </p>
 * <p>
 * <b>用途：</b> 快速写入模板型的文件进行的工具。可通过类中提供的词语替换和默认内容的方法，对一类模板型的文件内容（如，测试用例）
 * 进行快速写作，提高工作效率。并且，若存在内容相同，但模板不同的文件，还可通过调用{@link #toWriteFileJson()}方法
 * 以完成各个子类之间的消息互通，达到快速生成多种同一内容，不同存放文件的目的。
 * </p>
 * <p>
 * 在注释中，存在以下名词需要解释：
 * <ul>
 * <li><b>用例</b>：写入到模板文件中的一条数据，类似于一条测试用例</li>
 * <li><b>内容</b>：写入到模板文件中的所有数据，类似于写在测试用例文件中的所有测试用例</li>
 * </ul>
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月15日上午11:13:26
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月15日上午11:13:26
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public abstract class WriteTempletFile<T extends WriteTempletFile<T>> {
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TEXT = "text";
    public static final String KEY_DEFAULT = "default";
    public static final String KEY_CASE = "case";
    public static final String KEY_TEMPLET = "templet";
    public static final String KEY_DATA = "data";

    /**
     * 待替换词语的标记
     */
    public static final String WORD_SIGN = "#";

    /**
     * 存储需要写入到文件中的数据
     */
    protected WriteFileData data;

    /**
     * 指向当前需要编写的用例下标
     */
    protected int caseIndex = -1;
    /**
     * 存储需要分页的行数
     */
    protected int writeRowNum = 0;

    /**
     * 存储判断是否自动换行的字段
     */
    protected String endField = "";

    /**
     * 构造对象，初始化创建文件的模板
     *
     * @param templet 模板类对象
     */
    protected WriteTempletFile(FileTemplet templet) {
        data = new WriteFileData(templet);
    }

    /**
     * 无参构造，方便子类进行特殊的构造方法
     */
    protected WriteTempletFile() {
    }

    /**
     * 设置需要被替换的词语以及替换的内容
     * <p>
     * 在调用{@link #addContent(String, String...)}等方法编写内容时，用“#xx#”来表示待替换的词语，
     * </p>
     *
     * @param word        需要替换的词语
     * @param replactWord 被替换的词语
     * @throws FunctionExceptional 未指定替换词语或替换内容时抛出的异常
     */
    public void setReplactWord(String word, String replactWord) {
        setReplactWord(new DataDriverFunction(word, text -> replactWord));
    }

    /**
     * 用于添加匹配公式与处理方式
     * <p>
     * 该方法允许添加待替换词语的处理方式，在写入用例时，若指定的待替换内容符合此方法指定的正则时，则会使用存储的替换方式， 对词语进行替换。例如：
     * <code><pre>
     * {@literal @}Test
     * public void setReplactWordTest_DataDriverFunction() {
     * 	// 定义词语匹配规则和处理方式，当匹配到正则后，将获取“随机：”后的字母
     *	// 若字母为“N”，则随机生成两位数字字符串
     * 	// 若字母为“Y”，则随机生成两位中文字符串
     *	test.setReplactWord(new DataDriverFunction("随机：[NC]", text -&gt; {
     *		return "N".equals(text.split("：")[1]) ? RandomString.randomString(2, 2, StringMode.NUM)
     *				: RandomString.randomString(2, 2, StringMode.CH);
     *	}));
     *
     *	// 随机生成两位数字
     *	test.addContent("title", "内容：#随机：N#").end();
     *	// 随机生成两位中文
     *	test.addContent("title", "内容：#随机：C#").end();
     *
     * 	// 控制台输出生成的内容json串
     *	System.out.println(test.toWriteFileJson());
     * }
     * </pre></code>
     * </p>
     * <p>
     * 部分定义方法可调用工具类{@link Functions}类获取
     * </p>
     *
     * @param functions 替换词语使用的函数
     * @throws FunctionExceptional 未指定替换词语或替换内容时抛出的异常
     */
    public void setReplactWord(DataDriverFunction functions) {
        data.addReplaceWord(functions);
    }

    /**
     * 用于向字段中添加默认内容
     * <p>
     * 当完成一条用例编写后，若未写入存在默认内容的字段时，则在完成用例后自动将默认内容写入字段中。 若字段存在内容，则不会写入默认值
     * </p>
     * <p>
     * <b>注意：</b>若模板中不存在指定的字段，则不写入该默认值；每次重新编写字段默认值时，会覆盖上次编写的内容
     * </p>
     *
     * @param field    字段
     * @param contents 默认内容
     */
    public void setFieldValue(String field, String... contents) {
        // 判断字段是否存在，若不存在，则不进行操作
        if (!data.getTemplet().containsField(field)) {
            return;
        }

        // 若未传值或传入null，则不进行操作
        if (contents == null || contents.length == 0) {
            return;
        }

        // 定义新的内容集合
        JSONArray defaultListJson = new JSONArray();

        // 查找特殊词语，并对词语进行替换，并将内容写入到字段中
        Arrays.stream(contents).map(text -> replaceWord(text, data.getReplaceWordMap())).map(text -> {
            JSONObject fieldJson = new JSONObject();
            fieldJson.put(KEY_TEXT, text);

            return fieldJson;
        }).forEach(defaultListJson::add);

        JSONObject fieldJson = new JSONObject();
        fieldJson.put(KEY_DATA, defaultListJson);
        data.getDefaultCaseJson().put(field, fieldJson);
    }

    /**
     * 用于设置自动写入文件的极限用例数量
     * <p>
     * 设置该值后，则开启自动写入文件功能，当用例数达到指定数量时，则会自动调用写入文件的方法，将缓存的内容写入到文件中
     * </p>
     * <p>
     * <b>注意：</b>自动写入文件并非会将所有的内容写入到文件中，例如，设置值为5，用例存在11条，则前10条用例会被自动写入到文件，而最后一条数据，
     * 由于未达到设定值，则不会写入到文件中，需要手动调用{@link #write()}方法
     * </p>
     *
     * @param writeRowNum 自动写入到文件的极限用例数量
     */
    public void setWriteRowNum(int writeRowNum) {
        this.writeRowNum = writeRowNum < 0 ? 0 : writeRowNum;
    }

    /**
     * 用于移除字段的默认值
     *
     * @param field 字段
     */
    public void clearFieldValue(String field) {
        data.removeFieldDefault(field);
    }

    /**
     * 用于重置当前的写入模板
     *
     * @param templet 模板类对象
     */
    public void setFileTemplet(FileTemplet templet) {
        data.setTemplet(templet.toString());
    }

    /**
     * 用于接收一个内容json串来替换当前写入的内容
     * <p>
     * 该方法可用于在更换内容但不替换当前模板时使用。可通过其他写入类的{@link #toWriteFileJson()}方法，获取到内容json串后，来替换当前类对象的内容json串
     * </p>
     * <p>
     * <b>注意：</b>设置内容json串后，当前写入的内容将被覆盖
     * </p>
     *
     * @param writeJsonText 指定的内容json串
     */
    public void setContentJson(String writeJsonText) {
        JSONObject writeJson = null;
        try {
            writeJson = JSONObject.parseObject(writeJsonText);
        } catch (Exception e) {
            throw new WriteFileException("json格式不正确，无法转换", e);
        }

        // 判断json中是否包含必要参数
        judgeJson(writeJson, KEY_DEFAULT, true);
        judgeJson(writeJson, KEY_CONTENT, true);
        judgeJson(writeJson.getJSONObject(KEY_CONTENT), KEY_CASE, false);

        // 通过判断后，将内容写入到类中
        // TODO 多模板需要重写该方法，将当前数据回写到相应的模板中
        data.setDefaultCaseJson(writeJson.getJSONObject(KEY_DEFAULT).toJSONString());
        data.setContentJson(writeJson.getJSONObject(KEY_CONTENT).toJSONString());
    }

    /**
     * 用于将其他写入类对象的待写入信息设置到当前类对象中
     * <p>
     * 调用该方法后，会将传入的{@link WriteFileData}类对象中，除模板外的信息覆盖到当前的模板类中，等同于进行数据的复制
     * </p>
     *
     * @param data 待写入信息类对象，可通过{@link #getWriteData()}方法获取
     */
    public void setWriteData(WriteFileData data) {
        this.data.setDefaultCaseJson(data.getDefaultCaseJsonText());
        this.data.setContentJson(data.getContentJsonText());
        this.data.setNowCaseNum(data.getNowCaseNum());
        data.getReplaceWordMap().forEach((name, fun) -> this.data.addReplaceWord(new DataDriverFunction(name, fun)));
    }

    /**
     * 该方法用于设置自动换行的字段，当重新插入该字段后，则自动切换到下一行
     * <p>
     * <b>注意：</b>若不启动该功能，则将自动换行字段设置为空或不存在的字段即可
     * </p>
     *
     * @param endField 自动换行字段
     * @since autest 3.1.0
     */
    public void setEndField(String endField) {
        this.endField = Optional.ofNullable(endField).orElse("");
    }

    /**
     * 用于返回当前待写入模板的信息类对象
     *
     * @return 信息类对象
     */
    public WriteFileData getWriteData() {
        return data;
    }

    /**
     * 用于对指定的内容json串进行判断的方法
     *
     * @param contentJson  内容json
     * @param field        字段
     * @param isJsonObject 是否为json串（不是json串则判定为json数组串）
     */
    private void judgeJson(JSONObject contentJson, String field, boolean isJsonObject) {
        if (contentJson.containsKey(field)) {
            if (isJsonObject) {
                try {
                    contentJson.getJSONObject(field);
                } catch (Exception e) {
                    throw new WriteFileException(String.format("“%s”字段非json类型：%s", field, contentJson.getString(field)),
                            e);
                }
            } else {
                try {
                    contentJson.getJSONArray(field);
                } catch (Exception e) {
                    throw new WriteFileException(
                            String.format("“%s”字段非json数组类型：%s", field, contentJson.getString(field)), e);
                }
            }

        } else {
            throw new WriteFileException("json缺少必要字段：" + field);
        }
    }

    /**
     * 用于获取指定下标的用例内容，并对内容进行重写
     * <p>
     * 调用该方法后，当前编写的用例将指向为指定下标的用例。若当前正在编写用例，调用该方法后，将覆盖当前编写的用例。
     * </p>
     * <p>
     * <b>注意：</b>若下标为负数或大于当前内容最大个数时，则不获取指定内容。下标从0开始计算，及0表示第一条用例
     * </p>
     *
     * @param index 用例下标
     * @return 类本身
     */
    @SuppressWarnings("unchecked")
    public T getCase(int index) {
        if (index < 0) {
            return (T) this;
        }

        // 获取用例集合
        JSONArray caseListJson = data.getContentJson().getJSONArray(KEY_CASE);
        // 判断下标指向的集合内容是否存在，不存在，则不进行获取
        if (index >= caseListJson.size()) {
            return (T) this;
        }

        // 设置当前用例下标为指定的下标
        caseIndex = index;
        // 获取下标对应的用例内容
        data.setCaseJson(caseListJson.getJSONObject(caseIndex));

        return (T) this;
    }

    /**
     * 根据传入的字段信息，将指定的内容插入到用例相应字段的最后一行
     * <p>
     * 方法允许传入多条内容，每条内容在写入到文件时，均以换行符隔开。
     * </p>
     * <p>
     * <b>注意：</b>当设置了自动换行字段时，则在插入该字段内容后（内容非空时），则会自动进行换行，可参考{@link #setEndField(String)}方法
     * </p>
     *
     * @param field    字段id
     * @param contents 相应字段的内容
     * @return 类本身
     */
    public T addContent(String field, String... contents) {
        return addContent(field, -1, contents);
    }

    /**
     * 根据传入的字段信息，将指定的内容插入到用例相应字段的指定下标下
     * <p>
     * 方法允许传入多条内容，每条内容在写入到文件时，均以换行符隔开。若指定的下标小于0或大于当前内容的最大个数时，则将内容写入到集合最后
     * </p>
     * <p>
     * <b>注意：</b>当设置了自动换行字段时，则在插入该字段内容后（内容非空时），则会在插入当前内容前自动进行换行，再插入该内容，可参考{@link #setEndField(String)}的设置方法
     * </p>
     *
     * @param field    字段id
     * @param index    指定插入的位置
     * @param contents 相应字段的内容
     * @return 类本身
     */
    public T addContent(String field, int index, String... contents) {
        return addContent(field, index, null, contents);
    }

    /**
     * 根据传入的字段信息，将指定的内容插入到用例相应字段的指定下标下，并且可传入临时的替换词语，对文本中的占位符进行替换，且不影响已添加的替换词语
     * <p>
     * 方法允许传入多条内容，每条内容在写入到文件时，均以换行符隔开。若指定的下标小于0或大于当前内容的最大个数时，则将内容写入到集合最后
     * </p>
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>当设置了自动换行字段时，则在插入该字段内容后（内容非空时），则会在插入当前内容前自动进行换行，再插入该内容，可参考{@link #setEndField(String)}的设置方法</li>
     * <li>若临时替换的词语集合中包含类中添加的替换词语，则以类中设置替换词语为主</li>
     * </ol>
     * </p>
     * 
     * @param field          字段id
     * @param index          指定插入的位置
     * @param replaceWordMap 临时替换词语集合
     * @param contents       相应字段的内容
     * 
     * @return 类本身
     * @since autest 4.0.0
     */
    @SuppressWarnings("unchecked")
    public T addContent(String field, int index, Map<String, DataFunction> replaceWordMap, String... contents) {
        data.getCaseJson();
        // 判断字段是否存在，若不存在，则不进行操作
        if (!data.getTemplet().containsField(field)) {
            return (T) this;
        }

        // 若未传值或传入null，则不进行操作
        if (contents == null || contents.length == 0) {
            return (T) this;
        }

        // 判断是否写入设置自动换行字段且当前已存在写入文件的内容，如果存在，并且与当前的字段一致，则自动换行
        if (Optional.ofNullable(endField).filter(str -> !str.isEmpty()).filter(str -> Objects.equal(str, field))
                .isPresent() && data.caseContainsField(endField)) {
            end();
        }

        // 获取字段指向的用例内容
        JSONObject fieldJson = Optional.ofNullable(data.getCaseJson().getJSONObject(field)).orElse(new JSONObject());
        JSONArray contentListJson = Optional.ofNullable(fieldJson.getJSONArray(WriteTempletFile.KEY_DATA))
                .orElse(new JSONArray());

        // 查找特殊词语，并对词语进行替换，并将内容写入到字段中
        Arrays.stream(contents).map(text -> {
            // 判断replaceWordMap是否为null，若为null则将replaceWordMap设置为默认替换词；若不为null，则将默认替换词添加到replaceWordMap中
            if (replaceWordMap == null) {
                return replaceWord(text, data.getReplaceWordMap());
            } else {
                replaceWordMap.putAll(data.getReplaceWordMap());
                return replaceWord(text, replaceWordMap);
            }

        }).map(text -> {
            JSONObject fieldTextJson = new JSONObject();
            fieldTextJson.put(KEY_TEXT, text);
            return fieldTextJson;
        }).forEach(json -> {
            // 判断传入的下标是否符合当前内容集合的个数限制，不符合，则将内容写入到最后一行
            if (index < 0 || index >= contentListJson.size()) {
                contentListJson.add(json);
            } else {
                contentListJson.add(index, json);
            }
        });

        // 将内容写入到用例数据中
        fieldJson.put(WriteTempletFile.KEY_DATA, contentListJson);
        data.getCaseJson().put(field, fieldJson);
        return (T) this;
    }

    /**
     * 用于清除用例字段下指定下标段落的内容
     *
     * @param field 字段
     * @param index 段落下标
     * @return 类本身
     */
    @SuppressWarnings("unchecked")
    public T clearContent(String field, int index) {
        // 判断字段是否存在，若不存在，则不进行操作
        if (!data.getTemplet().containsField(field)) {
            return (T) this;
        }

        if (index < 0) {
            return (T) this;
        }

        // 获取字段指向的用例内容
        JSONObject caseJson = data.getCaseJson();
        if (caseJson.containsKey(field)) {
            JSONArray contentListJson = caseJson.getJSONObject(field).getJSONArray(KEY_DATA);
            // 判断字段内容是否为空
            if (contentListJson != null && !contentListJson.isEmpty()) {
                // 若指定的下标小于内容集合的个数，则移除相应的内容
                if (index < contentListJson.size()) {
                    contentListJson.remove(index);
                }
            }
        }

        return (T) this;
    }

    /**
     * 用于移除指定下标的一条用例
     *
     * @param index 用例下标
     */
    public void clearCase(int index) {
        if (index < 0) {
            return;
        }

        // 获取用例集合
        JSONArray caseListJson = data.getContentJson().getJSONArray(KEY_CASE);
        // 判断下标指向的集合内容是否存在，不存在，则不进行获取
        if (index >= caseListJson.size()) {
            return;
        }

        caseListJson.remove(index);
    }

    /**
     * 标记完成一条用例数据的编写，并将内容插入到最后一行内容下
     * <p>
     * 调用该方法后，则将数据的缓存写入到文本的缓存内容中，以表示当前段落数据编写完成， 之后再次调用添加数据方法时，则写入一段新的内容。
     * </p>
     *
     * @return 类本身
     */
    public T end() {
        return end(Integer.MAX_VALUE);
    }

    /**
     * 标记完成一条用例数据的编写，并将内容插入到指定行内容下
     * <p>
     * 调用该方法后，则将数据的缓存写入到文本的缓存内容中，以表示当前段落数据编写完成， 之后再次调用添加数据方法时，则写入一段新的内容。
     * </p>
     * <p>
     * <b>注意：</b>若调用了{@link #getCase(int)}方法覆盖指定行内容的用例时，则传入的下标无效
     * </p>
     *
     * @param contentIndex 内容下标
     * @return 类本身
     */
    @SuppressWarnings("unchecked")
    public T end(int contentIndex) {
        // 将默认值附加到字段上
        replenishDefaultContent();

        // 获取内容字段的数组，并将用例写入到文本中
        JSONArray contentListJson = Optional.ofNullable(data.getContentJson().getJSONArray(KEY_CASE))
                .orElse(new JSONArray());
        // 将caseJson中存储的内容重新放入到一个json中

        // 判断当前用例下标是否为-1，为-1，则记录到内容中的最后一条；反之，则使用当前内容进行覆盖
        if (caseIndex == -1) {
            // 判断传入的内容下标是否正确，不正确，则对下标进行修正
            // 由于fastjson无法直接存储caseJson（清空caseJson时，其存储的内容也被清除），故先转成字符串后再用json进行解析
            if (contentIndex < 0) {
                contentListJson.add(0, JSONObject.parse(data.getCaseJsonText()));
            } else if (contentIndex >= 0 && contentIndex < contentListJson.size()) {
                contentListJson.add(contentIndex, JSONObject.parse(data.getCaseJsonText()));
            } else {
                contentListJson.add(JSONObject.parse(data.getCaseJsonText()));
            }
        } else {
            contentListJson.set(caseIndex, JSONObject.parse(data.getCaseJsonText()));
        }

        // 将用例集合重新添加至内容json中
        data.getContentJson().put(KEY_CASE, contentListJson);

        // 若当前指定了分行写入文件，则判断当前行数是否需要分行写入文件
        if (writeRowNum != 0 && contentListJson.size() % writeRowNum == 0) {
            write(data.getTemplet(), data.getNowCaseNum(), -1);
            // 指定当前写入的行为当前内容的行数
            data.setNowCaseNum(contentListJson.size());
        }

        // 清除用例json中的内容，并指定用例下标为-1
        data.clearCaseJson();
        caseIndex = -1;

        return (T) this;
    }

    /**
     * 用于返回主体json串
     * <p>
     * 在主体json串中，包括文件模板内容、默认值内容以及需要写入到文件中的内容，可通过该json串对文件写入类进行构造
     * </p>
     *
     * @return 主体json串
     */
    public String toWriteFileJson() {
        JSONObject mainjson = new JSONObject();
        mainjson.put(KEY_DEFAULT, data.getDefaultCaseJson());
        mainjson.put(KEY_CONTENT, data.getContentJson());

        return mainjson.toJSONString();
    }

    /**
     * 用于将编写的所有内容写入到文件中
     */
    public abstract void write();

    /**
     * 用于将编写的部分内容写入到文件中
     * <p>
     * 方法支持反序遍历，即指定的下标为负数时，则表示反序遍历用例集，至指定位置的用例。若两个下标一致，则不处理数据
     * </p>
     *
     * @param templet        需要写入的模板类对象
     * @param caseStartIndex 写入文件开始下标
     * @param caseEndIndex   写入文件结束下标
     */
    protected abstract void write(FileTemplet templet, int caseStartIndex, int caseEndIndex);

    /**
     * 用于将缓存的数据内容，写入到模板中
     *
     * @param templet        模板类
     * @param caseStartIndex 用例起始行
     * @param caseEndIndex   用例结束行
     */
    protected abstract void contentWriteTemplet(FileTemplet templet, int caseStartIndex, int caseEndIndex);

    /**
     * 用于根据当前实际的模板数量，返回拼接后的模板json内容
     * <p>
     * 由于部分写入类可传入多个模板，为方便构造，则需要对所有模板的json进行返回
     * </p>
     *
     * @return 拼接所有模板json后得到的模板json内容
     */
    public String toTempletJson() {
        // 转换模板Json，并进行存储
        JSONArray templetJsonList = new JSONArray();
        // 若转换方法返回为null，则构造一个空串
        Optional.ofNullable(getAllTempletJson()).orElse(new ArrayList<String>()).stream().map(JSONObject::parseObject)
                .forEach(templetJsonList::add);

        JSONObject tempJson = new JSONObject();
        tempJson.put(KEY_TEMPLET, templetJsonList);

        return tempJson.toJSONString();
    }

    /**
     * 将类中存储的模板类转换为模板json串，通过List集合收集后，进行返回
     *
     * @return 返回类中所有的模板json串
     */
    protected abstract List<String> getAllTempletJson();

    /**
     * 用于在用例中补充默认的字段内容，若用例存在内容，则不添加默认值
     */
    protected void replenishDefaultContent() {
        JSONObject caseJson = data.getCaseJson();
        // 遍历defaultCaseJson，判断是否存在caseJson中不存在的字段
        for (String key : data.getDefaultCaseJson().keySet()) {
            if (!caseJson.containsKey(key)) { // 若文本中不包含该字段，则将默认内容添加至字段
                caseJson.put(key, data.getDefaultCaseJson().getJSONObject(key));
            } else { // 若字段存在，则判断默认值内容的字段中是否有字段在内容串不存在的内容，并附加至内容串中
                JSONObject defaultFieldJson = data.getDefaultCaseJson().getJSONObject(key);
                JSONObject caseFildJson = data.getCaseJson().getJSONObject(key);

                for (String fieldName : defaultFieldJson.keySet()) {
                    if (!caseFildJson.containsKey(fieldName)) {
                        caseFildJson.put(fieldName, defaultFieldJson.get(fieldName));
                    }
                }
            }
        }
    }

    /**
     * 用于对当前文本内容中的词语进行提取，并返回替换后的内容
     *
     * @param content 文本内容
     * @return 替换词语后的文本内容
     */
    protected String replaceWord(String content, Map<String, DataFunction> replaceWordMap) {
        // 获取内容中的待替换词语
        ArrayList<String> wordList = getReplaceWord(content);

        // 循环，遍历待替换词语，并对内容进行替换
        for (String word : wordList) {
            // 将词语与每一个规则进行匹配
            for (String key : replaceWordMap.keySet()) {
                // 若传入的内容与正则匹配，则将数据进行处理，并返回处理结果
                if (Pattern.compile(key).matcher(word).matches()) {
                    // 将待替换的词语进行拼装
                    String oldWord = WORD_SIGN + word + WORD_SIGN;
                    // 获取替换的词语
                    String newWord = data.getReplaceWordMap().get(key).apply(word);
                    // 循环，替换所有与oldWord相同的内容
                    // 由于oldWord可能包含括号等特殊字符，故不能使用replaceAll方法进行替换
                    while (content.contains(oldWord)) {
                        int startIndex = content.indexOf(oldWord);
                        int endIndex = startIndex + oldWord.length();
                        content = content.substring(0, startIndex) + newWord + content.substring(endIndex);
                    }
                }
            }
        }

        return content;
    }

    /**
     * 用于获取内容中的所有的带替换符号的词语
     *
     * @param content 文本内容
     * @return 替换的词语集合
     */
    protected ArrayList<String> getReplaceWord(String content) {
        ArrayList<String> wordList = new ArrayList<>();
        // 获取当前数据中是否存在符号
        int index = content.indexOf("#");
        // 循环，判断是否存在标记符号
        while (index > -1) {
            // 存在编号，则将内容按第一个符号切分，使其不包括第一个符号
            content = content.substring(index + 1);
            // 再次获取符号所在的编号
            index = content.indexOf("#");

            // 若存在符号，则获取符号后的词语，到当前index位置
            if (index > -1) {
                wordList.add(content.substring(0, index));

                // 再次按照符号切分，并获取index
                content = content.substring(index + 1);
                index = content.indexOf("#");
            }
        }

        return wordList;
    }

    /**
     * 用于返回指定下标的文本json
     * <p>
     * 获取文本json支持反序遍历，当传入的内容为负数时，则表示反序遍历集合，找到指定下标的内容
     * </p>
     *
     * @param field     字段
     * @param textIndex 文本下标
     * @return 文本json
     */
    protected JSONObject getTextJson(String field, int textIndex) {
        JSONObject caseJson = data.getCaseJson();
        if (caseJson.containsKey(field)) {
            JSONArray dataListJson = caseJson.getJSONObject(field).getJSONArray(KEY_DATA);
            if (Optional.ofNullable(dataListJson).filter(arr -> !arr.isEmpty()).isPresent()) {
                return dataListJson.getJSONObject(analysisIndex(dataListJson.size(), textIndex, true));
            }
        }

        return null;
    }

    /**
     * 用于解析真实下标内容
     * <p>
     * 数字最大为下标允许的最大值，是可以达到的，在解析集合下标时，其最大限制一定为“集合长度 - 1”
     * </p>
     *
     * @param maxNum       数字最大限制
     * @param index        下标
     * @param isReciprocal 是否允许反向遍历
     * @return 真实下标
     */
    protected int analysisIndex(int maxNum, int index, boolean isReciprocal) {
        // 若最大值为0，则直接返回0
        if (maxNum == 0) {
            return 0;
        }

        if (index < 0) {
            if (isReciprocal) {
                return Math.abs(index) > maxNum ? 0 : (maxNum + index);
            } else {
                return 0;
            }
        } else {
            return index >= maxNum ? maxNum - 1 : index;
        }
    }

    /**
     * 用于创建模板文件
     *
     * @param templet 模板文件对象
     */
    protected abstract void createTempletFile(FileTemplet templet);

    /**
     * 该方法用于处理在一次性插入多个字段内容时，进行的自动换行处理。
     * <p>
     * 当需要一次性对多个字段进行写入时（例如，插入一条测试用例），则需要将所有的字段看成是一个整体，若需要插入的字段包含自动换行的字段时，则先进行换行操作，再插入整体内容
     * </p>
     *
     * @param fieldList 需要换行的字段集合
     * @param sup       需要执行的操作，为函数式接口{@link VoidSupplier}
     * @return 类本身
     * @since autest 3.1.0
     */
    @SuppressWarnings("unchecked")
    protected T disposeWriteFieldsContent(Collection<String> fieldList, VoidSupplier sup) {
        // 获取当前存储的自动换行字段，并设置自动换行字段为空，使其不进行自动换行
        String endField = this.endField;
        setEndField("");

        // 若当前需要换行的字段与传入的字段一致且当前准备插入的内容包含该字段时，则先进行换行操作
        if (fieldList.contains(endField) && data.caseContainsField(endField)) {
            end();
        }

        // 执行需要写入的内容
        sup.apply();
        // 将原自动换行字段设置到其中
        setEndField(endField);

        return (T) this;
    }
}
