package com.example.android_app.RoomDB;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.security.auth.callback.Callback;

public class UserRepository{
    private final UserStatsDAO userStatsDAO;
    private final UpgradesUserDAO upgradesUserDAO;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        userStatsDAO = db.userStatsDAO();
        upgradesUserDAO = db.upgradesUserDAO();
        executorService = AppDataBase.databaseWriteExecutor;

        executorService.execute(() -> {

            List<UserStats> allUpgrades = userStatsDAO.getAllUserStats();
            if (allUpgrades == null || allUpgrades.isEmpty()) {
                Log.d("Clicker-> ", "Table is empty");
                upgradeUser();
            } else {
                Log.d("Clicker-> ", "Table is not empty");
            }
        });
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
    public void getUserStats(String userId, BaseCallback callback) {
        executorService.execute(()->{
            callback.onSuccess(userStatsDAO.getUserStatsById(userId));
        });
    }

    // obtener mejoras del usuario por id
    public UpgradesUser getUserUpgradesById(String id) {
        return upgradesUserDAO.getUpgradesByUserId(id);

    }
    //obtener mejoras y nivel del usuario
    public String getUserLevel(String idUpgrades) {
        return upgradesUserDAO.getUserLevel(idUpgrades);
    }
    //consultar mejoras de un nivel concreto
    public String getUserUpgrade(String idUpgrades, int userLevel) {
        return upgradesUserDAO.getUserUpgrade(idUpgrades, userLevel);
    }
    //obtener todas las mejoras del usuario
    public List<UpgradesUser> getAllUserUpgrades(String userId) {
        return upgradesUserDAO.getAllUserUpgrades(userId);
    }
    //obtener una mejora concreta de un usuario concreto
    public UpgradesUser getUserUpgrade(String userId, String upgradeId) {
        return upgradesUserDAO.getUserUpgrade(userId, upgradeId);
    }
    //obtener el nivel de una mejora concreta de un usuario concreto
    public String getUserUpgradeLevel(String userId, String upgradeId) {
        return upgradesUserDAO.getUserUpgradeLevel(userId, upgradeId);
    }

    public void upgradeUser(){
        executorService.execute(() -> {
            UserStats userStats = new UserStats("User1", "User1", 41000, 0, 1);
            userStatsDAO.insert(userStats);

            UpgradesUser upgradesUser1 = new UpgradesUser("upgradeuser_1", "ua_1", "0", "User1");
            upgradesUserDAO.insert(upgradesUser1);

            UpgradesUser upgradesUser2 = new UpgradesUser("upgradeuser_2", "ua_2", "0", "User1");
            upgradesUserDAO.insert(upgradesUser2);

            UpgradesUser upgradesUser3 = new UpgradesUser("upgradeuser_3", "up_1", "0", "User1");
            upgradesUserDAO.insert(upgradesUser3);

            UpgradesUser upgradesUser4 = new UpgradesUser("upgradeuser_4", "up_2", "0", "User1");
            upgradesUserDAO.insert(upgradesUser4);
        });
    }
}
