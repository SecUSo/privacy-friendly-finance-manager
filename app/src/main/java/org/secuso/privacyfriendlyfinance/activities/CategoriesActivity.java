/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */
package org.secuso.privacyfriendlyfinance.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
 * Activity that shows all categories. Also provides the possibility to create new categories.
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
        new AlertDialog.Builder(this)
                .setTitle(R.string.category_delete_dialog_title)
                .setMessage(HtmlCompat.fromHtml(getResources().getString(R.string.category_delete_question, category.getName()), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    FinanceDatabase.getInstance(this).categoryDao().deleteAsync(category);
                    Toast.makeText(getBaseContext(), R.string.category_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {})
                .create().show();
    }

    private void openCategoryDialog(Category category) {
        CategoryDialog.showCategoryDialog(category, getSupportFragmentManager());
    }

    @Override
    public void onItemClick(CategoryWrapper item) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(CategoryActivity.EXTRA_CATEGORY_ID, item.getId());
        startActivity(intent);
    }
}
