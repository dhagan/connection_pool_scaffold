package com.opower.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A factory for creating connections for accessing and processing data stored 
 * in a data source (usually a relational database)
 * 
 * @author dhagan
 */
public interface ConnectionFactory {

    /**
     * <P>
     * Gets a connection from the connection factory.
     * 
     * @return a valid connection.
     * @throws SQLException 
     */
    Connection getConnection() throws SQLException;
}
