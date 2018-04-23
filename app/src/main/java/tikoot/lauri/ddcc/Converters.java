package tikoot.lauri.ddcc;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Class handles type conversions for database handling
 */
public class Converters {

    /**
     * Method returns a 2D List from a String
     * @param value string to parse from
     * @return List<List<String>>
     */
    @TypeConverter
    public static List<List<String>> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    /**
     * Method converts 2D List to String
     * @param list convertable 2D List
     * @return String representation of given List
     */
    @TypeConverter
    public static String fromArrayList(List<List<String>> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d("tikoot.lauri.ddcc", "Converters: json = "+ json);
        return json;
    }

    /**
     * Method converts 2D integer array to a String
     * @param array array to convert
     * @return single String representating array
     */
    @TypeConverter
    public static String fromIntegerTwoDimensionalArray(int[][] array){
        Gson gson = new Gson();
        String json = gson.toJson(array);
        return json;
    }

    /**
     * Method converts String to 2D integer array
     * @param value
     * @return
     */
    @TypeConverter
    public static int[][] fromStringToIntegerTwoDArray(String value){
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
}