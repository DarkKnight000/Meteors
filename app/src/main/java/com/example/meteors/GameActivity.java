package com.example.meteors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.Arrays;


public class GameActivity extends AppCompatActivity
{

    String sqlcmd;
    int currentApiVersion;
    Animator.AnimatorListener animatorListener;

    int score = 0;
    int extraGold = 0;
    TextView textScore;
    TextView textLevel;
    TextView textPlayButton;
    TextView textGame;
    TextView earnGoldTextView;
    TextView extraGoldTextView;
    RelativeLayout main_layout;

    CardView earnGoldCardView;
    RelativeLayout relativeGameOver;
    RelativeLayout relativeDescription;
    LinearLayout extraGoldLinearLayout;

    boolean isGameOver = false;
    boolean isSendScoreToDB = false;
    boolean[] isLevelUse;

    SoundPool boom_sound;

    int height;
    int width;

    int duration = 8000;
    double k = 0.99;

    AnimationDrawable mAnimation;

    ImageView previewLittleMeteor;
    ImageView previewBigMeteor;
    ImageView meteorImage1;
    ImageView meteorImage2;
    ImageView meteorImage3;
    ImageView meteorImage4;
    ImageView meteorImage5;
    ImageView meteorImage6;
    ImageView meteorImage7;
    ImageView meteorImage8;
    ImageView meteorImage9;
    ImageView meteorImage10;
    ImageView meteorImage11;
    ImageView meteorImage12;
    ImageView meteorImage13;
    ImageView meteorImage14;
    ImageView meteorImage15;
    ImageView meteorImage16;
    ImageView meteorImage17;
    ImageView meteorImage18;
    ImageView meteorImage19;
    ImageView meteorImage20;
    ImageView meteorImage21;

    int meteorHigh = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        currentApiVersion = Build.VERSION.SDK_INT;

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
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
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

        // Хз для чего
        //setTheme(R.style.SplashScreenTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);




        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        textScore = findViewById(R.id.textScore);
        textLevel = findViewById(R.id.textLevel);
        textPlayButton = findViewById(R.id.textPlayButton);
        textGame = findViewById(R.id.textGame);
        earnGoldTextView = findViewById(R.id.earnGoldTextView);
        earnGoldCardView = findViewById(R.id.earnGoldCardView);
        extraGoldTextView = findViewById(R.id.extraGoldTextView);

        relativeGameOver = findViewById(R.id.relativeGameOver);
        relativeGameOver.setVisibility(View.VISIBLE);
        relativeDescription = findViewById(R.id.relativeDescription);
        extraGoldLinearLayout = findViewById(R.id.extraGoldLinearLayout);

        isLevelUse = new boolean[10];
        boom_sound = new SoundPool(0, AudioManager.STREAM_MUSIC, 100);
        boom_sound.load(this, R.raw.boom_sound, 1);

        main_layout = findViewById(R.id.main_layout);
        if (UserData.getWhichSpaceUses() != 0)
        {
            main_layout.setBackgroundResource(Settings.main_activity[UserData.getWhichSpaceUses()]);
        }

        animatorListener = new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                if (isGameOver)
                {
                    relativeGameOver.setVisibility(View.VISIBLE);

                    meteorImage1.animate().cancel();
                    meteorImage2.animate().cancel();
                    meteorImage3.animate().cancel();
                    meteorImage4.animate().cancel();
                    meteorImage5.animate().cancel();
                    meteorImage6.animate().cancel();
                    meteorImage7.animate().cancel();
                    meteorImage8.animate().cancel();
                    meteorImage9.animate().cancel();
                    meteorImage10.animate().cancel();
                    meteorImage11.animate().cancel();
                    meteorImage12.animate().cancel();
                    meteorImage13.animate().cancel();
                    meteorImage14.animate().cancel();
                    meteorImage15.animate().cancel();
                    meteorImage16.animate().cancel();
                    meteorImage17.animate().cancel();
                    meteorImage18.animate().cancel();
                    meteorImage19.animate().cancel();
                    meteorImage20.animate().cancel();
                    meteorImage21.animate().cancel();

                    if (meteorImage1.getTranslationY() == height+150)
                    {
                        meteorImage1.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage1.getBackground();
                    }
                    else if (meteorImage2.getTranslationY() == height+150)
                    {
                        meteorImage2.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage2.getBackground();
                    }
                    else if (meteorImage3.getTranslationY() == height+150)
                    {
                        meteorImage3.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage3.getBackground();
                    }
                    else if (meteorImage4.getTranslationY() == height+150)
                    {
                        meteorImage4.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage4.getBackground();
                    }
                    else if (meteorImage5.getTranslationY() == height+150)
                    {
                        meteorImage5.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage5.getBackground();
                    }
                    else if (meteorImage6.getTranslationY() == height+150)
                    {
                        meteorImage6.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage6.getBackground();
                    }
                    else if (meteorImage7.getTranslationY() == height+150)
                    {
                        meteorImage7.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage7.getBackground();
                    }
                    else if (meteorImage8.getTranslationY() == height+150)
                    {
                        meteorImage8.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage8.getBackground();
                    }
                    else if (meteorImage9.getTranslationY() == height+150)
                    {
                        meteorImage9.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage9.getBackground();
                    }
                    else if (meteorImage10.getTranslationY() == height+150)
                    {
                        meteorImage10.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage10.getBackground();
                    }
                    else if (meteorImage11.getTranslationY() == height-150)
                    {
                        meteorImage11.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage11.getBackground();
                    }
                    else if (meteorImage12.getTranslationY() == height+150)
                    {
                        meteorImage1.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage1.getBackground();
                    }
                    else if (meteorImage13.getTranslationY() == height+150)
                    {
                        meteorImage13.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage13.getBackground();
                    }
                    else if (meteorImage14.getTranslationY() == height+150)
                    {
                        meteorImage14.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage14.getBackground();
                    }
                    else if (meteorImage15.getTranslationY() == height+150)
                    {
                        meteorImage15.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage15.getBackground();
                    }
                    else if (meteorImage16.getTranslationY() == height+150)
                    {
                        meteorImage16.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage16.getBackground();
                    }
                    else if (meteorImage17.getTranslationY() == height+150)
                    {
                        meteorImage17.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage17.getBackground();
                    }
                    else if (meteorImage18.getTranslationY() == height+150)
                    {
                        meteorImage18.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage18.getBackground();
                    }
                    else if (meteorImage19.getTranslationY() == height+150)
                    {
                        meteorImage19.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage19.getBackground();
                    }
                    else if (meteorImage20.getTranslationY() == height+150)
                    {
                        meteorImage20.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage20.getBackground();
                    }
                    else if (meteorImage21.getTranslationY() == height+150)
                    {
                        meteorImage21.setBackgroundResource(R.drawable.boom);
                        mAnimation = (AnimationDrawable) meteorImage21.getBackground();
                    }

                    mAnimation.setOneShot(true);
                    mAnimation.start();

                    if (UserData.userTop[0] < score)
                    {
                        textGame.setText("NEW RECORD!!!!!");
                    }
                    else
                    {
                        textGame.setText("Game over");
                    }

                    isLevelUse = new boolean[10];

                    if (!isSendScoreToDB)
                    {
                        isSendScoreToDB = true;
                        SendScoreToDB ms = new SendScoreToDB();
                        ms.execute();
                    }
                }
                isGameOver = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        };

        textScore.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                textLevel.setText("" + duration);
                /*if ((score % 10) == 0 && score>=10)
                {
                    duration = (int) (duration * k);
                    level++;
                }*/

                if ((score % 10) == 0 && score>=10)
                {
                    if (score % 200 == 0 && score <= 1000)
                    {
                        k += 0.001;
                    }
                    else if (score > 1000)
                    {
                        k = 1;

                        // Дополнительное золото:
                        extraGoldLinearLayout.setVisibility(View.VISIBLE);
                        extraGold += 2;
                        extraGoldTextView.setText("" + extraGold);
                    }
                    duration = (int) (duration * k);
                }


                if ((score / 100) == 10 && !isLevelUse[9])
                {
                    initLevel10();
                    isLevelUse[9] = true;
                }
                else if ((score / 100) == 9 && !isLevelUse[8])
                {
                    initLevel9();
                    isLevelUse[8] = true;
                }
                else if ((score / 100) == 8 && !isLevelUse[7])
                {
                    initLevel8();
                    isLevelUse[7] = true;
                }
                else if ((score / 100) == 7 && !isLevelUse[6])
                {
                    initLevel7();
                    isLevelUse[6] = true;
                }
                else if ((score / 100) == 6 && !isLevelUse[5])
                {
                    initLevel6();
                    isLevelUse[5] = true;
                }
                else if ((score / 100) == 5 && !isLevelUse[4])
                {
                    initLevel5();
                    isLevelUse[4] = true;
                }
                else if ((score / 100) == 4 && !isLevelUse[3])
                {
                    initLevel4();
                    isLevelUse[3] = true;
                }
                else if ((score / 100) == 3 && !isLevelUse[2])
                {
                    initLevel3();
                    isLevelUse[2] = true;
                }
                else if ((score / 100) == 2 && !isLevelUse[1])
                {
                    initLevel2();
                    isLevelUse[1] = true;
                }
                else if ((score / 100) == 1 && !isLevelUse[0])
                {
                    initLevel1();
                    isLevelUse[0] = true;
                }
            }
        });


        initAnimation();
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

    // Инициализация анимации:
    void initAnimation()
    {
        previewLittleMeteor = findViewById(R.id.previewLittleMeteor);
        initMeteorAnimation(previewLittleMeteor);
        previewBigMeteor = findViewById(R.id.previewBigMeteor);
        initMeteorAnimation(previewBigMeteor);

        meteorImage1 = findViewById(R.id.meteorImage1);
        initMeteorAnimation(meteorImage1);

        meteorImage2 = findViewById(R.id.meteorImage2);
        initMeteorAnimation(meteorImage2);

        meteorImage3 = findViewById(R.id.meteorImage3);
        initMeteorAnimation(meteorImage3);

        meteorImage4 = findViewById(R.id.meteorImage4);
        initMeteorAnimation(meteorImage4);

        meteorImage5 = findViewById(R.id.meteorImage5);
        initMeteorAnimation(meteorImage5);

        meteorImage6 = findViewById(R.id.meteorImage6);
        initMeteorAnimation(meteorImage6);

        meteorImage7 = findViewById(R.id.meteorImage7);
        initMeteorAnimation(meteorImage7);

        meteorImage8 = findViewById(R.id.meteorImage8);
        initMeteorAnimation(meteorImage8);

        meteorImage9 = findViewById(R.id.meteorImage9);
        initMeteorAnimation(meteorImage9);

        meteorImage10 = findViewById(R.id.meteorImage10);
        initMeteorAnimation(meteorImage10);

        meteorImage11 = findViewById(R.id.meteorImage11);
        initMeteorAnimation(meteorImage11);

        meteorImage12 = findViewById(R.id.meteorImage12);
        initMeteorAnimation(meteorImage12);

        meteorImage13 = findViewById(R.id.meteorImage13);
        initMeteorAnimation(meteorImage13);

        meteorImage14 = findViewById(R.id.meteorImage14);
        initMeteorAnimation(meteorImage14);

        meteorImage15 = findViewById(R.id.meteorImage15);
        initMeteorAnimation(meteorImage15);

        meteorImage16 = findViewById(R.id.meteorImage16);
        initMeteorAnimation(meteorImage16);

        meteorImage17 = findViewById(R.id.meteorImage17);
        initMeteorAnimation(meteorImage17);

        meteorImage18 = findViewById(R.id.meteorImage18);
        initMeteorAnimation(meteorImage18);

        meteorImage19 = findViewById(R.id.meteorImage19);
        initMeteorAnimation(meteorImage19);

        meteorImage20 = findViewById(R.id.meteorImage20);
        initMeteorAnimation(meteorImage20);

        meteorImage21 = findViewById(R.id.meteorImage21);
        initMeteorAnimation(meteorImage21);

    }
    // Инициализация анимации метеоров:
    void initMeteorAnimation(ImageView meteorImage)
    {
        meteorImage.setBackgroundResource(R.drawable.meteor);
        mAnimation = (AnimationDrawable) meteorImage.getBackground();
        mAnimation.start();
    }

    // 0 уровень:
    void initLevel0()
    {
        initLevels(meteorImage1);
        initLevels(meteorImage3);
        initLevels(meteorImage5);
        initLevels(meteorImage7);
        initLevels(meteorImage9);
        initLevels(meteorImage11);
        initLevels(meteorImage13);
        initLevels(meteorImage15);
        initLevels(meteorImage17);
        initLevels(meteorImage19);
        initLevels(meteorImage21);
    }
    // 1 уровень:
    void initLevel1()
    {
        initLevels(meteorImage4);
    }
    // 2 уровень:
    void initLevel2()
    {
        initLevels(meteorImage18);
    }
    // 3 уровень:
    void initLevel3()
    {
        initLevels(meteorImage8);
    }
    // 4 уровень:
    void initLevel4()
    {
        initLevels(meteorImage14);
    }
    // 5 уровень:
    void initLevel5()
    {
        initLevels(meteorImage10);
    }
    // 6 уровень:
    void initLevel6()
    {
        initLevels(meteorImage12);
    }
    // 7 уровень:
    void initLevel7()
    {
        initLevels(meteorImage6);
    }
    // 8 уровень:
    void initLevel8()
    {
        initLevels(meteorImage16);
    }
    // 9 уровень:
    void initLevel9()
    {
        initLevels(meteorImage2);
    }
    // 10 уровень:
    void initLevel10()
    {
        initLevels(meteorImage20);
    }

    // Инициализация нового уровня:
    void initLevels(ImageView meteorImage)
    {
        meteorHigh = (int)(Math.random()*2000);
        meteorImage.setY(-300-meteorHigh);
        meteorImage.animate().translationY(height+150).setDuration(duration+meteorHigh).setInterpolator(new LinearInterpolator()).setListener(animatorListener).start();
    }

    public void imageClick1(View view)
    {
        boomMeteor(meteorImage1);
    }
    public void imageClick2(View view)
    {
        boomMeteor(meteorImage2);
    }
    public void imageClick3(View view)
    {
        boomMeteor(meteorImage3);
    }
    public void imageClick4(View view)
    {
        boomMeteor(meteorImage4);
    }
    public void imageClick5(View view)
    {
        boomMeteor(meteorImage5);
    }
    public void imageClick6(View view)
    {
        boomMeteor(meteorImage6);
    }
    public void imageClick7(View view)
    {
        boomMeteor(meteorImage7);
    }
    public void imageClick8(View view)
    {
        boomMeteor(meteorImage8);
    }
    public void imageClick9(View view)
    {
        boomMeteor(meteorImage9);
    }
    public void imageClick10(View view)
    {
        boomMeteor(meteorImage10);
    }
    public void imageClick11(View view)
    {
        boomMeteor(meteorImage11);
    }
    public void imageClick12(View view)
    {
        boomMeteor(meteorImage12);
    }
    public void imageClick13(View view)
    {
        boomMeteor(meteorImage13);
    }
    public void imageClick14(View view)
    {
        boomMeteor(meteorImage14);
    }
    public void imageClick15(View view)
    {
        boomMeteor(meteorImage15);
    }
    public void imageClick16(View view)
    {
        boomMeteor(meteorImage16);
    }
    public void imageClick17(View view)
    {
        boomMeteor(meteorImage17);
    }
    public void imageClick18(View view)
    {
        boomMeteor(meteorImage18);
    }
    public void imageClick19(View view)
    {
        boomMeteor(meteorImage19);
    }
    public void imageClick20(View view)
    {
        boomMeteor(meteorImage20);
    }
    public void imageClick21(View view)
    {
        boomMeteor(meteorImage21);
    }


    // Не работает OnTouch (
    private void boomMeteor (ImageView image)
    {
        boom_sound.play(1, 1f, 1f, 1, 0, 1f);
        isGameOver = false;
        meteorHigh = (int)(Math.random()*2000);

        image.setY(-300-meteorHigh);
        image.animate().translationY(height+150).setDuration(duration+meteorHigh).setInterpolator(new LinearInterpolator());

        gameScore();
    }

    // Подсчёт очков:
    void  gameScore()
    {
        score++;
        textScore.setText("" + score);
    }

    // Начать игру:
    public void playButton(View view)
    {
        Button buttonPlay = findViewById(R.id.playButton);
        buttonPlay.setText("Restart");

        relativeGameOver.setVisibility(View.INVISIBLE);
        relativeDescription.setVisibility(View.VISIBLE);
    }

    void play_restart()
    {
        isSendScoreToDB = false;
        initAnimation();
        score = 0;
        duration = 8000;
        extraGold = 0;
        extraGoldLinearLayout.setVisibility(View.INVISIBLE);
        k = 0.99;
        textScore.setText("" + score);

        meteorImage2.setY(-300);
        meteorImage4.setY(-300);
        meteorImage6.setY(-300);
        meteorImage8.setY(-300);
        meteorImage10.setY(-300);
        meteorImage12.setY(-300);
        meteorImage14.setY(-300);
        meteorImage16.setY(-300);
        meteorImage18.setY(-300);
        meteorImage20.setY(-300);

        initLevel0();
    }

    public void playGame(View view)
    {
        textScore.setVisibility(View.VISIBLE);
        play_restart();

        relativeDescription.setVisibility(View.INVISIBLE);

    }

    public void goToMenu(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
        return;
    }

    private class SendScoreToDB extends AsyncTask<String, Void, String>
    {
        String res = "";
        ResultSet rs;
        ConnectionDB conDB = new ConnectionDB();

        int gold = 0;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


            if (score >= 150)
            {
                gold = (score - 50)/20;
                gold += (int)(Math.random()*(score/10));
                if (UserData.getGold()*0.01 >= score*0.5)
                {
                    gold += score*0.5;
                }
                else
                {
                    gold += UserData.getGold()*0.01;
                }
            }
            gold += extraGold;
            earnGoldTextView.setText("" + gold);

            earnGoldCardView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String result = ""; // = "Database Connection Successful\n";

                try
                {
                    UserData.setGold(UserData.getGold() + gold);
                    sqlcmd = "UPDATE users \n" +
                             "SET \n" +
                             "gold = gold + " + gold + ", \n" +
                             "total_games = total_games + 1, \n" +
                             "total_meteors = total_meteors + " + score + " \n" +
                             "WHERE user_id = " + UserData.getUser_id();
                    rs = conDB.getConnection(sqlcmd);

                    if (UserData.userTop[9] == 0 && score > 0)
                    {
                        sqlcmd = "INSERT INTO local_score \n" +
                                 "(user_id, local_score) \n" +
                                 "VALUES \n" +
                                 "(" + UserData.getUser_id() + ", " + score + ")";
                        UserData.userTop[9] = score;
                        rs = conDB.getConnection(sqlcmd);
                    }
                    else if (UserData.userTop[9] < score)
                    {
                        sqlcmd = "UPDATE local_score \n" +
                                 "SET \n" +
                                 "local_score = " + score + " \n" +
                                 "WHERE user_id = " + UserData.getUser_id() + " AND local_score = " + UserData.userTop[9];
                        UserData.userTop[9] = score;
                        rs = conDB.getConnection(sqlcmd);
                    }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
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

            UserData.sortArray();

            UserData.setGold(UserData.getGold() + gold);
            UserData.setTotal_games(UserData.getTotal_games() + 1);
            UserData.setTotal_meteors(UserData.getTotal_meteors() + score);
        }
    }


}