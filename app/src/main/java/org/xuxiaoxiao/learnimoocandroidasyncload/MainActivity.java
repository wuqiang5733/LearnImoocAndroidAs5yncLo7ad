package org.xuxiaoxiao.learnimoocandroidasyncload;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    //  num 后面的数据是要获取多少个对象
    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.lv_main);
        new NewsAsyncTask().execute(URL);

    }

    /**
     * 实现网络的异步访问
     */
    class NewsAsyncTask extends AsyncTask<String, Void, List<NewsBean>> {

        @Override
        protected List<NewsBean> doInBackground(String... params) {

            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            NewsAdapter adapter = new NewsAdapter(MainActivity.this, result);
            mListView.setAdapter(adapter);
        }
    }

    /**
     * 将 url 对应的JSON格式数据转化为封装的NewsBean
     *
     * @param url
     * @return
     */
    private List<NewsBean> getJsonData(String url) {
        List<NewsBean> newsBeanList = new ArrayList<NewsBean>();
        try {
            // 用简单粗暴的方式获得网络数据
            String jsonString = readStream(new URL(url).openStream());
//            Log.d("xys", jsonString);
            JSONObject jsonObject;
            NewsBean newsBean;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    newsBean = new NewsBean();
                    newsBean.newsIconUrl = jsonObject.getString("picSmall");
                    newsBean.newsTitle = jsonObject.getString("name");
                    newsBean.newsContent = jsonObject.getString("description");
                    newsBeanList.add(newsBean);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newsBeanList;

    }

    /**
     * 通过is解析网页返回的数据
     *
     * @param is
     * @return
     */
    private String readStream(InputStream is) {
        InputStreamReader isr;
        String result = "";

        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
