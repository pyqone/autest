package pres.readme.code;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.testcase.file.BasicTestCaseWrite;
import pres.auxiliary.work.testcase.file.CreateCaseFile;

public class TestWriteCase {
	/**
	 * 写在方法外，便于在测试方法中调用
	 */
	BasicTestCaseWrite wtc;
	/**
	 * 配置文件类对象
	 */
	File conFile = new File(
			"ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
	/**
	 * 模板文件类对象
	 */
	File tempFile = new File("Result/测试用例.xlsx");

	@BeforeClass
	public void createTemplet() throws DocumentException, IOException {
		//创建测试用例模板文件
		CreateCaseFile temp = new CreateCaseFile(conFile, tempFile);
		temp.setCoverFile(true);
		temp.create();

		//构造用例编写类对象
		wtc = new BasicTestCaseWrite(conFile, tempFile);
		
		//添加常量词语
		wtc.setFieldValue("模块", "/测试项目/账号管理/创建账号");
		wtc.setFieldValue("目的", "验证创建账号界面各个控件输入是否有效");
		wtc.setFieldValue("状态", "1");
		wtc.setFieldValue("设计者", "test");
		wtc.setFieldValue("关联需求", "TEST-1");
	}
	
	@AfterClass
	public void openFolder() throws IOException {
		//将测试用例内容写入到文件中
		wtc.writeFile();
	}

	@Test
	public void addCase() {
		//添加姓名相关的用例
		wtc.addContent("标题", "通过不同的姓名创建账号")
				.addContent("前置条件", 
						"已在创建账号界面", 
						"除姓名字段外，其他信息均正确填写"
						)
				.addContent("步骤", 
						"“姓名”文本框不输入任何信息，点击“保存”按钮", 
						"在“姓名”文本框中只输入空格，点击“保存”按钮", 
						"在“姓名”文本框中输入HTML代码，点击“保存”按钮"
						)
				.addContent("预期", 
						"账号创建成功", 
						"账号创建成功", 
						"账号创建成功，且HTML代码不会被转义"
						)
				.addContent("优先级", "2")
				.addContent("关键用例", "2")
				.end();

		//添加身份证相关的用例
		wtc.addContent("标题", "通过不同的身份证创建账号")
				.addContent("目的", "验证创建账号界面各个控件输入是否有效")
				.addContent("前置条件", 
						"已在创建账号界面", 
						"除姓身份证段外，其他信息均正确填写"
						)
				.addContent("步骤", 
						"“身份证”文本框不输入任何信息，点击“保存”按钮", 
						"在“身份证”文本框中只输入空格，点击“保存”按钮", 
						"输入15位的证件信息，点击“保存”按钮", 
						"输入18位的证件信息，点击“保存”按钮", 
						"输入末尾带“X”或“x”的证件信息，点击“保存”按钮", 
						"输入大于18位的数字，点击“保存”按钮", 
						"输入小于18位但大于15位的数字，点击“保存”按钮", 
						"输入小于15位的数字，点击“保存”按钮", 
						"输入不符合证件规则但长度符合规则的数字（如123456789012345678），点击“保存”按钮", 
						"输入非数字字符，点击“保存”按钮"
						)
				.addContent("预期", 
						"账号创建失败，并给出相应的提示", 
						"账号创建失败，并给出相应的提示", 
						"账号创建成功", 
						"账号创建成功", 
						"账号创建成功", 
						"账号创建失败，并给出相应的提示", 
						"账号创建失败，并给出相应的提示", 
						"账号创建失败，并给出相应的提示", 
						"账号创建失败，并给出相应的提示", 
						"账号创建失败，并给出相应的提示"
						)
				.addContent("优先级", "1")
				.addContent("关键用例", "1")
				.end();
	}
}
