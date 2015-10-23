package com.wangsy.ouraccounts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    /**
     * 从IconPageAdapter传递过来的要选择的icon所在的页面索引，
     * 用于获取当前fragment页面要显示的数据列表
     */
    private int page;

    /**
     * 用于将选择的icon数据传递给父fragment
     */
    private IconSelectedCallback iconSelectedCallback;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.icons_page_fragment, null);

        page = getArguments().getInt("page");
        gridViewIcons = (GridView) view.findViewById(R.id.icon_grid_view);

        iconsList = getIconsList();
        adapter = new IconGridViewAdapter(iconsList, getActivity());

        gridViewIcons.setAdapter(adapter);
        bindOnIconClickListener();

        return view;
    }

    /**
     * 选中icon后要进行的工作
     */
    private void bindOnIconClickListener() {
        gridViewIcons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                IconModel iconModel = iconsList.get(position);

                for (int i = 0; i < iconsList.size(); i++) {
                    if (i != position) {
                        iconsList.get(i).iconNameColor = R.color.color_icon_normal;
                        iconsList.get(i).iconImageToShow = iconsList.get(i).normalIcon;
                    }
                }
                iconModel.iconNameColor = R.color.color_icon_selected;
                iconModel.iconImageToShow = iconsList.get(position).selectedIcon;

                // 将需要的数据通过回调方法返回给父fragment
                if (getParentFragment() != null && getParentFragment() instanceof IconSelectedCallback) {
                    iconSelectedCallback = (IconSelectedCallback) getParentFragment();
                    iconSelectedCallback.onIconSelected(iconModel.isOut, iconModel.type);
                }

                // 更新页面显示
                adapter.notifyDataSetChanged();
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

    // 当该Fragment从它所属的Activity中被删除时调用该方法
    @Override
    public void onDetach() {
        super.onDetach();
        iconSelectedCallback = null;
    }
}
