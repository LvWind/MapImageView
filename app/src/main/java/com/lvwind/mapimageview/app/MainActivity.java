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

package com.lvwind.mapimageview.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.lvwind.mapimageview.MapImageCache;
import com.lvwind.mapimageview.MapImageView;
import com.lvwind.mapimageview.ImageLoadListener;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapImageView miv = (MapImageView) findViewById(R.id.image);
        MapImageView miv2 = (MapImageView) findViewById(R.id.image2);

        //load google map
        miv
                .location(31.2314164, 121.471825)
                .size(500, 700)
                .zoom(16)
                .provider(MapImageView.TYPE_GOOGLE)
                .listener(new ImageLoadListener() {
                    @Override
                    public void onStart(String url) {
                        Log.d(TAG, "start load map: " + url);
                    }

                    @Override
                    public void onFinish(String url) {
                        Log.d(TAG, "finish load map: " + url);
                    }
                })
                .load();

        //load baidu map
        //get your key from http://lbsyun.baidu.com/apiconsole/key/create
        miv2.setBaiduAK("E4805d16520de693a3fe707cdc962045");
        miv2
                .location(31.2314164, 121.471825)
                .size(500, 700)
                .zoom(16)
                .provider(MapImageView.TYPE_BAIDU)
                .listener(new ImageLoadListener() {
                    @Override
                    public void onStart(String url) {
                        Log.d(TAG, "start load map: " + url);
                    }

                    @Override
                    public void onFinish(String url) {
                        Log.d(TAG, "finish load map: " + url);
                    }
                })
                .load();


    }

    @Override
    public void onDestroy() {
        MapImageCache.deleteAll(getCacheDir());
        super.onDestroy();
    }
}
