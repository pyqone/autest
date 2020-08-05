package pres.auxiliary.work.selenium.tool;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * 该类用于在使用selenium进行自动化测试中进行截图的工具。使用该类时可以指定截图保存的位置以及
 * 截图的名称，若不设置，则默认路径为C:\\AutoTest\\Screenshot\\，默认文件名称为Image。
 * 在指定截图保存的位置和指定文件名称时，若指定的名称不合法，则会抛出异常
 * 
 * @author 彭宇琦
 * @version Ver1.1
 */
public class Screenshot {
	// 用于存储截图保存的路径
	private File savePathFolder = new File("Screenshot/");
	// 用于存储指定的WebDriver对象
	private WebDriver driver;
	// 用于存储截图的时间
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
	 * @param savePath
	 *            指定的截图保存路径
	 * @param driver
	 *            WebDriver对象
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
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
	 * @param savePathFolder
	 */
	public void setSavePathFolder(File savePathFolder) {
		this.savePathFolder = savePathFolder;
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
	 * @param driver
	 *            指定的WebDriver对象
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * 用于设置截图的等待时间
	 * 
	 * @param time
	 *            设置等待时间，单位为毫秒
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * 该方法用于创建截图并保存到相应的路径下，通过指定的截图文件名称和类中存储的WebDriver对象、截图保存路径来创建截图
	 * 
	 * @param imageName
	 *            指定的截图文件名
	 * @throws IOException
	 *             文件流状态不正确时抛出的异常
	 * @throws WebDriverException
	 *             WebDriver引用错误时抛出的异常
	 * @throws NullPointerException
	 *             WebDriver为空时抛出的异常
	 * @throws UndefinedDirectoryException
	 *             截图保存路径或截图名称为指定时抛出的异常
	 * @see #creatImage()
	 */
	public synchronized File creatImage(String imageName) throws WebDriverException, IOException {
		// 调用无参方法
		return saveScreenshot(imageName);
	}
	
	/**
	 * 用于逐时进行截图，其截图的时间间隔与等待时间有关系，可对截图等待时间进行设置{@link #setTime(long)}
	 */
	public void screenshotToTime() {
		//开辟提条线程，让其独立于主线程运行
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 1;
				while(true) {
					try {
						creatImage("截图" + (i++) + "_");
					} catch (WebDriverException e) {
						return;
					} catch (IOException e) {
						return;
					}
				}
			}
		}).start();
	}
	
	/**
	 * 该方法用于保存并转移截图
	 * 
	 * @throws IOException
	 *             文件流状态不正确时抛出的异常
	 * @throws WebDriverException
	 *             WebDriver引用错误时抛出的异常
	 */
	private File saveScreenshot(String fileName) throws WebDriverException, IOException {
		// 判断driver对象是否为空，
		if (driver == null) {
			throw new NullPointerException("无效的WebDriver对象");
		}
				
		// 将savePath中保存的路径作为截图保存路径创建文件夹
		savePathFolder.mkdirs();

		// 判断是否有设置截图等待时间，若设置了，则加上等待时间
		if (time != 0) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		File imageFile = new File(savePathFolder + fileName + ".png");

		// 截图，并将得到的截图转移到指定的目录下
		// 由于通过selenium的getScreenshotAs()得到的截图会不知道存储在哪，故需要通过文件流的方式将截图复制到指定的文件夹下
		FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), imageFile);

		return imageFile;
	}
}
