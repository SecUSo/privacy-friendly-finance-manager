package org.secuso.privacyfriendlyexample.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.activities.Dialog;
import org.secuso.privacyfriendlyexample.activities.MainActivity;
import org.secuso.privacyfriendlyexample.activities.adapter.CategoryCustomListViewAdapter;
import org.secuso.privacyfriendlyexample.database.CategoryDataType;
import org.secuso.privacyfriendlyexample.database.CategorySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class AsyncQueryCategoryDialog extends AsyncTask<Void,Void,Void> {

    private Spinner category_spinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<>();
    private List<CategoryDataType> database_list;
    private Context context;


    public AsyncQueryCategoryDialog(Spinner category_spinner, Context context){
        this.category_spinner=category_spinner;
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
        list = new ArrayList<String>();

        for (CategoryDataType s : database_list){
            list.add(s.getCategoryName());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter=new ArrayAdapter (context, R.layout.spinner_row,R.id.spinner_content,list);
        category_spinner.setAdapter(adapter);
    }
}
