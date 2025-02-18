package com.example.android_app.RoomDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.List;

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

    @TypeConverter
    public static List<UpgradesUser> fromStringToUpgradesUserList(String value) {
        Type listType = new TypeToken<List<UpgradesUser>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromUpgradesUserListToString(List<UpgradesUser> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
