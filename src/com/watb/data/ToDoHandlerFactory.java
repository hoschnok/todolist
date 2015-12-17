package com.watb.data;

public class ToDoHandlerFactory {

    public ToDoHandlerFactory() {
    }

    public IToDoHandler getToDoHandler(String type)
    {
        switch (type)
        {
            case "SQLite": return new SQLiteToDoHandler();
            case "JSON": return new JSONToDoHandler();
            default: return new SQLiteToDoHandler();
        }
    }

}
