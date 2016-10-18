package com.qihoo.bumppic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qihoo.bumppic.utils.DensityUtils;

public class HintTextView extends TextView {
    private String mRefreshCount = "10";
    private boolean mShowCount = false;
    private Drawable mCountDrawable;
    private int mCountHeight;
    private int mCountBottomMarginCenter;
    private int mCountMarginCenter;
    private int mCountTextMarginHorizontal;
    private Paint mPaint;
    private Rect mRect;

    public HintTextView(Context context) {
        super(context);
        init();
    }

    public HintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCountHeight = DensityUtils.dip2px(getContext(),15);
        mCountBottomMarginCenter = DensityUtils.dip2px(getContext(),5);
        mCountMarginCenter = DensityUtils.dip2px(getContext(),12);
        mCountTextMarginHorizontal = DensityUtils.dip2px(getContext(), 5);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(DensityUtils.dip2px(getContext(),11));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShowCount && !"0".equals(mRefreshCount)) {
            mPaint.getTextBounds(mRefreshCount, 0, mRefreshCount.length(), mRect);
            int textWidth = mRect.width() + mCountTextMarginHorizontal * 2;
            textWidth = textWidth < mCountHeight ? mCountHeight : textWidth;
            int bottom = Math.round(getHeight() / 2.f - mCountBottomMarginCenter);
            int top = bottom - mCountHeight;
            int left = Math.round(getWidth() / 2.f + mCountMarginCenter - textWidth / 2.f);
            int right = left + textWidth;
            if (mCountDrawable != null) {
                mCountDrawable.setBounds(left, top, right, bottom);
                mCountDrawable.draw(canvas);
            }
            int y = (int) (mRect.height() / 2.f + top + (bottom - top) / 2.f);
            canvas.drawText(mRefreshCount, left + Math.round((right - left) / 2.f), y, mPaint);
        }
    }

    public void setCountText(String count) {
        if (TextUtils.isEmpty(count) || count.length() > 5) {
            return;
        }
        mRefreshCount = count;
        if (mShowCount && !"0".equals(mRefreshCount)) {
            invalidate();
        }
    }

    public void setCountTextColor(int color) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            if (mShowCount) {
                invalidate();
            }
        }
    }

    public void setShowCount(boolean show) {
        if (mShowCount != show) {
            mShowCount = show;
            invalidate();
        }
    }

    public void setHitTextSize(int size) {
        if (mPaint.getTextSize() != size) {
            mPaint.setTextSize(size);
            invalidate();
        }
    }

    public void setCountBackgroundDrawable(Drawable drawable) {
        if (mCountDrawable != drawable) {
            mCountDrawable = drawable;
            if (mShowCount) {
                invalidate();
            }
        }
    }

    public void setHitHeight(int height) {
        if (mCountHeight != height) {
            mCountHeight = height;
            invalidate();
        }
    }

    public void setHitMarginCenter(int bottomMarginCenter, int horizontalMarginCenter, int textMarginHorizontal) {
        boolean invalidate = false;
        if (mCountBottomMarginCenter != bottomMarginCenter) {
            mCountBottomMarginCenter = bottomMarginCenter;
            invalidate = true;
        }
        if (mCountMarginCenter != horizontalMarginCenter) {
            mCountMarginCenter = horizontalMarginCenter;
            invalidate = true;
        }
        if (mCountTextMarginHorizontal != textMarginHorizontal) {
            mCountTextMarginHorizontal = textMarginHorizontal;
            invalidate = true;
        }
        if (invalidate) {
            invalidate();
        }
    }
}
