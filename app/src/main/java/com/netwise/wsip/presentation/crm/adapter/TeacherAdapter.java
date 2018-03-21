package com.netwise.wsip.presentation.crm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherViewHolder> implements Filterable {
    private List<Teacher> teacherPresentationModel;
    private List<Teacher> filteredPresentationModel;

    public TeacherAdapter(List<Teacher> teachers) {
        this.teacherPresentationModel = teachers;
        this.filteredPresentationModel = teachers;
    }

    public void setTeacherPresentationModel(List<Teacher> teacherPresentationModel) {
        this.teacherPresentationModel = teacherPresentationModel;
        this.filteredPresentationModel = teacherPresentationModel;
    }


    @Override
    public int getItemCount() {
        if(filteredPresentationModel != null){
            return filteredPresentationModel.size();
        }
        else{
            return  0;
        }
    }

    @Override
    public void onBindViewHolder(TeacherViewHolder itemViewHolder, int i) {
        final Teacher model = filteredPresentationModel.get(i);
        itemViewHolder.bind(model);
        itemViewHolder.setVisibility();
    }

    @Override
    public TeacherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_list_row, viewGroup, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty() || charSequence.length() < 3) {
                    filteredPresentationModel = teacherPresentationModel;
                } else {

                    ArrayList<Teacher> filteredList = new ArrayList<>();
                    for (Teacher t : teacherPresentationModel) {
                        if(isMatchingFilter(t, charString)){
                            filteredList.add(t);
                        }
                    }
                    filteredPresentationModel = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPresentationModel;
                return filterResults;
            }

            private boolean isMatchingFilter(Teacher t, String charString){
                return t.firstName.toLowerCase().contains(charString) ||
                        t.lastName.toLowerCase().contains(charString) ||
                        t.email.toLowerCase().contains(charString) ||
                        t.mobilePhone.toLowerCase().contains(charString) ||
                        t.city.toLowerCase().contains(charString);
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPresentationModel = (ArrayList<Teacher>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}