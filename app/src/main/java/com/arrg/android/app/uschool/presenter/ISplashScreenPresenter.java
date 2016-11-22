package com.arrg.android.app.uschool.presenter;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.VolleySingleton;
import com.arrg.android.app.uschool.interfaces.SplashScreenPresenter;
import com.arrg.android.app.uschool.interfaces.SplashScreenView;
import com.arrg.android.app.uschool.model.entity.School;
import com.arrg.android.app.uschool.model.entity.User;
import com.google.gson.Gson;
import com.munix.utilities.AESCrypt;
import com.thefinestartist.Base;
import com.thefinestartist.utils.content.Ctx;
import com.thefinestartist.utils.content.ResourcesUtil;
import com.thefinestartist.utils.preferences.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.arrg.android.app.uschool.Constants.MESSAGE;
import static com.arrg.android.app.uschool.Constants.PASSWORD;
import static com.arrg.android.app.uschool.Constants.SCHOOLS;
import static com.arrg.android.app.uschool.Constants.STATE;
import static com.arrg.android.app.uschool.Constants.STORAGE_PERMISSION_RC;
import static com.arrg.android.app.uschool.Constants.USER;
import static com.arrg.android.app.uschool.Constants.USER_LOGGED_IN;


/**
 * Created by Alberto on 05-Nov-16.
 */

public class ISplashScreenPresenter implements SplashScreenPresenter {

    private SplashScreenView splashScreenView;
    private USchool uSchool;

    public ISplashScreenPresenter(SplashScreenView splashScreenView) {
        uSchool = USchool.getInstance();

        this.splashScreenView = splashScreenView;
    }

    @Override
    public void onCreate() {
        splashScreenView.configViews();
    }

    @Override
    public String getPictureFrom(Bitmap bitmap) {
        try {
            File backgroundFile = new File(Ctx.getExternalCacheDir(), ResourcesUtil.getString(R.string.background_name));
            File backgroundPath = new File(backgroundFile.getAbsolutePath());

            if (!backgroundPath.exists()) {
                OutputStream output = new FileOutputStream(backgroundFile);

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

                output.flush();
                output.close();

                Pref.put(ResourcesUtil.getString(R.string.background_path), backgroundPath.getAbsolutePath());

                return backgroundPath.getAbsolutePath();
            } else {
                return Pref.get(ResourcesUtil.getString(R.string.background_path), "");
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<Integer> permissionResults = new ArrayList<>();
        switch (requestCode) {
            case STORAGE_PERMISSION_RC:
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);
                }

                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
                } else {
                }

                break;
        }
    }

    @Override
    public void trySignInWith(final String identification, final String password) {
        String url = uSchool.getUserById(identification);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processResponse(response, password);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                splashScreenView.showErrorPrompt("onErrorResponse", error.getLocalizedMessage());
            }
        });

        VolleySingleton.getInstance(Base.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void getSchoolList() {
        String url = uSchool.getSchoolList();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processSchoolListResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                splashScreenView.showLoadSchoolErrorPrompt("onErrorResponse", error.getLocalizedMessage());
            }
        });

        VolleySingleton.getInstance(Base.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void processSchoolListResponse(JSONObject response) {
        try {
            int state = response.getInt(STATE);
            String message = null;

            switch (state) {
                case 1:
                    ArrayList<School> schoolArrayList = new ArrayList<>();

                    Gson gson = new Gson();

                    JSONArray jsonArray = response.getJSONArray(SCHOOLS);

                    School[] schools = gson.fromJson(jsonArray.toString(), School[].class);

                    Collections.addAll(schoolArrayList, schools);

                    uSchool.setSchools(schoolArrayList);

                    if (Pref.get(USER_LOGGED_IN, false)) {
                        uSchool.setUser(Pref.get(USER, new User()));

                        splashScreenView.startSchoolListActivity(Pref.get(USER_LOGGED_IN, false));
                    } else {
                        splashScreenView.showInput(true);
                    }
                    break;
                case 2:
                    message = response.getString(MESSAGE);

                    splashScreenView.showLoadSchoolErrorPrompt(ResourcesUtil.getString(R.string.alert_error), message);

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResponse(JSONObject response, String password) {
        try {
            int state = response.getInt(STATE);
            String message;

            switch (state) {
                case 1:
                    Gson gson = new Gson();

                    JSONObject jsonObject = response.getJSONObject(USER);

                    User user = gson.fromJson(jsonObject.toString(), User.class);

                    try {
                        AESCrypt aesCrypt = new AESCrypt(PASSWORD);

                        if (password.equals(aesCrypt.decrypt(user.getPassword()))) {
                            Pref.put(USER, user);
                            Pref.put(USER_LOGGED_IN, true);

                            uSchool.setUser(user);

                            splashScreenView.startSchoolListActivity(false);
                        } else {
                            splashScreenView.showPasswordsDoNotMatchError(ResourcesUtil.getString(R.string.passwords_do_not_match));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    message = response.getString(MESSAGE);

                    splashScreenView.showErrorPrompt(ResourcesUtil.getString(R.string.alert_error), message);

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
