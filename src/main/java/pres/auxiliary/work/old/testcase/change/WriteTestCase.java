package pres.auxiliary.work.old.testcase.change;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import pres.auxiliary.selenium.xml.IncorrectXmlPathException;
import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.old.testcase.writecase.AddInformation;
import pres.auxiliary.work.old.testcase.writecase.BrowseList;
import pres.auxiliary.work.old.testcase.writecase.FileType;
import pres.auxiliary.work.old.testcase.writecase.InputType;
import pres.auxiliary.work.old.testcase.writecase.PhoneType;

/**
 * 该类用于通过XML文件结构来生成测试用例
 * 
 * @author 彭宇琦
 * @version V1.0
 * @since JDK 1.7
 * @since DOM4J 1.6.1
 * @since POI 13.0
 */
public class WriteTestCase extends WriteCase {
	// 用于生成添加信息的测试用例类
	AddInformation ai = null;
	// 用于生成浏览信息的测试用例类
	BrowseList bl = null;

	public WriteTestCase(File xmlFile) throws DocumentException {
		setDocument(xmlFile);
	}

	public WriteTestCase(Document dom) throws DocumentException {
		setDocument(dom);
	}

	@Override
	public void run() {
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write() throws IOException {
		// 获取根节点
		Element root = dom.getRootElement();

		// 创建禅道模版
		// 添加创建模版的属性，判断各个属性是否正确填入
		// 判断用例存放位置是否定义
		if (root.attribute("case_file_path") != null
				&& !root.attributeValue("case_file_path").equals("")) {
			ZentaoTemplet.setSavePath(root.attributeValue("case_file_path"));
		} else {
			throw new IncorrectXmlPathException(
					"未定义XML中case标签的\"case_file_path\"属性的值");
		}

		// 判断用例名称是否定义
		if (root.attribute("name") != null
				&& !root.attributeValue("name").equals("")) {
			ZentaoTemplet.setFileName(root.attributeValue("name"));
		} else {
			throw new IncorrectXmlPathException("未定义XML中case标签的\"name\"属性的值");
		}

		// 创建模版
		ZentaoTemplet.create();

		// 判断是否添加模块与需求的数据有效性（若属性不存在或者为空则表示不添加）
		if (root.attribute("module_file_path") != null
				&& !root.attributeValue("module_file_path").equals("")) {
			ZentaoTemplet.readModlueData(new File(root
					.attributeValue("module_file_path")));
		}
		if (root.attribute("story_file_path") != null
				&& !root.attributeValue("story_file_path").equals("")) {
			ZentaoTemplet.readStoryData(new File(root
					.attributeValue("story_file_path")));
		}

		// 遍历根节点下所有的子节点，即遍历所有需要写测试用例的子模块
		for (@SuppressWarnings("unchecked")
		Iterator<Element> modules = root.elementIterator(); modules.hasNext();) {
			disposeModule(modules.next());
		}

		// 添加数据有效性
		ZentaoTemplet.setAllDataValidation();
	}

	@Override
	protected void disposeModule(Element module) throws IOException {
		// 判断是否已经创建了AddInformation、BrowseList对象，若未创建，则创建对象，若已创建，则不再创建对象
		if (ai == null) {
			ai = new AddInformation();
		}
		if (bl == null) {
			bl = new BrowseList();
		}

		// 判断XML中是否设置了模块的名称
		if (module.attribute("name") != null
				&& !module.attributeValue("name").equals("")) {
			ai.setModule(module.attributeValue("name"));
		} else {
			throw new IncorrectXmlPathException("未定义XML中module标签的\"name\"属性的值");
		}

		// 对模块中的生成用例的类型
		for (@SuppressWarnings("unchecked")
		Iterator<Element> types = module.elementIterator(); types.hasNext();) {
			disposeType(types.next());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void disposeType(Element type) throws IOException {
		// 判断待生成的测试用例类型
		// TODO 若新添加测试用例的类型，则需要向此处添加
		if (type.getName().equalsIgnoreCase("form")) {
			// 添加“新增信息”类型的测试用例
			// 设置新增的信息名称
			if (type.attribute("inormation") != null
					&& !type.attributeValue("inormation").equals("")) {
				ai.setInformationName(type.attributeValue("inormation"));
			} else {
				throw new IncorrectXmlPathException(
						"未定义XML中form标签的\"inormation\"属性的值");
			}

			// 用于定位元素
			Element temp = null;
			// 设置添加信息成功的预期
			if ((temp = type.element("success")) != null) {
				// 判断元素是否有内容，没有内容则不进行设置
				if (!"".equals(temp.getText())) {
					ai.setSuccessExpectation(temp.getText());
				}
			}

			// 设置失败时的期望
			if ((temp = type.element("fail")) != null) {
				// 判断元素是否有内容，没有内容则不进行设置
				if (!"".equals(temp.getText())) {
					ai.setFailExpectation(temp.getText());
				}
			}

			// 设置前置条件
			if ((temp = type.element("precondition")) != null) {
				// 读取填写的所有前置条件的步骤
				for (Iterator<Element> step = temp.elementIterator(); step
						.hasNext();) {
					ai.setPrecondition(step.next().getText());
				}
			}

			// 读取提交按钮名称
			if ((temp = type.element("submit")) != null) {
				if (!"".equals(temp.attribute("name"))) {
					ai.setButtonName(temp.attributeValue("name"));
				}
			} else {
				throw new IncorrectXmlPathException("未定义提交信息按钮标签");
			}
			
			//写入添加信息必要的前两条用例
			ai.addWholeInformationCase();
			ai.addUnWholeInformationCase();
			
			// 读取待写入测试用例的元素
			if ((temp = type.element("element")) != null) {
				// 循环，将调用处理元素的方法将所有的元素生成测试用例
				for (Iterator<Element> elements = temp.elementIterator(); elements
						.hasNext();) {
					disposeElement(elements.next());
				}
			} else {
				throw new IncorrectXmlPathException("未定义元素标签");
			}
		} else if (type.getName().equalsIgnoreCase("list")) {
			// 添加浏览列表的测试用例
			// 判断是否存在列表名称
			if (type.attribute("name") != null
					&& !type.attributeValue("name").equals("")) {
			} else {
				throw new IncorrectXmlPathException(
						"未定义XML中list标签的\"name\"属性的值");
			}

			// 判断需要浏览的列表属于何种样式
			if (type.attribute("type") != null
					&& !type.attributeValue("type").equals("")) {
				if (type.attributeValue("type").equals("web")) {
					bl.addWebBrowseListCase(type.attributeValue("name"));
				} else {
					bl.addAppBrowseListCase(type.attributeValue("name"));
				}
			}
		} else if (type.getName().equalsIgnoreCase("search")) {
			// 添加搜索列表的测试用例
			// 判断是否存在列表名称
			if (type.attribute("name") != null
					&& !type.attributeValue("name").equals("")) {
			} else {
				throw new IncorrectXmlPathException(
						"未定义XML中search标签的\"name\"属性的值");
			}

			// 循环，将调用处理元素的方法将所有的元素生成测试用例
			for (Iterator<Element> elements = type.elementIterator(); elements
					.hasNext();) {
				disposeElement(elements.next());
			}
		}
	}

	@Override
	protected void disposeElement(Element element) throws IOException {
		//存储父节点的标签的名称
		String parentName = element.getParent().getName();
		//判断标签的类型（即控件的类型）
		if (element.getName().equalsIgnoreCase("textbox")) {
			//判断父标签是什么种类的标签，并使用其对应的方法
			if ( parentName.equalsIgnoreCase("element") ) {
				//定义属性
				String name;
				boolean isMust = true;
				boolean isRepeat = false;
				char[] inputConfine;
				int[] lengthConfine;
				int[] numConfine;
				
				//判断是否定义控件的名称并存储控件的名称
				if ( (name = element.attributeValue("name")) == null) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
				}
				
				//判断是否定义了是否必填的属性
				try {
					isMust = Boolean.valueOf(element.attributeValue("must"));
				} catch (NullPointerException e) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
				}
				
				//判断是否定义了是否重复的属性
				try {
					isRepeat = Boolean.valueOf(element.attributeValue("repeat"));
				} catch (NullPointerException e) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"repeat\"属性的值");
				}
				
				//用于获取到被分割后的输入限制
				String[] s = null;
						
				//判断是否定义了输入限制的属性
				try {
					//判断获取到的元素是否为空或是字符串null
					if ( element.attributeValue("input_confine").equals("") || element.attributeValue("input_confine").equals("null") ) {
						inputConfine = null;
					} else {
						//分割获取到的字符串
						//TODO 在实现信息采集功能后分隔符应改为可自定义
						s = element.attributeValue("input_confine").split(",");
						//定义用于临时存放数据的数组
						ArrayList<Character> temp = new ArrayList<Character>();
						
						//解析输入的限制，将其转为char类型存入数组中
						for ( String str : s ) {
							//将字符串中的信息转为小写
							str = str.toLowerCase();
							//判断限制的信息，并将存入tem中
							switch ( str ) {
							case "num":
								temp.add(InputType.NUM);
								break;
							case "en":
								temp.add(InputType.EN);
								break;
							case "ch":
								temp.add(InputType.CH);
								break;
							case "spe":
								temp.add(InputType.SPE);
								break;
							default:
								break;
							}
						}
						
						//判断temp中是否有元素存在，若不为0，则存储，反之则将inputConfine设为null
						if ( temp.size() != 0 ) {
							//初始化inputConfine数组
							inputConfine = new char[temp.size()];
							//将元素存入inputConfine数组中
							for ( int i = 0; i < temp.size(); i++ ) {
								inputConfine[i] = temp.get(i).charValue();
							}
						} else {
							inputConfine = null;
						}
					}
					
				} catch (NullPointerException e) {
					//若抛出空指针异常时，则将输入限制置为null
					inputConfine = null;
				}
				
				//获取长度限制信息
				try {
					//判断获取到的元素是否为空或是字符串null
					if ( element.attributeValue("length_confine").equals("") || element.attributeValue("length_confine").equals("null") ) {
						lengthConfine = null;
					} else {
						//分割获取到的字符串
						//TODO 在实现信息采集功能后分隔符应改为可自定义
						s = element.attributeValue("length_confine").split(",");
						//定义用于临时存放数据的数组
						ArrayList<Integer> temp = new ArrayList<Integer>();
						
						//循环，获取长度限制，存入temp中，若出现无法转换的字符串，则不进行存储
						for ( String str : s ) {
							try {
								//判断字符串是否是NAN
								if ( str.equalsIgnoreCase("nan") ) {
									temp.add(ai.NUM_NAN);
								} else {
									temp.add(Integer.valueOf(str));
								}
							} catch(NumberFormatException e) {
								continue;
							}
						}
						
						//判断temp中是否有元素存在，若不为0，则存储，反之则将lengthConfine设为null
						if ( temp.size() != 0 ) {
							//初始化lengthConfine数组
							lengthConfine = new int[temp.size()];
							//将元素存入inputConfine数组中
							for ( int i = 0; i < temp.size(); i++ ) {
								lengthConfine[i] = temp.get(i).intValue();
							}
						} else {
							lengthConfine = null;
						}
					}
																																																																																																																																																																																																																																		
				} catch (NullPointerException e) {
					//若抛出空指针异常时，则将长度限制置为null
					lengthConfine = null;
				}
				
				//获取数字大小限制信息
				try {
					//判断获取到的元素是否为空或是字符串null
					if ( element.attributeValue("num_confine").equals("") || element.attributeValue("num_confine").equals("null") ) {
						numConfine = null;
					} else {
						//分割获取到的字符串
						//TODO 在实现信息采集功能后分隔符应改为可自定义
						s = element.attributeValue("num_confine").split(",");
						//定义用于临时存放数据的数组
						ArrayList<Integer> temp = new ArrayList<Integer>();
						
						//循环，获取长度限制，存入temp中，若出现无法转换的字符串，则不进行存储
						for ( String str : s ) {
							try {
								//判断字符串是否是NAN
								if ( str.equalsIgnoreCase("nan") ) {
									temp.add(ai.NUM_NAN);
								} else {
									temp.add(Integer.valueOf(str));
								}
								
							} catch(NumberFormatException e) {
								continue;
							}
						}
						
						//判断temp中是否有元素存在，若不为0，则存储，反之则将numConfine设为null
						if ( temp.size() != 0 ) {
							//初始化numConfine数组
							numConfine = new int[temp.size()];
							//将元素存入numConfine数组中
							for ( int i = 0; i < temp.size(); i++ ) {
								numConfine[i] = temp.get(i).intValue();
							}
						} else {
							numConfine = null;
						}
					}
																																																																																																																																																																																																																																		
				} catch (NullPointerException e) {
					//若抛出空指针异常时，则将长度限制置为null
					numConfine = null;
				}
				
				//添加测试用例
				ai.addTextboxCase(name, isMust, isRepeat, inputConfine, lengthConfine, numConfine);
				
			} else if ( parentName.equalsIgnoreCase("search") ) {
				//判断获取到的元素是否为空或是字符串null
				if ( element.attributeValue("name").equals("") || element.attribute("name") == null ) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
				} else {
					//添加测试用例
					bl.addInputSearchCase(element.attributeValue("name"), element.getParent().attributeValue("name"));
				}
			}
		} else if ( element.getName().equalsIgnoreCase("select") ) {
			//设置类型为下拉框时的测试用例
			//判断父节点的类型，并添加相应的测试用例
			if ( parentName.equalsIgnoreCase("element") ) {
				//若父节点的名称为element时，则添加ai中的测试用例
				//定义编写测试用例时需要传入的属性
				String name;
				boolean isMust;
				
				//判断是否定义控件的名称并存储控件的名称
				if ( (name = element.attributeValue("name")) == null) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
				}
				
				//判断是否定义了是否必填的属性
				try {
					isMust = Boolean.valueOf(element.attributeValue("must"));
				} catch (NullPointerException e) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
				}
				
				//添加测试用例
				ai.addSelectboxCase(name, isMust);
				
			} else if ( parentName.equalsIgnoreCase("search") ) {
				//若父节点的名称为search时，则添加bl中的测试用例
				//判断获取到的元素是否为空或是字符串null
				if ( element.attributeValue("name").equals("") || element.attribute("name") == null ) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
				} else {
					//添加测试用例
					bl.addSelectSearchCase(element.attributeValue("name"), element.getParent().attributeValue("name"));
				}
			}
		} else if ( element.getName().equalsIgnoreCase("radio") ) {
			//添加单选按钮的测试用例
			//定义编写测试用例时需要传入的属性
			String name;
			boolean isMust;
			
			//判断是否定义控件的名称并存储控件的名称
			if ( (name = element.attributeValue("name")) == null) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isMust = Boolean.valueOf(element.attributeValue("must"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
			}
			
			//添加测试用例
			ai.addRadioButtonCase(name, isMust);
		} else if ( element.getName().equalsIgnoreCase("check") ) {
			//定义编写测试用例时需要传入的属性
			String name;
			boolean isMust;
			
			//判断是否定义控件的名称并存储控件的名称
			if ( (name = element.attributeValue("name")) == null) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isMust = Boolean.valueOf(element.attributeValue("must"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
			}
			
			//添加测试用例
			ai.addCheckboxCase(name, isMust);
		} else if ( element.getName().equalsIgnoreCase("date") ) {
			//添加与日期相关的测试用例，包括普通日期，开始与结束相应的日期
			//定义编写测试用例时需要传入的属性
			String name;
			boolean isMust;
			boolean isInput;
			String typeName;
			
			//判断是否定义控件的名称并存储控件的名称
			if ( (name = element.attributeValue("name")) == null) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isMust = Boolean.valueOf(element.attributeValue("must"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
			}
			
			//判断是否定义了是否可输入的属性
			try {
				isInput = Boolean.valueOf(element.attributeValue("input"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"input\"属性的值");
			}
			
			//获取type属性中的内容，用以添加对应的测试用例
			typeName = element.attributeValue("type");
			//判断是否定义日期的类型并按照类型添加不同的测试用例
			//若为获取到type中的内容，则添加普通日期的测试用例
			if ( typeName == null || typeName.equals("")) {
				ai.addDateCase(name, isMust, isInput);
			} else if ( typeName.equalsIgnoreCase("start") ) {
				//若获取到的样式为start，则在该元素标签的兄弟节点中查找带end标记的标签
				//若未找到被end标记的标签，则仍然按照普通日期添加用例
				//若查找到该标签，则将标签name属性值赋给typeName
				//NOTE:被end标记的日期读取方式与其相反
				if ( element.selectSingleNode("../date[@type='end']") == null ) {
					ai.addDateCase(name, isMust, isInput);
				} else {
					Element d = (Element) element.selectSingleNode("../date[@type='end']");
					ai.addStartDateCase(name, isMust, isInput, d.attributeValue("name"));
				}
			} else if ( typeName.equalsIgnoreCase("end") ) {
				if ( element.selectSingleNode("../date[@type='start']") == null ) {
					ai.addDateCase(name, isMust, isInput);
				} else {
					Element d = (Element) element.selectSingleNode("../date[@type='start']");
					ai.addEndDateCase(name, isMust, isInput, d.attributeValue("name"));
				}
			}
		} else if ( element.getName().equalsIgnoreCase("phone") ) {
			//添加与号码相关的测试用例
			//定义编写测试用例时需要传入的属性
			String name;
			boolean isMust;
			boolean isRepeat;
			PhoneType phone;
			
			//判断是否定义控件的名称并存储控件的名称
			if ( (name = element.attributeValue("name")) == null) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isMust = Boolean.valueOf(element.attributeValue("must"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isRepeat = Boolean.valueOf(element.attributeValue("repeat"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"repeat\"属性的值");
			}
			
			//定义一个临时存储获取属性值的字符串变量，用以存储从type属性中获取到的值
			String temp;
			//判断是否定义号码的类型并存储其值，若未定义，则抛出异常，若已定义，则对类型进行处理
			if ( (temp = element.attributeValue("type")) == null ) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"type\"属性的值");
			} else {
				//判断temp中的内容，并设置其对应的PhoneType
				if ( temp.equalsIgnoreCase("moble") ) {
					phone = PhoneType.MOBLE;
				} else if ( temp.equalsIgnoreCase("fixed") ) {
					phone = PhoneType.FIXED;
				} else {
					throw new IncorrectXmlPathException(
							"XML中" + element.getName() + "标签的\"type\"属性值不能为：" + temp);
				}
			}
			
			ai.addPhoneCase(name, isMust, isRepeat, phone);
		} else if ( element.getName().equalsIgnoreCase("idcard") ) {
			//添加与身份证相关的测试用例
			//定义编写测试用例时需要传入的属性
			String name;
			boolean isMust;
			boolean isRepeat;
			
			//判断是否定义控件的名称并存储控件的名称
			if ( (name = element.attributeValue("name")) == null) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isMust = Boolean.valueOf(element.attributeValue("must"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isRepeat = Boolean.valueOf(element.attributeValue("repeat"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"repeat\"属性的值");
			}
			
			ai.addIDCardCase(name, isMust, isRepeat);
		} else if ( element.getName().equalsIgnoreCase("upload") ) {
			//添加上传文件的测试用例（包括上传文件以及上传图片）
			//定义基本参数
			String name;
			boolean isMust;
			boolean isRepeat;
			boolean isSizeConfine;
			char[] fileConfine;
			int[] fileNumConfine;
			
			//判断是否定义控件的名称并存储控件的名称
			if ( (name = element.attributeValue("name")) == null) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"name\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isMust = Boolean.valueOf(element.attributeValue("must"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"must\"属性的值");
			}
			
			//判断是否定义了是否必填的属性
			try {
				isRepeat = Boolean.valueOf(element.attributeValue("repeat"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"repeat\"属性的值");
			}
			
			//判断是否定义了是否有大小限制的属性
			try {
				isSizeConfine = Boolean.valueOf(element.attributeValue("size"));
			} catch (NullPointerException e) {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"size\"属性的值");
			}
			
			//用于获取到被分割后的输入限制
			String[] s = null;
					
			//判断是否定义了格式限制的属性
			try {
				//判断获取到的元素是否为空或是字符串null
				if ( element.attributeValue("file_confine").equals("") || element.attributeValue("file_confine").equals("null") ) {
					fileConfine = null;
				} else {
					//分割获取到的字符串
					//TODO 在实现信息采集功能后分隔符应改为可自定义
					s = element.attributeValue("file_confine").split(",");
					//定义用于临时存放数据的数组
					ArrayList<Character> temp = new ArrayList<Character>();
					
					//解析输入的限制，将其转为char类型存入数组中
					for ( String str : s ) {
						//将字符串中的信息转为小写
						str = str.toLowerCase();
						//判断限制的信息，并将存入tem中
						switch ( str ) {
						case "jpg":
							temp.add(FileType.JPG);
							break;
						case "gif":
							temp.add(FileType.GIF);
							break;
						case "png":
							temp.add(FileType.PNG);
							break;
						case "bmp":
							temp.add(FileType.BMP);
							break;
						case "doc":
							temp.add(FileType.DOC);
							break;
						case "docx":
							temp.add(FileType.DOCX);
							break;
						case "xls":
							temp.add(FileType.XLS);
							break;
						case "xlsx":
							temp.add(FileType.XLSX);
							break;
						case "txt":
							temp.add(FileType.TXT);
							break;
						default:
							break;
						}
					}
					
					//判断temp中是否有元素存在，若不为0，则存储，反之则将fileConfine设为null
					if ( temp.size() != 0 ) {
						//初始化inputConfine数组
						fileConfine = new char[temp.size()];
						//将元素存入inputConfine数组中
						for ( int i = 0; i < temp.size(); i++ ) {
							fileConfine[i] = temp.get(i).charValue();
						}
					} else {
						fileConfine = null;
					}
				}
				
			} catch (NullPointerException e) {
				//若抛出空指针异常时，则将输入限制置为null
				fileConfine = null;
			}
			
			//获取文件（图片）个数限制信息
			try {
				//判断获取到的元素是否为空或是字符串null
				if ( element.attributeValue("num_confine").equals("") || element.attributeValue("num_confine").equals("null") ) {
					fileNumConfine = null;
				} else {
					//分割获取到的字符串
					//TODO 在实现信息采集功能后分隔符应改为可自定义
					s = element.attributeValue("num_confine").split(",");
					//定义用于临时存放数据的数组
					ArrayList<Integer> temp = new ArrayList<Integer>();
					
					//循环，获取长度限制，存入temp中，若出现无法转换的字符串，则不进行存储
					for ( String str : s ) {
						try {
							//判断字符串是否是NAN
							if ( str.equalsIgnoreCase("nan") ) {
								temp.add(ai.NUM_NAN);
							} else {
								temp.add(Integer.valueOf(str));
							}
						} catch(NumberFormatException e) {
							continue;
						}
					}
					
					//判断temp中是否有元素存在，若不为0，则存储，反之则将lengthConfine设为null
					if ( temp.size() != 0 ) {
						//初始化lengthConfine数组
						fileNumConfine = new int[temp.size()];
						//将元素存入inputConfine数组中
						for ( int i = 0; i < temp.size(); i++ ) {
							fileNumConfine[i] = temp.get(i).intValue();
						}
					} else {
						fileNumConfine = null;
					}
				}
																																																																																																																																																																																																																																	
			} catch (NullPointerException e) {
				//若抛出空指针异常时，则将长度限制置为null
				fileNumConfine = null;
			}
			
			//获取type属性，判断上传的文件类型，并添加对应的测试用例
			if ( element.attributeValue("type").equalsIgnoreCase("file") ) {
				//如果是文件类型，则根据其属性添加测试用例
				ai.addUploadFileCase(name, isMust, isRepeat, isSizeConfine, fileConfine, fileNumConfine);
			} else if ( element.attributeValue("type").equalsIgnoreCase("image") ) {
				//如果是图片类型，则继续获取其两个属性值
				boolean isPhotogeraph;
				boolean isUpload;
				
				//判断是否定义了是否允许拍照上传的限制
				try {
					isPhotogeraph = Boolean.valueOf(element.attributeValue("photogeraph"));
				} catch (NullPointerException e) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"photogeraph\"属性的值");
				}
				
				//判断是否定义了是否允许从文件夹或者相册等本地文件直接上传的限制
				try {
					isUpload = Boolean.valueOf(element.attributeValue("upload"));
				} catch (NullPointerException e) {
					throw new IncorrectXmlPathException(
							"未定义XML中" + element.getName() + "标签的\"upload\"属性的值");
				}
				
				ai.addUploadImageCase(name, isMust, isRepeat, isPhotogeraph, isUpload, isSizeConfine, fileConfine, fileNumConfine);
				
			} else {
				throw new IncorrectXmlPathException(
						"未定义XML中" + element.getName() + "标签的\"type\"属性的值");
			}
		}
		
		//TODO 此处之上，用于添加需要添加测试用例的类型
	}
}
