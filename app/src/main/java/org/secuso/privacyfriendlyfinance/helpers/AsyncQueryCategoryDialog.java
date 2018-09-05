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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.Dialog;
import org.secuso.privacyfriendlyfinance.activities.MainActivity;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryCustomListViewAdapter;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;

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
