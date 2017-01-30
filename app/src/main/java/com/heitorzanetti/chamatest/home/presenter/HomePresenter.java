package com.heitorzanetti.chamatest.home.presenter;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.heitorzanetti.chamatest.home.model.HomeInteractor;
import com.heitorzanetti.chamatest.home.view.IHomeView;
import com.heitorzanetti.chamatest.network.ConnectivityStatusReceiver;
import com.heitorzanetti.chamatest.network.IConnectivityStatusListener;
import com.heitorzanetti.chamatest.utils.GithubProfile;

import java.util.ArrayList;

/**
 * Created by heitorzc on 25/01/2017.
 */

public class HomePresenter implements IHomePresenter, IConnectivityStatusListener {

    private IHomeView view;
    private HomeInteractor interactor;
    private ConnectivityStatusReceiver connReceiver;

    public HomePresenter(IHomeView view) {
        this.view = view;

        interactor = new HomeInteractor(this);
    }



    public void registerConnectivityStatusReceiver(Context context) {
        connReceiver = new ConnectivityStatusReceiver();
        connReceiver.register(this, context);
    }


    public void unregisterConnectivityStatusReceiver(Context context) {
        if (connReceiver != null) connReceiver.unregister(context);
    }


    public SpannableStringBuilder getWhiteString(String s) {
        return interactor.getWhiteString(s);
    }


    public void attemptRequestRepositories() {

        String user1 = view.getFirstUsername();
        String user2 = view.getSecondUsername();

        if (user1 != null && user2 != null){
            interactor.requestUserDataFromApi(user1, user2);
        }

    }


    public void cancelRequests() {
        interactor.cancellRequests();
    }




    @Override
    public void onInternetOnline() {
        view.onInternetOnline();
    }



    @Override
    public void onInternetOffline() {
        view.onInternetOffline();
    }



    @Override
    public void onAllRequestsCompleted(ArrayList<GithubProfile> userProfiles, ArrayList<Long> requestTimesMillis) {
        view.onRepoRequestsCompleted(userProfiles, requestTimesMillis);
    }



    @Override
    public void onRequestError(int code) {
        view.onRepoRequestsFailed(code);
    }



    @Override
    public void onRequestsCancelled() {
        view.onRequestsCanceled();
    }


}
