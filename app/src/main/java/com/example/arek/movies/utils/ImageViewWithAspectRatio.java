package com.example.arek.movies.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.arek.movies.R;

/**
 * Created by Arkadiusz Wilczek on 24.02.18.
 *
 * Class to calculate image height for specified aspect ratio
 */

public class ImageViewWithAspectRatio extends android.support.v7.widget.AppCompatImageView {
    private float mAspectRatio;

    public ImageViewWithAspectRatio(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ImageViewWithAspectRatio,
                0,
                0);
        try {
            mAspectRatio = a.getFloat(R.styleable.ImageViewWithAspectRatio_aspectRatio, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ( mAspectRatio>0 ){
            int minimumWidth = getPaddingLeft() + getPaddingRight() +getSuggestedMinimumWidth();
            int width = resolveSizeAndState(minimumWidth,widthMeasureSpec,1);
            int height = (int) (width / mAspectRatio);

            setMeasuredDimension(width,height);
        }
    }
}
