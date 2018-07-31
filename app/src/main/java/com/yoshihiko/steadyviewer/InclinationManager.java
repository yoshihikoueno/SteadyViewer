package com.yoshihiko.steadyviewer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;

/**
 * Created by yoshihiko on 9/1/17.
 */

public class InclinationManager implements SensorEventListener{
    private float inclination;
    private SensorManager mSensorManager;
    private Sensor acc;
    private Content ContentView;
    private InclinationManager This;

    public InclinationManager(SensorManager sm,Content content) {
        mSensorManager= sm;
        acc=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        inclination=0;
        ContentView=content;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] gravity=event.values;
        inclination = (float) Math.atan2(gravity[0],gravity[1]);
        ContentView.RotateContent(inclination);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void resume(){
        This=this;
        new Thread(){
            @Override
            public void run() {
                while (!ContentView.isReady()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}
                }
                mSensorManager.registerListener(This, acc, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }.start();
    }

    public void pause(){
        mSensorManager.unregisterListener(this,acc);
    }

    public float getInclination(){
        return inclination;
    }
}
