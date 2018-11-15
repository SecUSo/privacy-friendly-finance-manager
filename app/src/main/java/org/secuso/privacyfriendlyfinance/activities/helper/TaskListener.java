package org.secuso.privacyfriendlyfinance.activities.helper;

import android.os.AsyncTask;

public interface TaskListener {
    void onDone(Object result, AsyncTask<?, ?, ?> task);
    void onProgress(Double progress, AsyncTask<?, ?, ?> task);
    void onOperation(String operation, AsyncTask<?, ?, ?> task);
}
