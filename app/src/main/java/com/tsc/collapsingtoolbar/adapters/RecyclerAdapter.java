package com.tsc.collapsingtoolbar.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tsc.collapsingtoolbar.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<String> resNames,rating,thumb;
    private ArrayList<Integer> status;

    public RecyclerAdapter(ArrayList<String> resNames,ArrayList<String> rating,ArrayList<String> thumb,ArrayList<Integer> status) {
        this.resNames = resNames;
        this.rating = rating;
        this.thumb = thumb;
        this.status = status;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_layout, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.resName.setText(resNames.get(position));
        holder.ratingTxt.setText(rating.get(position));
        if(status.get(position)==0) {
            holder.orderTxt.setText("Currently not delivering");
            holder.orderTxt.setTextColor(Color.RED);
        }
        else {
            holder.orderTxt.setText("Delivery available");
            holder.orderTxt.setTextColor(Color.GREEN);
        }
        if(thumb.get(position).length()>0) {
            Picasso.get().load(thumb.get(position)).into(holder.resIcon);
        }
    }

    @Override
    public int getItemCount() {
       return resNames.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView orderTxt, ratingTxt, resName;
        ImageView resIcon;

        private RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            orderTxt = itemView.findViewById(R.id.orderTxt);
            ratingTxt = itemView.findViewById(R.id.rating);
            resName = itemView.findViewById(R.id.resName);
            resIcon = itemView.findViewById(R.id.resIcon);
        }
    }
}
