/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkedlist;

/**
 *
 * @author b-abi-karam
 */
public class Element<T>
{
    private T value;
    private Element<T> previous;
    private Element<T> next;
        
    public Element(T value, Element<T> previous, Element<T> next)
    {
        this.value = value;
        this.previous = previous;
        this.next = next;           
    }      
    
    @Override
    public String toString()
    {
        return value.toString();
    }

    public T Value()
    {
        return value;              
    }
    
    public void Value(T value)
    {
        this.value = value;
    }
   
    public Element<T> Previous()
    {
        return previous;                
    }
    
    public void Previous(Element value)
    {
        previous = value;                
    }
    
    public Element<T> Next()
    {
        return next;            
    }    
    
    public void Next(Element value)
    {
        next = value;                
    }
}
