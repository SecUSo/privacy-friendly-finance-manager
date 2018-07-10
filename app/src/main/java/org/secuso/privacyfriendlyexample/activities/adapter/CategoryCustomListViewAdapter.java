package org.secuso.privacyfriendlyexample.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.R.color;
import org.secuso.privacyfriendlyexample.database.CategoryDataType;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CategoryCustomListViewAdapter extends ArrayAdapter<CategoryDataType>{


    public CategoryCustomListViewAdapter(Context context, ArrayList<CategoryDataType> list) {
        super(context,0,list);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_category, parent, false);
        }

        CategoryDataType category = getItem(position);

        TextView category_listItem_amount = convertView.findViewById(R.id.category_listItem_amount);
        TextView category_listItem_name = convertView.findViewById(R.id.category_listItem_name);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        category_listItem_name.setText(category.getCategoryName());

        return convertView;
    }



}
