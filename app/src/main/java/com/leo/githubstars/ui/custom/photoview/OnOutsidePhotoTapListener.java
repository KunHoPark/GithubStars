package com.leo.githubstars.ui.custom.photoview;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Callback when the user tapped outside of the photo
 */
public interface OnOutsidePhotoTapListener extends Serializable {

    /**
     * The outside of the photo has been tapped
     */
    void onOutsidePhotoTap(ImageView imageView);
}
