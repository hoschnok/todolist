package com.watb.data;

import com.watb.model.ToDo;
import com.watb.model.ToDoList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SQLiteToDoHandler implements IToDoHandler
{

    public SQLiteToDoHandler()
    {

    }

    /**Speichert eine neue Liste in der DB
     *
     * @param list_name
     *              Name der neuen Liste
     */
    @Override
    public void saveToDoList(String list_name) {
        DBConnection dbc = new DBConnection();

        Connection c = null;
        PreparedStatement stmt = null;

        try {
            c = dbc.connect();
            stmt = c.prepareStatement("INSERT INTO ToDoList (id, name) VALUES (null, ?)");
            stmt.setString(1, list_name);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbc.close(stmt);
            dbc.close(c);
        }
    }

    /**Speichert den neuen Task in der DB
     *
     * @param toDoList_id
     *          ID der zugeordneten Liste des Tasks
     * @param task_name
     *          Name des neuen Tasks
     */
    @Override
    public void saveToDo(long toDoList_id, String task_name) {
        DBConnection dbc = new DBConnection();

        Connection c = null;
        PreparedStatement stmt = null;

        try {
            c = dbc.connect();
            stmt = c.prepareStatement("INSERT INTO ToDo (id, task, done, list_id) VALUES (null, ?,0,?)");
            stmt.setString(1, task_name);
            stmt.setLong(2, toDoList_id);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbc.close(stmt);
            dbc.close(c);
        }
    }

    /** Halt Tasks bei auswahl einer ToDoListe
     *
     * @return ToDoListe mit Tasks (ToDo's)
     */
    @Override
    public ToDoList setToDoList(ToDoList toDoList)
    {
        ArrayList<ToDo> listOfToDo = new ArrayList<>();
        DBConnection dbc = new DBConnection();

        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet res = null;

        try {
            c = dbc.connect();
            stmt = c.prepareStatement("SELECT * FROM ToDo WHERE list_id = ?");
            stmt.setLong(1, toDoList.getId());
            res = stmt.executeQuery();

            while (res.next())
            {
                listOfToDo.add(new ToDo(res.getLong("id"), res.getString("task"), res.getBoolean("done"), res.getLong("list_id")));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbc.close(res);
            dbc.close(stmt);
            dbc.close(c);
        }

        toDoList.setToDoList(listOfToDo);
        return toDoList;
    }

    /** Setzt das abhaken eines Tasks in der DB
     *
     * @param todo_id
     *          ID des abgehakten Tasks
     */
    @Override
    public void changeDoneStatus(long todo_id, boolean status) {
        DBConnection dbc = new DBConnection();

        Connection c = null;
        PreparedStatement stmt = null;

        try {
            c = dbc.connect();
            stmt = c.prepareStatement("Update ToDo SET done = " + status + " WHERE id = ?");
            stmt.setLong(1, todo_id);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbc.close(stmt);
            dbc.close(c);
        }
    }

    /** Für die Dropdown Auswahl aller ToDoListen
     *
     * @return Liste an ToDoListen ohne Tasks (ToDo's)
     */
    @Override
    public ArrayList<ToDoList> getLists() {
        ArrayList<ToDoList> listOfToDoLists = new ArrayList<>();
        DBConnection dbc = new DBConnection();

        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet res = null;

        try {
            c = dbc.connect();
            stmt = c.prepareStatement("SELECT * FROM ToDoList");
            res = stmt.executeQuery();

            while (res.next())
            {
                listOfToDoLists.add(new ToDoList(res.getLong("id"), res.getString("name")));
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbc.close(res);
            dbc.close(stmt);
            dbc.close(c);
        }
        return listOfToDoLists;
    }
}
