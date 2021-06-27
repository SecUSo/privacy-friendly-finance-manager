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

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.commonsware.cwac.saferoom.SafeHelperFactory;

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
    private static final String DB_NAME = "encryptedDB";
    public static final String KEY_ALIAS = "financeDatabaseKey";
    private static InitDatabaseTask initTask;
    private static FinanceDatabase instance;

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();
    public abstract AccountDao accountDao();
    public abstract RepeatingTransactionDao repeatingTransactionDao();

    public static FinanceDatabase getInstance() {
        return instance;
    }

    public static CommunicantAsyncTask<?, FinanceDatabase> connect(Context context, TaskListener listener) {
        if (initTask == null) {
            initTask = new InitDatabaseTask(context, DB_NAME);
            if (listener != null) initTask.addListener(listener);
            initTask.execute();
        }
        return initTask;
    }


    private static class InitDatabaseTask extends CommunicantAsyncTask<Void, FinanceDatabase> {
        private Context context;
        private String dbName;

        public InitDatabaseTask(Context context, String dbName) {
            this.context = context;
            this.dbName = dbName;
        }

        private void deleteDatabaseFile() {
            File databaseDir = new File(context.getApplicationInfo().dataDir + "/databases");
            File[] files = databaseDir.listFiles();
            if(files != null) {
                for(File f: files) {
                    if(!f.isDirectory() && f.getName().startsWith(DB_NAME)) {
                        f.delete();
                    }
                }
            }
        }

        private boolean dbFileExists() {
            File databaseFile = new File(context.getApplicationInfo().dataDir + "/databases/" + DB_NAME);
            return databaseFile.exists() && databaseFile.isFile();
        }

        @Override
        protected FinanceDatabase doInBackground(Void... voids) {
            try {

//                publishProgress(0.0);
//                publishOperation(context.getResources().getString(R.string.activity_startup_init_key_store_msg));
//
//                KeyStoreHelper keystore = new KeyStoreHelper(KEY_ALIAS);
//                String passphrase = SharedPreferencesManager.getDbPassphrase();
//
//                if (!keystore.keyExists()) {
//                    keystore.generateKey(context);
//                    if (passphrase != null) {
//                        Log.w("OpenDatabase", "database passphrase could not be recovered");
//                        SharedPreferencesManager.removeDbPassphrase();
//                    }
//                }
//
//                publishProgress(.2);
//
//                if (passphrase == null) {
//                    publishOperation(context.getResources().getString(R.string.activity_startup_create_passphrase_msg));
//                    passphrase = keystore.createPassphrase();
//                    SharedPreferencesManager.setDbPassphrase(passphrase);
//                }
//
//                publishProgress(.4);
//                publishOperation(context.getResources().getString(R.string.activity_startup_decrypt_phassphrase_msg));
//                byte[] decryptedPassphrase = keystore.rsaDecrypt(Base64.decode(passphrase, Base64.DEFAULT));
//
//                char[] charPassphrase = new char[decryptedPassphrase.length];
//                for (int i = 0; i < decryptedPassphrase.length; ++i) {
//                    charPassphrase[i] = (char) (decryptedPassphrase[i] & 0xFF);
//                }





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
                if (dbFileExists()) {
                } else {
                    publishOperation(context.getResources().getString(R.string.activity_startup_create_and_open_database_msg));
                }
                FinanceDatabase.instance = Room.databaseBuilder(context, FinanceDatabase.class, dbName)
                        .openHelperFactory(new SafeHelperFactory(key))
                        .fallbackToDestructiveMigration()
                        .build();

                if (FinanceDatabase.instance.accountDao().count() == 0) {
                    Account defaultAccount = new Account(context.getResources().getString(R.string.activity_startup_default_account_name));
                    defaultAccount.setId(0L);
                    FinanceDatabase.instance.accountDao().insert(defaultAccount);
                }

                if (MigrationFromUnencrypted.legacyDatabaseExists(context)) {
                    publishProgress(.8);
                    publishOperation(context.getResources().getString(R.string.activity_startup_migrate_database_msg));
                    MigrationFromUnencrypted.migrateTo(FinanceDatabase.instance, context);
                    MigrationFromUnencrypted.deleteLegacyDatabase(context);
                }
                return FinanceDatabase.instance;
            } catch (KeyStoreHelperException ex) {
//                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                alertDialog.setTitle("Database error!");
//                alertDialog.setMessage("Error creating database: " + ex.getMessage());
////                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
////                        new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int which) {
////                                dialog.dismiss();
////                            }
////                        });
//                alertDialog.show();

                throw new RuntimeException(ex);
            }
        }
    }
}
