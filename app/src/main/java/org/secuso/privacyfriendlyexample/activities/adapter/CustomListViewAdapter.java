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
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;

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

        TextView listItem_amount = (TextView) convertView.findViewById(R.id.listItem_amount);
        TextView listItem_name = (TextView) convertView.findViewById(R.id.listItem_name);
        TextView listItem_date = (TextView) convertView.findViewById(R.id.listItem_date);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        if(transaction.isTransaction_type()== "Income"){
            listItem_amount.setText(String.valueOf(format.format(transaction.getTransaction_amount())));
            listItem_amount.setTextColor(getContext().getResources().getColor(color.green));
        }
        else{
            listItem_amount.setText(String.valueOf(format.format(transaction.getTransaction_amount())));
            listItem_amount.setTextColor(getContext().getResources().getColor(color.red));
        }

        listItem_name.setText(transaction.getTransactionName());
        listItem_date.setText(transaction.getTransaction_date());

        return convertView;
    }



}
