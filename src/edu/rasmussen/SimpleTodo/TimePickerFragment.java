package edu.rasmussen.SimpleTodo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * @author Madison Liddell
 * @since 2015-10-07
 *
 * Time picker popup dialog. Updates time text display in caller activity
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                                    DateFormat.is24HourFormat(getActivity()));
    }

    // Send time back to caller
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DialogListener activity = (DialogListener) getActivity();
        activity.updateTime(hourOfDay, minute);
    }

    public interface DialogListener {
        void updateTime(int hourOfDay, int minute);
    }
}