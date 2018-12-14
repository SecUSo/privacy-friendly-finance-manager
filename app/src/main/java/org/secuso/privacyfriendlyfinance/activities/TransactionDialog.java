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

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.NullableArrayAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.CurrencyTextWatcher;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionDialogViewModel;
import org.secuso.privacyfriendlyfinance.domain.convert.LocalDateConverter;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dialog for adding new transactions and for editing existing transactions.
 *
 * @author Leonard Otto, Felix Hofmann
 */
public class TransactionDialog extends AppCompatDialogFragment {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";
    public static final String EXTRA_TRANSACTION_ID = "org.secuso.privacyfriendlyfinance.EXTRA_TRANSACTION_ID";

    private AlertDialog dialog;
    private View view;
    private EditText editTextTitle;
    private EditText editTextAmount;
    private TextView editTextDate;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonExpense;
    private RadioGroup radioGroupType;
    private Spinner categorySpinner;
    private Spinner accountSpinner;

    private TransactionDialogViewModel viewModel;

    private Transaction transactionObject;
    private long preselectedAccountId = -1L;
    private long preselectedCategoryId = -1L;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        viewModel = ViewModelProviders.of(this).get(TransactionDialogViewModel.class);
        viewModel.setTransactionId(getArguments().getLong(EXTRA_TRANSACTION_ID, -1L));

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_transaction, null);
        builder.setView(view);

        editTextTitle = view.findViewById(R.id.dialog_transaction_title);
        editTextAmount = view.findViewById(R.id.dialog_transaction_amount);
        editTextDate = view.findViewById(R.id.dialog_transaction_date);
        radioButtonIncome = view.findViewById(R.id.radioButton_transaction_income);
        radioButtonExpense = view.findViewById(R.id.radioButton_transaction_expense);
        radioGroupType = view.findViewById(R.id.radioGroup_transaction_type);
        categorySpinner = view.findViewById(R.id.category_spinner);
        accountSpinner = view.findViewById(R.id.account_spinner);


        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {}
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
//                submitTransaction();
            }
        });

        editTextAmount.addTextChangedListener(new CurrencyTextWatcher(editTextAmount, "#,###"));

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        dialog = builder.create();


        viewModel.getTransaction().observe(this, new Observer<Transaction>() {
            @Override
            public void onChanged(@Nullable Transaction transaction) {
                TransactionDialog.this.transactionObject = transaction;
                if (transaction.getId() == null) {
                    transaction.setAccountId(getArguments().getLong(EXTRA_ACCOUNT_ID, -1L));
                    transaction.setCategoryId(getArguments().getLong(EXTRA_CATEGORY_ID, -1L));
                    dialog.setTitle(R.string.dialog_transaction_create_title);
                } else {
                    dialog.setTitle(R.string.dialog_transaction_edit_title);
                }

                editTextTitle.setText(transaction.getName());
                editTextDate.setText(transaction.getDateAsString());
                setAmount(transaction.getAmount());
            }
        });

        return dialog;
    }



    private void setAmount(long amount) {
        editTextAmount.setText(String.valueOf((float) Math.abs(amount) / 100.0));
        radioButtonExpense.setChecked(amount <= 0);
        radioButtonIncome.setChecked(amount > 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        viewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts) {
                accountSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, accounts));
                if (preselectedAccountId != -1L) {
                    for (int i = 0; i < accounts.size(); i++) {
                        if (accounts.get(i).getId() == preselectedAccountId) {
                            accountSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });

        viewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                List<Category> categoriesAndVoid = new ArrayList<>();
                categoriesAndVoid.add(null);
                categoriesAndVoid.addAll(categories);
                categorySpinner.setAdapter(new NullableArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, categoriesAndVoid));
                if (preselectedCategoryId != -1L) {
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getId() == preselectedCategoryId) {
                            categorySpinner.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });
    }


    private void setUpCreateDialog(AlertDialog.Builder builder) {
        builder.setTitle(R.string.dialog_transaction_create_title);

        //Fill the date field with the date 'now'
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        LocalDate ld = new LocalDate(year, month, dayOfMonth);
        editTextDate.setText(LocalDateConverter.dateToString(ld));
    }

    private void dateSet(int year, int month, int dayOfMonth) {
        LocalDate ld = new LocalDate(year, month + 1, dayOfMonth);

        editTextDate.setText(LocalDateConverter.dateToString(ld));
    }

    private void openDatePicker() {
        //Open the date picker on the day that is already set
        LocalDate ld = LocalDateConverter.fromString(editTextDate.getText().toString());
        int year = ld.getYear();
        int month = ld.getMonthOfYear() + 1;
        int day = ld.getDayOfMonth();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateSet(year, month, dayOfMonth);
            }
        }, year, month, day);
        dialog.show();
    }

    private void submitTransaction() {
        String tmpName = editTextTitle.getText().toString();

        long tmpAmount = -1L;
        if (editTextAmount.getText().toString() == null) {
            //TODO: error message?
            return;
        } else {
            try {
                double tmpDoubleAmount = Double.parseDouble(editTextAmount.getText().toString());
                tmpAmount = (long) (tmpDoubleAmount * 100.0);
            } catch (NumberFormatException ex) {
                //This line should not be reachable
                //TODO: How to handle exceptions like that?
                System.out.println("Error parsing transaction amount. Got string: " + editTextAmount.getText().toString());
                return;
            }
        }

        if (tmpAmount == 0) {
            Toast toast = Toast.makeText(getContext(), getString(R.string.dialog_transaction_zero_value_impossible_msg), Toast.LENGTH_LONG);
            toast.show();
        } else {
            boolean isExpense = false;
            if (radioGroupType.getCheckedRadioButtonId() == R.id.radioButton_transaction_expense) {
                isExpense = true;
            } else {
                isExpense = false;
            }

            String tmpDate = editTextDate.getText().toString();
            Category tmpCategory = (Category) categorySpinner.getSelectedItem();
            Account tmpAccount = (Account) accountSpinner.getSelectedItem();

            transactionObject.setCategoryId(tmpCategory.getId());
            transactionObject.setAccountId(tmpAccount.getId());
            transactionObject.setAmount((isExpense ? -tmpAmount : tmpAmount));
            transactionObject.setName(tmpName);
            transactionObject.setDate(LocalDateConverter.fromString(tmpDate));

            //Save the edited/created transaction
            viewModel.editOrInsertTransaction(transactionObject);

            Toast.makeText(getContext(), R.string.activity_transaction_saved_msg, Toast.LENGTH_SHORT).show();

            dismiss();
        }
    }
}
