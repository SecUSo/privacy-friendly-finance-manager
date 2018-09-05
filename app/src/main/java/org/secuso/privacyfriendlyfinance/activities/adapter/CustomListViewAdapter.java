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
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<PFASampleDataType>{


    public CustomListViewAdapter( Context context, ArrayList<PFASampleDataType> list) {
        super(context,0,list);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        PFASampleDataType transaction = getItem(position);

        TextView listItem_amount = convertView.findViewById(R.id.listItem_amount);
        TextView listItem_name = convertView.findViewById(R.id.listItem_name);
        TextView listItem_date = convertView.findViewById(R.id.listItem_date);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        if (transaction.isTransaction_type()==0){
            listItem_amount.setTextColor(getContext().getResources().getColor(color.red));
            listItem_amount.setText(format.format(transaction.getTransaction_amount()));
        }else{
            listItem_amount.setTextColor(getContext().getResources().getColor(color.green));
            listItem_amount.setText(format.format(transaction.getTransaction_amount()));
        }

        listItem_name.setText(transaction.getTransactionName());

        listItem_date.setText(transaction.getTransaction_date());

        return convertView;
    }



}
