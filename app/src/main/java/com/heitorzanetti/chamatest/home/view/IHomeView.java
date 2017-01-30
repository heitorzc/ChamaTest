package com.heitorzanetti.chamatest.home.view;

import com.heitorzanetti.chamatest.utils.GithubProfile;

import java.util.ArrayList;

/**
 * Created by heitorzc on 25/01/2017.
 */

public interface IHomeView {
    String getFirstUsername();
    String getSecondUsername();
    void onRepoRequestsCompleted(ArrayList<GithubProfile> userProfieles, ArrayList<Long> requestTimesMillis);
    void onRepoRequestsFailed(int code);
    void onRequestsCanceled();
    void onInternetOnline();
    void onInternetOffline();
}
