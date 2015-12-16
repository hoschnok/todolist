import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import com.watb.data.*;
import com.watb.model.*;

public class ToDoListApp implements ActionListener
{
    JComboBox toDoListComboBox;
    JFrame frame;
    JTable table;
    ArrayList<ToDo> toDos;
    JPanel centerPanel;
    DefaultTableModel defaultTableModel;
    JSONToDoHandler jsonToDoHandler;
    TableModelListener tableModelListener;

    public static void main(String[] args)
    {
        //Referenz zu der Klasse in der wir uns befinden, da static Notationen wie this nicht zulässt
        ToDoListApp app = new ToDoListApp();
        app.init();
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

    public void init()
    {
        //Eventlistener für Tabelle muss vorher definiert werden, da wir ihn teilweise kurz entfernen müssen
        this.tableModelListener = new TableModelListener() {
            @Override
            /* wird dem defaultTableModel zugewiesen - bei Änderungen wird die Zeile ermittelt und der Datensatz
                erneuert. Problematik ist, dass beim Wechsel der ToDosList dieses Event triggert und es zu einer
                Exception kommt. Aus diesem Grund muss der Eventlistener bei Auswahl eines Elements der JCombobox
                explizit entfernt werden.
             */
            public void tableChanged(TableModelEvent e) {
                //in dem Fall dass wir nur den boolean ändern ist es egal ob wir firstRow oder lastRow nehmen
                int rowChanged = e.getFirstRow();
                //value ist ein Object, wird danach zum boolean gecastet
                Object value = defaultTableModel.getValueAt(rowChanged, 1);
                boolean done = (boolean) value;
                //wir holen uns das betroffene ToDos und ändern den Wert in dem JSON/SQLite
                ToDo toDo = toDos.get(rowChanged);
                jsonToDoHandler.changeDoneStatus(toDo.getId(), done);
            }
        };
    }

    public void createFrame()
    {
        // Erzeugung eines neuen JFrame (Hauptfenster) mit dem Titel "ToDoListApp"
        this.frame = new JFrame("ToDoListApp");

        //beende automatisch die Applikation, wenn das Fenster geschlossen wird
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Borderlayout besser geeignet als BoxLayout, damit feste Höhe für Reihen festgelegt werden können
        this.frame.getContentPane().setLayout(new BorderLayout());

        //erstellt eine JComboBox, welche die aktuellen ToDos-Listen enthält
        this.createComboBox();

        //erstelle Tabelle mit initial ausgewähltem Wert der ComboBox
        this.createTable();

        /* JFrame setSize() und setVisible() möglichst am Ende, sonst eventuell revalidate() bzw. repaint() nötig.
        Wir setzen die Breite und die Höhe unseres Fensters auf jeweils 400 Pixel */
        this.frame.setSize(800,400);

        // JFrame soll angezeigt werden
        this.frame.setVisible(true);
    }

    public void createComboBox()
    {
        //Erstelle einen JSONtoDoHandler, um die derzeitigen Einträge der JSON files zu bekommen
        this.jsonToDoHandler = new JSONToDoHandler();

        //Hole alle Listen aus den JSON files, um diese in einem DropDown darstellen zu können
        ArrayList<ToDoList> toDoLists = jsonToDoHandler.getLists();

        //Erstellen einer Combobox für Auswahl einer ToDos-Liste
        this.toDoListComboBox = new JComboBox(toDoLists.toArray());

        //falls ein Event bei der Combobox triggert wollen wir die ToDos der ausgewählten Liste anzeigen
        this.toDoListComboBox.addActionListener(this);

        //JComboBox wird Panel hinzugefügt - FlowLaylout, damit die Inhalte linksbündig angezeigt werden
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //füge die JComboBox dem JPanel hinzu
        header.add(this.toDoListComboBox);

        //Bevorzugte Höhe und Breite setzen
        header.setPreferredSize(new Dimension(200, 50));

        //Panel hinzufügen (PAGE_START, PAGE_END, LINE_START, LINE_END, CENTER)
        this.frame.getContentPane().add(header, BorderLayout.PAGE_START);
    }

    public void createTable()
    {
        //Wurde eine ToDoList in der ComboBox ausgewählt, holen wir uns die neue ToDoList
        ToDoList selected = (ToDoList) this.toDoListComboBox.getSelectedItem();
            /* danach setzen wir die ArrayList der ToDos als Object-Eigenschaft, damit wir daraus eine Tabelle erzeugen
             können
              */
        this.toDos = selected.getToDoList();

        this.defaultTableModel = new DefaultTableModel();
        String[] columnNames = {
                "ToDos",
                "Erledigt"
        };
        this.defaultTableModel.setColumnIdentifiers(columnNames);

        //erstelle Tabelle zur Listung der ToDos
        this.table = new JTable(defaultTableModel) {
            /*@Override
            public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();

            Muss überschrieben werden, damit die zweite Spalte auch als Checkbox angezeigt wird
            }*/
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };

        this.defaultTableModel.addTableModelListener(this.tableModelListener);

        //scrollable viewport size verkleinern, sonst größer als JFrame
        this.table.setPreferredScrollableViewportSize(new Dimension(400, 100));

        for (int i = 0; i < this.toDos.size(); i++){
            defaultTableModel.addRow(new Object[] { this.toDos.get(i), this.toDos.get(i).isDone() });
        }
        //erstelle neues Panel für die Tabelle
        this.centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //füge JScrollPane hinzu, damit wir längere Tabellen scrollen können
        this.centerPanel.add(new JScrollPane(this.table));

        //lasse die Höhe halbwegs gering, damit das Fenster kompakt bleibt und der Scroll-Effekt früh sichtbar ist
        this.centerPanel.setPreferredSize(new Dimension(350, 100));

        //füge das Panel dem Borderlayout hinzu (PAGE_START, PAGE_END, LINE_START, LINE_END, CENTER)
        this.frame.getContentPane().add(this.centerPanel, BorderLayout.CENTER);
    }

    public void refreshTable()
    {
        //entferne alle Reihen die momentan in der Tabelle sind
        int currentRowCount = this.defaultTableModel.getRowCount();
        for (int i = currentRowCount - 1; i >= 0; i--) {
            this.defaultTableModel.removeRow(i);
        }
        //Wurde eine ToDoList in der ComboBox ausgewählt, holen wir uns die neue ToDoList
        ToDoList selected = (ToDoList) this.toDoListComboBox.getSelectedItem();
        //erneure die selektierte ToDos
        this.toDos = selected.getToDoList();

        //befülle die nun geleerte Tabelle neu
        for (int i = 0; i < this.toDos.size(); i++){
            this.defaultTableModel.addRow(new Object[] { this.toDos.get(i), this.toDos.get(i).isDone() });
        }
        //changed event triggern, damit ein repaint stattfindet
        this.defaultTableModel.fireTableDataChanged();
        //add listener again
        this.defaultTableModel.addTableModelListener(this.tableModelListener);
    }

    public void actionPerformed (ActionEvent ae){
        //remove temprorarly, since else it will trigger on table refresh, which results in an Exception
        this.defaultTableModel.removeTableModelListener(this.tableModelListener);
        //Prüfe welches Element das Event erzeugt hat
        if(ae.getSource() == this.toDoListComboBox){
            //ernuere die Daten der Tabelle, wenn eine andere ToDos ausgewählt wurden
            this.refreshTable();
        }

    }
}