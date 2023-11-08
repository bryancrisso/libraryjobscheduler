/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryjobscheduler;

/**
 *
 * @author bryan
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeManager 
{
    //use a singleton?
    private static EmployeeManager emObj;
    
    private final Connection conn;
    private final TimetableManager tm;
    
    public EmployeeManager()
    {
        conn = DatabaseManager.getInstance().getConn();
        tm = TimetableManager.getInstance();
    }
    
    public static EmployeeManager getInstance()
    {
        if (emObj == null)
        {
            emObj = new EmployeeManager();
        }
        return emObj;
    }
    
    public boolean insertEmployee(Employee emp)
    {
        boolean bInsert = false;
        Statement stmt;
        ResultSet rs;
        
        try
        {
            stmt = conn.createStatement();
            //get maximum ID num
            rs = stmt.executeQuery("SELECT max(EmpID) as maxID FROM Employee");
            int id = (rs.next() ? rs.getInt("maxID") + 1 : 1);
            
            String sql = "INSERT INTO Employee (EmpID, FirstName, LastName, Email, IsEmployed)"
                    + " VALUES (" + id + ", '" + emp.getFirstName() + "', '" + emp.getLastName() + "', '" + 
                    emp.getEmail() + "', " + emp.isEmployed() + ");";
            
            stmt.executeUpdate(sql);
            
            for (int i = 1; i <= 7; i++)
            {
                sql = "INSERT INTO EmployeeAvailability (EmpID, DayOfWeek, IsWorking) VALUES (" + id + ", " + i + ", 1);";
                stmt.executeUpdate(sql);
            }
            
            stmt.close();
            conn.commit();
            bInsert = true;
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return bInsert;
    }
    
    public boolean modifyEmployee(Employee emp)
    {
        boolean bUpdate = false;
        Statement stmt;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "UPDATE Employee SET"
            + " FirstName = '" + emp.getFirstName()
            + "', LastName = '" + emp.getLastName()
            + "', Email = '" + emp.getEmail()
            + "', IsEmployed = " + (emp.isEmployed() ? 1:0)
            + " WHERE EmpID = " + emp.getID() + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        }
        catch(SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return bUpdate;
    }
    
    public boolean setEmployeeActive(int id, boolean active)
    {
        boolean bUpdate = false;
        Statement stmt;
        
        try
        {
            stmt = conn.createStatement();
            
            String sql = "UPDATE Employee SET IsEmployed = " + (active ? 1 : 0) + " WHERE EmpID = " + id + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        }
        catch(SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return bUpdate;
    }
    
    public int employeeCount()
    {
        Statement stmt;
        ResultSet rs;
        int count=0;
        
        try 
        {
            stmt = conn.createStatement();
            String sql = "SELECT count(*) AS count FROM Employee";
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                count = rs.getInt("count");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return count;
    }
    
    public Employee[] selectEmployees(int count, int offset)
    {
        Statement stmt;
        ResultSet rs;
        
        int id;
        String firstName;
        String lastName;
        String email;
        boolean isEmployed;
        
        Employee[] empArray = new Employee[count];
        int i = 0;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Employee LIMIT " + count + " OFFSET " + offset + ";";
            rs = stmt.executeQuery(sql);
            
            while (rs.next())
            {
                id = rs.getInt("EmpID");
                firstName = rs.getString("FirstName");
                lastName = rs.getString("LastName");
                email = rs.getString("Email");
                isEmployed = rs.getBoolean("isEmployed");
                empArray[i] = new Employee(id, firstName, lastName, email, isEmployed);
                i++;
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return (i == 0 ? new Employee[]{}: empArray);
    }
    
    public Employee getEmployee(int _id)
    {
        Statement stmt;
        ResultSet rs;
        
        int id = -1;
        String firstName = "";
        String lastName = "";
        String email = "";
        boolean isEmployed = false;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Employee WHERE EmpID = " + _id + ";";
            rs = stmt.executeQuery(sql);
            
            if (rs.next())
            {
                id = rs.getInt("EmpID");
                firstName = rs.getString("FirstName");
                lastName = rs.getString("LastName");
                email = rs.getString("Email");
                isEmployed = rs.getBoolean("isEmployed");
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return new Employee(id, firstName, lastName, email, isEmployed);
    }
    
    public boolean setEmployeeWorking(int dayOfWeek, boolean isWorking, int empID)
    {
        boolean bUpdate = false;
        Statement stmt;
        
        try
        {
            stmt = conn.createStatement();
            
            String sql = "UPDATE EmployeeAvailability SET IsWorking = " + (isWorking ? 1 : 0) + " WHERE EmpID = " + empID + " AND DayOfWeek = " + dayOfWeek + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        }
        catch(SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return bUpdate;
    }
    
    public boolean isEmployeeWorking(String date, int empID)
    {
        int dayOfWeek = LocalDate.parse(date, DateTimeFormatter.ISO_DATE).getDayOfWeek().getValue();
        Statement stmt;
        ResultSet rs;
        boolean isWorking = false;
        boolean isEmployed = false;
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT Employee.isEmployed, EmployeeAvailability.IsWorking FROM EmployeeAvailability, Employee\n" +
            "WHERE DayOfWeek == "+dayOfWeek+" AND Employee.EmpID == "+empID+" AND Employee.EmpID == EmployeeAvailability.EmpID;";
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                isEmployed = rs.getBoolean("IsEmployed");
                isWorking = rs.getBoolean("IsWorking");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return isWorking && isEmployed;
    }
    
    public boolean EmployeeAvailable(int slot, String date, int empID)
    {
        Timetable tt = tm.getTimetable(empID, date);
        boolean isAvailable = false;
        if (tt.getSlot(slot).getID() == -1)
        {
            isAvailable = true;
        }
        return isAvailable;
    }
}
