/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

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

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;
import org.secuso.privacyfriendlyfinance.helpers.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Adapter for transaction lists.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class TransactionsAdapter extends EntityListAdapter<Transaction, TransactionViewHolder> {
    public interface TransactionUiListener {
        void onOpen(Transaction transaction);
        void onSelectionChanged(int selectedCount, String groupedSummary);
    }

    private final Set<Long> selectedIds = new HashSet<>();
    private boolean selectionMode = false;
    private TransactionUiListener listener;
    private final LiveData<Map<Long, Account>> accounts = FinanceDatabase.getInstance(context).accountDao().getAllMap();
    private final LiveData<Map<Long, Category>> categories = FinanceDatabase.getInstance(context).categoryDao().getAllMap();

    public TransactionsAdapter(BaseActivity context, LiveData<List<Transaction>> data) {
        super(context, data);
        accounts.observe(context, map -> {
            if (selectionMode) {
                notifySelectionChanged();
            }
        });
        categories.observe(context, map -> {
            if (selectionMode) {
                notifySelectionChanged();
            }
        });
    }

    public void setListener(TransactionUiListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_transaction, parent, false);
        return new TransactionViewHolder(viewItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionViewHolder holder, int index) {
        Transaction transaction = getItem(index);
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position == androidx.recyclerview.widget.RecyclerView.NO_POSITION) return;
            Transaction clicked = getItem(position);
            if (selectionMode) {
                toggleSelection(clicked.getId());
            } else if (listener != null) {
                listener.onOpen(clicked);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position == androidx.recyclerview.widget.RecyclerView.NO_POSITION) return false;
            Transaction clicked = getItem(position);
            if (!selectionMode) {
                selectionMode = true;
            }
            toggleSelection(clicked.getId());
            return true;
        });

        holder.setTransactionName(transaction.getName());
        holder.setDate(transaction.getDate());
        holder.setAmount(transaction.getAmount());
        holder.setSelectedState(selectedIds.contains(transaction.getId()));

        FinanceDatabase.getInstance(context).accountDao().get(transaction.getAccountId()).observe(context, account -> {
            if (account != null) {
                holder.setAccountName(account.getName());
                holder.setAmount(transaction.getAmount(), SharedPreferencesManager.get(context).getAccountCurrencyCode(account.getId()));
            } else {
                holder.setAccountName(context.getResources().getString(R.string.not_found_error));
                holder.setAmount(transaction.getAmount());
            }
        });

        if (transaction.getCategoryId() != null) {
            FinanceDatabase.getInstance(context).categoryDao().get(transaction.getCategoryId()).observe(context, category -> {
                if (category != null) {
                    holder.setCategoryName(category.getName());
                    holder.setCategoryColor(category.getColor());
                } else {
                    holder.setCategoryName(context.getResources().getString(R.string.not_found_error));
                    holder.setCategoryColor(null);
                }
            });
        } else {
            holder.setCategoryName(null);
            holder.setCategoryColor(null);
        }

        if (transaction.getRepeatingId() != null) {
            FinanceDatabase.getInstance(context).repeatingTransactionDao().get(transaction.getRepeatingId()).observe(context, repeatingTransaction -> {
                if (repeatingTransaction != null) {
                    holder.setRepeatingName(repeatingTransaction.getName());
                } else {
                    holder.setRepeatingName(context.getResources().getString(R.string.not_found_error));
                }
            });
        } else {
            holder.setRepeatingName(null);
        }
    }

    private void toggleSelection(Long id) {
        if (id == null) return;
        if (selectedIds.contains(id)) {
            selectedIds.remove(id);
        } else {
            selectedIds.add(id);
        }
        if (selectedIds.isEmpty()) {
            selectionMode = false;
        }
        notifyDataSetChanged();
        notifySelectionChanged();
    }

    private void notifySelectionChanged() {
        if (listener != null) {
            listener.onSelectionChanged(selectedIds.size(), getGroupedSummary());
        }
    }

    public boolean isSelectionMode() {
        return selectionMode;
    }

    public void clearSelection() {
        selectedIds.clear();
        selectionMode = false;
        notifyDataSetChanged();
        notifySelectionChanged();
    }

    public List<Transaction> getSelectedTransactions() {
        List<Transaction> selected = new ArrayList<>();
        List<Transaction> all = getCurrentList();
        for (Transaction transaction : all) {
            if (selectedIds.contains(transaction.getId())) {
                selected.add(transaction);
            }
        }
        return selected;
    }

    public long getSelectedSum() {
        long sum = 0L;
        for (Transaction transaction : getCurrentList()) {
            if (selectedIds.contains(transaction.getId())) {
                sum += transaction.getAmount();
            }
        }
        return sum;
    }

    public String getGroupedSummary() {
        String defaultCurrency = SharedPreferencesManager.get(context).getDefaultCurrencyCode();
        Map<String, Long> totalsByCurrency = new LinkedHashMap<>();
        long defaultTotal = 0L;
        boolean hasUnknownConversion = false;

        Map<Long, Account> accountMap = accounts.getValue();
        Map<Long, Category> categoryMap = categories.getValue();

        for (Transaction transaction : getCurrentList()) {
            if (!selectedIds.contains(transaction.getId())) continue;

            String accountCurrency = defaultCurrency;
            if (accountMap != null) {
                Account account = accountMap.get(transaction.getAccountId());
                if (account != null && account.getId() != null) {
                    accountCurrency = SharedPreferencesManager.get(context).getAccountCurrencyCode(account.getId());
                }
            }

            long old = totalsByCurrency.containsKey(accountCurrency) ? totalsByCurrency.get(accountCurrency) : 0L;
            totalsByCurrency.put(accountCurrency, old + transaction.getAmount());

            if (transaction.getDefaultAmount() != null) {
                defaultTotal += transaction.getDefaultAmount();
                continue;
            }

            if (defaultCurrency.equalsIgnoreCase(accountCurrency)) {
                defaultTotal += transaction.getAmount();
                continue;
            }

            if (transaction.getCategoryId() != null && categoryMap != null) {
                Category category = categoryMap.get(transaction.getCategoryId());
                if (category != null && category.getCurrencyCode() != null &&
                        defaultCurrency.equalsIgnoreCase(category.getCurrencyCode()) &&
                        transaction.getCategoryAmount() != null) {
                    defaultTotal += transaction.getCategoryAmount();
                    continue;
                }
            }

            hasUnknownConversion = true;
        }

        List<String> parts = new ArrayList<>();
        for (Map.Entry<String, Long> entry : totalsByCurrency.entrySet()) {
            parts.add(CurrencyHelper.convertToCurrencyString(context, entry.getValue(), entry.getKey()));
        }

        StringBuilder result = new StringBuilder(String.join(" + ", parts));
        if (result.length() > 0) {
            result.append(" = ").append(CurrencyHelper.convertToCurrencyString(context, defaultTotal, defaultCurrency));
        } else {
            result.append(CurrencyHelper.convertToCurrencyString(context, 0L, defaultCurrency));
        }
        if (hasUnknownConversion) {
            result.append(" (partial)");
        }
        return result.toString();
    }
}
