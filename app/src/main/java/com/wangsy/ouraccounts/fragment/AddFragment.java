package com.wangsy.ouraccounts.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.IconFragmentPagerAdapter;
import com.wangsy.ouraccounts.callback.IconSelectedCallback;
import com.wangsy.ouraccounts.model.AccountModel;
import com.wangsy.ouraccounts.ui.SetDatetimeDialogActivity;
import com.wangsy.ouraccounts.ui.SetCommentDialogActivity;
import com.wangsy.ouraccounts.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 新增
 * <p/>
 * Created by wangsy on 15/10/22.
 */
public class AddFragment extends Fragment implements IconSelectedCallback {
    public static final int REQUEST_SET_DATE = 1;
    public static final int REQUEST_SET_COMMENT = 2;

    private EditText etMoneyAmount;
    private StringBuilder sbMoneyAmount;

    private String accountType;
    private boolean accountIsOut;
    private String accountComment = "";
    private String accountDatetime = "";
    private int accountIconToShow;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.title_new);

        // 主界面显示右侧按钮：保存数据
        initButtonRight(view);

        // 初始化 viewpager
        initViewPager(view);

        // 初始化数字键盘
        initNumberButtons(view);

        // 初始化选择时间
        initSetDatetime(view);

        // 初始化设置备注
        initSetComment(view);

        etMoneyAmount = (EditText) view.findViewById(R.id.id_money_amount);
        sbMoneyAmount = new StringBuilder();

        return view;
    }

    private void initSetComment(View view) {
        TextView tvSetComment = (TextView) view.findViewById(R.id.id_set_comment);
        tvSetComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetCommentDialogActivity.class);
                intent.putExtra(SetCommentDialogActivity.EXTRA_COMMENT, accountComment);
                startActivityForResult(intent, REQUEST_SET_COMMENT);
            }
        });
    }

    private void initSetDatetime(View view) {
        TextView tvSetDatetime = (TextView) view.findViewById(R.id.id_choose_date);
        tvSetDatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetDatetimeDialogActivity.class);
                intent.putExtra(SetDatetimeDialogActivity.EXTRA_DATETIME, accountDatetime);
                startActivityForResult(intent, REQUEST_SET_DATE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SET_DATE:
                    accountDatetime = data.getStringExtra(SetDatetimeDialogActivity.EXTRA_DATETIME);
                    break;
                case REQUEST_SET_COMMENT:
                    accountComment = data.getStringExtra(SetCommentDialogActivity.EXTRA_COMMENT);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onIconSelected(boolean isOut, String type, int iconToShow) {
        accountIsOut = isOut;
        accountType = type;
        accountIconToShow = iconToShow;

        if (isOut) {
            etMoneyAmount.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            etMoneyAmount.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void saveAccountData() {
        // 没有金额，提示，不保存
        if (sbMoneyAmount.toString().isEmpty()) {
            Toast.makeText(getActivity(), "金额是否输入了呢？", Toast.LENGTH_LONG).show();
            return;
        }

        // 没有类型，提示，不保存
        if (accountType == null) {
            Toast.makeText(getActivity(), "消费类型是否选择了呢？", Toast.LENGTH_LONG).show();
            return;
        }

        // 输入多位金额，如果第一个数字是0，在保存数据的时候，自动将0去除
        if (sbMoneyAmount.length() > 1 &&
                sbMoneyAmount.charAt(0) == '0' &&
                sbMoneyAmount.charAt(1) != '.') {
            sbMoneyAmount.deleteCharAt(0);
        }

        // 0元提示，不保存
        if (Float.parseFloat(sbMoneyAmount.toString()) == 0) {
            Toast.makeText(getActivity(), "请确保金额输入正确哦！", Toast.LENGTH_LONG).show();
            return;
        }

        // 如果没有设置时间，默认为当前时间
        if (accountDatetime == null || "".equals(accountDatetime)) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(Util.DATE_FORMAT);
            accountDatetime = sdf.format(c.getTime());
        }

        // 设置要保存的数据
        AccountModel accountData = new AccountModel();
        accountData.setAmount(Float.parseFloat(sbMoneyAmount.toString()));
        accountData.setDatetime(accountDatetime);
        accountData.setIsOut(accountIsOut);
        accountData.setType(accountType);
        accountData.setComment(accountComment);
        accountData.setIconToShow(accountIconToShow);

        // 保存数据
        boolean saveFlag = accountData.save();
        if (saveFlag) {
            Toast.makeText(getActivity(), "保存成功！" + accountData.toString(), Toast.LENGTH_LONG).show();
            cleanMoneyAmount();
        }
    }

    /**
     * 清除已经输入的金额信息
     */
    private void cleanMoneyAmount() {
        sbMoneyAmount.delete(0, sbMoneyAmount.length());
        etMoneyAmount.setText(sbMoneyAmount.toString());
        accountComment = "";
        accountDatetime = "";
    }

    class NumberButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_number_0:
                    // 第一个数字是0时，不允许继续添加数字0
                    if (sbMoneyAmount.length() == 1 && sbMoneyAmount.indexOf("0") == 0) {
                        return;
                    } else {
                        sbMoneyAmount.append("0");
                    }
                    break;
                case R.id.btn_number_dot:
                    // 保证只有一个小数点
                    if (sbMoneyAmount.length() == 0) {
                        sbMoneyAmount.append("0.");
                    } else if (!(sbMoneyAmount.indexOf(".") > -1)) {
                        sbMoneyAmount.append('.');
                    }
                    break;
                case R.id.btn_number_del:
                    if (!sbMoneyAmount.toString().isEmpty()) {
                        sbMoneyAmount.deleteCharAt(sbMoneyAmount.length() - 1);
                    }
                    break;
                default:
                    Button btn = (Button) v;
                    sbMoneyAmount.append(btn.getText().toString());
                    break;
            }
            etMoneyAmount.setText(sbMoneyAmount.toString());
            etMoneyAmount.setSelection(sbMoneyAmount.length());
        }
    }

    private void initViewPager(View view) {
        IconFragmentPagerAdapter mAdapter = new IconFragmentPagerAdapter(getChildFragmentManager());
        CirclePageIndicator mIndicator = (CirclePageIndicator) view.findViewById(R.id.id_viewpager_indicator);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initButtonRight(View view) {
        ImageButton imgBtn = (ImageButton) view.findViewById(R.id.id_title_right_btn);
        imgBtn.setVisibility(View.VISIBLE);
        imgBtn.setImageResource(R.mipmap.icon_ok);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存数据
                saveAccountData();
            }
        });
    }

    private void initNumberButtons(View view) {
        NumberButtonClickListener clickListener = new NumberButtonClickListener();

        Button btnNum1 = (Button) view.findViewById(R.id.btn_number_1);
        btnNum1.setOnClickListener(clickListener);

        Button btnNum2 = (Button) view.findViewById(R.id.btn_number_2);
        btnNum2.setOnClickListener(clickListener);

        Button btnNum3 = (Button) view.findViewById(R.id.btn_number_3);
        btnNum3.setOnClickListener(clickListener);

        Button btnNum4 = (Button) view.findViewById(R.id.btn_number_4);
        btnNum4.setOnClickListener(clickListener);

        Button btnNum5 = (Button) view.findViewById(R.id.btn_number_5);
        btnNum5.setOnClickListener(clickListener);

        Button btnNum6 = (Button) view.findViewById(R.id.btn_number_6);
        btnNum6.setOnClickListener(clickListener);

        Button btnNum7 = (Button) view.findViewById(R.id.btn_number_7);
        btnNum7.setOnClickListener(clickListener);

        Button btnNum8 = (Button) view.findViewById(R.id.btn_number_8);
        btnNum8.setOnClickListener(clickListener);

        Button btnNum9 = (Button) view.findViewById(R.id.btn_number_9);
        btnNum9.setOnClickListener(clickListener);

        Button btnNum0 = (Button) view.findViewById(R.id.btn_number_0);
        btnNum0.setOnClickListener(clickListener);

        Button btnNumDot = (Button) view.findViewById(R.id.btn_number_dot);
        btnNumDot.setOnClickListener(clickListener);

        ImageButton btnNumDel = (ImageButton) view.findViewById(R.id.btn_number_del);
        btnNumDel.setOnClickListener(clickListener);
        btnNumDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 长按删除按钮，清空已经输入的金额
                cleanMoneyAmount();
                return true;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        try {
//            Field childFragment = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragment.setAccessible(true);
//            childFragment.set(this, null);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}
