package com.dal.touristapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewAdaptor extends RecyclerView.Adapter<ViewAdaptor.ViewHolder> {

    private final ArrayList<Card> cardArrayList;
    public Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder holder = new ViewHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Card card = cardArrayList.get(position);
        Glide.with(context).load(card.getImg()).into(holder.imageView);
        holder.textView.setText(card.getText());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PlaceActivity.class);
                intent.putExtra("placeName",card.getText());
                intent.putExtra("imageUrl",card.getImg());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    public ViewAdaptor(ArrayList<Card> cardList) {
        cardArrayList = cardList;
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.placeImg);
            textView = itemView.findViewById(R.id.placeTxtView);

        }
    }
}