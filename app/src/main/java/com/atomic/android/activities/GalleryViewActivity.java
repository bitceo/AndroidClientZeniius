
package com.atomic.android.activities;

/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.os.Bundle;

import com.atomic.android.PicassoImageLoader;
import com.atomic.android.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewActivity extends BaseActivity {

    public static final String IMAGE_URLS_EXTRA_KEY = "GalleryViewActivity.IMAGE_URLS_EXTRA_KEY";

    private ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);
        String[] listImageUrl = getIntent().getStringArrayExtra(IMAGE_URLS_EXTRA_KEY);

        List<MediaInfo> infos = new ArrayList<MediaInfo>(listImageUrl.length);
        for (String url : listImageUrl)
            infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));

        scrollGalleryView = findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
               /* .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
                    @Override public boolean isImage() {
                        return true;
                    }

                    @Override public void loadMedia(Context context, ImageView imageView,
                                                    MediaLoader.SuccessCallback callback) {

                        callback.onSuccess();
                    }

                    @Override public void loadThumbnail(Context context, ImageView thumbnailView,
                                                        MediaLoader.SuccessCallback callback) {

                        callback.onSuccess();
                    }
                }))*/

                .addMedia(infos);
    }

}