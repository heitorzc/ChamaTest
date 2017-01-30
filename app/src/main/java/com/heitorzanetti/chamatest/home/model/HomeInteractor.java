package com.heitorzanetti.chamatest.home.model;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.heitorzanetti.chamatest.Chama;
import com.heitorzanetti.chamatest.R;
import com.heitorzanetti.chamatest.home.presenter.IHomePresenter;
import com.heitorzanetti.chamatest.network.HttpRequestManager;
import com.heitorzanetti.chamatest.network.IHttpEndPoints;
import com.heitorzanetti.chamatest.network.IHttpRequestErrorCodes;
import com.heitorzanetti.chamatest.network.IHttpRequestListener;
import com.heitorzanetti.chamatest.utils.GithubProfile;
import com.heitorzanetti.chamatest.utils.ResourceManager;

import java.util.ArrayList;

/**
 * Created by heitorzc on 25/01/2017.
 */

public class HomeInteractor implements IHttpEndPoints, IHttpRequestListener, IHttpRequestErrorCodes {

    private final String TAG = "HomeInteractor";

    private ArrayList<Long> requestTimesMillis;
    private ArrayList<GithubProfile> userProfiles;

    private IHomePresenter presenter;


    public HomeInteractor(IHomePresenter presenter){
        this.presenter = presenter;
    }


    public SpannableStringBuilder getWhiteString(String s){

        ForegroundColorSpan span = new ForegroundColorSpan(ResourceManager.getInstance().color(R.color.white));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(s);
        ssBuilder.setSpan(span, 0, s.length(), 0);

        return ssBuilder;

    }


    public void requestUserDataFromApi(String user1, String user2) {

        requestTimesMillis = new ArrayList<>();
        userProfiles       = new ArrayList<>();


        if (Chama.hasInternet) {
            //Request both user's profile information and repositories from GitHub API.
            new HttpRequestManager(this)
                    .username(user1).getUserDataAndRepos()
                    .username(user2).getUserDataAndRepos();


            //The first position of the array will represent the start-time of the requests.
            requestTimesMillis.add(System.currentTimeMillis());
            return;
        }

        presenter.onRequestError(ERROR_NO_INTERNET_CONNECTION);

    }


    public void cancellRequests(){
        AndroidNetworking.forceCancelAll();
        presenter.onRequestsCancelled();
    }


    @Override
    public void onSingleRequestCompleted(GithubProfile githubProfile) {
        Log.w(TAG, "Received data for: " + githubProfile.getLogin());


        //When an user data is retrieved, add the current time to the array, so we
        //know how long it took since the beginning and how long it took between
        //both username's requests.
        requestTimesMillis.add(System.currentTimeMillis());


        userProfiles.add(githubProfile);


        //If we got all the two GithubProfiles, then go ahead and deliver the data
        //to the Presenter.
        if (userProfiles.size() >= 2) {
            presenter.onAllRequestsCompleted(userProfiles, requestTimesMillis);
        }

    }


    @Override
    public void onSingleRequestFailed(int code) {
        Log.w(TAG, "Received error code: " + code);

        userProfiles.add(null);

        if (userProfiles.size() >= 2) {
            presenter.onRequestError(code);
        }

    }


}
