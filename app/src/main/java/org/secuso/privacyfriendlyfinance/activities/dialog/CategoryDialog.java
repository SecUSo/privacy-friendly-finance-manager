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

package org.secuso.privacyfriendlyfinance.activities.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.CurrencyInputFilter;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoryDialogViewModel;
import org.secuso.privacyfriendlyfinance.databinding.DialogCategoryBinding;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for adding or editing a category.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class CategoryDialog extends AppCompatDialogFragment {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";

    private CategoryDialogViewModel viewModel;
    private List<Category> allCategories = new ArrayList<>();
    private ColorPickerView colorPicker;
    private EditText editTextBudget;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(CategoryDialogViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final DialogCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_category, null, false);
        viewModel.setCategoryId(getArguments().getLong(EXTRA_CATEGORY_ID, -1L)).observe(this, new Observer<Category>() {
            @Override
            public void onChanged(@Nullable Category category) {
                viewModel.setCategory(category);
                binding.setViewModel(viewModel);
            }
        });

        View view = binding.getRoot();
        builder.setView(view);

        builder.setTitle(getArguments().getLong(EXTRA_CATEGORY_ID, -1L) == -1L ? R.string.dialog_category_create_title : R.string.dialog_category_edit_title);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {}
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                viewModel.cancel();
            }
        });

        viewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                synchronized (allCategories) {
                    allCategories = categories;
                }
            }
        });


        colorPicker = view.findViewById(R.id.color_picker_view);
        editTextBudget = view.findViewById(R.id.editText_budget);

        editTextBudget.setFilters(new InputFilter[] {new CurrencyInputFilter(0.0, null)});

        if (viewModel.getColor() != null) colorPicker.setInitialColor(viewModel.getColor(), false);
        colorPicker.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                if (i == -1) {
                    viewModel.setColor(null);
                } else {
                    viewModel.setColor(i);
                }
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCategoryName()) {
                    viewModel.submit();
                    Toast.makeText(getContext(), R.string.category_saved_msg, Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                if (validateCategoryName()) {
//                    viewModel.submit();
//                    Toast.makeText(getContext(), R.string.category_saved_msg, Toast.LENGTH_SHORT).show();
//                } else {
//
//                }
//            }
        });
        return dialog;
    }

    private boolean validateCategoryName() {
        synchronized (allCategories) {
            if (viewModel.getName() == null || viewModel.getName().isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.dialog_category_empty_name_impossible_msg), Toast.LENGTH_LONG).show();
                return false;
            }

            if (allCategories == null || allCategories.isEmpty()) {
                return true;
            }
            for (int i = 0; i < allCategories.size(); i++) {
                if (allCategories.get(i).getName().toLowerCase().equals(viewModel.getName().toLowerCase()) && allCategories.get(i).getId() != viewModel.getCategory().getId()) {
                    Toast.makeText(getContext(), getString(R.string.dialog_category_name_taken_msg), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            return true;
        }
    }
}
