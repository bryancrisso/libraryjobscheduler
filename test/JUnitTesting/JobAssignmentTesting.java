/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JUnitTesting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import libraryjobscheduler.DatabaseManager;
import libraryjobscheduler.JobManager;
import linkedlist.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author b-abi-karam
 */
public class JobAssignmentTesting {
    
    static JobManager jm;
    static Connection conn;
    
    public JobAssignmentTesting() {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
        jm = JobManager.getInstance();
        conn = DatabaseManager.getInstance().getConn();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testG()
    {
        LinkedList<Integer> assignedJobs = jm.assignRepeatingJobs("2022-12-20");
        
        LinkedList<Integer> retrievedJobs = new LinkedList<>();
        
        ResultSet rs;
        Statement stmt;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT JobDescID FROM JobObject WHERE SetDate = '2022-12-20' AND StartSlot = -1";
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                retrievedJobs.append(rs.getInt("JobDescID"));
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(JobAssignmentTesting.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0; i < retrievedJobs.length(); i++)
        {
            assertTrue(assignedJobs.search(retrievedJobs.get(i)));
        }
        
        System.out.println(assignedJobs);
        System.out.println(retrievedJobs);
    }
    
    @Test
    public void testK()
    {
        try
        {
            Statement stmt;
            ResultSet rs;
            stmt = conn.createStatement();
            String sql = "SELECT TimeToComplete FROM GetPredictedCompletionTime "
                    + "WHERE JobDescID = 13 AND EmpID = 1;";
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                assertEquals(150, rs.getInt("TimeToComplete"));
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(JobAssignmentTesting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testL()
    {
        try 
        {
            Statement stmt;
            ResultSet rs;
            stmt = conn.createStatement();
            String sql = "SELECT TimeToComplete FROM GetPredictedCompletionTime "
                    + "WHERE JobDescID = 15 AND EmpID = 1;";
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                assertEquals(26, rs.getInt("TimeToComplete"));
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(JobAssignmentTesting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
