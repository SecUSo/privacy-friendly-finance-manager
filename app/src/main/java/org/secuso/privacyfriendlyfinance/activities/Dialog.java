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
package org.secuso.privacyfriendlyfinance.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryCustomListViewAdapter;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;
import org.secuso.privacyfriendlyfinance.activities.MainActivity;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryCategory;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryCategoryDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Dialog extends AppCompatDialogFragment {
    private EditText editTextTitle;
    private EditText editTextAmount;
    private TextView editTextDate;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonExpense;
    private RadioGroup radioGroupType;
    private Spinner category_spinner;
    private Integer transactionType;
    private Double transactionAmount = 0.0;
    private String transactionCategory;
    private String transactionName;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private PFASQLiteHelper myDB;

    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    String date = day+"/"+month+"/"+year;

    private String transactionDate=date;

    //opens Dialog with layout defined in dialog.xml
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        myDB = new PFASQLiteHelper(getActivity());

        editTextTitle = view.findViewById(R.id.dialog_expense_title);
        editTextAmount = view.findViewById(R.id.dialog_expense_amount);
        editTextDate = view.findViewById(R.id.dialog_expense_date);

        radioButtonIncome = view.findViewById(R.id.radioButton_Income);
        radioButtonExpense = view.findViewById(R.id.radioButton_Expense);
        radioGroupType = view.findViewById(R.id.radioGroup_type);
        category_spinner= view.findViewById(R.id.category_spinner);

        radioButtonExpense.setChecked(true);
        radioButtonIncome.setChecked(false);
        editTextDate.setText(transactionDate);

        new AsyncQueryCategoryDialog(category_spinner,getContext()).execute();


                builder.setView(view)
                .setTitle(R.string.dialog_title)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                })

                //defines what happens when dialog is submitted
                .setPositiveButton(R.string.dialog_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        transactionName = editTextTitle.getText().toString();

                        if(editTextAmount.getText().toString()==null){

                        }
                        else{
                            try {
                                transactionAmount = Double.parseDouble(editTextAmount.getText().toString());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        if (transactionAmount==0){
                            CharSequence text = getString(R.string.dialog_toast);
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();
                        }
                        else{
                            if (radioGroupType.getCheckedRadioButtonId()==R.id.radioButton_Expense) {
                                transactionType = 0;
                            }
                            else {
                                transactionType=1;
                            }

                            transactionDate = editTextDate.getText().toString();

                            transactionCategory = category_spinner.getSelectedItem().toString();

                            myDB.addSampleData(new PFASampleDataType(1,transactionName,transactionAmount,transactionType,transactionDate,transactionCategory));

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
                transactionDate=dayOfMonth+"/"+month+"/"+year;
                editTextDate.setText(transactionDate);
            }
        };

        return builder.create();
    }

}
