package com.example.exampleproject.base.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;


/**
 * 带手势缩放PhotoView PagerAdapter基类
 * Created by chang on 2017/3/1.
 */

public class BasePhotoViewAdapter extends BasePagerAdapter {

    public BasePhotoViewAdapter(List<PhotoInfo> list) {
        super(list);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setImageURI(((PhotoInfo) mList.get(position)).getUri());

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    class PhotoInfo {
        private Uri uri;

        public Uri getUri() {
            return uri;
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }
    }
}
