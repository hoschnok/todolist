package com.watb.data;

public class ToDoHandlerFactory {

    public ToDoHandlerFactory() {
    }

    public IToDoHandler getToDoHandler(String type)
    {
        switch (type)
        {
            case "sqlite": return new SQLiteToDoHandler();
            case "json": return new JSONToDoHandler();
            default: return new SQLiteToDoHandler();
        }
    }

}
