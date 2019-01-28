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

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

import java.util.List;

/**
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class NullableArrayAdapter<T> extends ArrayAdapter<T> {
    private int resource;
    private int fieldId = 0;
    private LayoutInflater inflater;
    private String nullPlaceholderString;

    public NullableArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }
    public NullableArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.resource = resource;
        fieldId = textViewResourceId;
        inflater = LayoutInflater.from(context);
    }
    public NullableArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }
    public NullableArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
        fieldId = textViewResourceId;
        inflater = LayoutInflater.from(context);
    }
    public NullableArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }
    public NullableArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
        fieldId = textViewResourceId;
        inflater = LayoutInflater.from(context);
    }

    public NullableArrayAdapter<T> setNullPlaceholderString(String nullPlaceholderString) {
        this.nullPlaceholderString = nullPlaceholderString;
        return this;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(inflater, position, convertView, parent, resource);
    }

    // This method is private in android.widget.ArrayAdapter. IMHO it should be protected.
    protected @NonNull View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                         @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        final View view;
        final TextView text;
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }
        try {
            if (fieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = view.findViewById(fieldId);
                if (text == null) {
                    throw new RuntimeException("Failed to find view with ID "
                            + getContext().getResources().getResourceName(fieldId)
                            + " in item layout");
                }
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }
        final T item = getItem(position);
        text.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
        if (item == null) {
            if (nullPlaceholderString == null) {
                nullPlaceholderString = getContext().getResources().getString(R.string.none);
            }
            text.setText(nullPlaceholderString);
            text.setTextColor(getContext().getResources().getColor(android.R.color.tertiary_text_light));
        } else if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }
        return view;
    }

//    @Override
//    public void setDropDownViewResource(@LayoutRes int resource) {
//        dropDownResource = resource;
//    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        final LayoutInflater ddInflater = mDropDownInflater == null ? inflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent, resource);
    }
}
