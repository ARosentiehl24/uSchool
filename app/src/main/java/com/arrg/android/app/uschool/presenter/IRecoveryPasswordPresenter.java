package com.arrg.android.app.uschool.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.VolleySingleton;
import com.arrg.android.app.uschool.interfaces.RecoveryPasswordPresenter;
import com.arrg.android.app.uschool.interfaces.RecoveryPasswordView;
import com.arrg.android.app.uschool.model.entity.User;
import com.google.gson.Gson;
import com.munix.utilities.AESCrypt;
import com.thefinestartist.Base;

import org.json.JSONException;
import org.json.JSONObject;

import static com.arrg.android.app.uschool.Constants.MESSAGE;
import static com.arrg.android.app.uschool.Constants.PASSWORD;
import static com.arrg.android.app.uschool.Constants.STATE;
import static com.arrg.android.app.uschool.Constants.USER;

/**
 * Created by Alberto on 14-Nov-16.
 */

public class IRecoveryPasswordPresenter implements RecoveryPasswordPresenter {

    private RecoveryPasswordView recoveryPasswordView;
    private USchool uSchool;

    public IRecoveryPasswordPresenter(RecoveryPasswordView recoveryPasswordView) {
        this.recoveryPasswordView = recoveryPasswordView;
    }

    @Override
    public void onCreate() {
        uSchool = USchool.getInstance();

        recoveryPasswordView.configViews();
    }

    @Override
    public void requestPasswordRecovery(String email) {
        String url = uSchool.getUserById(email);

        Log.e(getClass().getSimpleName(), url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recoveryPasswordView.showErrorPrompt("onErrorResponse", error.getLocalizedMessage());
            }
        });

        VolleySingleton.getInstance(Base.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void processResponse(JSONObject response) {
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
                        String password = aesCrypt.decrypt(user.getPassword());

                        recoveryPasswordView.sendEmailTo(user.getEmail(), password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    message = response.getString(MESSAGE);

                    recoveryPasswordView.showErrorPrompt(uSchool.getString(R.string.alert_error), message);

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
