package com.example.lietotne_todo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Structure> todo;
    private Database base;
    private MainActivity activity;

    public Adapter(Database db, MainActivity activity) {
        this.base = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        base.openDatabase();

        final Structure item = todo.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    base.updateStatus(item.getId(), 1);
                } else {
                    base.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todo.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<Structure> todoList) {
        this.todo = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        Structure item = todo.get(position);
        base.deleteTask(item.getId());
        todo.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        Structure item = todo.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddTask fragment = new AddTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.checking);
        }
    }
}
