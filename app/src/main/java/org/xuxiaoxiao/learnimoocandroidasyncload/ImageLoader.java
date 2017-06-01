package org.xuxiaoxiao.learnimoocandroidasyncload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by WuQiang on 2017/6/1.
 */


public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl))
                mImageView.setImageBitmap((Bitmap) msg.obj);
        }
    };

    public void showImageByThread(ImageView imageView, final String url) {
        mImageView = imageView;
        mUrl = url;

        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
                super.run();
            }
        }.start();
    }

    public Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
//            Thread.sleep(900);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void showImageByAsyncTask(ImageView imageView, String url) {
        new NewsAsyncTask(imageView,url).execute(url);
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        private String mUrl;

        public NewsAsyncTask(ImageView imageView,String url) {
            mImageView = imageView;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mUrl))
            mImageView.setImageBitmap(bitmap);
        }
    }
    /*
    public ImageLoader(ListView listView) {
        mListView = listView;
        mTasks = new HashSet<LoadImageTask>(); //获取最大可用内存
        int maxMemory = (int)
                Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) { //返回图片大小，每次存入缓存时调用
                return value.getByteCount();
            }
        };
    }

    public void addBitmapToCache(String url, Bitmap bm) {
        mCache.put(url, bm);
        if (getBitmapFromCache(url) != null) {
            System.out.println("添加");
        }
    }

    public Bitmap getBitmapFromCache(String url) {
        return mCache.get(url);
    }


    public void showImageByAsyncTask(ImageView imageView, String url) {
        Bitmap bm = getBitmapFromCache(url);
        if (bm == null) {
            System.out.println(1); //
            //new LoadImageTask(imageView, url).execute(url);
        } else {
            imageView.setImageBitmap(bm);
        }
    }

    public void loadImage(int start, int end) {
        for (int i = start; i < end; i++) {
            String url = NewsAdapter.URLS[i];
            Bitmap bm = getBitmapFromCache(url);
            if (bm == null) {
                LoadImageTask loadImageTask = new LoadImageTask(url);
                loadImageTask.execute(url);
                mTasks.add(loadImageTask);
            } else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bm);
            }
        }
    }

    public void cancelAllTasks() {
        if (mTasks != null) {
            for (LoadImageTask loadImageTask : mTasks) {
                loadImageTask.cancel(true);
            }
        }
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private String url;

        public LoadImageTask(String url) {
            super();
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = getBitmapFromUrl(params[0]);
            if (bm != null) {
                addBitmapToCache(params[0], bm);
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            ImageView mImageView = (ImageView) mListView.findViewWithTag(url);
            if (mImageView != null && result != null) {
                mImageView.setImageBitmap(result);
            }
            mTasks.remove(this);
        }
    }

*/

}

