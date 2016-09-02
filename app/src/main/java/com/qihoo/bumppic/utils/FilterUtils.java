package com.qihoo.bumppic.utils;

import android.content.Context;
import android.graphics.Color;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

/**
 * Created by hacker on 16/9/2.
 */
public class FilterUtils {

    private Context context;
    public FilterUtils(Context context){
        this.context = context;
    }

    public DrawableRequestBuilder[] getFilter(String path){
        DrawableRequestBuilder arb1 =
        Glide.with(context).load(path)
                .bitmapTransform(new BlurTransformation(context));

        DrawableRequestBuilder arb2 =
        Glide.with(context).load(path)
                .bitmapTransform(new CropCircleTransformation(context));

        DrawableRequestBuilder arb3 =
        Glide.with(context).load(path)
                .bitmapTransform(new BlurTransformation(context, 25));

//        DrawableRequestBuilder arb4 =
//        Glide.with(context).load(path)
//                .bitmapTransform(new ColorFilterTransformation(context, Color.BLUE));
//
//        DrawableRequestBuilder arb5 =
//        Glide.with(context).load(path)
//                .bitmapTransform(new ColorFilterTransformation(context, Color.YELLOW));
//
//        DrawableRequestBuilder arb6 =
//        Glide.with(context).load(path)
//                .bitmapTransform(new ColorFilterTransformation(context, Color.RED));

        DrawableRequestBuilder arb7 =
        Glide.with(context).load(path)
                .bitmapTransform(new GrayscaleTransformation(context));

        DrawableRequestBuilder arb8 =
        Glide.with(context).load(path)
                .bitmapTransform(new MaskTransformation(context,1));
                  
        DrawableRequestBuilder arb9 =
        Glide.with(context).load(path)
                .bitmapTransform(new SketchFilterTransformation(context));

        DrawableRequestBuilder arb10 =
        Glide.with(context).load(path)
                .bitmapTransform(new PixelationFilterTransformation(context));

        DrawableRequestBuilder arb11 =
        Glide.with(context).load(path)
                .bitmapTransform(new ToonFilterTransformation(context));

        DrawableRequestBuilder arb12 =
        Glide.with(context).load(path)
                .bitmapTransform(new SepiaFilterTransformation(context));
                  
        DrawableRequestBuilder arb13 =
        Glide.with(context).load(path)
                .bitmapTransform(new InvertFilterTransformation(context));

        DrawableRequestBuilder arb14 =
        Glide.with(context).load(path)
                .bitmapTransform(new SwirlFilterTransformation(context));

        DrawableRequestBuilder arb15 =
        Glide.with(context).load(path)
                .bitmapTransform(new BrightnessFilterTransformation(context));

        DrawableRequestBuilder arb16 =
        Glide.with(context).load(path)
                .bitmapTransform(new VignetteFilterTransformation(context));
        DrawableRequestBuilder[] result = new  DrawableRequestBuilder[]{
                arb1,arb2,arb3,/*arb4,arb5,arb6,*/arb7,arb8,
                arb9,arb10,arb11,arb12,arb13,arb14,arb15,arb16
        };
        return result;
    }
}
