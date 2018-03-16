package com.netwise.wsip.presentation.attachmentSender;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.netwise.wsip.R;
import com.netwise.wsip.presentation.fake.FakeViewModel;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by dawido on 15.03.2018.
 */

public class AttachmentSenderActivity extends DaggerAppCompatActivity {

    private static String TAKEN_PHOTO_KEY = "photo";

    @BindView(R.id.attachmentType)
    Spinner dropdown;

    @Inject
    ViewModelProvider.Factory vmFactory;
    AttachementSenderViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender_form);
        ButterKnife.bind(this);
        viewModel  = ViewModelProviders.of(this, vmFactory).get(AttachementSenderViewModel.class);

        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public static Bundle createExtraData(Bitmap imageBitmap) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAKEN_PHOTO_KEY, imageBitmap);
        return bundle;
    }
}
