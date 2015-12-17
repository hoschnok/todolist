package com.watb.data;

import com.watb.model.ToDoList;

import java.io.IOException;
import java.util.ArrayList;

public interface IToDoHandler {

    /**Speichert eine neue Liste in der DB
     *
     * @param list_name
     *              Name der neuen Liste
     */
    void saveToDoList(String list_name);

    /**Speichert den neuen Task in der DB
     *
     * @param toDoList_id
     *          ID der zugeordneten Liste des Tasks
     * @param task_name
     *          Name des neuen Tasks
     */
    void saveToDo(long toDoList_id, String task_name);

    /** Für die Dropdown Auswahl aller ToDoListen
     *
     * @return Liste an ToDoListen ohne Tasks (ToDo's)
     */
    ArrayList<ToDoList> getLists();

    /** Halt Tasks bei auswahl einer ToDoListe
     *
     * @return ToDoListe mit Tasks (ToDo's)
     */
    ToDoList setToDoList(ToDoList toDoList);

    /** Setzt das abhaken eines Tasks in der DB
     *
     * @param todo_id
     *          ID des abgehakten Tasks
     */
    void changeDoneStatus(long todo_id, boolean status);

}
