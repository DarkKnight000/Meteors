package com.example.meteors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;

public class TopActivityPU extends AppCompatActivity
{
    public RecyclerView recyclerView;
    public ProgressBar myTopProgress;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_pu);


    }

    public void shLT()
    {
        showLocalTop();
        getLocalTop();
    }

    private void showLocalTop()
    {
        ArrayList<LocalTopItem> recyclerViewItems = new ArrayList<>();

        UserData.sortArray();

        for (int i = 0; i < UserData.userTop.length; i++)
        {
            if (UserData.userTop[i] !=0)
            {
                //scoreTextView.setTextSize(32);

                recyclerViewItems.add(new LocalTopItem((i+1) + ". " + UserData.getNickname(), "" + UserData.userTop[i]));
            }
        }

        // фиксированное количество строк
        recyclerView.setHasFixedSize(true);
        adapter = new LocalTopAdapter(recyclerViewItems);
        layoutManager = new GridLayoutManager(this, 1); // 2, если в 2 столбца

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getLocalTop()
    {
        GetLocalTopFromDB getLT = new GetLocalTopFromDB();
        getLT.execute();
    }

    private class GetLocalTopFromDB extends AsyncTask<String, Void, String>
    {
        String sqlcmd;
        String res = "";
        ResultSet rs = null;
        ConnectionDB conDB = new ConnectionDB();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            myTopProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {

                String result = ""; // = "Database Connection Successful\n";

                sqlcmd = "SELECT * FROM local_score\n" +
                         "WHERE user_id = " + UserData.getUser_id() + "";

                rs = conDB.getConnection(sqlcmd);

                int i = 0;
                while (rs.next())
                {
                    UserData.userTop[i] = rs.getInt(3);
                    i++;
                }

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
            showLocalTop();
            myTopProgress.setVisibility(View.INVISIBLE);
        }
    }
}