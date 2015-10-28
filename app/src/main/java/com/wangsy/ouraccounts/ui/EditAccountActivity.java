package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.fragment.ReportListFragment;
import com.wangsy.ouraccounts.model.AccountModel;

/**
 * 对记录进行编辑
 * <p/>
 * Created by wangsy on 15/10/28.
 */
public class EditAccountActivity extends Activity implements View.OnClickListener {

    public static final String EXTRA_EDIT_DATA = "extra_edit_data";

    private AccountModel waitToEditAccount;

    private EditText tvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        waitToEditAccount = (AccountModel) getIntent().getSerializableExtra(EXTRA_EDIT_DATA);

        TextView title = (TextView) findViewById(R.id.id_title);
        title.setText(R.string.title_edit);

        initViews();
    }

    private void initViews() {
        tvComment = (EditText) findViewById(R.id.id_edit_comment);
        tvComment.setText(waitToEditAccount.getComment());

        // 主界面显示右侧按钮：修改完成，保存数据
        initButtonRight();

        // 主界面显示左侧按钮：取消修改，返回
        initButtonLeft();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_title_right_btn:
                // TODO

                break;
            case R.id.id_title_left_btn:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private void initButtonRight() {
        ImageButton imgBtnRight = (ImageButton) findViewById(R.id.id_title_right_btn);
        imgBtnRight.setVisibility(View.VISIBLE);
        imgBtnRight.setImageResource(R.mipmap.icon_ok);
        imgBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将修改的信息返回给调用者
                //setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void initButtonLeft() {
        ImageButton imgBtnLeft = (ImageButton) findViewById(R.id.id_title_left_btn);
        imgBtnLeft.setVisibility(View.VISIBLE);
        imgBtnLeft.setImageResource(R.mipmap.icon_back);
        imgBtnLeft.setOnClickListener(this);
    }
}