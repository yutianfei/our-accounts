package com.wangsy.ouraccounts.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    private PullToRefreshSlideListView listViewAccounts;
    private List<AccountModel> data;
    MyCounter myCounter;
    AccountAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        listViewAccounts = (PullToRefreshSlideListView) view.findViewById(R.id.id_account_list);

        myCounter = new MyCounter(3 * 1000, 3 * 1000);
        data = DataSupport.order("datetime desc").find(AccountModel.class);
        adapter = new AccountAdapter(data, getActivity());
        listViewAccounts.setAdapter(adapter);

        // 设置滑动选项
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
                        // TODO
                        break;
                    case 1:
                        showDeleteDialog(position);
                        break;
                }
                return false;
            }
        });

        listViewAccounts.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                myCounter.start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                myCounter.start();
            }
        });

        return view;
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
                // 删除消息
                data.remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    /**
     * 倒计时
     */
    private class MyCounter extends CountDownTimer {

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            data = null;
            data = DataSupport.order("datetime desc").find(AccountModel.class);

            adapter = new AccountAdapter(data, getActivity());
            listViewAccounts.setAdapter(adapter);

            listViewAccounts.onRefreshComplete();
        }
    }

    // 查询数据
//    class QueryAccountsTask extends AsyncTask<>{
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            listViewAccounts.onRefreshComplete();
//            super.onPostExecute(o);
//        }
//    }
}
