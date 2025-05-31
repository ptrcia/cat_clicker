package com.patriciagracia.catclicker.RoomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;


public class MainActivityViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;

    public MainActivityViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);

    }
   // public final MutableLiveData<UserStats> userStatsLiveData = new MutableLiveData<>();

/*

    //resetar al clicar on start y darle a si al diálogo
    public void resetUserStats() {
        userRepository.resetUserStats();
    }
    public void resetUserUpgrades(){
        userRepository.resetUserUpgrades();
    }
*/

    // Resetear datos del usuario
    public void resetUserStats() {
        userRepository.resetUserStats();
        userRepository.resetUserUpgrades();

        // Después de resetear, recargar los datos del usuario
        getUserStats("User1");
    }

    //cargar datos
    public void getUserStats(String userId) {
        userRepository.getUserStats(userId, new BaseCallback<UserStats>(){
            @Override
            public void onSuccess(UserStats userStats) {
                //userStatsLiveData.postValue(userStats);
            }

        });
    }

}
