package com.bwie.zhangjunjingdong.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.UpLoadPicBean;
import com.bwie.zhangjunjingdong.model.bean.UserInfoBean;
import com.bwie.zhangjunjingdong.presenter.UpLoadPicPresenter;
import com.bwie.zhangjunjingdong.presenter.UserInfoPresenter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.GlideImgManager;
import com.bwie.zhangjunjingdong.view.Iview.UpLoadActivityInter;
import com.bwie.zhangjunjingdong.view.Iview.UserInforInter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener,UpLoadActivityInter,UserInforInter {

    private ImageView detail_image_back;
    private ImageView setting_icon;
    private LinearLayout linear_upload;
    private PopupWindow popupWindow;
    private TextView text_camera;
    private TextView text_local_pic;
    private TextView text_cancel;
    private View parent;

    private String pic_path = Environment.getExternalStorageDirectory()+"/head.jpg";

    private String crop_icon_path = Environment.getExternalStorageDirectory()+"/head_icon.jpg";
    private UpLoadPicPresenter upLoadPicPresenter;
    private UserInfoPresenter userInfoPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        detail_image_back = findViewById(R.id.detail_image_back);
        setting_icon = findViewById(R.id.setting_icon);
        linear_upload = findViewById(R.id.linear_upload);


        detail_image_back.setOnClickListener(this);
        linear_upload.setOnClickListener(this);

        initPopupWindown();

        initData();

        linear_upload.setOnClickListener(this);

    }

    private void initPopupWindown() {

        //父窗体的视图
        parent = View.inflate(UserInfoActivity.this, R.layout.activity_user_info,null);

        View contentView = View.inflate(UserInfoActivity.this,R.layout.upload_pop_layout,null);

        //第一个参数展示的popupwindown的视图,第二个参数宽度,第三参数是高度..
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //设置
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);//外边可以触摸
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //找到控件
        text_camera = contentView.findViewById(R.id.text_camera);
        text_local_pic = contentView.findViewById(R.id.text_local_pic);
        text_cancel = contentView.findViewById(R.id.text_cancel);

        //设置点击事件
        text_camera.setOnClickListener(this);
        text_local_pic.setOnClickListener(this);
        text_cancel.setOnClickListener(this);

    }


    private void initData() {

        //加载圆形
        GlideImgManager.glideLoader(UserInfoActivity.this, CommonUtils.getString("iconUrl"), R.drawable.user, R.drawable.user, setting_icon, 0);

        //mvp
        upLoadPicPresenter = new UpLoadPicPresenter(this);
        userInfoPresenter = new UserInfoPresenter(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_image_back:

                finish();
                break;
            case R.id.linear_upload:

                popupWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
                break;
            case R.id.text_camera://拍照

                paiZhao();

                popupWindow.dismiss();
                break;
            case R.id.text_local_pic://相册

                getLocalPic();

                popupWindow.dismiss();
                break;
            case R.id.text_cancel://取消

                popupWindow.dismiss();
                break;
        }
    }


    private void getLocalPic() {

        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent, 3000);

    }


    private void paiZhao() {

        Intent intent = new Intent();

        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(pic_path)));

        startActivityForResult(intent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            crop(Uri.fromFile(new File(pic_path)));
        }


        if (requestCode == 3000 && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            crop(uri);
        }

        if (requestCode == 2000 && resultCode == RESULT_OK) {

            Bitmap bitmap = data.getParcelableExtra("data");
            File saveIconFile = new File(crop_icon_path);

            if(saveIconFile.exists()){
                saveIconFile.delete();
            }

            try {
                saveIconFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(saveIconFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                upLoadPicPresenter.uploadPic(ApiUtil.UPLOAD_ICON_URL,saveIconFile,CommonUtils.getString("uid"),"touxiang.jpg");

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void crop(Uri uri) {

        Intent intent = new Intent();

        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //执行裁剪的指令
        intent.putExtra("crop", "true");
        //指定裁剪框的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //指定输出的时候宽度和高度
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        //设置取消人脸识别
        intent.putExtra("noFaceDetection", false);
        //设置返回数据
        intent.putExtra("return-data", true);

        //
        startActivityForResult(intent, 2000);

    }

    @Override
    public void uploadPicSuccess(UpLoadPicBean upLoadPicBean) {
        if ("0".equals(upLoadPicBean.getCode())) {
            Toast.makeText(UserInfoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();

            //上传成功之后需要获取用户信息,,,
            userInfoPresenter.getUserInfo(ApiUtil.USER_INFO_URL,CommonUtils.getString("uid"));


        }
    }

    @Override
    public void onUserInforSuccess(UserInfoBean userInfoBean) {
        if ("0".equals(userInfoBean.getCode())) {

            //获取用户信息成功....拿到icon在服务器上的路径,然后加载显示头像
            GlideImgManager.glideLoader(UserInfoActivity.this, userInfoBean.getData().getIcon(), R.drawable.user, R.drawable.user, setting_icon, 0);

            //并且需要保存icon的新路径
            CommonUtils.saveString("iconUrl",userInfoBean.getData().getIcon());
        }
    }
}
