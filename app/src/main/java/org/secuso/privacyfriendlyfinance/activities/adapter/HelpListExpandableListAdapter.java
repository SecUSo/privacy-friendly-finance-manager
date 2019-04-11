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
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

/**
 * Expandable list adapter used for the help activity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class HelpListExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private String[] questions;
    private String[] answers;

    public HelpListExpandableListAdapter(Context context, String[] questions, String[] answers) {
        this.context = context;
        this.questions = questions;
        this.answers = answers;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String answer = answers[listPosition];
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.helplist_list_item, null);
        }
        TextView tvExpandedAnswer = convertView.findViewById(R.id.tv_expanded_answer);
        tvExpandedAnswer.setText(answer);

        return convertView;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String question = questions[listPosition];
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.helplist_list_group, null);
        }
        TextView tvQuestion = convertView.findViewById(R.id.helpList_group_title);
        tvQuestion.setTypeface(null, Typeface.BOLD);
        tvQuestion.setText(question);
        return convertView;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return answers[listPosition];
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return questions[listPosition];
    }

    @Override
    public int getGroupCount() {
        return questions.length;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
