package com.wangsy.ouraccounts.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.AccountAdapter;
import com.wangsy.ouraccounts.model.AccountModel;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenu;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuCreator;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuItem;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuListView;
import com.wangsy.ouraccounts.ui.EditAccountActivity;
import com.wangsy.ouraccounts.utils.Util;
import com.wangsy.ouraccounts.view.PullToRefreshSlideListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 账目记录列表
 * <p/>
 * Created by wangsy on 15/10/26.
 */
public class ReportListFragment extends Fragment {

    public static final String REFRESH_DATA_BROADCAST_INTENT_FILTER = "com.refreshData";
    public static final int REQUEST_EDIT_DATA = 1;

    private PullToRefreshSlideListView listViewAccounts;
    private List<AccountModel> accountsList;
    private AccountAdapter accountAdapter;

    /**
     * 分页索引
     */
    private int page = 0;

    /**
     * 每页显示数据的数目
     */
    private static final int PER_PAGE_COUNT = 10;

    /**
     * 修改的数据索引
     */
    private int editIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        listViewAccounts = (PullToRefreshSlideListView) view.findViewById(R.id.id_account_list);
        accountsList = new ArrayList<>();
        accountAdapter = new AccountAdapter(accountsList, getActivity());
        listViewAccounts.setAdapter(accountAdapter);

        // 设置滑动选项
        createSwipeMenu();

        // 绑定下拉刷新上拉加载事件
        bindPullToRefresh();

        // 初始获取第一页数据
        new QueryAccountsTask().execute(page);

        return view;
    }

    private void bindPullToRefresh() {
        listViewAccounts.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
            // 刷新数据
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                page = 0;
                new QueryAccountsTask().execute(page);
            }

            // 加载下一页
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                page++;
                new QueryAccountsTask().execute(page);
            }
        });
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 创建按钮
                SwipeMenuItem editItem = new SwipeMenuItem(getActivity().getApplicationContext());
                // 设置按钮背景
                editItem.setBackground(R.drawable.item_edit);
                // 设置按钮宽高
                editItem.setWidth(Util.dp2px(getActivity(), 80));
                editItem.setHeight(Util.dp2px(getActivity(), 80));
                // 给按钮添加图片
                editItem.setIcon(R.mipmap.icon_edit);
                // 添加进按钮
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(R.drawable.item_delete);
                deleteItem.setWidth(Util.dp2px(getActivity(), 80));
                deleteItem.setHeight(Util.dp2px(getActivity(), 80));
                deleteItem.setIcon(R.mipmap.icon_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        listViewAccounts.getRefreshableView().setMenuCreator(creator);
        listViewAccounts.getRefreshableView().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // 前往编辑页面
                        gotoEditActivity(position);
                        break;
                    case 1:
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
        editIndex = position;
        AccountModel account = accountsList.get(position);
        Intent intent = new Intent(getActivity(), EditAccountActivity.class);
        intent.putExtra(EditAccountActivity.EXTRA_EDIT_DATA, account);
        getParentFragment().startActivityForResult(intent, REQUEST_EDIT_DATA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_EDIT_DATA) {
            AccountModel newAccount = (AccountModel) data.getSerializableExtra(EditAccountActivity.EXTRA_EDIT_DATA);
            refreshData(newAccount);
        }
    }

    /**
     * 更新数据
     */
    private void refreshData(AccountModel newAccount) {
        // 更新数据库数据
        newAccount.update(accountsList.get(editIndex).getId());
        // 更新显示数据
        accountsList.get(editIndex).setOut(newAccount.isOut());
        accountsList.get(editIndex).setIconToShow(newAccount.getIconToShow());
        accountsList.get(editIndex).setDatetime(newAccount.getDatetime());
        accountsList.get(editIndex).setComment(newAccount.getComment());
        accountsList.get(editIndex).setAmount(newAccount.getAmount());
        accountsList.get(editIndex).setType(newAccount.getType());
        accountAdapter.notifyDataSetChanged();
    }

    /**
     * 删除提示
     */
    private void showDeleteDialog(final int position) {
        final Dialog dialog = new Dialog(getActivity(), R.style.style_dialog_common);
        View view = View.inflate(getActivity(), R.layout.dialog_common, null);
        Button btnCancel = (Button) view.findViewById(R.id.id_button_cancel);
        Button btnOk = (Button) view.findViewById(R.id.id_button_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从数据库中删除
                accountsList.get(position).delete();
                // 从显示的列表中移除
                accountsList.remove(position);
                accountAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    /**
     * 异步查询数据
     */
    private class QueryAccountsTask extends AsyncTask<Integer, Void, List<AccountModel>> {

        @Override
        protected List<AccountModel> doInBackground(Integer... page) {
            return DataSupport.order("datetime desc")
                    .limit(PER_PAGE_COUNT)
                    .offset(PER_PAGE_COUNT * page[0])
                    .find(AccountModel.class);
        }

        @Override
        protected void onPostExecute(List<AccountModel> result) {
            if (page == 0) {
                accountsList.clear();
            }
            accountsList.addAll(result);
            accountAdapter.notifyDataSetChanged();
            listViewAccounts.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    /**
     * 接收刷新数据的广播
     */
    private class RefreshDataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            page = 0;
            new QueryAccountsTask().execute(page);
        }
    }

    private RefreshDataBroadcastReceiver refreshDataBroadcastReceiver;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 注册广播
        refreshDataBroadcastReceiver = new RefreshDataBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REFRESH_DATA_BROADCAST_INTENT_FILTER);
        activity.registerReceiver(refreshDataBroadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshDataBroadcastReceiver);
    }
}
