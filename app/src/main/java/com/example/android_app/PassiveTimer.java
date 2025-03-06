package com.example.android_app;

import java.util.Timer;
import java.util.TimerTask;

public class PassiveTimer {
    private Timer timer;
    private ScoreManager scoreManager;

    public PassiveTimer(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        this.timer = new Timer();
    }

    public void start() {
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scoreManager.ClickPassive();
            }
        }, 0, 1000);
    }
}
