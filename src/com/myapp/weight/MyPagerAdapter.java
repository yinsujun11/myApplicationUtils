package com.myapp.weight;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyPagerAdapter extends AbstractViewPagerAdapter<Integer>  
{  
    public MyPagerAdapter(List<Integer> datas,Context context)  
    {  
        super(datas,context);  
    }  
          
    @Override  
    public View newView(int position)  
    {  
        ImageView img = new ImageView(mContext);  
          
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,80);  
        img.setLayoutParams(params);  
          
        img.setImageResource(mData.get(position));  
        return img;  
    }  
}  
