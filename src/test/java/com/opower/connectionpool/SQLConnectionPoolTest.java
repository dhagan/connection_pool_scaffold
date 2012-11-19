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
     * Test of getConnection method, of class SQLConnectionPool.
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
     * Test of releaseConnection method, of class SQLConnectionPool.
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
     * Test of isValid method, of class SQLConnectionPool.
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
