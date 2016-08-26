package com.lezhi.app.mybatis;

public abstract class Dialect {

	public enum Type {
		MYSQL, ORACLE, SQLSERVER
	}

	public abstract String getLimitString(String querySqlString, int offset, int limit);

}
