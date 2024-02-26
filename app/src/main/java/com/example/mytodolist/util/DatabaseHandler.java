package com.example.mytodolist.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mytodolist.model.Todo;

import java.util.ArrayList;
import java.util.List;

//This is the class which will handle the database operations
//Sqlite is light weight database which we normally use for
//practising implementations of database while developing a project
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION =1;
    private static final String NAME ="todoDatabase";
    private static final String TABLE= "table_todos";

    //Now comes the table column definitions
    private static final String ID= "id";
    private static final String TODO ="task";
    private static final String STATUS = "status";

    //Here is the create table sql query
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TODO + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db ;


    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }


    //This function will create the table sql query
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        // Create tables again
        onCreate(db);
    }


    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(Todo task){
        ContentValues cv = new ContentValues();
        cv.put(TODO, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<Todo> getAllTasks(){
        db=this.getWritableDatabase();
        List<Todo> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        Todo task = new Todo();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TODO)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TODO, task);
        db.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}