package pres.auxiliary.work.old.testcase.writecase;

import java.io.File;
import java.io.IOException;

import pres.auxiliary.work.old.testcase.change.Tab;

/**
 * FileName: Username.java
 * 
 * 用于生成与用户名及密码相关的测试用例（如登录、注册、修改密码、忘记密码等操作）
 * 
 * @author 彭宇琦
 * @Deta 2018年6月13日 下午3:39:24
 * @version ver1.0
 */
public class Username extends Case {
	public Username() {
		super();
	}

	public Username(File excel) throws IOException {
		super(excel);
	}
	
	/**
	 * 该方法用于添加通过正确的用户名与密码进行登录的测试用例
	 * 
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addRightLoginCase() throws IOException {
		//存储方法名
		String methodName = "addRightLoginCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 该方法用于添加通过非常规的用户名与密码进行登录的测试用例
	 * 
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addErrorLoginCase() throws IOException {
		//存储方法名
		String methodName = "addErrorLoginCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 该方法用于添加通过输入不同的验证码进行登录的测试用例
	 * 
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addCaptchaCase() throws IOException {
		//存储方法名
		String methodName = "addCaptchaCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 该方法用于添加通过不同权限的用户名与密码进行登录的测试用例
	 * 
	 * @param isSparateLogin
	 *            是否需要分页分开登录
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addLoginAuthorityCase(boolean isSparateLogin)
			throws IOException {
		//存储方法名
		String methodName = "addLoginAuthorityCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = null;
		if ( isSparateLogin ) {
			contents = getStep(methodName, -1);
		}
		else {
			contents = getStep(methodName, 1);
		}

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 用于生成注册不同用户名的测试用例
	 * @param isPhone 是否为手机号码注册
	 * @return
	 * @throws IOException
	 */
	public Tab addUsernameRegisterCase(boolean isPhone) throws IOException {
		//存储方法名
		String methodName = "addUsernameRegisterCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = null;
		if ( isPhone ) {
			contents = getStep(methodName, 1, 2, 3, 4, 5, 7);
		}
		else {
			contents = getStep(methodName, 1, 2, 6, 7);
		}

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}
	
	/**
	 * 用于生成通过不同用户名进行忘记密码操作的测试用例
	 * @return
	 * @throws IOException
	 */
	public Tab addUsernameForgetCase() throws IOException {
		//存储方法名
		String methodName = "addUsernameForgetCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}
	
	/**
	 * 用于生成注册不同密码的测试用例
	 * @param operation 操作的类型
	 * @return
	 * @throws IOException
	 */
	public Tab addPasswordRegisterOrForgetCase(String operation) throws IOException {
		//存储方法名
		String methodName = "addPasswordRegisterOrForgetCase";
		//存储变量
		textMap.put("operation", operation);
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}
	
	/**
	 * 用于生成通过不同验证码来注册的测试用例
	 * @param operation 操作的类型
	 * @param isPast 是否有过期时间
	 * @return
	 * @throws IOException
	 */
	public Tab addCodeRegisterOrForgetCase(String operation, boolean isPast) throws IOException {
		//存储方法名
		String methodName = "addCodeRegisterOrForgetCase";
		//存储变量
		textMap.put("operation", operation);
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = null;
		//判断是否有过期时间
		if ( isPast ) {
			contents = getStep(methodName, -1);
		} else {
			contents = getStep(methodName, 1, 2, 3, 4, 5);
		}

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}
	
	/**
	 * 用于生成修改密码的测试用例
	 * @return
	 * @throws IOException
	 */
	public Tab addAlterPasswordCase() throws IOException {
		//存储方法名
		String methodName = "addAlterPasswordCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}
}
