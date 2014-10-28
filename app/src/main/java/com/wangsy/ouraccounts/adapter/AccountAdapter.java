package com.wangsy.ouraccounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.model.AccountModel;

import java.util.List;

/**
 * Created by wangsy on 15/10/27.
 */
public class AccountAdapter extends BaseAdapter {
    private List<AccountModel> accountsListData;
    private Context context;

    public AccountAdapter(List<AccountModel> list, Context context) {
        this.accountsListData = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return accountsListData.size();
    }

    @Override
    public AccountModel getItem(int i) {
        return accountsListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AccountModel account = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvAccountDate = (TextView) convertView.findViewById(R.id.id_account_date);
            viewHolder.tvAccountComment = (TextView) convertView.findViewById(R.id.id_account_comment);
            viewHolder.tvAccountMoney = (TextView) convertView.findViewById(R.id.id_account_money);
            viewHolder.imgViewAccountIcon = (ImageView) convertView.findViewById(R.id.id_account_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgViewAccountIcon.setImageResource(account.getIconToShow());
        viewHolder.tvAccountDate.setText(account.getDatetime());
        viewHolder.tvAccountMoney.setText(account.getAmount() + "");

        if (account.isOut()) {
            viewHolder.tvAccountMoney.setTextColor(
                    context.getResources().getColor(android.R.color.holo_green_light));
        } else {
            viewHolder.tvAccountMoney.setTextColor(
                    context.getResources().getColor(android.R.color.holo_red_light));
        }

        if (null == account.getComment() || "".equals(account.getComment())) {
            viewHolder.tvAccountComment.setVisibility(View.GONE);
        } else {
            viewHolder.tvAccountComment.setVisibility(View.VISIBLE);
            viewHolder.tvAccountComment.setText(account.getComment());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imgViewAccountIcon;
        TextView tvAccountDate, tvAccountComment, tvAccountMoney;
    }
}