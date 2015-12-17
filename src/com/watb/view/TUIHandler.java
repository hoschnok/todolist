package com.watb.view;

import com.watb.data.IToDoHandler;
import com.watb.data.JSONToDoHandler;
import com.watb.data.ToDoHandlerFactory;
import com.watb.model.ToDo;
import com.watb.model.ToDoList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mw on 17.12.2015.
 */
public class TUIHandler {

    Scanner scanner = new Scanner(System.in);
    private static int maxSpaces = 45;

    IToDoHandler toDoHandler;

    public void init(String db) {

        ToDoHandlerFactory toDoHandlerFactory = new ToDoHandlerFactory();
        this.toDoHandler = toDoHandlerFactory.getToDoHandler(db);

        printIntro();
        listTodoLists();
    }

    private void printIntro() {
        System.out.println(" *** TODO LIST APP - Text User Interface ***");
    }

    private void listTodoListMenu() {
        System.out.println("neue Liste [n]\n");
        System.out.println("Wählen Sie eine der ToDo Listen aus oder erstellen Sie eine neue.");
        System.out.print("-> ");
        String input = scanner.next().trim();
        if (!input.equals("")) {
            try {
                long listId = Long.parseLong(input);
                listTasks(listId);
            } catch (NumberFormatException nfe) {
                if (input.equals("n")) {
                    System.out.println("Geben Sie den Namen der neuen Liste ein.");
                    System.out.print("-> ");
                    try {
                        toDoHandler.saveToDoList(scanner.next().trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listTodoLists();
                } else {
                    System.out.println("Falsche Eingabe.");
                    listTodoLists();
                }
            }
        }
    }

    private void listTodoLists() {
        System.out.println("\n# Auflistung  der Todo Listen:");
        ArrayList<ToDoList> toDoLists = toDoHandler.getLists();
        for (ToDoList toDoList : toDoLists) {
            String spaces = "";
            String textToPrint = getToDoListEntry(toDoList.getName(), spaces, toDoList.getId());
            spaces = getSpacesToFill(textToPrint);
            System.out.println(getToDoListEntry(toDoList.getName(), spaces, toDoList.getId()));
        }
        listTodoListMenu();
    }

    private String getToDoListEntry(String name, String spaces, Long id) {
        return name + spaces + "(" + id + ")";
    }

    private void listTaskListMenu(ToDoList list) {
        ArrayList<ToDo> tasks = list.getToDoList();
        System.out.println("neue Aufgabe [n]");
        System.out.println("ToDo Listen anzeigen [a]");
        System.out.println("\nWählen Sie eine der Aufgaben aus um den erledigt-Status zu wechseln oder erstellen Sie eine neue.");
        System.out.print("-> ");
        String input = scanner.next().trim();

        if (!input.equals("")) {
            try {
                long taskId = Long.parseLong(input);
                for (ToDo task : tasks) {
                    if (task.getId() == taskId) {
                        boolean isDone = task.isDone();
                        toDoHandler.changeDoneStatus(taskId, !isDone);
                        listTasks(list.getId());
                    }
                }
            } catch (NumberFormatException nfe) {
                if (input.equals("n")) {
                    System.out.println("Geben Sie den Namen der neuen Aufgabe ein.");
                    System.out.print("-> ");
                    String taskName = scanner.next().trim();
                    toDoHandler.saveToDo(list.getId(), taskName);
                    listTasks(list.getId());
                } else if (input.equals("a")) {
                    listTodoLists();
                } else {
                    System.out.println("Falsche Eingabe.");
                    listTodoLists();
                }
            }
        }
    }

    private void listTasks(long todoId) {
        ArrayList<ToDoList> toDoLists = toDoHandler.getLists();
        ToDoList wantedList = new ToDoList();
        for (ToDoList toDoList : toDoLists) {
            if (toDoList.getId() == todoId) {
                wantedList = toDoList;
                break;
            }
        }

        if (wantedList.getName() != null) {
            ArrayList<ToDo> tasks = wantedList.getToDoList();
            System.out.println("Liste: " + wantedList.getName());
            if(tasks.isEmpty()){
                System.out.println("! Diese Liste enthält noch keine Aufgaben.");
            }
            for (ToDo task : tasks) {
                String spaces = "";
                String textToPrint = getTaskListEntry(task.getTask(), spaces, task.getId(), task.isDone());
                spaces = getSpacesToFill(textToPrint);
                System.out.println(getTaskListEntry(task.getTask(), spaces, task.getId(), task.isDone()));
            }
            listTaskListMenu(wantedList);
        } else {
            System.out.println("Diese Liste wurde nicht gefunden.");
        }
    }

    private String getTaskListEntry(String name, String spaces, long id, boolean isDone) {
        String erledigt;
        if (isDone) {
            erledigt = "OK";
        } else {
            erledigt = "XX";
        }
        return name + spaces + "(" + id + ") " + erledigt;
    }

    private String getSpacesToFill(String wholeText) {
        int amountSpacesNeeded = maxSpaces - wholeText.length();
        StringBuilder spacesBuilder = new StringBuilder("");
        for (int i = 0; i < amountSpacesNeeded; i++) {
            spacesBuilder.append(" ");
        }
        return spacesBuilder.toString();
    }
}