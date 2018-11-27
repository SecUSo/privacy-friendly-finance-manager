package org.secuso.privacyfriendlyfinance.activities.helper;

import android.os.AsyncTask;

public interface TaskListener {
    void onDone(Object result, AsyncTask<?, ?, ?> task);
}
