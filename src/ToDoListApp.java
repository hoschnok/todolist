import javax.swing.*;
import java.util.ArrayList;

import com.watb.data.*;
import com.watb.model.*;

public class ToDoListApp extends JPanel
{
    public static void main(String[] args)
    {
        /* Erzeugung eines neuen JFrame (Hauptfenster) mit dem
           Titel "ToDoListApp" */
        JFrame frame = new JFrame("ToDoListApp");

        //Panel für Bezeichnung der Combobox
        JPanel panel = new JPanel();

        JLabel comboBoxLabel = new JLabel("ToDoLists");
        panel.add(comboBoxLabel);

        JSONToDoHandler jsonToDoHandler = new JSONToDoHandler();
        ArrayList<ToDoList> toDoLists = jsonToDoHandler.getLists();

        // Array für unsere JComboBox
        String comboBoxList[] = {
                "Todo 1",
                "Todo 2",
                "Todo 3",
                "Todo 4",
                };

        //JComboBox mit Bundesländer-Einträgen wird erstellt
        JComboBox toDoListComboBox = new JComboBox(comboBoxList);
        //JComboBox wird Panel hinzugefügt
        panel.add(toDoListComboBox);
        //füge Panel dem frame hinzu
        frame.add(panel);


        /* JFrame setSize() und setVisible() möglichst am Ende, sonst eventuell revalidate() bzw. repaint() nötig.
        Wir setzen die Breite und die Höhe unseres Fensters auf jeweils 400 Pixel */
        frame.setSize(400,400);
        // JFrame soll angezeigt werden
        frame.setVisible(true);

        ToDoListApp app = new ToDoListApp();
        //erstellt beispielhaft Datensätze in sqlite und json
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