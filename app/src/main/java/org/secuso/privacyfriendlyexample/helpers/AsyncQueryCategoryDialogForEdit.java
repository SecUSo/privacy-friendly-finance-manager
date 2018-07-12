package org.secuso.privacyfriendlyexample.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.database.CategoryDataType;
import org.secuso.privacyfriendlyexample.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

public class AsyncQueryCategoryDialogForEdit extends AsyncTask<Void,Void,Void> {

    private Spinner category_spinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<>();
    private List<CategoryDataType> database_list;
    private Context context;
    private PFASampleDataType dataToEdit;


    public AsyncQueryCategoryDialogForEdit(Spinner category_spinner,PFASampleDataType dataToEdit, Context context){
        this.category_spinner=category_spinner;
        this.dataToEdit = dataToEdit;
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

        CategorySQLiteHelper myDB = new CategorySQLiteHelper(context);
        database_list = myDB.getAllSampleData();
        list = new ArrayList<>();

        for (CategoryDataType s : database_list){
            list.add(s.getCategoryName());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter=new ArrayAdapter (context, R.layout.spinner_row,R.id.spinner_content,list);
        category_spinner.setAdapter(adapter);

        String compareValue = dataToEdit.getTransaction_category();
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            category_spinner.setSelection(spinnerPosition);
        }
    }
}
