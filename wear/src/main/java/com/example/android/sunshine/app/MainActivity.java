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
import android.widget.ImageView;
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
    private TextView textViewHigh;
    private TextView textViewLow;
    private TextView mClockView;
    private TextView textViewDate;
    private ImageView imageIcon;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String high = intent.getStringExtra(HIGH);
            String low = intent.getStringExtra(LOW);
            int weatherId = intent.getIntExtra(WEATHER_ID, 0);
            updateWeatherInfo(high, low, weatherId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mClockView = (TextView) findViewById(R.id.clock);
        textViewHigh = (TextView) findViewById(R.id.textViewHigh);
        textViewLow = (TextView) findViewById(R.id.textViewLow);
        imageIcon = (ImageView) findViewById(R.id.detail_icon);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
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
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(broadcastReceiver);
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

    private void updateWeatherInfo(String high_temperature, String low_temperature, int ImageId) {
        textViewLow.setText(low_temperature);
        textViewHigh.setText(high_temperature);
        textViewDate.setText(Utility.getFullFriendlyDayString(this, System.currentTimeMillis()));
        imageIcon.setImageResource(Utility.getArtResourceForWeatherCondition(ImageId));
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
