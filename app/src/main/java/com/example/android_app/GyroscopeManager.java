package com.example.android_app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class GyroscopeManager implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gyroSensor;

    public GyroscopeManager(Context applicationContext) {
        this.sensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
        this.gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (this.gyroSensor == null) {
            Log.e("GyroscopeManager", "El dispositivo no tiene un sensor de giroscopio.");
        }
    }
    public void startListening(){
        if(gyroSensor != null){
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
    public void stopListening(){
        sensorManager.unregisterListener(this);
    }

  @Override
  public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            float x = event.values[0];
            float y = event.values[1];

           if (Game.getInstance() != null) {

               Game.getInstance().GyroPosition(x, y);
            } else {
                Log.e("GyroscopeManager", "Game instance is null!");
            }
        }
  }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Puedes manejar cambios de precisión si es necesario
    }
}
