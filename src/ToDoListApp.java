import com.watb.data.*;

/**
 * Note: "ToDos" Schreibweise wird verwendet, weil IntelliJ ansonsten Kommentare als Aufgaben anzeigt.
 *
 * ToDoListApp ermöglicht das Anlegen von ToDosList und einzelnen ToDos (bzw. Tasks) unterhalt der einzelnen Listen.
 * Jeder task kann dabei als abgeschlossen (done) makiert werden.
 *
 * Datenhaltung findet mittels SQLite oder JSON statt.
 * Bedienung erfolgt derzeit über eine mit Swing erzeugte GUI.
 */
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