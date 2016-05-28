package com.prasilabs.dropme.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.customs.CircleTransform;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by prasi on 26/5/16.
 */
public class ViewUtil
{
    public static void t(Context context, String message)
    {
        if(context != null && !TextUtils.isEmpty(message))
        {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void hideProgressView(Context context, ViewGroup viewGroup)
    {
        showProgressView(context, viewGroup, false, false);
    }

    public static void showProgressView(Context context, ViewGroup viewGroup, boolean isBig)
    {
        showProgressView(context, viewGroup, isBig, true);
    }

    private static void showProgressView(Context context, ViewGroup viewGroup, boolean isBig, boolean isShow)
    {
        if(isShow)
        {
            View view = View.inflate(context, R.layout.widget_progress, null);

            ProgressBar bigProgressBar = (ProgressBar) view.findViewById(R.id.big_progress_bar);
            ProgressBar smallProgressBar = (ProgressBar) view.findViewById(R.id.small_progress_bar);

            if(isBig)
            {
                bigProgressBar.setVisibility(View.VISIBLE);
                smallProgressBar.setVisibility(View.GONE);
            }
            else
            {
                bigProgressBar.setVisibility(View.GONE);
                smallProgressBar.setVisibility(View.VISIBLE);
            }



            for (int i = 0; i < viewGroup.getChildCount(); i++)
            {
                viewGroup.getChildAt(i).setVisibility(View.GONE);
            }
            viewGroup.addView(view);
        }
        else
        {
            if(viewGroup.getChildCount() > 0)
            {
                if(viewGroup.getChildCount() > 1)
                {
                    for(int i= 0; i<viewGroup.getChildCount() -1; i++)
                    {
                        viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }

                viewGroup.removeView(viewGroup.getChildAt(viewGroup.getChildCount() - 1));
            }
        }
    }

    public static void renderImage(ImageView view, String url, boolean isCircle)
    {
        if(!TextUtils.isEmpty(url))
        {
            try
            {
                RequestCreator requestCreator = Picasso.with(view.getContext()).load(url).placeholder(R.drawable.ic_file);
                if(isCircle)
                {
                    requestCreator.transform(new CircleTransform());
                }
                requestCreator.into(view);
            }
            catch (Exception e)
            {
                ConsoleLog.e(e);
            }
        }
    }

    public static String formatAsName(String s)
    {
        //TODO
        return s;
    }
}
