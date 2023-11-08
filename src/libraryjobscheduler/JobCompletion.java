/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryjobscheduler;

/**
 *
 * @author bryan
 */
public class JobCompletion 
{
    private final int jobDescID;
    private final int empID;
    private final String jobName;
    private final String setDate;
    private final int completionTime;

    public JobCompletion(int jobDescID, int empID, String jobName, String setDate, int completionTime) {
        this.jobDescID = jobDescID;
        this.empID = empID;
        this.jobName = jobName;
        this.setDate = setDate;
        this.completionTime = completionTime;
    }

    public int getJobID() {
        return jobDescID;
    }

    public int getEmpID() {
        return empID;
    }

    public String getName() {
        return jobName;
    }

    public String getDate() {
        return setDate;
    }

    public int getTime() {
        return completionTime;
    }
    
    public String toString()
    {
        return "Employee " + empID + " Completed job: " + jobDescID + ", " + jobName +
                "\nDate: " + setDate + "\n" + (completionTime != -1 ? "Completed in " + completionTime + " minutes" : "No Recorded Completion Time");
    }
}
