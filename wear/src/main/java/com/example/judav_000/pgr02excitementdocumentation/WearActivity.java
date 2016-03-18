package com.example.judav_000.pgr02excitementdocumentation;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/* Main activity class for wearable. Detects excitement in user by looking for changes in acceleration. */
public class WearActivity extends Activity {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        SensorEventListener mSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                // Not sure what a proper threshold is. Hopefully this is reasonable.
                if (x > 10 || y > 10 || z > 5) {
                    // Intent to tell mobile that user is excited.
                    Intent intent = new Intent(WearActivity.this, SendServiceToMobile.class);
                    // Detects if the user is super excited.
                    if (x > 50 || y > 50 || z > 25) {
                        intent.putExtra("excitement level" , "excite_level_two");
                    } else {
                        intent.putExtra("excitement level", "excite_level_one");
                    }

                    startService(intent);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // No need to detect accuracy.
            }
        };

        // Initialize the sensor.
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}

