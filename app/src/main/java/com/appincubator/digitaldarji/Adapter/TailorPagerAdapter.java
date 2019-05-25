package com.appincubator.digitaldarji.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appincubator.digitaldarji.Fragment.Portfolio;
import com.appincubator.digitaldarji.Fragment.Sales;

public class TailorPagerAdapter extends FragmentPagerAdapter {
    public TailorPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i==0){
            return new Sales();
        }else {
            return new Portfolio();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "Sales";
        }else {
            return "Portfolio";
        }
    }
}
