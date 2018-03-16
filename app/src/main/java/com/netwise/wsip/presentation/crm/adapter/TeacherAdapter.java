package com.netwise.wsip.presentation.crm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;

import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherViewHolder> {
    private List<Teacher> teacherPresentationModel;

    public void setTeacherPresentationModel(List<Teacher> teacherPresentationModel) {
        this.teacherPresentationModel = teacherPresentationModel;
    }

    public TeacherAdapter(List<Teacher> teachers) {
        this.teacherPresentationModel = teachers;
    }

    @Override
    public int getItemCount() {
        if(teacherPresentationModel != null){
            return teacherPresentationModel.size();
        }
        else{
            return  0;
        }
    }

    @Override
    public void onBindViewHolder(TeacherViewHolder itemViewHolder, int i) {
        final Teacher model = teacherPresentationModel.get(i);
        itemViewHolder.bind(model);
        itemViewHolder.setVisibility();
    }

    @Override
    public TeacherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_list_row, viewGroup, false);
        return new TeacherViewHolder(view);
    }

}