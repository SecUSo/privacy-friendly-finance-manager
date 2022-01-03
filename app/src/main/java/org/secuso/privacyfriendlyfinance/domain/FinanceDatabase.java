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

package org.secuso.privacyfriendlyfinance.domain;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SupportFactory;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.RepeatingTransactionDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.convert.LocalDateConverter;
import org.secuso.privacyfriendlyfinance.domain.legacy.MigrationFromUnencrypted;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.helpers.KeyStoreHelper;
import org.secuso.privacyfriendlyfinance.helpers.KeyStoreHelperException;

import java.io.File;

/**
 * The main database class. Also handles the initialization of the database.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
@Database(
    entities = {Account.class, Category.class, Transaction.class, RepeatingTransaction.class},
    exportSchema = false,
    version = 10
)
@TypeConverters({LocalDateConverter.class})
public abstract class FinanceDatabase extends RoomDatabase {

    private static final String TAG = FinanceDatabase.class.getName();

    private static FinanceDatabase financeDatabase;

    public static final String DB_NAME = "encryptedDB";
    public static final String KEY_ALIAS = "financeDatabaseKey";

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();
    public abstract AccountDao accountDao();
    public abstract RepeatingTransactionDao repeatingTransactionDao();

    @Nullable
    public static FinanceDatabase getInstance() {
        return financeDatabase;
    }

    public synchronized static void connect(Context context, TaskListener listener) {
        if(financeDatabase != null && financeDatabase.isOpen()) {
            throw new RuntimeException("Cannot reinitialize database once it is initialized and active");
        }

        InitDatabaseTask task = new InitDatabaseTask(context);
        if (listener != null) {
            task.addListener(listener);
        }
        task.execute();
    }

    private static class InitDatabaseTask extends CommunicantAsyncTask<Void, Void> {

        @SuppressLint("StaticFieldLeak")
        private final Context context;

        public InitDatabaseTask(Context context) {
            this.context = context;
        }

        private void deleteDatabaseFile() {
            File databaseDir = new File(context.getApplicationInfo().dataDir + "/databases");
            File[] files = databaseDir.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(!file.isDirectory() && file.getName().startsWith(DB_NAME)) {
                        file.delete();
                    }
                }
            }
        }

        private boolean databaseFileExists() {
            File databaseFile = new File(context.getApplicationInfo().dataDir + "/databases/" + DB_NAME);
            return databaseFile.exists() && databaseFile.isFile();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                publishProgress(0.0);
                publishOperation(context.getResources().getString(R.string.activity_startup_init_key_store_msg));

                KeyStoreHelper keystore = KeyStoreHelper.getInstance(KEY_ALIAS);

                if (!keystore.keyExists()) {
                    deleteDatabaseFile();
                    publishOperation(context.getResources().getString(R.string.activity_startup_open_database_msg));
                }

                char[] key = keystore.getKey(context);
                if (key == null) {
                    throw new RuntimeException("Key could not be retrieved!");
                }

                publishProgress(.6);

                if (!databaseFileExists()) {
                    publishOperation(context.getResources().getString(R.string.activity_startup_create_and_open_database_msg));
                }

                financeDatabase = Room.databaseBuilder(context, FinanceDatabase.class, DB_NAME)
                        .openHelperFactory(new SupportFactory(SQLiteDatabase.getBytes(key), new SQLiteDatabaseHook() {
                            @Override public void preKey(SQLiteDatabase database) {}
                            @Override public void postKey(SQLiteDatabase database) {
                                database.rawExecSQL("PRAGMA cipher_compatibility = 3;");
                            }
                        }))
                        .fallbackToDestructiveMigration()
                        .build();

                if (financeDatabase.accountDao().count() == 0) {
                    Account defaultAccount = new Account(context.getResources().getString(R.string.activity_startup_default_account_name));
                    defaultAccount.setId(0L);
                    financeDatabase.accountDao().insert(defaultAccount);
                }

                if (MigrationFromUnencrypted.legacyDatabaseExists(context)) {
                    publishProgress(.8);
                    publishOperation(context.getResources().getString(R.string.activity_startup_migrate_database_msg));
                    MigrationFromUnencrypted.migrateTo(financeDatabase, context);
                    MigrationFromUnencrypted.deleteLegacyDatabase(context);
                }
            } catch (KeyStoreHelperException e) {
                throw new RuntimeException(e);
            }

            return null;
        }
    }
}
