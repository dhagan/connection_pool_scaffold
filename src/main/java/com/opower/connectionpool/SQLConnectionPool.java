package com.opower.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * An implementation of ConnectionPool submitted to fulfill the Opower Connection Pool Homework assignment
 *
 * <P>
 * To grade this assignment please compile and run using the test SQLConnectionPoolTest.java which 
 * utilizes JUnit and EasyMock
 * 
 * <P>
 * Features
 * <ul>
 * <li>thread safe
 * <li>handles class of cases where the user closes a connection obtained from the pool
 * <li>closes connections which are older than one minute on the next connection
 * </ul>
 * <P>
 * Assumptions
 * <P>
 * Named the concrete class 'SQL' since the homework interface implies SQLException
 * <P>
 * TODO
 * <ul>
 * <li>Performance - what can be done to improve performance if a isValid() blocks for the maximum
 * is valid connection timeout?  Dial it down to a 'short' amount of time? 
 * Decrease the scope of synchronized?
 * <P>
 * <li>Implement a maximum pool size
 * <li>use log log4j
 * </ul>
 * <P>
 * props to http://sourcemaking.com/design_patterns/object_pool/java
 * 
 * @author dhagan
 * <P>
 * @see Connection#isValid 
 * 
 * 
 */
public class SQLConnectionPool implements ConnectionPool {

    private static int CONNECTION_EXPIRATION_TIME = 60000;
    /**
     * maximum time we are willing to wait to see if a connection is available
     */
    public static int MAX_IS_VALID_CONNECTION_TIMEOUT = 30000;
    private long expirationTime;
    // unlocked - collection of availble connections
    private Hashtable<Connection, Long> unlocked;
    // locked - collection in use connections
    private Hashtable<Connection, Long> locked;
    private ConnectionFactory connectionFactory = null;

    /**
     * 
     * @param _connectionFactory 
     */
    public SQLConnectionPool(ConnectionFactory _connectionFactory) {
        connectionFactory = _connectionFactory;
        expirationTime = CONNECTION_EXPIRATION_TIME;
        locked = new Hashtable<Connection, Long>();
        unlocked = new Hashtable<Connection, Long>();
    }

    /**
     * Gets a connection from the connection pool.
     * 
     * @return a valid connection from the pool.
     * @throws SQLException  
     */
    public synchronized Connection getConnection() throws SQLException {
        long now = System.currentTimeMillis();
        Connection connection;
        // TODO set maximum pool size here
        if (unlocked.size() > 0) {
            Enumeration<Connection> enumeration = unlocked.keys();
            while (enumeration.hasMoreElements()) {
                connection = enumeration.nextElement();
                if ((now - unlocked.get(connection)) > expirationTime) {
                    // remove and close connection
                    unlocked.remove(connection);
                    expire(connection);
                    connection = null;
                } else {
                    if (isValid(connection)) {
                        unlocked.remove(connection);
                        locked.put(connection, now);
                        return (connection);
                    } else {
                        // remove and close connection
                        unlocked.remove(connection);
                        expire(connection);
                        connection = null;
                    }
                }
            }
        }
        // no connections available, create a new one
        connection = create();
        locked.put(connection, now);
        return (connection);
    }

    /**
     * Releases a connection back into the connection pool.
     * 
     * @param connection the connection to return to the pool
     * @throws java.sql.SQLException
     */
    public synchronized void releaseConnection(Connection connection) throws SQLException {
        locked.remove(connection);
        unlocked.put(connection, System.currentTimeMillis());
    }

    /**
     * Opens a connection.
     * 
     * @return connection
     */
    protected Connection create() {
        try {
            return (connectionFactory.getConnection());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return (null);
        }
    }

    /**
     * Closes a connection
     * 
     * @param connection the connection to be closed
     */
    protected void expire(Connection connection) {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * Validates a connection. 
     * 
     * @param connection the connection to be validated.
     * @return boolean isValid from the Connection::isValid() javadoc isValid() 
     * Returns true if the connection has not been closed and is still valid. 
     * The driver shall submit a query on the connection or use some other mechanism that positively 
     * verifies the connection is still valid when this method is called.
     * The query submitted by the driver to validate the connection shall be executed 
     * in the context of the current transaction.validates connection of it is opened or closed
     * 
     * @see Connection#isValid 
     * 
     */
    protected boolean isValid(Connection connection) {
        try {
            return (connection.isValid(MAX_IS_VALID_CONNECTION_TIMEOUT));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return (false);
        }
    }
}
