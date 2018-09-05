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
package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.R.color;
import org.secuso.privacyfriendlyfinance.database.CategoryDataType;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

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

        PFASQLiteHelper myDB = new PFASQLiteHelper(getContext());

        CategoryDataType category = getItem(position);

        TextView category_listItem_amount = convertView.findViewById(R.id.category_listItem_amount);
        TextView category_listItem_name = convertView.findViewById(R.id.category_listItem_name);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        category_listItem_name.setText(category.getCategoryName());

        Double amount = myDB.getBalanceByCategory(category.getCategoryName());

        if (amount<0){
            category_listItem_amount.setTextColor(getContext().getResources().getColor(color.red));
            category_listItem_amount.setText(format.format(amount));
        }else{
            category_listItem_amount.setTextColor(getContext().getResources().getColor(color.green));
            category_listItem_amount.setText(format.format(amount));
        }

        return convertView;
    }

}
