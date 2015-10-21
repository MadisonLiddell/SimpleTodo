package edu.rasmussen.SimpleTodo;

/**
 * @author Madison Liddell
 * @since 10/20/2015
 *
 * SQLite Database Helper class
 * Adds abstraction for working with the Tasks database. It has a single Task table with columns for id (primary key),
 * name, type, and deadline
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "tasks.db";
    private static final int DB_VERSION = 1;

    // Task table has PK, name, type, and deadline
    public static final String TABLE_TASK = "Task";
    public static final String COLUMN_PRIMARY_KEY = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DEADLINE = "deadline";

    // SQL Statement to create a new database.
    private static final String DATABASE_CREATE = "create table " + TABLE_TASK
            + "(" + COLUMN_PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_DEADLINE + " text not null);";
    private SQLiteDatabase m_myDatabase;

    /**
     * Constructor
     * @param context context to use
     */
    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Close database session
     */
    @Override
    public synchronized void close()
    {
        if (m_myDatabase != null) {
            m_myDatabase.close();
            m_myDatabase = null;
        }

        super.close();
    }

    /**
     * Called when no database exists in disk and the helper class need to create a new one.
     * @param db database to use for creation
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * Upgrade database, deleting old version and creating new one
     * @param db database to upgrade
     * @param oldVersion old version number
     * @param newVersion new version number to use
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                      + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }
}