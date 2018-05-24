package org.secuso.privacyfriendlyexample.activities.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<PFASampleDataType>{
    ArrayList<PFASampleDataType> list;


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
        TextView listItem_account = (TextView) convertView.findViewById(R.id.listItem_account);
        TextView listItem_date = (TextView) convertView.findViewById(R.id.listItem_date);

        listItem_amount.setText(String.valueOf(transaction.getTransaction_amount()));
        listItem_name.setText(transaction.getTransactionName());
        listItem_account.setText(transaction.getTransaction_account());
        listItem_date.setText(transaction.getTransaction_date());

        return convertView;
    }

}
