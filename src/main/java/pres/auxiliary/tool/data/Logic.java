package pres.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p><b>文件名：</b>Condition.java</p>
 * <p><b>用途：</b>该类用于对文本的筛选条件进行编辑，可编辑的条件包含字符串、时间及数字</p>
 * <p><b>编码时间：</b>2020年1月2日上午7:48:53</p>
 * <p><b>修改时间：</b>2020年1月2日上午7:48:53</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class Logic {
	/**
	 * 用于定义条件组名称开始标记
	 */
	private final String GROUP_NAME_START_SINGN = "&{";
	/**
	 * 用于定义条件组名称结束标记
	 */
	private final String GROUP_NAME_END_SINGN = "}&";
	
	/**
	 * 拼接条件
	 */
	private StringBuilder logic = new StringBuilder("");
	
	/**
	 * 存储条件组
	 */
	private HashMap<String, ConditionGroup> conditionGroupMap = new HashMap<>(16);
	
	/**
	 * 构造Logic类，并初始化一个条件组，以作为整个逻辑的开始
	 * @param conditionGroupName 条件组名称
	 * @param condition 条件类对象
	 */
	public Logic(String conditionGroupName, Condition<?> condition) {
		//定义条件组，并加上根条件
		conditionGroupMap.put(conditionGroupName, new ConditionGroup(condition));
		//向逻辑中添加条件组名称
		logic.append(GROUP_NAME_START_SINGN + conditionGroupName + GROUP_NAME_END_SINGN);
	}
	
	/**
	 * 向条件组中添加条件，需要先添加根条件
	 * @param condition 条件类对象
	 * @param relationType 条件间关系（RelationType枚举）
	 * @throws GroupNotFoundException 未添加根条件时调用该方法将抛出该异常
	 */
//	public void addCondition(String groupName, Condition<?> condition, RelationType relationType) {
//		if (!conditionGroupMap.containsKey(groupName)) {
//			throw new GroupNotFoundException("不存在的条件组：" + groupName);
//		}
//		
//		conditionGroupMap.get(groupName).addCondition(condition, relationType);
//	}
	
	/**
	 * 返回条件组，可通过该方法对条件组进行增减条件的操作
	 * @param GroupName 条件组名称
	 * @return 条件组类
	 * @throws GroupNotFoundException 条件组不存在时抛出的异常
	 */
	public ConditionGroup getGroup(String groupName) {
		if (!conditionGroupMap.containsKey(groupName)) {
			throw new GroupNotFoundException("不存在的条件组：" + groupName);
		}
		
		return conditionGroupMap.get(groupName);
	}
	
	/**
	 * <p><b> 文件名：</b>Logic.java</p>
	 * <p><b>用途：</b>用于存放条件的组合，包含对条件的增删改查的操作</p>
	 * <p><b>编码时间：</b>2020年1月9日上午7:49:37</p>
	 * <p><b>修改时间：</b>2020年1月9日上午7:49:37</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	class ConditionGroup {
		/**
		 * 用于对条件进行取反的标记
		 */
		private final String NOT = "-";
		
		/**
		 * 存储条件，使用字符串进行存储，其条件类返回的条件也必须是字符串
		 */
		private ArrayList<String> conditions = new ArrayList<>();
		/**
		 * 存储条件间的关系
		 */
		private ArrayList<RelationType> relations = new ArrayList<>();
		
		/**
		 * 构造条件组，并向其中添加根条件
		 * @param condition 条件类对象
		 */
		public ConditionGroup(Condition<?> condition) {
			conditions.add(condition.toString());
		}
		
		/**
		 * 向条件组中添加正条件，即数据根据条件得到的结果无需取反
		 * @param condition 条件类对象
		 * @param relationType 与前一个条件间的关系，见{@link RelationType}枚举类
		 */
		public void addCondition(Condition<?> condition, RelationType relationType) {
			conditions.add(condition.toString());
			relations.add(relationType);
		}
		
		/**
		 * 向条件组中添加另一个正条件组，即数据根据条件得到的结果无需取反
		 * @param groupName 条件组名称
		 * @param relationType 与前一个条件间的关系，见{@link RelationType}枚举类
		 */
		public void addConditionGroup(String groupName, RelationType relationType) {
			//判断条件组是否存在
			if (!conditionGroupMap.containsKey(groupName)) {
				throw new GroupNotFoundException("不存在的条件组：" + groupName);
			}
			
			//添加条件组，并用开始和结束标志来标记条件组名称
			conditions.add(GROUP_NAME_START_SINGN + groupName + GROUP_NAME_END_SINGN);
			relations.add(relationType);
		}
		
		/**
		 * 向条件组中添加反条件，即数据根据条件得到的结果需要取反
		 * @param condition 条件类对象
		 * @param relationType 与前一个条件间的关系，见{@link RelationType}枚举类
		 */
		public void addNotCondition(Condition<?> condition, RelationType relationType) {
			conditions.add(NOT + condition.toString());
			relations.add(relationType);
		}
		
		/**
		 * 向条件组中添加另一个反条件组，即数据根据条件得到的结果需要取反
		 * @param groupName 条件组名称
		 * @param relationType 与前一个条件间的关系，见{@link RelationType}枚举类
		 */
		public void addNotConditionGroup(String groupName, RelationType relationType) {
			//判断条件组是否存在
			if (!conditionGroupMap.containsKey(groupName)) {
				throw new GroupNotFoundException("不存在的条件组：" + groupName);
			}
			
			//添加条件组，并用开始和结束标志来标记条件组名称
			conditions.add(NOT + GROUP_NAME_START_SINGN + groupName + GROUP_NAME_END_SINGN);
			relations.add(relationType);
		}
		
		/**
		 * 根据条件的索引值，对添加在条件组中的条件进行删除。条件在条件组中的索引值为添加条件的顺序
		 * @param index 条件索引值
		 */
		public void deleteCondition(int index) {
			conditions.remove(index);
		}

		/**
		 * 清空条件组
		 */
		public void clearCondition() {
			conditions.clear();
		}
		
		/**
		 * 该方法用于清除一类所有的条件，例如，清空所有对字符串处理的条件
		 */
		public void deleteConditionClass() {
			//TODO 条件类返回结果定下后再确定该位置的写法
		}
		
		/**
		 * 返回条件集合
		 * @return 条件集合
		 */
		public ArrayList<String> getConditions() {
			return conditions;
		}
		
		/**
		 * 返回关系集合
		 * @return 关系集合
		 */
		public ArrayList<RelationType> getRelations() {
			return relations;
		}
	}
}
