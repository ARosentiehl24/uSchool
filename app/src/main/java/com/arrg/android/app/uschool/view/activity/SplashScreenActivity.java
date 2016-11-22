package com.arrg.android.app.uschool.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arrg.android.app.uschool.BuildConfig;
import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.interfaces.SplashScreenView;
import com.arrg.android.app.uschool.presenter.ISplashScreenPresenter;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.FadeInAnimation;
import com.easyandroidanimations.library.FadeOutAnimation;
import com.easyandroidanimations.library.ParallelAnimator;
import com.easyandroidanimations.library.SlideInAnimation;
import com.easyandroidanimations.library.SlideOutAnimation;
import com.thefinestartist.utils.ui.KeyboardUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import org.fingerlinks.mobile.android.navigator.Navigator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.wangyuwei.particleview.ParticleView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends AutoLayoutActivity implements SplashScreenView {

    @BindView(R.id.particleView)
    ParticleView particleView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.signInLayout)
    LinearLayout signInLayout;
    @BindView(R.id.etIdentification)
    EditText etIdentification;
    @BindView(R.id.etPassword)
    EditText etPassword;

    private Boolean requestWasSent = false;
    private ISplashScreenPresenter splashScreenPresenter;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        getWindow().setExitTransition(new Slide(Gravity.START).excludeTarget(particleView, true).setDuration(250));

        splashScreenPresenter = new ISplashScreenPresenter(this);
        splashScreenPresenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (requestWasSent && progressBar.getVisibility() == View.VISIBLE) {
            onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1000:
                    splashScreenPresenter.getSchoolList();
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
        Navigator.with(this).utils().finishWithAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void configViews() {
        sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        sweetAlertDialog.setTitleText(getString(R.string.please_wait));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);

        particleView.post(new Runnable() {
            @Override
            public void run() {
                particleView.startAnim();
            }
        });
        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                splashScreenPresenter.getSchoolList();
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInput(boolean showInput) {
        /*progressBar.setVisibility(showInput ? View.INVISIBLE : View.VISIBLE);
        signInLayout.setVisibility(showInput ? View.VISIBLE : View.INVISIBLE);*/

        new ParallelAnimator()
                .add(new FadeOutAnimation(progressBar)
                        .setDuration(250))
                .add(new SlideOutAnimation(progressBar)
                        .setDirection(Animation.DIRECTION_DOWN)
                        .setDuration(2500))
                .add(new FadeInAnimation(signInLayout)
                        .setDuration(2000)
                        .setInterpolator(new DecelerateInterpolator()))
                .add(new SlideInAnimation(signInLayout)
                        .setDirection(Animation.DIRECTION_DOWN)
                        .setDuration(500)
                        .setInterpolator(new AccelerateInterpolator()))
                .animate();
    }

    @Override
    public void showLoadSchoolErrorPrompt(String title, String error) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(error)
                .setConfirmText(getString(R.string.try_again))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        splashScreenPresenter.getSchoolList();
                    }
                })
                .setCancelText(getString(android.R.string.cancel))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finishActivity();
                    }
                }).show();
    }

    @Override
    public void showErrorPrompt(String title, String message) {
        sweetAlertDialog.dismissWithAnimation();

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(getString(R.string.try_again))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }

    @Override
    public void startSchoolListActivity(boolean userLogin) {
        if (!userLogin) {
            sweetAlertDialog.dismissWithAnimation();
        }

        Navigator.with(this).build().goTo(SchoolListActivity.class).animation().commit();

        finish();
    }

    @Override
    public void showPasswordsDoNotMatchError(String errorMessage) {
        sweetAlertDialog.dismissWithAnimation();

        etPassword.setError(errorMessage);

        KeyboardUtil.showImmediately(etPassword);
    }

    @Override
    public void finishActivity() {
        if (BuildConfig.DEBUG) {
            requestWasSent = true;

            Navigator.with(this).build().goTo(ServerSettingsActivity.class).animation().commit();
            finish();
        } else {
            onBackPressed();
        }
    }

    @OnClick({R.id.signInButton, R.id.tvForgotPasswor, R.id.tvNewAccount, R.id.fbLogin, R.id.googleLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInButton:
                KeyboardUtil.hide(this);

                if (etIdentification.getText().length() == 0) {
                    etIdentification.getText().clear();
                    etIdentification.requestFocus();
                    etIdentification.setError(getString(R.string.empty_message_field));

                    KeyboardUtil.showImmediately(etIdentification);
                } else if (etPassword.getText().length() == 0) {
                    etPassword.getText().clear();
                    etPassword.requestFocus();
                    etPassword.setError(getString(R.string.empty_message_field));

                    KeyboardUtil.showImmediately(etPassword);
                } else {
                    sweetAlertDialog.show();

                    splashScreenPresenter.trySignInWith(etIdentification.getText().toString(), etPassword.getText().toString());
                }
                break;
            case R.id.tvForgotPasswor:
                startActivity(new Intent(this, RecoveryPasswordActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(this, particleView, getString(R.string.app_name)).toBundle());
                break;
            case R.id.tvNewAccount:
                startActivity(new Intent(this, SignUpActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(this, particleView, getString(R.string.app_name)).toBundle());
                break;
            case R.id.fbLogin:

                break;
            case R.id.googleLogin:
                break;
        }
    }
}
