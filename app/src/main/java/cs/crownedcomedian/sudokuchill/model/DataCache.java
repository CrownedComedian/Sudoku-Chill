package cs.crownedcomedian.sudokuchill.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs.crownedcomedian.sudoku.GameBoard;
import cs.crownedcomedian.sudokuchill.R;

public class DataCache {
    private static DataCache singletonInstance = new DataCache();
    public static final Gson gson = new Gson();
    public Map<Integer, String> values = new HashMap<>();

    //theme colors
    public List<SudokuTheme> themes = new ArrayList<>();
    public SudokuTheme activeTheme;

    public GameBoard beginnerGame = null;
    public GameBoard easyGame = null;
    public GameBoard mediumGame = null;
    public GameBoard hardGame = null;
    public GameBoard expertGame = null;

    //achievements tree?

    private DataCache() {
        values.put(0, " ");
        values.put(1, "1");
        values.put(2, "2");
        values.put(3, "3");
        values.put(4, "4");
        values.put(5, "5");
        values.put(6, "6");
        values.put(7, "7");
        values.put(8, "8");
        values.put(9, "9");
        themes.add(new SudokuTheme());
        activeTheme = themes.get(0);

        //TODO - load achievements tree progress
    }

    public static DataCache getInstance() {
        return singletonInstance;
    }

    public static DataCache newInstance() {
        singletonInstance = new DataCache();

        return getInstance();
    }

    public static DataCache saveInstance(AppCompatActivity activity) {
        String dataCacheJsonStr = gson.toJson(singletonInstance);
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(activity.getResources().getString(R.string.app_name), dataCacheJsonStr);
        editor.apply();

        return singletonInstance;
    }

    public static DataCache loadInstance(AppCompatActivity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String dataCacheJsonStr = sharedPref.getString(activity.getString(R.string.app_name), null);

        if(dataCacheJsonStr == null) {
            singletonInstance = newInstance();
        } else {
            singletonInstance = gson.fromJson(dataCacheJsonStr, DataCache.class);
        }

        return singletonInstance;
    }
}
