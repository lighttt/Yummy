package com.yummy.test.yummy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yummy.test.yummy.fragments.AboutFragment;
import com.yummy.test.yummy.fragments.GalleryFragment;
import com.yummy.test.yummy.fragments.HomeFragment;

/**
 * Created by User on 3/21/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int no_of_tabs;

    public ViewPagerAdapter(FragmentManager fm, int no_of_tabs) {
        super(fm);
        this.no_of_tabs = no_of_tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                AboutFragment aboutFragment = new AboutFragment();
                return aboutFragment;

            case 2:
                GalleryFragment galleryFragment = new GalleryFragment();
                return galleryFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return no_of_tabs;
    }
}
