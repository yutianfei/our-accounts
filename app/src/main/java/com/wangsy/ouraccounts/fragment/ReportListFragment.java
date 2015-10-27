package com.wangsy.ouraccounts.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenu;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuCreator;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuItem;
import com.wangsy.ouraccounts.swipeMenuListView.SwipeMenuListView;
import com.wangsy.ouraccounts.view.PullToRefreshSlideListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 账目记录列表
 * <p/>
 * Created by wangsy on 15/10/26.
 */
public class ReportListFragment extends Fragment {

    private PullToRefreshSlideListView lvListHaveAnim;
    private List<String> data;
    MyCounter myCounter;
    AccountAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_list, null);

        lvListHaveAnim = (PullToRefreshSlideListView) view.findViewById(R.id.lvListHaveAnim);

        myCounter = new MyCounter(3 * 1000, 3 * 1000);
        data = new ArrayList<>();
        data.add("start");
        data.add("aa");
        data.add("bb");
        data.add("cc");
        data.add("dd");
        data.add("ee");
        data.add("ff");
        data.add("gg");
        data.add("hh");
        data.add("ii");
        data.add("jj");
        data.add("kk");
        data.add("lll");
        data.add("mm");
        data.add("nn");
        data.add("oo");
        data.add("pp");
        data.add("qq");
        data.add("rr");
        data.add("ss");
        data.add("tt");
        data.add("uu");
        data.add("vv");
        data.add("ww");
        data.add("ss");
        data.add("yy");
        data.add("zz");
        data.add("end");
        adapter = new AccountAdapter(data, getActivity());

        lvListHaveAnim.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 创建按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                // 设置按钮背景
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // 设置按钮宽度
                deleteItem.setWidth(dp2px(getActivity(), 90));
                deleteItem.setHeight(dp2px(getActivity(), 100));
                // 给按钮添加图片
                deleteItem.setIcon(R.mipmap.normal_add);
                // 添加进按钮
                menu.addMenuItem(deleteItem);

                // 创建按钮
                SwipeMenuItem item2 = new SwipeMenuItem(getActivity().getApplicationContext());
                // 设置按钮背景
                item2.setBackground(R.color.color_text_selected);
                // 设置按钮宽度
                item2.setWidth(dp2px(getActivity(), 90));
                item2.setHeight(dp2px(getActivity(), 100));
                // 给按钮添加图片
                item2.setIcon(R.mipmap.normal_add);
                // 添加进按钮
                menu.addMenuItem(item2);
            }
        };

        lvListHaveAnim.getRefreshableView().setMenuCreator(creator);
        lvListHaveAnim.getRefreshableView().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        showDeleteDialog(position);
                        break;
                }
                return false;
            }
        });

        lvListHaveAnim.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
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

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
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
            lvListHaveAnim.onRefreshComplete();
        }
    }
}
