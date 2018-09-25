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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private Context context;
    private List<MyData> my_data;

    public CustomAdapter(Context context, List<MyData> my_data){
        this.context = context;
        this.my_data = my_data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.name.setText(my_data.get(position).getName());
            holder.description.setText(my_data.get(position).getDecript());
            holder.pID.setText(Integer.toString(my_data.get(position).getPid()));
        Glide.with(context).load(my_data.get(position).getImage_link()).into(holder.imageView);

            //opens the plan when clicked
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pID = my_data.get(position).getPid();
                    Intent intent = new Intent(context, displayPlan.class);
                    intent.putExtra("pID", pID);
                    intent.putExtra("name",my_data.get(position).getName());
                    intent.putExtra("image",my_data.get(position).getImage_link());
                    intent.putExtra("description",my_data.get(position).getDecript());
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return my_data.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView imageView;
        public TextView description;
        public TextView pID;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_n);
            description = (TextView) itemView.findViewById(R.id.des_d);
            imageView = (ImageView) itemView.findViewById(R.id.image_i);
            pID = (TextView) itemView.findViewById(R.id.id_id);

        }

    }
}
