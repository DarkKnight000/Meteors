package com.example.meteors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB
{
    Crypt crypt = new Crypt();
    private static final String url = ")JPддD:kbbУ8rцAm#нШeLНeRg1MьЩчEIьwб7ч?@ЕхМ_9vf5ZHPЬйMОдОц+иPe*8l3L";
    private static final String user = "EIьwб7ч?";
    private static final String pass = "Ца8iёЬDхvf";

    /*private static final String url = "jdbc:mysql://f0586228.xsph.ru/f0586228_test?characterEncoding=utf8";  //latin1
    private static final String user = "f0586228";
    private static final String pass = "deuzatihza";*/

    Connection con;
    Statement st;
    ResultSet rs;

    int isUserCreated;

    public ResultSet getConnection(String sqlcmd)
    {
        try
        {
            con = DriverManager.getConnection(crypt.Decode(url), crypt.Decode(user), crypt.Decode(pass));
            st = con.createStatement();
            rs = st.executeQuery(sqlcmd);
            //con.close();
        }
        catch (SQLException e)
        {
            try
            {
                st.executeUpdate(sqlcmd);
                isUserCreated = 2;
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return rs;
    }

}
