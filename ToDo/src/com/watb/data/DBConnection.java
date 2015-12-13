package com.watb.data;

import java.sql.*;

public class DBConnection {

    public DBConnection()
    {

    }

    public Connection connect()
    {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data/ToDo.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        return c;
    }


    public static void initDB()
    {
        DBConnection dbc = new DBConnection();

        Connection c = null;
        PreparedStatement stmt = null;
        try {

            c = dbc.connect();

            stmt = c.prepareStatement("CREATE TABLE ToDoList (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL)");
            stmt.executeUpdate();
            stmt = c.prepareStatement("CREATE TABLE ToDo (id INTEGER PRIMARY KEY AUTOINCREMENT, task VARCHAR(255) NOT NULL, done BIT, list_id INT)");
            stmt.executeUpdate();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public void close(Connection c)
    {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Statement stmt)
    {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(ResultSet res)
    {
        try {
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main( String args[] )
    {
        DBConnection.initDB();
    }

}
