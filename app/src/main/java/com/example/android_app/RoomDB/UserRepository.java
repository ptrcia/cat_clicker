package com.example.android_app.RoomDB;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class UserRepository {
    private final UserStatsDAO userStatsDAO;
    private final UpgradesUserDAO upgradesUserDAO;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        userStatsDAO = db.userStatsDAO();
        upgradesUserDAO = db.upgradesUserDAO();
        executorService = AppDataBase.databaseWriteExecutor;

        LiveData<List<UserStats>>  allUpgrades = userStatsDAO.getAllUserStats();
        if (allUpgrades.getValue() == null || allUpgrades.getValue().isEmpty()) {
            Log.d("Clicker-> ", "Table is empty");
            upgradeUser();
        } else {
            Log.d("Clicker-> ", "Table is not empty");
        }
    }

    //region Introoducir dato aa usuario
    //Introducir a UserStats
    public void insert(UserStats userStats) {
        executorService.execute(() -> userStatsDAO.insert(userStats));
    }
    //Introducir a UpgradesUser
    public void insert(UpgradesUser upgradesUser) {
        executorService.execute(() -> upgradesUserDAO.insert(upgradesUser));
    }
    //endregion

    // Obtener estadísticas del usuario por ID
    public LiveData<UserStats> getUserStats(String userId) {
        return userStatsDAO.getUserStatsById(userId);
    }

    // obtener mejoras del usuario por id
    public LiveData<UpgradesUser> getUserUpgradesById(String id) {
        return upgradesUserDAO.getUpgradesByUserId(id);

    }
    //obtener mejoras y nivel del usuario
    public LiveData<Integer> getUserLevel(String idUpgrades) {
        return upgradesUserDAO.getUserLevel(idUpgrades);
    }
    //consultar mejoras de un nivel concreto
    public LiveData<String> getUserUpgrade(String idUpgrades, int userLevel) {
        return upgradesUserDAO.getUserUpgrade(idUpgrades, userLevel);
    }
    //obtener todas las mejoras del usuario
    public LiveData<List<UpgradesUser>> getAllUserUpgrades(String userId) {
        return upgradesUserDAO.getAllUserUpgrades(userId);
    }
    //obtener una mejora concreta de un usuario concreto
    public LiveData<UpgradesUser> getUserUpgrade(String userId, String upgradeId) {
        return upgradesUserDAO.getUserUpgrade(userId, upgradeId);
    }
    //obtener el nivel de una mejora concreta de un usuario concreto
    public LiveData<Integer> getUserUpgradeLevel(String userId, String upgradeId) {
        return upgradesUserDAO.getUserUpgradeLevel(userId, upgradeId);
    }



    public void upgradeUser(){
        executorService.execute(() -> {
            UserStats userStats = new UserStats("User1", "User1", 41000, 0, 1);
            userStatsDAO.insert(userStats);

            UpgradesUser upgradesUser1 = new UpgradesUser("upgradeuser_1", "up_1", 0, "User1");
            upgradesUserDAO.insert(upgradesUser1);

            UpgradesUser upgradesUser2 = new UpgradesUser("upgradeuser_2", "up_2", 0, "User1");
            upgradesUserDAO.insert(upgradesUser2);
        });
    }
}
