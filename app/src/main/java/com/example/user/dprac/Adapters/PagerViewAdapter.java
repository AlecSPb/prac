package com.example.user.dprac.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.dprac.NotAvailableFragment;
import com.example.user.dprac.OrderFragment;
import com.example.user.dprac.ReturnFragment;

public class PagerViewAdapter extends FragmentPagerAdapter {
        public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
     switch (position){
         case 0:
             OrderFragment orderFragment = new OrderFragment();
             return orderFragment;

         case 1:
             NotAvailableFragment notAvailableFragment = new NotAvailableFragment();
             return notAvailableFragment;


         case 2:
             ReturnFragment retrunFragment = new ReturnFragment();
             return retrunFragment;

             default:
                 return null;
     }
    }

    @Override
    public int getCount() {
        return 3;
    }


}
