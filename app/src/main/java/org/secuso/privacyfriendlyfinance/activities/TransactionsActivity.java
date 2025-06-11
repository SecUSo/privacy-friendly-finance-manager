/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019-2023 Leonard Otto, Felix Hofmann, MaxIsV, k3b

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
package org.secuso.privacyfriendlyfinance.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.secuso.privacyfriendlyfinance.GoodbyeGoogleHelperKt;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.FileHelper;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionsViewModel;
import org.secuso.privacyfriendlyfinance.csv.CsvExporter;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Id2Name;
import org.secuso.privacyfriendlyfinance.services.CsvImportService;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Transactions activity that show ALL transactions in the system. Is the main activity of this app.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class TransactionsActivity extends TransactionListActivity {
    private static final int PICK_CSV_INTENT = 0;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST = 1;
    public final static String CSV_RESULT_MESSAGE = "CSV_RESULT_MESSAGE";

    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return TransactionsViewModel.class;
    }

    private static final String TAG = "mytag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.e("intent", (intent != null) + "");
        if (intent != null) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM); // used by send
            if (uri == null) {
                uri = intent.getData();
            }
            if (uri != null) {
                Log.e("uri", uri.toString());
                importCsv(uri);
            }
        }
        GoodbyeGoogleHelperKt.checkGoodbyeGoogle(this, getLayoutInflater());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.transactions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exportButton:
                return onExportCsv();
            case R.id.importCSV:
                openFilePicker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean onExportCsv() {
        File file = FileHelper.getCsvFile(this,"Transactions-Export.csv");
        final List<Transaction> transactionList = this.viewModel.getTransactions().getValue();
        new Thread(() -> doExportAsync(transactionList, file)).start();
        return true;
    }

    private void doExportAsync(List<Transaction> transactionList, File file) {
        CsvExporter exporter = null;

        Id2Name<Category> id2Category = new Id2Name<>(FinanceDatabase.getInstance(getApplication()).categoryDao().getAllSynchron());
        Id2Name<Account> id2Account = new Id2Name<>(FinanceDatabase.getInstance(getApplication()).accountDao().getAllSynchron());
        try {
            exporter = new CsvExporter(new FileWriter(file), id2Category, id2Account);
            exporter.writeTransactions(transactionList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                exporter.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        runOnUiThread(() -> {
            FileHelper.sendCsv(this, getString(R.string.nav_title_export_as_csv), file);
        });
    }
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE_REQUEST);
            } else {
                startActivityForResult(intent, PICK_CSV_INTENT);
            }
        } else {
            startActivityForResult(intent, PICK_CSV_INTENT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CSV_INTENT && resultCode == RESULT_OK && data.getData() != null) {
            importCsv(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void importCsv(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent csvImport = new Intent(this, CsvImportService.class);
            ImportCsvResultReceiver resultReceiver = new ImportCsvResultReceiver(new Handler());
            csvImport.putExtra(Intent.EXTRA_RESULT_RECEIVER, resultReceiver);
            csvImport.putExtra(Intent.EXTRA_STREAM, uri);
            startService(csvImport);
        }
    }

    private class ImportCsvResultReceiver extends ResultReceiver {
        public ImportCsvResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String message = resultData.getString(CSV_RESULT_MESSAGE);
            Log.e("Receiver", message);
            if (message != null) {
                Toast.makeText(TransactionsActivity.this, message, Toast.LENGTH_LONG).show();
            }
            super.onReceiveResult(resultCode, resultData);
        }
    }
}
