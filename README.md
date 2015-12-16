ToDoListApp
---

**Einrichtung**

- IntelliJ IDEA Module (.iml) file mit im Repository
- erstellt IntelliJ diesen file automatisch kann der content kopiert werden, anstatt die run config über die IDE zu konfigurieren
- andernfalls muss nach dem Klonen des Repos die .jar files unter /libs manuell zu den libraries hinzugefügt werden

**Hinweise**

- main() unter src/ToDoListApp.java
- main() hat die Funktion runTest() zum Erstellen von Beispiel Datensätzen
- SQLite Tabellen und JSON files zur Datenhaltung liegen unter data und müssen derzeit ggf. vor runTest() entfernt werden