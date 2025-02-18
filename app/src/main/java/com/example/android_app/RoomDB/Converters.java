package com.example.android_app.RoomDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<Level> fromStringToLevelList(String value) {
        if (value == null) return null;
        Type listType = new TypeToken<List<Level>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromLevelListToString(List<Level> list) {
        if (list == null) return null;
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<UpgradesUser> fromStringToUpgradesUserList(String value) {
        if (value == null) return null;
        Type listType = new TypeToken<List<UpgradesUser>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromUpgradesUserListToString(List<UpgradesUser> list) {
        if (list == null) return null;
        return gson.toJson(list);
    }
}
