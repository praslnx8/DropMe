package com.prasilabs.dropme.modules.splashLogin.views;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.utils.ViewUtil;

/**
 * Created by prasi on 30/5/16.
 */
public class IntroPagerAdapter extends PagerAdapter
{
    private static IntroPagerAdapter instance;
    private CoreActivity coreActivity;

    public static IntroPagerAdapter getInstance(CoreActivity coreActivity)
    {
        if(instance == null)
        {
            instance = new IntroPagerAdapter(coreActivity);
        }

        return instance;
    }

    private IntroPagerAdapter(CoreActivity coreActivity)
    {
        this.coreActivity = coreActivity;
    }

    @Override
    public int getCount()
    {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position)
    {
        View v = coreActivity.getLayoutInflater().inflate(R.layout.item_intro, collection,false);

        ImageView imageView = (ImageView) v.findViewById(R.id.intro_image);

        ViewUtil.renderImage(imageView, "http://image005.flaticon.com/139/svg/131/131638.svg", false);

        collection.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view)
    {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==(View)object;
    }

    @Override
    public float getPageWidth(int position)
    {
        /*if (position == 0 || position == getCount())
        {
            return 0.95f;
        }
        return 1f;*/

        /*if(position == 0) {
            return 0.90f;
        }else if(position==(getCount()-1)){
            return 0.90f;
        }
        return 1f;*/

        return super.getPageWidth(position);
    }
}
