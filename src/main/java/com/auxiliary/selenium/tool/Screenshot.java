package com.auxiliary.selenium.tool;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.auxiliary.tool.date.Time;

/**
 * <p>
 * <b>文件名：</b>Screenshot.java
 * </p>
 * <p>
 * <b>用途：</b> 该类用于在使用selenium进行自动化测试中进行截图的工具。使用该类时可以指定截图保存的位置以及截图的名称，
 * 若不设置，则默认路径为在当前项目路径下，创建一个“Screenshot”文件夹进行存储，默认文件名称为Image。
 * 在指定截图保存的位置和指定文件名称时，若指定的名称不合法，则会抛出异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年2月14日 下午4:38:55
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月14日 下午4:38:55
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class Screenshot {
    /**
     * 指向截图文件默认存放路径
     */
    private final File SCREENSHOT_DEFAULT_SAVE_PATH = new File("Screenshot/");

    /**
     * 用于存储截图保存的路径
     */
    private File savePathFolder = SCREENSHOT_DEFAULT_SAVE_PATH;
    /**
     * 用于存储指定的WebDriver对象
     */
    private WebDriver driver;
    /**
     * 用于存储截图的时间
     */
    private long time = 500;

    /**
     * 用于按默认的方式创建截图，默认截图保存位置为项目目录下的“Screenshot”文件夹<br>
     *
     * @param driver {@link WebDriver}对象
     */
    public Screenshot(WebDriver driver) {
        setDriver(driver);
    }

    /**
     * 用于按指定的路径以及默认的文件名保存截图<br/>
     * 默认文件名为（不带后缀）：Image<br/>
     * 注意，传入的文件路径可为相对路径，也可为绝对路径，若路径不符合windows下文件夹名称的名称规则，
     * 则抛出IncorrectDirectoryException异常
     *
     * @param driver         {@link WebDriver}对象
     * @param savePathFolder 指定的截图保存路径
     * @throws IncorrectDirectoryException 传入路径不合法时抛出的异常
     */
    public Screenshot(WebDriver driver, File savePathFolder) {
        setDriver(driver);
        setSavePathFolder(savePathFolder);
    }

    /**
     * 该方法用于返回截图保存的路径
     *
     * @return 返回截图保存的路径
     */
    public File getSavePathFolder() {
        return savePathFolder;
    }

    /**
     * 该方法用于设置截图文件保存路径
     *
     * @param savePathFolder 截图文件保存路径
     */
    public void setSavePathFolder(File savePathFolder) {
        this.savePathFolder = Optional.ofNullable(savePathFolder).orElse(SCREENSHOT_DEFAULT_SAVE_PATH);
    }

    /**
     * 该方法用于返回指定的WebDriver对象
     *
     * @return 返回指定的WebDriver对象
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * 该方法用于设置WebDriver对象
     *
     * @param driver 指定的WebDriver对象
     */
    public void setDriver(WebDriver driver) {
        this.driver = Optional.ofNullable(driver).orElseThrow(() -> new WebDriverException("未指定WebDriver类对象"));
    }

    /**
     * 用于设置截图的等待时间
     *
     * @param time 设置等待时间，单位为毫秒
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 该方法用于创建截图并保存到相应的路径下，通过指定的截图文件名称和类中存储的WebDriver对象、截图保存路径来创建截图
     *
     * @param imageName 指定的截图文件名
     */
    public synchronized File creatImage(String imageName) {
        // 调用无参方法
        return saveScreenshot(imageName);
    }

    /**
     * 用于逐时进行截图，其截图的时间间隔与等待时间有关系，可对截图等待时间进行设置{@link #setTime(long)}
     */
    public void screenshotToTime() {
        // 开辟提条线程，让其独立于主线程运行
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                while (true) {
                    try {
                        creatImage("截图" + (i++) + "_");
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }).start();
    }

    /**
     * 该方法用于保存并转移截图
     *
     * @throws IOException        文件流状态不正确时抛出的异常
     * @throws WebDriverException WebDriver引用错误时抛出的异常
     */
    private File saveScreenshot(String fileName) {
        // 将savePath中保存的路径作为截图保存路径创建文件夹
        savePathFolder.mkdirs();

        // 判断是否有设置截图等待时间，若设置了，则加上等待时间
        if (time != 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
            }
        }

        // 判断是否传入文件名，若未传入文件名，则指定当前时间戳为文件名
        File imageFile = new File(savePathFolder + "/" + Optional.ofNullable(fileName).filter(text -> !text.isEmpty())
                .orElse(String.valueOf(Time.parse().getMilliSecond())) + ".png");

        // 截图，并将得到的截图转移到指定的目录下
        // 由于通过selenium的getScreenshotAs()得到的截图会不知道存储在哪，故需要通过文件流的方式将截图复制到指定的文件夹下
        try {
            FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), imageFile);
        } catch (WebDriverException | IOException e) {
            throw new IncorrectDirectoryException("文件路径存在异常：" + imageFile.getAbsolutePath());
        }

        return imageFile;
    }
}
