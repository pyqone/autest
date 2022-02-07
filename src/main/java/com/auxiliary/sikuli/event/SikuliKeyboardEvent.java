package com.auxiliary.sikuli.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import org.sikuli.script.Key;
import org.sikuli.script.Region;

import com.auxiliary.sikuli.element.FindSikuliElement;
import com.auxiliary.sikuli.element.SikuliElement;
import com.auxiliary.sikuli.element.TimeoutException;
import com.auxiliary.tool.data.KeyType;

/**
 * <p>
 * <b>文件名：</b>SikuliKeyboardEvent.java
 * </p>
 * <p>
 * <p>
 * <b>用途：</b> 封装sikuli工具的键盘事件，可设置{@link FindSikuliElement}类对象，用以元素名称来进行操作
 * <p>
 * <b>注意：</b>通过元素名称对元素进行查找的方式不支持外链词语
 * </p>
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月23日 上午8:03:49
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月7日 下午3:11:51
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 * @since autest 3.0.0
 */
public class SikuliKeyboardEvent extends SikuliAbstractEvent {
    /**
     * 构造对象
     *
     * @since autest 3.0.0
     */
    public SikuliKeyboardEvent() {
        super();
    }

    /**
     * 该方法用于在目标元素中，输入指定的字符串内容
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>当传入的文本为空或为null时，则不进行任何操作，直接返回文本</li>
     * </ul>
     * </p>
     *
     * @param element 元素类对象
     * @param text    在元素中输入的文本
     * @return 在元素中输入的文本
     * @since autest 3.0.0
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String input(SikuliElement element, String text) {
        if (Optional.ofNullable(text).filter(str -> !str.isEmpty()).isPresent()) {
            String eventName = "输入";

            actionOperate(eventName, element, match -> {
                // 根据点击事件的返回值是否为0判断操作是否成功
                match.click();
                if (match.paste(text) != 0) {
                    return text;
                } else {
                    return null;
                }
            });

            recordLog(String.format("在“%s”元素指向的文本框中%s：%s", element.getName(), eventName, text), 0);
        }

        return text;
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，在目标元素中，输入指定的字符串内容
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>当传入的文本为空或为null时，则不进行任何操作，直接返回文本</li>
     * </ul>
     * </p>
     *
     * @param elementName 元素名称
     * @param index       多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param text        在元素中输入的文本
     * @return 在元素中输入的文本
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String input(String elementName, int index, String text) {
        if (Optional.ofNullable(find).isPresent()) {
            return input(find.findElement(elementName, index), text);
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，在目标元素中，输入指定的字符串内容
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>当传入的文本为空或为null时，则不进行任何操作，直接返回文本</li>
     * </ul>
     * </p>
     *
     * @param elementName 元素名称
     * @param text        在元素中输入的文本
     * @return 在元素中输入的文本
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String input(String elementName, String text) {
        return input(elementName, 1, text);
    }

    /**
     * 该方法用于目标元素上，按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）</li>
     * <li>当传入的按键组为空或为null时，则不进行任何操作，返回空字符串</li>
     * </ul>
     * </p>
     *
     * @param element 元素类对象
     * @param keys    按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     * @deprecated 由{@link #type(SikuliElement, KeyType...)}代替，将在3.2.0或之后版本中删除
     */
    @Deprecated
    public String type(SikuliElement element, char... keys) {
        String text = "";

        if (Optional.ofNullable(keys).filter(arr -> arr.length != 0).isPresent()) {
            String eventName = "执行组合键";

            text = actionOperate(eventName, element, match -> {
                // 根据点击事件的返回值是否为0判断操作是否成功
                match.click();
                return type(match, keys);
            }).toString();

            recordLog(String.format("在“%s”元素上%s：%s", element.getName(), eventName, text), 0);
        }

        return text;
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，在目标元素上，按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）</li>
     * <li>当传入的按键组为空或为null时，则不进行任何操作，返回空字符串</li>
     * </ul>
     * </p>
     *
     * @param elementName 元素名称
     * @param index       多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param keys        按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     * @deprecated 由{@link #type(String, int, KeyType...)}代替，将在3.2.0或之后版本中删除
     */
    @Deprecated
    public String type(String elementName, int index, char... keys) {
        if (Optional.ofNullable(find).isPresent()) {
            return type(find.findElement(elementName, index), keys);
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，在目标元素上，按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）</li>
     * <li>当传入的按键组为空或为null时，则不进行任何操作，返回空字符串</li>
     * </ul>
     * </p>
     *
     * @param elementName 元素名称
     * @param keys        按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     * @deprecated 由{@link #type(String, KeyType...)}代替，将在3.2.0或之后版本中删除
     */
    @Deprecated
    public String type(String elementName, char... keys) {
        return type(elementName, 1, keys);
    }

    /**
     * 该方法用于在默认的识别区域内（即主屏幕范围），按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）
     * </p>
     *
     * @param keys 按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @deprecated 由{@link #type(KeyType...)}代替，将在3.2.0或之后版本中删除
     */
    @Deprecated
    public String type(char... keys) {
        // 使用当前屏幕作为默认识别范围
        String result = type(DEFAULT_REGION, keys);

        recordLog(String.format("在当前主屏幕下，执行以下组合键：%s", result), 0);
        return result;
    }

    /**
     * 该方法用于根据指定的识别范围，依次按下相应的快捷按键，之后释放相应按键
     *
     * @param region 识别范围
     * @param keys   需要按下的按键组合集合
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @deprecated 由{@link #type(Region, KeyType...)}代替，将在3.2.0或之后版本中删除
     */
    @Deprecated
    private String type(Region region, char... keys) {
        StringJoiner keyText = new StringJoiner(" + ", "[", "]");

        List<Character> keyList = new ArrayList<>();
        for (char key : keys) {
            keyList.add(Character.valueOf(key));
            // 拼接按键名称文本
            String keyStr = Key.toJavaKeyCodeText(key);
            if (keyStr.contains("#")) {
                keyText.add(keyStr.substring(1, keyStr.length() - 1));
            } else {
                keyText.add(keyStr);
            }

        }

        // 循环，依次按下相应的按键
        for (Character key : keyList) {
            region.keyDown(String.valueOf(key));
        }

        // 按下按键后，再循环松开所有按键
        for (Character key : keyList) {
            region.keyUp(key);
        }

        return keyText.toString();
    }

    /**
     * 该方法用于目标元素上，按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）</li>
     * <li>当传入的按键组为空或为null时，则不进行任何操作，返回空字符串</li>
     * </ul>
     * </p>
     *
     * @param element  元素类对象
     * @param keyTypes 按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String type(SikuliElement element, KeyType... keyTypes) {
        String text = "";

        List<KeyType> keyList = Optional.ofNullable(keyTypes).filter(arr -> arr.length != 0).map(Arrays::asList)
                .orElse(new ArrayList<KeyType>());
        if (!keyList.isEmpty()) {
            String eventName = "执行组合键";

            text = actionOperate(eventName, element, match -> {
                // 根据点击事件的返回值是否为0判断操作是否成功
                match.click();
                return type(match, keyTypes);
            }).toString();

            recordLog(String.format("在“%s”元素上%s：%s", element.getName(), eventName, text), 0);
        }

        return text;
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，在目标元素上，按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）</li>
     * <li>当传入的按键组为空或为null时，则不进行任何操作，返回空字符串</li>
     * </ul>
     * </p>
     *
     * @param elementName 元素名称
     * @param index       多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param keyTypes    按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String type(String elementName, int index, KeyType... keyTypes) {
        if (Optional.ofNullable(find).isPresent()) {
            return type(find.findElement(elementName, index), keyTypes);
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，在目标元素上，按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>
     * <ul>
     * <li>在输入内容时，会先点击相应的元素，否则sikuli工具将无法进行输入，故在编写脚本时请确认元素是否能点击</li>
     * <li>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）</li>
     * <li>当传入的按键组为空或为null时，则不进行任何操作，返回空字符串</li>
     * </ul>
     * </p>
     *
     * @param elementName 元素名称
     * @param keyTypes    按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String type(String elementName, KeyType... keyTypes) {
        return type(elementName, 1, keyTypes);
    }

    /**
     * 该方法用于在默认的识别区域内（即主屏幕范围），按下指定的按键，进行相应快捷键的操作。按键映射字符可通过{@link Key}类中进行调用
     * <p>
     * <b>注意：</b>调用{@link Key}类时，需要使用“C_”开头的静态属性，例如需要按下“F1”按键，则使用{@link Key#C_F1}；若需要使用普通英文按键，则传入相应按键的char字符即可（如需要按下按键“A”，则可传入'a'）
     * </p>
     *
     * @param keyTypes 按键组
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.0.0
     */
    public String type(KeyType... keyTypes) {
        // 使用当前屏幕作为默认识别范围
        String result = type(DEFAULT_REGION, keyTypes);

        recordLog(String.format("在当前主屏幕下，执行以下组合键：%s", result), 0);
        return result;
    }

    /**
     * 该方法用于根据指定的识别范围，依次按下相应的快捷按键，之后释放相应按键
     *
     * @param region   识别范围
     * @param keyTypes 需要按下的按键组合集合
     * @return 组合按键名称，以“[按键1 + 按键2 + ... + 按键n]”的形式返回
     * @since autest 3.1.0
     */
    private String type(Region region, KeyType... keyTypes) {
        StringJoiner keyText = new StringJoiner(" + ", "[", "]");

        List<Character> keyList = new ArrayList<>();
        for (KeyType key : keyTypes) {
            keyList.add(key.getSikuliKey());
            // 拼接按键名称文本
            keyText.add(key.getName());
        }

        // 循环，依次按下相应的按键
        for (Character key : keyList) {
            region.keyDown(String.valueOf(key));
        }

        // 按下按键后，再循环松开所有按键
        for (Character key : keyList) {
            region.keyUp(key);
        }

        return keyText.toString();
    }
}
