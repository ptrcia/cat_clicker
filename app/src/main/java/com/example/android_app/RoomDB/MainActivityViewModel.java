package com.example.android_app.RoomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;

    public MainActivityViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);
    }


    //resetar al clicar on start y darle a si al diálogo
    public void resetUserStats() {
        userRepository.resetUserStats();
    }
    public void resetUserUpgrades(){
        userRepository.resetUserUpgrades();
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
    public void getUpgradesUser() {
        upgradesRepository.getAllUpgrades(new BaseCallback<List<UpgradesUser>>(){
            @Override
            public void onSuccess(List<UpgradesUser> upgradesUsers) {
                //upgradesUserLiveData.postValue(upgradesUsers);
            }
        });
    }
}
