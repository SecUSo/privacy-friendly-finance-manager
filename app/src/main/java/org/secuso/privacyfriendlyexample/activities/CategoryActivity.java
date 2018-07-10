/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */
package org.secuso.privacyfriendlyexample.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.activities.adapter.CategoryCustomListViewAdapter;
import org.secuso.privacyfriendlyexample.activities.adapter.CustomListViewAdapter;
import org.secuso.privacyfriendlyexample.activities.helper.BaseActivity;
import org.secuso.privacyfriendlyexample.database.CategoryDataType;
import org.secuso.privacyfriendlyexample.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Meiborg on 10.07.2018.
 */

public class CategoryActivity extends BaseActivity {

    private CategorySQLiteHelper myDB;
    private CategoryCustomListViewAdapter adapter;
    private List<CategoryDataType> database_list;
    private ArrayList<CategoryDataType> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        overridePendingTransition(0, 0);

        FloatingActionButton add_category = findViewById(R.id.add_category);
        add_category.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                openCategoryDialog();
            }
        });

        ListView categoryList = (ListView) findViewById(R.id.categoryList);

        myDB = new CategorySQLiteHelper(this);
        database_list = myDB.getAllSampleData();
        list = new ArrayList<>();

        for (CategoryDataType s : database_list){
            list.add(s);
        }

        adapter = new CategoryCustomListViewAdapter(this,list);
        categoryList.setAdapter(adapter);

    }

    private void openCategoryDialog() {
        Dialog_Category dialog = new Dialog_Category();
        dialog.show(getSupportFragmentManager(),"Dialog");
    }


    /**
     * This method connects the Activity to the menu item
     *
     * @return ID of the menu item it belongs to
     */

    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }
}
