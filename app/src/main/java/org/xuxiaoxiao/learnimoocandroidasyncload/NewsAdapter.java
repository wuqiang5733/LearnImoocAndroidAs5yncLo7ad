package org.xuxiaoxiao.learnimoocandroidasyncload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WuQiang on 2017/6/1.
 */


public class NewsAdapter extends BaseAdapter{
    private List<NewsBean> mList;
    private LayoutInflater mInflater;
    public NewsAdapter(Context context, List<NewsBean> list){
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout, null);
            viewHolder.iv_Icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(viewHolder);

        }else{
            viewHolder =(ViewHolder) convertView.getTag();
        }

        viewHolder.iv_Icon.setImageResource(R.drawable.ic_launcher);
        String url = mList.get(position).newsIconUrl;
        viewHolder.iv_Icon.setTag(url);
        new ImageLoader().showImageByThread(viewHolder.iv_Icon,url);
        viewHolder.tv_title.setText(mList.get(position).newsTitle);
        viewHolder.tv_content.setText(mList.get(position).newsContent);

        return convertView;
    }

    class ViewHolder{
        public TextView tv_title,tv_content;
        public ImageView iv_Icon;
    }

}
