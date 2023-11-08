/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamichashtable;

/**
 *
 * @author b-abi-karam
 */

import java.util.Arrays;
import linkedlist.LinkedList;

public class HashTable<K,V>
{
    //private LinkedList<KeyValuePair<K,V>> hashMap;
    private KeyValuePair<K,V>[] hashMap;
    
    private final int startSize = 1;
    
    public HashTable()
    {
        hashMap = new KeyValuePair[startSize];
        
        //alternatively use this - appropriate when hidden, e.g. backing a datastructure
        //E[] arr = (E[])new Object[INITIAL_ARRAY_LENGTH];
        //hashMap = (KeyValuePair<K,V>[])new Object[startSize];
        
        //fill with empty kvps
        
        Arrays.fill(hashMap, new KeyValuePair<K,V>(null,null,true));
        
    }
    
    private void rehash()
    {
        //create tempmap
        KeyValuePair[] tempMap = hashMap;
        
        //empty og hashmap and increase size
        
        int size = (int)((tempMap.length+1)/0.75);
        hashMap = new KeyValuePair[size];
        Arrays.fill(hashMap, new KeyValuePair<K,V>(null,null,true));
        
        
        //rehash from tempmap to hashmap
        for (KeyValuePair kvp : tempMap) 
        {
            add((K) kvp.key(), (V) kvp.value());
        }
    }
    
    public void add(K _key, V _value)
    {
        boolean added = false;
        
        int index = hash(_key, hashMap.length);
        
        do
        {
            if(hashMap[index].isDeleted())
            {
                KeyValuePair<K,V> kvp = new KeyValuePair<>(_key,_value, false);
                hashMap[index]=kvp;
                added = true;
            }
            else if (hashMap[index].key() == _key)
            {
                //key already added to hashmap, throw IllegalArgumentException
                throw new IllegalArgumentException("The key was already added to the Hash Table!");
            }
            index = (index+1)%hashMap.length;
            if (index%hashMap.length == hash(_key, hashMap.length) && !added) //the pointer has made a complete loop of the hashmap
            {
                rehash();
                add(_key,_value);
                added = true;
            }
        } while(!added);
    }
    
    public V item(K _key)
    {
        int index = itemIndex(_key);
        if (index >= 0)
        {
            return (V)hashMap[itemIndex(_key)].value();
        }
        else
        {
            throw new IllegalArgumentException("The key doesn't exist in the Hash Table!");
        }
    }
    
    public void set(K _key, V _newValue)
    {
        if (contains(_key))
        {
            hashMap[itemIndex(_key)].value(_newValue);
        }
        else
        {
            throw new IllegalArgumentException("The key doesn't exist in the Hash Table!");
        }
    }

    public void delete(K _key)
    {
        int index = itemIndex(_key);
        if (index >= 0) 
        {
            hashMap[itemIndex(_key)].setDeleted(true);
        }
        else //if the index is -1 then the key doesnt exist, so we throw error
        {
            throw new IllegalArgumentException("The key doesn't exist in the Hash Table!");
        }
    }
    
    private int itemIndex(K _key)
    {
        boolean found = false;
        
        int index = hash(_key, hashMap.length);
        
        int returnItem = -1;
        
        KeyValuePair<K,V> kvp;
        
        do
        {
            kvp = hashMap[index];
            if (!kvp.isDeleted() && kvp.key() == _key) //item is not deleted and found
            {
                returnItem = index;
                found = true;
            }
            index = (index+1)%hashMap.length; //wrap around the hashmap
            
            if (index%hashMap.length == hash(_key, hashMap.length) && !found) //has made a full loop and so isnt found
            {
                //don't throw an error here as we can use the -1 value to show that the key isnt in the table
                //this is useful in the "contains" method
                found = true;
            }
        } while (!found);
        
        return returnItem;
    }
    
    public boolean contains(K _key)
    {
        return itemIndex(_key)>=0;
    }
    
    public int length()
    {
        int count = 0;
        for (int i=0;i<hashMap.length;i++)
        {
            KeyValuePair<K,V> kvp = hashMap[i];
            if (!kvp.isDeleted())
            {
                count++;
            }
        }
        return count;
    }
    
    public boolean isEmpty()
    {
        return length()<1;
    }

    public boolean isFull()
    {
        return length()==hashMap.length;
    }
    
    private int hash(K _key, int size)
    {
        String keyString = _key.toString();

        int total = 0;

        for (int i = 0; i < keyString.length(); i++)
        {
            int asciiValue = keyString.charAt(i);
            total += asciiValue * (i+1);
        }
        return total%size;
    }
    
    public LinkedList<K> getKeys()
    {
        LinkedList<K> returnKeys = new LinkedList<>();

        for (KeyValuePair<K, V> kvp : hashMap) {
            if (!kvp.isDeleted())
            {
                returnKeys.append(kvp.key());
            }
        }
        return returnKeys;
    }
    
    @Override
    public String toString()
    {
        String message = "{";
        for (int i = 0; i < hashMap.length; i++)
        {
            if (!hashMap[i].isDeleted() && i < hashMap.length-1)
            {
                message += hashMap[i].key().toString() + ":" + hashMap[i].value().toString() + ",";
            }
            else if (!hashMap[i].isDeleted())
            {
                message += hashMap[i].key().toString() + ":" + hashMap[i].value().toString();
            }
        }
        message += "}";
        return message;
    }
}
