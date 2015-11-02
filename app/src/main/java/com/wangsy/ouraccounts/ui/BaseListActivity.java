package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.AccountListAdapter;
import com.wangsy.ouraccounts.asynctask.QueryDataTask;
import com.wangsy.ouraccounts.callback.OnQueryDataReceived;
import com.wangsy.ouraccounts.model.AccountModel;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenu;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuCreator;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuItem;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuListView;
import com.wangsy.ouraccounts.utils.Util;
import com.wangsy.ouraccounts.view.PullToRefreshSlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 列表基本activity
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class BaseListActivity extends Activity implements OnQueryDataReceived {

    /**
     * 数据是否进行了修改
     */
    public static boolean isDataModified = true;

    private PullToRefreshSlideListView listViewAccounts;
    private List<AccountModel> accountsList;
    private AccountListAdapter accountListAdapter;

    private LinearLayout emptyTipLayout;

    /**
     * 分页索引
     */
    private int page = 0;

    /**
     * 类型
     */
    protected String strType = "";

    /**
     * 开始时间
     */
    protected String strStartDatetime = "";

    /**
     * 结束时间
     */
    protected String strEndDatetime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_report_list);

        emptyTipLayout = (LinearLayout) findViewById(R.id.id_empty_tip);

        listViewAccounts = (PullToRefreshSlideListView) findViewById(R.id.id_account_list);
        accountsList = new ArrayList<>();
        accountListAdapter = new AccountListAdapter(accountsList, this);
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

        // 显示左侧按钮：返回
        initButtonLeft();
    }

    private void initButtonLeft() {
        ImageButton imgBtnLeft = (ImageButton) findViewById(R.id.id_title_left_btn);
        imgBtnLeft.setVisibility(View.VISIBLE);
        imgBtnLeft.setImageResource(R.mipmap.icon_back);
        imgBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    /**
     * 异步获取数据
     */
    private void queryData() {
        Map<String, Object> params = new HashMap<>();
        params.put(QueryDataTask.PAGE, page);
        params.put(QueryDataTask.TYPE, strType);
        params.put(QueryDataTask.START_DATETIME, strStartDatetime);
        params.put(QueryDataTask.END_DATETIME, strEndDatetime);
        new QueryDataTask(this, this).execute(params);
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

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 创建按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(BaseListActivity.this);
                // 设置按钮背景
                deleteItem.setBackground(R.drawable.item_delete);
                // 设置按钮宽高
                deleteItem.setWidth(Util.dp2px(BaseListActivity.this, 80));
                deleteItem.setHeight(Util.dp2px(BaseListActivity.this, 80));
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
        isDataModified = false;
        AccountModel account = accountsList.get(position);
        Intent intent = new Intent(BaseListActivity.this, EditAccountActivity.class);
        intent.putExtra(EditAccountActivity.EXTRA_EDIT_DATA, account);
        intent.putExtra(EditAccountActivity.EXTRA_EDIT_DATA_ID, accountsList.get(position).getId());
        startActivity(intent);
    }

    /**
     * 删除提示
     */
    private void showDeleteDialog(final int position) {
        final Dialog dialog = new Dialog(this, R.style.style_dialog_common);
        View view = View.inflate(this, R.layout.dialog_common, null);
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
                // 通知数据更新
                sendBroadcastToRefreshData();
                queryData();

                Toast.makeText(BaseListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    /**
     * 通知数据更新
     */
    private void sendBroadcastToRefreshData() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.REFRESH_DATA_BROADCAST_INTENT_FILTER);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDataModified) {// 获取第一页数据
            queryData();
        }
    }
}
