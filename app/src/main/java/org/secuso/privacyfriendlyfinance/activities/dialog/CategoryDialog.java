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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoryDialogViewModel;
import org.secuso.privacyfriendlyfinance.databinding.DialogCategoryBinding;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for adding or editing a category.
 *
 * @author Felix Hofmann, Leonard Otto
 */
public class CategoryDialog extends AppCompatDialogFragment {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";

    private CategoryDialogViewModel viewModel;
    private List<Category> allCategories = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(CategoryDialogViewModel.class);
        viewModel.setCategoryId(getArguments().getLong(EXTRA_CATEGORY_ID, -1L));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final DialogCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_category, null, false);
        View view = binding.getRoot();
        binding.setViewModel(viewModel);
        builder.setView(view);

        builder.setTitle(viewModel.isNewCategory() ? R.string.dialog_category_create_title : R.string.dialog_category_edit_title);

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

        AlertDialog dialog  = builder.create();
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



//
//    @Override
//    protected void retrieveData() {
//        viewModel = ViewModelProviders.of(this).get(CategoryDialogViewModel.class);
//
//        Bundle args = getArguments();
//        long categoryId = args.getLong(EXTRA_CATEGORY_ID, -1L);
//        if (categoryId == -1L) {
//            editingExistingCategory = false;
//        } else {
//            editingExistingCategory = true;
//            viewModel.getCategoryById(categoryId).observe(this, new Observer<Category>() {
//                @Override
//                public void onChanged(@Nullable Category category) {
//                    editTextInput.setText(category.getName());
//                    categoryObject = category;
//                }
//            });
//        }

//    }

//    @Override
//    protected void onClickPositive(String textFromTextInput) {
//        String categoryName = textFromTextInput.trim();
//        if (categoryName.isEmpty()) {
//            Toast.makeText(getContext(), getString(R.string.dialog_category_empty_name_impossible_msg), Toast.LENGTH_LONG).show();
//        } else if (isCategoryNameTaken(categoryName)) {
//            Toast.makeText(getContext(), getString(R.string.dialog_category_name_taken_msg), Toast.LENGTH_LONG).show();
//        } else {
//            if (categoryObject == null) {
//                //We are working on a new category object and are NOT editing an existing one
//                categoryObject = new Category(categoryName);
//            } else {
//                categoryObject.setName(categoryName);
//            }
//            viewModel.updateOrInsert(categoryObject);
//
//            Toast.makeText(getContext(), R.string.category_saved_msg, Toast.LENGTH_SHORT).show();
//
//            dismiss();
//        }
//    }

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
                if (allCategories.get(i).getName().toLowerCase().equals(viewModel.getName().toLowerCase())) {
                    Toast.makeText(getContext(), getString(R.string.dialog_category_name_taken_msg), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            return true;
        }
    }
}
