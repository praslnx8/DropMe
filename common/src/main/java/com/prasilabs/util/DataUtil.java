package com.prasilabs.util;

/**
 * Created by prasi on 27/5/16.
 */
public class DataUtil
{
    public static boolean isEmpty(String s)
    {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(Long s)
    {
        return s == null || s == 0L;
    }

    public static boolean isEmpty(Boolean s)
    {
        return s == null || !s;
    }

    public static boolean isEmpty(Double s)
    {
        return s == null || s == 0 || s == 0.0;
    }

    public static int stringToInt(String s)
    {
        try
        {
            if(!isEmpty(s))
            {
                return Integer.parseInt(s);
            }
        }
        catch (Exception e) {}

        return 0;
    }

    public static Double stringToDouble(String s)
    {
        try
        {
            if(!isEmpty(s))
            {
                return Double.parseDouble(s);
            }
        }
        catch (Exception e) {}

        return 0.0;
    }

    public static int longToInt(long l) {
        try {
            return (int) l;
        } catch (Exception e) {
        }

        return 0;
    }

    public static long stringToLong(String s)
    {
        try
        {
            if(!isEmpty(s))
            {
                return Long.parseLong(s);
            }
        }
        catch (Exception e) {}

        return 0;
    }
}
