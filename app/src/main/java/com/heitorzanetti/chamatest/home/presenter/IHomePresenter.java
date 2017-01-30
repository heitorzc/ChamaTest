package com.heitorzanetti.chamatest.home.presenter;


import com.heitorzanetti.chamatest.utils.GithubProfile;

import java.util.ArrayList;

/**
 * Created by heitorzc on 25/01/2017.
 */

public interface IHomePresenter {
    void onAllRequestsCompleted(ArrayList<GithubProfile> userProfiles, ArrayList<Long> requestTimesMillis);
    void onRequestError(int code);
    void onRequestsCancelled();
}
