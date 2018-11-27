package org.secuso.privacyfriendlyfinance.activities.helper;

import android.os.AsyncTask;

public interface FullTaskListener extends TaskListener {
    void onProgress(Double progress, AsyncTask<?, ?, ?> task);
    void onOperation(String operation, AsyncTask<?, ?, ?> task);
}
