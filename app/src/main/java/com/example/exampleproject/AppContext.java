package com.example.exampleproject;

import com.example.exampleproject.base.BaseApplication;
import com.tencent.bugly.Bugly;

/**
 * Created by chang on 2017/2/23.
 */

public class AppContext extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initBugly();
    }

    private void init(){
        requestPermission(new String[]{"android.permission.READ_PHONE_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE"});
    }

    private void initBugly(){
        Bugly.init(getApplicationContext(), "24a02b61a6", false);
    }

}
