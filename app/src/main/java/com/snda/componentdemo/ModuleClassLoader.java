package com.snda.componentdemo;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by liuxiaofeng02 on 2017/3/4.
 */

public class ModuleClassLoader extends ClassLoader {
    private List<ClassLoader> list = new ArrayList<>(1);

    protected ModuleClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void add(ClassLoader classLoader){
        list.add(classLoader);
    }

    public void addAssets(Context context, String assetName){
        File dexDir = new File(context.getFilesDir(), "module");
        dexDir.mkdir();
        File optDir = new File(dexDir, "opt");
        if (!optDir.exists() && !optDir.mkdirs()) {// make directory fail
        }

        String dexPath = null;
        try {
            dexPath = copyAsset(context, assetName, dexDir);
        } catch (IOException e) {
        } finally {
            if (dexPath != null && new File(dexPath).exists()) {
                DexClassLoader dexClassLoader = new DexClassLoader(dexPath, optDir.getAbsolutePath(), null, new ClassLoader(){

                });
                add(dexClassLoader);
            }
        }
    }

    public static String copyAsset(Context context, String assetName, File dir) throws IOException {
        File outFile = new File(dir, assetName);
        if (!outFile.exists()) {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(assetName);
            OutputStream out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.close();
        }
        return outFile.getAbsolutePath();
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class c = null;
        try{
            c = super.loadClass(name);
        } catch (ClassNotFoundException e){
            // ClassNotFoundException thrown if class not found
            // from the non-null parent class loader
        }

        if(null == c){
            for(ClassLoader classLoader : list){
                try{
                    c = classLoader.loadClass(name);
                    if(null != c){
                        break;
                    }
                } catch (ClassNotFoundException e){
                    //continue loop
                }
            }
        }

        if(null == c){
            throw new ClassNotFoundException("Class[" + name + "] is not found.");
        }

        return c;
    }
}
