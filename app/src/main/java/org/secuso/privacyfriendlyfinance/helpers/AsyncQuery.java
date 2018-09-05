/*
 This file is part of Privacy Friendly App Finance Manager.

 Privacy Friendly App Finance Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Finance Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Finance Manager. If not, see <http://www.gnu.org/licenses/>.
 */
package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.CustomListViewAdapter;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * AsyncTask for creating transaction list
 * @author David Meiborg
 * @version 2018
 */
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
