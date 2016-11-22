package com.arrg.android.app.uschool.interfaces;

/**
 * Created by Alberto on 05-Nov-16.
 */

public interface SplashScreenView {
    void configViews();

    void showMessage(String message);

    void showInput(boolean showInput);

    void showLoadSchoolErrorPrompt(String title, String error);

    void showErrorPrompt(String title, String message);

    void showPasswordsDoNotMatchError(String errorMessage);

    void finishActivity();

    void startSchoolListActivity(boolean userLoggedIn);
}
