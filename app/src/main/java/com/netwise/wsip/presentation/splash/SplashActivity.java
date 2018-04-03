package com.netwise.wsip.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mindorks.paracamera.Camera;
import com.netwise.wsip.domain.crm.CameraHelper;
import com.netwise.wsip.presentation.login.LoginActivity;

/**
 * Created by dawido on 26.03.2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onStop(){
        super.onStop();
        Camera cam = CameraHelper.getCamera();
        if(cam != null){
            cam.deleteImage();
        }
    }
}