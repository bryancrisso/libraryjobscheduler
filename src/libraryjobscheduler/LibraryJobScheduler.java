/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package libraryjobscheduler;

import dynamichashtable.HashTable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import pdfwriter.PDFWriter;

/**
 *
 * @author bryan
 */
public class LibraryJobScheduler 
{
    private final Scanner sc;
    
    private final DatabaseManager dbm;
    private final EmployeeManager em;
    private final TimetableManager tm;
    private final JobManager jm;
    private final ApplicationSettings as;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        LibraryJobScheduler menu = new LibraryJobScheduler();
        menu.mainMenu();
    }
    
    public LibraryJobScheduler()
    {
        sc = new Scanner(System.in);
        dbm = DatabaseManager.getInstance();
        em = EmployeeManager.getInstance();
        tm = TimetableManager.getInstance();
        jm = JobManager.getInstance();
        as = ApplicationSettings.getInstance();
    }
    
    public void mainMenu()
    {
        boolean isRunning = true;
        while (isRunning)
        {
            System.out.println("Welcome to the Library Job Scheduler. Please select a valid option");
            System.out.println("0- Exit");
            System.out.println("1- Employee Management");
            System.out.println("2- Job Management");
            System.out.println("3- Timetables and Job Assignment");
            System.out.println("4- General Settings");
            int[] choices = {0,1,2,3,4};
            int choice = getChoice(choices);

            switch(choice)
            {
                case 0:
                    System.out.println("Exiting Program...");
                    dbm.close();
                    isRunning = false;
                    break;
                case 1:
                    employeeManagementMenu();
                    break;
                case 2:
                    jobManagementMenu();
                    break;
                case 3:
                    timetableMenu();
                    break;
                case 4:
                    generalSettingsMenu();
                    break;
                default:
                    break;
            }
        }
    }
    
    public void timetableMenu()
    {
        boolean isRunning = true;
        while (isRunning)
        {
            System.out.println("Timetable and Job Assignment Menu");
            System.out.println("0- Go Back");
            System.out.println("1- Assign Jobs");
            System.out.println("2- Generate Timetable PDF");
            System.out.println("3- Email Timetable PDF");
            int[] choices = {0,1,2,3};
            int choice = getChoice(choices);
            switch (choice)
            {
                case 0:
                    isRunning = false;
                    break;
                case 1:
                    assignJobs();
                    break;
                case 2:
                    generateTimetablePDF();
                    break;
                case 3:
                    emailTimetablePDF();
                    break;
                default:
                    break;
            }
        }
    }
    
    public void emailTimetablePDF()
    {
        //get the date to send email for
        sc.nextLine();
        String date = "";
        boolean dateChosen = false;
        while (!dateChosen)
        {
            try
            {
                System.out.println("Date of timetable to send: (YYYY-MM-DD)");
                date = sc.nextLine();
                date = LocalDate.parse(date).toString();
                System.out.println("Date accepted");
                dateChosen = true;
                
            }
            catch(DateTimeParseException ex)
            {
                System.out.println("Invalid date format!");
            }
        }
        String emailPWD = "";
        while ("".equals(emailPWD))
        {
            System.out.println("Password for system email address");
            emailPWD = sc.nextLine();
        }
        EmailHandler.sendTimetables(date, emailPWD);
    }
    
    public void generateTimetablePDF()
    {
        //get the date to generate a timetable for
        sc.nextLine();
        String date = "";
        boolean dateChosen = false;
        while (!dateChosen)
        {
            try
            {
                System.out.println("Date to generate timetable for: (YYYY-MM-DD)");
                date = sc.nextLine();
                date = LocalDate.parse(date).toString();
                System.out.println("Date accepted");
                dateChosen = true;
                
            }
            catch(DateTimeParseException ex)
            {
                System.out.println("Invalid date format!");
            }
        }
        //create a hastable of employees and their timetables
        HashTable<Employee, Timetable> tables = new HashTable<>();
        for (int i = 1; i < em.employeeCount()+1; i++)
        {
            //only fill in employees that are working on the day
            if (em.isEmployeeWorking(date, i))
            {
                Timetable tt = tm.getTimetable(i, date);
                tables.add(em.getEmployee(i), tt);
            }
        }
        PDFWriter.createPDFTimetable("/timetables/"+date+".pdf", tables);
    }
    
    public void assignJobs()
    {
        LocalDate now = LocalDate.now();
        LocalDate input;
        String date = "";
        boolean dateChosen = false;
        sc.nextLine();
        while (!dateChosen)
        {
            try
            {
                System.out.println("Date to assign jobs to employees: (YYYY-MM-DD)");
                date = sc.nextLine();
                input = LocalDate.parse(date);
                if (input.isAfter(now) || input.isEqual(now))
                {
                    System.out.println("Date accepted");
                    dateChosen = true;
                }
                else
                {
                    System.out.println("Date of assignment cannot be in the past!");
                }
            }
            catch(DateTimeParseException ex)
            {
                System.out.println("Invalid date format!");
            }
        }
        jm.assignRepeatingJobs(date);
        jm.assignJobs(date);
        System.out.println("Job Assignment Successfully completed. Generate PDF to view.");
    }
    
    public void jobManagementMenu()
    {
        boolean isRunning = true;
        while (isRunning)
        {
            System.out.println("Job Management Menu");
            System.out.println("0- Go Back");
            System.out.println("1- View Job Descriptions");
            System.out.println("2- Create a Job Description");
            System.out.println("3- Modify a Job Description");
            System.out.println("4- Mark a job for assignment");
            System.out.println("5- View Job Completions");
            System.out.println("6- Record Job Completion");
            System.out.println("7- Remove Job Completion");
            int[] choices = {0,1,2,3,4,5,6,7};
            int choice = getChoice(choices);
            switch (choice)
            {
                case 0:
                    isRunning = false;
                    break;
                case 1:
                    viewJobDescriptions();
                    break;
                case 2:
                    createJobDescription();
                    break;
                case 3:
                    modifyJobDescription();
                    break;
                case 4:
                    markJobForAssignment();
                    break;
                case 5:
                    viewJobCompletions();
                    break;
                case 6:
                    recordJobCompletion();
                    break;
                case 7:
                    removeJobCompletion();
                    break;
                default:
                    break;
            }
        }
    }
    
    public void removeJobCompletion()
    {
        int jobID = -1;
        String date = "";
        boolean foundCompletion = false;
        //only continue to removing if the job completion was found
        while (!foundCompletion)
        {
            //get the ID of the job in the completion
            jobID = -1;
            date = "";
            while (jm.getJobDescription(jobID).getID() == -1)
            {
                System.out.println("ID of Job Completion to record:");
                try
                {
                    jobID = sc.nextInt();
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input format!");
                    sc.next();
                    jobID = -1;
                }
            }
            System.out.println("Job Description ID found in database");
            //get the date of the job completion
            sc.nextLine();
            LocalDate now = LocalDate.now();
            LocalDate input;
            boolean dateChosen = false;
            while (!dateChosen)
            {
                try
                {
                    System.out.println("Date of job completion: (YYYY-MM-DD)");
                    date = sc.nextLine();
                    input = LocalDate.parse(date);
                    if (input.isBefore(now) || input.isEqual(now))
                    {
                        System.out.println("Date accepted");
                        dateChosen = true;
                    }
                    else
                    {
                        System.out.println("Date of completion cannot be in the future!");
                    }
                }
                catch(DateTimeParseException ex)
                {
                    System.out.println("Invalid date format!");
                }
            }
            //check if the job completion exists
            if (jm.getJobCompletion(jobID, date).getJobID() == -1)
            {
                System.out.println("No Job Completion with those criteria found");
            }
            else
            {
                System.out.println("Job Completion found.");
                foundCompletion = true;
            }
        }
        
        sc.nextLine();
        boolean choice = getYorN("Confirm remove job completion? y/n");
        if (choice)
        {
            
            jm.updateJobCompletion(jobID, date, -1);
            System.out.println("Successfully removed completion time");
        }
        else
        {
            System.out.println("Cancelling...");
        }
    }
    
    public void recordJobCompletion()
    {
        int jobID = -1;
        String date = "";
        boolean foundCompletion = false;
        //only continue to recording if the job completion was found
        while (!foundCompletion)
        {
            //get the ID of the job in the completion
            jobID = -1;
            date = "";
            while (jm.getJobDescription(jobID).getID() == -1)
            {
                System.out.println("ID of Job Completion to record:");
                try
                {
                    jobID = sc.nextInt();
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input format!");
                    sc.next();
                    jobID = -1;
                }
            }
            System.out.println("Job Description ID found in database");
            //get the date of the job completion
            sc.nextLine();
            LocalDate now = LocalDate.now();
            LocalDate input;
            boolean dateChosen = false;
            while (!dateChosen)
            {
                try
                {
                    System.out.println("Date of job completion: (YYYY-MM-DD)");
                    date = sc.nextLine();
                    input = LocalDate.parse(date);
                    if (input.isBefore(now) || input.isEqual(now))
                    {
                        System.out.println("Date accepted");
                        dateChosen = true;
                    }
                    else
                    {
                        System.out.println("Date of completion cannot be in the future!");
                    }
                }
                catch(DateTimeParseException ex)
                {
                    System.out.println("Invalid date format!");
                }
            }
            //check if the job completion exists
            if (jm.getJobCompletion(jobID, date).getJobID() == -1)
            {
                System.out.println("No Job Completion with those criteria found");
            }
            else
            {
                System.out.println("Job Completion found.");
                foundCompletion = true;
            }
        }
        int timeToComplete = -1;
        //get the time to complete for the job completion
        while (timeToComplete <= 0)
        {
            System.out.println("Time to complete this job:");
            try
            {
                timeToComplete = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                timeToComplete = -1;
            }
        }
        sc.nextLine();
        boolean choice = getYorN("Confirm record job completion? y/n");
        if (choice)
        {
            
            jm.updateJobCompletion(jobID, date, timeToComplete);
            System.out.println("Successfully recorded completion time");
        }
        else
        {
            System.out.println("Cancelling...");
        }
    }
    
    public void viewJobCompletions()
    {
        //get the date for which we are viewing job completions for
        sc.nextLine();
        String date = "";
        boolean dateChosen = false;
        while (!dateChosen)
        {
            try
            {
                System.out.println("Date to view timetables for: (YYYY-MM-DD)");
                date = sc.nextLine();
                date = LocalDate.parse(date).toString();
                System.out.println("Date accepted");
                dateChosen = true;
            }
            catch(DateTimeParseException ex)
            {
                System.out.println("Invalid date format!");
            }
        }
        //window for viewing job completions
        System.out.println("Viewing Job Completions for date: " + date);
        int[] choices = {0,1,2};
        int choice = 1;
        JobCompletion[] viewWindow;
        int offset = 0;
        do
        {
            viewWindow = jm.getJobCompletions(date, as.getViewWindowSize(), offset);
            if (viewWindow.length > 0)
            {
                for (JobCompletion j : viewWindow)
                {
                    if (j != null)
                    {
                        System.out.println(j.toString() + "\n");
                    }
                }
            }
            else
            {
                System.out.println("End of Job Completions");
            }
            
            System.out.println("0- Go Back");
            System.out.println("1- Previous Page");
            System.out.println("2- Next Page");
            choice = getChoice(choices);
            switch(choice)
            {
                case 1:
                    if (offset > 0)
                    {
                        offset -= as.getViewWindowSize();
                    }
                    break;
                case 2:
                    if (viewWindow.length > 0)
                    {
                        offset += as.getViewWindowSize();
                    }
                    break;
                default:
                    break;
            }
        }
        while (choice != 0);
    }
    
    public void markJobForAssignment()
    {
        int ID = -1;
        System.out.println("Mark a job to be assigned by the system");
        sc.nextLine();
        //get the ID of the job to mark for assignment: make sure it is a job that is marked to be manually assigned
        boolean chosen = false;
        while (!chosen)
        {
            System.out.println("Job Description ID to assign:");
            try
            {
                ID = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                ID = -1;
            }
            if (jm.getJobDescription(ID).getRepetition() != -1 && jm.getJobDescription(ID).getID() > -1)
            {
                System.out.println("Job must not repeat automatically to be assigned manually.");
            }
            else if (jm.getJobDescription(ID).getRepetition() == -1 && jm.getJobDescription(ID).getID() != -1)
            {
                chosen = true;
            }
        }
        System.out.println("Job Description ID found in database");
        //get the date to assign to: it either needs to be in the present or future
        sc.nextLine();
        LocalDate now = LocalDate.now();
        LocalDate input;
        String date = "";
        boolean dateChosen = false;
        while (!dateChosen)
        {
            try
            {
                System.out.println("Date to assign job: (YYYY-MM-DD)");
                date = sc.nextLine();
                input = LocalDate.parse(date);
                if (input.isAfter(now) || input.isEqual(now))
                {
                    System.out.println("Date accepted");
                    dateChosen = true;
                }
                else
                {
                    System.out.println("Date of assignment cannot be in the past!");
                }
            }
            catch(DateTimeParseException ex)
            {
                System.out.println("Invalid date format!");
            }
        }
        int itemCount = -1;
        //get item count to assign
        while (itemCount <= 0)
        {
            System.out.println("Number of items to assign:");
            try
            {
                itemCount = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                itemCount = -1;
            }
        }
        sc.nextLine();
        boolean choice = getYorN("Confirm job assignment? y/n");
        if (choice)
        {
            boolean success = jm.addToAssignment(ID, date, itemCount);
            System.out.println(success ? "Successfully marked job for assignment" : "Error encountered. The job may have already been marked for assignment on the given date");
        }
        else
        {
            System.out.println("Cancelling assignment");
        }
    }
    
    public void viewJobDescriptions()
    {
        //window to view job descriptions
        System.out.println("Viewing Job Descriptions");
        int[] choices = {0,1,2};
        int choice = 1;
        JobDescription[] viewWindow;
        int offset = 0;
        do
        {
            viewWindow = jm.selectJobDescriptions(as.getViewWindowSize(), offset);
            if (viewWindow.length > 0)
            {
                for (JobDescription j : viewWindow)
                {
                    if (j != null)
                    {
                        System.out.println(j.toString() + "\n");
                    }
                }
            }
            else
            {
                System.out.println("End of Job Descriptions");
            }
            
            System.out.println("0- Go Back");
            System.out.println("1- Previous Page");
            System.out.println("2- Next Page");
            choice = getChoice(choices);
            switch(choice)
            {
                case 1:
                    if (offset > 0)
                    {
                        offset -= as.getViewWindowSize();
                    }
                    break;
                case 2:
                    if (viewWindow.length > 0)
                    {
                        offset += as.getViewWindowSize();
                    }
                    break;
                default:
                    break;
            }
        }
        while (choice != 0);
    }
    
    public void modifyJobDescription()
    {
        int ID = -1;
        String jobName = "";
        boolean isLoose;
        int repetition = -1;
        int timePerItem = -1;
        //get id of job description to modify
        System.out.println("Modify a Job Description");
        sc.nextLine();
        while (jm.getJobDescription(ID).getID() == -1)
        {
            System.out.println("Job Description ID to modify:");
            try
            {
                ID = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                ID = -1;
            }
        }
        System.out.println("Job Description ID found in database");
        // get all the data that is in the job description
        sc.nextLine();
        while (!jobName.matches("[a-zA-Z ]+"))
        {
            System.out.println("Job Name (alphabetic characters only): ");
            jobName = sc.nextLine();
        }
        System.out.println("Job name accepted");
        System.out.println("1- LOOSE time restriction");
        System.out.println("2- STRICT time restriction");
        isLoose = getChoice(new int[]{1,2}) == 1;
        
        sc.nextLine();
        if (getYorN("Set job to be repeated?"))
        {
            while (repetition <= 0)
            {
                System.out.println("Repeat job every ? days:");
                try
                {
                    repetition = sc.nextInt();
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input format!");
                    sc.next();
                    repetition = -1;
                }
            }
        }
        
        while (timePerItem <= 0)
        {
            System.out.println("Number of minutes per item:");
            try
            {
                timePerItem = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                timePerItem = -1;
            }
        }
        
        sc.nextLine();
        boolean choice = getYorN("Confirm modification of job description? y/n");
        if (choice)
        {
            jm.modifyJobDescription(new JobDescription(ID, jobName, (isLoose ? "LOOSE" : "STRICT"), repetition, timePerItem));
            System.out.println("Successfully modified job description");
        }
        else
        {
            System.out.println("Cancelling modification");
        }
    }
    
    public void createJobDescription()
    {
        String jobName = "";
        boolean isLoose;
        int repetition = -1;
        int timePerItem = -1;
        //get all the data needed to put into a job description
        sc.nextLine();
        while (!jobName.matches("[a-zA-Z ]+"))
        {
            System.out.println("Job Name (alphabetic characters only): ");
            jobName = sc.nextLine();
        }
        System.out.println("Job name accepted");
        System.out.println("1- LOOSE time restriction");
        System.out.println("2- STRICT time restriction");
        isLoose = getChoice(new int[]{1,2}) == 1;
        sc.nextLine();
        if (getYorN("Set job to be repeated?"))
        {
            while (repetition <= 0)
            {
                System.out.println("Repeat job every ? days:");
                try
                {
                    repetition = sc.nextInt();
                }
                catch (InputMismatchException e)
                {
                    sc.nextLine();
                    System.out.println("Invalid input format!");
                    repetition = -1;
                }
            }
        }
        
        while (timePerItem <= 0)
        {
            System.out.println("Number of minutes per item:");
            try
            {
                timePerItem = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                timePerItem = -1;
            }
        }
        
        sc.nextLine();
        boolean choice = getYorN("Confirm creation of job description? y/n");
        if (choice)
        {
            jm.insertJobDescription(new JobDescription(0, jobName, (isLoose ? "LOOSE" : "STRICT"), repetition, timePerItem));
            System.out.println("Successfully created job description");
        }
        else
        {
            System.out.println("Cancelling creation");
        }
    }
    
    public void generalSettingsMenu()
    {
        boolean isRunning = true;
        while (isRunning)
        {
            System.out.println("General Settings Menu");
            System.out.println("0- Go Back");
            System.out.println("1- Set system email address");
            System.out.println("2- Set number of slots in one day");
            System.out.println("3- Set number of items to view at one time");
            int[] choices = {0,1,2,3};
            int choice = getChoice(choices);
            switch (choice)
            {
                case 0:
                    isRunning = false;
                    break;
                case 1:
                    setEmail();
                    break;
                case 2:
                    setSlotCount();
                    break;
                case 3:
                    setWindowViewNum();
                    break;
                default:
                    break;
            }
        }
    }
    
    public void setWindowViewNum()
    {
        int viewNum = -1;
        //gets the size of the view window
        sc.nextLine();
        while (viewNum <= 0)
        {
            System.out.println("Number of items to view at a time:");
            try
            {
                viewNum = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                viewNum = -1;
            }
        }
        sc.nextLine();
        boolean choice = getYorN("Confirm change? y/n");
        if (choice)
        {
            as.setViewWindowSize(viewNum);
            System.out.println("Changed number of items to view");
        }
        else
        {
            System.out.println("Cancelling change");
        }
    }
    
    public void setSlotCount()
    {
        int slotCount = -1;
        //changes the number of slots for assigning during the day
        sc.nextLine();
        while (slotCount <= 0)
        {
            System.out.println("Number of slots in one day:");
            try
            {
                slotCount = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                slotCount = -1;
            }
        }
        sc.nextLine();
        boolean choice = getYorN("Confirm change slot count? y/n");
        if (choice)
        {
            as.setSlotCount(slotCount);
            System.out.println("Changed slot count");
        }
        else
        {
            System.out.println("Cancelling slot count change");
        }
    }
    
    public void setEmail()
    {
        //set the system email for sending emails to employees
        String email = "";
        sc.nextLine();
        while (!isValidEmailAddress(email))
        {
            System.out.println("New System Email: ");
            email = sc.nextLine();
        }
        System.out.println("System email accepted");
        boolean choice = getYorN("Confirm change system email? y/n");
        if (choice)
        {
            as.setEmail(email);
            System.out.println("Changed system email");
        }
        else
        {
            System.out.println("Cancelling changing system email");
        }
    }
    
    public void employeeManagementMenu()
    {
        boolean isRunning = true;
        while (isRunning)
        {
            System.out.println("Employee Management Menu");
            System.out.println("0- Go Back");
            System.out.println("1- View Employees");
            System.out.println("2- Create Employee");
            System.out.println("3- Modify Employee");
            System.out.println("4- Deactivate/Activate Employee");
            System.out.println("5- Set Working Days for Employee");
            int[] choices = {0,1,2,3,4,5};
            int choice = getChoice(choices);
            switch (choice)
            {
                case 0:
                    isRunning = false;
                    break;
                case 1:
                    viewEmployees();
                    break;
                case 2:
                    createEmployee();
                    break;
                case 3:
                    modifyEmployee();
                    break;
                case 4:
                    setEmployeeActive();
                    break;
                case 5:
                    setWorkingDays();
                default:
                    break;
            }
        }
    }
    
    public void setWorkingDays()
    {
        //allows to set working days for employees
        //get id of worker to modify
        int ID = -1;
        boolean isWorking;
        System.out.println("Set Working Days for Employee");
        sc.nextLine();
        while (em.getEmployee(ID).getID() == -1)
        {
            System.out.println("Employee ID to modify:");
            try
            {
                ID = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                ID = -1;
            }
        }
        System.out.println("Employee ID found in database");
        isWorking = em.getEmployee(ID).isEmployed();
        String employeeName = em.getEmployee(ID).getFirstName() + " " + em.getEmployee(ID).getLastName();
        if (isWorking)
        {
            //flip one of the working days of the employee
            System.out.println("Current working days for employee " + employeeName);
            LocalDate firstDay = LocalDate.of(2022, 11, 28);//a generic Monday
            boolean[] isWorkingArr = new boolean[7];
            for (int i = 1; i <= 7; i++)
            {
                isWorkingArr[i-1] = em.isEmployeeWorking(firstDay.toString(), ID);
                System.out.println(i + ": " + (isWorkingArr[i-1] ? "Is working" : "Is not working"));
                System.out.println(firstDay.getDayOfWeek());
                firstDay = firstDay.plusDays(1);
            }
            System.out.println("Choose a day to swap working status: ");
            int[] choices = {1,2,3,4,5,6,7};
            int choice = getChoice(choices);
            sc.nextLine();
            boolean confirm = getYorN("Swap working status of chosen day? y/n");
            if (confirm)
            {
                em.setEmployeeWorking(choice, !isWorkingArr[choice-1], ID);
                System.out.println("Successfully modified employee");
            }
            else
            {
                System.out.println("Cancelling employee modification");
            }
        }
        else
        {
            System.out.println("Employee is currently inactive");
            System.out.println("Please activate employee to modify working days");
        }
    }
    
    public void setEmployeeActive()
    {
        //change whether the employee is active or inactive for the assignment system
        int ID = -1;
        boolean isWorking;
        System.out.println("Deactivate/Activate Employee");
        sc.nextLine();
        while (em.getEmployee(ID).getID() == -1)
        {
            System.out.println("Employee ID to modify:");
            try
            {
                ID = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                ID = -1;
            }
        }
        System.out.println("Employee ID found in database");
        isWorking = em.getEmployee(ID).isEmployed();
        String employeeName = em.getEmployee(ID).getFirstName() + " " + em.getEmployee(ID).getLastName();
        System.out.println("Employee "+employeeName+" is currently " + (isWorking ? "active" : "inactive"));
        
        sc.nextLine();
        boolean choice = getYorN("Would you like to swap the employee's activity status? y/n");
        
        if (choice)
        {
            if(em.setEmployeeActive(ID, !isWorking))
            {
                System.out.println("Successfully modified employee");
            }
            else
            {
                System.out.println("Could not modify employee");
            }
        }
        else
        {
            System.out.println("Cancelling modification of employee");
        }
    }
    
    public void modifyEmployee()
    {
        //change employee data
        int ID = -1;
        String firstName = "", lastName = "", email = "";
        boolean isWorking;
        System.out.println("Modify an Employee");
        sc.nextLine();
        //get id of employee to modify
        while (em.getEmployee(ID).getID() == -1)
        {
            System.out.println("Employee ID to modify:");
            try
            {
                ID = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                ID = -1;
            }
        }
        System.out.println("Employee ID found in database");
        //get all employee data, but also dont change whether employee is active or not
        isWorking = em.getEmployee(ID).isEmployed();
        sc.nextLine();
        while (!firstName.matches("[a-zA-Z ]+"))
        {
            System.out.println("Employee First Name (alphabetic characters only): ");
            firstName = sc.nextLine();
        }
        System.out.println("Employee first name accepted");
        while (!lastName.matches("[a-zA-Z ]+"))
        {
            System.out.println("Employee Last Name (alphabetic characters only): ");
            lastName = sc.nextLine();
        }
        System.out.println("Employee last name accepted");
        while (!isValidEmailAddress(email))
        {
            System.out.println("Employee Email: ");
            email = sc.nextLine();
        }
        System.out.println("Employee email accepted");
        
        boolean choice = getYorN("Modify employee? y/n");
        
        if (choice)
        {
            if(em.modifyEmployee(new Employee(ID, firstName, lastName, email, isWorking)))
            {
                System.out.println("Successfully modified employee");
            }
            else
            {
                System.out.println("Could not modify employee");
            }
        }
        else
        {
            System.out.println("Cancelling modification of employee");
        }
    }
    
    public void createEmployee()
    {
        //create a new employee
        //get employee data
        String firstName = "", lastName = "", email = "";
        System.out.println("Create an Employee");
        sc.nextLine();
        //simple regex to accept strings with alphabetic characters and spaces
        while (!firstName.matches("[a-zA-Z ]+"))
        {
            System.out.println("Employee First Name (alphabetic characters only): ");
            firstName = sc.nextLine();
        }
        System.out.println("Employee first name accepted");
        while (!lastName.matches("[a-zA-Z ]+"))
        {
            System.out.println("Employee Last Name (alphabetic characters only): ");
            lastName = sc.nextLine();
        }
        System.out.println("Employee last name accepted");
        while (!isValidEmailAddress(email))
        {
            System.out.println("Employee Email: ");
            email = sc.nextLine();
        }
        System.out.println("Employee email accepted");
        
        boolean choice = getYorN("Create employee? y/n");
        
        if (choice)
        {
            if(em.insertEmployee(new Employee(0, firstName, lastName, email, true)))
            {
                System.out.println("Successfully created employee");
            }
            else
            {
                System.out.println("Could not insert employee");
            }
        }
        else
        {
            System.out.println("Cancelling creation of employee");
        }
    }
    
    public void viewEmployees()
    {
        //view window to view all employees
        System.out.println("Viewing Employees");
        int[] choices = {0,1,2};
        int choice = 1;
        Employee[] viewWindow;
        int offset = 0;
        do
        {
            viewWindow = em.selectEmployees(as.getViewWindowSize(), offset);
            if (viewWindow.length > 0)
            {
                for (Employee e : viewWindow)
                {
                    if (e != null)
                    {
                        System.out.println(e.toString() + "\n");
                    }
                }
            }
            else
            {
                System.out.println("End of Employees");
            }
            
            System.out.println("0- Go Back");
            System.out.println("1- Previous Page");
            System.out.println("2- Next Page");
            choice = getChoice(choices);
            switch(choice)
            {
                case 1:
                    if (offset > 0)
                    {
                        offset -= as.getViewWindowSize();
                    }
                    break;
                case 2:
                    if (viewWindow.length > 0)
                    {
                        offset += as.getViewWindowSize();
                    }
                    break;
                default:
                    break;
            }
        }
        while (choice != 0);
    }
    
    public int getChoice(int[] choices)
    {
        //gets an integer choice out of an array of choices
        int choice;
        do
        {
            System.out.println("Choice: ");
            try
            {
                choice = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Invalid input format!");
                sc.next();
                choice = -1;
            }
        } while (!arrayContains(choices, choice));
        return choice;
    }
    
    public boolean arrayContains(int[] array, int elem)
    {
        //checks whether an int array contains an integer
        boolean found = false;
        for (int e : array)
        {
            if (e == elem)
            {
                found = true;
            }
        }
        return found;
    }
    
    public boolean isValidEmailAddress(String email) 
    {
        //basic test whether an email address is valid
        boolean result = true;
        try 
        {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        }
        catch (AddressException ex) 
        {
            result = false;
        }
        return result;
    }
    
    public boolean getYorN(String prompt)
    {
        //gets a yes or no answer
        boolean result;
        String choice = "";
        while (!"y".equals(choice.toLowerCase()) && !"n".equals(choice.toLowerCase()))
        {
            System.out.println(prompt);
            choice = sc.nextLine();
        }
        result = "y".equals(choice.toLowerCase());
        return result;
    }
}
