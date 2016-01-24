package com.example.android.sunshine.app;

import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class SunshineWearableListenerService extends WearableListenerService{

    public interface DataReceivedListener {
        public void handleDataReceived(DataMap dataMap);
    }

    public static DataReceivedListener listener;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        for(DataEvent e: dataEvents){
            if(e.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(e.getDataItem()).getDataMap();
                if(listener != null){
                    listener.handleDataReceived(dataMap);
                }
            }
        }
    }
}
