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
package org.secuso.privacyfriendlyexample.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;
import org.secuso.privacyfriendlyexample.activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dialog extends AppCompatDialogFragment {
    private EditText editTextTitle;
    private EditText editTextAmount;
    private EditText editTextAccount;
    private EditText editTextDate;
    private RadioButton radioButtonIncome;
    private RadioButton radioButtonExpense;


    private PFASQLiteHelper myDB;


    //opens Dialog with layout defined in dialog.xml
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        myDB = new PFASQLiteHelper(getContext());

        builder.setView(view)
                .setTitle("New Transaction")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                })

                //defines what happens when dialog is submitted
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String transactionName = editTextTitle.getText().toString();
                        Integer transactionAmount = Integer.parseInt(editTextAmount.getText().toString());
                        Boolean transactionType;
                        if (radioButtonExpense.isChecked()) {
                            transactionType = false;
                            transactionAmount=transactionAmount*(-1);
                        }else {
                            transactionType = true;
                        }
                        String transactionAccount = editTextAccount.getText().toString();
                        String transactionDate = editTextDate.getText().toString();

                        myDB.addSampleData(new PFASampleDataType(1,transactionName,transactionAmount,transactionType,transactionAccount,transactionDate));

                        Intent main = new Intent((Context)getActivity(),MainActivity.class);
                        startActivity(main);
                    }
                });

        editTextTitle = view.findViewById(R.id.dialog_expense_title);
        editTextAmount = view.findViewById(R.id.dialog_expense_amount);
        editTextAccount = view.findViewById(R.id.dialog_expense_account);
        editTextDate = view.findViewById(R.id.dialog_expense_date);
        radioButtonIncome = view.findViewById(R.id.radioButton_Income);
        radioButtonExpense = view.findViewById(R.id.radioButton_Expense);


        return builder.create();
    }

    /**public void AddData(String transactionName) {
        myDB.addSampleData(new PFASampleDataType(1, transactionName));
    }
    **/
}