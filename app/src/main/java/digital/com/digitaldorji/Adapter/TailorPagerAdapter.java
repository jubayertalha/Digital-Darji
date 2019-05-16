package digital.com.digitaldorji.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import digital.com.digitaldorji.Fragment.Portfolio;
import digital.com.digitaldorji.Fragment.Sales;

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
