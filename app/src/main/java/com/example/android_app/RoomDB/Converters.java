package com.example.android_app.RoomDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.List;

/*public class Converters {

    private static final Gson gson = new Gson();

    //Convertir la lista en json para que Room lo entienda
    @TypeConverter
    public static String convertToJson(List<Level> levels){
        return gson.toJson(levels);
    }

    //Convierte el Json en la lista de niveles
    @TypeConverter
    public static List<Level> convertToObject(String levelsJson){
        return new Gson().fromJson(levelsJson, new TypeToken<List<Level>>(){}.getType());
    }
}*/
public class Converters {
    @TypeConverter
    public static List<Level> fromString(String value) {
        Type listType = new TypeToken<List<Level>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Level> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
