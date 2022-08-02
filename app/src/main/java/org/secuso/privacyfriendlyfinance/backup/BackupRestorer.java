package org.secuso.privacyfriendlyfinance.backup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil;
import org.secuso.privacyfriendlybackup.api.backup.FileUtil;
import org.secuso.privacyfriendlybackup.api.pfa.IBackupRestorer;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.helpers.KeyStoreHelper;
import org.secuso.privacyfriendlyfinance.helpers.SharedPreferencesManager;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BackupRestorer implements IBackupRestorer {

    private static final String TAG = BackupRestorer.class.getName();

    private static final String RESTORE_DB_NAME = "restoreDatabase";

    private void readDatabase(Context context, JsonReader reader) throws Exception {
        reader.beginObject();

        assert reader.nextName().equals("version");
        int version = reader.nextInt();
        assert reader.nextName().equals("content");

        char[] databasePassword = KeyStoreHelper.getInstance(FinanceDatabase.KEY_ALIAS).getKey(context);
        SQLiteDatabase restoreDatabase = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(RESTORE_DB_NAME).getPath(), databasePassword, null, new SQLiteDatabaseHook() {
            @Override public void preKey(SQLiteDatabase database) {}
            @Override public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_compatibility = 3;");
            }
        });

        restoreDatabase.beginTransaction();
        restoreDatabase.setVersion(version);
        DatabaseUtil.readDatabaseContent(reader, restoreDatabase);
        restoreDatabase.setTransactionSuccessful();
        restoreDatabase.endTransaction();
        restoreDatabase.close();

        reader.endObject();

        File restoreDatabaseFile = context.getDatabasePath(RESTORE_DB_NAME);
        FileUtil.copyFile(restoreDatabaseFile, context.getDatabasePath(FinanceDatabase.DB_NAME));

        DatabaseUtil.deleteRoomDatabase(context, RESTORE_DB_NAME);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private void readPreferences(Context context, JsonReader reader) throws Exception {
        reader.beginObject();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesManager.PREF_NAME, Context.MODE_PRIVATE);

        while(reader.hasNext()) {
            String key = reader.nextName();

            switch (key) {
                case SharedPreferencesManager.KEY_IS_FIRST_TIME_LAUNCH:
                    sharedPreferences.edit().putBoolean(key, reader.nextBoolean()).commit();
                    break;
                default:
                    reader.skipValue();
                    Log.i(TAG, String.format("Unknown preference %s", key));
            }
        }

        reader.endObject();
    }

    @Override
    public boolean restoreBackup(@NonNull Context context, @NonNull InputStream restoreData) {
        if(FinanceDatabase.isInitialized()) {
            FinanceDatabase.getInstance(context).close();
        }

        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(restoreData));
            jsonReader.beginObject();

            while(jsonReader.hasNext()) {
                String type = jsonReader.nextName();
                switch (type) {
                    case "database":
                        readDatabase(context, jsonReader);
                        break;
                    case "preferences":
                        readPreferences(context, jsonReader);
                        break;
                }
            }

            System.exit(0);
            return true;
        } catch(Exception e) {
            Log.e(TAG, "Failed to restore backup", e);
            return false;
        }
    }
}
