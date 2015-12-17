package com.watb.controller;

import com.watb.data.*;
import com.watb.view.SwingHandler;

public class ToDoListApp
{

    public static void main(String[] args)
    {
        //Referenz zu der Klasse in der wir uns befinden, da static Notationen wie this nicht zulässt
        SwingHandler swingHandler = new SwingHandler();
        //initialisiert den TableModelListener
        swingHandler.init();
        //Erzeugt den Hauptframe mit Auswahl der derzeitigen ToDos-Listen
        swingHandler.createFrame();

        //erstellt beispielhaft Datensätze in sqlite und json
        //ToDoListApp app = new com.watb.controller.ToDoListApp();
        //app.runTest();
    }

    public void runTest()
    {
        DataTest test = new DataTest();
        /**
         * needs sqlite jdbc driver
         * https://github.com/xerial/sqlite-jdbc
         * https://bitbucket.org/xerial/sqlite-jdbc/downloads
         */
        test.sqliteTest();
        /**
         * needs simple json lib
         * https://code.google.com/p/json-simple/
         */
        test.jsonTest();
    }

}