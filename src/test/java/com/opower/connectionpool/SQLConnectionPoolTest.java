package com.opower.connectionpool;

import java.sql.SQLException;
import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author dhagan
 */
public class SQLConnectionPoolTest {

    /**
     * 
     */
    public SQLConnectionPoolTest() {
    }
    private SQLConnectionFactory mockSQLConnectionFactory;
    private Connection mockConnection;

    /**
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * 
     * @throws SQLException
     */
    @Before
    public void setUp() throws SQLException {
        mockSQLConnectionFactory = createMock(SQLConnectionFactory.class);
        mockConnection = createMock(Connection.class);
        expect(mockConnection.isValid(SQLConnectionPool.MAX_IS_VALID_CONNECTION_TIMEOUT)).andReturn(Boolean.TRUE);
        replay(mockConnection);
    }

    /**
     * 
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of getConnection method, of class SQLConnectionPool.  Basic smoke test, create a connection.
     * @throws Exception 
     */
    @Test
    public void testGetConnection() throws Exception {
        System.out.println("getConnection");
        expect(mockSQLConnectionFactory.getConnection()).andReturn(mockConnection);
        replay(mockSQLConnectionFactory);

        SQLConnectionPool sqlConnectionPool = new SQLConnectionPool(mockSQLConnectionFactory);
        assertEquals("Connection not returned", sqlConnectionPool.getConnection(), mockConnection);
        verify(mockSQLConnectionFactory);
        System.out.println("Success getConnection");
    }

    /**
     * Test of releaseConnection method, of class SQLConnectionPool.  Create a connection,
     * and then release the same connection.
     * @throws Exception 
     */
    @Test
    public void testReleaseConnection() throws Exception {
        System.out.println("releaseConnection");
        expect(mockSQLConnectionFactory.getConnection()).andReturn(mockConnection);
        replay(mockSQLConnectionFactory);

        SQLConnectionPool sqlConnectionPool = new SQLConnectionPool(mockSQLConnectionFactory);
        assertEquals("Connection not returned", sqlConnectionPool.getConnection(), mockConnection);
        sqlConnectionPool.releaseConnection(mockConnection);
        verify(mockSQLConnectionFactory);
        System.out.println("Success releaseConnection");
    }

    /**
     * Gets 2 different connections and releases one, then re-acquires the first connection 
     * @throws Exception 
     */
    @Test
    public void testGetConnections() throws Exception {
        System.out.println("getConnectionsAndRelease");
        Connection mockConnection2 = createMock(Connection.class);
        expect(mockConnection2.isValid(SQLConnectionPool.MAX_IS_VALID_CONNECTION_TIMEOUT)).andReturn(Boolean.TRUE);
        replay(mockConnection2);

        expect(mockSQLConnectionFactory.getConnection()).andReturn(mockConnection).andReturn(mockConnection2);
        replay(mockSQLConnectionFactory);

        SQLConnectionPool sqlConnectionPool = new SQLConnectionPool(mockSQLConnectionFactory);
        assertEquals("Connection not returned", sqlConnectionPool.getConnection(), mockConnection);
        assertEquals("Connection not returned", sqlConnectionPool.getConnection(), mockConnection2);
        sqlConnectionPool.releaseConnection(mockConnection);
        assertEquals("Connection not returned", sqlConnectionPool.getConnection(), mockConnection);

        verify(mockSQLConnectionFactory);
        System.out.println("Success getConnectionsAndRelease");
    }

    /**
     * Having a little fun here, get 2 different connections and but 1 of them is invalid, release the first connection
     * and then get a new connection.  
     * <P>
     * Writing this test exposes that the connection factory should not allow you to get an invalid connection.
     * So if the connection factory / DriverManager does not check if a connection is valid, we should do so
     * at the bottom of SQLConnectionPool::getConnection().  And so it goes!
     * @throws Exception 
     */
    @Test
    public void testGetConnectionClosed() throws Exception {
        System.out.println("getConnectionsAndRelease");
        Connection mockConnection2 = createMock(Connection.class);
        mockConnection2.close();
        // return false, invalid connection
        expect(mockConnection2.isValid(SQLConnectionPool.MAX_IS_VALID_CONNECTION_TIMEOUT)).andReturn(Boolean.FALSE);
        replay(mockConnection2);

        // N.B. reversal of connections 2 first
        expect(mockSQLConnectionFactory.getConnection()).andReturn(mockConnection2).andReturn(mockConnection);
        replay(mockSQLConnectionFactory);

        // release connection 2 forces expire, since it's invalid
        SQLConnectionPool sqlConnectionPool = new SQLConnectionPool(mockSQLConnectionFactory);
        assertEquals("Connection not returned", sqlConnectionPool.getConnection(), mockConnection2);
        sqlConnectionPool.releaseConnection(mockConnection2);
        assertEquals("Connection not returned", sqlConnectionPool.getConnection(), mockConnection);

        verify(mockSQLConnectionFactory);
        System.out.println("Success getConnectionsAndRelease");
    }

    /**
     * Test of create method, of class SQLConnectionPool.
     */
    @Ignore
    public void testCreate() {
        System.out.println("create");
        SQLConnectionPool instance = null;
        Connection expResult = null;
        Connection result = instance.create();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of expire method, of class SQLConnectionPool.
     */
    @Ignore
    public void testExpire() {
        System.out.println("expire");
        Connection connection = null;
        SQLConnectionPool instance = null;
        instance.expire(connection);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValid method, of class SQLConnectionPool.
     * @throws SQLException 
     */
    @Test
    public void testIsValidFalse() throws SQLException {
        System.out.println("testIsValidFalse");

        expect(mockSQLConnectionFactory.getConnection()).andReturn(mockConnection).times(2);
        replay(mockSQLConnectionFactory);

        SQLConnectionPool mockSQLConnectionPool = createMock(SQLConnectionPool.class);
        expect(mockSQLConnectionPool.isValid(mockConnection)).andReturn(false);
        replay(mockSQLConnectionPool);

        SQLConnectionPool sqlConnectionPool = new SQLConnectionPool(mockSQLConnectionFactory);
        Connection connection = sqlConnectionPool.getConnection();
        sqlConnectionPool.releaseConnection(connection);
        sqlConnectionPool.getConnection();

        System.out.println("Success testIsValidFalse");

    }

    /**
     * Having a little more fun, couldn't - couldn't get this test to work...
     * @throws SQLException 
     */
    @Ignore //(expected = SQLException.class)
    public void testIsValidSQLException() throws SQLException {
        System.out.println("testIsValidSQLException");

        expect(mockSQLConnectionFactory.getConnection()).andReturn(mockConnection);
        replay(mockSQLConnectionFactory);

        SQLConnectionPool mockSQLConnectionPool = createMock(SQLConnectionPool.class);
        expect(mockSQLConnectionPool.isValid(mockConnection)).andReturn(true).andThrow(new SQLException());
        replay(mockSQLConnectionPool);

        SQLConnectionPool sqlConnectionPool = new SQLConnectionPool(mockSQLConnectionFactory);
        sqlConnectionPool.getConnection();
        sqlConnectionPool.getConnection();
        System.out.println("Success testIsValidSQLException");

    }
}
