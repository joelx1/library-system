package com.jx1.libsystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class MainApp
{
    public static void main(String args[])
    {
        try (Connection con = Database.getConnection();
        Scanner s = new Scanner(System.in))
        {
            System.out.println("=== Welcome to library-system ===");

            // Menu Loop
            boolean ls = false;
            while (!ls)
            {
                System.out.println("1. Login");
                System.out.println("2. Sign up");
                System.out.print("Choose an option: ");
                String lin = s.nextLine();
                switch (lin)
                {
                    case "1" -> ls = true;
                    case "2" -> createAccount(con, s);
                    default -> System.out.println("Invalid option. Try again.");
                }
            }

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

                String q = "SELECT user_id, name, role, password FROM users WHERE uname = ?"; // query
                try (PreparedStatement st = con.prepareStatement(q))
                {
                    st.setString(1, uname);
                    ResultSet rs = st.executeQuery();
                    if (rs.next())
                    {    
                        String hPw = rs.getString("password"); // reads hashed pw
                        if (PasswordHash.checkpw(pw, hPw))
                        {
                            uid = rs.getInt("user_id");
                            String nm = rs.getString("name");
                            r = rs.getString("role");
                            l = true;
                            System.out.println("Login successful");
                            System.out.println();
                            System.out.println("Welcome " + nm);
                            if (r.equalsIgnoreCase("admin"))
                            {
                                System.out.println("You have administrative access");
                            }
                        }
                        else
                        {
                            System.out.println("Invalid password. Try again.");
                        }
                    } 
                    else
                    {
                        System.out.println("Username not found");
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
                    case "0" -> {
                        exit = true;
                        System.out.println("Logging out...");
                    }
                    case "1" -> viewBooks(con);
                    case "2" -> borrowBook(con, s, uid);
                    case "3" -> {
                        if (r.equalsIgnoreCase("admin"))
                            returnBook(con, s, uid);
                    }
                    case "4" -> {
                        if (r.equalsIgnoreCase("admin"))
                            addBook(con, s);
                    }
                    case "5" -> {
                        if (r.equalsIgnoreCase("admin"))
                            removeBook(con, s);
                    }
                    default -> System.out.println("Invalid option. Try again.");
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
        System.out.println("2. Borrow Book");
        System.out.println("3. Return Book");
        if (r.equalsIgnoreCase("admin"))
        {
            System.out.println("4. Add Book");
            System.out.println("5. Remove Book");
        }
            System.out.println("0. Logout");
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

    private static void createAccount(Connection con, Scanner s)
    {
        try
        {
            System.out.print("Enter a username: ");
            String uname = s.nextLine();

            System.out.print("Enter full name: ");
            String name = s.nextLine();

            System.out.print("Enter a password: ");
            String pw = s.nextLine();

            String hPw = PasswordHash.hashpw(pw); // hashing the password

            String q = """
                INSERT INTO users (uname, name, password, role)
                VALUES (?, ?, ?, 'user')
            """;

            try (PreparedStatement st = con.prepareStatement(q))
            {
                st.setString(1, uname);
                st.setString(2, name);
                st.setString(3, hPw);
                st.executeUpdate();
                System.out.println("Account created successfully!");
            }
        }
        catch (SQLException e)
        {
            if (e.getMessage().contains("unique"))
            {
                System.out.println("This username already exists. Please choose another one.");
            }
            else
            {
                e.printStackTrace();
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