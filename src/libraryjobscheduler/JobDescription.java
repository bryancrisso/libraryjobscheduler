/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryjobscheduler;

/**
 *
 * @author b-abi-karam
 */
public class JobDescription 
{
    private final int jobDescID;
    private final String jobName;
    private final String timeRestriction;
    private final int repetition;
    private final int timePerItem;

    public JobDescription(int jobDescID, String jobname, String timeRestriction, int repetition, int timePerItem) {
        this.jobDescID = jobDescID;
        this.jobName = jobname;
        this.timeRestriction = timeRestriction;
        this.repetition = repetition;
        this.timePerItem = timePerItem;
    }

    public int getID() {
        return jobDescID;
    }

    public String getName() {
        return jobName;
    }

    public String getTimeRestriction() {
        return timeRestriction;
    }

    public int getRepetition() {
        return repetition;
    }

    public int getTimePerItem() {
        return timePerItem;
    }
    
    @Override
    public String toString()
    {
        return "ID = " + jobDescID +
               "\nName = " + jobName +
               "\nTime Restriction = " + timeRestriction + 
               "\n" + (repetition == -1 ? "Does not repeat":"Repeats every " + repetition + (repetition == 1 ? " day":" days"))+
               "\nTime Per Item = " + timePerItem;
    }
}
