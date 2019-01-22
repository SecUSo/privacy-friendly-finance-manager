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

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoriesAdapter;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryWrapper;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.dialog.CategoryDialog;
import org.secuso.privacyfriendlyfinance.activities.helper.SwipeController;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoriesViewModel;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

/**
 * Activity to CRUD categories.
 *
 * @author Felix Hofmann
 */

public class CategoriesActivity extends BaseActivity implements OnItemClickListener<CategoryWrapper> {
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private CategoriesViewModel viewModel;
    private SwipeController swipeController = null;


    @Override
    protected Class<? extends BaseViewModel> getViewModelClass() {
        return CategoriesViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent(R.layout.content_recycler);

        viewModel = (CategoriesViewModel) super.viewModel;
        categoriesAdapter = new CategoriesAdapter(this, viewModel.getCategories());
        categoriesAdapter.onItemClick(this);

        addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryDialog(null);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoriesAdapter);

        emptyView = findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.activity_categories_empty_list_label));
        viewModel.getCategories().observe(this, new Observer<List<CategoryWrapper>>() {
            @Override
            public void onChanged(@Nullable List<CategoryWrapper> categoryWrappers) {
                if (categoryWrappers.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

        SwipeController.SwipeControllerAction deleteAction = new SwipeController.SwipeControllerAction() {
            @Override
            public void onClick(int position) {
                deleteCategory(viewModel.getCategories().getValue().get(position).getCategory());
            }
            @Override
            public Drawable getIcon() {
                return ContextCompat.getDrawable(CategoriesActivity.this, R.drawable.ic_delete_red_24dp);
            }
        };

        swipeController = new SwipeController(this, deleteAction, deleteAction);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

    }

    public void deleteCategory(final Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.category_delete_dialog_title);
        builder.setMessage(Html.fromHtml(getResources().getString(R.string.category_delete_question, category.getName())));
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FinanceDatabase.getInstance().categoryDao().deleteAsync(category);
                Toast.makeText(getBaseContext(), R.string.category_deleted, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
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
    public void onItemClick(CategoryWrapper item) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(CategoryActivity.EXTRA_CATEGORY_ID, item.getId());
        startActivity(intent);
    }
}
