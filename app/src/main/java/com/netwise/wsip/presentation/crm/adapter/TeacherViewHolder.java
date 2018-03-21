package com.netwise.wsip.presentation.crm.adapter;

/**
 * Created by dawido on 13.03.2018.
 */

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.teacher_firstName)
    TextView firstName;

    @BindView(R.id.teacher_lastName)
    TextView lastName;

    @BindView(R.id.teacher_mainSchool)
    TextView schoolName;

    @BindView(R.id.teacher_street1)
    TextView street1;

    @BindView(R.id.teacher_street2)
    TextView street2;

    @BindView(R.id.teacher_city)
    TextView city;

    @BindView(R.id.teacher_postcode)
    TextView postcode;

    @BindView(R.id.teacher_province)
    TextView province;

    public TeacherViewHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Teacher teacherModel) {
        firstName.setText(teacherModel.firstName + " " + teacherModel.lastName);
        schoolName.setText(teacherModel.mainSchoolName);
        street1.setText(teacherModel.street1);
        street2.setText(teacherModel.street2);
        city.setText(teacherModel.city);
        postcode.setText(teacherModel.postalCodeName);
        province.setText(teacherModel.provinceName);
    }

    public void setVisibility(){
        if(TextUtils.isEmpty(firstName.getText())){
            firstName.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(lastName.getText())){
            lastName.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(schoolName.getText())){
            schoolName.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(street1.getText())){
            street1.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(street2.getText())){
            street2.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(city.getText())){
            city.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(postcode.getText())){
            postcode.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(province.getText())){
            province.setVisibility(View.GONE);
        }
    }

}
