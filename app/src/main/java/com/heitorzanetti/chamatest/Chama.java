package com.heitorzanetti.chamatest;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.heitorzanetti.chamatest.utils.ResourceManager;


/**
 * Created by heitorzc on 19/01/2017.
 */

public class Chama extends Application {

    public static boolean hasInternet = false;

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(this);
        ResourceManager.init(this);
    }

}
