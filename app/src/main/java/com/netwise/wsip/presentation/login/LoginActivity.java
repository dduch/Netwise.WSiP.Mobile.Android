package com.netwise.wsip.presentation.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.infastucture.network.CredentialsDto;
import com.netwise.wsip.infastucture.network.ResponseError;
import com.netwise.wsip.presentation.crm.CrmActivity;
import com.netwise.wsip.presentation.dialogHelper.DialogHelper;
import com.rey.material.widget.ProgressView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.http.HTTP;

public class LoginActivity extends DaggerAppCompatActivity {
    private static String PREFS_NAME="PREFERENCES";
    private static String PREF_USERNAME="USERRNAME";
    private static String PREF_PASSWORD="PASSWORD";
    private static String PREF_REMEMBER_ME="REMEMBERME";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    LoginViewModel loginViewModel;

    @BindView(R.id.user_password)
    EditText passwordEditText;
    @BindView(R.id.user_name)
    EditText userNameEditText;
    @BindView(R.id.remeber_me)
    AppCompatCheckBox rememberMeCheckBox;
    @BindView(R.id.progressInfo)
    TextView progressInfo;
    @BindView(R.id.progressBar)
    ProgressView progressBar;
    @BindView(R.id.login_in)
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        ButterKnife.bind(this);

        progressInfo.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        getStoredData();
    }

    private void getStoredData() {
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        Boolean isRememberMe = pref.getBoolean(PREF_REMEMBER_ME, false);
        rememberMeCheckBox.setChecked(isRememberMe);
        if(isRememberMe){
            userNameEditText.setText(username);
            passwordEditText.setText(password);
        }
    }

    @OnClick(R.id.login_in)
    public void onLoginClick() {
        if(rememberMeCheckBox.isChecked()){
            SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            pref.edit().putString(PREF_USERNAME, userNameEditText.getText().toString()).apply();
            pref.edit().putString(PREF_PASSWORD, passwordEditText.getText().toString()).apply();
        }

        progressInfo.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        userNameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        rememberMeCheckBox.setEnabled(false);
        CredentialsDto cred = new CredentialsDto();
        cred.userName = userNameEditText.getText().toString();
        cred.password = passwordEditText.getText().toString();
        loginViewModel.repository.SetCredentials(cred);
        loginViewModel.repository.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        token -> handleTokenSuccess(),
                        throwable -> handleTokenError(throwable));
    }

    @OnCheckedChanged(R.id.remeber_me)
    public void onRememberMeClick(){
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        pref.edit().putBoolean(PREF_REMEMBER_ME, rememberMeCheckBox.isChecked()).apply();
        if(rememberMeCheckBox.isChecked()){
            pref.edit().putString(PREF_USERNAME, userNameEditText.getText().toString()).apply();
            pref.edit().putString(PREF_PASSWORD, passwordEditText.getText().toString()).apply();
        }
        else{
            pref.edit().putString(PREF_USERNAME, "").apply();
            pref.edit().putString(PREF_PASSWORD, "").apply();
        }
    }

    private void handleTokenSuccess(){
        this.progressInfo.setText(getResources().getString(R.string.data_loading_in_progress));
        loginViewModel.repository.getCrmData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        crmData -> handleDownloadingSuccess(crmData),
                        throwable -> handleDownloadingError(throwable));
    }

    private void handleTokenError(Throwable error){
        hideLogingControls();
        String message = "";
        if(SocketTimeoutException.class.isInstance(error)){
            message = getResources().getString(R.string.server_timeout);
        }
        if(HttpException.class.isInstance(error)){
            ResponseError errorMsg = null;
            try {
                errorMsg = new Gson().fromJson(((HttpException)error).response().errorBody().string(), ResponseError.class);
                message = errorMsg.error_description;
            } catch (IOException e) {
                message = getResources().getString(R.string.error_login);
                e.printStackTrace();
            }
        }

        DialogHelper.displayErrorDialog(this, message);
    }

    private void handleDownloadingSuccess(CrmData crmData){
        hideLogingControls();
        Intent intent = new Intent(this, CrmActivity.class);
        startActivity(intent);
    }

    private void handleDownloadingError(Throwable error){
        hideLogingControls();
        DialogHelper.displayErrorDialog(this, getResources().getString(R.string.no_data_available));
    }

    private void hideLogingControls(){
        progressInfo.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setEnabled(true);
        userNameEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        rememberMeCheckBox.setEnabled(true);
    }

}
