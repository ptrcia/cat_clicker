package com.example.android_app.RoomDB;
import android.app.Application;
import androidx.lifecycle.LiveData;
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
    public LiveData<UserStats> getUserStats(int userId) {
        return userStatsDAO.getUserStats(userId);
    }

    // obtener mejoras del usuario
    public LiveData<UpgradesUser> getUserUpgrades(int id) {
        return upgradesUserDAO.getUpgradesByUserId(id);

    }

    public void upgradeUser(){
        UserStats userStats = new UserStats("User1", 0, 0, 0, 1);
        UpgradesUser upgradesUser = new UpgradesUser(1, 0, 0, 0);
    }

}
