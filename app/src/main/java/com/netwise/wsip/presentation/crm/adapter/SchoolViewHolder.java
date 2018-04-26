package com.netwise.wsip.presentation.crm.adapter;

/**
 * Created by dawido on 13.03.2018.
 */
import android.app.Activity;
import android.bluetooth.le.AdvertiseData;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.test.ServiceTestCase;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.presentation.crm.CrmActivity;
import com.netwise.wsip.presentation.schoolTeachers.SchoolTeachersActivity;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

public class SchoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;

    @BindView(R.id.school_name)
    TextView schoolName;

    @BindView(R.id.school_type)
    TextView schoolType;

    @BindView(R.id.school_street1)
    TextView street1;

    @BindView(R.id.school_street2)
    TextView street2;

    @BindView(R.id.school_postcode)
    TextView postcode;

    @BindView(R.id.school_city)
    TextView city;

    @BindView(R.id.school_province)
    TextView province;

    @BindView(R.id.selectedCheckbox)
    CheckBox isSelected;

    @BindView(R.id.card_view)
    CardView cardView;

    String itemId;
    List<Teacher> teachers;
    SchoolAdapter adapter;

    public SchoolViewHolder(View itemView, SchoolAdapter parentAdapter) {
        super(itemView);
        itemView.setClickable(true);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        adapter = parentAdapter;
    }

    public void bind(School schoolModel) {
        schoolName.setText(schoolModel.name);
        schoolType.setText(schoolModel.schoolTypeName);
        street1.setText(schoolModel.street1);
        street2.setText(schoolModel.street2);
        city.setText(schoolModel.city);
        postcode.setText(schoolModel.postalCode);
        province.setText(schoolModel.provinceName);
        itemId = schoolModel.itemId;
        teachers = schoolModel.teachers;
        setVisibility();
    }

    public void setVisibility() {
        if (TextUtils.isEmpty(schoolName.getText())) {
            schoolName.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(schoolType.getText())) {
            schoolType.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(street1.getText())) {
            street1.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(street2.getText())) {
            street2.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(city.getText())) {
            city.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(postcode.getText())) {
            postcode.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(province.getText())) {
            province.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.detailsButton)
    public void onDetailsClick(View v) {
        School school = new School();
        school.name = this.schoolName.getText().toString();
        school.itemId = itemId;
        school.teachers = new ArrayList<Teacher>(teachers);
        Bundle bundle = SchoolTeachersActivity.createExtraData(school);
        Intent intent = new Intent(v.getContext(), SchoolTeachersActivity.class);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        this.isSelected.setChecked(true);
        ((CrmActivity)adapter.getParentFragment().getActivity()).showHideTakePhotoButton(View.VISIBLE);
    }

    @OnCheckedChanged(R.id.selectedCheckbox)
    public void onChexkBoxChanage(){
        setSelection();
        InputMethodManager imm = (InputMethodManager)this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(isSelected.getWindowToken(), 0);
    }

    private void setSelection(){
        if(this.isSelected.isChecked()){
            this.cardView.setCardBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.wsip_accent_color_light, null));
        }
        adapter.unselectHolder();
        adapter.setSelectedPos(getAdapterPosition());
    }
}

