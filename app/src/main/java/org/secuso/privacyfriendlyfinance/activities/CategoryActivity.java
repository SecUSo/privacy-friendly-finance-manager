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
package org.secuso.privacyfriendlyfinance.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryCustomListViewAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.BaseActivity;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.CategorySQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryCategory;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryDeleteCategory;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryUpdateCategory;

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
    protected void onResume() {
        super.onResume();
        ListView categoryList = (ListView) findViewById(R.id.categoryList);
        new AsyncQueryCategory(categoryList,this).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

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

        new AsyncQueryCategory(categoryList,this).execute();

        registerForContextMenu(categoryList);

    }

    //opens menu for delete or edit categories
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_click_menu_category,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(item.getItemId()==R.id.listDeleteCategory){
            if(info.position==0){
                Toast.makeText(CategoryActivity.this,R.string.category_deletion_standard, Toast.LENGTH_SHORT).show();
            }
            else{
                new AsyncQueryDeleteCategory(info.position,CategoryActivity.this).execute();

                Toast.makeText(getApplicationContext(), R.string.toast_delete, Toast.LENGTH_SHORT).show();

            }

            Intent categoryActivity = new Intent(getBaseContext(),CategoryActivity.class);
            startActivity(categoryActivity);
        }
        if (item.getItemId()==R.id.listEditCategory){
            if(info.position==0){
                Toast.makeText(CategoryActivity.this,R.string.category_deletion_standard, Toast.LENGTH_SHORT).show();
            }
            else{
                new AsyncQueryUpdateCategoryOpenDialog(info.position,CategoryActivity.this).execute();
            }
        }


        return super.onContextItemSelected(item);
    }

    /**
     * This method opens the Dialog for adding a Category
     *
     * @return void
     */

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

    /**
     * This nested class opens the Edit Dialog with an AsyncTask
     *
     */
    private class AsyncQueryUpdateCategoryOpenDialog extends AsyncTask<Void,Void,Void> {

        private ArrayList<CategoryDataType> list = new ArrayList<>();
        int position;
        CategorySQLiteHelper myDB;
        Context context;


        public AsyncQueryUpdateCategoryOpenDialog(int position, Context context){
            this.position=position;
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
            myDB = new CategorySQLiteHelper(context);
            List<CategoryDataType> database_list = myDB.getAllSampleData();
            list = new ArrayList<>();

            for (CategoryDataType s : database_list){
                list.add(s);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Dialog_Category_Edit dialog = new Dialog_Category_Edit(list.get(position),context);
            dialog.show(getSupportFragmentManager(),"EditDialog");
        }
    }
}
