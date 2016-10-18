package com.qihoo.bumppic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class DrawableCenterButton extends TextView {

    public DrawableCenterButton(Context context) {
        super(context);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int textWidth;
    int textHeight;
    Drawable mDrawableLeft = null;
    int startDrawableX = 0;
    int startDrawableY= 0;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initText();
    }

    private void initText() {
        String textStr = super.getText().toString();
        Rect rect = new Rect();
        getPaint().getTextBounds(textStr,0,textStr.length(),rect);
        getPaint().setColor(getTextColors().getDefaultColor());
        getPaint().setTextSize(getTextSize());
        textWidth = rect.width();
        textHeight = rect.height();
        if (mDrawableLeft == null) mDrawableLeft = getCompoundDrawables()[0];

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mDrawableLeft == null) {
            super.onDraw(canvas);
            return;
        }
        int drawablePadding = getCompoundDrawablePadding();
        int drawableWidth = this.mDrawableLeft.getIntrinsicWidth();
        int drawableHeight = this.mDrawableLeft.getIntrinsicHeight();
        startDrawableX = (getWidth() >> 1) - ((drawablePadding + textWidth + drawableWidth) >> 1);
        startDrawableY = (getHeight() >> 1) - (drawableHeight >> 1);
        //画旋转图片
        canvas.save();
        canvas.translate(startDrawableX, startDrawableY);
        this.mDrawableLeft.draw(canvas);
        canvas.restore();

        //画文字
        int boxht = this.getMeasuredHeight() - this.getExtendedPaddingTop() - this.getExtendedPaddingBottom();
        int textht = getLayout().getHeight();
        int  voffsetText = boxht - textht >> 1;
        canvas.save();
        canvas.translate((float) (startDrawableX + drawableWidth + drawablePadding), (float) (getExtendedPaddingTop() + voffsetText));
        getLayout().draw(canvas);
        canvas.restore();
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
//        super.invalidateDrawable(drawable);
        final Rect dirty = drawable.getBounds();
        int scrollX = 0;
        int scrollY = 0;
        if(drawable == this.mDrawableLeft){
            scrollX = startDrawableX;
            scrollY = startDrawableY;
        }
        this.invalidate(dirty.left + scrollX-2, dirty.top + scrollY-2, dirty.right + scrollX+2, dirty.bottom + scrollY+2);
    }

    public Drawable getDrawableLeft() {
        return mDrawableLeft;
    }

}
