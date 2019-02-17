package com.sateekot.soccer.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sateekot.soccer.exception.SoccerDBException;
import com.sateekot.soccer.utils.SoccerConstants;

/**
 * 
 * @author sateekot
 * Date: 14-02-2019
 * Provides h2 database connections.
 */
public class H2ConnectionFactory {
	
	private static final Logger LOGGER = Logger.getLogger(H2ConnectionFactory.class.getName());

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(SoccerConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(SoccerConstants.DB_URL);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Exception occured while getting database connection and details = "+e.getMessage());
		}
		return connection;
	}
	
	public static void closeConnection(Connection connection) throws SoccerDBException {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException ex) {
				throw new SoccerDBException("Exception occured while closing the database connection.", ex);
			}
		}
	}
}
