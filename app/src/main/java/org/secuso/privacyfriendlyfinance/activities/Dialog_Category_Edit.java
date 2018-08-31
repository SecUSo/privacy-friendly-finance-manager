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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.CategoryActivity;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Dialog_Category_Edit extends AppCompatDialogFragment {

    private CategoryDataType dataToEdit;
    private EditText editTextTitle;
    private CategorySQLiteHelper myDB;
    private PFASQLiteHelper db;
    Context context;
    ArrayList<CategoryDataType> list;

    public Dialog_Category_Edit(CategoryDataType dataToEdit, Context context) {
        this.dataToEdit = dataToEdit;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_category_edit, null);
        myDB = new CategorySQLiteHelper(getContext());

        editTextTitle = view.findViewById(R.id.dialog_category_edit_name);
        editTextTitle.setText(dataToEdit.getCategoryName());

        builder.setView(view)
                .setTitle(R.string.dialog_category_edit)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                })

                //defines what happens when dialog is submitted
                .setPositiveButton(R.string.dialog_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String categoryName = editTextTitle.getText().toString();
                        if (categoryName==null){
                            CharSequence text = getString(R.string.dialog_category_toast);
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();
                        }
                        else {
                            new AsyncUpdateCategoryTransactions(dataToEdit,categoryName,context).execute();

                            Toast.makeText(context, R.string.toast_update, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent((Context) getActivity(), CategoryActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        return builder.create();
    }


    /**
     * This nested class opens the Edit Dialog with an AsyncTask
     *
     */
    private class AsyncUpdateCategoryTransactions extends AsyncTask<Void,Void,Void> {

        ArrayList<CategoryDataType> list = new ArrayList<>();
        CategoryDataType data;
        CategorySQLiteHelper myDB;
        Context context;
        PFASQLiteHelper db;
        String categoryName;



        public AsyncUpdateCategoryTransactions(CategoryDataType data,String categoryName, Context context){
            this.data=data;
            this.categoryName=categoryName;
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDB = new CategorySQLiteHelper(context);
            List<CategoryDataType> database_list = myDB.getAllSampleData();
            list = new ArrayList<>();

            for (CategoryDataType s : database_list){
                list.add(s);
            }

            db = new PFASQLiteHelper(context);
            List<PFASampleDataType> db_list = db.getAllSampleData();

            for (PFASampleDataType p : db_list){
                if (p.getTransaction_category().equals(data.getCategoryName())){
                    p.setTransaction_category(categoryName);
                    db.updateSampleData(p);
                }
            }

            myDB.updateSampleData(new CategoryDataType(dataToEdit.getID(),categoryName));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }


}
