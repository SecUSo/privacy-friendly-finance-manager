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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;

import java.util.Calendar;

/**
 * Dialog for adding a new transaction
 *
 * @author David Meiborg
 */
public class TransactionDialog extends AppCompatDialogFragment {
    private EditText editTextTitle;
    private EditText editTextAmount;
    private TextView editTextDate;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonExpense;
    private RadioGroup radioGroupType;
    private Spinner categorySpinner;

    private Double transactionAmount = 0.0;
    private Integer transactionType;
    private String transactionDate;
    private String transactionCategory;
    private String transactionName;


    //opens Dialog with layout defined in dialog.xml
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        editTextTitle = view.findViewById(R.id.dialog_expense_title);
        editTextAmount = view.findViewById(R.id.dialog_expense_amount);
        editTextDate = view.findViewById(R.id.dialog_expense_date);

        radioButtonIncome = view.findViewById(R.id.radioButton_Income);
        radioButtonExpense = view.findViewById(R.id.radioButton_Expense);
        radioGroupType = view.findViewById(R.id.radioGroup_type);
        categorySpinner = view.findViewById(R.id.category_spinner);

        radioButtonExpense.setChecked(true);
        radioButtonIncome.setChecked(false);

        //Handle the arguments
        {
            Bundle arguments = getArguments();
            long id = arguments.getLong("transactionId", -1L);
            if (id == -1L) {
                
            }
        }

        //set transactionDate to current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        String monthString = String.valueOf(month);
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }

        String dayString = String.valueOf(day);
        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }

        transactionDate = dayString + "/" + monthString + "/" + year;

        editTextDate.setText(transactionDate);

        //Retrieve categories and put them in the spinner
        {
            CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();
            categoryDao.getAllAsync(new TaskListener() {
                @Override
                public void onDone(Object result, AsyncTask<?, ?, ?> task) {
                    //TODO: FILL SPINNER
                }
                @Override
                public void onProgress(Double progress, AsyncTask<?, ?, ?> task) {
                }
                @Override
                public void onOperation(String operation, AsyncTask<?, ?, ?> task) {
                }
            });
        }


        builder.setView(view)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //Nothing to do here
                    }
                })
                //defines what happens when dialog is submitted
                .setPositiveButton(R.string.dialog_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        submitTransaction();
                    }
                });


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        return builder.create();
    }

    private void dateSet(int year, int month, int dayOfMonth) {
        month = month + 1;

        String monthString = String.valueOf(month);
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }

        String dayString = String.valueOf(dayOfMonth);
        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }

        transactionDate = dayString + "/" + monthString + "/" + year;
        editTextDate.setText(transactionDate);
    }

    private void openDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateSet(year, month, dayOfMonth);
            }
        }, year, month, day);
        dialog.show();
    }

    private void submitTransaction() {
        transactionName = editTextTitle.getText().toString();

        if (editTextAmount.getText().toString() == null) {

        } else {
            try {
                transactionAmount = Double.parseDouble(editTextAmount.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (transactionAmount == 0) {
            CharSequence text = getString(R.string.dialog_toast_0);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getContext(), text, duration);
            toast.show();
        } else {
            if (radioGroupType.getCheckedRadioButtonId() == R.id.radioButton_Expense) {
                transactionType = 0;
            } else {
                transactionType = 1;
            }

            transactionDate = editTextDate.getText().toString();
            transactionCategory = categorySpinner.getSelectedItem().toString();

//            myDB.addSampleData(new PFASampleDataType(1, transactionName, transactionAmount, transactionType, transactionDate, transactionCategory));

            //Save the edited/created transaction
            {
                TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();
                transactionDao.updateOrInsertAsync(tran)
            }

            Toast.makeText(getContext(), R.string.toast_new_entry, Toast.LENGTH_SHORT).show();

//            Intent main = new Intent(getActivity(), MainActivity.class);
//            startActivity(main);
            System.out.println("Transaction submit");

        }
    }
}
