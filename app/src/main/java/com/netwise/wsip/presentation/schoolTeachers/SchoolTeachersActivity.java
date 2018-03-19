package com.netwise.wsip.presentation.schoolTeachers;

import android.Manifest;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.presentation.attachmentSender.AttachmentSenderActivity;
import com.netwise.wsip.presentation.crm.CrmViewModel;
import com.netwise.wsip.presentation.crm.CrmViewState;
import com.netwise.wsip.presentation.crm.TeacherFragement;
import com.netwise.wsip.presentation.crm.adapter.ViewPagerAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;

/**
 * Created by dawido on 19.03.2018.
 */

public class SchoolTeachersActivity extends DaggerAppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String SCHOOL_DATA = "school";

    private ViewPager viewPager;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @Inject
    ViewModelProvider.Factory vmFactory;
    CrmViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewModel = ViewModelProviders.of(this, vmFactory).get(CrmViewModel.class);
        observeViewModel();
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            return;
        }

        School school = bundle.getParcelable(SCHOOL_DATA);
        setupViewPager(viewPager, school);
    }


    private void setupViewPager(ViewPager viewPager, School school) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TeacherFragement(), getResources().getString(R.string.teacherTab));
        viewPager.setAdapter(adapter);
    }


    private void observeViewModel() {
        viewModel.viewState().observe(this, this::handleViewState);
    }

    private void handleViewState(CrmViewState crmViewState) {
        Toast.makeText(this, "Got new vs", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Associate searchable configuration with the SearchView
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.clear();
    }

    @OnClick(R.id.take_photoButton)
    public void onTakePhotoClick() {
        //SchoolTeachersActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
    }


    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(this, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        // SchoolTeachersActivity.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bundle attachmentSenderBundle = AttachmentSenderActivity.createExtraData(imageBitmap);
            Intent intent = new Intent(this, AttachmentSenderActivity.class);
            intent.putExtras(attachmentSenderBundle);
            startActivity(intent);
        }
    }

    public static Bundle createExtraData(School schoolDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SCHOOL_DATA, schoolDetails);
        return bundle;
    }
}
