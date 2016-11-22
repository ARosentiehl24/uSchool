package com.arrg.android.app.uschool.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.interfaces.SchoolListView;
import com.arrg.android.app.uschool.model.entity.School;
import com.arrg.android.app.uschool.presenter.ISchoolListPresenter;
import com.arrg.android.app.uschool.view.adapter.SchoolAdapter;
import com.example.jackmiras.placeholderj.library.PlaceHolderJ;
import com.jaouan.revealator.Revealator;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mukesh.permissions.AppPermissions;
import com.thefinestartist.utils.content.Res;
import com.thefinestartist.utils.ui.KeyboardUtil;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.AutoRelativeLayout;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arrg.android.app.uschool.Constants.LOCATION_PERMISSION;
import static com.arrg.android.app.uschool.Constants.LOCATION_PERMISSION_RC;
import static com.arrg.android.app.uschool.Constants.SCHOOL;
import static com.arrg.android.app.uschool.Constants.SPEECH_INPUT_RC;
import static com.thefinestartist.Base.getContext;

public class SchoolListActivity extends AutoLayoutActivity implements SchoolListView, SpaceOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarLayout)
    AutoRelativeLayout toolbarLayout;
    @BindView(R.id.searchLayout)
    LinearLayout searchLayout;
    @BindView(R.id.listOfSchools)
    DragSelectRecyclerView listOfSchools;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.spaceNavigationView)
    SpaceNavigationView spaceNavigationView;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.inputSearch)
    EditText inputSearch;
    @BindView(R.id.voiceSearch)
    ImageButton voiceSearch;


    private AppPermissions appPermissions;
    private ISchoolListPresenter schoolListPresenter;
    private PlaceHolderJ placeHolderJ;
    private SchoolAdapter schoolAdapter;
    private USchool uSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);
        ButterKnife.bind(this);

        getWindow().setExitTransition(new Slide(Gravity.START));

        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        schoolListPresenter = new ISchoolListPresenter(this);
        schoolListPresenter.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SPEECH_INPUT_RC:
                    ArrayList<String> strings = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    inputSearch.setText(strings.get(0));
                    inputSearch.setSelection(strings.get(0).length());

                    break;
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (searchLayout.getVisibility() == View.VISIBLE) {
            toggleSearchView();
        } else {
            Navigator.with(this).utils().finishWithAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.school_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.app_bar_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    public void configViews() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");

        placeHolderJ = new PlaceHolderJ(container, R.id.listOfSchools, USchool.getPlaceHolderManager());
        placeHolderJ.init(R.id.view_loading, R.id.view_empty, R.id.view_error);
        placeHolderJ.viewEmptyTryAgainButton.setBackgroundTintList(ColorStateList.valueOf(Res.getColor(R.color.colorAccent)));
        placeHolderJ.viewEmptyTryAgainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        placeHolderJ.viewErrorTryAgainButton.setBackgroundTintList(ColorStateList.valueOf(Res.getColor(R.color.colorAccent)));
        placeHolderJ.viewErrorTryAgainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        placeHolderJ.showEmpty(null);

        spaceNavigationView.setCentreButtonRippleColor(ContextCompat.getColor(this, R.color.ripple));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.compare), R.drawable.ic_compare_arrows_white_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.search), R.drawable.ic_search_white_24dp));
        spaceNavigationView.setSpaceOnClickListener(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        uSchool = USchool.getInstance();

        Log.e(getClass().getSimpleName(), uSchool.getUser().toString());

        appPermissions = new AppPermissions(this);
        if (appPermissions.hasPermission(LOCATION_PERMISSION)) {
            loadSchoolList(uSchool.getSchools());
        } else {
            appPermissions.requestPermission(LOCATION_PERMISSION, LOCATION_PERMISSION_RC);
        }

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                schoolListPresenter.makeQuery(uSchool.getSchools(), charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    voiceSearch.setImageResource(R.drawable.ic_mic_accent_24dp);
                } else {
                    voiceSearch.setImageResource(R.drawable.ic_close_accent_24dp);
                }
            }
        });
    }

    @Override
    public void showWarningMessage(String title, String message, SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(getString(R.string.close))
                .setConfirmClickListener(onSweetClickListener)
                .show();
    }

    @Override
    public void loadSchoolList(ArrayList<School> schools) {
        if (schools.size() > 0) {
            placeHolderJ.hideEmpty();
            swipeRefreshLayout.setRefreshing(false);

            schoolAdapter = new SchoolAdapter(schools, SchoolListActivity.this);

            listOfSchools.setAdapter(schoolAdapter);
            listOfSchools.setHasFixedSize(true);
            listOfSchools.setLayoutManager(new LinearLayoutManager(SchoolListActivity.this, LinearLayoutManager.VERTICAL, false));

            schoolAdapter.setOnItemClickListener(new SchoolAdapter.OnItemClickListener() {
                @Override
                public void onClick(SchoolAdapter.ViewHolder viewHolder, View itemView, int position) {
                    /*Bundle bundle = new Bundle();
                    bundle.putSerializable(SCHOOL, schoolAdapter.getSchool(position));

                    Navigator.with(SchoolListActivity.this).build().goTo(DetailActivity.class, bundle).animation().commit();*/

                    Bundle plate = new Bundle();
                    plate.putSerializable(SCHOOL, schoolAdapter.getSchool(position));

                    Intent intent = new Intent(SchoolListActivity.this, DetailActivity.class);
                    intent.putExtras(plate);

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SchoolListActivity.this, itemView.findViewById(R.id.schoolLogo), SCHOOL);
                    startActivity(intent, activityOptionsCompat.toBundle());
                }

                @Override
                public void onLongClick(SchoolAdapter.ViewHolder viewHolder, View itemView, int position) {

                }
            });
        } else {
            placeHolderJ.showEmpty(null);
        }
    }

    @Override
    public void toggleSearchView() {
        if (searchLayout.getVisibility() == View.VISIBLE) {
            Log.e(getClass().getSimpleName(), "UnReveal");

            Revealator.unreveal(searchLayout)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            inputSearch.getText().clear();

                            KeyboardUtil.hide(inputSearch, true);
                        }
                    })
                    .start();
        } else {
            Log.e(getClass().getSimpleName(), "Reveal");

            Revealator.reveal(searchLayout)
                    .withChildsAnimation()
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            inputSearch.getText().clear();

                            KeyboardUtil.showImmediately(inputSearch);
                        }
                    })
                    .start();
        }
    }

    @Override
    public void showLoadSchoolErrorPrompt(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(getString(R.string.try_again))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        schoolListPresenter.getSchools();
                    }
                })
                .setCancelText(getString(android.R.string.cancel))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        schoolListPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCentreButtonClick() {

    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        schoolListPresenter.onItemClick(itemIndex, itemName);
    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {
        schoolListPresenter.onItemClick(itemIndex, itemName);
    }

    @Override
    public void onRefresh() {
        schoolListPresenter.getSchools();
    }

    @OnClick({R.id.closeSearch, R.id.voiceSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeSearch:
                toggleSearchView();
                break;
            case R.id.voiceSearch:
                if (inputSearch.getText().length() == 0) {
                    promptSpeechInput();
                } else {
                    inputSearch.getText().clear();
                }
                break;
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, SPEECH_INPUT_RC);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }
}
