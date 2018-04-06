package com.netwise.wsip.presentation.crm.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.presentation.crm.TeacherFragement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherViewHolder> implements Filterable, SectionTitleProvider {
    private List<Teacher> teacherPresentationModel;
    private List<Teacher> filteredPresentationModel;
    private TeacherFragement parentFragment;
    public int selectedPos = 0;
    Context context;

    public TeacherAdapter(List<Teacher> teachers, TeacherFragement parentFragment) {
        this.teacherPresentationModel = teachers;
        this.filteredPresentationModel = teachers;
        this.parentFragment = parentFragment;
    }

    public void setTeacherPresentationModel(List<Teacher> teacherPresentationModel){
        this.teacherPresentationModel = teacherPresentationModel;
        this.filteredPresentationModel = teacherPresentationModel;
    }

    public List<Teacher> getTeacherPresentationModel(){
        return this.filteredPresentationModel;
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
        if(i == selectedPos){
            itemViewHolder.isSelected.setChecked(true);
            itemViewHolder.cardView.setCardBackgroundColor
                    (ResourcesCompat.getColor(context.getResources(), R.color.wsip_accent_color_light, null));
        }
        else {
            itemViewHolder.isSelected.setChecked(false);
            itemViewHolder.cardView.setCardBackgroundColor
                    (ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }
    }

    @Override
    public TeacherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_list_row, viewGroup, false);
        this.context = view.getContext();
        return new TeacherViewHolder(view, this );
    }

    @Override
    public String getSectionTitle(int position) {
        String sectionTitle = "A";
        if(filteredPresentationModel != null){
            Teacher t = filteredPresentationModel.get(position);
            if(!TextUtils.isEmpty(t.fullName)){
                sectionTitle = t.fullName;
            }
        }
        return  sectionTitle.substring(0, 1);
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
                return
                        (!TextUtils.isEmpty(t.fullName) && t.fullName.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(t.email) && t.email.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(t.mobilePhone) && t.mobilePhone.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(t.city) && t.city.toLowerCase().contains(charString));
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPresentationModel = (ArrayList<Teacher>) filterResults.values;
                if(filteredPresentationModel.size() == 0){
                    parentFragment.setMessageVisibility(View.VISIBLE);
                }
                else if(filteredPresentationModel.size() != 0){
                    parentFragment.setMessageVisibility(View.GONE);
                }
                selectedPos = 0;
                notifyDataSetChanged();
            }
        };
    }
    public void unselectHolder() {
        TeacherViewHolder holder = (TeacherViewHolder)parentFragment.recyclerView.findViewHolderForAdapterPosition(this.selectedPos);
        if(holder != null && holder.isSelected != null){
            holder.isSelected.setChecked(false);
        }
        if(holder != null && holder.cardView != null){
            holder.cardView.setCardBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }
    }
}