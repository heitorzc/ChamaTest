package com.heitorzanetti.chamatest.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.heitorzanetti.chamatest.Chama;


/**
 * Created by heitorzc on 18/01/17.
 */
public class ConnectivityStatusReceiver extends BroadcastReceiver {

    private final String TAG = "INTERNET";
    
    IConnectivityStatusListener listener;
    Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        NetworkInfo info = extras.getParcelable("networkInfo");

        if (info != null) {

            NetworkInfo.State state = info.getState();

            if (state == NetworkInfo.State.CONNECTED && !Chama.hasInternet) {
                Log.w(TAG, "---------------------------------------------------");
                Log.i(TAG, "---------------------------------------------------");
                Log.e(TAG, "--------------- INTERNET AVAILABLE ----------------");
                Log.i(TAG, "---------------------------------------------------");
                Log.w(TAG, "---------------------------------------------------");

                Chama.hasInternet = true;
                if (listener != null) listener.onInternetOnline();

            } else if (state == NetworkInfo.State.DISCONNECTED && Chama.hasInternet) {
                Log.w(TAG, "---------------------------------------------------");
                Log.i(TAG, "---------------------------------------------------");
                Log.e(TAG, "----------------- DEVICE OFFLINE ------------------");
                Log.i(TAG, "---------------------------------------------------");
                Log.w(TAG, "---------------------------------------------------");

                Chama.hasInternet = false;
                if (listener != null) listener.onInternetOffline();
            }

        }

    }


    public void register(IConnectivityStatusListener listener, Context mContext){
        this.listener = listener;
        this.mContext = mContext;

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(this, intentFilter);

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        Chama.hasInternet = (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected());

    }


    public void unregister(Context mContext){
        this.mContext = mContext;
        mContext.unregisterReceiver(this);
    }

}

