package org.ideoholic.datamigrator.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
	private final Connection connection;
	private List<PreparedStatement> listOfStatements;
	private static DBUtils thisObject = null;

	private DBUtils() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mifostenant-default", "root", "mysql");
		connection.setAutoCommit(false);
		listOfStatements = new ArrayList<PreparedStatement>();
	}

	public static synchronized DBUtils getInstance() throws ClassNotFoundException, SQLException {
		if (thisObject == null)
			thisObject = new DBUtils();
		return thisObject;
	}

	public void executePreparedStatement(String sql) throws SQLException {
		System.out.println("DBUtils.executePreparedStatement()::going to run SQL:" + sql);
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.closeOnCompletion();
		statement.execute();
	}

	public ResultSet executeQueryStatement(String sql) throws SQLException {
		System.out.println("DBUtils.executeQueryStatement()::going to run SQL:" + sql);
		PreparedStatement statement = connection.prepareStatement(sql);
		// Store the statement here to enable closing when done
		listOfStatements.add(statement);
		return statement.executeQuery();
	}

	/**
	 * This is method is used to close any statements that are not yet closed. The
	 * try-catch within the loop ensures that all the statements are iterated over,
	 * in case any one of the connection throws an exception, the exception is
	 * consumed, logged and execution continues
	 */
	public void closeAllStatements() {
		for (PreparedStatement statement : listOfStatements) {
			try {
				if (!statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		listOfStatements.clear();
	}

	public void commitTransaction() {
		System.out.println("DBUtils.commitTransaction(): Committing");
		try {
			closeAllStatements();
			if (connection.isValid(1)) {
				connection.commit();
				connection.close();
				thisObject = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("DBUtils.commitTransaction(): Committed");
	}

	public void rollbackTransaction() {
		System.out.println("DBUtils.rollbackTransaction(): Rolling back");
		try {
			closeAllStatements();
			if (connection.isValid(1)) {
				connection.rollback();
				connection.close();
				thisObject = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("DBUtils.rollbackTransaction(): Rolled back");
	}

	/**
	 * This is the destructor that is invoked by JVM when the class gets unloaded
	 * from memory
	 */
	@Override
	public void finalize() {
		closeAllStatements();
		commitTransaction();
	}

}
