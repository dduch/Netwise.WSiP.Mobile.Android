package com.netwise.wsip.presentation.crm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolViewHolder> {
    private List<School> schoolPresentationModel;

    public void setSchoolPresentationModel(List<School> schoolPresentationModel) {
        this.schoolPresentationModel = schoolPresentationModel;
    }

    public SchoolAdapter(List<School> schools) {
        this.schoolPresentationModel = schools;
    }

    @Override
    public int getItemCount() {
        if(schoolPresentationModel != null){
            return schoolPresentationModel.size();
        }
        else{
            return  0;
        }
    }

    @Override
    public void onBindViewHolder(SchoolViewHolder itemViewHolder, int i) {
        final School model = schoolPresentationModel.get(i);
        itemViewHolder.bind(model);
        itemViewHolder.setVisibility();
    }

    @Override
    public SchoolViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.school_list_row, viewGroup, false);
        return new SchoolViewHolder(view);
    }
}
