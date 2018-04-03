package com.netwise.wsip.presentation.crm;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.netwise.wsip.R;
import com.netwise.wsip.presentation.crm.adapter.SchoolAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dawido on 13.03.2018.
 */

public class SchoolFragment extends Fragment {
    private CrmViewModel viewModel;
    public SchoolAdapter adapter;

    @BindView(R.id.recyclerview)
    public RecyclerView recyclerView;

    @BindView(R.id.fastscroll)
    FastScroller fastScroller;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(CrmViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new SchoolAdapter(viewModel.viewState().getValue().schools(), this);
        recyclerView.setAdapter(adapter);
        fastScroller.setRecyclerView(recyclerView);
        viewModel.viewState().observe(this, CrmViewState -> adapter.setSchoolPresentationModel(CrmViewState.schools()));
        adapter.notifyDataSetChanged();
    }

    public void setMessageVisibility(int vis){
        emptyView.setText(getContext().getResources().getString(R.string.no_data_available));
        emptyView.setTextColor(getContext().getResources().getColor(R.color.Wsip_base_color));
        emptyView.setVisibility(vis);
    }
}
