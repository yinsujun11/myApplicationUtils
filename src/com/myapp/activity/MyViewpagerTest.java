package com.myapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.myapp.weight.MyPagerAdapter;
import com.myapplicationdemo.R;

public class MyViewpagerTest extends Activity  
{  
    ViewPager viewPager;  
  
    ImageView[] tips;  
    private RatingBar ratingbar;
  
    @Override  
    protected void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.viewpager_test);  
  
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);  
          
        viewPager = (ViewPager) findViewById(R.id.viewPager);  
        ratingbar=(RatingBar) findViewById(R.id.viewpager_rating);
        List<Integer> resIdLst = new ArrayList<Integer>();  
        resIdLst.add(R.drawable.img_url_for_user_login);  
        resIdLst.add(R.drawable.index_redpacket_for_friends);  
//      resIdLst.add(R.drawable.item03);  
//      resIdLst.add(R.drawable.item04);  
//      resIdLst.add(R.drawable.item05);  
//      resIdLst.add(R.drawable.item06);  
//      resIdLst.add(R.drawable.item07);  
//      resIdLst.add(R.drawable.item08);  
  
        int len = resIdLst.size();  
        tips = new ImageView[len];  
  
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);  
        params.leftMargin = 5;  
        params.rightMargin = 5;  
          
        for (int i = 0; i < len; i++)  
        {  
            ImageView img = new ImageView(this);  
            if (i == 0)  
            {  
                img.setBackgroundResource(R.drawable.circle_hecked);  
            }  
            else  
            {  
                img.setBackgroundResource(R.drawable.circle_unhecked);  
            }  
  
            img.setLayoutParams(params);  
            tips[i] = img;  
  
            group.addView(img);  
        }  
  
        viewPager.setAdapter(new MyPagerAdapter(resIdLst, this));  
  
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()  
        {  
            @Override  
            public void onPageSelected(int arg0)  
            {  
                // TODO Auto-generated method stub  
                setImageBackground(arg0 % tips.length);  
            }  
  
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2)  
            {  
                // TODO Auto-generated method stub  
            }  
  
            @Override  
            public void onPageScrollStateChanged(int arg0)  
            {  
                // TODO Auto-generated method stub  
  
            }  
        });  
  
        // 设置ViewPager的默认项, 设置为长度的100倍，这样开始就能往左滑动  
        viewPager.setCurrentItem((tips.length) * 100);//  
        
        ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				Log.e("星级评分", rating+"rating");
			}
		});
    }  
  
    private void setImageBackground(int selectItems)  
    {  
        int len = tips.length;  
        for (int i = 0; i < len; i++)  
        {  
            if (i == selectItems)  
            {  
                tips[i].setBackgroundResource(R.drawable.circle_hecked);  
            }  
            else  
            {  
                tips[i].setBackgroundResource(R.drawable.circle_unhecked);  
            }  
        }  
    }  
} 
