package org.secuso.privacyfriendlyfinance.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.List;

public abstract class TransactionListActivity extends BaseActivity implements TaskListener {
    protected TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();
    private List<Transaction> transactions;
    private ListView listViewTransactionList;
    private TextView tvBalance;
    private FloatingActionButton btAddTransaction;

    protected abstract void getTransactionListAsync();
    protected abstract String getTransactionListTitle();
    protected abstract String getTotalBalanceText();
    protected abstract long getPreselectedAccountId();
    protected abstract long getPreselectedCategoryId();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        overridePendingTransition(0, 0);

        /*
        Get all needed elements from the layout
         */
        btAddTransaction = findViewById(R.id.bt_addTransaction);
        listViewTransactionList = findViewById(R.id.listView_transactionList);
        tvBalance = findViewById(R.id.tv_totalBalance);


        /*
        Set the elements up
         */
        {
            setTitle(getTransactionListTitle());

            //Plus Button opens Dialog to add a new Transaction
            btAddTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openTransactionDialog(null);
                }
            });

            tvBalance.setText(getTotalBalanceText());

            // Open transaction edit dialog on click on list item
            listViewTransactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openTransactionDialog(transactions.get(position));
                }
            });
            registerForContextMenu(listViewTransactionList);
        }

    }

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_click_menu, menu);
    }

    @Override
    protected final void onResume() {
        super.onResume();
        getTransactionListAsync();
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

                        Toast.makeText(TransactionListActivity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();

                        Intent main = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(main);
                    }
                })
                .setNegativeButton(R.string.transaction_delete_dialog_negative, null);

        AlertDialog alert = builder.create();
        alert.show();
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

    @Override
    public final void onDone(Object result, AsyncTask<?, ?, ?> task) {
        transactions = (List<Transaction>) result;

        //TODO: remove debug code
        if (transactions.size() == 0) {
            transactions.add(new Transaction("trans1", 100, new LocalDate(42_000_000_000L), 0L));
            transactions.add(new Transaction("trans2", -500, new LocalDate(420_000_000_000L), 0L));
            transactions.add(new Transaction("trans3", 42000, new LocalDate(840_000_000_000L), 0L));
        }

        listViewTransactionList.setAdapter(new TransactionArrayAdapter(this, transactions));
    }
}
