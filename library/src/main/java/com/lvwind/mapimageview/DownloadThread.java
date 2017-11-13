/*
 * Copyright (c) 2017. Jason Shaw <lvwind.shaw@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lvwind.mapimageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadThread extends Thread {
    private final RequestList requestList;

    DownloadThread(String name, RequestList requestList) {
        super(name);
        this.requestList = requestList;
    }

    @Override
    public void run() {
        while (true) {
            Request request = requestList.takeRequest();
            request.setStatus(Request.Status.LOADING);
            SoftReference<Bitmap> image = MapImageCache.getImage(
                    request.getCacheDir(), request.getUrl());
            if (image == null || image.get() == null) {
                image = getImage(request.getUrl());
                if (image != null && image.get() != null) {
                    MapImageCache.saveBitmap(request.getCacheDir(),
                            request.getUrl(), image.get());
                }
            }
            request.setStatus(Request.Status.LOADED);
            request.getRunnable().run();
        }
    }

    private SoftReference<Bitmap> getImage(String url) {
        try {
            return new SoftReference<Bitmap>(getBitmapFromURL(url));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getBitmapFromURL(String strUrl) throws IOException {
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            connection.setRequestMethod("GET");
            connection.setReadTimeout(500000);
            connection.setConnectTimeout(50000);
            connection.connect();
            is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } finally {
            try {
                if (connection != null)
                    connection.disconnect();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}