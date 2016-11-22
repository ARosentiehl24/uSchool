package com.arrg.android.app.uschool.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Alberto on 05-Nov-16.
 */

public interface SplashScreenPresenter {
    void onCreate();

    String getPictureFrom(Bitmap bitmap);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void trySignInWith(String identification, String password);

    void getSchoolList();
}
