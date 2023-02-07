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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return TransactionsViewModel.class;
    }

    private static final String TAG = "mytag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
