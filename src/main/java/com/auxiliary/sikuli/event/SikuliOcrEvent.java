package com.auxiliary.sikuli.event;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import org.sikuli.script.OCR;

import com.auxiliary.sikuli.IncorrectFileException;
import com.auxiliary.sikuli.element.FindSikuliElement;
import com.auxiliary.sikuli.element.SikuliElement;
import com.auxiliary.sikuli.element.TimeoutException;

/**
 * <p>
 * <b>文件名：</b>SikuliOcrEvent.java
 * </p>
 * <p>
 * <b>用途：</b> 封装sikuli工具的OCR事件，可设置{@link FindSikuliElement}类对象，用以元素名称来进行操作
 * <p>
 * <b>注意：</b>通过元素名称对元素进行查找的方式不支持外链词语
 * </p>
 * <p>
 * <b>编码时间：</b>2022年2月16日 下午5:54:31
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月16日 下午5:54:31
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.1.0
 */
public class SikuliOcrEvent extends SikuliAbstractEvent {
    /**
     * 默认语言包路径下的语言包名称
     */
    public static final String DEFAULT_LANGUAGE_NAME = "eng";
    /**
     * 语言包文件的后缀名称
     */
    public static final String LANGUAGE_FILE_SUFFIX = "traineddata";

    /**
     * 存储tess4j语言包所在文件路径
     */
    private String tessdataFilePath = "";
    /**
     * 存储tess4j语言包名称
     */
    private String languageName = OCR.globalOptions().language();

    /**
     * 构造对象
     *
     * @since autest 3.1.0
     */
    public SikuliOcrEvent() {
        super();
    }

    /**
     * 该方法用于设置语言包的名称
     * <p>
     * 语言包名称一般为traineddata文件后缀前的内容，如中文语言包的文件名称为“chi_sim.traineddata”，则传入的语言包名称为“chi_sim”。
     * </p>
     *
     * @param languageName 语言包名称
     * @since autest 3.1.0
     * @throws IncorrectFileException 当语言包名称为空或指向的语言包文件不存在时，抛出的异常
     */
    public void setDefaultLanguage(String languageName) {
        if (!Optional.ofNullable(languageName).filter(name -> !name.isEmpty()).isPresent()) {
            throw new IncorrectFileException("未指定语言包名称或语言包名称为空");
        }

        // 判断当前语言包文件是否存在
        if (!isExistsLanguage(tessdataFilePath, languageName)) {
            throw IncorrectFileException.throwLanguageException(tessdataFilePath, languageName);
        }

        this.languageName = languageName;
    }

    /**
     * 该方法用于设置OCR语言包的文件所在路径
     *
     * @param dataPathFolder 语言包文件所在路径
     * @since autest 3.1.0
     * @throws IncorrectFileException 当文件对象为null或不存在时抛出的异常
     */
    public void setDataPath(File dataPathFolder) {
        if (dataPathFolder == null) {
            throw new IncorrectFileException("未指定语言包文件夹路径：null");
        }

        if (!dataPathFolder.exists()) {
            throw new IncorrectFileException("语言包文件夹路径不存在，请检查路径是否正确：" + dataPathFolder.getAbsolutePath());
        }

        tessdataFilePath = dataPathFolder.getAbsolutePath();
    }

    /**
     * 该方法用于识别指定元素中的文本内容
     * <p>
     * <b>注意：</b>该工具调用Tess4J的OCR工具包，其英文与数字的识别率较高，而对于中文，其识别率较低，且每个中文间均用空格分开，需要自行处理
     * </p>
     *
     * @param element 元素类对象
     * @return 元素中的文本内容
     * @since autest 3.1.0
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String readText(SikuliElement element) {
        String eventName = "识别";

        Object text = actionOperate(eventName, element, match -> {
            // 获取原始内容
            String ocrPath = OCR.globalOptions().dataPath();
            String ocrLanguage = OCR.globalOptions().language();
            // 存储是否启用自定义配置
            boolean isMyConfig = false;

            // 判断当前是否进行自定义配置
            if (!tessdataFilePath.isEmpty()) {
                // 判断当前语言包文件是否存在
                if (!isExistsLanguage(tessdataFilePath, languageName)) {
                    throw IncorrectFileException.throwLanguageException(tessdataFilePath, languageName);
                }

                OCR.globalOptions().dataPath(tessdataFilePath);
                OCR.globalOptions().language(languageName);

                isMyConfig = true;
            }

            // 识别文本
            String result = OCR.readText(match);

            // 判断当前是否启用配置，若启用配置，则设置回原内容，保证OCR中的内容不便换
            if (isMyConfig) {
                OCR.globalOptions().dataPath(ocrPath);
                OCR.globalOptions().language(ocrLanguage);
            }

            return result;
        });

        recordLog(String.format("%s“%s”元素中的文本内容", eventName, element.getName()), 0);

        return text.toString();
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，识别目标元素中的文本内容
     *
     * @param elementName 元素名称
     * @param index       多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @return 元素中的文本内容
     * @since autest 3.1.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String readText(String elementName, int index) {
        if (Optional.ofNullable(find).isPresent()) {
            return readText(find.findElement(elementName, index));
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，识别目标元素中的文本内容
     *
     * @param elementName 元素名称
     * @return 元素中的文本内容
     * @since autest 3.1.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public String readText(String elementName) {
        return readText(elementName, 1);
    }

    /**
     * 该方法用于判断指定路径下是否存在指定的语言包文件
     *
     * @param path 语言包包所在路径
     * @param languageName 语言包名称
     * @return 路径下是否存在语言包
     * @since autest 3.1.0
     */
    private boolean isExistsLanguage(String path, String languageName) {
        // 判断当前是否重新指定语言包路径
        if (path.isEmpty()) {
            return Objects.equals(languageName, DEFAULT_LANGUAGE_NAME);
        } else {
            return new File(new File(tessdataFilePath),
                    String.format("%s.%s", languageName, LANGUAGE_FILE_SUFFIX)).exists();
        }
    }
}
