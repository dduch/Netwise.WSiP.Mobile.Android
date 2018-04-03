package com.netwise.wsip.presentation.crm;

import android.Manifest;
import android.app.ActionBar;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Toast;

import com.mindorks.paracamera.Camera;
import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.CameraHelper;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.domain.crm.Attachment;
import com.netwise.wsip.presentation.attachmentSender.AttachmentSenderActivity;
import com.netwise.wsip.presentation.crm.adapter.ViewPagerAdapter;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CrmActivity extends DaggerAppCompatActivity implements SearchView.OnQueryTextListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private SearchView searchView = null;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private SchoolFragment schoolFragment;
    private TeacherFragement teacherFragement;


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this, vmFactory).get(CrmViewModel.class);
        observeViewModel();
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        schoolFragment = new SchoolFragment();
        teacherFragement = new TeacherFragement();
        adapter.addFragment(schoolFragment, getResources().getString(R.string.schoolTab));
        adapter.addFragment(teacherFragement, getResources().getString(R.string.teacherTab));
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.clear();
    }

    @OnClick(R.id.take_photoButton)
    public void onTakePhotoClick() {
        CrmActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
    }


    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        try {
            CameraHelper.getCamera(this).takePicture();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        CrmActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = CameraHelper.getCamera().getCameraBitmap();
            if(imageBitmap != null){
                Attachment attachementData = getAttchmentData();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                this.viewModel.addImageDataToRepo(stream.toByteArray().clone());
                Bundle attachmentSenderBundle = AttachmentSenderActivity.createExtraData(attachementData);
                Intent intent = new Intent(this, AttachmentSenderActivity.class);
                intent.putExtras(attachmentSenderBundle);
                CameraHelper.getCamera().deleteImage();
                startActivity(intent);
            }
        }
    }

    private Attachment getAttchmentData(){
        Attachment attachmentData = new Attachment();
        int currentTab = viewPager.getCurrentItem();
        if(currentTab == 0){
            if(schoolFragment != null){
                int selectedSchool = schoolFragment.adapter.selectedPos;
                School school = schoolFragment.adapter.getSchoolPresentationModel().get(selectedSchool);
                attachmentData.crmEntityName = getResources().getString(R.string.crm_school_entity_name);
                attachmentData.id = school.itemId;
            }
        }
        else if(currentTab == 1) {
            if(teacherFragement != null){
                int selectedTeacher = teacherFragement.adapter.selectedPos;
                Teacher teacher = teacherFragement.adapter.getTeacherPresentationModel().get(selectedTeacher);
                attachmentData.crmEntityName = getResources().getString(R.string.crm_techer_entity_name);
                attachmentData.id = teacher.itemId;
            }
        }

        return  attachmentData;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        int currentTab = viewPager.getCurrentItem();
        if(currentTab == 0){
            if(schoolFragment != null){
                schoolFragment.adapter.getFilter().filter(newText);
            }
            return  true;
        }
        else if(currentTab == 1){
            if(teacherFragement != null){
                teacherFragement.adapter.getFilter().filter(newText);
            }
            return  true;
        }

        return false;
    }
}
