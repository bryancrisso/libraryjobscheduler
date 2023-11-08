/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkedlist;

import java.util.Arrays;

/**
 *
 * @author b-abi-karam
 */
public class LinkedList<T>// implements Iterable<T>
{
    private Element<T> front;
    
    private int length = 0;
    
    public String[] asStringArray()
    {//Converts the list into an array
        String[] returnArray = new String[length()];
        
        for (int i = 0; i < length(); i++)
        {
            returnArray[i] = get(i).toString();
        }
        
        return returnArray;
    }
    @Override
    public String toString()
    {
        return Arrays.toString(asStringArray());
    }
    
    public boolean isEmpty()
    {//Return is the queue is empty
        return length()==0;
    }

    public void append(T value)
    {//Append to the end of the list 
        Element<T> current;
        Element<T> tail;
        
        if (front != null)
        {       
            current = front;// Start at the front of the LinkedList
            
            while (current.Next() != null)
            {//Iterate through elements in the LinkedList
                current = current.Next();           
            }
            
            tail = new Element(value, current, null);// Create new tail Element pointing back to the previous Tail
            current.Next(tail);//Update the end of the LinkedList to point to this new Element
            length++;
        }
        else
        {//Add the front of the linked list
            front = new Element(value, null, null);
            length++;
        }
    }
    
    public void remove(T value)
    {//Remove the first element that has a matching value, and reorganise the queue
        int index = index(value);
        if (index != -1)
        {
            pop(index);
        }
        else
        {
            throw new IllegalArgumentException("value not found in list!");
        }
    }
   
    public T pop()
    {//Remove the 1ast element from the queue and reorganise the queue
        return pop(0);
    }
    
    public T pop(int index)
    {//Remove from the queue the element at the specified index  reorganise the queue
        Element<T> current;
        
        current = getElement(index);
        if (index == 0) //if front
        {
            front = current.Next();
            if (current.Next() != null)
            {
                current.Next().Previous(null);
            }
        }
        else if (index + 1 == length()) //if end
        {
            current.Previous().Next(null);
        } 
        else //somewhere in the middle
        {
            current.Previous().Next(current.Next());
            current.Next().Previous(current.Previous());
        }
        length--;
        return current.Value();
    }
    
    public T get(int index)
    {
        return getElement(index).Value();
    }
    
    private Element<T> getElement(int index)
    {
        Element<T> element;
        Element<T> current;
        int currentIndex = 0;
        
        if (index < length() && index >= 0)
        {
            current = front;
            while (currentIndex < index)
            {
                current = current.Next();
                currentIndex++;
            }
            
            element = current;
        }
        else
        {
            throw new IndexOutOfBoundsException("Given index is out of bounds!");
        }
        return element;
    }
    
    public void set(T value, int index)
    {//change the value at index
        Element<T> current = getElement(index);
        
        current.Value(value);
    }
    
    public void insert(T value, int index)
    {//Insert the item into the correct position in the linked list 
        Element<T> element = new Element(value, null, null);
        
        Element<T> current = getElement(index);
        
        
        element.Next(current);
        
        if (index != 0)
        {
            element.Previous(current.Previous());
            current.Previous().Next(element);
        }
        else
        {
            front = element;
        }
        
        current.Previous(element);
        
        length++;
    }
    
    public int index(T value)
    {//Return the position of the value in the linked list, -1 if not present
        int index = -1;
        int currentIndex = 0;
        Element e = front;
        
        while (e != null)
        {//Iterate through elements adding to the array
            if (e.Value().equals(value))
            {
                index = currentIndex;
                break;
            }
            e = e.Next();
            currentIndex++;
        }
        return index;
    }
    
    public int length()
    {//Return the number of elements in the Linked List
        return length;
    }
    
    public boolean search(T value)
    {//Return true if the value exists in the Linked List
        return index(value)!=-1;
    }

    private Element<T> getHead()
    {
        return front;
    }
}
