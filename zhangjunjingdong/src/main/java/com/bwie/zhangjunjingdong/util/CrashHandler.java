package com.bwie.zhangjunjingdong.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler crashHandler;
    private Context context;
    private Thread.UncaughtExceptionHandler defaultHandler;

    private File logDir = new File(Environment.getExternalStorageDirectory(),"crash");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

    public static CrashHandler getInstance(){

        if (crashHandler == null){
            synchronized (CrashHandler.class){
                if (crashHandler == null){
                    crashHandler = new CrashHandler();
                }
            }
        }

        return crashHandler;
    }


    public void init(Context context){
        this.context = context;


        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();


        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {


        if (!handlerException(throwable) && defaultHandler != null){

            defaultHandler.uncaughtException(thread,throwable);
        }else {


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            uploadLogToServer();

            android.os.Process.killProcess(android.os.Process.myPid());//杀死应用的进程

            System.exit(1110);
        }

    }


    private void uploadLogToServer() {



    }

    private boolean handlerException(Throwable throwable) {
        if (throwable == null){
            return false;
        }


        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,"程序出现异常,即将退出",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();


        if (! logDir.exists()){
            logDir.mkdirs();
        }


        File logFile = new File(logDir,"crash-"+dateFormat.format(new Date())+"-.log");
        if (!logFile.exists()){
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            PrintWriter printWriter = new PrintWriter(logFile);

            collectLogInfoToSdCard(printWriter,throwable);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;

    }


    private void collectLogInfoToSdCard(PrintWriter printWriter, Throwable throwable) {


        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            printWriter.print("版本:");
            printWriter.println(packageInfo.versionCode);
            printWriter.print("版本名称:");
            printWriter.println(packageInfo.versionName);

            printWriter.print("time:");
            printWriter.println(dateFormat.format(new Date()));

            throwable.printStackTrace(printWriter);

            printWriter.close();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
