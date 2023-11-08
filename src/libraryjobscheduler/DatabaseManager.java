/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryjobscheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bryan
 */
public class DatabaseManager 
{
    //use a singleton????
    private static DatabaseManager dmObj;
    
    private Connection conn;
    private String dbPath = "libraryjobs.db";
    
    public DatabaseManager()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");//Specify the SQLite Java driver
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbPath);//Specify the database, since relative in the main project folder
            conn.setAutoCommit(false);// Important as you want control of when data is written
            System.out.println("Opened database successfully");
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public static DatabaseManager getInstance()
    {
        if (dmObj == null)
        {
            dmObj = new DatabaseManager();
        }
        
        return dmObj;
    }
    
    public void close()
    {
        try
        {
            conn.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConn() 
    {
        return conn;
    }
}
