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
import android.widget.Spinner;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;
/**
 * AsyncTask for creating category spinner in edit dialog
 * @author David Meiborg
 * @version 2018
 */
public class AsyncQueryCategoryDialogForEdit extends AsyncTask<Void,Void,Void> {

    private Spinner category_spinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<>();
    private List<CategoryDataType> database_list;
    private Context context;
    private PFASampleDataType dataToEdit;


    public AsyncQueryCategoryDialogForEdit(Spinner category_spinner,PFASampleDataType dataToEdit, Context context){
        this.category_spinner=category_spinner;
        this.dataToEdit = dataToEdit;
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
            list.add(s.getCategoryName());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter=new ArrayAdapter (context, R.layout.spinner_row,R.id.spinner_content,list);
        category_spinner.setAdapter(adapter);

        String compareValue = dataToEdit.getTransaction_category();
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            category_spinner.setSelection(spinnerPosition);
        }
    }
}
