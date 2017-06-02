package org.xuxiaoxiao.learnimoocandroidasyncload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WuQiang on 2017/6/1.
 */


public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private List<NewsBean> mList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private int mStart,mEnd;
    // 保存当前所有获得图片的URL地址
    public static String[] URLS;

    public NewsAdapter(Context context, List<NewsBean> data, ListView listView){
        mList = data;
        mInflater = LayoutInflater.from(context);
        // 这样就只会有一个LruCache存在
        mImageLoader = new ImageLoader(listView);
        URLS = new String[data.size()];
        for (int i=0;i<data.size();i++){
            URLS[i] = data.get(i).newsIconUrl;
        }
        listView.setOnScrollListener(this);
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
//        new ImageLoader().showImageByThread(viewHolder.iv_Icon,url);
        mImageLoader.showImageByAsyncTask(viewHolder.iv_Icon, url);
        viewHolder.tv_title.setText(mList.get(position).newsTitle);
        viewHolder.tv_content.setText(mList.get(position).newsContent);

        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
       if (scrollState == SCROLL_STATE_IDLE){
           // 加载可见项
           mImageLoader.loadImages(mStart,mEnd);
       }else {
           // 停止加载
           mImageLoader.cancelAllTasks();
       }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
    }

    class ViewHolder{
        public TextView tv_title,tv_content;
        public ImageView iv_Icon;
    }

}
