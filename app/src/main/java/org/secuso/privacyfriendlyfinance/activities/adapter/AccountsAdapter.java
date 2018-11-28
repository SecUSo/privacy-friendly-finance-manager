package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

public class AccountsAdapter extends RecyclerView.Adapter<AccountViewHolder> {
    private Context context;
    private List<Account> accounts;

    public AccountsAdapter(BaseActivity context, LiveData<List<Account>> data) {
        this.context = context;
        data.observe(context, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> newAccounts) {
                accounts = newAccounts;
                notifyDataSetChanged();
            }
        });
    }


    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_card_account, parent, false);
        return new AccountViewHolder(viewItem);
    }



    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int index) {
        Account account = accounts.get(index);
        holder.getTvAccountName().setText(account.getName());
        holder.getTvAccountBalanceCurrent().setText(String.valueOf(account.getInitialBalance()));
        holder.getTvAccountBalanceCurrent().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDebugToast("Clicked on balance");
            }
        });
        //TODO: Fill in the right value...
        holder.getTvAccountBalanceMonth().setText(String.valueOf(42));
    }

    @Override
    public int getItemCount() {
        if (accounts == null) return 0;
        return accounts.size();
    }

    private void showDebugToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
