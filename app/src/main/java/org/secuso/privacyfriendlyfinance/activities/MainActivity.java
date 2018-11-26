/*
 This file is part of Privacy Friendly App Finance Manager.

 Privacy Friendly App Finance Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Finance Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Finance Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlyfinance.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.TransactionArrayAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;


/**
 * This Activity handles the navigation and operation on transactions.
 *
 * @author David Meiborg
 * @version 2018
 */
public class MainActivity extends BaseActivity implements TaskListener {
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();
    private ListView transactionListView;
    private List<Transaction> transactions;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        transactionDao.getAllAsync(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(0, 0);

        //Plus Button opens Dialog to add new Transaction
        FloatingActionButton btAddTransaction = findViewById(R.id.add_expense);
        btAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTransactionDialog(null);
            }
        });

        transactionListView = (ListView) findViewById(R.id.transactionList);
        TextView balanceView = (TextView) findViewById(R.id.totalBalance);
        //TODO: Retrieve balance and put in balanceView, also set color
        balanceView.setText("TODO: Retrieve balance");

        //Edit Transaction if click on item
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openTransactionDialog(transactions.get(position));
            }
        });

        //Menu for listview items
        registerForContextMenu(transactionListView);
    }

    /**
     * Opens a new transaction dialog. If the given transaction object is not null
     * the dialog is opened as a edit dialog for the object.
     *
     * @param transactionObject the transaction object to be edited or null in order to open a creation dialog
     */
    public void openTransactionDialog(Transaction transactionObject) {
        TransactionDialog transactionDialog = new TransactionDialog();
        Bundle args = new Bundle();

        if (transactionObject != null) {
            args.putLong("transactionId", transactionObject.getId());
            args.putLong("transactionAmount", transactionObject.getAmount());
            args.putString("transactionName", transactionObject.getName());

            DateTimeFormatter formatter = DateTimeFormat.forPattern(getResources().getString(R.string.time_format_string));
            args.putString("transactionDate", formatter.print(transactionObject.getDate()));

            args.putLong("categoryId", transactionObject.getCategoryId());
        }

        transactionDialog.setArguments(args);
        transactionDialog.show(getSupportFragmentManager(), "TransactionDialog");
    }

    //opens menu for delete or edit list items
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_click_menu, menu);
    }

    //action when menu item is selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.listDeleteItem:
                deleteItem(info.position);
                break;
            case R.id.listEditItem:
                editItem(info.position);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void editItem(int indexToEdit) {
        openTransactionDialog(transactions.get(indexToEdit));
    }

    private void deleteItem(final int indexToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.transaction_delete_dialog_title)
                .setPositiveButton(R.string.transaction_delete_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transactionDao.deleteAsync(transactions.get(indexToDelete));

                        Toast.makeText(MainActivity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();

                        Intent main = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(main);
                    }
                })
                .setNegativeButton(R.string.transaction_delete_dialog_negative, null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * This method connects the Activity to the menu item
     *
     * @return ID of the menu item it belongs to
     */
    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_main;
    }

    @Override
    public void onDone(Object result, AsyncTask<?, ?, ?> task) {
        //TODO: uncomment
//        transactions = (List<Transaction>) result;

        //TODO: remove debug code
        transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction("Transaction1", 101, new LocalDate(42_000L), 0));
        transactions.add(new Transaction("Transaction2", 42, new LocalDate(420_000L), 0));

        transactionListView.setAdapter(new TransactionArrayAdapter(this, transactions));
    }

    @Override
    public void onProgress(Double progress, AsyncTask<?, ?, ?> task) {
    }

    @Override
    public void onOperation(String operation, AsyncTask<?, ?, ?> task) {
    }
}
