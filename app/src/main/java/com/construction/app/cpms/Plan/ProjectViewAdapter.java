package com.construction.app.cpms.Plan;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.construction.app.cpms.R;

import java.util.List;
//public class ProjectViewAdapter extends RecyclerView.Adapter<ProjectViewAdapter.ViewHolder> {

public class ProjectViewAdapter extends RecyclerView.Adapter<ProjectViewAdapter.ViewHolder>{
    private Context context;
    private List<My_Data> myData;

    public ProjectViewAdapter(Context context, List<My_Data> myData){
        this.context = context;
        this.myData = myData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_projects,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(myData.get(position).getNAME());
        holder.description.setText(myData.get(position).getDECRIPT());
        holder.PID.setText(Integer.toString(myData.get(position).getPID()));

        //opens the project when clicked
        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int PID = myData.get(position).getPID();
                    Intent intent = new Intent(context, DisplayProject.class);
                    intent.putExtra("PID", PID);
                    intent.putExtra("name",myData.get(position).getNAME());
                    intent.putExtra("description",myData.get(position).getDECRIPT());
                    context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return myData.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView description;
        public TextView PID;
        RelativeLayout parentLayout;
        CardView cardView;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.pr_name_n);
            description = (TextView) itemView.findViewById(R.id.pr_des_d);
            PID = (TextView) itemView.findViewById(R.id.pr_id_id);

        }

    }


}
