package com.example.meteors;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameFragment extends Fragment
{

    TextView goldTextView;
    UserDataActivityPU userDataPopUpActivity;
    TopActivityPU topPopUpActivity;
    public View popupView;

    PopupWindow userDataPopupWindow;
    private ImageView showUserImageView;

    PopupWindow topPopupWindow;
    private ImageView showTopImageView;

    private Button playButton;
    ImageView starImageView;
    AnimationDrawable starAnimation;
    Crypt crypt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_game, null);


        starImageView = v.findViewById(R.id.starImageView);
        starImageView.setBackgroundResource(R.drawable.star);
        starAnimation = (AnimationDrawable) starImageView.getBackground();
        starAnimation.start();

        crypt = new Crypt();

        goldTextView = v.findViewById(R.id.goldTextView);
        goldTextView.setText("" + UserData.getGold());

        playButton = v.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                play();

            }
        });

        showUserImageView = v.findViewById(R.id.showUserImageView);
        showUserImageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                showUserData();
            }
        });
        showTopImageView = v.findViewById(R.id.showTopImageView);
        showTopImageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                showTop();
            }
        });

        userDataPopUpActivity = new UserDataActivityPU();
        topPopUpActivity = new TopActivityPU();

        return v;
    }

    // Показать данные пользователя:
    private void showUserData()
    {
        LayoutInflater inflater = (LayoutInflater) GameFragment.this
                .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.activity_user_data_pu, null, false);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        userDataPopupWindow = new PopupWindow(popupView, width, height, true);
        userDataPopupWindow.setOutsideTouchable(true);
        userDataPopupWindow.setBackgroundDrawable (new ColorDrawable());
        userDataPopupWindow.setFocusable(false);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        userDataPopupWindow.showAtLocation(showUserImageView, Gravity.CENTER, 0, -100); // dropDown рядом с кнопкой


        userDataPopUpActivity.userNickTextView = popupView.findViewById(R.id.userNickTextView);
        userDataPopUpActivity.userIdTextView = popupView.findViewById(R.id.userIdTextView);
        userDataPopUpActivity.userEmailTextView = popupView.findViewById(R.id.userEmailTextView);


        FloatingActionButton exitUserDataButton = popupView.findViewById(R.id.exitUserDataButton);
        exitUserDataButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                userDataPopupWindow.dismiss();
            }
        });

        userDataPopUpActivity.getUserData();
    }

    // Показать ТОП:
    private void showTop()
    {
        LayoutInflater inflater = (LayoutInflater) GameFragment.this
                .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.activity_top_pu, null, false);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        topPopupWindow = new PopupWindow(popupView, width, height, true);
        topPopupWindow.setOutsideTouchable(true);
        topPopupWindow.setBackgroundDrawable (new ColorDrawable());
        topPopupWindow.setFocusable(false);

        FloatingActionButton exitTopButton = popupView.findViewById(R.id.exitTopButton);
        exitTopButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                topPopupWindow.dismiss();
            }
        });

        Button myTopButton = popupView.findViewById(R.id.myTopButton);
        Button globalTopButton = popupView.findViewById(R.id.globalTopButton);
        myTopButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                myTopButton.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
                globalTopButton.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                if (topPopupWindow.isShowing())
                {
                    topPopUpActivity.shLT();
                };
            }
        });
        globalTopButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                globalTopButton.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
                myTopButton.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
            }
        });


        myTopButton.callOnClick();

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        topPopupWindow.showAtLocation(showTopImageView, Gravity.CENTER, 0, -100); // dropDown рядом с кнопкой

        topPopUpActivity.recyclerView = popupView.findViewById(R.id.recyclerView);
        topPopUpActivity.myTopProgress = popupView.findViewById(R.id.myTopProgress);

        topPopUpActivity.shLT();

    }

    // Запустить игру:
    private void play()
    {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        startActivity(intent);
        getActivity().finish();
        return;
    }

}