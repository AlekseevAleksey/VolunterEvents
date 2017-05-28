package com.glyuk.volunterevents.adpter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.glyuk.volunterevents.R;

import com.glyuk.volunterevents.activity.ViewEvent;
import com.glyuk.volunterevents.model.ModelEvents;

import java.util.ArrayList;

import io.realm.RealmChangeListener;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> implements RealmChangeListener {

    //private RealmResults<ModelEvents> mEvents;

    private ArrayList<ModelEvents> eventDetailsArrayList;
    private Context cont;
    private LayoutInflater inflater;

    public RVAdapter(ArrayList<ModelEvents> eventDetailsArrayList, Context context) {
        this.eventDetailsArrayList = eventDetailsArrayList;
        this.cont = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public void onBindViewHolder(final EventViewHolder holder, final int position) {
        holder.name.setText(eventDetailsArrayList.get(position).getName());
        holder.description.setText(eventDetailsArrayList.get(position).getDescription());
        holder.image.setImageURI(Uri.parse(eventDetailsArrayList.get(position).getImage().toString()));


        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent=new Intent(context, ViewEvent.class);
                intent.putExtra("PersonID", eventDetailsArrayList.get(position).getId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventDetailsArrayList.size();
    }



    //listener for rv, add new card
    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        TextView description;
        TextView name;
        ImageView image;

        public EventViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            description = (TextView) itemView.findViewById(R.id.card_description);
            name = (TextView) itemView.findViewById(R.id.card_name);
            image = (ImageView) itemView.findViewById(R.id.card_image);

        }

    }

}
