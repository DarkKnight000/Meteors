package com.example.meteors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

public class SpacePlanetAdapter extends RecyclerView.Adapter<SpacePlanetAdapter.SpacePlanetViewHolder>
{

    private ArrayList<SpacePlanetItem> arrayList;
    Context context;

    //private int lastPosition = -1;
    int row_index = -1;

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder
    {
        //public TextView textView1;
        //public TextView textView2;

        public RecyclerViewViewHolder(@NonNull View itemView)
        {
            super(itemView);
            //textView1 = itemView.findViewById(R.id.nickTextView);
            //textView2 = itemView.findViewById(R.id.scoreTextView);
        }
    }

    public SpacePlanetAdapter(ArrayList<SpacePlanetItem> arrayList)
    {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override

    //public SpacePlanetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    public SpacePlanetAdapter.SpacePlanetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.space_planet_item, viewGroup, false);

        SpacePlanetViewHolder recyclerViewViewHolder = new SpacePlanetViewHolder(view);

        return recyclerViewViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final SpacePlanetViewHolder recyclerViewViewHolder, @SuppressLint("RecyclerView") final int position)
    {
        SpacePlanetItem spacePlanetItem = arrayList.get(position);

        // Нажатие на элемент:
        recyclerViewViewHolder.cardViewColor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                row_index=position;
                UserData.setWhichSpaceUses(position);

                SettingsFragment sf = new SettingsFragment();
                sf.setSP();

                notifyDataSetChanged();
            }
        });

        // Изменение цвета при нажатии:
        if (row_index==position || position==UserData.getWhichSpaceUses())
        {
            recyclerViewViewHolder.cardViewColor.setCardBackgroundColor(Color.parseColor("#008000"));
        }
        else
        {
            recyclerViewViewHolder.cardViewColor.setCardBackgroundColor(Color.GRAY);
        }


        // Установка изображения космоса:
        recyclerViewViewHolder.spaceImageView.setImageResource(spacePlanetItem.getImageResource());
        // Установка названия космоса:
        recyclerViewViewHolder.spaceNameTextView.setText(spacePlanetItem.getSpaceNameTextView());
        // Установка стоимости:
        recyclerViewViewHolder.spaceCostTextView.setText(spacePlanetItem.getCostTextView());
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    class SpacePlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public CardView cardViewColor;
        public ImageView spaceImageView;
        public TextView spaceNameTextView;
        public RelativeLayout main_layout;
        public TextView spaceCostTextView;

        // Нажатие на элемент:
        @Override
        public void onClick(View view)
        {
            //По нажатию на элемент

        }


        public SpacePlanetViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            cardViewColor = itemView.findViewById(R.id.cardViewColor);
            spaceImageView = itemView.findViewById(R.id.spaceImageView);
            spaceNameTextView = itemView.findViewById(R.id.spaceNameTextView);
            main_layout = itemView.findViewById(R.id.main_layout);
            spaceCostTextView = itemView.findViewById(R.id.spaceCostTextView);
        }
    }
}
