/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kizzy
 */
public class MySqlTollEventDaoTest
{
    
    public MySqlTollEventDaoTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAllTollEvent method, of class MySqlTollEventDao.
     */
    @Test
    public void testGetAllTollEvent()
    {
        System.out.println("=== getAllTollEventTest ===");       
        MySqlTollEventDao instance = new MySqlTollEventDao();
        Set result = instance.getAllTollEvent();      
        assertNotNull( result );
    }

    /**
     * Test of getTollEventByVehicleRegistration method, of class MySqlTollEventDao.
     */
    @Test
    public void testGetTollEventByVehicleRegistration()
    {
        System.out.println("=== getTollEventByVehicleRegistration ===");
        MySqlTollEventDao instance = new MySqlTollEventDao();
        Set result = instance.getTollEventByVehicleRegistration("123");
        int expResult = 0;
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getTollEventSinceDate method, of class MySqlTollEventDao.
     */
    @Test
    public void testGetTollEventSinceDate()
    {
        System.out.println("=== getTollEventSinceDate ===");   
        Timestamp timeStamp = Timestamp.valueOf("2020-02-16 10:15:31.88");
        MySqlTollEventDao instance = new MySqlTollEventDao();     
        Set result = instance.getTollEventSinceDate(timeStamp);
        boolean expResult = result.size() > 0;
        assertTrue(expResult);
    }

    /**
     * Test of getTollEventBetween method, of class MySqlTollEventDao.
     */
    @Test
    public void testGetTollEventBetween()
    {
        System.out.println("=== getTollEventBetween ===");
        Timestamp start = Timestamp.valueOf("2024-02-16 10:15:31.88");;
        Timestamp finish = Timestamp.valueOf("2024-02-16 10:15:31.88");;
        MySqlTollEventDao instance = new MySqlTollEventDao();
        
        Set result = instance.getTollEventBetween(start, finish);       
        assertEquals( 0, result.size() );
    }

    /**
     * Test of getAllUniqueRegistrationNumberThatPassedThroughToll method, of class MySqlTollEventDao.
     */
    @Test
    public void testGetAllUniqueRegistrationNumberThatPassedThroughToll()
    {
        System.out.println("=== getAllUniqueRegistrationNumberThatPassedThroughToll ===");
        MySqlTollEventDao instance = new MySqlTollEventDao();       
        ArrayList<String> result = instance.getAllUniqueRegistrationNumberThatPassedThroughToll();    
        assertNotEquals(result.get(0), result.get(1));
    }

    /**
     * Test of getAllTollEventMap method, of class MySqlTollEventDao.
     */
    @Test
    public void testGetAllTollEventMap()
    {
        System.out.println("=== getAllTollEventMap ===");
        MySqlTollEventDao instance = new MySqlTollEventDao();
        
        Map result = instance.getAllTollEventMap(); 
        boolean expResult = result.containsKey("123");
        
        assertFalse(expResult);
    }
    
}
