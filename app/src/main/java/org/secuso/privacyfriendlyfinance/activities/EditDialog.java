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

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;
import org.secuso.privacyfriendlyfinance.activities.MainActivity;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryCategoryDialog;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryCategoryDialogForEdit;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryUpdate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * @author David Meiborg
 * Dialog for editing a existing transaction
 *
 */
@SuppressLint("ValidFragment")
public class EditDialog extends AppCompatDialogFragment {
    private PFASampleDataType dataToEdit;
    private EditText editTextTitle;
    private EditText editTextAmount;
    private TextView editTextDate;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonExpense;
    private Spinner category_spinner;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private PFASQLiteHelper myDB;
    String transactionDate;


    public EditDialog(PFASampleDataType dataToEdit) {
        this.dataToEdit = dataToEdit;
    }

    //opens Dialog with layout defined in edit_dialog.xml
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_dialog, null);
        myDB = new PFASQLiteHelper(getContext());


        editTextTitle = view.findViewById(R.id.edit_dialog_expense_title);
        editTextAmount = view.findViewById(R.id.edit_dialog_expense_amount);
        editTextDate = view.findViewById(R.id.edit_dialog_expense_date);
        radioButtonIncome = view.findViewById(R.id.edit_radioButton_Income);
        radioButtonExpense = view.findViewById(R.id.edit_radioButton_Expense);
        category_spinner = view.findViewById(R.id.edit_category_spinner);

        editTextTitle.setText(dataToEdit.getTransactionName());
        editTextAmount.setText(String.valueOf(dataToEdit.getTransaction_amount()));
        editTextDate.setText(dataToEdit.getTransaction_date());

        new AsyncQueryCategoryDialogForEdit(category_spinner,dataToEdit,getContext()).execute();


        if (dataToEdit.isTransaction_type()==1) {
            radioButtonIncome.setChecked(true);
            radioButtonExpense.setChecked(false);
        }else {
            radioButtonExpense.setChecked(true);
            radioButtonIncome.setChecked(false);
        }


        builder.setView(view)
                .setTitle(R.string.edit_dialog_title)
                .setNegativeButton(R.string.edit_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                })

                //defines what happens when dialog is submitted
                .setPositiveButton(R.string.edit_dialog_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String transactionName;
                        Integer transactionType;
                        String transactionCategory;
                        Double transactionAmount = 0.0;

                        transactionName = editTextTitle.getText().toString();

                        if(editTextAmount.getText().toString()==null){}
                        else {
                            try {
                                transactionAmount = Double.parseDouble(editTextAmount.getText().toString());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        if (transactionAmount==0){
                            CharSequence text = getString(R.string.dialog_toast_0);
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();
                        }

                        else{
                                if (radioButtonExpense.isChecked()) {
                                    transactionType = 0;
                                }else {
                                    transactionType = 1;
                                }

                                transactionDate = editTextDate.getText().toString();

                                transactionCategory = category_spinner.getSelectedItem().toString();

                                PFASQLiteHelper myDB = new PFASQLiteHelper(getActivity());
                                myDB.updateSampleData( new PFASampleDataType(dataToEdit.getID(),transactionName,transactionAmount,transactionType,transactionDate,transactionCategory));

                                Toast.makeText(getContext(), R.string.toast_update, Toast.LENGTH_SHORT).show();

                                Intent main = new Intent((Context)getActivity(),MainActivity.class);
                                startActivity(main);
                            }


                    }
                });



        editTextDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),mDateSetListener,year,month,day);
                dialog.show();
            }
        });

        mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String monthString = String.valueOf(month);
                if (monthString.length() == 1) {
                    monthString = "0" + monthString;
                }

                String dayString = String.valueOf(dayOfMonth);
                if (dayString.length() == 1) {
                    dayString = "0" + dayString;
                }

                transactionDate=dayString+"/"+monthString+"/"+year;
                editTextDate.setText(transactionDate);
            }
        };

        return builder.create();
    }

}

