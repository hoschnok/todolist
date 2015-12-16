import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.watb.data.*;
import com.watb.model.*;

public class ToDoListApp implements ActionListener
{
    JComboBox toDoListComboBox;
    JFrame frame;
    JPanel panel;

    public static void main(String[] args)
    {
        //Referenz zu der Klasse in der wir uns befinden, da static Notationen wie this nicht zulässt
        ToDoListApp app = new ToDoListApp();
        //Erzeugt den Hauptframe mit Auswahl der derzeitigen ToDos-Listen
        app.createFrame();

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

    public void createFrame()
    {
        // Erzeugung eines neuen JFrame (Hauptfenster) mit dem Titel "ToDoListApp"
        this.frame = new JFrame("ToDoListApp");
        //Panel für Bezeichnung der Combobox
        this.panel = new JPanel();

        //Label für unsere Combobox
        JLabel comboBoxLabel = new JLabel("ToDoLists");
        this.panel.add(comboBoxLabel);

        //erstellt eine JComboBox hinzu, welche die aktuellen ToDos-Listen enthält
        this.createComboBox();

        //JComboBox wird Panel hinzugefügt
        this.panel.add(this.toDoListComboBox);
        //füge Panel dem frame hinzu
        frame.add(this.panel);

        /* JFrame setSize() und setVisible() möglichst am Ende, sonst eventuell revalidate() bzw. repaint() nötig.
        Wir setzen die Breite und die Höhe unseres Fensters auf jeweils 400 Pixel */
        frame.setSize(400,400);
        // JFrame soll angezeigt werden
        frame.setVisible(true);
    }

    public void createComboBox()
    {
        //Erstelle einen JSONtoDoHandler, um die derzeitigen Einträge der JSON files zu bekommen
        JSONToDoHandler jsonToDoHandler = new JSONToDoHandler();
        //Hole alle Listen aus den JSON files, um diese in einem DropDown darstellen zu können
        ArrayList<ToDoList> toDoLists = jsonToDoHandler.getLists();
        //Erstellen einer Combobox für Auswahl einer ToDos-Liste
        this.toDoListComboBox = new JComboBox(toDoLists.toArray());
        //falls ein Event bei der Combobox triggert wollen wir die ToDos der ausgewählten Liste anzeigen
        this.toDoListComboBox.addActionListener(this);
    }


    public void actionPerformed (ActionEvent ae){
        if(ae.getSource() == this.toDoListComboBox){
            ToDoList selected = (ToDoList) this.toDoListComboBox.getSelectedItem();
            ArrayList<ToDo> toDos = selected.getToDoList();
            for (int i = 0; i < toDos.size(); i++){
                System.out.println(toDos.get(i));
            }
        }
    }
}