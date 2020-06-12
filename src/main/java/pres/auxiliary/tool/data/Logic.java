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
	 * 用于标记对信息的分隔
	 */
	private final String GROUP_SIGN = "group=";
	
	/**
	 * 表示逻辑的入口
	 */
	private String mainGroup = "";
	
	/**
	 * 存储条件组
	 */
	private HashMap<String, ConditionGroup> conditionGroupMap = new HashMap<>(16);
	
	/**
	 * 初始化一个条件组，以作为整个逻辑的开始
	 * @param conditionGroupName 条件组名称
	 * @param condition 条件类对象
	 * @param not 是否对条件取反
	 */
	public Logic(String conditionGroupName, Condition<?> condition, boolean not) {
		//定义条件组，并加上根条件
		conditionGroupMap.put(conditionGroupName, new ConditionGroup(condition, not));
		//将逻辑类的入口定义为相应的条件组
		mainGroup = GROUP_SIGN + conditionGroupName;
	}
	
	/**
	 * 向逻辑中添加一个条件组，并向条件组中添加一个根条件
	 * @param groupName 条件组名称
	 * @param condition 条件类对象
	 */
	public void addGroup(String groupName, Condition<?> condition) {
		
	}
	
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
		 * 构造条件组，并向其中添加条件，该条件作为逻辑组的根条件使用
		 * @param condition 条件类对象
		 * @param not 是否对条件取反
		 */
		public ConditionGroup(Condition<?> condition, boolean not) {
			//判断是否需要对条件进行取反
			if (not) {
				conditions.add(NOT + condition.toString());
			} else {
				conditions.add(condition.toString());
			}
		}
		
		/**
		 * 构造条件组，并向其中添加另一组条件组，该条件组作为逻辑组的根条件使用
		 * @param condition 条件类对象
		 * @param not 是否对条件取反
		 */
		public ConditionGroup(String groupName, boolean not) {
			//判断条件组是否存在
			if (!conditionGroupMap.containsKey(groupName)) {
				throw new GroupNotFoundException("不存在的条件组：" + groupName);
			}
			
			//添加条件组，并用开始和结束标志来标记条件组名称
			//判断是否需要对条件进行取反
			if (not) {
				//添加条件组，并用开始和结束标志来标记条件组名称
				conditions.add(NOT + GROUP_SIGN + groupName);
			} else {
				conditions.add(GROUP_SIGN + groupName);
			}
		}

		
		/**
		 * 向条件组中添加正条件，即数据根据条件得到的结果无需取反
		 * @param condition 条件类对象
		 * @param relationType 与前一个条件间的关系，见{@link RelationType}枚举类
		 * @param not 是否对条件取反
		 */
		public void addCondition(Condition<?> condition, RelationType relationType, boolean not) {
			//判断是否需要对条件进行取反
			if (not) {
				conditions.add(NOT + condition.toString());
			} else {
				conditions.add(condition.toString());
			}
			
			relations.add(relationType);
		}
		
		/**
		 * 向条件组中添加另一个正条件组，即数据根据条件得到的结果无需取反
		 * @param groupName 条件组名称
		 * @param relationType 与前一个条件间的关系，见{@link RelationType}枚举类
		 * @param not 是否对条件取反
		 */
		public void addConditionGroup(String groupName, RelationType relationType, boolean not) {
			//判断条件组是否存在
			if (!conditionGroupMap.containsKey(groupName)) {
				throw new GroupNotFoundException("不存在的条件组：" + groupName);
			}
			
			//判断是否需要对条件进行取反
			if (not) {
				//添加条件组，并用开始和结束标志来标记条件组名称
				conditions.add(NOT + GROUP_SIGN + groupName);
			} else {
				conditions.add(GROUP_SIGN + groupName);
			}
			relations.add(relationType);
		}
		
		/**
		 * <p>
		 * 根据条件的索引值，对添加在条件组中的条件进行删除，条件在条件组中的索引值为添加条件的顺序。
		 * 该方法亦可以删除根条件，删除根条件后，其下一个条件将作为根条件，并删除其与之前的根节点的条件，
		 * 删除普通条件时，将删除被删除的条件与上一个条件的逻辑关系。
		 * </p>
		 * <p>
		 * 例如：存在逻辑“p∧q∨a”（p、q、a均为条件，p为根条件）
		 * <ul>
		 * 	<li>删除条件p时，则逻辑变为：“q∨a”</li>
		 * 	<li>删除条件q时，则逻辑变为：“p∨a”</li>
		 * </ul>
		 * </p>
		 * @param index 条件索引值
		 */
		public void deleteCondition(int index) {
//			conditions.remove(index);
			//删除分为两种情况：
			//1.删除根条件，则连同删除与下一个条件间的逻辑关系
			//2.删除普通条件，则连同删除与上一个条件间的逻辑关系
			
			//先删除条件，若传入的下标有误，则可以先抛出异常，而不会删除关系
			conditions.remove(index);
			//判断删除的条件是否为根条件
			if (index == 0) {
				relations.remove(0);
			} else {
				//根据存储机制，被删除的节点与上一个节点的关系将为相应的下标 - 1（根节点除外）
				relations.remove(index - 1);
			}
		}

		/**
		 * 清空条件组
		 */
		public void clearCondition() {
			//清空条件集合及关系集合
			conditions.clear();
			relations.clear();
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
