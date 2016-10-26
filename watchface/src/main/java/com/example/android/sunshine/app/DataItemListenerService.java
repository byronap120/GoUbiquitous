package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class DataItemListenerService extends WearableListenerService {

    public final String LOG_TAG = DataItemListenerService.class.getSimpleName();
    static private final String WEAR_PATH = "/updateWear";
    static public final String HIGH = "high_temperature";
    static public final String LOW = "low_temperature";
    static public final String WEATHER_ID = "weather_id";
    static public final String ACTION_FILTER = "weatherNewData";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.i(LOG_TAG, "onDataChanged");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(WEAR_PATH) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    getDataItemsAndSendLocalBroadcast(dataMap);
                }
            }
        }
    }

    private void getDataItemsAndSendLocalBroadcast(DataMap dataMap) {
        Intent in = new Intent();
        in.putExtra(HIGH, dataMap.getString(HIGH));
        in.putExtra(LOW, dataMap.getString(LOW));
        in.putExtra(WEATHER_ID, dataMap.getInt(WEATHER_ID));
        in.setAction(ACTION_FILTER);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }
}