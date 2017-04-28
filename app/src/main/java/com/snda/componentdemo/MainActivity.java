package com.snda.componentdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        int resId = getResources().getIdentifier("daoyu", "drawable", getPackageName());
        System.out.println("resId=" + resId);

        //int t = 0x5e020000;
        imageView.setImageResource(resId);
    }

    public void launchModule(View view){
        try{
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.snda.componentdemo", "com.snda.component.MainActivity"));
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void test(View view){
        System.out.println(getResources().getResourcePackageName(0x5e020000) + "," + getResources().getResourceTypeName(0x5e020000) + "," + getResources().getResourceName(0x5e020000));
        System.out.println(getResources().getResourcePackageName(0x5e040000) + "," + getResources().getResourceTypeName(0x5e040000) + "," + getResources().getResourceName(0x5e040000));
        System.out.println(getResources().getResourcePackageName(0x5e030000) + "," + getResources().getResourceTypeName(0x5e030000) + "," + getResources().getResourceName(0x5e030000));
        System.out.println(getResources().getResourcePackageName(0x5e050000) + "," + getResources().getResourceTypeName(0x5e050000) + "," + getResources().getResourceName(0x5e050000));
    }

}
