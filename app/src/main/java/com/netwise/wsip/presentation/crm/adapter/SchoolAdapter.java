package com.netwise.wsip.presentation.crm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolViewHolder> implements Filterable {
    private List<School> schoolPresentationModel;
    private List<School> filteredPresentationModel;

    public SchoolAdapter(List<School> schools) {
        this.schoolPresentationModel = schools;
        this.filteredPresentationModel = schools;
    }

    public void setSchoolPresentationModel(List<School> schoolPresentationModel) {
        this.schoolPresentationModel = schoolPresentationModel;
        this.filteredPresentationModel = schoolPresentationModel;
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
    public void onBindViewHolder(SchoolViewHolder itemViewHolder, int i) {
        final School model = filteredPresentationModel.get(i);
        itemViewHolder.bind(model);
        itemViewHolder.setVisibility();
    }

    @Override
    public SchoolViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.school_list_row, viewGroup, false);
        return new SchoolViewHolder(view);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty() || charSequence.length() < 3) {
                    filteredPresentationModel = schoolPresentationModel;
                } else {

                    ArrayList<School> filteredList = new ArrayList<>();
                    for (School s : schoolPresentationModel) {
                        if(isMatchingFilter(s,charString)){
                            filteredList.add(s);
                        }
                    }
                    filteredPresentationModel = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPresentationModel;
                return filterResults;
            }

            private boolean isMatchingFilter(School s, String charString){
                return s.name.toLowerCase().contains(charString) ||
                        s.schoolTypeName.toLowerCase().contains(charString) ||
                        s.street1.toLowerCase().contains(charString) ||
                        s.street2.toLowerCase().contains(charString) ||
                        s.postalCode.toLowerCase().contains(charString) ||
                        s.city.toLowerCase().contains(charString) ||
                        s.provinceName.toLowerCase().contains(charString);
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPresentationModel = (ArrayList<School>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
