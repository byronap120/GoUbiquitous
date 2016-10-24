package com.example.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    static public final String HIGH = "high_temperature";
    static public final String LOW = "low_temperature";
    static public final String WEATHER_ID = "weather_id";
    static public final String ACTION_FILTER = "weatherNewData";

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    private BoxInsetLayout mContainerView;
    private TextView mClockView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double high = intent.getDoubleExtra(HIGH, 0.0);
            Double low = intent.getDoubleExtra(LOW, 0.0);
            int weatherId = intent.getIntExtra(WEATHER_ID, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mClockView = (TextView) findViewById(R.id.clock);
        updateDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver, new IntentFilter(ACTION_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        if (isAmbient()) {
            mContainerView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        } else {
            mContainerView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));
        }
    }
}
