package com.example.meteors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import java.sql.ResultSet;

public class UserDataActivityPU extends AppCompatActivity
{
    TextView userNickTextView;
    TextView userIdTextView;
    TextView userEmailTextView;
    TextView totalGoldTextView;
    TextView totalGamesTextView;
    TextView totalMeteorsTextView;
    TextView averageTextView;

    Crypt crypt;

    String sqlcmd;

    int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            {

                @Override
                public void onSystemUiVisibilityChange(int visibility)
                {
                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                    {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

        // Отключение ночной темы
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_pu);



    }
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }



    public void getUserData()
    {
        GetUserDataFromDB ms = new GetUserDataFromDB();
        ms.execute();
    }


    private class GetUserDataFromDB extends AsyncTask<String, Void, String>
    {
        String res = "";
        ResultSet rs = null;

        ConnectionDB conDB = new ConnectionDB();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Установка начальных значений:
            userNickTextView.setText(UserData.getNickname());
            userIdTextView.setText(UserData.getID());
            userEmailTextView.setText(UserData.getEmail());
            totalGoldTextView.setText("" + UserData.getGold());
            totalGamesTextView.setText("" + UserData.getTotal_games());
            totalMeteorsTextView.setText("" + UserData.getTotal_meteors());

            try
            {
                int average = UserData.getTotal_meteors()/UserData.getTotal_games();
                averageTextView.setText("" + average);
            }
            catch (Exception e)
            {}
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {

                String result = ""; // = "Database Connection Successful\n";


                String nick = crypt.Encode(UserData.getNickname());
                String password = crypt.Encode(UserData.getPassword());

                sqlcmd = "SELECT * FROM users\n" +
                         "WHERE nickname = '" + nick + "' AND password = '" + password + "'";

                rs = conDB.getConnection(sqlcmd);

                while (rs.next())
                {

                    UserData.setUser_id(rs.getInt("user_id"));
                    UserData.setID(crypt.Decode(rs.getString("ID")));
                    UserData.setNickname(crypt.Decode(rs.getString("nickname")));
                    UserData.setEmail(crypt.Decode(rs.getString("Email")));
                    UserData.setPassword(crypt.Decode(rs.getString("password")));
                    UserData.setGold(rs.getInt("gold"));
                    UserData.setTotal_games(rs.getInt("total_games"));
                    UserData.setTotal_meteors(rs.getInt("total_meteors"));
                    UserData.setOnline(rs.getInt("online"));
                    UserData.setReg_gate(rs.getTimestamp("reg_date"));
                    UserData.setLast_access(rs.getTimestamp("last_access"));
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

            userNickTextView.setText(UserData.getNickname());
            userIdTextView.setText(UserData.getID());
            userEmailTextView.setText(UserData.getEmail());
            totalGoldTextView.setText("" + UserData.getGold());
            totalGamesTextView.setText("" + UserData.getTotal_games());
            totalMeteorsTextView.setText("" + UserData.getTotal_meteors());

            try
            {
                int average = UserData.getTotal_meteors()/UserData.getTotal_games();
                averageTextView.setText("" + average);
            }
            catch (Exception e)
            {}
        }
    }
}