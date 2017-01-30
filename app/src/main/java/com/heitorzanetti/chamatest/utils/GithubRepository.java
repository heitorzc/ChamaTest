package com.heitorzanetti.chamatest.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by heitorzc on 25/01/2017.
 */

public class GithubRepository implements Parcelable {

    private String name;
    private String created_at;
    private String html_url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.created_at);
        dest.writeString(this.html_url);
    }

    public GithubRepository() {
    }

    protected GithubRepository(Parcel in) {
        this.name = in.readString();
        this.created_at = in.readString();
        this.html_url = in.readString();
    }

    public static final Creator<GithubRepository> CREATOR = new Creator<GithubRepository>() {
        @Override
        public GithubRepository createFromParcel(Parcel source) {
            return new GithubRepository(source);
        }

        @Override
        public GithubRepository[] newArray(int size) {
            return new GithubRepository[size];
        }
    };
}


