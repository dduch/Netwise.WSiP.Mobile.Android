package com.netwise.wsip.presentation.crm.adapter;

/**
 * Created by dawido on 13.03.2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.school_name)
    TextView schoolName_TextView;

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        ButterKnife.bind(this, itemView);
    }

    public void bind(School schoolModel) {
        schoolName_TextView.setText(schoolModel.name);
    }
}
