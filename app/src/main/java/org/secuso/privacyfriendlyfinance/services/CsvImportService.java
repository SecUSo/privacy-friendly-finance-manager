/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2023 MaxIsV, k3b

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

package org.secuso.privacyfriendlyfinance.services;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.TransactionsActivity;
import org.secuso.privacyfriendlyfinance.csv.Account2Id;
import org.secuso.privacyfriendlyfinance.csv.Category2Id;
import org.secuso.privacyfriendlyfinance.csv.CsvImporter;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CsvImportService extends IntentService {
    private ResultReceiver resultReceiver;

    public CsvImportService() {
        super(CsvImportService.class.getSimpleName());
    }
    private static final String TAG = CsvImportService.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("Service", "started");
        if (intent != null) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM); // used by send
            resultReceiver = intent.getParcelableExtra(Intent.EXTRA_RESULT_RECEIVER);
            if (uri == null) {
                uri = intent.getData(); // used by sendTo and view
            }

            if (uri != null) {
                final Uri finalUri = uri;
                final FinanceDatabase database = FinanceDatabase.getInstance(getApplication());

                doImportCsv(finalUri, database);
            }
        }
    }

    private void doImportCsv(Uri uri, FinanceDatabase database) {
        InputStream in = null;
        String messages = "";
        try {
            in = getContentResolver().openInputStream(uri);

            messages = importCsv(in, database);
        } catch (Exception ex) {
            Log.w(TAG,"Error in onCreate-startActivityForResult " + uri, ex);
            messages += getString(R.string.err_cannot_read_import_file, uri.toString(), ex.getMessage());
        } finally {
            final String finalMessages = messages;
            Bundle bundle = new Bundle();
            bundle.putString(TransactionsActivity.CSV_RESULT_MESSAGE, finalMessages);
            resultReceiver.send(1, bundle);
            Log.e("Service", "finished");
            close(in, uri);
        }
    }

    private String importCsv(InputStream in, FinanceDatabase database) throws Exception {
        CsvImporter importer = new CsvImporter(
                new InputStreamReader(in),
                new Account2Id(database.accountDao()),
                new Category2Id(database.categoryDao()));
            List<Transaction> transactions = importer.readFromCsv();

            StringBuilder messageLines = new StringBuilder(importer.getErrors());

            TransactionDao dao = database.transactionDao();
            int countImported = 0;
            for (Transaction t : transactions) {

                try {
                    if (t.getAmount() != 0 && dao.findSameTransaction(t).isEmpty()) {
                        // Do not insert duplicate entries or entries with no amount
                        dao.insert(t);
                        countImported ++;
                    }
                } catch (Exception ex) {
                   messageLines.append(getString(R.string.err_cannot_save_to_db,
                           t.toString(),ex.getMessage()));
                }
            }
            messageLines.append(getString(R.string.info_import_success,
                ""+ countImported,"" + transactions.size()));
            return messageLines.toString();
    }

    public static void close(Closeable stream, Object source) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.w(TAG, "Error close " + source, e);
            }
        }
    }
}