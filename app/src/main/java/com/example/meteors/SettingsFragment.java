package com.example.meteors;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import java.sql.ResultSet;
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

    public void setSP()
    {
        SetNewSpacePlanetMySql setsp = new SetNewSpacePlanetMySql();
        setsp.execute();
    }

    private class SetNewSpacePlanetMySql extends AsyncTask<String, Void, String>
    {
        String res = "";
        ResultSet rs = null;

        ConnectionDB conDB = new ConnectionDB();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {

                String result = ""; // = "Database Connection Successful\n";

                String sqlcmd;
                sqlcmd = "UPDATE users \n" +
                         "SET space_bg_id = " + UserData.getWhichSpaceUses() + ", \n" +
                         "planet_bg_id = " + UserData.getWhichPlanetUses() + " \n" +
                         "WHERE user_id = " + UserData.getUser_id();
                rs = conDB.getConnection(sqlcmd);
                //Toast.makeText(getContext(), "" + sqlcmd, Toast.LENGTH_LONG).show();

                res = result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }
}