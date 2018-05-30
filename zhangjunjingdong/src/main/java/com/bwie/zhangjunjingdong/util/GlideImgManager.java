package com.bwie.zhangjunjingdong.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlideImgManager {

    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {

        Glide.with(context).load(url).apply(new RequestOptions().placeholder(emptyImg).error(erroImg)).into(iv);
    }


    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(url).apply(new RequestOptions().placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context))).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(url).apply(new RequestOptions().placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context,10))).into(iv);
        }
    }


}
