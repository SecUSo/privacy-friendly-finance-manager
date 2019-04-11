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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.secuso.privacyfriendlyfinance.R;

/**
 * Dialog to input simple unformatted text.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public abstract class SimpleTextInputDialog extends AppCompatDialogFragment {
    protected EditText editTextInput;

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_simple_text_input, null);
        builder.setView(view);

        getViewElements(view);

        setUpViewElements();

        setUpDialogOptions(builder);

        retrieveData();

        // This line must be at the end in order for data retrieval to be finished at this point
        builder.setTitle(getTitleResourceId());

        return builder.create();
    }

    protected abstract int getTitleResourceId();
    protected abstract void retrieveData();
    protected abstract void onClickPositive(String textFromTextInput);
    protected void onClickNegative() {}
    protected abstract String getTextInputHint();

    private void setUpViewElements() { editTextInput.setHint(getTextInputHint());
    }

    private void getViewElements(View view) {
        editTextInput = view.findViewById(R.id.dialog_simple_input);
    }

    private void setUpDialogOptions(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                onClickNegative();
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                onClickPositive(editTextInput.getText().toString());
            }
        });
    }
}
