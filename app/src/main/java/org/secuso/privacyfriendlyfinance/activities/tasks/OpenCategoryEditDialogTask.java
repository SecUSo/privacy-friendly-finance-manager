package org.secuso.privacyfriendlyfinance.activities.tasks;


import android.os.AsyncTask;

import org.secuso.privacyfriendlyfinance.activities.CategoryActivity;
import org.secuso.privacyfriendlyfinance.activities.CategoryEditDialog;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;

import java.util.List;

/**
 * This class opens the Edit Dialog with an AsyncTask
 *
 */
public class OpenCategoryEditDialogTask extends AsyncTask<Void,Void,Void> {
    private CategorySQLiteHelper myDB;
    private CategoryActivity context;
    private CategoryDataType categoryObject;
    private int categoryObjectIndex;

    public OpenCategoryEditDialogTask(CategoryActivity context, int categoryObjectIndex) {
        this.context = context;
        this.categoryObjectIndex = categoryObjectIndex;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        myDB = new CategorySQLiteHelper(context);
        List<CategoryDataType> databaseList = myDB.getAllSampleData();

        if (categoryObjectIndex < 0 || categoryObjectIndex >= databaseList.size()) {
            // Should not be possible
            throw new IllegalArgumentException("Illegal index for category object! Index: " + categoryObjectIndex);
        }

        categoryObject = databaseList.get(categoryObjectIndex);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        CategoryEditDialog dialog = new CategoryEditDialog(categoryObject, context);
        dialog.show(context.getSupportFragmentManager(),"EditDialog");
    }
}