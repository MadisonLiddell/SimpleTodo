package edu.rasmussen.SimpleTodo;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * @author Madison Liddell
 * @since 2015-10-12
 *
 * Activity for adding a new task.
 * Allows user to enter the task's name, type, and date + time deadline.
 * Returns the user's Task object
 */
public class AddTaskActivity extends Activity implements DatePickerFragment.DialogListener,TimePickerFragment
        .DialogListener
{
    private GregorianCalendar calendar;         // holds user's currently selected deadline time/date
    // Keys for state saving
    static final String STATE_NAME = "name";
    static final String STATE_TYPE = "type";
    static final String STATE_DATE = "date";
    static final String STATE_TIME = "time";
    static final String TASK = "task";          // task object id
    // View object handles
    private EditText name;
    private Spinner type;
    private TextView date, time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        calendar = new GregorianCalendar();
        name = (EditText) findViewById(R.id.editTextTaskName);
        type = (Spinner) findViewById(R.id.spinnerTaskTypes);
        date = (TextView) findViewById(R.id.textViewDate);
        time = (TextView) findViewById(R.id.textViewTime);
        // Restore state
        if (savedInstanceState != null)
        {
            name.setText(savedInstanceState.getString(STATE_NAME));
            date.setText(savedInstanceState.getString(STATE_DATE));
            time.setText(savedInstanceState.getString(STATE_TIME));
            type.setSelection(savedInstanceState.getInt(STATE_TYPE));
        }

        // Save button: send current task details to main activity and go back to main
        Button button1 =(Button)findViewById(R.id.buttonSaveTask);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = saveTask();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra(TASK, task);
                setResult(RESULT_OK, intent);
                //savedInstanceState.clear();
                finish();
            }
        });

        // Cancel button: go back to main To-do list
        Button button2 =(Button)findViewById(R.id.buttonCancel);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // savedInstanceState.clear();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    // Save user entries
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString(STATE_NAME, name.getText().toString());
        savedInstanceState.putInt(STATE_TYPE, type.getSelectedItemPosition());
        savedInstanceState.putString(STATE_DATE, date.getText().toString());
        savedInstanceState.putString(STATE_TIME, time.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    // Shows calendar picker popup
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePickerDialog");
    }

    // Shows time picker popup
    public void showTimePickerDialog(View v)
    {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePickerDialog");
    }

    // Save task details using current input, returns Task object
    private Task saveTask()
    {
        // Create new task object from user entered data
        Task task = new Task(name.getText().toString(), type.getSelectedItem().toString(), calendar.getTimeInMillis());

        return task;
    }

    // Receives selected calendar from calendar picker dialog fragment
    @Override
    public void updateDate(int year, int month, int day) {
        calendar.set(year, month, day);
        // set the textview
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        sdf.setCalendar(calendar);
        date.setText(sdf.format(calendar.getTime()));
    }

    // Receives selected time from time picker dialog fragment
    @Override
    public void updateTime(int hourOfDay, int minute) {
        calendar.set(calendar.get(GregorianCalendar.YEAR), calendar.get(GregorianCalendar.MONTH),
                     calendar.get(GregorianCalendar.DAY_OF_MONTH), hourOfDay, minute);
        // Set textview
        SimpleDateFormat sdf = new SimpleDateFormat("KK:mm a");
        sdf.setCalendar(calendar);
        time.setText(sdf.format(calendar.getTime()));
    }
}