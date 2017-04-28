package com.snda.componentdemo;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;


import com.snda.componentdemo.reflect.FieldUtils;
import com.snda.componentdemo.reflect.MethodUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * Created by liuxiaofeng02 on 2017/1/10.
 */

public class DemoApplication extends Application{
    public static final String ModuleName = "module1.apk";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //ModuleInstaller.initPathFromAssets(this, ModuleName);

        ModuleClassLoader classLoader = new ModuleClassLoader(base.getClassLoader());
        classLoader.addAssets(this, ModuleName);
        try{
            Object object = ActivityThreadCompat.currentActivityThread();
            if(null != object){
                Object mPackagesObj = FieldUtils.readField(object, "mPackages");
                Object loadedApkWeakRef = MethodUtils.invokeMethod(mPackagesObj, "get", getPackageName());
                Object loadedApk = MethodUtils.invokeMethod(loadedApkWeakRef, "get");
                if(null != loadedApk){
                    FieldUtils.writeDeclaredField(loadedApk, "mClassLoader", classLoader);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Thread.currentThread().setContextClassLoader(classLoader);

        try{
            AssetManager assetManager = getAssets();
            Method method = AssetManager.class.getMethod("addAssetPath", new Class[]{String.class});
            method.setAccessible(true);
            File dexDir = new File(getFilesDir(), "module");
            String dexPath = dexDir.getAbsolutePath() + File.separator + DemoApplication.ModuleName;
            File dexFile = new File(dexPath);
            System.out.println("dexPath=" + dexFile.exists());
            method.invoke(assetManager, dexPath);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
