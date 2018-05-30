package com.bwie.jingdong.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.WoBean;
import com.bwie.jingdong.presenter.WoPresenter;
import com.bwie.jingdong.view.IWoView;

import java.io.File;

public class WoActivity extends AppCompatActivity implements IWoView, View.OnClickListener {

    private TextView text_username;
    private WoPresenter woPresenter;
    private WoBean woBean;
    private ImageView image_touxiang;
    private View contentView;
    private PopupWindow popupWindow;
    private Button button;
    private View parent;
    private ImageView image_zhanshi;
    String path= File.separator + "IMG_" + System.currentTimeMillis() + ".jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo);
        text_username = findViewById(R.id.text_username);
        woPresenter = new WoPresenter(this);
        image_touxiang = findViewById(R.id.image_touxiang);
        image_zhanshi = findViewById(R.id.image_zhanshi);
        String name = getIntent().getStringExtra("username");
        contentView = View.inflate(WoActivity.this, R.layout.pop_layout, null);
        //父窗体
        parent = View.inflate(WoActivity.this, R.layout.activity_wo, null);
        //通过构造方法创建一个popupWindown
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        /**
         * 出现的问题,,,点击周围不消失,,点击返回键直接退出这个activity...里面的editText控件不能输入
         */
        popupWindow.setTouchable(true);//设置窗体可以触摸,,,默认就是true
        popupWindow.setFocusable(true);//让窗体获取到焦点...一般情况下窗体里面的控件都能获取到焦点,但是editText特殊

        popupWindow.setOutsideTouchable(true);//设置窗体外部可以触摸
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置背景

        //popupWindown里面的控件怎么去处理?
        image_touxiang.setOnClickListener(this);
        image_zhanshi.setOnClickListener(this);
        text_username.setText(name);
//        image_touxiang.setImageResource(Integer.parseInt(woBean.getData().getIcon()));
    }

    public void tuichu(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定要退出吗");
        builder.setNegativeButton("取消", null);//取消按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences login = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor edit = login.edit();
                edit.putBoolean("islogin",false);
                edit.putString("username","");
                edit.commit();
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void getSuccess(final WoBean woBean) {
        this.woBean=woBean;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WoBean.DataBean data = woBean.getData();
//                text_username.setText(data.getUsername());
                Glide.with(WoActivity.this)
                        .load(data.getIcon())
//                        .placeholder(R.mipmap.ic_launcher)
                        .into(image_touxiang);
            }
        });
    }

    public void paizhao(View view) {
        if (ContextCompat.checkSelfPermission(WoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //不允许...的时候,,,请求用户允许这个权限
            // Activity arg0代表当前的activity, @NonNull String[] arg1请求的权限的数组,也就是需要请求允许哪些权限, int arg2请求码
            ActivityCompat.requestPermissions(WoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);

        }else {
            //允许...进行跳转
            tiaoZhuan();
        }
    }

    private void tiaoZhuan() {
        Intent intent = new Intent();
        //指定动作...拍照的动作 CAPTURE...捕获
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        //给相机传递一个指令,,,告诉他拍照之后保存..MediaStore.EXTRA_OUTPUT向外输出的指令,,,指定存放的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));

        //拍照的目的是拿到拍的图片
        startActivityForResult(intent, 1000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            //给imageView设置保存的图片

            image_touxiang.setImageURI(Uri.fromFile(new File(path)));
        }
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            //获取的是相册里面某一张图片的uri地址
            Uri uri = data.getData();

            //imageView.setImageURI(uri);

            //根据这个uri地址进行裁剪
            crop(uri);
        }

        if (requestCode == 2000 && resultCode == RESULT_OK) {
            //拿到裁剪完的bitmap
            Bitmap bitmap = data.getParcelableExtra("data");

            image_touxiang.setImageBitmap(bitmap);
        }
    }
    public void bendi(View view) {
        Intent intent = new Intent();
        //指定选择/获取的动作...PICK获取,拿
        intent.setAction(Intent.ACTION_PICK);
        //指定获取的数据的类型
        intent.setType("image/*");

        startActivityForResult(intent, 1002);
    }
    /**
     * 根据图片的uri路径进行
     * @param fromFile
     */
    private void crop(Uri fromFile) {
        Intent intent = new Intent();

        //指定裁剪的动作
        intent.setAction("com.android.camera.action.CROP");

        //设置裁剪的数据(uri路径)....裁剪的类型(image/*)
        intent.setDataAndType(fromFile, "image/*");

        //执行裁剪的指令
        intent.putExtra("crop", "true");
        //指定裁剪框的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //指定输出的时候宽度和高度
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        //设置取消人脸识别
        intent.putExtra("noFaceDetection", false);
        //设置返回数据
        intent.putExtra("return-data", true);

        //
        startActivityForResult(intent, 2000);

    }

    public void quxiao(View view) {
        popupWindow.dismiss();
    }

    @Override
    public void onClick(View view) {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}
