package com.wangsy.ouraccounts.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wangsy.ouraccounts.model.TypeIconsList;
import com.wangsy.ouraccounts.fragment.IconFragment;

/**
 * Created by wangsy on 15/10/21.
 */
public class IconPageAdapter extends FragmentPagerAdapter {

    public static final int PER_PAGE_NUMBER = 10;

    public IconPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        IconFragment fragment = new IconFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return TypeIconsList.getIconsList().size() % PER_PAGE_NUMBER == 0 ?
                TypeIconsList.getIconsList().size() / PER_PAGE_NUMBER :
                TypeIconsList.getIconsList().size() / PER_PAGE_NUMBER + 1;
    }
}
