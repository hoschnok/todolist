package com.watb.data;

public class ToDoHandlerFactory {

    public enum ToDoHanlerType { SQLITE, JSON }

    public ToDoHandlerFactory() {
    }

    public IToDoHandler getToDoHandler(ToDoHanlerType type)
    {
        switch (type)
        {
            case SQLITE: return new SQLiteToDoHandler();
            case JSON: return new JSONToDoHandler();
            default: return new SQLiteToDoHandler();
        }
    }

}
