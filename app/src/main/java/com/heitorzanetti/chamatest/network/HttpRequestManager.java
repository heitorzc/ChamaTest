package com.heitorzanetti.chamatest.network;

import android.util.Log;

import com.androidnetworking.common.Priority;
import com.google.gson.reflect.TypeToken;
import com.heitorzanetti.chamatest.utils.GithubProfile;
import com.heitorzanetti.chamatest.utils.GithubRepository;
import com.rxandroidnetworking.RxAndroidNetworking;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.heitorzanetti.chamatest.network.IHttpEndPoints.BASE_URL;
import static com.heitorzanetti.chamatest.network.IHttpEndPoints.GET_PROFILE_URL;
import static com.heitorzanetti.chamatest.network.IHttpEndPoints.GET_REPOS_URL;

/**
 * Created by heitorzc on 18/01/2017.
 */

public class HttpRequestManager implements IHttpRequestErrorCodes {

    private final String TAG = "HTTPREQUEST";

    private String username;
    private IHttpRequestListener   listener;


    public HttpRequestManager(IHttpRequestListener listener) {
        this.listener = listener;
    }



    public HttpRequestManager username(String username){
        this.username = username;
        return this;
    }


    public HttpRequestManager getUserDataAndRepos(){
        Log.w(TAG, "Sent request for: " + username);

        Observable.zip(getProfile(), getRepositories(), mergeReposInProfile())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GithubProfile>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onSingleRequestFailed(ERROR_API_CONNECTION);
                    }

                    @Override
                    public void onNext(GithubProfile githubProfile) {
                        listener.onSingleRequestCompleted(githubProfile);
                    }
                });

        return this;
        
    }


    private Func2<GithubProfile, ArrayList<GithubRepository>, GithubProfile> mergeReposInProfile(){
        return new Func2<GithubProfile, ArrayList<GithubRepository>, GithubProfile>() {

            @Override
            public GithubProfile call(GithubProfile githubProfile, ArrayList<GithubRepository> githubRepos) {

                githubProfile.setRepos(githubRepos);
                return githubProfile;

            }

        };
    }


    private Observable<GithubProfile> getProfile() {

       return RxAndroidNetworking.get(BASE_URL + GET_PROFILE_URL)
                .addPathParameter("username", username)
                .setPriority(Priority.HIGH)
                .build()
                .getParseObservable(new TypeToken<GithubProfile>(){});


    }


    private Observable<ArrayList<GithubRepository>> getRepositories() {

        return RxAndroidNetworking.get(BASE_URL + GET_REPOS_URL)
                .addPathParameter("username", username)
                .setPriority(Priority.HIGH)
                .build()
                .getParseObservable(new TypeToken<ArrayList<GithubRepository>>(){});

    }


}
