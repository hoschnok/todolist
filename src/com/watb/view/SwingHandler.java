package com.watb.view;

import com.watb.data.IToDoHandler;
import com.watb.data.JSONToDoHandler;
import com.watb.data.ToDoHandlerFactory;
import com.watb.model.ToDo;
import com.watb.model.ToDoList;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Jahns on 17.12.2015.
 *
 * Note: "ToDos" Schreibweise wird verwendet, weil IntelliJ ansonsten Kommentare als Aufgaben anzeigt.
 *
 * Erzeugt einen JFrame mittels Swing. Dieser ist der einzige Frame den wir derzeit benötigen und beinhaltet eine
 * JCombobox zur Auswahl der bestehenden ToDosListen und eine JTable mit der jeweiligen ausgewählten ToDosList.
 */
public class SwingHandler implements ActionListener {
    JComboBox toDoListComboBox;
    JFrame frame;
    JTable table;
    ArrayList<ToDoList> toDoLists;
    ArrayList<ToDo> toDos;
    JPanel centerPanel;
    DefaultTableModel defaultTableModel;
    IToDoHandler toDoHandler;
    TableModelListener tableModelListener;
    JButton addNewList;
    JButton addNewTask;
    JTextField newList;
    JTextField newTask;
    JPanel header;
    JPanel footer;

    /**
     * Initialisiert den tableModelListener. Dieser muss während der Laufzeit der JTable hinzugefügt und teilweise
     * wieder entfernt werden, deshalb initialisieren wir diesen schon vor Erstellung des JFrames.
     */
    public void init(String db)
    {
        ToDoHandlerFactory toDoHandlerFactory = new ToDoHandlerFactory();
        this.toDoHandler = toDoHandlerFactory.getToDoHandler(db);
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
                //change on database level
                toDoHandler.changeDoneStatus(toDo.getId(), done);
                //if we still want to work with the entities and not refresh from db we have to change them too
                toDo.setDone(done);
            }
        };
    }

    /**
     * Erstellt unseren Hauptframe mit der JComboBox und der JTable, zur Auswahl von Listen und Darstellung der Tasks.
     */
    public void createFrame()
    {
        // Erzeugung eines neuen JFrame (Hauptfenster) mit dem Titel "com.watb.controller.ToDoListApp"
        this.frame = new JFrame("com.watb.controller.ToDoListApp");

        //beende automatisch die Applikation, wenn das Fenster geschlossen wird
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Borderlayout besser geeignet als BoxLayout, damit feste Höhe für Reihen festgelegt werden können
        this.frame.getContentPane().setLayout(new BorderLayout());

        //erstellt eine JComboBox, welche die aktuellen ToDos-Listen enthält
        this.createComboBox();

        this.createNewListInput();
        this.createNewTaskInput();

        //erstelle Tabelle mit initial ausgewähltem Wert der ComboBox
        this.createTable();

        /* JFrame setSize() und setVisible() möglichst am Ende, sonst eventuell revalidate() bzw. repaint() nötig.
        Wir setzen die Breite und die Höhe unseres Fensters auf jeweils 400 Pixel */
        this.frame.setSize(800,275);

        // JFrame soll angezeigt werden
        this.frame.setVisible(true);
    }

    /**
     * Fügt die benötigten Elemente zum Hinzufügen neuer Listen dem Headerbereich hinzu.
     */
    public void createNewListInput()
    {
        //Textfeld zum Hinzufügen neuer ToDos-Listen
        this.newList = new JTextField();
        //Größe an Elemente JComboBox und JButton in der Reihe anpassen
        this.newList.setPreferredSize(new Dimension(200, 30));
        //Button zum hinzufügen der ToDos-Liste
        this.addNewList = new JButton("Liste hinzufügen");
        //actionListener des Buttons wieder mit Verweis auf dieses Objekt - permformed action prüft dann das Element
        this.addNewList.addActionListener(this);
        //Hinzufügen der Elemente zu dem Header-Bereich
        this.header.add(newList);
        this.header.add(addNewList);
    }

    /**
     * Fügt die benötigten Elemente zum Hinzufügen eines neuen Tasks dem Footer hinzu.
     */
    public void createNewTaskInput()
    {
        //Textfeld zum Hinzufügen neuer ToDos-Listen
        this.newTask = new JTextField();
        //Größe an Elemente JComboBox und JButton in der Reihe anpassen
        this.newTask.setPreferredSize(new Dimension(200, 30));
        //Button zum hinzufügen der ToDos-Liste
        this.addNewTask = new JButton("Aufgabe hinzufügen");
        //actionListener des Buttons wieder mit Verweis auf dieses Objekt - permformed action prüft dann das Element
        this.addNewTask.addActionListener(this);

        //JTextField und JButton werden Panel hinzugefügt - FlowLaylout, damit die Inhalte linksbündig angezeigt werden
        this.footer = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //Hinzufügen der Elemente zu dem Header-Bereich
        this.footer.add(this.newTask);
        this.footer.add(this.addNewTask);
        this.frame.getContentPane().add(footer, BorderLayout.PAGE_END);
    }

    /**
     * Erzeugt initial unsere JComboBox in der alle derzeitig gespeicherten ToDosListen angezeigt werden.
     * Weiterhin bekommt die ComboxBox einen ActionListener, welcher die Inhalte der Tabelle bei einer Auswahl erneuert.
     */
    public void createComboBox()
    {
        //Hole alle Listen aus den JSON files, um diese in einem DropDown darstellen zu können
        this.toDoLists = this.toDoHandler.getLists();

        //Erstellen einer Combobox für Auswahl einer ToDos-Liste
        this.toDoListComboBox = new JComboBox(toDoLists.toArray());

        //falls ein Event bei der Combobox triggert wollen wir die ToDos der ausgewählten Liste anzeigen
        this.toDoListComboBox.addActionListener(this);

        //JComboBox wird Panel hinzugefügt - FlowLaylout, damit die Inhalte linksbündig angezeigt werden
        this.header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //füge die JComboBox dem JPanel hinzu
        this.header.add(this.toDoListComboBox);

        //Bevorzugte Höhe und Breite setzen
        this.header.setPreferredSize(new Dimension(200, 50));

        //Panel hinzufügen (PAGE_START, PAGE_END, LINE_START, LINE_END, CENTER)
        this.frame.getContentPane().add(header, BorderLayout.PAGE_START);
    }

    /**
     * Erneuert die ComboBox, Actionlistener muss anfangs entfernt und am Ende wieder zugefügt werden.
     * @param select int ist dieser Parameter gesetzt wird der Index der ComboBox nach dem Refresh gesetzt.
     */
    public void refreshComboBox(int select)
    {
        //Actionlistener temporär entfernen, da sonst jedes mal triggert
        this.toDoListComboBox.removeActionListener(this);
        //Hole alle Listen aus den JSON files, um diese in einem DropDown darstellen zu können
        this.toDoLists = this.toDoHandler.getLists();

        this.toDoListComboBox.removeAllItems();

        for(ToDoList str : this.toDoLists) {
            this.toDoListComboBox.addItem(str);
        }
        //re-enable action listener
        this.toDoListComboBox.addActionListener(this);
        if (select >= 0){
            this.toDoListComboBox.setSelectedIndex(select);
        } else {
            //neue Anzahl von Listen holen
            int count = this.toDoListComboBox.getItemCount();
            //neu erstellte Liste auswählen
            this.toDoListComboBox.setSelectedIndex(count - 1);
        }
    }

    /**
     * Initial muss auch die Tabelle einmal mit der Vorauswahl der JComboBox gefüllt werden. Danach wird lediglich
     * die Funktion refreshTable() aufegrufen. GetColumnClass() muss überschrieben werden, damit die boolean-Werte
     * als CheckBo angezeigt werden. Die Tabelle hat zudem einen tableModelListener, mit dem wir erkennen, wenn ein
     * Task als erledigt makiert wurde und diese Änderung im Model, bzw. in der Datenbank übernommen werden muss.
     */
    public void createTable()
    {
        //Wurde eine ToDoList in der ComboBox ausgewählt, holen wir uns die neue ToDoList
        ToDoList selected = (ToDoList) this.toDoListComboBox.getSelectedItem();
        /* danach setzen wir die ArrayList der ToDos als Object-Eigenschaft, damit wir daraus eine Tabelle erzeugen
         können*/
        /*
         * todos für sqlite müssen mittels setToDoList erneuert werden - für json nicht nötig wird aber wegen
         * Abstraktion trotzdem getan
         */
        this.toDoHandler.setToDoList(selected);
        //erneure die selektierte ToDos
        this.toDos = selected.getToDoList();
        //do not go on if no Todos
        if(this.toDos == null){
            return;
        }
        this.defaultTableModel = new DefaultTableModel();
        //Spaltennamen bestimmen
        String[] columnNames = {
                "ToDos",
                "Erledigt"
        };
        //Zuweisen der Spaltennamen zu unserem defaultTableModel
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
        //scrollable viewport size verkleinern, sonst größer als JFrame
        this.table.setPreferredScrollableViewportSize(new Dimension(400, 100));

        //Durchlaufen der einzelnen Task einer Liste und hinzufügen zu unserer Tabelle
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

        /*
         *   Hinzufügen unseres tableModelListeners ganz am Ende, da dieser sonst bereits beim erstmaligen Hinzufügen
         *   unserer Datensätze jedes mal Triggern würde.
         *
         *   Note: muss vor jedem Refresh der Tabelle durch eine Auswahl der JComboBox entfernt werden, damit das
         *   Event nur eintritt, wenn ein Task als done makiert wird.
         */
        this.defaultTableModel.addTableModelListener(this.tableModelListener);
    }


    /**
     * Erneuert die Inhalte der Tabelle, wenn der User eine andere Liste aus der JComboBox wählt. Dabei wird vorher
     * der TableModelListener entfernt, damit das neue Belesen der Tabelle keine Events auslöst und danach wieder
     * zugewiesen.
     */
    public void refreshTable()
    {
        //do not go on if no Todos
        if(this.toDos != null){
            //remove temprorarly, since else it will trigger on table refresh, which results in an Exception
            this.defaultTableModel.removeTableModelListener(this.tableModelListener);
            //entferne alle Reihen die momentan in der Tabelle sind
            int currentRowCount = this.defaultTableModel.getRowCount();
            //bevor die Tabelle neu belesen werden kann, müssen alle bisherigen Datensätze aus ihr entfernt werden
            for (int i = currentRowCount - 1; i >= 0; i--) {
                this.defaultTableModel.removeRow(i);
            }
        }

        //Wurde eine ToDoList in der ComboBox ausgewählt, holen wir uns die neue ToDoList
        ToDoList selected = (ToDoList) this.toDoListComboBox.getSelectedItem();
        /*
         * todos für sqlite müssen mittels setToDoList erneuert werden - für json nicht nötig wird aber wegen
         * Abstraktion trotzdem getan
         */
        this.toDoHandler.setToDoList(selected);
        //erneure die selektierte ToDos
        this.toDos = selected.getToDoList();

        //do not go on if no Todos
        if(this.toDos == null){
            return;
        }

        //befülle die nun geleerte Tabelle neu
        for (int i = 0; i < this.toDos.size(); i++){
            this.defaultTableModel.addRow(new Object[] { this.toDos.get(i), this.toDos.get(i).isDone() });
        }
        //changed event triggern, damit ein repaint stattfindet
        this.defaultTableModel.fireTableDataChanged();
        //add listener again
        this.defaultTableModel.addTableModelListener(this.tableModelListener);
    }

    /**
     * Funktion des ActionListeners. Wird aufegerufen, egal welches Actionevent eintritt. Folglich muss über getSource()
     * geprüft werden, welches Element betroffen ist.
     *
     * @param ae
     */
    public void actionPerformed (ActionEvent ae){
        //Prüfe welches Element das Event erzeugt hat
        if(ae.getSource() == this.toDoListComboBox){
            //erneuere die Daten der Tabelle, wenn eine andere ToDos ausgewählt wurden
            this.refreshTable();
        }else if(ae.getSource() == this.addNewList){
            //hole den Aktuellen Wert für die neue ToDosListe
            String input = this.newList.getText();
            //speicher den neuen Wert in der jeweiligen DatenBank
            this.toDoHandler.saveToDoList(input);
            //setze das textfeld zurück
            this.newList.setText("");
            //erneuere die ComboBox mit der neuen Liste
            this.refreshComboBox(-1);
        } else if(ae.getSource() == this.addNewTask){
            String input = this.newTask.getText();
            //aktuell ausgewählte Liste
            ToDoList selected = (ToDoList) this.toDoListComboBox.getSelectedItem();
            //ID der ausgewählten Liste
            long id = selected.getId();
            //speichern des neuen ToDos zu der Liste
            this.toDoHandler.saveToDo(id, input);
            //setze das textfeld zurück
            this.newTask.setText("");
            //hole aktuell ausgewählten index, damit wir diesen nach neuem Befüllen der Combobox neu setzen können
            int index = this.toDoListComboBox.getSelectedIndex();
            //ComboBox erneuern
            this.refreshComboBox(index);
            //Tabelle erneuern
            this.refreshTable();
        }
    }
}
