package com.heitorzanetti.chamatest.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;


/**
 * Created by heitorzc on 10/06/16.
 */
public class ResourceManager {

    private Application application;
    private static ResourceManager instance;

    private ResourceManager(Application application) {
        this.application = application;
    }




    public static ResourceManager init(Application application) {

        if (instance == null) {
            instance = new ResourceManager(application);
        }

        return instance;
    }

    public static ResourceManager getInstance(){
        return instance;
    }



    /**
     * Method getColor(int color) is deprecated on API 23+. Should use getColor(int color, Theme theme) instead.
     * @return color from resourses.
     */
    @SuppressLint("NewApi")
    public int color(int res){
        if (Build.VERSION.SDK_INT < 23){
            return application.getResources().getColor(res);
        }
        else{
            return application.getResources().getColor(res, null);
        }
    }



    /**
     * Method getDrawable(int drawable) is deprecated on API 22+. Should use getDrawable(int drawable, Theme theme) instead.
     * @return color from resourses.
     */
    @SuppressLint("NewApi")
    public Drawable drawable(int res){
        if (Build.VERSION.SDK_INT < 22){
            return application.getResources().getDrawable(res);
        }
        else{
            return application.getResources().getDrawable(res, null);
        }
    }



    /**
     * Method setBackgroundDrawable(int drawable) is deprecated on API 16+. Should use setBackground(int drawable) instead.
     * @return color from resourses.
     */
    @SuppressLint("NewApi")
    public void setBackgroundDrawable(View iv, int resource){
        if (Build.VERSION.SDK_INT < 16){
            iv.setBackgroundDrawable(drawable(resource));
        }
        else {
            iv.setBackground(drawable(resource));
        }
    }



    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }



    public int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }


}
