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

package org.secuso.privacyfriendlyfinance.activities;

import android.os.Bundle;
import android.widget.ExpandableListView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.HelpListExpandableListAdapter;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;

/**
 * Help activity. Shows the help faq.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class HelpActivity extends BaseActivity {
    @Override
    protected Class<? extends BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_help);

        getViewModel().setNavigationDrawerId(R.id.nav_help);

        String[] questions = getResources().getStringArray(R.array.activity_help_questions);
        String[] answers = getResources().getStringArray(R.array.activity_help_answers);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView_questionList);
        HelpListExpandableListAdapter expandableListAdapter = new HelpListExpandableListAdapter(this, questions, answers);
        expandableListView.setAdapter(expandableListAdapter);
    }
}
