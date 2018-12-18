package org.secuso.privacyfriendlyfinance.activities;

import android.app.AlertDialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountWrapper;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountsAdapter;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.helper.SwipeController;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountsViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

public class AccountsActivity extends BaseActivity implements OnItemClickListener<AccountWrapper> {
    private AccountsViewModel viewModel;
    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;

    @Override
    protected Class<? extends BaseViewModel> getViewModelClass() {
        return AccountsViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = (AccountsViewModel) super.viewModel;


        final MutableLiveData<List<AccountWrapper>> accountWrappers = new MutableLiveData<>();

        accountsAdapter = new AccountsAdapter(this, viewModel.getAccounts());

        accountsAdapter.onItemClick(this);

        setContent(R.layout.content_recycler);
        addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountDialog(null);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountsAdapter);

        SwipeController.SwipeControllerAction deleteAction = new SwipeController.SwipeControllerAction() {
            @Override
            public void onClick(int position) {
                deleteAccount(viewModel.getAccounts().getValue().get(position).getAccount());
            }
            @Override
            public Drawable getIcon() {
                return ContextCompat.getDrawable(AccountsActivity.this, R.drawable.ic_delete_red_24dp);
            }
        };

        final SwipeController swipeController = new SwipeController(this, deleteAction, deleteAction);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void deleteAccount(final Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.account_delete_action);
        builder.setMessage(Html.fromHtml(getResources().getString(R.string.account_delete_question, account.getName())));
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FinanceDatabase.getInstance().accountDao().deleteAsync(account);
                Toast.makeText(getBaseContext(), R.string.account_deleted_msg, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }

    private void openAccountDialog(Account account) {
        Bundle args = new Bundle();
        if (account == null) {
        } else {
            args.putLong(CategoryDialog.EXTRA_CATEGORY_ID, account.getId());
        }

        AccountDialog accountDialog = new AccountDialog();
        accountDialog.setArguments(args);

        accountDialog.show(getSupportFragmentManager(), "CategoryDialog");
    }

    @Override
    public void onItemClick(AccountWrapper item) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_ACCOUNT_ID, item.getId());
        startActivity(intent);
    }
}
