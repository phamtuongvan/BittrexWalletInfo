package PACore.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by KhoaVin on 2/15/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public String[] TabTitle;
    public FragmentPattern[] listFragment;
    public ViewPagerAdapter(FragmentManager fm, String[] tabTitle, FragmentPattern[] ListFragment) {

        super(fm);
        TabTitle = tabTitle;
        listFragment = ListFragment;
    }
    @Override
    public Fragment getItem(int position) {
        /*if (listFragment.length>0)
        return listFragment[position];
        else
        return null;*/
        if (0 <= position && position < listFragment.length) {
            return listFragment[position];
        }else{
            return new Fragment();
        }
    }



    @Override
    public int getCount() {
        return listFragment.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String s = "";
        if(TabTitle.length>0)
            s = TabTitle[position];
        return s;
    }
}
