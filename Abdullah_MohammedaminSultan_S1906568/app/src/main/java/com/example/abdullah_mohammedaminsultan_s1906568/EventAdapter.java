package com.example.abdullah_mohammedaminsultan_s1906568;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{

    List<mymodel> eventList;
    Activity context;

    public EventAdapter(List<mymodel> eventList, Activity context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        mymodel item= eventList.get(position);

        holder.date.setText(item.getDate());
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,desc,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date_tv);
        }
    }
}
