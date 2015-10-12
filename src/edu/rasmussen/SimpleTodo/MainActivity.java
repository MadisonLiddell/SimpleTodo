package edu.rasmussen.SimpleTodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * @author Madison Liddell
 * @since 2015-10-12
 *
 * Simple To-Do List application
 * Saves state using bundles
 */

public class MainActivity extends Activity {
    static final int ADD_TASK_REQUEST = 0;
    // Keys
    static final String TASK = "task";
    static final String STATE_LIST = "todoList";

    private ArrayList<Task> todoList;               // list holding all pending tasks
    // View object handles
    private Button button;
    private TableLayout table;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (savedInstanceState != null)
        {
             // Restore list from saved state
             todoList = savedInstanceState.getParcelableArrayList(STATE_LIST);
         } else
        {
             // Setup new list
             todoList = new ArrayList<>();
         }

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
            // Setup 3 textviews
            TableRow row = new TableRow(this);
            TextView col1 = new TextView(this);
            TextView col2 = new TextView(this);
            TextView col3 = new TextView(this);
            // Set using Task object values
            col1.setText(task.type + " ");
            col2.setText(task.name);
            GregorianCalendar today = new GregorianCalendar();  // get current date
            if (today.get(GregorianCalendar.MONTH) == task.deadline.get(GregorianCalendar.MONTH)
                    && today.get(GregorianCalendar.DAY_OF_MONTH) == task.deadline.get(GregorianCalendar.DAY_OF_MONTH))
                col3.setText(task.getStringTime());             // display only the time for tasks due today
            else
                col3.setText(task.getStringDate());             // display only the date for future tasks
            // Add the 3 textviews to a row
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
            }
        }
        else if (resultCode == RESULT_CANCELED)
        {
            // do nothing right now
        }
    }

    // Update visual table
    @Override
    protected void onResume() {
        super.onResume();
        updateTable();
    }
}
