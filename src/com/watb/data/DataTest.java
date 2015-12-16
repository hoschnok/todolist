package com.watb.data;

import com.watb.model.ToDo;
import com.watb.model.ToDoList;

import java.util.ArrayList;

public class DataTest {

    public DataTest()
    {
    }

    public void sqliteTest()
    {
        DBConnection.initDB();

        SQLiteToDoHandler handler = new SQLiteToDoHandler();
        handler.saveToDoList("Hausaufgaben");
        handler.saveToDoList("Haushalt");

        ArrayList<ToDoList> lists = handler.getLists();
        handler.saveToDo(lists.get(0).getId(), "AS-Vortrag");
        handler.saveToDo(lists.get(0).getId(), "AS-Projekt");

        handler.saveToDo(lists.get(1).getId(), "Bad putzen");
        handler.saveToDo(lists.get(1).getId(), "Staubsaugen");
        handler.saveToDo(lists.get(1).getId(), "Abwaschen");

        ToDoList hausaufgaben = handler.setToDoList(lists.get(0));
        System.out.println(hausaufgaben.toString());
        for (ToDo td : hausaufgaben.getToDoList()) {
            System.out.println(td.toString());
        }

        ToDoList haushalt = handler.setToDoList(lists.get(1));
        System.out.println(haushalt.toString());
        for (ToDo td : haushalt.getToDoList()) {
            System.out.println(td.toString());
        }

        handler.changeDoneStatus(haushalt.getToDoList().get(2).getId(), true);
        handler.changeDoneStatus(haushalt.getToDoList().get(0).getId(), false);
        haushalt = handler.setToDoList(lists.get(1));
        System.out.println(haushalt.toString());
        for (ToDo td : haushalt.getToDoList()) {
            System.out.println(td.toString());
        }
    }

    public void jsonTest()
    {
        JSONToDoHandler handler = new JSONToDoHandler();
        handler.saveToDoList("Hausaufgaben");
        handler.saveToDoList("Haushalt");

        for (ToDoList list : handler.getLists())
            System.out.println(list.toString());

        System.out.println("-----------------------------------------------------------");

        handler.saveToDo(1, "Vortrag");
        handler.saveToDo(1, "Projekt");

        for (ToDoList list : handler.getLists()) {
            System.out.println(list.toString());
            for (ToDo todo : list.getToDoList())
            {
                System.out.println(todo.toString());
            }
        }

        System.out.println("-----------------------------------------------------------");

        handler.saveToDo(2, "Staubsaugen");
        handler.saveToDo(2, "Abwaschen");

        for (ToDoList list : handler.getLists()) {
            System.out.println(list.toString());
            for (ToDo todo : list.getToDoList())
            {
                System.out.println(todo.toString());
            }
        }

        System.out.println("-----------------------------------------------------------");

        handler.changeDoneStatus(2, true);
        handler.changeDoneStatus(4, false);

        for (ToDoList list : handler.getLists()) {
            System.out.println(list.toString());
            for (ToDo todo : list.getToDoList())
            {
                System.out.println(todo.toString());
            }
        }

    }

}
