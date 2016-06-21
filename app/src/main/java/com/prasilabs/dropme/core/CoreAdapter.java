package com.prasilabs.dropme.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.debug.ConsoleLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 4/3/16.
 */
public abstract class CoreAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>
{
    private static final String TAG = CoreAdapter.class.getSimpleName();
    protected List<E> list = new ArrayList<E>();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            notifyDataSetChanged();
        }
    };

    private LocalBroadcastManager localBroadcastManager;

    public void clearAndAddItem(E item)
    {
        this.list.clear();
        notifyDataSetChanged();
        addListItem(item);
    }

    public View getInlfatedView(Context context, ViewGroup parent, int layoutResourceId) {
        View view = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);

        return view;
    }

    public void clearAndAddItem(List<E> list)
    {
        this.list.clear();
        notifyDataSetChanged();
        addListItem(list);
    }

    public void bindToBroadCast(Context context, String broadCast)
    {
        if(!TextUtils.isEmpty(broadCast))
        {
            bindToBroadCast(context, new String[]{broadCast});
        }
    }

    public void bindToBroadCast(Context context, String[] broadCastArray)
    {
        if(broadCastArray != null && broadCastArray.length > 0)
        {
            IntentFilter intentFilter = new IntentFilter();
            for(String broadcast : broadCastArray)
            {
                if(!TextUtils.isEmpty(broadcast)) {
                    intentFilter.addAction(broadcast);
                }
            }
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    public void addListItem(List<E> list)
    {
        if(list != null)
        {
            int prevSize = list.size();
            this.list.addAll(list);
            notifyDataSetChanged();
        }
        else
        {
            ConsoleLog.w(TAG, "list is null");
        }
    }

    public void addListItem(E item)
    {
        if(item != null)
        {
            this.list.add(0, item);
            notifyItemChanged(0);
        }
    }
}
