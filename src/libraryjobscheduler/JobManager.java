/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryjobscheduler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import linkedlist.LinkedList;
import hungarianalgorithm.HungarianAlgorithm;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.Matrix;

/**
 *
 * @author b-abi-karam
 */
public class JobManager 
{
    private static JobManager jmObj;
    
    private final Connection conn;
    private final EmployeeManager em;
    private final ApplicationSettings as;
    
    public JobManager()
    {
        conn = DatabaseManager.getInstance().getConn();
        em = EmployeeManager.getInstance();
        as = ApplicationSettings.getInstance();
    }
    
    public static JobManager getInstance()
    {
        if (jmObj == null)
        {
            jmObj = new JobManager();
        }
        return jmObj;
    }
    
    public boolean addToAssignment(int jobID, String date, int itemCount)
    {
        boolean bUpdate = false;
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            
            //have to check if job has already been assigned
            String sql = "INSERT INTO JobObject (JobDescID, SetDate, StartSlot, ItemCount, EmpID, CompletionTime, PredictedCompletionTime)"
                    + " VALUES (" + jobID + ", '" + date + "', -1, " + itemCount + ", -1, -1, -1);";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return bUpdate;
    }
    
    public LinkedList<Integer> assignRepeatingJobs(String date)
    {
        LinkedList<Integer> jobs = new LinkedList<>();
        Statement stmt;
        ResultSet rs;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT JobDescription.JobDescID as ID, ROUND(julianday('"+date+"')-julianday(JobObject.SetDate), 0) as DaysSince\n" +
                        "FROM JobDescription, JobObject\n" +
                        "WHERE JobDescription.Repetition != -1 AND JobObject.JobDescID == JobDescription.JobDescID AND "
                        + "((JobObject.StartSlot == -1  AND JobObject.SetDate == '"+date+"') OR (JobObject.StartSlot != -1))\n" +
                        "GROUP BY JobDescription.JobDescID\n" +
                        "HAVING ROUND(julianday('"+date+"')-julianday(MAX(JobObject.SetDate)), 0) >= JobDescription.Repetition\n" +
                        "ORDER BY DaysSince DESC;";
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt("ID");
                jobs.append(id);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        for (int i = 0; i < jobs.length(); i++)
        {
            addToAssignment(jobs.get(i), date, 1);
        }
        return jobs;
    }
    
    public void assignJobs(String date)
    {
        boolean alreadyAssigned = false;
        try
        {
            Statement chkStmt;
            ResultSet chkrs;
            //check if we've actually already done job assignment on the date
            chkStmt = conn.createStatement();
            String chksql = "SELECT count(*) AS count FROM JobObject WHERE StartSlot != -1 AND SetDate = '" + date+"';";
            chkrs = chkStmt.executeQuery(chksql);
            alreadyAssigned = chkrs.getInt("count") > 0;
            if (alreadyAssigned)
            {
                System.out.println("System has already assigned jobs for that date.");
            }
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        
        if (!alreadyAssigned)
        {
            Employee[] emList = em.selectEmployees(em.employeeCount(), 0);
            LinkedList<Employee> workingEmployees = new LinkedList<>();
            //getting list of employees
            for (Employee e : emList)
            {
                if (em.isEmployeeWorking(date, e.getID()))
                {
                    workingEmployees.append(e);
                }
            }
            //looping through each slot
            for (int i = 0; i < as.getSlotCount(); i++)
            {
                //getting list of free employees (no job in current slot)
                LinkedList<Employee> freeEmployees = new LinkedList<>();
                for (int j = 0; j < workingEmployees.length(); j++)
                {
                    if (em.EmployeeAvailable(i, date, workingEmployees.get(j).getID()))
                    {
                        freeEmployees.append(workingEmployees.get(j));
                    }
                }
                //array of jobs
                Job[] jobs = new Job[freeEmployees.length()];
                Arrays.fill(jobs, new Job(-1, "", -1));
                int jobCount = 0;
                Statement stmt;
                ResultSet rs;
                try
                {
                    stmt = conn.createStatement();
                    //get list of jobs (checks if there is enough time left in the day, and limits it to the number of free employees
                    String sql ="SELECT JobDescription.JobDescID AS JobDescID, JobDescription.TimePerItem*JobObject.ItemCount as TimeToComplete, JobObject.ItemCount\n" +
                                "FROM JobDescription, JobObject\n" +
                                "WHERE JobObject.StartSlot == -1 AND JobDescription.JobDescID == JobObject.JobDescID AND JobObject.SetDate == '"+date+"'"
                                + "AND TimeToComplete <= " + (as.getSlotCount()-i)*60 +
                                "\nORDER BY JobDescription.TimeRestriction DESC, ROUND(julianday("+date+")-julianday(JobObject.SetDate), 0) DESC, TimeToComplete DESC"
                                + " LIMIT " + freeEmployees.length();
                    rs = stmt.executeQuery(sql);
                    while(rs.next())
                    {
                        int jobID =rs.getInt("JobDescID");
                        int itemCount = rs.getInt("ItemCount");
                        jobs[jobCount] = new Job(jobID, "", itemCount);
                        jobCount++;
                    }
                }
                catch (SQLException ex)
                {
                    System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                }
                //only assign if there are actually jobs available
                if (jobCount > 0)
                {
                    //(handle when less jobs than employees)
                    Matrix matrix = new Matrix(freeEmployees.length(), freeEmployees.length());
                    for (int j = 0; j < freeEmployees.length(); j++)
                    {
                        for (int k = 0; k < freeEmployees.length(); k++)
                        {
                            try
                            {
                                if (jobs[k].getID() == -1)
                                {
                                    //dummy job, set it to infinity
                                    matrix.set(j, k, Integer.MAX_VALUE);
                                }
                                else
                                {
                                    //get time to complete for each employee
                                    //(implement unique/historical completion time for each employee)
                                    //PUT INTO SQL STATEMENT PREDICTED COMPLETION TIME
                                    stmt = conn.createStatement();
                                    String sql = "SELECT TimeToComplete FROM GetPredictedCompletionTime WHERE JobDescID = " + jobs[k].getID()+
                                            " AND EmpID = " + freeEmployees.get(j).getID() + ";";
                                    rs = stmt.executeQuery(sql);
                                    if (rs.next())
                                    {
                                        matrix.set(j, k, rs.getInt("TimeToComplete")*jobs[k].getItemCount());
                                    }
                                }
                            }
                            catch (SQLException ex)
                            {
                                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                            }
                        }
                    }

                    //do hungarian assignment
                    HungarianAlgorithm hAlg = new HungarianAlgorithm();
                    int[] assignmentArray = hAlg.Assignment(matrix);
                    
                    for (int j = 0; j < assignmentArray.length; j++)
                    {
                        //execute update, assign a job to each employee (if it isn't a dummy assignment)
                        if (assignmentArray[j] != -1)
                        {
                            try
                            {
                                stmt = conn.createStatement();
                                //TODO record predicted completion time
                                String sql = "UPDATE JobObject SET StartSlot = " + (i+1) + ", EmpID = "
                                        + freeEmployees.get(j).getID() + ", PredictedCompletionTime = " + matrix.get(j, assignmentArray[j])
                                        + " WHERE JobDescID = " + jobs[assignmentArray[j]].getID() + ""
                                        + " AND StartSlot == -1" + " AND SetDate = '" + date + "';";
                                stmt.executeUpdate(sql);
                                stmt.close();
                                conn.commit();
                            }
                            catch(SQLException ex)
                            {
                                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
    
    public JobDescription getJobDescription(int ID)
    {
        Statement stmt;
        ResultSet rs;
        
        int id = -1;
        String jobName = "";
        String timeRestriction = "";
        int repetition = -1;
        int timePerItem = -1;
        
        try 
        {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM JobDescription WHERE JobDescID = " + ID + ";";
            rs = stmt.executeQuery(sql);
            
            if (rs.next())
            {
                id = rs.getInt("JobDescID");
                jobName = rs.getString("JobName");
                timeRestriction = rs.getString("TimeRestriction");
                repetition = rs.getInt("Repetition");
                timePerItem = rs.getInt("TimePerItem");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return new JobDescription(id, jobName, timeRestriction, repetition, timePerItem);
    }
    
    public JobDescription[] selectJobDescriptions(int count, int offset)
    {
        Statement stmt;
        ResultSet rs;
        
        int id = -1;
        String jobName = "";
        String timeRestriction = "";
        int repetition = -1;
        int timePerItem = -1;
        
        JobDescription[] jobArray = new JobDescription[count];
        int i = 0;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM JobDescription LIMIT " + count + " OFFSET " + offset + ";";
            rs = stmt.executeQuery(sql);
            
            while (rs.next())
            {
                id = rs.getInt("JobDescID");
                jobName = rs.getString("JobName");
                timeRestriction = rs.getString("TimeRestriction");
                repetition = rs.getInt("Repetition");
                timePerItem = rs.getInt("TimePerItem");
                jobArray[i] = new JobDescription(id, jobName, timeRestriction, repetition, timePerItem);
                i++;
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return (i == 0 ? new JobDescription[]{}:jobArray);
    }
    
    public boolean modifyJobDescription(JobDescription jobDesc)
    {
        boolean bUpdate = false;
        Statement stmt;
        ResultSet rs;
        boolean createBaseJob;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "UPDATE JobDescription SET"
            + " JobName = '" + jobDesc.getName()
            + "', TimeRestriction = '" + jobDesc.getTimeRestriction()
            + "', Repetition = " + jobDesc.getRepetition()
            + ", TimePerItem = " + jobDesc.getTimePerItem()
            + " WHERE JobDescID = " + jobDesc.getID() + ";";
            stmt.executeUpdate(sql);
            if (jobDesc.getRepetition()>-1)
            {
                sql = "SELECT count(*) as count FROM JobObject WHERE SetDate = '1970-01-01' AND JobDescID = " + jobDesc.getID() + ";";
                rs = stmt.executeQuery(sql);
                
                createBaseJob = rs.getInt("count") == 0;
                
                if (createBaseJob)
                {
                    sql = "INSERT INTO JobObject (JobDescID, SetDate, StartSlot, ItemCount, EmpID, CompletionTime, PredictedCompletionTime) VALUES ("
                        + jobDesc.getID() + ", '1970-01-01', 0, 1, -1, -1, -1);";
                    stmt.executeUpdate(sql);
                }
            }
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
    
    public boolean insertJobDescription(JobDescription jobDesc)
    {
        boolean bInsert = false;
        Statement stmt;
        ResultSet rs;
        
        
        try
        {
            stmt = conn.createStatement();
            //get max ID num
            rs = stmt.executeQuery("SELECT max(JobDescID) as maxID FROM JobDescription");
            int id = (rs.next() ? rs.getInt("maxID") + 1 : 1);
            
            String sql = "INSERT INTO JobDescription (JobDescID, JobName, TimeRestriction, Repetition, TimePerItem) VALUES ("
                    + id + ", '" + jobDesc.getName() + "', '" + jobDesc.getTimeRestriction() + "', " + jobDesc.getRepetition() + ", "
                    + jobDesc.getTimePerItem() + ");";
            stmt.executeUpdate(sql);
            
            //create a dummy jobcompletion if repetition > -1 (allows the assigner to assign the first iteration of the job)
            if (jobDesc.getRepetition() > -1)
            {
                sql = "INSERT INTO JobObject (JobDescID, SetDate, StartSlot, ItemCount, EmpID, CompletionTime, PredictedCompletionTime) VALUES ("
                    + id + ", '1970-01-01', 0, 1, -1, -1, -1);";
                stmt.executeUpdate(sql);
            }
            
            stmt.close();
            conn.commit();
            bInsert = true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(JobManager.class.getName()).log(Level.WARNING, null, ex);
        }
        return bInsert;
    }
    
    public JobCompletion getJobCompletion(int ID, String date)
    {
        Statement stmt;
        ResultSet rs;
        
        int id = -1;
        String jobName = "";
        int completionTime = -1;
        int empID = -1;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT O.JobDescID, O.CompletionTime, D.JobName, O.EmpID "
                    + "FROM JobObject O, JobDescription D "
                    + "WHERE O.StartSlot != -1 AND O.JobDescID = "+ID+" AND "
                    + " O.SetDate != '1970-01-01' AND O.SetDate = '"+date+"' AND O.JobDescID = D.JobDescID";
            rs = stmt.executeQuery(sql);
            
            if (rs.next())
            {
                empID = rs.getInt("EmpID");
                id = rs.getInt("JobDescID");
                jobName = rs.getString("JobName");
                completionTime = rs.getInt("CompletionTime");
            }
            
            rs.close();
            stmt.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(JobManager.class.getName()).log(Level.WARNING, null, ex);
        }
        
        return new JobCompletion(id, empID, jobName, date, completionTime);
    }
    
    public JobCompletion[] getJobCompletions(String date, int count, int offset)
    {
        Statement stmt;
        ResultSet rs;
        
        int id = -1;
        int empID = -1;
        String jobName = "";
        int completionTime = -1;
        
        JobCompletion[] jobArray = new JobCompletion[count];
        int i = 0;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "SELECT O.EmpID, O.JobDescID, O.CompletionTime, D.JobName, O.SetDate "
                    + "FROM JobObject O, JobDescription D "
                    + "WHERE O.JobDescID = D.JobDescID AND O.SetDate = '"+date+"' AND "
                    + " O.SetDate != '1970-01-01' AND O.StartSlot != -1 ORDER BY O.StartSlot ASC "
                    + "LIMIT " + count + " OFFSET " + offset + ";";
            rs = stmt.executeQuery(sql);
            
            while (rs.next())
            {
                id = rs.getInt("JobDescID");
                jobName = rs.getString("JobName");
                completionTime = rs.getInt("CompletionTime");
                date = rs.getString("SetDate");
                empID = rs.getInt("EmpID");
                jobArray[i] = new JobCompletion(id, empID, jobName, date, completionTime);
                i++;
            }
            
            rs.close();
            stmt.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(JobManager.class.getName()).log(Level.WARNING, null, ex);
        }
        
        return (i==0 ? new JobCompletion[]{} : jobArray);
    }
    
    public boolean updateJobCompletion(int jobID, String date, int completionTime)
    {
        boolean bUpdate = false;
        Statement stmt;
        
        try
        {
            stmt = conn.createStatement();
            
            String sql = "UPDATE JobObject SET CompletionTime = (" + completionTime +
                    "/JobObject.ItemCount) WHERE JobDescID = " + jobID +
                    " AND SetDate = '" + date + "';";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(JobManager.class.getName()).log(Level.WARNING, null, ex);
        }
        
        return bUpdate;
    }
    
    public boolean removeJobCompletion(int jobID, String date)
    {
        boolean bUpdate = false;
        Statement stmt;
        
        try
        {
            stmt = conn.createStatement();
            String sql = "UPDATE JobObject SET CompletionTime = " + -1 +
                    " WHERE JobDescID = " + jobID +
                    " AND SetDate = '" + date + "';";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(JobManager.class.getName()).log(Level.WARNING, null, ex);
        }
        
        return bUpdate;
    }
}
