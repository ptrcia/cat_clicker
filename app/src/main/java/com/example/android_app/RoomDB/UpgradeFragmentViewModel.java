package com.example.android_app;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.app.Application;

import com.example.android_app.RoomDB.ClickUpgrade;
import com.example.android_app.RoomDB.UpgradesRepository;

import java.util.List;

public class UpgradeFragmentViewModel extends AndroidViewModel {
    private UpgradesRepository upgradesRepository;

    // Constructor
    public UpgradeFragmentViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
    }

    // insetar click en el repositorio
    public void insertClickUpgrade(ClickUpgrade clickUpgrade) {
        upgradesRepository.insert(clickUpgrade);
    }

    // objener las mejoras
    public LiveData<List<ClickUpgrade>> getAllUpgrades() {
        return upgradesRepository.getAllUpgrades();

    }
}

