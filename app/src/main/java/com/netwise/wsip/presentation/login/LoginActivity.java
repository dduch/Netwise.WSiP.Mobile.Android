package com.netwise.wsip.presentation.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.netwise.wsip.R;
import com.netwise.wsip.presentation.crm.CrmActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    LoginViewModel loginViewModel;

    @BindView(R.id.status)
    TextView statusTextView;
    @BindView(R.id.user_password)
    EditText passwordEditText;
    @BindView(R.id.user_name)
    EditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_in)
    public void onLoginClick() {
        statusTextView.setText("go to crm activity");
        statusTextView.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, CrmActivity.class);
        startActivity(intent);
    }
}
