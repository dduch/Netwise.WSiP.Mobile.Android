package com.netwise.wsip.presentation.fake;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.netwise.wsip.R;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class FakeActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory vmFactory;

    FakeViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel  = ViewModelProviders.of(this, vmFactory).get(FakeViewModel.class);
    }
}
