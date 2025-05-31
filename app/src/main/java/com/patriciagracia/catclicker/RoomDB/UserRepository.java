package com.patriciagracia.catclicker.RoomDB;
import android.app.Application;
import android.util.Log;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class UserRepository{
    private final UserStatsDAO userStatsDAO;
    private final UpgradesUserDAO upgradesUserDAO;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        userStatsDAO = db.userStatsDAO();
        upgradesUserDAO = db.upgradesUserDAO();
        executorService = AppDataBase.databaseWriteExecutor;
        //db.clearAllTables();

        executorService.execute(() -> {
            List<UserStats> allUpgrades = userStatsDAO.getAllUserStats();
            if (allUpgrades == null || allUpgrades.isEmpty()) {
                Log.d("Clicker-> ", "Table is empty");
                //upgradeUser();
            } else {
                Log.d("Clicker-> ", "Table is not empty");
                //upgradeUser();///// TENER EN CUENTA ESTO PARA LA BUILD FINAL
            }
        });
    }

    //region Introoducir dato aa usuario
    //no hace falta callback
    //Introducir a UserStats
    public void insert(UserStats userStats) {
        executorService.execute(() -> {
            userStatsDAO.insert(userStats);
        });
    }
    //Introducir a UpgradesUser
    public void insert(UpgradesUser upgradesUser) {
        executorService.execute(() -> {
            upgradesUserDAO.insert(upgradesUser);
        });
    }
    //endregion

    // Obtener estadísticas del usuario por ID
    //coger los datos al inicializar

    public void getUserStats(String userId, BaseCallback<UserStats> callback) {
        executorService.execute(()->{
            callback.onSuccess(userStatsDAO.getUserStatsById(userId));
        });
    }
    //obtener mejoras y nivel del usuario
    public void getUserLevel(String idUpgrades, BaseCallback<String> callback) {
        executorService.execute(()-> {
            callback.onSuccess(upgradesUserDAO.getUserLevel(idUpgrades));

        });
    }

    //resetear  para mnueva partida
    public void resetUserStats() {
        executorService.execute(() -> {
            userStatsDAO.resetUserStats("User1");
        });
    }
    public void resetUserUpgrades() {
        executorService.execute(()->{
            upgradesUserDAO.resetUserUpgrades();
        });
    }

    //Guardar info de la partida
    public void updateUserStats(double score, double pcu, double acu) {
        executorService.execute(() -> {
            userStatsDAO.updateUserStats(score, pcu, acu, "User1");
        });
    }
    //actualizar las mejoras del usuario
    public void updateUserLevel(String idUpgrade, String level) {
        executorService.execute(() -> {
            upgradesUserDAO.updateUserLevel(idUpgrade, level);
        });
    }


    public void upgradeUser(){
        executorService.execute(() -> {

            //Datos del usuario
            UserStats userStats = new UserStats("User1", "User1", 0, 0, 1);
            userStatsDAO.insert(userStats);

            //Datos sobre las mejoras que tiene adquiridas el usuario.
            UpgradesUser upgradesUserActive1 = new UpgradesUser("upgradeuserActive_1", "ua_1", "0", "User1");
            upgradesUserDAO.insert(upgradesUserActive1);

            UpgradesUser upgradesUserActive2 = new UpgradesUser("upgradeuserActive_2", "ua_2", "0", "User1");
            upgradesUserDAO.insert(upgradesUserActive2);

            //UpgradesUser upgradesUserActive3 = new UpgradesUser("upgradeuserActive_3", "ua_3", "0", "User1");
            //upgradesUserDAO.insert(upgradesUserActive3);


            UpgradesUser upgradesUserPassive1 = new UpgradesUser("upgradeuserPassive_1", "up_1", "0", "User1");
            upgradesUserDAO.insert(upgradesUserPassive1);

            UpgradesUser upgradesUserPassive2 = new UpgradesUser("upgradeuserPassive_2", "up_2", "0", "User1");
            upgradesUserDAO.insert(upgradesUserPassive2);

            //UpgradesUser upgradesUserPassive3 = new UpgradesUser("upgradeuserPassive_3", "up_3", "0", "User1");
            //upgradesUserDAO.insert(upgradesUserPassive3);


        });
    }


}
