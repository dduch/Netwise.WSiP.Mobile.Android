package com.netwise.wsip.presentation.crm;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netwise.wsip.R;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.presentation.crm.adapter.SchoolAdapter;
import com.netwise.wsip.presentation.crm.adapter.TeacherAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dawido on 13.03.2018.
 */

public class TeacherFragement extends Fragment {
    private CrmViewModel viewModel;
    private TeacherAdapter adapter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        viewModel = ViewModelProviders.of(getActivity()).get(CrmViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if(recyclerView != null){
            recyclerView.setLayoutManager(layoutManager);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new TeacherAdapter(viewModel.viewState().getValue().teachers());
        recyclerView.setAdapter(adapter);
        viewModel.viewState().observe(this,  CrmViewState -> adapter.setTeacherPresentationModel(CrmViewState.teachers()));
    }
}
