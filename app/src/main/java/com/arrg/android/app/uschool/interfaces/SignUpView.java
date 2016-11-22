package com.arrg.android.app.uschool.interfaces;

/**
 * Created by Alberto on 12-Nov-16.
 */

public interface SignUpView {

    void configViews();

    void showPrompt(String title, String message, int dialogType);

    void showPasswordsDoNotMatchError(String errorMessage);

    void finishActivity();
}
