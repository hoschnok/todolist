package com.watb.controller;

import com.watb.data.*;
import com.watb.view.SwingHandler;
import com.watb.view.TUIHandler;

/**
 * Note: "ToDos" Schreibweise wird verwendet, weil IntelliJ ansonsten Kommentare als Aufgaben anzeigt.
 * <p/>
 * ToDoListApp ermöglicht das Anlegen von ToDosList und einzelnen ToDos (bzw. Tasks) unterhalt der einzelnen Listen.
 * Jeder task kann dabei als abgeschlossen (done) makiert werden.
 * <p/>
 * Datenhaltung findet mittels SQLite oder JSON statt.
 * Bedienung erfolgt derzeit über eine mit Swing erzeugte GUI.
 */
public class ToDoListApp {

    private final static String REGEX_UI_DECLARATION = "(gui|tui)";
    private final static String REGEX_DB_DECLARATION = "(json|sqlite)";

    public static void main(String[] args) {
        // gui or tui
        String ui = "tui";
        //sqlite or json
        String db = "sqlite";

        //Programm-Parameter auswerten (ob null und ob Eingabe anhand der oben definierten Regular Expression korrekt)
        if (ui.equals("")) {
            if (args == null || args.length == 0) {
                System.out.println(getUsageStatement());
            } else {
                if (args[0] != null) {
                    ui = args[0];
                    if (!isUiInputCorrect(ui)) {
                        System.out.println(getUsageStatement());
                    }
                }
                if(args[1] != null){
                    db = args[1];
                    if(!isDbInputCorrect(db)){
                        System.out.println(getUsageStatement());
                    }
                }
            }
        }

        if (ui.equals("gui")) {
            //Referenz zu der Klasse in der wir uns befinden, da static Notationen wie this nicht zulässt
            SwingHandler swingHandler = new SwingHandler();
            //initialisiert den TableModelListener
            swingHandler.init(db);
            //Erzeugt den Hauptframe mit Auswahl der derzeitigen ToDos-Listen
            swingHandler.createFrame();
        } else if (ui.equals("tui")) {
            TUIHandler tuiHandler = new TUIHandler();
            tuiHandler.init(db);
        }

//        //erstellt beispielhaft Datensätze in sqlite und json
//        ToDoListApp app = new ToDoListApp();
//        app.runTest();
    }

    public void runTest() {
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

    private static String getUsageStatement() {
        return "usage: java -jar todolist.jar [gui|tui] [json|sqlite]";
    }

    private static boolean isUiInputCorrect(String input) {
        if (input.matches(REGEX_UI_DECLARATION)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDbInputCorrect(String input) {
        if (input.matches(REGEX_DB_DECLARATION)) {
            return true;
        } else {
            return false;
        }
    }
}