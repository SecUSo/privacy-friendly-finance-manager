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

package org.secuso.privacyfriendlyfinance.activities.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.NullableArrayAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.CurrencyInputFilter;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.RepeatingTransactionDialogViewModel;
import org.secuso.privacyfriendlyfinance.databinding.DialogRepeatingTransactionBinding;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

import java.util.List;

/**
 * Dialog used to create and edit repeating transactions.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class RepeatingTransactionDialog extends AppCompatDialogFragment {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";
    public static final String EXTRA_TRANSACTION_ID = "org.secuso.privacyfriendlyfinance.EXTRA_TRANSACTION_ID";

    private AlertDialog dialog;
    private View view;
    private EditText editTextAmount;
    private TextView editTextDate;
    private Spinner categorySpinner;
    private Spinner accountSpinner;
    private ImageButton endClearButton;

    private RepeatingTransactionDialogViewModel viewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        viewModel = new ViewModelProvider(this).get(RepeatingTransactionDialogViewModel.class);

        final DialogRepeatingTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_repeating_transaction, null, false);
        view = binding.getRoot();
        builder.setView(view);

        editTextAmount = view.findViewById(R.id.dialog_transaction_amount);
        editTextDate = view.findViewById(R.id.dialog_transaction_date);
        categorySpinner = view.findViewById(R.id.category_spinner);
        accountSpinner = view.findViewById(R.id.account_spinner);
        endClearButton = view.findViewById(R.id.imageButton_clearEnd);
        viewModel.setCurrencyColors(getResources().getColor(R.color.green), getResources().getColor(R.color.red));

        long transactionId = getArguments().getLong(EXTRA_TRANSACTION_ID, -1L);

        if (transactionId >= 0) {
            builder.setTitle(R.string.dialog_repeating_transaction_edit_title);
        } else {
            builder.setTitle(R.string.dialog_repeating_transaction_create_title);
        }
        if (viewModel.getTransaction() == null) {
            viewModel.setTransactionId(transactionId).observe(this, new Observer<RepeatingTransaction>() {
                @Override
                public void onChanged(@Nullable RepeatingTransaction transaction) {
                    // Is it a transaction dummy?
                    if (transaction.getId() == null) {
                        transaction.setAccountId(getArguments().getLong(EXTRA_ACCOUNT_ID, -1L));
                        transaction.setCategoryId(getArguments().getLong(EXTRA_CATEGORY_ID, -1L));
                    }
                    viewModel.setTransaction(transaction);
                    binding.setViewModel(viewModel);
                }
            });
        } else {
            binding.setViewModel(viewModel);
        }

        AutoCompleteTextView etName = view.findViewById(R.id.editText_transaction_name);
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(getContext(), R.layout.transaction_name_autocomplete_dropdown_item);
        etName.setAdapter(autoCompleteAdapter);
        viewModel.getAllDistinctRepeatingTransactionTitles().observe(this, list -> autoCompleteAdapter.addAll(list));
        viewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts) {
                accountSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, accounts));
            }
        });

        viewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                categorySpinner.setAdapter(new NullableArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, categories));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                viewModel.cancel();
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                viewModel.submit();
            }
        });

        editTextAmount.setFilters(new InputFilter[] {new CurrencyInputFilter()});

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });


        dialog = builder.create();
        return dialog;
    }

    private void openDatePicker() {
        LocalDate date = viewModel.getEnd();

        if (date == null) date = LocalDate.now();
        Log.d("onDateSet", "input: " + date);
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("onDateSet", "day: " + dayOfMonth);
                viewModel.setEnd(new LocalDate(year, month + 1, dayOfMonth));
            }
        }, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        datePicker.show();
    }
}