package com.arrg.android.app.uschool.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.model.entity.School;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arrg.android.app.uschool.Constants.SCHOOL;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.header)
    CardView header;
    @BindView(R.id.schoolLogo)
    ImageView schoolLogo;
    @BindView(R.id.municipality)
    TextView municipality;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.attention)
    TextView attention;
    @BindView(R.id.emphasis)
    TextView emphasis;
    @BindView(R.id.calendar)
    TextView calendar;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.nature)
    TextView nature;
    @BindView(R.id.grade)
    TextView grade;
    @BindView(R.id.qualification)
    TextView qualification;
    @BindView(R.id.space)
    TextView space;
    @BindView(R.id.spaceAvailable)
    TextView spaceAvailable;
    @BindView(R.id.icfesAverage)
    TextView icfesAverage;
    @BindView(R.id.icfesPosition)
    TextView icfesPosition;

    private School school;
    private USchool uSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");

        uSchool = USchool.getInstance();

        getWindow().setEnterTransition(new Slide(Gravity.END));
        getWindow().setExitTransition(new Slide(Gravity.END));

        school = (School) getIntent().getExtras().getSerializable(SCHOOL);

        String path = "http://" + uSchool.getHost() + ":" + uSchool.getPort() + "/uSchool/img/" + school.getNit() + "/logo.png";

        Glide.with(this).load(path).into(schoolLogo);

        title.setText(school.getName());
        municipality.setText(school.getMunicipality());
        department.setText(school.getDepartment());
        address.setText(school.getAddress());
        phone.setText(school.getPhone());
        email.setText(school.getEmail());
        attention.setText(school.getAttention());
        emphasis.setText(school.getEmphasis());
        calendar.setText(school.getCalendar());
        day.setText(school.getDay());
        nature.setText(school.getNature());
        grade.setText(school.getGrade());
        qualification.setText(String.valueOf(school.getQualification()));
        space.setText(String.valueOf(school.getSpace()));
        spaceAvailable.setText(String.valueOf(school.getSpaceAvailable()));
        icfesAverage.setText(String.valueOf(school.getIcfesAverage()));
        icfesPosition.setText(String.valueOf(school.getIcfesPosition()));
    }

    @Override
    public void onBackPressed() {
        //Navigator.with(this).utils().finishWithAnimation();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btnBack, R.id.btnLocation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnLocation:
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + school.getLatitude() + "," + school.getLongitude() + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);

                break;
        }
    }
}
