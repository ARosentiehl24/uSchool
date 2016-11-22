package com.arrg.android.app.uschool.interfaces;

import com.arrg.android.app.uschool.model.entity.School;

import java.util.ArrayList;

/**
 * Created by Alberto on 13-Nov-16.
 */

public interface SchoolListPresenter {
    void onCreate();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void onItemClick(int itemIndex, String itemName);

    void makeQuery(ArrayList<School> schools, CharSequence query);

    void getSchools();
}
