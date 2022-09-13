package com.example.meteors;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.sql.ResultSet;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity
{

    int isFragmentSelected = 0;

    int currentApiVersion;

    Button buttonGame;
    Button buttonSettings;
    Button buttonMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //UserData ud = new UserData();
        //ud.getDataFromDb();

        buttonGame = findViewById(R.id.buttonGame);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonMessages = findViewById(R.id.buttonMessages);


        openGame(null);
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


    public void openGame(View view)
    {
        if (isFragmentSelected != 2)
        {
            buttonSettings.animate().scaleX(1f).scaleY(1f).translationY(0).setDuration(200).start();
            buttonMessages.animate().scaleX(1f).scaleY(1f).translationY(0).setDuration(200).start();

            buttonGame.animate().scaleX(1.2f).scaleY(1.2f).translationY(-30).setDuration(200).start();

            GameFragment gameFragment = new GameFragment();
            setNewFragment(gameFragment);

            buttonGame.setText("Game");
            buttonSettings.setText(null);
            buttonMessages.setText(null);

            isFragmentSelected = 2;
        }

    }
    public void openSettings(View view)
    {
        if (isFragmentSelected != 1)
        {
            buttonGame.animate().scaleX(1f).scaleY(1f).translationY(0).setDuration(200).start();
            buttonMessages.animate().scaleX(1f).scaleY(1f).translationY(0).setDuration(200).start();

            buttonSettings.animate().scaleX(1.2f).scaleY(1.2f).translationY(-30).setDuration(200).start();

            SettingsFragment settingsFragment = new SettingsFragment();
            setNewFragment(settingsFragment);

            buttonGame.setText(null);
            buttonSettings.setText("Settings");
            buttonMessages.setText(null);

            isFragmentSelected = 1;
        }

    }
    public void openMessages(View view)
    {
        /*if (isFragmentSelected != 3)
        {
            buttonGame.animate().scaleX(1f).scaleY(1f).translationY(0).setDuration(200).start();
            buttonSettings.animate().scaleX(1f).scaleY(1f).translationY(0).setDuration(200).start();

            buttonMessages.animate().scaleX(1.2f).scaleY(1.2f).translationY(-30).setDuration(200).start();

            SettingsFragment settingsFragment = new SettingsFragment();
            setNewFragment(settingsFragment);

            buttonGame.setText(null);
            buttonSettings.setText(null);
            buttonMessages.setText("Messages");

            isFragmentSelected = 3;
        }*/


    }

    private void setNewFragment(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayoutGame, fragment);
        //ft.addToBackStack(null);
        ft.commit();
    }

}