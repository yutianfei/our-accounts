package com.wangsy.ouraccounts.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.IconGridViewAdapter;
import com.wangsy.ouraccounts.adapter.IconFragmentPagerAdapter;
import com.wangsy.ouraccounts.callback.IconSelectedCallback;
import com.wangsy.ouraccounts.model.IconModel;
import com.wangsy.ouraccounts.model.IconsList;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一页要显示的icon
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class IconFragment extends Fragment {

    private GridView gridViewIcons;
    private IconGridViewAdapter adapter;
    private List<IconModel> iconsList;

    private IconSelectedBroadcastReceiver iconSelectedBroadcastReceiver;

    /**
     * 从IconPageAdapter传递过来的要选择的icon所在的页面索引，
     * 用于获取当前fragment页面要显示的数据列表
     */
    private int page;

    /**
     * 用于将选择的icon数据传递给父fragment
     */
    private IconSelectedCallback iconSelectedCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.icons_page_fragment, container, false);

        iconSelectedCallback = (IconSelectedCallback) getParentFragment();
        gridViewIcons = (GridView) view.findViewById(R.id.icon_grid_view);

        page = getArguments().getInt("page");
        iconsList = getIconsList();
        adapter = new IconGridViewAdapter(iconsList, getActivity());
        gridViewIcons.setAdapter(adapter);

        // 选择icon后的处理工作
        bindOnIconClickListener();

        // 默认选择第一页第一个icon
        setDefaultSelectedIcon();

        return view;
    }

    private void setDefaultSelectedIcon() {
        setSelectIcon(0, 0);
        adapter.notifyDataSetChanged();
    }

    /**
     * 选中icon后要进行的工作
     */
    private void bindOnIconClickListener() {
        gridViewIcons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 发送广播，通知修改图标状态
                sendBroadcastToChangeIconsStates(page, position);
            }
        });
    }

    /**
     * 计算当前页面要显示的第一个及最后一个icon的位置，生成相应的列表数据
     */
    private List<IconModel> getIconsList() {
        List<IconModel> original = IconsList.getIconsList();
        List<IconModel> list = new ArrayList<>();

        int pagesNumber = original.size() % IconFragmentPagerAdapter.PER_PAGE_NUMBER == 0 ?
                original.size() / IconFragmentPagerAdapter.PER_PAGE_NUMBER :
                original.size() / IconFragmentPagerAdapter.PER_PAGE_NUMBER + 1;
        int startIndex = IconFragmentPagerAdapter.PER_PAGE_NUMBER * (page);
        int endIndex = page == pagesNumber - 1 ?
                startIndex + original.size() % IconFragmentPagerAdapter.PER_PAGE_NUMBER - 1 :
                startIndex + IconFragmentPagerAdapter.PER_PAGE_NUMBER - 1;

        for (int i = startIndex; i <= endIndex; i++) {
            IconModel icon = original.get(i);
            list.add(icon);
        }
        return list;
    }

    // 当该Fragment从它所属的Activity中被删除时调用
    @Override
    public void onDetach() {
        super.onDetach();
        iconSelectedCallback = null;
    }

    private static final String ICON_SELECT_BROADCAST_INTENT_FILTER = "com.iconSelected";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 注册广播
        iconSelectedBroadcastReceiver = new IconSelectedBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ICON_SELECT_BROADCAST_INTENT_FILTER);
        activity.registerReceiver(iconSelectedBroadcastReceiver, filter);
    }

    /**
     * 根据选择的图标位置，设置图标的选中状态
     * <p/>
     * 传递父fragment所需的数据
     */
    private void setSelectIcon(int currentPage, int currentPosition) {
        if (currentPage == page) {
            IconModel iconModel = iconsList.get(currentPosition);
            iconModel.iconNameColor = R.color.color_icon_selected;
            iconModel.iconImageToShow = iconModel.selectedIcon;

            // 将需要的数据通过回调，返回给父fragment
            iconSelectedCallback.onIconSelected(iconModel.isOut, iconModel.type, iconModel.iconImageToShow);
        }
    }

    /**
     * 清除图标状态
     */
    private void resetIconStates() {
        for (int i = 0; i < iconsList.size(); i++) {
            iconsList.get(i).iconNameColor = R.color.color_icon_normal;
            iconsList.get(i).iconImageToShow = iconsList.get(i).normalIcon;
        }
    }

    /**
     * 发送广播，将当前选择的图标所在位置通知给接收者，使其根据这些信息进行图标状态的切换
     */
    private void sendBroadcastToChangeIconsStates(int page, int position) {
        Intent intent = new Intent();
        intent.setAction(ICON_SELECT_BROADCAST_INTENT_FILTER);
        intent.putExtra("select_page", page);
        intent.putExtra("select_position", position);
        getActivity().sendBroadcast(intent);
    }

    /**
     * 接收切换图标状态的广播
     */
    private class IconSelectedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int currentPage = intent.getExtras().getInt("select_page");
            int currentPosition = intent.getExtras().getInt("select_position");

            resetIconStates();
            setSelectIcon(currentPage, currentPosition);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(iconSelectedBroadcastReceiver);
    }
}
