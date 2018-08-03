package org.secuso.privacyfriendlyexample.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.activities.adapter.CustomListViewAdapter;
import org.secuso.privacyfriendlyexample.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AsyncQuery extends AsyncTask<Void,Void,Void> {

    private ListView transactionList;
    private CustomListViewAdapter adapter;
    private ArrayList<PFASampleDataType> list = new ArrayList<>();
    private List<PFASampleDataType> database_list;
    private Context context;



    public AsyncQuery (ListView transactionList, Context context){
        this.transactionList=transactionList;
        this.context=context;
    }




    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        PFASQLiteHelper myDB = new PFASQLiteHelper(context);
        database_list = myDB.getAllSampleData();
        list = new ArrayList<>();

        for (PFASampleDataType s : database_list){
            list.add(s);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter = new CustomListViewAdapter(context,list);
        transactionList.setAdapter(adapter);
    }
}
