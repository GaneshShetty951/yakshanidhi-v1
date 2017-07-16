package com.yakshanidhi.art;

import android.app.Application;

/**
 * Created by ganesh on 2/7/17.
 */

public class Yakshanidhi extends Application{
    private static Yakshanidhi yakshanidhi;
    @Override
    public void onCreate() {
        super.onCreate();
        yakshanidhi=this;
    }

    public static synchronized Yakshanidhi getInstance()
    {
        return yakshanidhi;
    }
}
