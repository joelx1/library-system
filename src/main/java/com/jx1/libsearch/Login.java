package com.jx1.libsearch;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Login
{
    public static void main(String args[])
    {
        try (Connection con = Database.getConnection();
        Scanner s = new Scanner(System.in))
        {
            System.out.println("=== Welcome to library-search ===");
        
            // Login loop
            boolean l = false; // logged in
            int uid = -1;
            String r = ""; // role

            while (!l)
            {
                System.out.print("Enter username: ");
                String uname = s.nextLine();

                System.out.print("Enter password: ");
                String pw = s.nextLine();

                String q = "SELECT user_id, name, role FROM users WHERE uname = ? AND password = ?"; // query
                try (PreparedStatement stmt = con.prepareStatement(q))
                {
                    stmt.setString(1, uname);
                    stmt.setString(2, pw);

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next())
                    {    
                        uid = rs.getInt("user_id");
                        String nm = rs.getString("name");
                        r = rs.getString("role");
                        l = true;
                        System.out.println("Login successful");
                        System.out.println("Welcome " + nm);
                        if (r.equalsIgnoreCase("admin"))
                        {
                            System.out.println("You have administrative access");
                        }
                    } 
                    else
                    {
                        System.out.println("Invalid username or password. Try again.");
                    }
                }
            }

            // Menu loop
            boolean exit = false;
            while (!exit)
            {
                printMenu(r);
                System.out.print("Choose an option: ");
                String c = s.nextLine(); // option chosen

                switch (c)
                {
                    case "0":
                        exit = true;
                        System.out.println("Logging out...");
                        break;
                    case "1":
                        viewBooks(con);
                    break;
                    case "2":
                        if (r.equalsIgnoreCase("admin"))
                        {
                            addBook(con, s);
                        }
                        else
                        {
                            borrowBook(con, s, uid);
                        }
                        break;
                    case "3":
                        if (r.equalsIgnoreCase("admin"))
                        {
                            removeBook(con, s);
                        }
                        else
                        {
                            returnBook(con, s, uid);
                        }
                        break;
                    case "4":
                        exit = true;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void printMenu(String r)
    {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. View Books");

        if (r.equalsIgnoreCase("admin"))
        {
            System.out.println("2. Add Book");
            System.out.println("3. Remove Book");
            System.out.println("0. Logout");
        }
        else
        {
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("0. Logout");
        }
    }

    private static void viewBooks(Connection con) throws SQLException
    {
        String q = "SELECT item_id, title, author, genre, available_copies FROM books"; // query
        try (PreparedStatement stmt = con.prepareStatement(q);
        ResultSet rs = stmt.executeQuery())
        {
            System.out.println("\n--- Books in Library ---");
            while (rs.next())
            {
                System.out.printf("%d: %s by %s [%s] (Available: %d)%n",
                    rs.getInt("item_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("available_copies"));
            }
        }
    }

    // Placeholders
    private static void borrowBook(Connection con, Scanner s, int uid)
    {
        System.out.println("Borrow book functionality to be implemented...");
    }

    private static void returnBook(Connection con, Scanner s, int uid)
    {
        System.out.println("Return book functionality to be implemented...");
    }

    private static void addBook(Connection con, Scanner s)
    {
        System.out.println("Add book functionality to be implemented...");
    }

    private static void removeBook(Connection con, Scanner s)
    {
        System.out.println("Remove book functionality to be implemented...");
    }
}