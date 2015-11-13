package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 提供Toast提示
 * <p/>
 * Created by wangsy on 15/11/13.
 */
public class BaseActivity extends Activity {

    /**
     * Toast提示
     */
    protected Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }
}
