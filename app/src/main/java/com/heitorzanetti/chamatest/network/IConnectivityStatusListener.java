package com.heitorzanetti.chamatest.network;

/**
 * Created by heitorzc on 19/01/2017.
 */

public interface IConnectivityStatusListener {
    void onInternetOnline();
    void onInternetOffline();
}
