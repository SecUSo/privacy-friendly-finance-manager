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

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

/**
 * Dialog for adding or editing a category.
 *
 * @author Felix Hofmann, Leonard Otto
 */
public class CategoryDialog extends AppCompatDialogFragment {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";

    private CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();

    private Category category;
    private EditText editTextName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_category, null);
        builder.setView(view);

        getViewElements(view);

        // Title
        long id = getArguments().getLong(EXTRA_CATEGORY_ID, -1L);
        if (id == -1L) {
            builder.setTitle(R.string.dialog_category_title);
        } else {
            builder.setTitle(R.string.dialog_category_edit);
            categoryDao.get(id).observe(this, new Observer<Category>() {
                @Override
                public void onChanged(@Nullable Category category) {
                    CategoryDialog.this.category = category;
                    editTextName.setText(category.getName());
                }
            });
        }

        setUpDialogOptions(builder);

        return builder.create();
    }

    private void getViewElements(View view) {
        editTextName = view.findViewById(R.id.dialog_category_name);
    }

    private void saveCategory() {
        String categoryName = editTextName.getText().toString().trim();
        if (categoryName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.dialog_category_toast), Toast.LENGTH_LONG).show();
        } else {
            category.setName(categoryName);

            categoryDao.updateOrInsertAsync(category, null);

            Toast.makeText(getContext(), R.string.toast_new_entry, Toast.LENGTH_SHORT).show();

            //TODO: Is this really done like this?
            Intent intent = new Intent(getActivity(), CategoriesActivity.class);
            startActivity(intent);
        }
    }

    private void setUpDialogOptions(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.transaction_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.setPositiveButton(R.string.transaction_dialog_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                saveCategory();
            }
        });
    }
}
