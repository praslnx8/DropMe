package com.prasilabs.util;

/**
 * Created by prasi on 27/5/16.
 */
public class DataUtil
{
    public static boolean isStringEmpty(String s)
    {
        return s == null || s.length() == 0;
    }

    public static int stringToInt(String s)
    {
        try
        {
            if(!isStringEmpty(s))
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
            if(!isStringEmpty(s))
            {
                return Double.parseDouble(s);
            }
        }
        catch (Exception e) {}

        return 0.0;
    }

    public static long stringToLong(String s)
    {
        try
        {
            if(!isStringEmpty(s))
            {
                return Long.parseLong(s);
            }
        }
        catch (Exception e) {}

        return 0;
    }
}
