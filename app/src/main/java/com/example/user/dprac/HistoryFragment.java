package com.example.user.dprac;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.dprac.Adapters.PagerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener{

    PagerViewAdapter pagerViewAdapter;
    ViewPager viewPager;
    LinearLayout order_tab,return_tab,not_available_tab;
    TextView order_label,return_label,not_available_label;
    ImageView order_icon,return_icon,not_available_icon;
    Toolbar toolbar;
    TextView bar_title;
    private Fragment[] fragments = {new OrderFragment(),new NotAvailableFragment(),new ReturnFragment()};
    Dialog dialog;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history, container, false);

        Constants.inner_fragment_position = 1;

        ((MainActivity)getActivity()).bar_title.setText("History");
        order_tab = (LinearLayout)view.findViewById(R.id.order_tab);
        return_tab = (LinearLayout)view.findViewById(R.id.return_tab);
        not_available_tab = (LinearLayout)view.findViewById(R.id.not_available_tab);
//
        order_label = (TextView)view.findViewById(R.id.order_label);
        return_label = (TextView)view.findViewById(R.id.return_label);
        not_available_label = (TextView)view.findViewById(R.id.not_available_label);

        order_icon = (ImageView)view.findViewById(R.id.order_icon);
        return_icon = (ImageView)view.findViewById(R.id.return_icon);
        not_available_icon = (ImageView)view.findViewById(R.id.not_available_icon);

        order_tab.setOnClickListener(this);
        return_tab.setOnClickListener(this);
        not_available_tab.setOnClickListener(this);

        viewPager = (ViewPager)view.findViewById(R.id.mainPager);
        viewPager.setOffscreenPageLimit(2);
      //  setUpViewPager();
        dialog = Helper.showLoadingBar(getContext());
        dialog.show();

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       new MyTask().execute();

                    }
                });

            }
        };
        thread.start();


        return view;
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

    class MyTask extends AsyncTask<Void, Fragment, String> {


        @Override
        protected void onPreExecute() {
            pagerViewAdapter = new PagerViewAdapter(getChildFragmentManager());
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

        @Override
        protected String doInBackground(Void... voids) {
            for(Fragment fragment : fragments){
                publishProgress(fragment);
            }

            return "success";
        }

        @Override
        protected void onProgressUpdate(Fragment... values) {
            pagerViewAdapter.addFragment(values[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            viewPager.setAdapter(pagerViewAdapter);

        }
    }

}
