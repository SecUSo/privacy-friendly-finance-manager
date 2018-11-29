package org.secuso.privacyfriendlyfinance.activities;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

/**
 * This abstract class is provided as a base class for all
 * activities that show a list of transactions. Classes that use
 * this class a super class are: MainActivity, AccountActivity,
 * CategoryActivity...
 *
 * @author Leonard Otto, Felix Hofmann
 */
public abstract class TransactionListActivity extends BaseActivity {
    private ListView listViewTransactionList;
    private TextView tvBalance;
    private TextView tvBalanceLabel;
    private View separator;
    private FloatingActionButton btAddTransaction;

    /**
     * This method should return the title of this activity. It is
     * the title that is shown on the very top of the screen.
     *
     * @return the title of this activity
     */
    protected abstract String getTransactionListTitle();

    /**
     * This method should either return the total balance of this
     * view or null. If a string is returned that string will then be
     * displayed on top of the transaction list. If null is returned
     * the balance line will be removed and the only thing shown in the
     * activity will be the transaction list.
     *
     * @return the total balance text as a string or null
     */
    protected abstract String getTotalBalanceText();

    /**
     * This method should return either -1 or the id of the account
     * that should be preselected when the transaction creation dialog
     * is opened. When -1 is returned no specific account will be
     * preselected.
     *
     * @return the account id to be preselected or -1
     */
    protected long getPreselectedAccountId() {
        return -1L;
    }

    /**
     * This method should return either -1 or the id of the category
     * that should be preselected when the transaction creation dialog
     * is opened. When -1 is returned no specific category will be
     * preselected.
     *
     * @return the category id to be preselected of -1
     */
    protected long getPreselectedCategoryId(){
        return -1L;
    }

    protected TransactionListViewModel viewModel;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TransactionListViewModel.class);

        setContent(R.layout.content_transaction_list);
        btAddTransaction = addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTransactionDialog(null);
            }
        });

        getViewElements();

        fillViewElements();
    }

    private void fillViewElements() {
        setTitle(getTransactionListTitle());


        String balanceText = getTotalBalanceText();
        if (balanceText == null) {
            separator.setVisibility(View.GONE);
            tvBalance.setVisibility(View.GONE);
            tvBalanceLabel.setVisibility(View.GONE);
        } else {
            tvBalance.setText(balanceText);
        }
    }

    private void getViewElements() {
        listViewTransactionList = findViewById(R.id.listView_transactionList);
        tvBalance = findViewById(R.id.tv_totalBalance);
        separator = findViewById(R.id.separator);
        tvBalanceLabel = findViewById(R.id.tv_label_totalBalance);
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
//        getTransactionListAsync();
    }

    private void editItem(int indexToEdit) {
//        openTransactionDialog(transactions.get(indexToEdit));
    }

    private void deleteItem(final int indexToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.transaction_delete_dialog_title)
                .setPositiveButton(R.string.transaction_delete_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        transactionDao.deleteAsync(transactions.get(indexToDelete));

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
    protected void openTransactionDialog(Transaction transactionObject) {
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
}
