package org.secuso.privacyfriendlyfinance.backup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonWriter;
import android.util.Log;

import androidx.annotation.NonNull;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil;
import org.secuso.privacyfriendlybackup.api.backup.PreferenceUtil;
import org.secuso.privacyfriendlybackup.api.pfa.IBackupCreator;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.helpers.KeyStoreHelper;
import org.secuso.privacyfriendlyfinance.helpers.SharedPreferencesManager;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class BackupCreator implements IBackupCreator {
    @Override
    public boolean writeBackup(@NonNull Context context, @NonNull OutputStream outputStream) {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.setIndent("");

        try {
            writer.beginObject();
            writer.name("database");

            SQLiteDatabase.loadLibs(context);

            if (context.getDatabasePath(FinanceDatabase.DB_NAME).exists()) {
                SQLiteDatabase database = SQLiteDatabase.openDatabase(context.getDatabasePath(FinanceDatabase.DB_NAME).getPath(), KeyStoreHelper.getInstance(FinanceDatabase.KEY_ALIAS).getKey(context), null, SQLiteDatabase.OPEN_READONLY, new SQLiteDatabaseHook() {
                    @Override
                    public void preKey(SQLiteDatabase database) {
                    }

                    @Override
                    public void postKey(SQLiteDatabase database) {
                        database.rawExecSQL("PRAGMA cipher_compatibility = 3;");
                    }
                });

                DatabaseUtil.writeDatabase(writer, database);
                database.close();
            } else {
                writer.beginObject();
                writer.endObject();
            }

            writer.name("preferences");
            SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesManager.PREF_NAME, SharedPreferencesManager.PREF_MODE);
            // Don't backup the db passphrase. Instead, we should always use the passphrase that's already used on-device.
            PreferenceUtil.writePreferences(writer, sharedPreferences, new String[]{SharedPreferencesManager.KEY_DB_PASSPHRASE});

            writer.endObject();
            writer.close();
        } catch (Exception e) {
            Log.e("PFA BackupCreator", "Error occurred", e);
        }
        return true;
    }
}
