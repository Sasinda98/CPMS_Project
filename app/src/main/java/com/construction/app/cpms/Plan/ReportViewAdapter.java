package com.construction.app.cpms.Plan;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;

import java.util.List;

public class ReportViewAdapter extends RecyclerView.Adapter<ReportViewAdapter.ViewHolder> {

    private Context context;
    private List<ReportData> report_data;

    public ReportViewAdapter(Context context, List<ReportData> report_data){
        this.context = context;
        this.report_data = report_data;
    }

    @NonNull
    @Override
    public ReportViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_project_report,parent,false);
        return new ReportViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportViewAdapter.ViewHolder holder, final int position) {

        holder.name.setText(report_data.get(position).getNamE());
        holder.pID.setText(Integer.toString(report_data.get(position).getPiD()));
        holder.status.setText(report_data.get(position).getStatuS());

        Glide.with(context).load(report_data.get(position).getImage_linK()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return report_data.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView imageView;
        public TextView pID;
        public TextView status;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.pr_name_n_report);
            imageView = (ImageView) itemView.findViewById(R.id.image_i_report);
            pID = (TextView) itemView.findViewById(R.id.pr_id_id_report);
            status = (TextView)itemView.findViewById(R.id.pr_status);

        }

    }




}
