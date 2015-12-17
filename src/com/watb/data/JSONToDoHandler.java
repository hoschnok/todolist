package com.watb.data;

import java.io.*;
import java.util.ArrayList;
import static java.lang.Math.toIntExact;

import com.watb.model.ToDo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.watb.model.ToDoList;

public class JSONToDoHandler implements IToDoHandler
{
	
	private int last_list_id;
	private int last_task_id;
	private static final String LISTFILE = "json_list_data.json";
	private String path;
	
	public JSONToDoHandler()
	{
		path = System.getProperty("user.dir") + File.separator + "data" + File.separator;
		setLast_list_id();
		setLast_task_id();
	}

	/**Speichert eine neue Liste in der DB
	 *
	 * @param list_name
	 *              Name der neuen Liste
	 */
	@Override
	public void saveToDoList(String list_name)
	{
		last_list_id++;
		JSONObject newList = new JSONObject();
		newList.put("id", last_list_id);
		newList.put("name", list_name);
		newList.put("task_list", null);

		JSONArray base = null;
		try {
			base = readFileBase();
			base.add(newList);
			}
		catch (FileNotFoundException e)
		{
			base = initToDoList(newList);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		writeFileBase(base);
	}

	/**Speichert den neuen Task in der DB
	 *
	 * @param toDoList_id
	 *          ID der zugeordneten Liste des Tasks
	 * @param task_name
	 *          Name des neuen Tasks
	 */
	@Override
	public void saveToDo(long toDoList_id, String task_name) {
		last_task_id++;
		JSONObject task = new JSONObject();
		task.put("id", last_task_id);
		task.put("name", task_name);
		task.put("done", false);

		JSONArray base = null;
		try {
			base = readFileBase();
			for (int i = 0; i < base.size(); i++)
			{
				JSONObject json = (JSONObject) base.get(i);
				int id = toIntExact((long) json.get("id"));
				if (id == toIntExact(toDoList_id))
				{
					JSONArray todos = (JSONArray) json.get("task_list");
					if (todos == null) {
						todos = new JSONArray();
					}
					todos.add(task);
					json.put("task_list", todos);
					base.set(i, json);
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		writeFileBase(base);
	}

	/** Für die Dropdown Auswahl aller ToDoListen
	 *
	 * @return Liste an ToDoListen ohne Tasks (ToDo's)
	 */
	@Override
	public ArrayList<ToDoList> getLists() {
		ArrayList<ToDoList> lists = new ArrayList<>();

		JSONArray base = null;
		try {
			base = readFileBase();

			for (int i=0; i<base.size(); i++) {
				JSONObject obj = (JSONObject) base.get(i);
				ToDoList tdl = jsonToList(obj);
				lists.add(tdl);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return lists;
	}

	/** Halt Tasks bei auswahl einer ToDoListe
	 *
	 * @return ToDoListe mit Tasks (ToDo's)
	 */
	@Override
	public ToDoList setToDoList(ToDoList toDoList) {

		return toDoList;
	}

	/** Setzt das abhaken eines Tasks in der DB
	 *
	 * @param todo_id
	 *          ID des abgehakten Tasks
	 */
	@Override
	public void changeDoneStatus(long todo_id, boolean status) {

		JSONArray base = null;
		try {
			base = readFileBase();

			for (int i = 0; i < base.size(); i++) {
				JSONObject json = (JSONObject) base.get(i);
				JSONArray ids = (JSONArray) json.get("task_list");

				if (ids != null) {
					for (int j = 0; j < ids.size(); j++) {
						JSONObject task = (JSONObject) ids.get(j);
						int id = toIntExact((long) task.get("id"));
						if (id == todo_id)
						{
							task.replace("done", status);
							ids.set(j, task);
							json.replace("task_list", ids);
							base.set(i, json);
						}
					}
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		writeFileBase(base);
	}

	private JSONArray initToDoList(JSONObject newList)
	{
		JSONArray array = new JSONArray();
		array.add(newList);

		return array;
	}

	private ToDoList jsonToList(JSONObject toDoList)
	{
		JSONArray task_list = (JSONArray)toDoList.get("task_list");
		ArrayList<ToDo> tasks = new ArrayList<>();
		if (task_list != null) {
			for (int j = 0; j < task_list.size(); j++) {
				JSONObject task = (JSONObject) task_list.get(j);
				tasks.add(new ToDo((long) task.get("id"), task.get("name").toString(), (Boolean) task.get("done"), (long) toDoList.get("id")));
			}
		}
		ToDoList list = new ToDoList(((long) toDoList.get("id")), toDoList.get("name").toString(), tasks);

		return list;
	}

	private JSONObject listToJson(ToDoList toDoList)
	{
		JSONObject jsonList = new JSONObject();
		jsonList.put("id", toDoList.getId());
		jsonList.put("name", toDoList.getName());

		JSONArray toDos = new JSONArray();
		for (ToDo todo : toDoList.getToDoList())
		{
			JSONObject jsonToDo = new JSONObject();
			jsonToDo.put("id",todo.getId());
			jsonToDo.put("task",todo.getTask());
			jsonToDo.put("done",todo.isDone());

			toDos.add(jsonToDo);
		}
		jsonList.put("task_list", toDos);

		return jsonList;
	}

	private JSONArray readFileBase() throws FileNotFoundException
	{
		JSONParser parser = new JSONParser();
		JSONArray base = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path + LISTFILE));
			base = (JSONArray) parser.parse(in.readLine());
		}
		catch (FileNotFoundException e)
		{
			throw new FileNotFoundException();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return base;
	}

	private void writeFileBase(JSONArray base)
	{
		try (FileWriter file = new FileWriter(path + LISTFILE)) {
			file.write(base.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setLast_list_id() {
		try {
			JSONArray base = readFileBase();

			for (int i = 0; i < base.size(); i++) {
				JSONObject json = (JSONObject) base.get(i);
				int id = toIntExact((long)json.get("id"));
				if (id > last_list_id)
					last_list_id = id;
			}
		}
		catch (FileNotFoundException e)
		{
			this.last_list_id = 0;
		}
	}

	private void setLast_task_id() {
		try {
			JSONArray base = readFileBase();

			for (int i = 0; i < base.size(); i++) {
				JSONObject json = (JSONObject) base.get(i);
				JSONArray ids = (JSONArray) json.get("task_list");

				if (ids != null) {
					for (int j = 0; j < ids.size(); j++) {
						JSONObject task = (JSONObject) ids.get(j);
						int id = toIntExact((long) task.get("id"));
						if (id > last_task_id)
							last_task_id = id;
					}
				}
				else
				{
					this.last_task_id = 0;
				}
			}
		}
		catch (FileNotFoundException e)
		{
			this.last_task_id = 0;
		}
	}
}
