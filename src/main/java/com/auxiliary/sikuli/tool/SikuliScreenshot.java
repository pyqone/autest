package com.auxiliary.sikuli.tool;

import java.io.File;
import java.util.Optional;

import org.sikuli.script.Image;
import org.sikuli.script.Region;

import com.auxiliary.sikuli.SikuliToolsExcepton;
import com.auxiliary.tool.date.Time;

/**
 * <p>
 * <b>文件名：</b>SikuliScreenshot.java
 * </p>
 * <p>
 * <b>用途：</b> 用于在使用sikuli进行自动化测试中进行截图的工具。使用该类时可以指定截图保存的位置以及截图的名称，
 * 若不设置，则默认路径为在当前项目路径下，创建一个“Screenshot”文件夹进行存储，默认文件名称为Image。
 * 在指定截图保存的位置和指定文件名称时，若指定的名称不合法，则会抛出异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年2月14日 下午4:31:07
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月14日 下午4:31:07
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.1.0
 */
public class SikuliScreenshot {
    /**
     * 指向截图文件默认存放路径
     */
    private final File SCREENSHOT_DEFAULT_SAVE_PATH = new File("Screenshot/");
    private final String SCREENSHOT_DEFAULT_FILE_NAME = "截图_%d";

    /**
     * 指向当前识别范围类对象
     */
    private Region region;
    /**
     * 用于存储截图保存的路径
     */
    private File savePathFolder = SCREENSHOT_DEFAULT_SAVE_PATH;
    /**
     * 用于存储截图的时间
     */
    private long time = 500;

    /**
     * 用于按默认的方式创建截图，默认截图保存位置为项目路径下的“Screenshot”文件夹
     *
     * @param region {@link Region}对象
     * @since autest 3.1.0
     */
    public SikuliScreenshot(Region region) {
        this.region = region;
    }

    /**
     * 用于按指定的截图文件保存路径创建截图
     *
     * @param region         {@link Region}对象
     * @param savePathFolder 指定的截图保存路径
     * @since autest 3.1.0
     */
    public SikuliScreenshot(Region region, File savePathFolder) {
        setSavePathFolder(savePathFolder);
        this.region = region;
    }

    /**
     * 该方法用于设置截图文件保存路径
     *
     * @param savePathFolder 截图文件保存路径
     * @since autest 3.1.0
     */
    public void setSavePathFolder(File savePathFolder) {
        this.savePathFolder = Optional.ofNullable(savePathFolder).orElse(SCREENSHOT_DEFAULT_SAVE_PATH);
    }

    /**
     * 用于设置截图的等待时间
     *
     * @param time 设置等待时间，单位为毫秒
     * @since autest 3.1.0
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 该方法用于根据指定的文件名称，对屏幕进行截图，并保存至指定的文件夹下
     *
     * @param fileName 文件名称
     * @return 截图文件类对象
     * @since autest 3.1.0
     */
    public File createImage(String fileName) {
        // 判断文件名是否为空，若文件名为空，则使用默认名称
        fileName = Optional.ofNullable(fileName).filter(name -> !name.isEmpty())
                .orElse(String.format(SCREENSHOT_DEFAULT_FILE_NAME, Time.parse().getMilliSecond()));

        // 根据当前屏幕，获取截图类对象
        Image image = region.getImage();
        // 对屏幕进行截图，由于截图方法在sikuli内部进行了处理，导致其不会抛出IO异常，但可能会抛出空指针异常，为避免存在其他的问题，故此处直接处理整体异常
        try {
            Thread.sleep(time);
            image.save(fileName, savePathFolder.getAbsolutePath());
        } catch (Exception e) {
            throw new ImageFileException("无法保存当前截图，截图保存路径：" + savePathFolder.getAbsolutePath(), e);
        }

        // 返回截图文件类对象
        return new File(savePathFolder, (fileName + ".png"));
    }

    /**
     * <p>
     * <b>文件名：</b>SikuliScreenshot.java
     * </p>
     * <p>
     * <b>用途：</b> 定义当截图文件存在错误时，抛出的异常
     * </p>
     * <p>
     * <b>编码时间：</b>2022年2月14日 下午5:29:15
     * </p>
     * <p>
     * <b>修改时间：</b>2022年2月14日 下午5:29:15
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.1.0
     */
    public class ImageFileException extends SikuliToolsExcepton {
        private static final long serialVersionUID = 1L;

        public ImageFileException() {
            super();
        }

        public ImageFileException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
            super(arg0, arg1, arg2, arg3);
        }

        public ImageFileException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public ImageFileException(String arg0) {
            super(arg0);
        }

        public ImageFileException(Throwable arg0) {
            super(arg0);
        }
    }
}
