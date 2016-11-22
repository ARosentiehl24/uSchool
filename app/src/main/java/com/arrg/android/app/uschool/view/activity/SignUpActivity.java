package com.arrg.android.app.uschool.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.arrg.android.app.uschool.interfaces.SignUpView;
import com.arrg.android.app.uschool.presenter.ISignUpPresenter;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.thefinestartist.utils.ui.KeyboardUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.wangyuwei.particleview.ParticleView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;
import static com.basgeekball.awesomevalidation.utility.RegexTemplate.NOT_EMPTY;

public class SignUpActivity extends AutoLayoutActivity implements SignUpView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.particleView)
    ParticleView particleView;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.etLastName)
    TextInputEditText etLastName;
    @BindView(R.id.etIdentification)
    TextInputEditText etIdentification;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.etRepeatPassword)
    TextInputEditText etRepeatPassword;
    @BindView(R.id.etPhoneNumber)
    TextInputEditText etPhoneNumber;

    private AwesomeValidation awesomeValidation;
    private Boolean allFieldsAreValid = false;
    private ISignUpPresenter signUpPresenter;
    private SweetAlertDialog sweetAlertDialog;
    private USchool uSchool = USchool.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        getWindow().setEnterTransition(new Slide(Gravity.END).excludeTarget(particleView, true).setDuration(250));

        signUpPresenter = new ISignUpPresenter(this);
        signUpPresenter.onCreate();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void configViews() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        title.setText(R.string.sign_up);

        particleView.post(new Runnable() {
            @Override
            public void run() {
                particleView.startAnim();
            }
        });

        awesomeValidation = new AwesomeValidation(COLORATION);
        awesomeValidation.setColor(ContextCompat.getColor(this, R.color.colorAccent));

        awesomeValidation.addValidation(etName, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etLastName, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etIdentification, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etEmail, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etEmail, Patterns.EMAIL_ADDRESS, getString(R.string.invalid_email_message));
        awesomeValidation.addValidation(etPassword, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etRepeatPassword, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etPhoneNumber, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etPhoneNumber, Patterns.PHONE, getString(R.string.invalid_phone_number_message));
    }

    @Override
    public void showPrompt(String title, String message, int dialogType) {
        sweetAlertDialog.dismissWithAnimation();

        new SweetAlertDialog(this, dialogType)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(title.equals(getString(R.string.done)) ? getString(R.string.close) : getString(R.string.try_again))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        if (sweetAlertDialog.getTitleText().equals(getString(R.string.done))) {
                            finishActivity();
                        }
                    }
                }).show();
    }

    @Override
    public void showPasswordsDoNotMatchError(String errorMessage) {
        sweetAlertDialog.dismissWithAnimation();

        etRepeatPassword.setError(errorMessage);

        KeyboardUtil.showImmediately(etRepeatPassword);
    }

    @Override
    public void finishActivity() {
        supportFinishAfterTransition();
    }

    @OnClick(R.id.signUpButton)
    public void onClick() {
        if (awesomeValidation.validate()) {
            sweetAlertDialog = new SweetAlertDialog(this);
            sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorAccent));
            sweetAlertDialog.setTitleText(getString(R.string.please_wait));
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setCanceledOnTouchOutside(false);
            sweetAlertDialog.show();

            signUpPresenter.trySignUpWith(
                    getTextFrom(etName),
                    getTextFrom(etLastName),
                    getTextFrom(etIdentification),
                    getTextFrom(etEmail),
                    getTextFrom(etPassword),
                    getTextFrom(etRepeatPassword),
                    getTextFrom(etPhoneNumber)
            );
        }
    }

    public String getTextFrom(TextInputEditText inputEditText) {
        return inputEditText.getText().toString();
    }
}
