/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryjobscheduler;

import linkedlist.LinkedList;
/**
 *
 * @author bryan
 */
public class Timetable 
{
    private final LinkedList<Job> slots;
    private final int size;
    
    public Timetable(int slotCount)
    {
        size = slotCount;
        slots = new LinkedList<>();
        for (int i = 0; i < size; i++)
        {
            slots.append(new Job(-1, "N/A", -1));
        }
    }
    
    public int getSize()
    {
        return size;
    }
    
    public void setSlot(int slot, int jobID, String jobName, int itemCount)
    {
        slots.set(new Job(jobID, jobName, itemCount), slot);
    }
    
    public Job getSlot(int slot)
    {
        return slots.get(slot);
    }
    
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++)
        {
            str.append(getSlot(i).toString()).append("\n");
        }
        return str.toString();
    }
}
