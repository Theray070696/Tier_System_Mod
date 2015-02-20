package io.github.Theray070696.tiersystem.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Theray on 10/31/14.
 */
public class ArrayUtilities
{

    public static int[] objectArrayToIntArray(Object[] objectArray)
    {
        int[] returnInt = new int[objectArray.length];
        for(int i = 0; i < objectArray.length; i++)
        {
            returnInt[i] = (Integer) objectArray[i];
        }
        return returnInt;
    }

    public static String[] objectArrayToStringArray(Object[] objectArray)
    {
        String[] returnString = new String[objectArray.length];
        for(int i = 0; i < objectArray.length; i++)
        {
            returnString[i] = (String) objectArray[i];
        }
        return returnString;
    }

    public static <T> boolean haveSameElements(Collection<T> col1, Collection<T> col2)
    {
        if(col1 == col2)
        {
            return true;
        }

        // If either list is null, return whether the other is empty
        if(col1 == null)
        {
            return col2.isEmpty();
        }
        if(col2 == null)
        {
            return col1.isEmpty();
        }

        // If lengths are not equal, they can't possibly match
        if(col1.size() != col2.size())
        {
            return false;
        }

        // Helper class, so we don't have to do a whole lot of autoboxing
        class Count
        {

            // Initialize as 1, as we would increment it anyway
            public int count = 1;
        }

        final Map<T, Count> counts = new HashMap<T, Count>();

        // Count the items in list1
        for(final T item : col1)
        {
            final Count count = counts.get(item);
            if(count != null)
            {
                count.count++;
            } else
            {
                // If the map doesn't contain the item, put a new count
                counts.put(item, new Count());
            }
        }

        // Subtract the count of items in list2
        for(final T item : col2)
        {
            final Count count = counts.get(item);
            // If the map doesn't contain the item, or the count is already reduced to 0, the lists are unequal
            if(count == null || count.count == 0)
            {
                return false;
            }
            count.count--;
        }

        // If any count is nonzero at this point, then the two lists don't match
        for(final Count count : counts.values())
        {
            if(count.count != 0)
            {
                return false;
            }
        }

        return true;
    }
}