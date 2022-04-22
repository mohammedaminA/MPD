package com.example.abdullah_mohammedaminsultan_s1906568;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RssFeedListAdapter extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {


        private Context context;
        private List<RssFeedModel> mRssFeedModels = null;
        private ArrayList<RssFeedModel> e1;

    public RssFeedListAdapter(List<RssFeedModel> RssFeedModels, Context contex) {
        context = contex;
        this.mRssFeedModels = RssFeedModels;
        this.e1 = new ArrayList<RssFeedModel>();
        this.e1.addAll(RssFeedModels);
    }

        public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
            private View rssFeedView;

            public FeedModelViewHolder(View v) {
                super(v);
                rssFeedView = v;
            }
        }

        @Override
        public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rss_feed, parent, false);
            FeedModelViewHolder holder = new FeedModelViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(FeedModelViewHolder holder, int position) {
            final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
            ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);
            if(rssFeedModel.link.length()>= 10){                        //Making the font smaller if the length exceeds from 10
                TextView t = holder.rssFeedView.findViewById(R.id.linkText);
                t.setText(rssFeedModel.link);
                t.setTextSize(10);
            }else{
                ((TextView)holder.rssFeedView.findViewById(R.id.linkText)).setText(rssFeedModel.link);
            }/*
            if(position == 0){                      // Hide the date for only the first item because its header
                ((TextView)holder.rssFeedView.findViewById(R.id.dt)).setText(rssFeedModel.date);
                CardView cd = holder.rssFeedView.findViewById(R.id.cd);
                ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(rssFeedModel.description);
                cd.setCardBackgroundColor(context.getResources().getColor(R.color.teal_300));
                ((LinearLayout)holder.rssFeedView.findViewById(R.id.l2)).setVisibility(View.INVISIBLE);
                ((LinearLayout)holder.rssFeedView.findViewById(R.id.l1)).setVisibility(View.INVISIBLE);
            }else{*/
                ((TextView)holder.rssFeedView.findViewById(R.id.dt)).setText(rssFeedModel.date);
                CardView cd = holder.rssFeedView.findViewById(R.id.cd);
                ((TextView)holder.rssFeedView.findViewById(R.id.rd)).setText(rssFeedModel.road);
                ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(rssFeedModel.description);
                ((TextView)holder.rssFeedView.findViewById(R.id.strt)).setText(rssFeedModel.overstrt);
                ((TextView)holder.rssFeedView.findViewById(R.id.end)).setText(rssFeedModel.overend);


                cd.setCardBackgroundColor(context.getResources().getColor(R.color.teal_200));
                cd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, MapsActivity.class);
                        i.putExtra("lat", rssFeedModel.lat);
                        i.putExtra("lon", rssFeedModel.lon);
                        i.putExtra("t", rssFeedModel.title);
                        context.startActivity(i);
                    }
                });
    //}
    }


        public void filterss(String charText) throws ParseException {     //This method is for filtering
            mRssFeedModels.clear();
            if (charText.length() == 0) {
                mRssFeedModels.addAll(e1);
            } else {
                for (int i=1; i<3000; i++) {
                    if (e1.get(i).overstrt.equals(charText)||e1.get(i).overend.equals(charText)) {          // check if the any of date matches with the date matches with the given date
                        mRssFeedModels.add(e1.get(i));
                    }
                }
            }
            notifyDataSetChanged();
        }

        public void performFiltering(CharSequence constraint) {
            List<RssFeedModel> filteredList = new ArrayList<>(mRssFeedModels);
            mRssFeedModels.clear();
            if (constraint == null || constraint.length() == 0) {
                mRssFeedModels.addAll(filteredList);
            } else {
                String filterPattern = constraint.toString().toUpperCase();
                for (RssFeedModel item : filteredList) {
                    if (item.road.contains(filterPattern)) {
                        Log.e(filterPattern, item.road);
                        mRssFeedModels.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mRssFeedModels.size();
        }
    }

