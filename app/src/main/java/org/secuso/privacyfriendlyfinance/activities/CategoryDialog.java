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
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoryDialogViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

/**
 * Dialog for adding or editing a category.
 *
 * @author Felix Hofmann, Leonard Otto
 */
public class CategoryDialog extends SimpleTextInputDialog {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";

    private CategoryDialogViewModel viewModel;

    @Override
    protected void retrieveData() {
        viewModel = ViewModelProviders.of(this).get(CategoryDialogViewModel.class);

        Bundle args = getArguments();
        long categoryId = args.getLong(EXTRA_CATEGORY_ID, -1L);
        if (categoryId == -1L) {
        } else {
            viewModel.getCategoryById(categoryId).observe(this, new Observer<Category>() {
                @Override
                public void onChanged(@Nullable Category category) {
                    editTextInput.setText(category.getName());
                }
            });
        }
    }

    @Override
    protected void onClickPositive(String textFromTextInput) {
        String categoryName = textFromTextInput.trim();
        if (categoryName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.dialog_category_empty_name_impossible_msg), Toast.LENGTH_LONG).show();
        } else {
            Category tmpCategory = new Category(categoryName);

            viewModel.updateOrInsert(tmpCategory);

            Toast.makeText(getContext(), R.string.activity_transaction_saved_msg, Toast.LENGTH_SHORT).show();

            dismiss();
        }
    }

    @Override
    protected String getTextInputHint() {
        return getResources().getString(R.string.dialog_category_name_hint);
    }
}
