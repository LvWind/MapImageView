# MapImageView
ImageView of static map images


```xml
	<com.lvwind.mapimageview.MapImageView
			android:id="@+id/miv"
			android:layout_width="match_parent"
			android:layout_height="wrap_content"
	/>
```


```Java
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
```