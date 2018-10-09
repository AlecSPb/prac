package com.example.user.dprac.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.dprac.NotAvailableFragment;
import com.example.user.dprac.OrderFragment;
import com.example.user.dprac.ReturnFragment;

import java.util.ArrayList;
import java.util.List;


public class PagerViewAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }


    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }


}
