package pres.auxiliary.tool.sql;

public enum SqlType {
	/**
	 * 查询
	 */
	SELECT("SELECT"),
	/**
	 * 删除
	 */
	DELECT("DELECT"),
	/**
	 * 插入
	 */
	INSERT("INSERT INTO"),
	/**
	 * 更新
	 */
	UPDATE("UPDATE"),
	/**
	 * 清除表数据
	 */
	TRUNCATE("TRUNCATE TABLE"),
	/**
	 * 执行存储过程
	 */
	CALL("CALL")
	;
	String name;

	private SqlType(String name) {
		this.name = name;
	}
	
	/**
	 * 返回SQL名称
	 * @return SQL名称
	 */
	public String getName() {
		return name;
	}
}
