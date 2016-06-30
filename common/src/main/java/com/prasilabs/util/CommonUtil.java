package com.prasilabs.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by prasi on 10/6/16.
 */
public class CommonUtil
{
    public static <T> List getSubList(int skip, int pageSize, List<T> list)
    {
        int offset = skip*pageSize;
        if(offset < 0)
        {
            offset = 0;
        }
        if(pageSize == -1 || offset < pageSize)
        {
            pageSize = list.size();
        }

        int subSize = list.size() >= (offset + pageSize) ? (offset + pageSize) : list.size();

        List<T> tempList = new ArrayList<>();
        if(list.size() > offset && subSize > offset)
        {
            tempList.addAll(list.subList(offset, subSize));
        }

        return tempList;

    }

    public static int getRandomNumber(int start, int end) {
        Random rand = new Random();
        return rand.nextInt((end + 1) - start) + start;
    }
}
