package com.arrg.android.app.uschool;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.arrg.android.app.uschool.model.entity.School;
import com.arrg.android.app.uschool.model.entity.User;
import com.cloudrail.si.CloudRail;
import com.example.jackmiras.placeholderj.library.PlaceHolderManager;
import com.thefinestartist.Base;
import com.thefinestartist.utils.preferences.Pref;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arrg.android.app.uschool.Constants.IDENTIFICATION;
import static com.arrg.android.app.uschool.Constants.PACKAGE_NAME;
import static com.arrg.android.app.uschool.Constants.SETTINGS_PREFERENCES;

/**
 * Created by Alberto on 05-Nov-16.
 */

public class USchool extends Application {

    private static USchool uSchool;

    public static USchool getInstance() {
        return uSchool;
    }

    public static PlaceHolderManager placeHolderManager;

    private ArrayList<School> schools;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        uSchool = this;

        PACKAGE_NAME = getPackageName().toUpperCase();
        SETTINGS_PREFERENCES = PACKAGE_NAME + ".SETTINGS";

        AutoLayoutConifg.getInstance().useDeviceSize();
        Base.initialize(this);
        CloudRail.setAppKey("581dfe02d1167570c552d1ce");
        Pref.setDefaultName(SETTINGS_PREFERENCES);

        placeHolderManager = new PlaceHolderManager.Configurator()
                .emptyBackground(R.color.colorPrimary)
                .emptyButton(R.string.try_again, 0)
                .emptyText(R.string.empty_content_message, 0, R.color.colorAccent)
                .errorBackground(R.color.colorPrimary)
                .errorButton(R.string.try_again, 0)
                .errorText(R.string.error_occurred_message, 0, R.color.colorAccent)
                .loadingBackground(R.color.colorPrimary)
                .loadingText(R.string.loading_content_message, 0, R.color.colorAccent)
                .progressBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .config();

        String fontPath = Pref.get(getString(R.string.font_path), "fonts/Teen.ttf");
        setAppFontTo(fontPath);

        schools = new ArrayList<>();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    public static PlaceHolderManager getPlaceHolderManager() {
        return placeHolderManager;
    }

    public void setAppFontTo(String fontPath) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(fontPath)
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public ArrayList<School> getSchools() {
        return schools;
    }

    public void setSchools(ArrayList<School> schools) {
        Collections.sort(schools, new Comparator<School>() {
            @Override
            public int compare(School school, School t1) {
                return school.getName().compareToIgnoreCase(t1.getName());
            }
        });
        this.schools = schools;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHost() {
        return Pref.get("host", "192.168.43.122");
    }

    public String getPort() {
        return Pref.get("port", "80");
    }

    public String getServicePath() {
        return "/uSchool/controller/service/";
    }

    public String createUser() {
        return "http://" + getHost() + ":" + getPort() + getServicePath() + "createUser.php";
    }

    public String getUserById(String identification) {
        return "http://" + getHost() + ":" + getPort() + getServicePath() + "getUserById.php?" + IDENTIFICATION + "=" + identification;
    }

    public String getSchoolList() {
        return "http://" + getHost() + ":" + getPort() + getServicePath() + "getSchoolList.php";
    }
}
