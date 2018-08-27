package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.os.AsyncTask;

import org.secuso.privacyfriendlyfinance.activities.adapter.CustomListViewAdapter;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

public class AsyncQueryDelete extends AsyncTask<Void,Void,Void> {

    private PFASQLiteHelper myDB;
    private ArrayList<PFASampleDataType> list;
    private int position;
    private Context context;

    public AsyncQueryDelete(int position, Context context) {
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
        myDB = new PFASQLiteHelper(context);
        List<PFASampleDataType> database_list = myDB.getAllSampleData();
        list = new ArrayList<>();

        for (PFASampleDataType s : database_list){
            list.add(s);
        }

        myDB.deleteSampleData(list.get(position));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
