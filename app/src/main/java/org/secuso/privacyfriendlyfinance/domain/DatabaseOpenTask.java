package org.secuso.privacyfriendlyfinance.domain;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

public class DatabaseOpenTask extends AsyncTask<String, Long, FinanceDatabase> {
    private Context context;

    public DatabaseOpenTask(Context context) {
        this.context = context;
    }

    @Override
    protected FinanceDatabase doInBackground(String... params) {
        if (params.length != 2) throw new AssertionError("database name was expected");
        return Room.databaseBuilder(context, FinanceDatabase.class, params[0]).build();
    }
}
