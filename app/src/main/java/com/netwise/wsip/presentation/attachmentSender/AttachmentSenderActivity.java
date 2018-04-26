package com.netwise.wsip.presentation.attachmentSender;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.NoteHeader;
import com.netwise.wsip.domain.crm.Attachment;
import com.netwise.wsip.infastucture.ModelMapper;
import com.netwise.wsip.infastucture.network.AttachmentRequestDto;
import com.netwise.wsip.presentation.crm.CrmActivity;
import com.netwise.wsip.presentation.crm.CrmViewModel;
import com.netwise.wsip.presentation.dialogHelper.DialogHelper;
import com.rey.material.widget.ProgressView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;

/**
 * Created by dawido on 15.03.2018.
 */

public class AttachmentSenderActivity extends DaggerAppCompatActivity implements View.OnTouchListener{
    private static String ATTACHMENT_DATA = "atachment";
    private static String TAKEN_PHOTO_KEY = "photo";
    private Attachment attachmentData;
    private Boolean isSending = false;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.attachmentType)
    Spinner dropdown;

    @BindView(R.id.attachmentName)
    EditText attachmentName;

    @BindView(R.id.attachmentContent)
    EditText attachmentContent;

    @BindView(R.id.sendButton)
    Button sendAttachmentButton;

    @BindView(R.id.progressInfo)
    TextView progressInfo;

    @BindView(R.id.progressBar)
    ProgressView progressBar;

    @BindView(R.id.backgroundLayout)
    ConstraintLayout backgroundLayout;

    @Inject
    ViewModelProvider.Factory vmFactory;
    CrmViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender_form);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel = ViewModelProviders.of(this, vmFactory).get(CrmViewModel.class);
        hideProgressControls();
        handleIntent(getIntent());
        initComboBox();
        this.backgroundLayout.setOnTouchListener(this);
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
        if(isSending){
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(isSending){
            return;
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.clear();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initComboBox(){
        List<String> headers = new ArrayList<String>();
        for(NoteHeader h : this.viewModel.getNoteHeaders()) {
            headers.add(h.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, headers);
        dropdown.setAdapter(adapter);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        this.attachmentData = bundle.getParcelable(ATTACHMENT_DATA);
    }

    public static Bundle createExtraData(Attachment attachmentDto) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ATTACHMENT_DATA, attachmentDto);
        return bundle;
    }

    @OnItemSelected(R.id.attachmentType)
    public void onAtachementTypeChange(){
        this.attachmentName.setText(this.dropdown.getSelectedItem().toString() +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()).toString().replace('-','_').replace(':','_')
                + ".jpg");
    }

    @OnClick(R.id.sendButton)
    public void onSendClick() {
        this.attachmentData.body = this.attachmentContent.getText().toString();
        if(this.dropdown != null) {
            this.attachmentData.subject = this.dropdown.getSelectedItem() != null ? this.dropdown.getSelectedItem().toString() : "";
        }
        this.attachmentData.data = viewModel.getImageDataFromRepo();
        showProgressControls();
        setAttachmentName();
        isSending = true;
        AttachmentRequestDto attachmentDto = ModelMapper.mapAttachmentData(attachmentData);
        viewModel.uploadAttachment(attachmentDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> handleSuccess(result),
                        throwable -> handleError(throwable));
    }

    private void setAttachmentName(){
        this.attachmentData.fileName = this.attachmentName.getText().toString();
        if(!this.attachmentData.fileName.contains(getResources().getString(R.string.defaultAttachmentExtension))){
            this.attachmentData.fileName = this.attachmentData.fileName + getResources().getString(R.string.defaultAttachmentExtension);
        }
    }

    private void handleSuccess(ResponseBody result) {
        hideProgressControls();
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog)
                .setTitle(this.getResources().getString(R.string.success_header))
                .setMessage(this.getResources().getString(R.string.upload_succeed))
                .setPositiveButton(this.getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        setResult(0, i);
                        finish();
                    }
                }).create().show();
    }

    private void handleError(Throwable error) {
        hideProgressControls();
        DialogHelper.displayErrorDialog(this, getResources().getString(R.string.upload_error));
    }

    private void hideProgressControls(){
        progressInfo.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        this.dropdown.setEnabled(true);
        this.attachmentName.setEnabled(true);
        this.attachmentContent.setEnabled(true);
        this.sendAttachmentButton.setEnabled(true);
        isSending = false;
    }

    private void showProgressControls(){
        progressInfo.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        this.dropdown.setEnabled(false);
        this.attachmentName.setEnabled(false);
        this.attachmentContent.setEnabled(false);
        this.sendAttachmentButton.setEnabled(false);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(!(view instanceof EditText)){
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            catch(NullPointerException ex)
            {
                ex.printStackTrace();
            }
        }
        return true;
    }
}
