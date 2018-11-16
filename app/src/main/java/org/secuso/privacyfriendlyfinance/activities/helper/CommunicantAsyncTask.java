package org.secuso.privacyfriendlyfinance.activities.helper;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public abstract class CommunicantAsyncTask<Params, Result> extends AsyncTask<Params, Double, Result> {
    private List<TaskListener> listeners = new ArrayList<>();
    private Result result;
    private Double progress;
    private String operation;

      @Override
    protected void onPostExecute(Result result) {
        this.result = result;
        progress = 1.0;
        for (TaskListener listener : listeners) {
            listener.onDone(result, this);
        }
        listeners.clear();
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        Double value = null;
        if (values.length > 0 && values[0] != null) {
            value = values[0];
            if (value > 1.0) value = 1.0;
            if (value < 0.0) value = 0.0;
        }

        for (TaskListener listener : listeners) {
            listener.onProgress(value, this);
        }
    }

    protected void publishOperation(String operation) {
        for (TaskListener listener : listeners) {
            listener.onOperation(operation, this);
        }
    }

    public List<TaskListener> getListeners() {
        return new ArrayList<>(listeners);
    }

    public void addListener(TaskListener listener) {
        if (!listeners.contains(listener)) {
            if (getStatus() == Status.FINISHED) {
                listener.onProgress(progress, this);
                listener.onDone(result, this);
            } else {
                listeners.add(listener);
            }
        }
    }

    public boolean removeListener(TaskListener listener) {
        return listeners.remove(listener);
    }

    public Double getProgress() {
        return progress;
    }

    public String getOperation() {
        return operation;
    }
}
