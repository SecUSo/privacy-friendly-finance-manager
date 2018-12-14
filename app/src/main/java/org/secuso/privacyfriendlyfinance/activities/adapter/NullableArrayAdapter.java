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

public class NullableArrayAdapter<T> extends ArrayAdapter<T> {
    private int resource;
    private int fieldId = 0;
    private LayoutInflater inflater;

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
            text.setText(getContext().getResources().getString(R.string.none));
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
