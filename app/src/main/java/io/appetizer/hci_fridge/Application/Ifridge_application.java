package io.appetizer.hci_fridge.Application;

import android.app.Application;

import com.mob.MobSDK;

/**
 * Created by user on 2018/6/4.
 */

public class Ifridge_application extends Application {
    @Override
    public void onTerminate(){
        super.onTerminate();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        MobSDK.init(this,"25ce2124a4674","abd70af559c5ad9d4f83d5efc4b1bfa7");
    }
}
