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

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryArrayAdapter;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoriesViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to CRUD categories.
 *
 * @author Felix Hofmann
 */

public class CategoriesActivity extends BaseActivity {
    private CategoriesViewModel categoriesViewModel;

    private ListView listViewCategoryList;
    private FloatingActionButton btAddCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent(R.layout.content_categories);
        btAddCategory = addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryDialog(null);
            }
        });

        listViewCategoryList = findViewById(R.id.categoryList);
        listViewCategoryList.setAdapter(new CategoryArrayAdapter(CategoriesActivity.this,
                new ArrayList<Category>()));
        registerForContextMenu(listViewCategoryList);

        setUpViewModel();
    }

    private void setUpViewModel() {
        categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        categoriesViewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                listViewCategoryList.setAdapter(new CategoryArrayAdapter(CategoriesActivity.this, categories));
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.list_click_menu_category, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Category tmp = categoriesViewModel.getCategories().getValue().get(menuInfo.position);
        if (item.getItemId() == R.id.menuItem_deleteCategory) {
            categoriesViewModel.deleteCategory(tmp);
            Toast.makeText(getApplicationContext(), R.string.toast_delete, Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menuItem_editCategory) {
            openCategoryDialog(tmp);
        }
        return super.onContextItemSelected(item);
    }

    private void openCategoryDialog(Category category) {
        Bundle args = new Bundle();
        if (category == null) {
        } else {
            args.putLong(CategoryDialog.EXTRA_CATEGORY_ID, category.getId());
        }

        CategoryDialog categoryDialog = new CategoryDialog();
        categoryDialog.setArguments(args);

        categoryDialog.show(getSupportFragmentManager(), "CategoryDialog");
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }
}
