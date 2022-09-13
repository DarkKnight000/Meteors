package com.example.meteors;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsFragment extends Fragment
{

    public static TextView goldTextView;

    RecyclerView recyclerView2;
    RecyclerView.Adapter adapter2;
    RecyclerView.LayoutManager layoutManager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_settings, container, false);

        View v = inflater.inflate(R.layout.fragment_settings, null);

        goldTextView = v.findViewById(R.id.goldTextView);
        goldTextView.setText("" + UserData.getGold());

        recyclerView2 = v.findViewById(R.id.spacePlanetRecyclerView);

        showSpacePlanet();

        return v;
    }

    void showSpacePlanet()
    {

        ArrayList<SpacePlanetItem> recyclerViewItems = new ArrayList<>();

        UserData.sortArray();

        Settings.main_activity[0] = R.drawable.space1;
        Settings.main_activity[1] = R.drawable.space1;
        Settings.main_activity[2] = R.drawable.space2;
        Settings.main_activity[3] = R.drawable.space3;
        Settings.main_activity[4] = R.drawable.space4;
        Settings.main_activity[5] = R.drawable.space5;
        Settings.main_activity[6] = R.drawable.space6;
        Settings.main_activity[7] = R.drawable.space7;
        Settings.main_activity[8] = R.drawable.space8;
        Settings.main_activity[9] = R.drawable.space9;
        Settings.main_activity[10] = R.drawable.space10;
        Settings.main_activity[11] = R.drawable.space11;
        Settings.main_activity[12] = R.drawable.space12;
        try
        {
            recyclerViewItems.add(new SpacePlanetItem(R.drawable.space_background,"SPACE #0", "FREE"));

            if (!UserData.getIsGuest())
            {
                for (int i = 1; i < UserData.spaceBgData.length; i++)
                {
                    recyclerViewItems.add(new SpacePlanetItem(Settings.main_activity[i], "" + UserData.spaceBgData[i][1], "" + UserData.spaceBgData[i][2]));
                }
            }
        }
        catch (Exception e)
        { }

        // фиксированное количество строк
        recyclerView2.setHasFixedSize(true);
        adapter2 = new SpacePlanetAdapter(recyclerViewItems);
        layoutManager2 = new GridLayoutManager(getActivity(), 2); // 2, если в 2 столбца

        recyclerView2.setAdapter(adapter2);
        recyclerView2.setLayoutManager(layoutManager2);
    }
}