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
package org.secuso.privacyfriendlyfinance.activities;

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
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryArrayAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.BaseActivity;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for categories.
 *
 * @author David Meiborg
 */

public class CategoryActivity extends BaseActivity implements TaskListener {
    private final CategoryDao dao = FinanceDatabase.getInstance().categoryDao();
    private ListView categoryList;
    private ArrayList<Category> categories;


    @Override
    protected void onResume() {
        super.onResume();
        categoryList = findViewById(R.id.categoryList);
        dao.getAllAsync(this);
//        new AsyncQueryCategory(categoryList, this).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    /**
     * This method creates the content for the activity
     * FAB, the list of categories and the contextMenu for the list entries
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        overridePendingTransition(0, 0);

        FloatingActionButton add_category = findViewById(R.id.add_category);
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryDialog(null);
            }
        });

        categoryList = findViewById(R.id.categoryList);
        registerForContextMenu(categoryList);
    }

    /**
     * opens menu for delete or edit categories
     *
     * @return void
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_click_menu_category, menu);
    }

    /**
     * actions when menu item is selected for delete or edit categories
     *
     * @return void
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.listDeleteCategory) {

            Category category = categories.get(menuInfo.position);
    //                TODO delete category
            Toast.makeText(getApplicationContext(), R.string.toast_delete, Toast.LENGTH_SHORT).show();

            Intent categoryActivity = new Intent(getBaseContext(), CategoryActivity.class);
            startActivity(categoryActivity);
        }
        if (item.getItemId() == R.id.listEditCategory) {
            Category category = categories.get(menuInfo.position);
            openCategoryDialog(categories.get(menuInfo.position));
        }

        return super.onContextItemSelected(item);
    }

    private void openCategoryDialog(Category category) {
        CategoryDialog dialog = new CategoryDialog();
        Bundle args = new Bundle();
        if (category != null) args.putLong("categoryId", category.getId());
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    /**
     * This method connects the Activity to the menu item
     *
     * @return ID of the menu item it belongs to
     */

    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }

    @Override
    public void onDone(Object result, AsyncTask<?, ?, ?> task) {
        categories = new ArrayList<>((List<Category>) result);
        categoryList.setAdapter(new CategoryArrayAdapter(this, categories));
    }

    @Override
    public void onProgress(Double progress, AsyncTask<?, ?, ?> task) {

    }

    @Override
    public void onOperation(String operation, AsyncTask<?, ?, ?> task) {

    }
}
