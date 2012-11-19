package com.opower.connectionpool;

import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *
 * @author dhagan
 */
public class SQLConnectionFactoryTest {
    
    /**
     * 
     */
    public SQLConnectionFactoryTest() {
    }

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
     */
    @Before
    public void setUp() {
    }
    
    /**
     * 
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of getConnection method, of class SQLConnectionFactory.
     * @throws Exception 
     */
    @Ignore
    public void testGetConnection() throws Exception {
        System.out.println("SQLConnectionFactoryTest::getConnection");
        SQLConnectionFactory instance = null;
        Connection expResult = null;
        Connection result = instance.getConnection();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
