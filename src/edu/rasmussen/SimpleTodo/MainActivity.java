package edu.rasmussen.SimpleTodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
* Madison Liddell
* 2015-10-09
* Saves state using SharedPreferences
* */

public class MainActivity extends Activity {
    static final int ADD_TASK_REQUEST = 0;
    static final String TODOLIST = "TodoList";
    private ArrayList<Task> todoList;
    private Button button;
    private TableLayout table;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        table = (TableLayout) findViewById(R.id.tableTasks);
        // Add new task button, saves to-do list and starts add task activity
        button =(Button)findViewById(R.id.buttonNewTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), AddTaskActivity.class), ADD_TASK_REQUEST);
            }
        });
    }

    // Save state by saving to-do list
    private void saveList()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String jsonTodoList = gson.toJson(todoList);
        editor.putString(TODOLIST, jsonTodoList);
        editor.commit();
    }

    // Retrieve saved to-do list
    private void getList()
    {
        if (sharedpreferences.contains(TODOLIST))
        {
            String jsonTodoList = sharedpreferences.getString(TODOLIST, null);
            Gson gson = new Gson();
            todoList = gson.fromJson(jsonTodoList, new TypeToken<ArrayList<Task>>() {}.getType());
        }
        else    // create new list
        {
            todoList = new ArrayList<>();
        }
    }

    // Update table with all saved tasks
    private void updateTable()
    {
        if (todoList.isEmpty())
            return;

        table.removeAllViews();
        for (Task task : todoList) {
            // Add 3 textviews to row, then add the row to the table
            TableRow row = new TableRow(this);
            TextView col1 = new TextView(this);
            TextView col2 = new TextView(this);
            TextView col3 = new TextView(this);

            col1.setText(task.type + " ");
            col2.setText(task.name);
            GregorianCalendar today = new GregorianCalendar();
            if (today.get(GregorianCalendar.MONTH) == task.deadline.get(GregorianCalendar.MONTH)
                    && today.get(GregorianCalendar.DAY_OF_MONTH) == task.deadline.get(GregorianCalendar.DAY_OF_MONTH))
                col3.setText(task.getStringTime());         // use only time for tasks due today
            else
                col3.setText(task.getStringDate());         // use full date for future tasks

            row.addView(col1);
            row.addView(col2);
            row.addView(col3);

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
                task = (Task) b.getSerializable("task");
                // add new task to list
                todoList.add(task);
                // Save todoList
                saveList();
            }
        }
        else if (resultCode == RESULT_CANCELED)
        {

        }
    }

    // Get to-do list and update visual table onStart
    @Override
    protected void onResume() {
        super.onResume();
        getList();
        updateTable();
        Log.i("MainActivity", "onResume called getlist and updateTable");
    }
}
