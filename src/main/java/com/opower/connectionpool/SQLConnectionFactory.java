package com.opower.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A factory for creating connections for accessing and processing data stored 
 * in a data source (usually a relational database)
 *
 * @see DriverManager#getConnection
 * 
 * @author dhagan
 */
public class SQLConnectionFactory implements ConnectionFactory {

    private Properties properties;

    /**
     * <P>
     * Creates a new <code>SQLConnectionFactory</code> object.
     * 
     * <P>
     * NOTED: The DataSource interface, new in the JDBC 2.0 API, provides another way to 
     * connect to a data source. The use of a DataSource object is the preferred means of 
     * connecting to a data source
     * 
     * @param driverClassName 
     * @param url a database url of the form jdbc:subprotocol:subname
     * @param user the database user on whose behalf the connection is being made
     * @param password the user's password
     * @throws Exception 
     * 
     * @see DriverManager
     */
    public SQLConnectionFactory(String driverClassName, String url, String user, String password) throws Exception {
        Class.forName(driverClassName).newInstance();
        properties = new Properties();
        properties.put("url", url);
        properties.put("user", user);
        properties.put("password", password);
    }

    /**
     * <P>
     * Attempts to establish a connection to the given database URL. The DriverManager 
     * attempts to select an appropriate driver from the set of registered JDBC drivers.
     * 
     * @return a connection
     * @throws SQLException 
     * 
     * @see DriverManager#getConnection
     */
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties);
        return connection;
    }
}
