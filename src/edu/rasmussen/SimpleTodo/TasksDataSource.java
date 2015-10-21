package edu.rasmussen.SimpleTodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * @author Madison Liddell
 * @since 10/20/2015
 *
 * Handles interactions with the SQLite database and DatabaseHelper
 */
public class TasksDataSource
{
    // Handles
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    // Database fields/columns
    private String[] allColumns = {DatabaseHelper.COLUMN_PRIMARY_KEY, DatabaseHelper.COLUMN_NAME, DatabaseHelper
            .COLUMN_TYPE, DatabaseHelper.COLUMN_DEADLINE};

    /**
     * Constructor
     * @param context current context to use with DatabaseHelper
     */
    public TasksDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Open the connection
     */
    public void open()
    {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close the connection
     */
    public void close()
    {
        dbHelper.close();
    }

    /**
     * Inserts a table into the database with columns for a Task and returns the new Task
     * @param name task name
     * @param type task type
     * @param deadline date and time in long form
     * @return Task
     */
    public Task createTask(String name, String type, long deadline)
    {
        ContentValues values = new ContentValues();
        // add fields to table
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_TYPE, type);
        values.put(DatabaseHelper.COLUMN_DEADLINE, deadline);
        // add the table to the database
        long insertId = database.insert(DatabaseHelper.TABLE_TASK, null, values);
        // create cursor to the database
        Cursor cursor = database.query(DatabaseHelper.TABLE_TASK,
                                       allColumns, DatabaseHelper.COLUMN_PRIMARY_KEY + " = " + insertId, null,
                                       null, null, null);
        // set to first entry
        cursor.moveToFirst();
        // create new task object using the cursor
        Task newTask = cursorToTask(cursor);
        cursor.close();
        return(newTask);
    }

    /**
     * Delete a task from the database
     * @param task to delete
     */
    public void deleteTask(Task task)
    {
        int id = task.getId();
        // Delete entries matching the task id
        database.delete(DatabaseHelper.TABLE_TASK, DatabaseHelper.COLUMN_PRIMARY_KEY + "=" + id, null);
    }

    /**
     * Get all saved tasks from the database
     * @return List of matching tasks
     */
    public ArrayList<Task> getAllTasks()
    {
        ArrayList<Task> todoList = new ArrayList<>();
        // Query all tasks
        Cursor cursor = database.query(DatabaseHelper.TABLE_TASK, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            // create new task using current cursor data
            Task task = cursorToTask(cursor);
            // add task to list
            todoList.add(task);
            cursor.moveToNext();
        }
        cursor.close(); //close cursor
        return todoList;
    }

    /**
     * Get only tasks ending today from the database
     * @return List of matching tasks
     */
    public ArrayList<Task> getTodaysTasks(GregorianCalendar today)
    {
        ArrayList<Task> todoList = new ArrayList<>();
        // Select * where deadline >= 12 hours ago and <= 12 hours from now
        String whereClause = DatabaseHelper.COLUMN_DEADLINE + " >= " + " ? " +
                "and " + DatabaseHelper.COLUMN_DEADLINE + " <= ?";
        // Compare to 12 hours past and 12 hours ahead
        Long currentTime = System.currentTimeMillis() - (TimeUnit.DAYS.toMillis(1)/2);
        Long currentDay = System.currentTimeMillis() + (TimeUnit.DAYS.toMillis(1));
        String[] whereArgs = {currentTime.toString(), currentDay.toString()};

        // Query tasks ending today
        Cursor cursor = database.query(DatabaseHelper.TABLE_TASK, allColumns, whereClause,  whereArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            // create new task using current cursor data
            Task task = cursorToTask(cursor);
            // add task to list
            todoList.add(task);
            cursor.moveToNext();
        }
        cursor.close(); //close cursor
        return todoList;
    }

    /**
     * Create Task object from Cursor object
     * @param cursor to create Task from
     * @return Task
     */
    private Task cursorToTask(Cursor cursor)
    {
        Task task = new Task();
        task.setName(cursor.getString(1));  // name
        task.setType(cursor.getString(2));  // type
        task.setDeadline(cursor.getLong(3));    // date
        return(task);
    }
}
