package com.example.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static final String HIGHEST_SCORE = "highest_score";
    public static final String LAST_INDEX = "Last_Index";
    private SharedPreferences preferences;

    public Prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score){
        int lastScore = preferences.getInt(HIGHEST_SCORE,0);
        if(score>lastScore){
            preferences.edit().putInt(HIGHEST_SCORE,score).apply();
        }
    }

    public int getHighestScore(){
        return preferences.getInt(HIGHEST_SCORE,0);
    }

    public void continue_quiz(int index){
        preferences.edit().putInt(LAST_INDEX,index).apply();
    }

    public int getLastIndex(){
        return preferences.getInt(LAST_INDEX,0);
    }
}
