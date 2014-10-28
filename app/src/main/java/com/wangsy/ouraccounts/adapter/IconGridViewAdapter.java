package com.wangsy.ouraccounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.model.IconModel;

import java.util.List;

/**
 * 为IconFragment里的GridView提供adapter
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class IconGridViewAdapter extends BaseAdapter {

    private List<IconModel> iconsList;
    private Context context;
    private LayoutInflater mInflater;


    public IconGridViewAdapter(List<IconModel> iconsList, Context context) {
        this.iconsList = iconsList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iconsList.size();
    }

    @Override
    public Object getItem(int position) {
        return iconsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        IconModel typeIcon = (IconModel) getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.icon_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvIcon = (TextView) convertView.findViewById(R.id.icon_item);
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.icon_item_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvIcon.setText(typeIcon.iconName);
        viewHolder.tvIcon.setTextColor(context.getResources().getColor(typeIcon.iconNameColor));
        viewHolder.imgIcon.setImageResource(typeIcon.iconImageToShow);

        return convertView;
    }

    class ViewHolder {
        TextView tvIcon;
        ImageView imgIcon;
    }
}
