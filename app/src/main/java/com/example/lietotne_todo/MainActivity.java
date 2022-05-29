package com.example.lietotne_todo;
// Adrians Mackevičs, 201RDB281, 6.Grupa
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DCListener {

    private FloatingActionButton FAB;
    private List<Structure> td;
    private Database base;
    private RecyclerView Trecycle;
    private Adapter TAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        base = new Database(this);
        base.openDatabase();

        Trecycle = findViewById(R.id.recycler);
        Trecycle.setLayoutManager(new LinearLayoutManager(this));
        TAdp = new Adapter(base,MainActivity.this);
        Trecycle.setAdapter(TAdp);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new Recycle(TAdp));
        itemTouchHelper.attachToRecyclerView(Trecycle);

        FAB = findViewById(R.id.floatingButton);
        td = base.getAllTasks();
        Collections.reverse(td);
        TAdp.setTasks(td);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTask.newInstance().show(getSupportFragmentManager(), AddTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        td = base.getAllTasks();
        Collections.reverse(td);
        TAdp.setTasks(td);
        TAdp.notifyDataSetChanged();
    }
}