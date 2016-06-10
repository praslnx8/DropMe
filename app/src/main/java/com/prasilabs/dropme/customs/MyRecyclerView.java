package com.prasilabs.dropme.customs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/*
 * Created by prasi on 8/2/16.
 * this recycler view is suitable for pagination purpose
 * */


public class MyRecyclerView extends RecyclerView
{
    public static final int VERTICAL_MODE = 0;
    public static final int HORIZONTAL_MODE = 1;

    private int skip = 0;
    public int pageSize = 10; //default page size
    private int prevItemCount = 0;

    private Context context;
    private PreCachingLayoutManager linearLayoutManager;
    private HandlePaginationScroll handlePaginationScroll;

    private int visibleItemCount, totalItemCount, pastVisiblesItems;

    private boolean isPageLoading = false;//default true until page is loaded from api call

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        setRecyclerView(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        setRecyclerView(context);
    }

    public MyRecyclerView(Context context)
    {
        super(context);

        setRecyclerView(context);
    }

    private void setRecyclerView(Context context)
    {
        this.context = context;

        //default manager
        linearLayoutManager = new PreCachingLayoutManager(context);
        setLayoutManager(linearLayoutManager);
        setHasFixedSize(true);
        setItemAnimator(new DefaultItemAnimator());
    }

    public void setType(int mode)
    {
        if(mode == VERTICAL_MODE)
        {
            setLayoutManager(linearLayoutManager);
        }
        else
        {
            linearLayoutManager = new PreCachingLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            setLayoutManager(linearLayoutManager);
        }
    }

    public void handleScroll(final HandlePaginationScroll handlePaginationScroll)
    {
        this.handlePaginationScroll = handlePaginationScroll;
        addOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                if(linearLayoutManager != null)
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (dy > 0 && getAdapter().getItemCount() > 0)
                    {
                        if(!isPageLoading)
                        {
                            if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount))
                            {
                                skip ++;

                                isPageLoading = true;
                                handlePaginationScroll.onScroll(pageSize, skip);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setAdapter(final Adapter adapter)
    {

        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                int newItemCount = adapter.getItemCount();

                if(newItemCount < prevItemCount)
                {
                    skip = 0;
                    isPageLoading = false;
                }
                else if(newItemCount > prevItemCount)
                {
                    isPageLoading = false;
                }
                else
                {
                    //none dont refresh
                }

                if(newItemCount != 0) //TODO
                {
                    /*if (newItemCount * getChildAt(0).getHeight() < getHeight()) {
                        if (!isPageLoading) {
                            if (handlePaginationScroll != null) {
                                handlePaginationScroll.onScroll(pageSize, offset);
                            }
                        }
                    }*/
                }


                prevItemCount = newItemCount;

            }
        });

        prevItemCount = adapter.getItemCount();

        super.setAdapter(adapter);
    }

    public void setOffset(int skip)
    {
        this.skip = skip;
    }

    public interface HandlePaginationScroll
    {
        void onScroll(int pageSize, int skip);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
