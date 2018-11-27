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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.R.color;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.text.NumberFormat;
import java.util.List;

/**
 * @author David Meiborg
 * Adapter for displaying the transaction list
 */
public class TransactionArrayAdapter extends ArrayAdapter<Transaction> {
    private TextView tvAmount;
    private TextView tvName;
    private TextView tvDate;
    private NumberFormat numberFormat;

    public TransactionArrayAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        getViewElements(convertView);

        fillViewElements(getItem(position));

        return convertView;
    }

    private void getViewElements(View convertView) {
        tvAmount = convertView.findViewById(R.id.listItem_amount);
        tvName = convertView.findViewById(R.id.listItem_name);
        tvDate = convertView.findViewById(R.id.listItem_date);
    }

    private void fillViewElements(Transaction transaction) {
        tvAmount.setText(numberFormat.format(transaction.getAmount()));
        if (transaction.getAmount() < 0) {
            tvAmount.setTextColor(getContext().getResources().getColor(color.red));
        } else {
            tvAmount.setTextColor(getContext().getResources().getColor(color.green));
        }

        tvName.setText(transaction.getName());
        tvName.setGravity(Gravity.CENTER);
        tvName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        tvDate.setText(transaction.getDateAsString());
    }
}
