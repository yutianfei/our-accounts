package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.IconGridViewAdapter;
import com.wangsy.ouraccounts.model.AccountModel;
import com.wangsy.ouraccounts.model.IconModel;
import com.wangsy.ouraccounts.constants.IconConstants;
import com.wangsy.ouraccounts.constants.TableConstants;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 对记录进行编辑
 * <p/>
 * Created by wangsy on 15/10/28.
 */
public class EditAccountActivity extends Activity implements View.OnClickListener {

    public static final String EXTRA_EDIT_DATA = "extra_edit_data";
    public static final String EXTRA_EDIT_DATA_ID = "extra_edit_data_id";
    public static final int REQUEST_SET_DATE = 1;

    private List<IconModel> iconsList;

    private AccountModel editAccount;
    private long id;

    private EditText etComment;
    private EditText etAmount;
    private TextView tvDatetime;

    private GridView iconGridView;
    private IconGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        id = getIntent().getLongExtra(EXTRA_EDIT_DATA_ID, -1);
        editAccount = (AccountModel) getIntent().getSerializableExtra(EXTRA_EDIT_DATA);

        TextView title = (TextView) findViewById(R.id.id_title);
        title.setText(R.string.title_edit);

        initViews();
    }

    private void initViews() {
        // 备注
        etComment = (EditText) findViewById(R.id.id_edit_comment);
        etComment.setText(editAccount.getComment());
        etComment.setSelection(etComment.getText().toString().length());

        // 金额
        etAmount = (EditText) findViewById(R.id.id_edit_amount);
        etAmount.setText(editAccount.getAmount() + "");
        etAmount.setSelection(etAmount.getText().toString().length());
        setEtAmountColor();

        // 时间
        tvDatetime = (TextView) findViewById(R.id.id_edit_datetime);
        tvDatetime.setText(editAccount.getDatetime());
        tvDatetime.setOnClickListener(this);

        // 类型
        initIconType();

        // 主界面显示右侧按钮：修改完成，保存数据
        initButtonRight();

        // 主界面显示左侧按钮：取消修改，返回
        initButtonLeft();
    }

    /**
     * 设置金额的颜色
     */
    private void setEtAmountColor() {
        if (editAccount.isOut()) {
            etAmount.setTextColor(getResources().getColor(R.color.color_money_out));
        } else {
            etAmount.setTextColor(getResources().getColor(R.color.color_money_in));
        }
    }

    private void initIconType() {
        iconGridView = (GridView) findViewById(R.id.id_edit_icon);
        iconsList = IconConstants.getIconsList();
        adapter = new IconGridViewAdapter(iconsList, this);
        iconGridView.setAdapter(adapter);

        // 默认选择传递过来的数据的图标
        setDefaultIconSelection();

        iconGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetIconStates();

                IconModel iconModel = iconsList.get(position);
                iconModel.typeTextColor = R.color.color_icon_selected;
                iconModel.iconImageToShow = iconModel.selectedIcon;

                editAccount.setOut(iconModel.isOut);
                editAccount.setType(iconModel.type);
                editAccount.setIconImageName(iconModel.iconImageName);
                setEtAmountColor();

                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setDefaultIconSelection() {
        for (int i = 0; i < iconsList.size(); i++) {
            if (editAccount.getType().equals(iconsList.get(i).type)) {
                iconsList.get(i).typeTextColor = R.color.color_icon_selected;
                iconsList.get(i).iconImageToShow = iconsList.get(i).selectedIcon;
                iconGridView.setSelection(i);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 清除图标状态
     */
    private void resetIconStates() {
        for (int i = 0; i < iconsList.size(); i++) {
            iconsList.get(i).typeTextColor = R.color.color_icon_normal;
            iconsList.get(i).iconImageToShow = iconsList.get(i).normalIcon;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_edit_datetime:
                setDatetime();
                break;
            case R.id.id_title_right_btn:
                saveEditData();
                break;
            case R.id.id_title_left_btn:
                finish();
                break;
        }
    }

    /**
     * 设置时间
     */
    private void setDatetime() {
        Intent intent = new Intent(EditAccountActivity.this, SetDatetimeDialogActivity.class);
        intent.putExtra(SetDatetimeDialogActivity.EXTRA_DATETIME, editAccount.getDatetime());
        startActivityForResult(intent, REQUEST_SET_DATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SET_DATE) {
            editAccount.setDatetime(data.getStringExtra(SetDatetimeDialogActivity.EXTRA_DATETIME));
            tvDatetime.setText(editAccount.getDatetime());
        }
    }

    /**
     * 保存更该完成的数据
     */
    private void saveEditData() {
        // 没有金额，提示，不保存
        if (TextUtils.isEmpty(etAmount.getText().toString())) {
            Toast.makeText(this, R.string.tip_amount_null, Toast.LENGTH_LONG).show();
            return;
        }

        // 0元提示，不保存
        if (Float.parseFloat(etAmount.getText().toString()) == 0) {
            Toast.makeText(this, R.string.tip_amount_ensure, Toast.LENGTH_LONG).show();
            return;
        }

        // 设置新的数据
        editAccount.setAmount(Float.parseFloat(etAmount.getText().toString()));
        editAccount.setComment(etComment.getText().toString());

        // 更新数据库数据
        ContentValues values = new ContentValues();
        values.put(TableConstants.ISOUT, editAccount.isOut());
        values.put(TableConstants.TYPE, editAccount.getType());
        values.put(TableConstants.AMOUNT, editAccount.getAmount());
        values.put(TableConstants.COMMENT, editAccount.getComment());
        values.put(TableConstants.DATETIME, editAccount.getDatetime());
        values.put(TableConstants.ICONIMAGENAME, editAccount.getIconImageName());
        int count = DataSupport.update(AccountModel.class, values, id);
        if (count > 0) {
            Toast.makeText(this, R.string.tip_modified_ok, Toast.LENGTH_SHORT).show();
            sendBroadcastToRefreshData();
            BaseListActivity.isDataModified = true;
            finish();
        }
    }

    /**
     * 通知数据更新
     */
    private void sendBroadcastToRefreshData() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.REFRESH_DATA_BROADCAST_INTENT_FILTER);
        sendBroadcast(intent);
    }

    private void initButtonRight() {
        ImageButton imgBtnRight = (ImageButton) findViewById(R.id.id_title_right_btn);
        imgBtnRight.setVisibility(View.VISIBLE);
        imgBtnRight.setOnClickListener(this);
    }

    private void initButtonLeft() {
        ImageButton imgBtnLeft = (ImageButton) findViewById(R.id.id_title_left_btn);
        imgBtnLeft.setVisibility(View.VISIBLE);
        imgBtnLeft.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
