package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.os.AsyncTask;

import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

public class AsyncQueryUpdateCategory extends AsyncTask<Void,Void,Void> {

    CategoryDataType dataToUpdate;
    CategorySQLiteHelper myDB;
    Context context;

    public AsyncQueryUpdateCategory(CategoryDataType dataToUpdate, Context context){
        this.dataToUpdate=dataToUpdate;
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
        myDB.updateSampleData(dataToUpdate);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
