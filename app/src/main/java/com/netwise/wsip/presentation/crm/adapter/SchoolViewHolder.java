package com.netwise.wsip.presentation.crm.adapter;

/**
 * Created by dawido on 13.03.2018.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.presentation.crm.CrmActivity;
import com.netwise.wsip.presentation.schoolTeachers.SchoolTeachersActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SchoolViewHolder extends RecyclerView.ViewHolder{

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

    String itemId;
    List<Teacher> teachers;

    public SchoolViewHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        ButterKnife.bind(this, itemView);
    }

    public void bind(School schoolModel) {
        schoolName.setText(schoolModel.name);
        schoolType.setText(schoolModel.schoolTypeName);
        street1.setText(schoolModel.street1);
        street2.setText(schoolModel.street2);
        city.setText(schoolModel.city);
        province.setText(schoolModel.provinceName);
        itemId = schoolModel.itemId;
        teachers = schoolModel.teachers;
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

        if (TextUtils.isEmpty(postcode.getText())) {
            postcode.setVisibility(View.GONE);
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
}

