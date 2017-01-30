package com.heitorzanetti.chamatest.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by heitorzc on 25/01/2017.
 */

public class GithubProfile implements Parcelable {

    private String login;
    private String name;
    private String html_url;
    private int public_repos;

    ArrayList<GithubRepository> repos;

    public ArrayList<GithubRepository> getRepos() {
        return repos;
    }

    public void setRepos(ArrayList<GithubRepository> repos) {
        this.repos = repos;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.name);
        dest.writeString(this.html_url);
        dest.writeInt(this.public_repos);
        dest.writeTypedList(this.repos);
    }

    public GithubProfile() {
    }

    protected GithubProfile(Parcel in) {
        this.login = in.readString();
        this.name = in.readString();
        this.html_url = in.readString();
        this.public_repos = in.readInt();
        this.repos = in.createTypedArrayList(GithubRepository.CREATOR);
    }

    public static final Parcelable.Creator<GithubProfile> CREATOR = new Parcelable.Creator<GithubProfile>() {
        @Override
        public GithubProfile createFromParcel(Parcel source) {
            return new GithubProfile(source);
        }

        @Override
        public GithubProfile[] newArray(int size) {
            return new GithubProfile[size];
        }
    };
}

