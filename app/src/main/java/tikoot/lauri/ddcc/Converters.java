package tikoot.lauri.ddcc;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<List<String>> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<List<String>> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d("tikoot.lauri.ddcc", "Converters: json = "+ json);
        return json;
    }

    @TypeConverter
    public static String fromIntegerTwoDimensionalArray(int[][] array){
        Gson gson = new Gson();
        String json = gson.toJson(array);
        return json;
    }

    @TypeConverter
    public static int[][] fromStringToIntegerTwoDArray(String value){
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
}