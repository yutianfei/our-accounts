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
 * 为记录列表提供数据
 * <p/>
 * Created by wangsy on 15/10/27.
 */
public class AccountListAdapter extends BaseAdapter {
    private List<AccountModel> accountsListData;
    private Context context;

    public AccountListAdapter(List<AccountModel> list, Context context) {
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
            viewHolder.tvAccountIconType = (TextView) convertView.findViewById(R.id.id_account_icon_type);
            viewHolder.imgViewIcon = (ImageView) convertView.findViewById(R.id.id_account_icon_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvAccountIconType.setText(account.getType());
        viewHolder.imgViewIcon.setImageResource(account.getIconToShow());
        viewHolder.tvAccountDate.setText(account.getDatetime());
        viewHolder.tvAccountMoney.setText(account.getAmount() + "");

        if (account.isOut()) {
            viewHolder.tvAccountMoney.setTextColor(
                    context.getResources().getColor(R.color.color_money_out));
        } else {
            viewHolder.tvAccountMoney.setTextColor(
                    context.getResources().getColor(R.color.color_money_in));
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
        ImageView imgViewIcon;
        TextView tvAccountDate, tvAccountComment, tvAccountMoney, tvAccountIconType;
    }
}
