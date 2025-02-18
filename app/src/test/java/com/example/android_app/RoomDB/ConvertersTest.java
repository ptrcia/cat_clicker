package com.example.android_app.RoomDB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class ConvertersTest {

    @Test
    public void testFromStringToLevelList() {
        String json = "[{\"level\":1,\"upgrade\":2},{\"level\":3,\"upgrade\":4}]";
        List<Level> levels = Converters.fromStringToLevelList(json);
        assertEquals(2, levels.size());
        assertEquals(1, levels.get(0).getLevel());
        assertEquals(2, levels.get(0).getCost());
        assertEquals(3, levels.get(1).getLevel());
        assertEquals(4, levels.get(1).getEffect());
    }

    @Test
    public void testFromLevelListToString() {
        List<Level> levels = Arrays.asList(new Level(1, 2, 3), new Level(4, 5, 6));
        String json = Converters.fromLevelListToString(levels);
        assertEquals("[{\"level\":1,\"upgrade\":2},{\"level\":3,\"upgrade\":4}]", json);
    }

    @Test
    public void testFromStringToUpgradesUserList() {
        String json = "[{\"level\":1,\"upgrade\":2},{\"level\":3,\"upgrade\":4}]";
        List<UpgradesUser> upgradesUsers = Converters.fromStringToUpgradesUserList(json);
        assertEquals(2, upgradesUsers.size());
        assertEquals(1, upgradesUsers.get(0).getUserLevel());
        assertEquals(2, upgradesUsers.get(0).getUserUpgrade());
        assertEquals(3, upgradesUsers.get(1).getUserLevel());
        assertEquals(4, upgradesUsers.get(1).getUserUpgrade());
    }

    @Test
    public void testFromUpgradesUserListToString() {
        List<UpgradesUser> upgradesUsers = Arrays.asList(new UpgradesUser(1, 2), new UpgradesUser(3, 4));
        String json = Converters.fromUpgradesUserListToString(upgradesUsers);
        assertEquals("[{\"level\":1,\"upgrade\":2},{\"level\":3,\"upgrade\":4}]", json);
    }

    @Test
    public void testNullConversions() {
        assertNull(Converters.fromStringToLevelList(null));
        assertNull(Converters.fromLevelListToString(null));
        assertNull(Converters.fromStringToUpgradesUserList(null));
        assertNull(Converters.fromUpgradesUserListToString(null));
    }
}
