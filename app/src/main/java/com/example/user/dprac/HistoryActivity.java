package com.example.user.dprac;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.user.dprac.Adapters.PagerViewAdapter;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    PagerViewAdapter pagerViewAdapter;
    ViewPager viewPager;
    LinearLayout order_tab,return_tab,not_available_tab;
    TextView order_label,return_label,not_available_label;
    ImageView order_icon,return_icon,not_available_icon;
    Toolbar toolbar;
    TextView bar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        /**
         * Adding Toolbar
         */

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toolbar,null);
        actionBar.setCustomView(view);
        bar_title = (TextView)findViewById(R.id.bar_title);
        bar_title.setText("History");

        order_tab = (LinearLayout)findViewById(R.id.order_tab);
        return_tab = (LinearLayout)findViewById(R.id.return_tab);
        not_available_tab = (LinearLayout)findViewById(R.id.not_available_tab);

        order_label = (TextView)findViewById(R.id.order_label);
        return_label = (TextView)findViewById(R.id.return_label);
        not_available_label = (TextView)findViewById(R.id.not_available_label);

        order_icon = (ImageView)findViewById(R.id.order_icon);
        return_icon = (ImageView)findViewById(R.id.return_icon);
        not_available_icon = (ImageView)findViewById(R.id.not_available_icon);


        order_tab.setOnClickListener(this);
        return_tab.setOnClickListener(this);
        not_available_tab.setOnClickListener(this);

        viewPager = (ViewPager)findViewById(R.id.mainPager);
        viewPager.setOffscreenPageLimit(2);
        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
             ChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }


    public void ChangeTab(int position){
          if(position==0){
             order_icon.setImageResource(R.drawable.tab_orders_onpress_icon);
             order_label.setTextColor(getResources().getColor(R.color.active_icon_color));

              not_available_icon.setImageResource(R.drawable.tab_notavailabe_icon);
              not_available_label.setTextColor(getResources().getColor(R.color.primary_text_color));

             return_icon.setImageResource(R.drawable.tab_returns_icon);
             return_label.setTextColor(getResources().getColor(R.color.primary_text_color));


          }

          if(position==1){
              order_icon.setImageResource(R.drawable.tab_orders_icon);
              order_label.setTextColor(getResources().getColor(R.color.primary_text_color));

              not_available_icon.setImageResource(R.drawable.tab_notavailabe_onpress_icon);
              not_available_label.setTextColor(getResources().getColor(R.color.active_icon_color));

              return_icon.setImageResource(R.drawable.tab_returns_icon);
              return_label.setTextColor(getResources().getColor(R.color.primary_text_color));

          }

          if(position==2){
              order_icon.setImageResource(R.drawable.tab_orders_icon);
              order_label.setTextColor(getResources().getColor(R.color.primary_text_color));

              not_available_icon.setImageResource(R.drawable.tab_notavailabe_icon);
              not_available_label.setTextColor(getResources().getColor(R.color.primary_text_color));

              return_icon.setImageResource(R.drawable.tab_returns_onpress_icon);
              return_label.setTextColor(getResources().getColor(R.color.active_icon_color));

          }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_tab:
                viewPager.setCurrentItem(0);
                break;
            case R.id.not_available_tab:
                viewPager.setCurrentItem(1);
                break;

            case R.id.return_tab:
                viewPager.setCurrentItem(2);
                break;
        }
    }
}
