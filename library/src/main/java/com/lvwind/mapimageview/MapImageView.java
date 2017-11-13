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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.ref.SoftReference;


public class MapImageView extends ImageView {
    public static final int TYPE_GOOGLE = 1;
    public static final int TYPE_BAIDU = 2;
    public static final int TYPE_OSM = 3;

    private String BAIDU_API_KEY;

    private final Context context;
    private final Handler handler = new Handler();
    private Request request;
    private String url;
    private double mLatitude = 0;
    private double mLongitude = 0;
    private int mZoom = 15;
    private int mHeight = 200;
    private int mWidth = 300;
    private int mProvider = TYPE_GOOGLE;
    private boolean isLoading = false;
    private ImageLoadListener mListener;
    private final Runnable imageLoadRunnable = new Runnable() {
        public void run() {
            setImageLocalCache();
        }
    };
    private final Runnable threadRunnable = new Runnable() {
        public void run() {
            handler.post(imageLoadRunnable);
        }
    };

    public MapImageView(Context context) {
        super(context);
        this.context = context;
    }

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MapImageView);

        mLatitude = array.getFloat(R.styleable.MapImageView_mLatitude, 0);
        mLongitude = array.getFloat(R.styleable.MapImageView_mLongitude, 0);
        mZoom = array.getInt(R.styleable.MapImageView_mZoom, 15);
        mHeight = array.getDimensionPixelSize(R.styleable.MapImageView_mHeight, 100);
        mWidth = array.getDimensionPixelSize(R.styleable.MapImageView_mWidth, 100);
//        mProvider = array.getInt(R.styleable.MapImageView_mProvider, TYPE_GOOGLE);
    }

    private boolean setImageLocalCache() {
        SoftReference<Bitmap> image = MapImageCache.getImage(
                context.getCacheDir(), url);
        if (image != null && image.get() != null) {
            setImageBitmap(image.get());
            mListener.onFinish(url);
            isLoading = false;
            return true;
        }
        return false;
    }

    public void setImageLoadListener(ImageLoadListener listener) {
        this.mListener = listener;
    }

    public MapImageView listener(ImageLoadListener listener) {
        setImageLoadListener(listener);
        return this;
    }

    public void load() {
        if (mLatitude == 0 && mLongitude == 0) {
            throw new RuntimeException("Location not set");
        }
        switch (mProvider) {
            case TYPE_GOOGLE:
                url = "http://maps.google.com/maps/api/staticmap?center=" + mLatitude + "," + mLongitude
                        + "&zoom=" + mZoom + "&size=" + mWidth + "x" + mHeight + "&sensor=false";
                break;
            case TYPE_BAIDU:
                if (TextUtils.isEmpty(BAIDU_API_KEY)) {
                    throw new RuntimeException("Baidu Map API Key not set");
                }
                url = "http://api.map.baidu.com/staticimage?ak=" + BAIDU_API_KEY
                        + "&center=" + mLongitude + "," + mLatitude + "&width=" + mWidth + "&height=" + mHeight
                        + "&zoom=" + mZoom;
            default:
                break;
        }

        isLoading = true;
        request = new Request(url, context.getCacheDir(), threadRunnable);
        if (setImageLocalCache()) {
            return;
        }
        mListener.onStart(url);
        RequestList.getInstance().putRequest(request, RequestList.Priority.HIGH);
    }

    public Boolean isLoading() {
        return isLoading;
    }

    public void setLocation(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public MapImageView location(double latitude, double longitude) {
        setLocation(latitude, longitude);
        return this;
    }

    public void setLatitude(float latitude) {
        this.mLatitude = latitude;
    }

    public MapImageView latitude(float latitude) {
        setLatitude(latitude);
        return this;
    }

    public void setLongitude(float longitude) {
        this.mLongitude = longitude;
    }

    public MapImageView longitude(float longitude) {
        setLongitude(longitude);
        return this;
    }

    public void setZoom(int zoom) {
        this.mZoom = zoom;
    }

    public MapImageView zoom(int zoom) {
        setZoom(zoom);
        return this;
    }

    public void setSize(int height, int width) {
        this.mHeight = height;
        this.mWidth = width;
    }

    public MapImageView size(int height, int width) {
        setSize(height, width);
        return this;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public MapImageView height(int height) {
        setHeight(height);
        return this;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public MapImageView width(int width) {
        setWidth(width);
        return this;
    }

    public void setProvider(int provider) {
        this.mProvider = provider;
    }

    public MapImageView provider(int provider) {
        setProvider(provider);
        return this;
    }

    public void setBaiduAK(String key) {
        this.BAIDU_API_KEY = key;
    }
}
