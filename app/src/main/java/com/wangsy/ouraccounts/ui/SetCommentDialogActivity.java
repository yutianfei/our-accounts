package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.wangsy.ouraccounts.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 设置备注
 * <p/>
 * Created by wangsy on 15/10/26.
 */
public class SetCommentDialogActivity extends Activity {

    public static final String EXTRA_COMMENT = "extra_comment";

    private EditText etInputComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_comment);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        etInputComment = (EditText) findViewById(R.id.id_input_comment);
        etInputComment.setText(getIntent().getStringExtra(EXTRA_COMMENT));
        etInputComment.setSelection(etInputComment.getText().toString().length());

        // 初始化确定、取消按钮及事件
        initButtons();

        // 弹出软键盘
        showKeyBoard();
    }

    /**
     * 自动弹出软键盘
     * <p/>
     * 在调用时showSoftInput时，可能界面还未加载完成，etInputComment可能还为空，所以加上一个延时任务，延迟显示
     */
    private void showKeyBoard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //调用系统输入法
                InputMethodManager inputManager = (InputMethodManager) etInputComment
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etInputComment, 0);
            }
        }, 200);
    }

    private void initButtons() {
        Button btnOk = (Button) findViewById(R.id.id_button_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将设置的备注信息返回给调用者
                Intent data = new Intent();
                data.putExtra(EXTRA_COMMENT, etInputComment.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.id_button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}