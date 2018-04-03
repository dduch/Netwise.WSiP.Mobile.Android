package com.netwise.wsip.presentation.crm.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.presentation.crm.SchoolFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;

/**
 * Created by dawido on 13.03.2018.
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolViewHolder> implements Filterable, SectionTitleProvider {
    private List<School> schoolPresentationModel;
    private List<School> filteredPresentationModel;
    private SchoolViewHolder schoolViewHolder;
    private SchoolFragment parentFragment;
    public int selectedPos = 0;
    Context context;

    public SchoolAdapter(List<School> schools, SchoolFragment parentFragment) {
        this.filteredPresentationModel = schools;
        this.parentFragment = parentFragment;
    }

    public void setSchoolPresentationModel(List<School> schoolPresentationModel) {
        this.schoolPresentationModel = schoolPresentationModel;
        this.filteredPresentationModel = schoolPresentationModel;
    }

    public List<School> getSchoolPresentationModel(){
        return filteredPresentationModel;
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
    public SchoolViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.school_list_row, viewGroup, false);
        this.context = view.getContext();
        schoolViewHolder = new SchoolViewHolder(view, this);
        return schoolViewHolder;
    }

    @Override
    public String getSectionTitle(int position) {
        String sectionTitle = "A";
        if(filteredPresentationModel != null && !TextUtils.isEmpty(filteredPresentationModel.get(position).name)){
            sectionTitle = filteredPresentationModel.get(position).name;
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
                return
                        (!TextUtils.isEmpty(s.name) && s.name.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(s.schoolTypeName) && s.schoolTypeName.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(s.street1) && s.street1.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(s.street2) && s.street2.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(s.postalCode) && s.postalCode.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(s.city) && s.city.toLowerCase().contains(charString)) ||
                        (!TextUtils.isEmpty(s.provinceName) && s.provinceName.toLowerCase().contains(charString));
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPresentationModel = (ArrayList<School>) filterResults.values;
                notifyDataSetChanged();
                if(filteredPresentationModel == null || filteredPresentationModel.size() == 0){
                    parentFragment.setMessageVisibility(View.VISIBLE);
                }
                else if(filteredPresentationModel.size() != 0){
                    parentFragment.setMessageVisibility(View.GONE);
                }
            }
        };
    }

    public void unselectHolder() {
        SchoolViewHolder holder = (SchoolViewHolder)parentFragment.recyclerView.findViewHolderForAdapterPosition(this.selectedPos);
        if(holder != null && holder.isSelected != null){
            holder.isSelected.setChecked(false);
        }
        if(holder != null && holder.cardView != null){
            holder.cardView.setCardBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }
    }
}
