package com.prasilabs.dropme.backend.utils;

import com.google.apphosting.api.DatastorePb.Query.Order.Direction;

/**
 * Created by prasi on 26/4/16.
 */
public class SortWrapper
{
    private static final String TAG = SortWrapper.class.getSimpleName();
    private String sortKey;
    private Direction sortOrder;

    public SortWrapper(String key, Direction order)
    {
        this.sortKey = key;
        this.sortOrder = order;
    }

    public String getSorting()
    {
        if(sortKey != null)
        {
            String sort = "";
            if(sortOrder == Direction.DESCENDING)
            {
                sort += "-";
            }

            sort += sortKey;

            return sort;
        }

        return null;
    }
}
