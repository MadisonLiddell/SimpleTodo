package edu.rasmussen.SimpleTodo;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by Madison Liddell on 10/7/2015.
 */
public class AddTaskActivity extends Activity implements DatePickerFragment.DialogListener,TimePickerFragment
        .DialogListener
{
    private GregorianCalendar calendar;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String NAME_KEY = "TASK_NAME";
    public static final String TYPE_KEY ="SELECTED_TYPE";
    SharedPreferences sharedpreferences;

    EditText name;
    Spinner type;
    TextView date, time;
    int typePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        calendar = new GregorianCalendar();
        name = (EditText) findViewById(R.id.editTextTaskName);
        type = (Spinner) findViewById(R.id.spinnerTaskTypes);
        date = (TextView) findViewById(R.id.textViewDate);
        time = (TextView) findViewById(R.id.textViewTime);

        // Save button: send current task details to main activity and go back to main
        Button button1 =(Button)findViewById(R.id.buttonSaveTask);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = saveTask();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("task", task);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Cancel button: go back to main To-do list
        Button button2 =(Button)findViewById(R.id.buttonCancel);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearState();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    // Save all current entries
    private void saveState()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NAME_KEY, name.getText().toString());
        typePosition = type.getSelectedItemPosition();
        editor.putInt(TYPE_KEY, typePosition);

        editor.commit();
    }

    // Restore saved entries
    private void restoreState()
    {
        name.setText(sharedpreferences.getString(NAME_KEY, null));
        type.setSelection(sharedpreferences.getInt(TYPE_KEY, 0));
    }

    // Clear all entries from preferences
    private void clearState()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(NAME_KEY);
        editor.remove(TYPE_KEY);
        editor.commit();
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

    // Save task details, returns Task object
    private Task saveTask()
    {
        Task task = new Task();
        task.name = name.getText().toString();
        task.type = type.getSelectedItem().toString();
        task.deadline = calendar;

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

    @Override
    protected void onStop() {
        super.onStop();
        saveState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearState();
    }
}