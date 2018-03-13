package com.netwise.wsip.presentation.crm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class RVAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<School> schoolPresentationModel;
    private List<Teacher> teacherPresentationModel;

    public RVAdapter() {
        this.schoolPresentationModel = new ArrayList<School>();
        this.schoolPresentationModel.add(new School("Nazwa1"));
        this.schoolPresentationModel.add(new School("Nazwa2"));
        this.schoolPresentationModel.add(new School("Nazwa3"));
        this.schoolPresentationModel.add(new School("Nazwa4"));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        final School model = schoolPresentationModel.get(i);
        itemViewHolder.bind(model);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.school_list_row, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return schoolPresentationModel.size();
    }

    public void setFilter(List<School> schoolModel){

    }

}
