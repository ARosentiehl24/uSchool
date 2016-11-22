package com.arrg.android.app.uschool;

import android.Manifest;

/**
 * Created by Alberto on 14-Nov-16.
 */

public class Constants {
    public static String PACKAGE_NAME;
    public static String SETTINGS_PREFERENCES;

    public static final String STATE = "state";
    public static final String MESSAGE = "message";
    public static final String USER = "user";
    public static final String SCHOOL = "school";
    public static final String SCHOOLS = "schools";
    public static final String IDENTIFICATION = "identification";
    public static final String PASSWORD = "password";

    public static final String USER_LOGGED_IN = "user_logged_in";

    public static final int STORAGE_PERMISSION_RC = 100;
    public static final String STORAGE_PERMISSION[] = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final int LOCATION_PERMISSION_RC = 101;
    public static final String LOCATION_PERMISSION[] = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    public static final int SPEECH_INPUT_RC = 200;

    public static String ACCOUNT_SID = "ACb652f609f26462260f505857408839f7";
    public static String AUTH_TOKEN = "086a66256efe555a3fb1ba48f79c1f2c";

}
