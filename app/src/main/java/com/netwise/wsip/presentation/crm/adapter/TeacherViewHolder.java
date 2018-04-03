package com.netwise.wsip.presentation.crm.adapter;

/**
 * Created by dawido on 13.03.2018.
 */

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.fullName)
    TextView fullName;

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

    @BindView(R.id.selectedCheckbox)
    CheckBox isSelected;

    @BindView(R.id.card_view)
    CardView cardView;

    TeacherAdapter adapter;
    Context context;

    public TeacherViewHolder(View itemView, TeacherAdapter parentAdapter) {
        super(itemView);
        context = itemView.getContext();
        itemView.setClickable(true);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.adapter = parentAdapter;
    }

    public void bind(Teacher teacherModel) {
        fullName.setText(teacherModel.fullName);
        schoolName.setText(teacherModel.mainSchoolName);
        street1.setText(teacherModel.street1);
        street2.setText(teacherModel.street2);
        city.setText(teacherModel.city);
        postcode.setText(teacherModel.postalCodeName);
        province.setText(teacherModel.provinceName);
    }

    public void setVisibility(){
        if(TextUtils.isEmpty(fullName.getText())){
            fullName.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {
        this.isSelected.setChecked(true);
    }

    @OnCheckedChanged(R.id.selectedCheckbox)
    public void onChexkBoxChnage(){
        setSelection();
    }

    private void setSelection(){
        if(this.isSelected.isChecked()){
            this.cardView.setCardBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.wsip_accent_color_light, null));
        }
        adapter.unselectHolder();
        adapter.selectedPos = getAdapterPosition();
    }
}
