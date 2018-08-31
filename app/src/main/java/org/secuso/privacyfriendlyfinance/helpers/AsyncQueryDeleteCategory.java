package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

public class AsyncQueryDeleteCategory extends AsyncTask<Void,Void,Void> {

    private CategorySQLiteHelper myDB;
    private ArrayList<CategoryDataType> list;
    private PFASQLiteHelper db;
    private int position;
    private Context context;

    public AsyncQueryDeleteCategory(int position, Context context) {
        this.position = position;
        this.context = context;
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
            if (p.getTransaction_category().equals(list.get(position).getCategoryName())){
                p.setTransaction_category("Standard");
                db.updateSampleData(p);
            }
        }

        myDB.deleteSampleData(list.get(position));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
