package com.example.meteors;

public class LocalTopItem
{
    private String nickname;
    private String score;

    public LocalTopItem(String nickname, String score)
    {
        this.nickname = nickname;
        this.score = score;
    }


    public String getNickname()
    {
        return nickname;
    }

    public String getScore()
    {
        return score;
    }

}
