package edu.rasmussen.SimpleTodo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * @author Madison Liddell
 * @since 2015-10-12
 *
 * Task class represents a single to-do list task.
 * Has a name, type, and deadline date.
 */
public class Task implements Parcelable
{
    private int id;
    private String name;
    private String type;
    private Long date;                   // only used for passing date in parcel
    private GregorianCalendar deadline;  // set to task deadline date and time
    public static final String KEY_NAME ="name", KEY_TYPE = "type", KEY_DATE = "date";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public GregorianCalendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Long date) {
        this.deadline = new GregorianCalendar();
        this.deadline.setTimeInMillis(date);
        setDate(date);
    }

    // Constructor for a Task
    public Task(String name, String type, Long date)
    {
        this.name = name;
        this.type = type;
        this.date = date;
        this.deadline = new GregorianCalendar();
        deadline.setTimeInMillis(date);     // set the deadline using date param
    }

    public Task() {}

    // Return formatted string using task's date
    public String getStringDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        sdf.setCalendar(deadline);
        String s = sdf.format(deadline.getTime());
        return s;
    }

    // Return formatted string using task's time
    public String getStringTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("KK:mm a");
        sdf.setCalendar(deadline);
        String s = sdf.format(deadline.getTime());
        return s;
    }

    // Parcelable required creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        @Override
        public Task createFromParcel(Parcel source) {
            // read bundle from the parcel
            Bundle bundle = source.readBundle();
            // create new Task from bundle key-value pairs
            return new Task(bundle.getString(KEY_NAME), bundle.getString(KEY_TYPE), bundle.getLong(KEY_DATE));
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        Bundle bundle = new Bundle();
        // add key-value pairs for a Task to the bundle
        bundle.putString(KEY_NAME, name);
        bundle.putString(KEY_TYPE, type);
        bundle.putLong(KEY_DATE, date);
        // write the bundle to the parcel
        dest.writeBundle(bundle);
    }
}
