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

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryArrayAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.convert.DateTimeConverter;
import org.secuso.privacyfriendlyfinance.domain.convert.LocalDateConverter;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.Calendar;
import java.util.List;

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

    private Transaction transactionObject;


    //opens Dialog with layout defined in dialog.xml
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.transaction_dialog, null);

        // Create views
        editTextTitle = view.findViewById(R.id.dialog_expense_title);
        editTextAmount = view.findViewById(R.id.dialog_expense_amount);
        editTextDate = view.findViewById(R.id.dialog_expense_date);

        radioButtonIncome = view.findViewById(R.id.radioButton_Income);
        radioButtonExpense = view.findViewById(R.id.radioButton_Expense);
        radioGroupType = view.findViewById(R.id.radioGroup_type);
        categorySpinner = view.findViewById(R.id.category_spinner);

        radioButtonExpense.setChecked(true);
        radioButtonIncome.setChecked(false);

        //Retrieve categories and put them in the spinner
        {
            CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();
            categoryDao.getAllAsync(new TaskListener() {
                @Override
                public void onDone(Object result, AsyncTask<?, ?, ?> task) {
                    List<Category> categories = (List<Category>) result;
                    System.out.println("########################");
                    System.out.println("########################");
                    System.out.println("#########category#######");
                    System.out.println(categories.size());
                    System.out.println("########################");
                    System.out.println("########################");
                    System.out.println("########################");
                    categorySpinner.setAdapter(new CategoryArrayAdapter(getActivity(), categories));
                }
            });
        }

        // Create buttons
        builder.setView(view)
                .setNegativeButton(R.string.transaction_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //Nothing to do here
                    }
                })
                //defines what happens when dialog is submitted
                .setPositiveButton(R.string.transaction_dialog_submit, new DialogInterface.OnClickListener() {
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

        //Handle the arguments given to this dialog
        boolean editingExistingTransaction = false;
        transactionObject = new Transaction();
        {
            Bundle arguments = getArguments();
            long id = arguments.getLong("transactionId", -1L);
            if (id == -1L) {
                editingExistingTransaction = false;
            } else {
                editingExistingTransaction = true;

                transactionObject.setId(id);
                transactionObject.setAmount(arguments.getLong("transactionAmount", -1L));
                //DATE
                {
                    String dateArg = arguments.getString("transactionDate", "ERROR");
                    if (dateArg.equals("ERROR")) {
                        transactionObject.setDate(LocalDate.now());
                    } else {
                        transactionObject.setDate(LocalDateConverter.fromString(dateArg));
                    }
                }
                transactionObject.setName(arguments.getString("transactionName", "ERROR"));
            }
        }

        //Do edit/setup specific things
        if (editingExistingTransaction) {
            setUpEditDialog(builder);
        } else {
            setUpCreateDialog(builder);
        }

        return builder.create();
    }

    private void setUpEditDialog(AlertDialog.Builder builder) {
        builder.setTitle(R.string.transaction_dialog_title_edit);
        editTextTitle.setText(transactionObject.getName());
        editTextAmount.setText(String.valueOf(transactionObject.getAmount()));
        editTextDate.setText(transactionObject.getDateAsString());
    }

    private void setUpCreateDialog(AlertDialog.Builder builder) {
        builder.setTitle(R.string.transaction_dialog_title_new);

        //Fill the date field with the date 'now'
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        DateTime dt = new DateTime().withYear(year).withMonthOfYear(month).withDayOfMonth(dayOfMonth);
        editTextDate.setText(DateTimeConverter.datetimeToString(dt));
    }

    private void dateSet(int year, int month, int dayOfMonth) {
        DateTime dt = new DateTime();
        dt = dt.withYear(year).withMonthOfYear(month).withDayOfMonth(dayOfMonth);

        editTextDate.setText(DateTimeConverter.datetimeToString(dt));
    }

    private void openDatePicker() {
        //Open the date picker on the day that is already set
        DateTime dt = DateTimeConverter.fromString(editTextDate.getText().toString());
        int year = dt.getYear();
        int month = dt.getMonthOfYear() + 1;
        int day = dt.getDayOfMonth();

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
            Toast toast = Toast.makeText(getContext(), getString(R.string.dialog_toast_0), Toast.LENGTH_LONG);
            toast.show();
        } else {
            boolean isExpense = false;
            if (radioGroupType.getCheckedRadioButtonId() == R.id.radioButton_Expense) {
                isExpense = true;
            } else {
                isExpense = false;
            }

            String tmpDate = editTextDate.getText().toString();
            Category tmpCategory = (Category) categorySpinner.getSelectedItem();

            transactionObject.setCategoryId(tmpCategory.getId());
            transactionObject.setAmount((isExpense ? -tmpAmount : tmpAmount));
            transactionObject.setName(tmpName);
            transactionObject.setDate(LocalDateConverter.fromString(tmpDate));

            //Save the edited/created transaction
            FinanceDatabase.getInstance().transactionDao().updateOrInsertAsync(transactionObject, null);

            Toast.makeText(getContext(), R.string.toast_new_entry, Toast.LENGTH_SHORT).show();

            //TODO: I don't think this should be done this way. But works for now.
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }
}
