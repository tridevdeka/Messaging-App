package com.example.messagingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.messagingapp.Models.MessageModel;
import com.example.messagingapp.Models.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Helper {

    public  static final Gson gson=new Gson();

    public void saveTasks(Context context, ArrayList<Users> tasks){
        SharedPreferences sharedPreferences= context.getSharedPreferences("names",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("keys", gson.toJson(tasks));
        editor.apply();

    }

    public ArrayList<Users> getTasks(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("names", Context.MODE_PRIVATE);
        String tasksString = sharedPreferences.getString("keys", null);
        Type taskType = new TypeToken<ArrayList<Users>>() {
        }.getType();
        ArrayList<Users> tasks = gson.fromJson(tasksString, taskType);

        if (tasks != null) return tasks;
        else return new ArrayList<>();
    }
}
