package com.arrg.android.app.uschool.interfaces;

import com.arrg.android.app.uschool.model.entity.School;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Alberto on 13-Nov-16.
 */

public interface SchoolListView {
    void configViews();

    void showWarningMessage(String title, String message, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void loadSchoolList(ArrayList<School> schools);

    void toggleSearchView();

    void showLoadSchoolErrorPrompt(String title, String message);
}
