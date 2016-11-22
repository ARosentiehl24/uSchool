package com.arrg.android.app.uschool.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.VolleySingleton;
import com.arrg.android.app.uschool.interfaces.SignUpPresenter;
import com.arrg.android.app.uschool.interfaces.SignUpView;
import com.munix.utilities.AESCrypt;
import com.thefinestartist.Base;
import com.thefinestartist.utils.content.Res;
import com.thefinestartist.utils.content.ResourcesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.arrg.android.app.uschool.Constants.MESSAGE;
import static com.arrg.android.app.uschool.Constants.PASSWORD;
import static com.arrg.android.app.uschool.Constants.STATE;

/**
 * Created by Alberto on 12-Nov-16.
 */

public class ISignUpPresenter implements SignUpPresenter {

    private SignUpView signUpView;
    private USchool uSchool;

    public ISignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    @Override
    public void onCreate() {
        uSchool = USchool.getInstance();

        signUpView.configViews();
    }

    @Override
    public void trySignUpWith(String name, String lastName, String identification, String email, String password, final String repeatPassword, String phoneNumber) {
        if (password.equals(repeatPassword)) {
            String url = uSchool.createUser();

            try {
                AESCrypt aesCrypt = new AESCrypt(PASSWORD);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("lastName", lastName);
                hashMap.put("identification", identification);
                hashMap.put("email", email);
                hashMap.put("password", aesCrypt.encrypt(password));
                hashMap.put("phoneNumber", phoneNumber);

                JSONObject jsonObject = new JSONObject(hashMap);

                Log.e(getClass().getSimpleName(), url);
                Log.e(getClass().getSimpleName(), jsonObject.toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        signUpView.showPrompt("onErrorResponse", error.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                };
                VolleySingleton.getInstance(Base.getContext()).addToRequestQueue(jsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            signUpView.showPasswordsDoNotMatchError(ResourcesUtil.getString(R.string.passwords_do_not_match));

            signUpView.showPrompt(Res.getString(R.string.alert_error), ResourcesUtil.getString(R.string.passwords_do_not_match_message), SweetAlertDialog.ERROR_TYPE);
        }
    }

    private void processResponse(JSONObject response) {
        Log.e(getClass().getSimpleName(), "onResponse: " + response.toString());

        try {
            int state = response.getInt(STATE);
            String message = response.getString(MESSAGE);

            switch (state) {
                case 0:
                    signUpView.showPrompt(ResourcesUtil.getString(R.string.done), message, SweetAlertDialog.SUCCESS_TYPE);
                    break;
                case 1:
                    signUpView.showPrompt(ResourcesUtil.getString(R.string.alert_error), message, SweetAlertDialog.ERROR_TYPE);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
