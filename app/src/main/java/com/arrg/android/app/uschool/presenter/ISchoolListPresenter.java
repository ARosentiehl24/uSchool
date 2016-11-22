package com.arrg.android.app.uschool.presenter;

import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.VolleySingleton;
import com.arrg.android.app.uschool.interfaces.SchoolListPresenter;
import com.arrg.android.app.uschool.interfaces.SchoolListView;
import com.arrg.android.app.uschool.model.entity.School;
import com.google.gson.Gson;
import com.thefinestartist.Base;
import com.thefinestartist.utils.content.ResourcesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.arrg.android.app.uschool.Constants.LOCATION_PERMISSION_RC;
import static com.arrg.android.app.uschool.Constants.MESSAGE;
import static com.arrg.android.app.uschool.Constants.SCHOOLS;
import static com.arrg.android.app.uschool.Constants.STATE;

/**
 * Created by Alberto on 13-Nov-16.
 */

public class ISchoolListPresenter implements SchoolListPresenter {

    private SchoolListView schoolListView;
    private USchool uSchool;

    public ISchoolListPresenter(SchoolListView schoolListView) {
        uSchool = USchool.getInstance();

        this.schoolListView = schoolListView;
    }

    @Override
    public void onCreate() {
        schoolListView.configViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<Integer> permissionResults = new ArrayList<>();
        switch (requestCode) {
            case LOCATION_PERMISSION_RC:
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);
                }

                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
                    schoolListView.showWarningMessage(uSchool.getString(R.string.alert_warning), uSchool.getString(R.string.location_permission_request_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                } else {
                    schoolListView.loadSchoolList(uSchool.getSchools());
                }

                break;
        }
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        switch (itemIndex) {
            case 0:
                break;
            case 1:
                schoolListView.toggleSearchView();
                break;

        }
    }

    @Override
    public void makeQuery(ArrayList<School> schools, CharSequence query) {
        ArrayList<School> filteredList = new ArrayList<>();

        for (School school : schools) {
            if (school.getName().toLowerCase().contains(query.toString().toLowerCase())) {
                filteredList.add(school);
            }
        }

        schoolListView.loadSchoolList(filteredList);
    }

    @Override
    public void getSchools() {
        String url = uSchool.getSchoolList();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processSchoolListResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                schoolListView.showLoadSchoolErrorPrompt("onErrorResponse", error.getLocalizedMessage());
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

                    schoolListView.loadSchoolList(uSchool.getSchools());
                    break;
                case 2:
                    message = response.getString(MESSAGE);

                    schoolListView.showLoadSchoolErrorPrompt(ResourcesUtil.getString(R.string.alert_error), message);

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
