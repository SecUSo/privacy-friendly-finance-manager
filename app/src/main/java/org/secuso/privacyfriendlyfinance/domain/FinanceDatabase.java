package org.secuso.privacyfriendlyfinance.domain;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.concurrent.TimeUnit;

@Database(entities = {Account.class, Category.class, Transaction.class}, version = 3)
public abstract class FinanceDatabase extends RoomDatabase {
    private static final String DB_NAME = "db";
    private static OpenDatabaseTask openTask;
    private static FinanceDatabase instance;

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();

    public static FinanceDatabase getInstance() {
        return instance;
    }

    public static CommunicantAsyncTask<?, FinanceDatabase> connect(Context context, TaskListener listener) {
        if (openTask == null) {
            openTask = new OpenDatabaseTask(context);
            if (listener != null) openTask.addListener(listener);
            openTask.execute(DB_NAME);
        }
        return openTask;
    }

    private static class OpenDatabaseTask extends CommunicantAsyncTask<String, FinanceDatabase> {
        private Context context;

        public OpenDatabaseTask(Context context) {
            this.context = context;
        }

        @Override
        protected FinanceDatabase doInBackground(String... params) {
            publishProgress(0.0);
            if (params.length != 1) throw new IllegalArgumentException("one database name was expected");
            for (int i = 1; i <= 1000; i += 16) {
                publishProgress(((double) i) / 1000.0);
                try {
                    TimeUnit.MILLISECONDS.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return Room.databaseBuilder(context, FinanceDatabase.class, params[0])
                    .fallbackToDestructiveMigration()
                    .build();
        }

        @Override
        protected void onPostExecute(FinanceDatabase result) {
            FinanceDatabase.instance = result;
            super.onPostExecute(result);
        }
    }
}
