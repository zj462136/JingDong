package com.bwie.jingdong.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.jingdong.R;
import com.bwie.jingdong.activity.GoodsActivity;
import com.bwie.jingdong.activity.LoginActivity;
import com.bwie.jingdong.activity.WoActivity;
import com.bwie.jingdong.adapter.JianAdapter;
import com.bwie.jingdong.bean.ShouYeBean;
import com.bwie.jingdong.bean.UserBean;
import com.bwie.jingdong.inter.ItemClickListener;
import com.bwie.jingdong.util.CommonUtils;
import com.bwie.jingdong.util.ImageUtils;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/4.
 */

public class FragmenMy extends Fragment {

    private TextView text_login;
    private ImageView image_touxiang;
    private RecyclerView tuijian;
    private String username;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        text_login = view.findViewById(R.id.text_login);
        image_touxiang = view.findViewById(R.id.image_touxiang);
        tuijian = view.findViewById(R.id.tuijian);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTuijian();
        username = getActivity().getIntent().getStringExtra("username");
        tuijian.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        boolean islogin = sp.getBoolean("islogin", false);
        Log.d("TAG",islogin+"");
        if(islogin){
            //如果登录了，要怎么处理
            final String username = sp.getString("username", "");
            text_login.setText(username);
            image_touxiang.setImageResource(R.drawable.jing);
            text_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), WoActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
            });
        }else {
            text_login.setText("登录/注册>");
            image_touxiang.setImageResource(R.drawable.user);
            text_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //没有登录怎么处理
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //得到从相册中选择的照片
        if (requestCode == 200) {
            //Uri:统一资源定位符
            Uri imgPath = data.getData();
            //img.setImageURI(imgPath);
            crop(imgPath); //调用裁剪的方法
        }
        if (requestCode == 9999) {
            //得到裁剪后的图片并显示
            Bitmap bi = data.getParcelableExtra("data");
            Bitmap bitmap = ImageUtils.toRoundBitmap(bi);
            image_touxiang.setImageBitmap(bitmap);
            File file = new File(Environment.getExternalStorageDirectory(), "abc.jpg");
            Map<String, String> params = new HashMap<>();
            params.put("uid", "2797");


            OkHttp3Util.uploadFile("https://www.zhaoapi.cn/file/upload", file, "dash.jpg", params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        //此时上传成功....获取用户信息
                        OkHttp3Util.doGet("https://www.zhaoapi.cn/user/getUserInfo?uid=2797", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //子线程
                                if (response.isSuccessful()) {
                                    String json = response.body().string();

                                    //解析出icon字段...使用glide加载上传到网络上这个图片
                                    final UserBean userBean = new Gson().fromJson(json, UserBean.class);


                                    CommonUtils.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Glide.with(getActivity()).load(userBean.getData().getIcon()).into(image_touxiang);

                                        }
                                    });

                                }

                            }
                        });

                    }
                }
            });
        }
    }

        //裁剪图片
    private void crop(Uri uri) {

        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", false);// 取消人脸识别
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 9999);
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }





    private void getTuijian() {
        OkHttp3Util.doGet("https://www.zhaoapi.cn/ad/getAd", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final ShouYeBean shouYeBean = new Gson().fromJson(json, ShouYeBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            final ShouYeBean.TuijianBean tuijian = shouYeBean.getTuijian();
                            JianAdapter tuiJianAdapter = new JianAdapter(getActivity(), tuijian);
                            FragmenMy.this.tuijian.setAdapter(tuiJianAdapter);
                            tuiJianAdapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void setItemClickListener(int position) {
                                    int pid = tuijian.getList().get(position).getPid();
                                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                                    intent.putExtra("pid", pid);
                                    startActivity(intent);
                                }

                                @Override
                                public void setItemLongClickListener(int position) {
                                    int pid = tuijian.getList().get(position).getPid();
                                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                                    intent.putExtra("pid", pid);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}