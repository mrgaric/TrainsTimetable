package com.igordubrovin.trainstimetable.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.activities.MainActivity;
import com.igordubrovin.trainstimetable.utils.ConstProject;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSelectionTrain extends Fragment {
    TextView tvInformFragment;
    ProgressBar pbLoad;
    NestedScrollView svTvInfo;
    NestedScrollView svPbInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection_train, container, false);
        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);
        pbLoad = (ProgressBar)view.findViewById(R.id.pbLoad);
        svTvInfo = (NestedScrollView) view.findViewById(R.id.svTvInfo);
        svPbInfo = (NestedScrollView) view.findViewById(R.id.svPbInfo);

        TextView tv = (TextView)getActivity().findViewById(R.id.etSearchToStation);
        if (tv.getText().toString().equals("")) tvInformFragment.setText(ConstProject.INF_ENTER_TO);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof MainActivity){
            boolean fromIsClear = ((MainActivity)getActivity()).getCetFromIsClear();
            boolean toIsClear = ((MainActivity)getActivity()).getCetToIsClear();
            if (fromIsClear || toIsClear){
                if (fromIsClear){
                    tvInformFragment.setText(ConstProject.INF_ENTER_FROM);
                }
                else {
                    tvInformFragment.setText(ConstProject.INF_ENTER_TO);
                }
            } else {
                svTvInfo.setVisibility(View.GONE);
                svPbInfo.setVisibility(View.VISIBLE);
            }
        }
    }
}
