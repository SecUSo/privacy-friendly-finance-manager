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
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryArrayAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to CRUD categories.
 *
 * @author Felix Hofmann
 */

public class CategoriesActivity extends BaseActivity implements TaskListener {
    private final CategoryDao dao = FinanceDatabase.getInstance().categoryDao();
    private ListView categoryList;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        findViewById(R.id.add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryDialog(null);
            }
        });

        categoryList = findViewById(R.id.categoryList);
        registerForContextMenu(categoryList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dao.getAllAsync(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.list_click_menu_category, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.listDeleteCategory) {
            dao.deleteAsync(categories.get(menuInfo.position));
            Toast.makeText(getApplicationContext(), R.string.toast_delete, Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.listEditCategory) {
            openCategoryDialog(categories.get(menuInfo.position));
        }
        return super.onContextItemSelected(item);
    }

    private void openCategoryDialog(Category category) {
        //TODO: remove commented code
//        CategoryDialog dialog = new CategoryDialog();
//        Bundle args = new Bundle();
//        if (category != null) args.putLong("categoryId", category.getId());
//        dialog.setArguments(args);
//        dialog.show(getSupportFragmentManager(), "Dialog");

        Bundle args = new Bundle();
        args.putLong("categoryId", category.getId());
        args.putString("categoryName", category.getName());

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }


    @Override
    public void onDone(Object result, AsyncTask<?, ?, ?> task) {
        categories = new ArrayList<>((List<Category>) result);
        categoryList.setAdapter(new CategoryArrayAdapter(this, categories));
    }
}
