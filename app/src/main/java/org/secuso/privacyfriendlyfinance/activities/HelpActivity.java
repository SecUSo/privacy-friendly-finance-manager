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

package org.secuso.privacyfriendlyfinance.activities;

import android.os.Bundle;
import android.widget.ExpandableListView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.HelpListExpandableListAdapter;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;

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
