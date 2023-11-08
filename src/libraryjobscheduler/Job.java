/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryjobscheduler;

/**
 *
 * @author bryan
 */
public class Job
{
    private final int ID;
    private final String name;
    private final int itemCount;

    public Job(int ID, String name, int itemCount) {
        this.ID = ID;
        this.name = name;
        this.itemCount = itemCount;
    }

    public int getID()
    {
        return ID;
    }

    public String getName() 
    {
        return name;
    }

    public int getItemCount() 
    {
        return itemCount;
    }
    
    @Override
    public String toString()
    {
        return "ID = " + ID + 
               "\nName = " + name +
               "\n Item Count = " + itemCount;
    }
}
