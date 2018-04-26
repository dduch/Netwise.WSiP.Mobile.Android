package com.netwise.wsip.presentation.crm;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.CameraHelper;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.domain.crm.Attachment;
import com.netwise.wsip.presentation.attachmentSender.AttachmentSenderActivity;
import com.netwise.wsip.presentation.crm.adapter.ViewPagerAdapter;
import com.netwise.wsip.presentation.dialogHelper.DialogHelper;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CrmActivity extends DaggerAppCompatActivity implements SearchView.OnQueryTextListener, View.OnFocusChangeListener, ViewPager.OnPageChangeListener {
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

    @BindView(R.id.take_photoButton)
    FloatingActionButton takePhotoButton;

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
        this.takePhotoButton.setVisibility(View.GONE);
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
            case android.R.id.home:
                return handleBackButton(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean handleBackButton(MenuItem item){
        if(searchView.getQuery().toString().length() > 0){
            searchView.setQuery("", true);
            return true;
        }
        else{
             return super.onOptionsItemSelected(item);
        }
    }

    private void refreshData() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.progress, null);
        AlertDialog dialog = new AlertDialog.Builder(CrmActivity.this, R.style.Theme_AppCompat_DayNight_Dialog)
                .setTitle(getResources().getString(R.string.wait))
                .setView(dialogView)
                .create();

        viewModel.getCrmRepository().refreshData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(
                        x ->
                    {
                        turnOffControls(dialog);
                        dialog.show();
                        dialog.getWindow().setLayout(700,600);
                    })
                .subscribe(
                    crmData ->
                    {

                        schoolFragment.adapter.setSchoolPresentationModel(crmData.getSchools());
                        teacherFragement.adapter.setTeacherPresentationModel(crmData.getTeachers());
                        turnOnControls(dialog);
                        dialog.dismiss();
                    },
                    throwable -> {
                        turnOnControls(dialog);
                        dialog.dismiss();
                    });
    }

    private void turnOnControls(AlertDialog dialog) {
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void turnOffControls(AlertDialog dialog) {
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,  WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,  WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        schoolFragment = new SchoolFragment();
        teacherFragement = new TeacherFragement();
        adapter.addFragment(schoolFragment, getResources().getString(R.string.schoolTab));
        adapter.addFragment(teacherFragement, getResources().getString(R.string.teacherTab));
        viewPager.addOnPageChangeListener(this);
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
        searchView.setIconifiedByDefault(true);
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);
        searchView.setOnFocusChangeListener(this);
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

    public void showHideTakePhotoButton(int visibility){
        this.takePhotoButton.setVisibility(visibility);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        int currentTab = viewPager.getCurrentItem();
        int itemsCount =0;
        if(currentTab == 0){
            if(schoolFragment != null){
                itemsCount = schoolFragment.adapter.getItemCount();
            }
        }
        else if(currentTab == 1) {
            if(teacherFragement != null){
                itemsCount= teacherFragement.adapter.getItemCount();
            }
        }
        if(itemsCount>0) {
            try {
                CameraHelper.getCamera(this).takePicture();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else
        {
            DialogHelper.displayDialog(this, "Nie wybrano rekordu", "Prosze wybrać rekord przed zrobieniem zdjęcia");
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
                Attachment attachementData = getAttachmentData();
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

    private Attachment getAttachmentData(){
        Attachment attachmentData = new Attachment();
        int currentTab = viewPager.getCurrentItem();
        if(currentTab == 0){
            if(schoolFragment != null){
                int selectedSchool = schoolFragment.adapter.getSelectedPos();
                School school = schoolFragment.adapter.getSchoolPresentationModel().get(selectedSchool);
                attachmentData.crmEntityName = getResources().getString(R.string.crm_school_entity_name);
                attachmentData.id = school.itemId;
            }
        }
        else if(currentTab == 1) {
            if(teacherFragement != null){
                int selectedTeacher = teacherFragement.adapter.getSelectedPos();
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
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        searchView.setQuery("", false);
        showHideTakePhotoButton(View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
