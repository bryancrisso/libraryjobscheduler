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

public class TimetableManager 
{
    //use a singleton?
    private static TimetableManager tmObj;
    private ApplicationSettings as;
    
    private final Connection conn;
    
    public TimetableManager()
    {
        conn = DatabaseManager.getInstance().getConn();
        as = ApplicationSettings.getInstance();
    }
    
    public static TimetableManager getInstance()
    {
        if (tmObj == null)
        {
            tmObj = new TimetableManager();
        }
        
        return tmObj;
    }
    
    public Timetable getTimetable(int empID, String date)
    {
        Statement stmt;
        ResultSet rs;
        
        Timetable timetable = new Timetable(as.getSlotCount());
        
        String jobName;
        int currentSlot = 1;
        int currentJobID;
        int completionTime;
        int numItems;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT JobDescription.JobName, JobObject.PredictedCompletionTime, JobObject.StartSlot, JobObject.JobDescID, JobObject.ItemCount "+
                         "FROM JobDescription, JobObject "+
                         "WHERE JobObject.StartSlot != -1 AND JobDescription.JobDescID = JobObject.JobDescID AND JobObject.SetDate = '" + date + "' AND JobObject.EmpID = " + empID
                    + " ORDER BY JobObject.StartSlot ASC;";
            rs = stmt.executeQuery(sql);
            
            while (rs.next())
            {
                currentJobID = rs.getInt("JobDescID");
                numItems = rs.getInt("ItemCount");
                // TODO use historical data instead (DONE)
                completionTime = rs.getInt("PredictedCompletionTime");
                jobName = rs.getString("JobName");
                
                int slotsTaken = (int) Math.ceil(completionTime/60d);
                
                for (int i = 0; i < slotsTaken; i++)
                {
                    timetable.setSlot(currentSlot-1+i, currentJobID, jobName, numItems);
                }
                currentSlot += slotsTaken;
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        
        return timetable;
    }
}
