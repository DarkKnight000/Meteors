package com.example.meteors;

import java.security.SecureRandom;
import java.util.Random;

public class GenerateUserID
{

    private static String arrayOfSymb = "QW0ER1TY2UI3OP4AS5DFG6HJK7LZ8XCVB9NM";

    private String GenerateUserId()
    {
        String genID = "";
        SecureRandom rnd = new SecureRandom();
        try
        {
            for (int i = 0; i < 8; i++)
            {
                int randomIndex = rnd.nextInt(arrayOfSymb.length());
                genID += (arrayOfSymb.charAt(randomIndex));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return genID;
    }

    public String getGenerateUserId()
    {
        return GenerateUserId();
    }

}
