package com.example.meteors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity
{

    int currentApiVersion;

    private String nick_email_id;
    private String password;

    private EditText editTextNick;
    private EditText editTextPassword;

    String sqlcmd;
    String sqlcmd2;

    Crypt crypt;

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
        setContentView(R.layout.activity_login);


        editTextNick = findViewById(R.id.editTextNick);
        editTextPassword = findViewById(R.id.editTextPassword);

        crypt = new Crypt();
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

    public void goToSignUp(View view)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void logIn(View view)
    {
        nick_email_id = editTextNick.getText().toString().trim();
        if (nick_email_id.contains("#"))
        {
            nick_email_id = nick_email_id.toUpperCase();
        }

        String ID = nick_email_id.toUpperCase();
        ID = crypt.Encode("#"+ID);

        nick_email_id = crypt.Encode(nick_email_id);

        password  = editTextPassword.getText().toString().trim();
        password  = crypt.Encode(password);

        sqlcmd = "SELECT * FROM users \n" +
                 "WHERE (nickname = '" + nick_email_id + "' OR Email = '" + nick_email_id + "' OR ID = '" + nick_email_id + "' OR ID = '" + ID + "') " +
                 "AND password = '" + password + "';";

        //Toast.makeText(this, "" + sqlcmd, Toast.LENGTH_LONG).show();

        ConnectMySql ms = new ConnectMySql();
        ms.execute();


    }

    // Вход в игру:
    void goToMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void goToRecovery(View view)
    {
        Intent intent = new Intent(this, RecoveryActivity.class);
        startActivity(intent);
        finish();
    }

    private class ConnectMySql extends AsyncTask<String, Void, String>
    {
        String res = "";
        ResultSet rs = null;

        ConnectionDB conDB = new ConnectionDB();

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, null, "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                UserData.setUser_id(0);
                UserData.setID("");
                UserData.setNickname("");
                UserData.setEmail("");
                UserData.setPassword("");
                UserData.setGold(500);
                UserData.setTotal_games(0);
                UserData.setTotal_meteors(0);
                UserData.setOnline(0);
                UserData.setReg_gate(null);
                UserData.setLast_access(null);

                String result = ""; // = "Database Connection Successful\n";

                rs = conDB.getConnection(sqlcmd);

                while (rs.next())
                {
                    //result = rs.getString(1);

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

                // Обновление последнего входа:
                sqlcmd = "UPDATE users \n" +
                         "SET last_access = CURRENT_TIMESTAMP \n" +
                         "WHERE user_id = " + UserData.getUser_id();
                rs = conDB.getConnection(sqlcmd);

                // Если пользователь существует:
                if(UserData.getUser_id() != 0)
                {
                    UserData.setIsGuest(false);

                    // Локальные рекорды:
                    sqlcmd = "SELECT * FROM local_score \n" +
                             "WHERE user_id = " + UserData.getUser_id() + " \n" +
                             "ORDER BY local_score DESC";
                    rs = conDB.getConnection(sqlcmd);
                    try
                    {
                        int i = 0;
                        while (rs.next())
                        {
                            UserData.userTop[i] = rs.getInt(3);

                            i++;
                        }
                    }
                    catch (Exception e)
                    { }

                    // Загрузка данных о фонах космоса и планетах:
                    try
                    {
                        sqlcmd = "SELECT *\n" +
                                 "FROM `space_bg` " +
                                 "LEFT OUTER JOIN `planet_bg` " +
                                 "ON space_bg.id=planet_bg.id";
                        rs = conDB.getConnection(sqlcmd);
                        int i = 0;
                        rs.last();
                        int rowCount = rs.getRow();
                        rs.beforeFirst();
                        UserData.spaceBgData = new String[rowCount][3];
                        UserData.planetBgData = new String[rowCount][3];
                        while (rs.next())
                        {
                            UserData.spaceBgData[i][0] = rs.getString(1);
                            UserData.spaceBgData[i][1] = rs.getString(2);
                            UserData.spaceBgData[i][2] = rs.getString(3);
                            i++;
                        }
                        i=0;
                        rs.beforeFirst();
                        while (rs.next())
                        {
                            if (rs.getString(5) != null)
                            {
                                UserData.planetBgData[i][0] = rs.getString(5);
                                UserData.planetBgData[i][1] = rs.getString(6);
                                UserData.planetBgData[i][2] = rs.getString(7);
                            }
                            else
                            {
                                break;
                            }
                            i++;
                        }
                    }
                    catch (Exception e) { }

                    sqlcmd = "SELECT * FROM space_settings \n" +
                             "WHERE user_id = " + UserData.getUser_id();
                    rs = conDB.getConnection(sqlcmd);
                    try
                    {
                        int i = 0;
                        rs.last();
                        int rowCount = rs.getRow();
                        rs.beforeFirst();
                        UserData.spaceSettings = new int[rowCount][3];
                        while (rs.next())
                        {
                            UserData.spaceSettings[i][0] = rs.getInt(2);
                            UserData.spaceSettings[i][1] = rs.getInt(3);
                            UserData.spaceSettings[i][2] = rs.getInt(4);

                            i++;
                        }
                    }
                    catch (Exception e)
                    { }

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
            progressDialog.dismiss();

            if (conDB.con == null || conDB.st == null)
            {
                Toast.makeText(LoginActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(UserData.getUser_id() != 0)
                {
                    goToMainActivity();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Invalid  nickname/email/ID  and/or  password", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}