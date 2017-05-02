package com.glyuk.volunterevents;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        TextView description;
        ImageView imageEvents;

        public EventViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            description = (TextView) itemView.findViewById(R.id.description);
            imageEvents= (ImageView) itemView.findViewById(R.id.image_events);
        }

    }
    List<ModelEvents> events;

    public RVAdapter(List<ModelEvents> events) {
        this.events = events;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        EventViewHolder holder = new EventViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.imageEvents.setImageResource(events.get(position).image_id);
        holder.description.setText(events.get(position).description);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }




}
