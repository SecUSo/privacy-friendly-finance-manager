package org.secuso.privacyfriendlyfinance.domain;

import android.app.IntentService;
import android.content.Intent;

public class PeriodicDatabaseWorker extends IntentService {
    public PeriodicDatabaseWorker() {
        super("PeriodicDatabaseWorker");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("HANDLE PERIODIC TASK????");
    }
}
