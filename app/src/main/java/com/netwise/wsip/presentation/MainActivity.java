package com.netwise.wsip.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.netwise.wsip.R;
import com.netwise.wsip.presentation.crm.CrmActivity;
import com.netwise.wsip.presentation.login.LoginActivity;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.login_in)
    public void onLoginClick()
    {
        startActivity(new Intent(this, CrmActivity.class));
    }
}
