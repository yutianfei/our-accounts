////package com.wangsy.ouraccounts.ui;
////
////import android.support.v4.app.FragmentActivity;
////import android.support.v4.view.ViewPager;
////import android.os.Bundle;
////import android.view.View;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.ImageButton;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import com.viewpagerindicator.CirclePageIndicator;
////import com.wangsy.ouraccounts.R;
////import com.wangsy.ouraccounts.adapter.IconPageAdapter;
////import com.wangsy.ouraccounts.callback.IconSelectedCallback;
////import com.wangsy.ouraccounts.model.AccountModel;
////
////import java.util.Date;
////
////public class MainActivity extends FragmentActivity implements IconSelectedCallback {
////
//
//
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
//import com.viewpagerindicator.IconPagerAdapter;
//import com.wangsy.ouraccounts.fragment.AddFragment;
//import com.wangsy.ouraccounts.fragment.ReportFragment;
//import com.wangsy.ouraccounts.fragment.SettingFragment;
//
//private static final String[] TABS = {
//        "新的", "统计", "设置"
//        };
//
//private static final int[] ICONS = new int[]{
//        R.drawable.tab_btn_add,
//        R.drawable.tab_btn_report,
//        R.drawable.tab_btn_setting
//        };
//
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        FragmentPagerAdapter adapter = new AccountPagerAdapter(getSupportFragmentManager());
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.id_main_pager);
//        viewPager.setAdapter(adapter);
//
//        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.id_main_indicator);
//        indicator.setViewPager(viewPager);
//        }
//
///**
// * 提供不同的fragment
// */
//class AccountPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
//
//    public AccountPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public int getIconResId(int index) {
//        return ICONS[index];
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return TABS[position];
//    }
//
//    @Override
//    public int getCount() {
//        return ICONS.length;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        switch (position) {
//            case 0:
//                fragment = new AddFragment();
//                break;
//            case 1:
//                fragment = new ReportFragment();
//                break;
//            case 2:
//                fragment = new SettingFragment();
//                break;
//        }
//        return fragment;
//    }
//}