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
package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Felix Hofmann, Leonard Otto
 * Adapter for displaying the transaction list
 */
public class TransactionArrayAdapter extends ArrayAdapter<Transaction> {
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private AppCompatActivity context;

    public TransactionArrayAdapter(AppCompatActivity context, List<Transaction> transactions) {
        super(context, 0, transactions);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_transaction, parent, false);
        }
        final TextView tvName = convertView.findViewById(R.id.textview_transaction_name);
        final TextView tvAmount = convertView.findViewById(R.id.textView_amount);
        final TextView tvAccount = convertView.findViewById(R.id.textView_account);
        final TextView tvCategory = convertView.findViewById(R.id.textView_category);
        final TextView tvMonth = convertView.findViewById(R.id.textView_month);
        final TextView tvDay = convertView.findViewById(R.id.textView_day_of_month);

        Transaction transaction = getItem(position);

        tvName.setText(transaction.getName());
        tvMonth.setText(transaction.getDate().toString("MMM", Locale.getDefault()));
        tvDay.setText(transaction.getDate().toString("dd"));
        tvAccount.setText("#" + transaction.getAccountId() + " (TODO)");

        FinanceDatabase.getInstance().accountDao().get(transaction.getAccountId()).observe(context,  new Observer<Account>() {
            @Override
            public void onChanged(@Nullable Account account) {
                if (account != null) {
                    tvAccount.setText(account.getName());
                } else {
                    tvAccount.setText(R.string.not_found_error);
                    tvAccount.setTextColor(getContext().getResources().getColor(R.color.red));
                }
            }
        });

        if (transaction.getCategoryId() != null) {
            FinanceDatabase.getInstance().categoryDao().get(transaction.getCategoryId()).observe(context,  new Observer<Category>() {
                @Override
                public void onChanged(@Nullable Category category) {
                    if (category != null) {
                        tvCategory.setText(category.getName());
                    } else {
                        tvCategory.setText(R.string.not_found_error);
                        tvCategory.setTextColor(getContext().getResources().getColor(R.color.red));
                    }
                }
            });
        } else {
            tvCategory.setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.imageView_category).setVisibility(View.INVISIBLE);
        }


        tvAmount.setText(currencyFormat.format(((double) transaction.getAmount()) / 100.0));
        if (transaction.getAmount() < 0) {
            tvAmount.setTextColor(getContext().getResources().getColor(R.color.red));
        } else {
            tvAmount.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        return convertView;
    }
}
