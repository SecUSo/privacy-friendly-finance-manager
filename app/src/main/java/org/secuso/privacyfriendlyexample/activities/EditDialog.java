package org.secuso.privacyfriendlyexample.activities;

/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */

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

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.database.CategoryDataType;
import org.secuso.privacyfriendlyexample.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;
import org.secuso.privacyfriendlyexample.activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class EditDialog extends AppCompatDialogFragment {
    private PFASampleDataType dataToEdit;
    private EditText editTextTitle;
    private EditText editTextAmount;
    private TextView editTextDate;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonExpense;
    private Spinner category_spinner;

    String transactionDate;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private PFASQLiteHelper myDB;

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

        editTextTitle.setText(dataToEdit.getTransactionName());
        editTextAmount.setText(dataToEdit.getTransaction_amount().toString());
        editTextDate.setText(dataToEdit.getTransaction_date());

        category_spinner= view.findViewById(R.id.edit_category_spinner);

        CategorySQLiteHelper myDBCategory = new CategorySQLiteHelper(getActivity());
        List<CategoryDataType> database_list = myDBCategory.getAllSampleData();
        ArrayList<String> list = new ArrayList<>();

        for (CategoryDataType s : database_list){
            list.add(s.getCategoryName());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String> (getActivity(),R.layout.spinner_row,R.id.spinner_content,list);
        category_spinner.setAdapter(adapter);

        String compareValue = dataToEdit.getTransaction_category();
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            category_spinner.setSelection(spinnerPosition);
        }

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
                        String transactionName = editTextTitle.getText().toString();
                        Integer transactionType;
                        String transactionCategory;
                        Double transactionAmount = Double.parseDouble(editTextAmount.getText().toString());

                        if (radioButtonExpense.isChecked()) {
                            transactionType = 0;
                            transactionAmount=transactionAmount*(-1);
                        }else {
                            transactionType = 1;
                        }

                        transactionDate = editTextDate.getText().toString();

                        transactionCategory = category_spinner.getSelectedItem().toString();

                        myDB.updateSampleData(new PFASampleDataType(dataToEdit.getID(),transactionName,transactionAmount,transactionType,transactionDate,transactionCategory));

                        Intent main = new Intent((Context)getActivity(),MainActivity.class);
                        startActivity(main);
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
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                transactionDate=dayOfMonth+"/"+month+"/"+year;
                editTextDate.setText(transactionDate);
            }
        };

        return builder.create();
    }

}

