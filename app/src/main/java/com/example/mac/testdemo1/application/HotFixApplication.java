package com.example.mac.testdemo1.application;

import android.app.Application;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.IOException;

/**
 * Created by mac on 16/9/13.
 */
public class HotFixApplication extends Application {
    private PatchManager patchManager;
    private static HotFixApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application= (HotFixApplication) getApplicationContext();
        patchManager = new PatchManager(getApplicationContext());
        patchManager.init("1.0");//对应版本
        patchManager.loadPatch();
    }

    public static HotFixApplication getInstance(){
        return application;
    }
    public void addpatch(String patch){
        try {
            patchManager.addPatch(patch);
            patchManager.removeAllPatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
