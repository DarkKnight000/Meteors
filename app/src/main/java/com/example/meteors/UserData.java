package com.example.meteors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class UserData
{
    private static boolean isGuest = true;
    private static int user_id;
    private static String ID;
    private static String nickname;
    private static String Email;
    private static String password;
    private static int gold = 500;
    private static int online;
    private static Timestamp reg_gate;
    private static Timestamp last_access;
    public static int[] userTop = new int[10];

    public static String[][] spaceBgData;
    public static int[][] spaceSettings;
    public static String[][] planetBgData;


    public static boolean getIsGuest()
    {
        return isGuest;
    }
    public static int getUser_id()
    {
        return user_id;
    }
    public static String getID()
    {
        return ID;
    }
    public static String getNickname()
    {
        return nickname;
    }
    public static String getEmail()
    {
        return Email;
    }
    public static String getPassword()
    {
        return password;
    }
    public static int getGold()
    {
        return gold;
    }
    public static int getOnline()
    {
        return online;
    }
    public static Timestamp getReg_gate()
    {
        return reg_gate;
    }
    public static Timestamp getLast_access()
    {
        return last_access;
    }

    public static void setIsGuest(boolean isGuest)
    {
        UserData.isGuest = isGuest;
    }
    public static void setUser_id(int user_id)
    {
        UserData.user_id = user_id;
    }
    public static void setID(String ID)
    {
        UserData.ID = ID;
    }
    public static void setNickname(String nickname)
    {
        UserData.nickname = nickname;
    }
    public static void setEmail(String email)
    {
        Email = email;
    }
    public static void setPassword(String password)
    {
        UserData.password = password;
    }
    public static void setGold(int gold)
    {
        UserData.gold = gold;
    }
    public static void setOnline(int online)
    {
        UserData.online = online;
    }
    public static void setReg_gate(Timestamp reg_gate)
    {
        UserData.reg_gate = reg_gate;
    }
    public static void setLast_access(Timestamp last_access)
    {
        UserData.last_access = last_access;
    }


    public static void sortArray()
    {
        for (int i = UserData.userTop.length-1 ; i > 0 ; i--)
        {
            for (int j = 0 ; j < i ; j++)
            {
                if (UserData.userTop[j] < UserData.userTop[j+1])
                {
                    int tmp = UserData.userTop[j];
                    UserData.userTop[j] = UserData.userTop[j+1];
                    UserData.userTop[j+1] = tmp;
                }
            }
        }
    }

}
