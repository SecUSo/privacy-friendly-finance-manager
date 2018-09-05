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
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
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
