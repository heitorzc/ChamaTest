package com.heitorzanetti.chamatest.network;

import com.heitorzanetti.chamatest.utils.GithubProfile;

/**
 * Created by heitorzc on 18/01/2017.
 */

public interface IHttpRequestListener {
    void onSingleRequestCompleted(GithubProfile githubProfile);
    void onSingleRequestFailed(int code);
}
