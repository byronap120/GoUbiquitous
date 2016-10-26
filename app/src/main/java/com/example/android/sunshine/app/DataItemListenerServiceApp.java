package com.example.android.sunshine.app;

import com.example.android.sunshine.app.sync.SunshineSyncAdapter;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.WearableListenerService;

public class DataItemListenerServiceApp extends WearableListenerService {

    public final String LOG_TAG = DataItemListenerServiceApp.class.getSimpleName();
    static private final String WEAR_PATH = "/wakeMessage";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(WEAR_PATH) == 0) {
                    getDataItemsAndSendLocalBroadcast();
                }
            }
        }
    }

    private void getDataItemsAndSendLocalBroadcast() {
        SunshineSyncAdapter.syncImmediately(this);
    }
}