package com.arrg.android.app.uschool.interfaces;

/**
 * Created by Alberto on 12-Nov-16.
 */

public interface SignUpPresenter {

    void onCreate();

    void trySignUpWith(String name, String lastName, String identification, String email, String password, String repeatPassword, String phoneNumber);
}
