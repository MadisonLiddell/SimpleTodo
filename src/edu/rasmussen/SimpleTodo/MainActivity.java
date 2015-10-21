package edu.rasmussen.SimpleTodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * @author Madison Liddell
 * @since 2015-10-20
 *
 * Simple To-Do List application
 * Saves state using bundles, saves user data using SQLite database.
 */

public class MainActivity extends Activity {
    static final int ADD_TASK_REQUEST = 0;
    // Keys
    static final String TASK = "task";
    static final String STATE_LIST = "todoList";

    private ArrayList<Task> todoList;               // list holding all pending tasks
    // View object handles
    private Button buttonAdd, buttonToday;
    private TableLayout table;

    private TasksDataSource dataSource;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Initialize and open Tasks data source
        dataSource = new TasksDataSource(this);
        dataSource.open();
        // Set todolist to the database tasks
        todoList = dataSource.getAllTasks();

        table = (TableLayout) findViewById(R.id.tableTasks);
        // Add new task button, saves to-do list and starts add task activity
        buttonAdd =(Button)findViewById(R.id.buttonNewTask);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), AddTaskActivity.class), ADD_TASK_REQUEST);
            }
        });
        // Show only Today's Tasks button
        buttonToday =(Button)findViewById(R.id.buttonTodaysTasks);
        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fill todoList only with tasks ending today
                GregorianCalendar today = new GregorianCalendar();
                todoList = dataSource.getTodaysTasks(today);
                updateTable();
            }
        });
    }

    // Save to-do list
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putParcelableArrayList(STATE_LIST, todoList);
        super.onSaveInstanceState(savedInstanceState);
    }

    // Update table with all saved tasks
    private void updateTable()
    {
        if (todoList == null || todoList.isEmpty())
            return;
        // clear the table before adding all tasks back
        table.removeAllViews();
        for (Task task : todoList)
        {
            // Setup 3 text views
            TableRow row = new TableRow(this);
            TextView col1 = new TextView(this);
            TextView col2 = new TextView(this);
            TextView col3 = new TextView(this);
            // Set using Task object values
            col1.setText(task.getType() + " ");
            col2.setText(task.getName());
            //GregorianCalendar today = new GregorianCalendar();  // get current date
            if (DateUtils.isToday(task.getDate()))
                col3.setText(task.getStringTime());             // display only the time for tasks due today
            else
                col3.setText(task.getStringDate());             // display only the date for future tasks
            // Add the 3 text views to a row
            row.addView(col1);
            row.addView(col2);
            row.addView(col3);
            // Add the row to the table
            table.addView(row, new TableLayout.LayoutParams
                    (TableLayout.LayoutParams.MATCH_PARENT,
                     TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    // Receive and save created Task
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK)
        {
            Task task;
            Bundle b = data.getExtras();
            if (b != null)
            {
                task = b.getParcelable(TASK);
                // add new task to list
                todoList.add(task);
                // save task to database
                dataSource.open();
                dataSource.createTask(task.getName(), task.getType(), task.getDate());
            }
        }
        else if (resultCode == RESULT_CANCELED)
        {
            // do nothing right now
        }
    }

    /**
     * Open
     */
    @Override
    protected void onResume() {
        dataSource.open();
        updateTable();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
