package com.wangsy.ouraccounts.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.IconPageAdapter;
import com.wangsy.ouraccounts.callback.IconSelectedCallback;
import com.wangsy.ouraccounts.model.AccountModel;

import java.util.Date;

public class MainActivity extends FragmentActivity implements IconSelectedCallback {

    private EditText etMoneyAmount;
    private StringBuilder sbMoneyAmount;

    private String accountType;
    private boolean accountIsOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fragment);

        TextView title = (TextView) findViewById(R.id.id_title);
        title.setText(R.string.title_new);

        // 主界面显示右侧按钮：保存数据
        initButtonRight();

        // 初始化 viewpager
        initViewPager();

        // 初始化数字键盘
        initNumberButtons();

        etMoneyAmount = (EditText) findViewById(R.id.id_money_amount);
        sbMoneyAmount = new StringBuilder();
    }

    @Override
    public void onIconSelected(boolean isOut, String type) {
        accountIsOut = isOut;
        accountType = type;
    }

    private void saveData() {

        // 根据显示的金额进行数据处理
        StringBuilder moneyString = new StringBuilder();
        moneyString.append(etMoneyAmount.getText().toString());

        // 没有金额或没有类型，不进行保存
        if (moneyString.toString().isEmpty() || accountType == null) {
            Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
            return;
        }

        // 输入多位金额，第一个数字是0时，在保存数据的时候，自动将0去除
        if (moneyString.length() > 1 &&
                moneyString.charAt(0) == '0' &&
                moneyString.charAt(1) != '.') {
            moneyString.deleteCharAt(0);
        }

        // 设置要保存的数据
        AccountModel accountData = new AccountModel();
        accountData.setAmount(Float.parseFloat(moneyString.toString()));
        accountData.setDate(new Date());
        accountData.setIsOut(accountIsOut);
        accountData.setType(accountType);

        // 保存数据
        boolean saveFlag = accountData.save();
        if (saveFlag) {
            Toast.makeText(this, accountData.toString(), Toast.LENGTH_LONG).show();
            // 保存完成后，清除当前状态
            clearStates();
        }
    }

    private void clearStates() {
        sbMoneyAmount.delete(0, sbMoneyAmount.length());
        etMoneyAmount.setText("0");
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
                    etMoneyAmount.setText(sbMoneyAmount.toString());
                    break;
                case R.id.btn_number_dot:
                    // 保证只有一个小数点
                    if (sbMoneyAmount.length() == 0) {
                        sbMoneyAmount.append("0.");
                    } else if (!(sbMoneyAmount.indexOf(".") > -1)) {
                        sbMoneyAmount.append('.');
                    }
                    etMoneyAmount.setText(sbMoneyAmount.toString());
                    break;
                case R.id.btn_number_del:
                    if (!sbMoneyAmount.toString().isEmpty()) {
                        sbMoneyAmount.deleteCharAt(sbMoneyAmount.length() - 1);
                    }
                    etMoneyAmount.setText(sbMoneyAmount.toString());
                    break;
                default:
                    Button btn = (Button) v;
                    sbMoneyAmount.append(btn.getText().toString());
                    etMoneyAmount.setText(sbMoneyAmount.toString());
                    break;
            }
            etMoneyAmount.setSelection(sbMoneyAmount.length());
        }
    }

    private void initViewPager() {
        IconPageAdapter mAdapter = new IconPageAdapter(getSupportFragmentManager());
        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.id_viewpager_indicator);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initButtonRight() {
        ImageButton imgBtn = (ImageButton) findViewById(R.id.id_title_right_btn);
        imgBtn.setVisibility(View.VISIBLE);
        imgBtn.setImageResource(R.drawable.number_btn_ok);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存数据
                saveData();
            }
        });
    }

    private void initNumberButtons() {
        NumberButtonClickListener clickListener = new NumberButtonClickListener();

        Button btnNum1 = (Button) findViewById(R.id.btn_number_1);
        btnNum1.setOnClickListener(clickListener);

        Button btnNum2 = (Button) findViewById(R.id.btn_number_2);
        btnNum2.setOnClickListener(clickListener);

        Button btnNum3 = (Button) findViewById(R.id.btn_number_3);
        btnNum3.setOnClickListener(clickListener);

        Button btnNum4 = (Button) findViewById(R.id.btn_number_4);
        btnNum4.setOnClickListener(clickListener);

        Button btnNum5 = (Button) findViewById(R.id.btn_number_5);
        btnNum5.setOnClickListener(clickListener);

        Button btnNum6 = (Button) findViewById(R.id.btn_number_6);
        btnNum6.setOnClickListener(clickListener);

        Button btnNum7 = (Button) findViewById(R.id.btn_number_7);
        btnNum7.setOnClickListener(clickListener);

        Button btnNum8 = (Button) findViewById(R.id.btn_number_8);
        btnNum8.setOnClickListener(clickListener);

        Button btnNum9 = (Button) findViewById(R.id.btn_number_9);
        btnNum9.setOnClickListener(clickListener);

        Button btnNum0 = (Button) findViewById(R.id.btn_number_0);
        btnNum0.setOnClickListener(clickListener);

        Button btnNumDot = (Button) findViewById(R.id.btn_number_dot);
        btnNumDot.setOnClickListener(clickListener);

        ImageButton btnNumDel = (ImageButton) findViewById(R.id.btn_number_del);
        btnNumDel.setOnClickListener(clickListener);
    }
}
