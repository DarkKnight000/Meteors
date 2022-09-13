package com.example.meteors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocalTopAdapter extends RecyclerView.Adapter<LocalTopAdapter.RecyclerViewViewHolder>
{

    private ArrayList<LocalTopItem> arrayList;

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView1;
        public TextView textView2;

        public RecyclerViewViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textView1 = itemView.findViewById(R.id.nickTextView);
            textView2 = itemView.findViewById(R.id.scoreTextView);
        }
    }

    public LocalTopAdapter(ArrayList<LocalTopItem> arrayList)
    {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.local_top_item, viewGroup, false);

        RecyclerViewViewHolder recyclerViewViewHolder = new RecyclerViewViewHolder(view);

        return recyclerViewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder recyclerViewViewHolder, int i)
    {
        LocalTopItem localTopItem = arrayList.get(i);

        if (i == 0)
        {
            recyclerViewViewHolder.textView2.setTextSize(32);
        }
        else
        {
            recyclerViewViewHolder.textView2.setTextSize(24);
        }
        recyclerViewViewHolder.textView1.setText(localTopItem.getNickname());
        recyclerViewViewHolder.textView2.setText(localTopItem.getScore());
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

}
