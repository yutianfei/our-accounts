package com.wangsy.ouraccounts.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wangsy.ouraccounts.model.IconsList;
import com.wangsy.ouraccounts.fragment.IconFragment;

/**
 * 为AddFragment的子viewpager提供adapter
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class IconFragmentPagerAdapter extends FragmentPagerAdapter {

    /**
     * 每页显示的icon数目
     */
    public static final int PER_PAGE_NUMBER = 15;

    public IconFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // 根据页面索引，生成对应的fragment数据
    @Override
    public Fragment getItem(int position) {
        IconFragment fragment = new IconFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    // 计算总共有多少页
    @Override
    public int getCount() {
        return IconsList.getIconsList().size() % PER_PAGE_NUMBER == 0 ?
                IconsList.getIconsList().size() / PER_PAGE_NUMBER :
                IconsList.getIconsList().size() / PER_PAGE_NUMBER + 1;
    }
}
