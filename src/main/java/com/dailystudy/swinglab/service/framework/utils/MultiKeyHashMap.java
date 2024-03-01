package com.dailystudy.swinglab.service.framework.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.*;

/**
 * MultiKeyHashMap provides to store values with two level hierarchy of keys,
 * super key (K1) and sub key (K2). The objects are inserted using super and sub keys.
 * It is not mandatory to use both keys as hierarchy, user can use two keys to store
 * the values and use either of the key to retrieve it.
 *
 * @author Prathab K
 */
public class MultiKeyHashMap<K1, K2, V>
{

    /**
     * Map structure holding another Map structure to implement MultiKeyHashMap
     */
    private final Map<K1, Map<K2, V>> mkMap = new HashMap<>();

    private final Set<K2> emptySet = ImmutableSet.<K2>builder().build();
    private final List<V> emptyValueSet = ImmutableList.<V>builder().build();

    /**
     * Initializes the MultiKeyHashMap
     */
    public MultiKeyHashMap()
    {
    }

    /**
     * Puts the value object based on the (super)key K1 and (sub)key K2
     *
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @param v  value object
     * @return previous value associated with specified key, or <tt>null</tt>
     * if there was no mapping for key.
     */
    public V put(K1 k1, K2 k2, V v)
    {
        Map<K2, V> k2Map = null;
        if (mkMap.containsKey(k1))
        {
            k2Map = mkMap.get(k1);
        } else
        {
            k2Map = new HashMap<K2, V>();
            mkMap.put(k1, k2Map);
        }
        return k2Map.put(k2, v);
    }

    /**
     * Returns <tt>true</tt> if value object is present for the specified (super)key K1 and (sub)key K2
     *
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return <tt>true</tt> if value object present
     */
    public boolean containsKey(K1 k1, K2 k2)
    {
        if (mkMap.containsKey(k1))
        {
            Map<K2, V> k2Map = mkMap.get(k1);
            return k2Map.containsKey(k2);
        }
        return false;
    }

    /**
     * Returns <tt>true</tt> if value object is present for the specified (super)key K1
     *
     * @param k1 key1 (super-key)
     * @return <tt>true</tt> if value object present
     */
    public boolean containsKey(K1 k1)
    {
        return mkMap.containsKey(k1);
    }

    /**
     * Gets the value object for the specified (super)key K1 and (sub)key K2
     *
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public V get(K1 k1, K2 k2)
    {
        if (mkMap.containsKey(k1))
        {
            Map<K2, V> k2Map = mkMap.get(k1);
            return k2Map.get(k2);
        }
        return null;
    }

    /**
     * Gets the value object for the specified (super)key K1
     *
     * @param k1 key1 (super-key)
     * @return HashMap structure contains the values for the key k1 if exists
     * or <tt>null</tt> if does not exists
     */
    public Map<K2, V> get(K1 k1)
    {
        return mkMap.get(k1);
    }

    /**
     * Gets the value object for the specified (sub)key K2
     *
     * @param k2 key2 (sub-key)
     * @return value object if exists or <tt>null</tt> if does not exists
     */
    public V getBySubKey(K2 k2)
    {
        for (Map<K2, V> m : mkMap.values())
        {
            if (m.containsKey(k2))
            {
                return m.get(k2);
            }
        }
        return null;
    }

    /**
     * Removes the value object for the specified (super)key K1 and (sub)key K2
     *
     * @param k1 key1 (super-key)
     * @param k2 key2 (sub-key)
     * @return previous value associated with specified key, or <tt>null</tt>
     * if there was no mapping for key.
     */
    public V remove(K1 k1, K2 k2)
    {
        if (mkMap.containsKey(k1))
        {
            Map<K2, V> k2Map = mkMap.get(k1);
            return k2Map.remove(k2);
        }
        return null;
    }

    /**
     * Removes the value object(s) for the specified (super)key K1
     *
     * @param k1 key1 (super-key)
     * @return previous value (HashMap structure) associated with specified key, or <tt>null</tt>
     * if there was no mapping for key.
     */
    public Map<K2, V> remove(K1 k1)
    {
        return mkMap.remove(k1);
    }

    /**
     * Size of MultiKeyHashMap
     *
     * @return MultiKeyHashMap size
     */
    public int size()
    {
        int size = 0;
        for (Map<K2, V> m : mkMap.values())
        {
            size++;
            size += m.size();
        }
        return size;
    }

    public int size(K1 k1)
    {
        Map<K2, V> k2VMap = get(k1);
        if (k2VMap == null)
        {
            return 0;
        } else
        {
            return k2VMap.size();
        }
    }

    /**
     * Clears the entire hash map
     */
    public void clear()
    {
        for (Map<K2, V> m : mkMap.values())
        {
            m.clear();
        }

        mkMap.clear();
    }

    public Set<K2> keys(K1 key)
    {
        Map<K2, V> mapItem = mkMap.get(key);
        if (mapItem == null)
        {
            return emptySet;
        }

        return mapItem.keySet();
    }

    public List<V> values(K1 key)
    {
        Map<K2, V> mapItem = mkMap.get(key);
        if (mapItem == null)
        {
            return emptyValueSet;
        }

        Collection<V> values = mapItem.values();
        if (values instanceof List)
        {
            return (List<V>)values;
        } else
        {
            return new ArrayList<>(values);
        }
    }

    /**
     * Returns all the value objects in the MultiKeyHashMap
     *
     * @return value objects as List
     */
    public List<V> values()
    {
        Collection<Map<K2, V>> values1 = mkMap.values();

        List<V> values = new ArrayList<>();
        for(Map<K2,V> m : values1)
        {
            values.addAll(m.values());
        }
        return values;
    }

    public Set<K1> keys()
    {
        return mkMap.keySet();
    }

    /**
     * key 가 존재하지 않으면 true.
     * @param key1
     * @param key2
     * @return
     */
    public boolean isAbsent(K1 key1, K2 key2)
    {
        Map<K2, V> k2VMap = get(key1);
        if (k2VMap != null)
        {
            return !k2VMap.containsKey(key2);
        } else
        {
            return true;
        }
    }

    public void put(K1 key1, Map<K2, V> v2Map)
    {
        mkMap.put(key1, v2Map);
    }
}