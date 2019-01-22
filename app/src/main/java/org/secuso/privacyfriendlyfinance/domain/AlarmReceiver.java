package org.secuso.privacyfriendlyfinance.domain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 2340585;
    public static final String ACTION = "org.secuso.privacyfriendlyfinance.domain.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent periodicDatabaseIntent = new Intent(context, PeriodicDatabaseWorker.class);
        context.startService(periodicDatabaseIntent);
    }
}
