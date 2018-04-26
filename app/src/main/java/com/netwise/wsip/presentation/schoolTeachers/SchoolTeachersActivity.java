package com.netwise.wsip.presentation.schoolTeachers;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;

import com.mindorks.paracamera.Camera;
import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.CameraHelper;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Attachment;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.presentation.attachmentSender.AttachmentSenderActivity;
import com.netwise.wsip.presentation.crm.CrmViewModel;
import com.netwise.wsip.presentation.crm.TeacherFragement;
import com.netwise.wsip.presentation.crm.adapter.ViewPagerAdapter;
import com.netwise.wsip.presentation.dialogHelper.DialogHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by dawido on 19.03.2018.
 */
@RuntimePermissions
public class SchoolTeachersActivity extends DaggerAppCompatActivity implements SearchView.OnQueryTextListener, View.OnFocusChangeListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String SCHOOL_DATA = "school";

    private ViewPager viewPager;
    private TeacherFragement teacherFragement;
    private SearchView searchView = null;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.take_photoButton)
    FloatingActionButton takePhotoButton;

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
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        takePhotoButton.setVisibility(View.GONE);
        handleIntent(getIntent());
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
        teacherFragement = new TeacherFragement();
        teacherFragement.setArguments(TeacherFragement.createExtraData(school));
        adapter.addFragment(teacherFragement, school.name);
        viewPager.setAdapter(adapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.clear();
    }

    @OnClick(R.id.take_photoButton)
    public void onTakePhotoClick() {
        SchoolTeachersActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
    }


    @NeedsPermission(Manifest.permission.CAMERA)
        public void showCamera() {
            int teachersCount =0;
            teachersCount = teacherFragement.adapter.getItemCount();
            if(teachersCount > 0) {
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
        // NOTE: delegate the permission handling to generated method
        SchoolTeachersActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = CameraHelper.getCamera().getCameraBitmap();
            if(imageBitmap != null){
                Attachment attatchment = getAttachmentData();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                viewModel.addImageDataToRepo(stream.toByteArray().clone());
                Bundle attachmentSenderBundle = AttachmentSenderActivity.createExtraData(attatchment);
                Intent intent = new Intent(this, AttachmentSenderActivity.class);
                intent.putExtras(attachmentSenderBundle);
                CameraHelper.getCamera().deleteImage();
                startActivity(intent);
            }
        }
    }

    private Attachment getAttachmentData() {
        Attachment attachmentData = new Attachment();
        if (teacherFragement != null) {

            int selectedTeacher = teacherFragement.adapter.getSelectedPos();
            Teacher teacher = teacherFragement.adapter.getTeacherPresentationModel().get(selectedTeacher);
            attachmentData.crmEntityName = getResources().getString(R.string.crm_techer_entity_name);
            attachmentData.id = teacher.itemId;

        }
        return attachmentData;
    }

    public static Bundle createExtraData(School schoolDetails) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SCHOOL_DATA, schoolDetails);
        return bundle;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(teacherFragement != null){
            teacherFragement.adapter.getFilter().filter(newText);
        }
        return  true;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus){
            hideKeyboard();
        }
    }

    public void showHideTakePhotoButton(int visibility){
        this.takePhotoButton.setVisibility(visibility);
    }

    private void hideKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace();
        }
    }
}
