package com.bwie.jingdong.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.dash.zxinglibrary.activity.CaptureFragment;
import com.dash.zxinglibrary.activity.CodeUtils;
import com.dash.zxinglibrary.util.ImageUtil;

public class CustomActivity extends AppCompatActivity implements View.OnClickListener {
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {//解析成功
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CustomActivity.this.setResult(RESULT_OK, resultIntent);
            CustomActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {//解析失败
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CustomActivity.this.setResult(RESULT_OK, resultIntent);
            CustomActivity.this.finish();
        }
    };

//    private boolean flag;
    private ImageView image_xiangce;
    private TextView text_xiangce;
    private static final int REQUEST_IMAGE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        CaptureFragment captureFragment = new CaptureFragment();
        //给扫描的fragment定制一个页面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, captureFragment).commit();

//        flag = false;
//        //闪光灯
//        findViewById(R.id.linear1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!flag) {
//                    //打开
//                    CodeUtils.isLightEnable(true);
//                    flag = true;
//                } else {
//                    CodeUtils.isLightEnable(false);
//                    flag = false;
//                }
//            }
//        });
        image_xiangce = findViewById(R.id.image_xiangce);
        text_xiangce = findViewById(R.id.text_xiangce);
        image_xiangce.setOnClickListener(this);
        text_xiangce.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE){
            //取回选中的那种图片uri路径
            Uri uri = data.getData();

            try {
                //解析图片的方法...ImageUtil.getImageAbsolutePath(this, uri)通过uri路径得到图片在手机中的绝对路径
                CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                    //Bitmap mBitmap 解析的那张图片, String result解析的结果
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        Toast.makeText(CustomActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        Toast.makeText(CustomActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
