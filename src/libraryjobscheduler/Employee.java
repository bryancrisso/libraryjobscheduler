/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryjobscheduler;

/**
 *
 * @author bryan
 */
public class Employee 
{
    private final int ID;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final boolean isEmployed;

    public Employee(int ID, String firstName, String lastName, String email, boolean isEmployed) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isEmployed = isEmployed;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmployed() {
        return isEmployed;
    }

    @Override
    public String toString()
    {
        return "ID = " + getID() + "\nName = " + getFirstName() + " " + getLastName() + "\nEmail Address = " + getEmail()
                + "\nIs Employed = " + isEmployed();
    }
}
