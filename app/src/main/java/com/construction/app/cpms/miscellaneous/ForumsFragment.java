package com.construction.app.cpms.miscellaneous;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.app.cpms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumsFragment extends Fragment {


    public ForumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums, container, false);

        setUpTopBar(view);

        return view;
    }

    //Used mdc codelabs as reference
    private void setUpTopBar(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null){
            activity.setSupportActionBar(toolbar);
        }
    }

}
