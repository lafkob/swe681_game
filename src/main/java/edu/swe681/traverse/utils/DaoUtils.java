package edu.swe681.traverse.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utils for the dao layer.
 */
public class DaoUtils {
	
	/**
	 * Gets the Integer value of a column in the ResultSet. Necessary because the default
	 * value for getInt is 0 if the column is null. This handles this by returning null instead.
	 * 
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public static Integer getInteger(ResultSet rs, String columnName) throws SQLException {
		int retVal = rs.getInt(columnName);
		if(rs.wasNull()) return null;
		else return retVal;
	}
	
	/**
	 * Gets the Long value of a column in the ResultSet. Necessary because the default
	 * value for getLong is 0 if the column is null. This handles this by returning null instead.
	 * 
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public static Long getLong(ResultSet rs, String columnName) throws SQLException {
		long retVal = rs.getLong(columnName);
		if(rs.wasNull()) return null;
		else return retVal;
	}
}
