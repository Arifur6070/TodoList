package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.mytodolist.adapter.TodoAdapter;
import com.example.mytodolist.model.Todo;
import com.example.mytodolist.util.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


///This is the main activity where we will see the dummy task in the list
public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView taskView;
    private FloatingActionButton fab;//this is the green button
    private TodoAdapter todoAdapter;
    private DatabaseHandler db = new DatabaseHandler(this);

    //List of tasks
    private List<Todo> todoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getActionBar().hide();

        taskView=findViewById(R.id.tasksRecyclerView);
        fab = findViewById(R.id.fab);
        taskView.setLayoutManager(new LinearLayoutManager(this));

        //List is getting processed here todo adaptar
        todoAdapter = new TodoAdapter(this,db);
        taskView.setAdapter(todoAdapter);

        Todo todo = new Todo();

        todo.setTask("Test Task");
        todo.setStatus(0);
        todo.setId(1);

        todoList=db.getAllTasks();
        Collections.reverse(todoList);
        todoAdapter.setTodo(todoList);

        //swiping behaviour of deletion
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(todoAdapter));
        itemTouchHelper.attachToRecyclerView(taskView);

       //clicking button this event gets fired up
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        todoList = db.getAllTasks();
        Collections.reverse(todoList);
        todoAdapter.setTodo(todoList);
        todoAdapter.notifyDataSetChanged();
    }
}


