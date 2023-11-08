/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JUnitTesting;

import libraryjobscheduler.EmployeeManager;
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
public class EmployeeTesting {
    
    EmployeeManager em;
    
    public EmployeeTesting() {
    }
    
    @Before
    public void setUp() 
    {
        em = EmployeeManager.getInstance();
    }
    
    @Test
    public void testP()
    {
        em.setEmployeeWorking(1, true, 1);
        em.setEmployeeWorking(2, true, 1);
        em.setEmployeeWorking(3, false, 1);
        em.setEmployeeWorking(4, false, 1);
        em.setEmployeeWorking(5, true, 1);
        em.setEmployeeWorking(6, false, 1);
        em.setEmployeeWorking(7, true, 1);
        
        assertTrue(em.isEmployeeWorking("2022-12-12", 1));
        assertTrue(em.isEmployeeWorking("2022-12-13", 1));
        assertFalse(em.isEmployeeWorking("2022-12-14", 1));
        assertFalse(em.isEmployeeWorking("2022-12-15", 1));
        assertTrue(em.isEmployeeWorking("2022-12-16", 1));
        assertFalse(em.isEmployeeWorking("2022-12-17", 1));
        assertTrue(em.isEmployeeWorking("2022-12-18", 1));
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
