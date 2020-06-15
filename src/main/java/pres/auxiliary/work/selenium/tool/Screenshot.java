package pres.auxiliary.work.selenium.tool;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.directory.exception.UndefinedDirectoryException;
import pres.auxiliary.directory.operate.MakeDirectory;

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
	private StringBuilder savePath = new StringBuilder("C:\\AutoTest\\Screenshot\\");
	// 用于存储截图的文件名称
	private StringBuilder imageName = new StringBuilder("Image");
	// 用于存储指定的WebDriver对象
	private WebDriver driver;
	// 用于存储截图的时间
	private long time = 500;

	/**
	 * 用于按默认的方式创建截图保存位置及截图文件名称<br>
	 * 默认位置为：C:/AutoTest/Screenshot/<br>
	 * 默认文件名为（不带后缀）：Image
	 * 
	 * @param driver
	 *            WebDriver对象
	 */
	public Screenshot(WebDriver driver) {
		setDriver(driver);
	}
	
	/**
	 * 构造对象，<br>
	 * 默认位置为：C:/AutoTest/Screenshot/<br>
	 * 默认文件名为（不带后缀）：Image<br>
	 * <b>注意：</b>该方法只构建对象，由于未传入WebDriver对象，故不能直接用于截图
	 * 
	 * @param driver
	 *            WebDriver对象
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
	 */
	public Screenshot(String savePath) {
		setSavePath(savePath);
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
	public Screenshot(WebDriver driver, String savePath) {
		setDriver(driver);
		setSavePath(savePath);
	}

	/**
	 * 该方法用于返回截图的名称（不带后缀）
	 * 
	 * @return 返回截图的名称（不带后缀）
	 */
	public String getImageName() {
		return imageName.toString();
	}

	/**
	 * 该方法用于设置截图的文件名称，若传入的文件名不符合windows下文件的命名规则， 则抛出IncorrectDirectoryException异常
	 * 
	 * @param imageName
	 *            指定的截图名称
	 * @throws IncorrectDirectoryException
	 *             文件命名不正确时抛出的异常
	 */
	public void setImageName(String imageName) {
		// 判断传入的截图名称是否符合windows下的命名规则，若不符合，则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isFileName(imageName)) {
			throw new IncorrectDirectoryException("不合理的文件名称，文件名称：" + imageName);
		}

		// 通过判断后，则清空imageName存储的信息并将新的文件名称放入imageName种属性中
		this.imageName.delete(0, this.imageName.length());
		this.imageName.append(new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()));
		this.imageName.append(imageName);
	}

	/**
	 * 该方法用于返回截图保存的路径
	 * 
	 * @return 返回截图保存的路径
	 */
	public String getSavePath() {
		return savePath.toString();
	}

	/**
	 * 该方法用于设置截图保存的位置，可传入相对路径，也可传入绝对路径，
	 * 若传入的路径不符合windows下文件夹名称的命名规则时，则抛出IncorrectDirectoryException异常
	 * 
	 * @param savePath
	 *            传入的截图保存路径
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
	 */
	public void setSavePath(String savePath) {
		// 将传入的路径封装成StringBuilder，以便格式化
		StringBuilder sb = new StringBuilder(savePath);
		// 格式化传入的路径
		sb = MakeDirectory.formatPath(sb);

		// 判断传入的路径是否符合windows下对文件夹名称命名的规则，如果不符合则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isPath(sb.toString())) {
			throw new IncorrectDirectoryException("不合理的文件夹路径，文件路径：" + sb.toString());
		}

		// 将通过判断的sb赋给savePath属性
		this.savePath = sb;
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
		// 将名称放入属性中
		setImageName(imageName);
		// 调用无参方法
		return saveScreenshot();
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
	 * 该方法用于创建截图并保存到相应的路径下，通过指定的截图文件名称和类中存储的WebDriver对象、截图保存路径来创建截图，
	 * 该方法可先按照控件名称查找页面是否存在该控件，存在则截图，若不存在，则返回null
	 * 
	 * @param event 事件类（Event）对象
	 * @param controlName 控件名称
	 * @param imageName 指定的截图文件名
	 * @return
	 * @throws IOException
	 *             文件流状态不正确时抛出的异常
	 * @throws WebDriverException
	 *             WebDriver引用错误时抛出的异常
	 * @throws NullPointerException
	 *             WebDriver为空时抛出的异常
	 * @throws UndefinedDirectoryException
	 *             截图保存路径或截图名称为指定时抛出的异常
	 * @see #creatImage(String)
	 */
	public File creatImage(Event event, String controlName, String imageName) throws WebDriverException, IOException {
		//判断控件名对应的控件是否存在，存在后则截图，否则返回null
		if ( event.getJudgeEvent().judgeControl(controlName).getBooleanValue() ) {
			return null;
		}
		// 将名称放入属性中
		setImageName(imageName);
		// 调用无参方法
		return saveScreenshot();
	}

	/**
	 * 该方法用于保存并转移截图
	 * 
	 * @throws IOException
	 *             文件流状态不正确时抛出的异常
	 * @throws WebDriverException
	 *             WebDriver引用错误时抛出的异常
	 */
	private File saveScreenshot() throws WebDriverException, IOException {
		// 判断driver对象是否为空，
		if (driver == null) {
			throw new NullPointerException("无效的WebDriver对象");
		}

		// 判断截图保存路径和截图文件名是否存在，若不存在则抛出UndefinedDirectoryException异常
		if ("".equals(savePath.toString()) || "".equals(imageName.toString())) {
			throw new UndefinedDirectoryException(
					"未定义文件路径或者文件名，文件路径:" + savePath.toString() + "，文件名：" + imageName.toString());
		}
				
		// 将savePath中保存的路径作为截图保存路径创建文件夹
		File f = new File(savePath.toString());
		f.mkdirs();

		// 在imageName的后面加上当前时间以及后缀名
		imageName.append(".png");

		// 判断是否有设置截图等待时间，若设置了，则加上等待时间
		if (time != 0) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		f = new File(f + "\\" + imageName.toString());

		// 截图，并将得到的截图转移到指定的目录下
		// 由于通过selenium的getScreenshotAs()得到的截图会不知道存储在哪，故需要通过文件流的方式将截图复制到指定的文件夹下
		FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), f);

		return f;
	}

	@Override
	public String toString() {
		return "savePath=" + savePath + "\r\nimageName=" + imageName + "\r\ndriver=" + driver;
	}

}
