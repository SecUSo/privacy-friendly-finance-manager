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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private final CategoryDao dao = FinanceDatabase.getInstance().categoryDao();
    private Category category;
    private EditText nameEditText;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_category, null);
        nameEditText = view.findViewById(R.id.dialog_category_name);

        builder.setView(view);
        long id = -1L;
        if (savedInstanceState != null) savedInstanceState.getLong("categoryId", -1L);
        if (id == -1L) {
             builder.setTitle(R.string.dialog_category_title);
             category = new Category();
        } else {
            builder.setTitle(R.string.dialog_category_edit);
            category = dao.get(id);
        }

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
                //defines what happens when dialog is submitted
        builder.setPositiveButton(R.string.dialog_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String categoryName = nameEditText.getText().toString().trim();
                if (categoryName.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.dialog_category_toast), Toast.LENGTH_LONG).show();
                } else {
                    category.setName(categoryName);
                    dao.upsertAsync(category, null);
                    Toast.makeText(getContext(), R.string.toast_new_entry, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    startActivity(intent);
                }
            }
        });

        return builder.create();
    }


}
