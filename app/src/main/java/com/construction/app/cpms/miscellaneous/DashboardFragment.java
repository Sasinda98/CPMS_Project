package com.construction.app.cpms.miscellaneous;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.R;
import com.construction.app.cpms.expenses.actiExpenses;
import com.construction.app.cpms.inventoryManagement.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private CardView plansTile;
    private CardView inventoryTile;
    private CardView financesTile;
    private CardView forumsTile;
    private CardView milestonesTile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setUpTopBar(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        //Getting references to the tiles available to select from.
        plansTile = (CardView) view.findViewById(R.id.plansTile);
        inventoryTile = (CardView) view.findViewById(R.id.inventoryTile);
        financesTile = (CardView) view.findViewById(R.id.financesTile);
        forumsTile = (CardView) view.findViewById(R.id.forumsTile);
        milestonesTile = (CardView) view.findViewById(R.id.milestonesTile);

        //Click listeners for the tiles, navigate to the relevant activity you made using intents.
        plansTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getContext(), "Plans Tile Clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        inventoryTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast toast = Toast.makeText(getContext(), "Inventory Tile Clicked", Toast.LENGTH_SHORT);
//                toast.show();
                Intent intent = new Intent(getActivity(), inventory_items_list.class);
                startActivity(intent, null);

            }
        });

        financesTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast toast = Toast.makeText(getContext(), "Finances Tile Clicked", Toast.LENGTH_SHORT);
                toast.show();*/

                Intent intent = new Intent(getActivity(), actiExpenses.class);
                startActivity(intent, null);

            }
        });

        forumsTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Navigation)getActivity()).naviagateTo(new ForumsFragment(), true);
            }
        });

        milestonesTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getContext(), "Milestones Tile Clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

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
