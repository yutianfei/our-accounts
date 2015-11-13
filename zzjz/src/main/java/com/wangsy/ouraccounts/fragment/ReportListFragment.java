package com.wangsy.ouraccounts.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.AccountListAdapter;
import com.wangsy.ouraccounts.asynctask.QueryDataTask;
import com.wangsy.ouraccounts.callback.CommonDialogEvent;
import com.wangsy.ouraccounts.callback.OnQueryDataReceived;
import com.wangsy.ouraccounts.model.AccountModel;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenu;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuCreator;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuItem;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuListView;
import com.wangsy.ouraccounts.ui.EditAccountActivity;
import com.wangsy.ouraccounts.ui.MainActivity;
import com.wangsy.ouraccounts.ui.SearchConditionActivity;
import com.wangsy.ouraccounts.utils.Utils;
import com.wangsy.ouraccounts.view.PullToRefreshSlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账目记录列表
 * <p/>
 * Created by wangsy on 15/10/26.
 */
public class ReportListFragment extends Fragment implements OnQueryDataReceived {

    private PullToRefreshSlideListView listViewAccounts;
    private List<AccountModel> accountsList;
    private AccountListAdapter accountListAdapter;

    private LinearLayout emptyTipLayout;

    /**
     * 分页索引
     */
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.tab_report_list);

        // 显示右侧按钮：搜索
        initButtonRight(view);

        emptyTipLayout = (LinearLayout) view.findViewById(R.id.id_empty_tip);

        listViewAccounts = (PullToRefreshSlideListView) view.findViewById(R.id.id_account_list);
        accountsList = new ArrayList<>();
        accountListAdapter = new AccountListAdapter(accountsList, getActivity());
        listViewAccounts.setAdapter(accountListAdapter);
        listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 前往编辑页面
                gotoEditActivity(position - 1);
            }
        });

        // 设置滑动选项
        createSwipeMenu();

        // 绑定下拉刷新上拉加载事件
        bindPullToRefresh();

        // 初始获取第一页数据
        queryData();

        return view;
    }

    private void initButtonRight(View view) {
        ImageButton imgBtnRight = (ImageButton) view.findViewById(R.id.id_title_right_btn);
        imgBtnRight.setVisibility(View.VISIBLE);
        imgBtnRight.setImageResource(R.mipmap.icon_search);
        imgBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchConditionActivity.class);
                intent.putExtra(SearchConditionActivity.EXTRA_SEARCH_FLAG, SearchConditionActivity.SEARCH_FLAG_ALL);
                startActivity(intent);
            }
        });
    }

    private void bindPullToRefresh() {
        listViewAccounts.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
            // 刷新数据
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                page = 0;
                queryData();
            }

            // 加载下一页
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                page++;
                queryData();
            }
        });
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 创建按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                // 设置按钮背景
                deleteItem.setBackground(R.drawable.item_delete);
                // 设置按钮宽高
                deleteItem.setWidth(Utils.dp2px(getActivity(), 80));
                deleteItem.setHeight(Utils.dp2px(getActivity(), 80));
                // 给按钮添加图片
                deleteItem.setIcon(R.mipmap.icon_delete);
                // 添加进按钮
                menu.addMenuItem(deleteItem);
            }
        };
        listViewAccounts.getRefreshableView().setMenuCreator(creator);
        listViewAccounts.getRefreshableView().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // 显示删除提示
                        showDeleteDialog(position);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 跳转到编辑信息的Activity
     */
    private void gotoEditActivity(int position) {
        AccountModel account = accountsList.get(position);
        Intent intent = new Intent(getActivity(), EditAccountActivity.class);
        intent.putExtra(EditAccountActivity.EXTRA_EDIT_DATA, account);
        intent.putExtra(EditAccountActivity.EXTRA_EDIT_DATA_ID, accountsList.get(position).getId());
        startActivity(intent);
    }

    /**
     * 删除提示
     */
    private void showDeleteDialog(final int position) {
        Utils.getCommonDialog(getActivity(), new CommonDialogEvent() {
            @Override
            public void onButtonOkClick() {
                // 从数据库中删除
                accountsList.get(position).delete();
                // 从显示的列表中移除
                accountsList.remove(position);
                // 通知数据更新
                sendBroadcastToRefreshData();

                Toast.makeText(getActivity(), R.string.tip_delete_ok, Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    /**
     * 异步获取数据
     */
    private void queryData() {
        Map<String, Object> params = new HashMap<>();
        params.put(QueryDataTask.PAGE, page);
        params.put(QueryDataTask.TYPE, "");
        params.put(QueryDataTask.START_DATETIME, "");
        params.put(QueryDataTask.END_DATETIME, "");
        new QueryDataTask(getActivity(), this).execute(params);
    }

    @Override
    public void onQueryDataReceived(Map<String, Object> result) {
        int totalPages = (int) result.get(QueryDataTask.TOTAL_PAGES);
        List<AccountModel> resultList = (List<AccountModel>) result.get(QueryDataTask.RESULT_LIST);

        if (null == resultList || 0 == resultList.size()) {
            emptyTipLayout.setVisibility(View.VISIBLE);
            listViewAccounts.setVisibility(View.GONE);
        } else {
            emptyTipLayout.setVisibility(View.GONE);
            listViewAccounts.setVisibility(View.VISIBLE);

            if (page == 0) {
                accountsList.clear();
            }
            accountsList.addAll(resultList);
            accountListAdapter.notifyDataSetChanged();
            listViewAccounts.onRefreshComplete();

            if (page == totalPages - 1) { // 已经是最后一页，取消加载更多
                listViewAccounts.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            } else {
                listViewAccounts.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }

    /**
     * 通知数据更新
     */
    private void sendBroadcastToRefreshData() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.REFRESH_DATA_BROADCAST_INTENT_FILTER);
        getActivity().sendBroadcast(intent);
    }

    /**
     * 接收刷新数据的广播
     */
    private class RefreshDataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            page = 0;
            queryData();
        }
    }

    private RefreshDataBroadcastReceiver refreshDataBroadcastReceiver;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 注册广播
        refreshDataBroadcastReceiver = new RefreshDataBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.REFRESH_DATA_BROADCAST_INTENT_FILTER);
        activity.registerReceiver(refreshDataBroadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshDataBroadcastReceiver);
    }
}
