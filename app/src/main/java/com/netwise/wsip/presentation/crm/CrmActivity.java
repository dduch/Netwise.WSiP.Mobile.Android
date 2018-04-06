package com.netwise.wsip.presentation.crm;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CrmActivity extends DaggerAppCompatActivity implements SearchView.OnQueryTextListener, View.OnFocusChangeListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private SearchView searchView = null;
    private ViewPagerAdapter adapter;
    private SchoolFragment schoolFragment;
    private TeacherFragement teacherFragement;
    ProgressDialog progressDialog;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Inject
    ViewModelProvider.Factory vmFactory;

    CrmViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, vmFactory).get(CrmViewModel.class);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshData:
                refreshData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Pobieranie danych");
        progressDialog.setMessage("Proszę czekać na pobranie danych z CRM...");
        progressDialog.show();
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
        searchView.setOnQueryTextFocusChangeListener(this);
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

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus){
         hideKeyboard();
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }
}
