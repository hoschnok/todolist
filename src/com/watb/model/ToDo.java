package com.watb.model;

public class ToDo {

    private long id;
    private String task;
    private boolean done;
    private long list_id;

    public ToDo()
    {
    }

    public ToDo(long id, String task, boolean done, long list_id) {
        this.id = id;
        this.task = task;
        this.done = done;
        this.list_id = list_id;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getList_id() {
        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }

    @Override
    public String toString() {
        return getTask();
    }
}
