package com.heitorzanetti.chamatest.home.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.heitorzanetti.chamatest.BuildConfig;
import com.heitorzanetti.chamatest.R;
import com.heitorzanetti.chamatest.home.model.HomeAdapter;
import com.heitorzanetti.chamatest.home.presenter.HomePresenter;
import com.heitorzanetti.chamatest.network.IHttpRequestErrorCodes;
import com.heitorzanetti.chamatest.utils.GithubProfile;
import com.heitorzanetti.chamatest.widgets.OpenSansTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IHomeView, IHttpRequestErrorCodes, DialogInterface.OnCancelListener{
    @BindString(R.string.blank_username_error)  String blank_username_error;
    @BindString(R.string.request_total)         String request_time;
    @BindString(R.string.please_wait)           String progress_title;
    @BindString(R.string.progress_msg)          String progress_msg;
    @Bind(R.id.toolbar)                         Toolbar toolbar;
    @Bind(R.id.etUser1)                         EditText user1;
    @Bind(R.id.etUser2)                         EditText user2;
    @Bind(R.id.llEmptyMessage)                  LinearLayout llEmptyMessage;
    @Bind(R.id.rvRepoList)                      RecyclerView rvRepoList;
    @Bind(R.id.tvRequestTime)                   OpenSansTextView tvRequestTime;


    private HomePresenter presenter;
    private ProgressDialog progressDialog;
    private Snackbar connMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        connMessage = Snackbar.make(getWindow().getDecorView(), R.string.waiting_internet_msg, Snackbar.LENGTH_INDEFINITE);

        presenter = new HomePresenter(this);
        presenter.registerConnectivityStatusReceiver(this);


        //Insert two usernames if running in debug mode. So we don't need to type them every time.
        if (BuildConfig.DEBUG) {
            user1.setText("GrenderG");
            user2.setText("amitshekhariitbhu");
        }

    }



    /**
     * Show a SnackBar to notify the users that the device is off-line.
     */
    private void showConnMessage(){
        if (connMessage != null) connMessage.show();
    }



    /**
     * Dismiss the SnackBar that notifies the users that the device is off-line.
     */
    private void dismissConnMessage(){
        if (connMessage != null && connMessage.isShown()){
            connMessage.dismiss();
        }
    }



    /**
     * Attempt to request data from the Github API.
     */
    @OnClick(R.id.fab)
    public void onClickFab(){

        progressDialog  = ProgressDialog.show(this, progress_title, progress_msg, true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);

        presenter.attemptRequestRepositories();

        //Hide Keyboard after pressing the search button.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }



    /**
     * Gets the first Github username provided by the user. If it's blank, show an error message.
     * @return the typed username.
     */
    @Override
    public String getFirstUsername() {

        String text = user1.getText().toString();

        if (!text.isEmpty()){
            return text;
        }

        user1.setError(presenter.getWhiteString(blank_username_error));
        if (progressDialog.isShowing()) progressDialog.dismiss();

        return null;
    }



    /**
     * Gets the second Github username provided by the user. If it's blank, show an error message.
     * @return the typed username.
     */
    @Override
    public String getSecondUsername() {

        String text = user2.getText().toString();

        if (!text.isEmpty()){
            return text;
        }

        user2.setError(presenter.getWhiteString(blank_username_error));
        if (progressDialog.isShowing()) progressDialog.dismiss();
        return null;

    }


    /**
     * ProgressDialog's listener. Called when the progress dialog is dismissed.
     */
    @Override
    public void onCancel(DialogInterface dialogInterface) {
        presenter.cancelRequests();
    }



    /**
     * Method called when both usernames requests are completed so the UI can be updated.
     */
    @Override
    public void onRepoRequestsCompleted(ArrayList<GithubProfile> userProfiles, ArrayList<Long> requestTimesMillis) {

        String request_waiting = String.valueOf(requestTimesMillis.get(2) - requestTimesMillis.get(1));
        String request_total   = String.valueOf(requestTimesMillis.get(2) - requestTimesMillis.get(0));


        //Show analytics about the requests.
        tvRequestTime.setText(request_time.replace("%W", request_waiting).replace("%T", request_total));
        tvRequestTime.setVisibility(View.VISIBLE);

        //Hide the placeholder view.
        llEmptyMessage.setVisibility(View.GONE);

        //Prepare the list to receive data.
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvRepoList.setLayoutManager(mLayoutManager);
        rvRepoList.setHasFixedSize(true);

        //Load users data into the list.
        HomeAdapter adapter = new HomeAdapter(this, userProfiles);
        rvRepoList.setAdapter(adapter);

        progressDialog.dismiss();

    }



    /**
     * Called if the Github requests has failed.
     * @param code Error code from {@link IHttpRequestErrorCodes}
     */
    @Override
    public void onRepoRequestsFailed(int code) {
        Log.w("RERERE", "FAILED");

        progressDialog.dismiss();

        int msg = (code == ERROR_API_CONNECTION) ? R.string.api_error_msg : R.string.internet_error_msg;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setNeutralButton(R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }



    /**
     * Called when user cancels the requests by dismissing the progressDialog.
     */
    @Override
    public void onRequestsCanceled() {
        Snackbar.make(getWindow().getDecorView(), R.string.aborted, Snackbar.LENGTH_SHORT).show();
    }



    /**
     * Called every time an Internet connection gets active.
     */
    @Override
    public void onInternetOnline() {
        dismissConnMessage();
    }



    /**
     * Called every time the device lost connection to the Internet.
     */
    @Override
    public void onInternetOffline() {
        showConnMessage();
    }



    /**
     * Unregister the connectivity Broadcast Receiver before destroying the activity.
     */
    @Override
    protected void onDestroy() {
        presenter.unregisterConnectivityStatusReceiver(this);
        super.onDestroy();
    }


}