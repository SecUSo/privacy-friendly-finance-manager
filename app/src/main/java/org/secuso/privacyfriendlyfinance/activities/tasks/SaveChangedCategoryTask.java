package org.secuso.privacyfriendlyfinance.activities.tasks;


import android.content.Context;
import android.os.AsyncTask;

import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;

import java.util.ArrayList;
import java.util.List;

/**
 * This nested class opens the Edit Dialog with an AsyncTask
 */
public class SaveChangedCategoryTask extends AsyncTask<Void, Void, Void> {
    CategoryDataType data;
    PFASQLiteHelper db;
    private CategoryDataType categoryObject;
    private String newCategoryName;

    public SaveChangedCategoryTask(CategoryDataType categoryObject, String newCategoryName) {
        this.categoryObject = categoryObject;
        this.newCategoryName = newCategoryName;
    }

    @Override
    protected Void doInBackground(Void... voids) {
//        myDB = new CategorySQLiteHelper(context);
//        List<CategoryDataType> database_list = myDB.getAllSampleData();
//        list = new ArrayList<>();
//
//        for (CategoryDataType s : database_list) {
//            list.add(s);
//        }
//
//        db = new PFASQLiteHelper(context);
//        List<PFASampleDataType> db_list = db.getAllSampleData();
//
//        for (PFASampleDataType p : db_list) {
//            if (p.getTransaction_category().equals(data.getCategoryName())) {
//                p.setTransaction_category(categoryName);
//                db.updateSampleData(p);
//            }
//        }
//
//        myDB.updateSampleData(new CategoryDataType(dataToEdit.getID(), categoryName));


        //TODO: Retrieve category dao!
        CategoryDao categoryDao = null;


        //TODO: Change to correct data type
        categoryDao.updateCategory(null);
//        categoryDao.updateCategory(categoryObject);


        return null;
    }
}