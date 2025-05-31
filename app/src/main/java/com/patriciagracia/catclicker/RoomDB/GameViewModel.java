package com.patriciagracia.catclicker.RoomDB;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GameViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;
    public final MutableLiveData<UserStats> userStatsLiveData = new MutableLiveData<>();

    public GameViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);
    }

    public void getUserStats(String userId) {
        Log.d("Clicker->", "GameViewModel////getUserStats called for userId: " + userId);

        userRepository.getUserStats(userId, new BaseCallback<UserStats>(){
            @Override
            public void onSuccess(UserStats userStats) {
                Log.d("Clicker->", "GameViewModel///UserStats received: " +
                        "Passive: " + userStats.getPcuTotal() +
                        ", Active: " + userStats.getAcuTotal() +
                        ", Score: " + userStats.getTotalScore());
                userStatsLiveData.postValue(userStats);
            }

        });
    }

    //guardar los datos
    public void updateUserStats(double score, double pcu, double acu) {
        userRepository.updateUserStats(score, pcu, acu);
    }
    public void updateUserLevel(String idUpgrade, String level) {
        userRepository.updateUserLevel(idUpgrade, level);
    }

    // Resetear datos del usuario
    public void resetUserStats() {
        userRepository.resetUserStats();
        userRepository.resetUserUpgrades();

        // Después de resetear, recargar los datos del usuario
        getUserStats("User1");
    }
}
