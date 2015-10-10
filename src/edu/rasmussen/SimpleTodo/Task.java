package edu.rasmussen.SimpleTodo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by Madison Liddell on 10/8/2015.
 */
public class Task implements Serializable{
    public String name;
    public String type;
    public GregorianCalendar deadline;

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
}
