package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryCustomListViewAdapter;
import org.secuso.privacyfriendlyfinance.activities.adapter.CustomListViewAdapter;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

public class AsyncQueryCategory extends AsyncTask<Void,Void,Void> {

    private ListView categoryList;
    private CategoryCustomListViewAdapter adapter;
    private ArrayList<CategoryDataType> list = new ArrayList<>();
    private List<CategoryDataType> database_list;
    private Context context;


    public AsyncQueryCategory(ListView categoryList, Context context){
        this.categoryList=categoryList;
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
            list.add(s);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter = new CategoryCustomListViewAdapter(context,list);
        categoryList.setAdapter(adapter);
    }
}
