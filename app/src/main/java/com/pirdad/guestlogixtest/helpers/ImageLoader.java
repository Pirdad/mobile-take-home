package com.pirdad.guestlogixtest.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ImageLoader {

    private static ImageLoader INSTANCE;
    public static ImageLoader getInstance(){
        if (INSTANCE == null) {
            synchronized (ImageLoader.class){
                if (INSTANCE == null) {
                    INSTANCE = new ImageLoader();
                }
            }
        }
        return INSTANCE;
    }

    private LruCache<String, Bitmap> cache;
    private HashMap<ImageView, LruCacheAsyncTask> taskSet;

    private ImageLoader(){
        taskSet = new HashMap<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        cache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    private Bitmap getBitmapFromMemory(String url){
        return cache.get(url);
    }

    private void putBitmapToMemory(String url, Bitmap bitmap) {
        if (getBitmapFromMemory(url) == null) {
            cache.put(url, bitmap);
        }
    }

    public void loadImage(ImageView imageView, String imageUrl) {
        Bitmap bitmap = getBitmapFromMemory(imageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            LruCacheAsyncTask currTask = taskSet.get(imageView);
            if (currTask != null) {
                currTask.imageView = null;
                currTask.cancel(false);
            }
            LruCacheAsyncTask task = new LruCacheAsyncTask(imageView);
            task.execute(imageUrl);
            taskSet.put(imageView, task);
        }
    }

    private class LruCacheAsyncTask extends AsyncTask<String, Void, Bitmap>{

        private ImageView imageView;

        public LruCacheAsyncTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = getBitmapFromUrl(strings[0]);
            if (bitmap != null) {
                putBitmapToMemory(strings[0], bitmap);
            }
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            if (imageView != null) {
                taskSet.remove(imageView);
            }
        }

        private Bitmap getBitmapFromUrl(String urlPath) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(urlPath);
                URLConnection conn = url.openConnection();
                conn.connect();
                if (isCancelled()) {
                    return null;
                }
                InputStream in;
                in = conn.getInputStream();
                if (isCancelled()) {
                    return null;
                }
                bitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    public void cancelAllTask() {
        if (taskSet != null) {
            for (ImageView key : taskSet.keySet()) {
                taskSet.get(key).cancel(false);
            }
            taskSet.clear();
        }
    }
}