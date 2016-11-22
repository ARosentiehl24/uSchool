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
import android.widget.Button;
import android.widget.TextView;

import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.interfaces.RecoveryPasswordView;
import com.arrg.android.app.uschool.presenter.IRecoveryPasswordPresenter;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.cloudrail.si.interfaces.SMS;
import com.cloudrail.si.services.Twilio;
import com.slmyldz.random.Randoms;
import com.thefinestartist.utils.ui.KeyboardUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.wangyuwei.particleview.ParticleView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arrg.android.app.uschool.Constants.ACCOUNT_SID;
import static com.arrg.android.app.uschool.Constants.AUTH_TOKEN;
import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;
import static com.basgeekball.awesomevalidation.utility.RegexTemplate.NOT_EMPTY;

public class RecoveryPasswordActivity extends AutoLayoutActivity implements RecoveryPasswordView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.particleView)
    ParticleView particleView;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.recoveryPasswordButton)
    Button recoveryPasswordButton;

    private AwesomeValidation awesomeValidation;
    private IRecoveryPasswordPresenter recoveryPasswordPresenter;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        ButterKnife.bind(this);

        getWindow().setEnterTransition(new Slide(Gravity.END).excludeTarget(particleView, true).setDuration(250));

        recoveryPasswordPresenter = new IRecoveryPasswordPresenter(this);
        recoveryPasswordPresenter.onCreate();
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

    @OnClick(R.id.recoveryPasswordButton)
    public void onClick() {
        if (awesomeValidation.validate()) {
            sweetAlertDialog = new SweetAlertDialog(this);
            sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorAccent));
            sweetAlertDialog.setTitleText(getString(R.string.please_wait));
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setCanceledOnTouchOutside(false);
            sweetAlertDialog.show();

            recoveryPasswordPresenter.requestPasswordRecovery(etEmail.getText().toString());
        } else {
            KeyboardUtil.showImmediately(etEmail);
        }
    }

    @Override
    public void configViews() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        title.setText(R.string.recovery_password);

        particleView.post(new Runnable() {
            @Override
            public void run() {
                particleView.startAnim();
            }
        });

        awesomeValidation = new AwesomeValidation(COLORATION);
        awesomeValidation.setColor(ContextCompat.getColor(this, R.color.colorAccent));

        awesomeValidation.addValidation(etEmail, NOT_EMPTY, getString(R.string.empty_message_field));
        awesomeValidation.addValidation(etEmail, Patterns.EMAIL_ADDRESS, getString(R.string.invalid_email_message));
    }

    @Override
    public void sendEmailTo(String email, final String password) {
        sweetAlertDialog.dismissWithAnimation();

        final SMS sms = new Twilio(this, ACCOUNT_SID, AUTH_TOKEN);

        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendSMS(getString(R.string.app_name), "+" + Randoms.Integer(100000000, 99999999), String.format(getString(R.string.password_recovery_request_email_body), password));
            }
        });
        /*EmailIntentBuilder.from(this)
                .to(email)
                .subject(getString(R.string.recovery_password))
                .body(String.format(getString(R.string.password_recovery_request_email_body), password))
                .start();*/
    }

    @Override
    public void showErrorPrompt(String title, String error) {
        sweetAlertDialog.dismissWithAnimation();

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(error)
                .setConfirmText(getString(R.string.try_again))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }
}
