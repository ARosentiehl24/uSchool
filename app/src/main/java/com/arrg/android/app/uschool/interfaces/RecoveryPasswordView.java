package com.arrg.android.app.uschool.interfaces;

/**
 * Created by Alberto on 14-Nov-16.
 */

public interface RecoveryPasswordView {

    void configViews();

    void sendEmailTo(String email, String password);

    void showErrorPrompt(String title, String error);
}
