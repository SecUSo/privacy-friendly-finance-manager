package org.secuso.privacyfriendlyexample.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import org.secuso.privacyfriendlyexample.activities.adapter.CustomListViewAdapter;
import org.secuso.privacyfriendlyexample.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

public class AsyncQueryUpdate extends AsyncTask<Void,Void,Void> {

    PFASampleDataType dataToUpdate;
    PFASQLiteHelper myDB;


    public AsyncQueryUpdate(PFASampleDataType dataToUpdate){
        this.dataToUpdate=dataToUpdate;
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

        myDB.updateSampleData(dataToUpdate);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
