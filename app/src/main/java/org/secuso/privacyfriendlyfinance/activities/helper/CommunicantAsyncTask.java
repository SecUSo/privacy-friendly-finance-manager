/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.activities.helper;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Task that runs asynchronously.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
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
            if (listener instanceof FullTaskListener) {
                ((FullTaskListener) listener).onProgress(value, this);
            }
        }
    }

    protected void publishOperation(String operation) {
        for (TaskListener listener : listeners) {
            if (listener instanceof FullTaskListener) {
                ((FullTaskListener) listener).onOperation(operation, this);
            }
        }
    }

    public List<TaskListener> getListeners() {
        return new ArrayList<>(listeners);
    }

    public void addListener(TaskListener listener) {
        if (!listeners.contains(listener)) {
            if (getStatus() == Status.FINISHED) {
                if (listener instanceof FullTaskListener) {
                    ((FullTaskListener) listener).onProgress(progress, this);
                }
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
