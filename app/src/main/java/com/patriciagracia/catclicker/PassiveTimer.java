package com.patriciagracia.catclicker;

import java.util.Timer;
import java.util.TimerTask;

public class PassiveTimer {
    private Timer timer;
    private TimerTask timerTask;
    private ScoreManager scoreManager;
    boolean isRunning;
    private TimerTask timeCheckerTask; // Nuevo TimerTask
    private long startTime;
    private long lastCheckTime = 0;

    public PassiveTimer(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        this.isRunning = false;
        this.timer = new Timer();
    }

    public void start() {
        if (!isRunning) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    scoreManager.ClickPassive(null, Game.getInstance().applyCatBonus());
                }
            };
            timer.schedule(timerTask, 0, 1000);
            isRunning = true;
        }
    }
//Stop ara tener control
    public void stop() {
        if (isRunning) {
            timer.cancel();
            timer.purge();
            timerTask.cancel();
            isRunning = false;
        }
    }
    //guardar progreso cada x segundos
    public void startAutoSaveTimer(Runnable saveProgressAction) {
        startTime  = System.currentTimeMillis();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                saveProgressAction.run();
            }
        };
        timer.schedule(timerTask, 0, 20000);
    }

}
