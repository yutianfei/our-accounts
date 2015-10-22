package com.wangsy.ouraccounts.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.IconPageAdapter;
import com.wangsy.ouraccounts.adapter.IconGridAdapter;
import com.wangsy.ouraccounts.callback.IconSelectedCallback;
import com.wangsy.ouraccounts.model.TypeIconModel;
import com.wangsy.ouraccounts.model.TypeIconsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangsy on 15/10/21.
 */
public class IconFragment extends Fragment {

    private GridView gridViewIcons;
    private IconGridAdapter adapter;
    private List<TypeIconModel> iconsList;
    private int position;

    private IconSelectedCallback iconSelectedCallback;

    public IconFragment() {
        super();
    }

    // 当该Fragment被添加,显示到Activity时调用该方法
    // 在此判断显示到的Activity是否已经实现了接口
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IconSelectedCallback) {
            iconSelectedCallback = (IconSelectedCallback) activity;
        }
    }

    //  当该Fragment从它所属的Activity中被删除时调用该方法
    @Override
    public void onDetach() {
        super.onDetach();
        iconSelectedCallback = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.icons_page_fragment, null);

        position = getArguments().getInt("position");
        gridViewIcons = (GridView) view.findViewById(R.id.icon_grid_view);

        iconsList = getIconsList();
        adapter = new IconGridAdapter(iconsList, getActivity());

        gridViewIcons.setAdapter(adapter);
        bindOnIconClickListener();

        return view;
    }

    private void bindOnIconClickListener() {
        gridViewIcons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TypeIconModel iconModel = iconsList.get(position);

                for (int i = 0; i < iconsList.size(); i++) {
                    if (i != position) {
                        iconsList.get(i).iconNameColor = R.color.color_icon_normal;
                        iconsList.get(i).iconImageToShow = iconsList.get(i).normalIcon;
                    }
                }
                iconModel.iconNameColor = R.color.color_icon_selected;
                iconModel.iconImageToShow = iconsList.get(position).selectedIcon;

                // 将需要的数据通过回调方法返回给activity
                iconSelectedCallback.onIconSelected(iconModel.isOut, iconModel.type);

                adapter.notifyDataSetChanged();
            }
        });
    }

    private List<TypeIconModel> getIconsList() {
        List<TypeIconModel> original = TypeIconsList.getIconsList();
        List<TypeIconModel> list = new ArrayList<>();

        int pagesNumber = original.size() % IconPageAdapter.PER_PAGE_NUMBER == 0 ?
                original.size() / IconPageAdapter.PER_PAGE_NUMBER :
                original.size() / IconPageAdapter.PER_PAGE_NUMBER + 1;
        int startIndex = IconPageAdapter.PER_PAGE_NUMBER * (position);
        int endIndex = position == pagesNumber - 1 ?
                startIndex + original.size() % IconPageAdapter.PER_PAGE_NUMBER - 1 :
                startIndex + IconPageAdapter.PER_PAGE_NUMBER - 1;

        for (int i = startIndex; i <= endIndex; i++) {
            TypeIconModel icon = original.get(i);
            list.add(icon);
        }
        return list;
    }
}
