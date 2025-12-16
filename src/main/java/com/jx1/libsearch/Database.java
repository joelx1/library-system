package com.jx1.libsearch;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database 
{
    /* set this value to false if not using enviromental variables and
    you want to use hardcoded variables instead */
    private static final boolean ev = true;

    private static String URL;
    private static String USER;
    private static String PASSWORD;
        
    static
    {
        if (ev == true)
        {
            URL = System.getenv("DB_URL");
            USER = System.getenv("DB_USER");
            PASSWORD = System.getenv("DB_PASS");
            
            if (URL == null || USER == null || PASSWORD == null)
            {
                throw new RuntimeException("Database environment variables not set. DB_URL, DB_USER, DB_PASS.");
            }
        }
        else
        {
            URL = "URL";
            USER = "USER";
            PASSWORD = "PASSWORD";
        }
    }

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Connection Test
    public static void main(String[] args)
    {
        try (Connection c = Database.getConnection())
        {
            if (c != null)
                {
                System.out.println("Connected");
            }
            else
            {
                System.out.println("Failed to connect");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}