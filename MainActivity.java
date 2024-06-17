package com.devsquad.thegame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView redSquare;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float ALPHA = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redSquare = findViewById(R.id.red_square);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.status_bar_color));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];

            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;

            float newX = redSquare.getX() - x * 10;
            float newY = redSquare.getY() + y * 10;

            if (newX < 0) newX = 0;
            else if (newX + redSquare.getWidth() > screenWidth) newX = screenWidth - redSquare.getWidth();

            if (newY < 0) newY = 0;
            else if (newY + redSquare.getHeight() > screenHeight) newY = screenHeight - redSquare.getHeight();

            float currentX = redSquare.getX();
            float currentY = redSquare.getY();

            newX = currentX + ALPHA * (newX - currentX);
            newY = currentY + ALPHA * (newY - currentY);

            redSquare.setX(newX);
            redSquare.setY(newY);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}