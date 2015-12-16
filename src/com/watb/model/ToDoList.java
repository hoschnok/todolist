package com.watb.model;

import java.util.ArrayList;

public class ToDoList {

    private long id;
    private String name;
    private ArrayList<ToDo> toDoList;

    public ToDoList()  {

    }

    public ToDoList(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ToDoList(long id, String name, ArrayList<ToDo> toDoList) {
        this.id = id;
        this.name = name;
        this.toDoList = toDoList;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ToDo> getToDoList() {
        return toDoList;
    }

    public void setToDoList(ArrayList<ToDo> toDoList) {
        this.toDoList = toDoList;
    }

    @Override
    public String toString() {
        return getName();
    }
}
