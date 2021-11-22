package com.example.messagingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.messagingapp.Models.MessageModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DatabaseHelper {

    public  static final Gson gson=new Gson();

    public void saveTasks(Context context, ArrayList<MessageModel>tasks){
        SharedPreferences sharedPreferences= context.getSharedPreferences("name",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("key", gson.toJson(tasks));
        editor.apply();

    }

    public ArrayList<MessageModel> getTasks(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("name", Context.MODE_PRIVATE);
        String tasksString = sharedPreferences.getString("key", null);
        Type taskType = new TypeToken<ArrayList<MessageModel>>() {
        }.getType();
        ArrayList<MessageModel> tasks = gson.fromJson(tasksString, taskType);

        if (tasks != null) return tasks;
        else return new ArrayList<>();
    }
}
