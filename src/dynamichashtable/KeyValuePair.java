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
public class KeyValuePair<K,V>
{
    
    public KeyValuePair(K _key, V _value, boolean _deleted)
    {
        key = _key;
        value = _value;
        deleted = _deleted;
    }
    
    private K key;
    private V value;

    public K key() {
        return key;
    }

    public void key(K key) {
        this.key = key;
    }

    public V value() {
        return value;
    }

    public void value(V value) {
        this.value = value;
    }
    private boolean deleted;
    
    public boolean isDeleted()
    {
        return deleted;
    }
    
    public void setDeleted(boolean value)
    {
        deleted = value;
    }
}
