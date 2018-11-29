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
import org.secuso.privacyfriendlyfinance.activities.adapter.HelpExpandableListAdapter;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author David Meiborg
 * Activity that gives the user some help regarding the handling of the app.
 *
 */
public class HelpActivity extends BaseActivity {

    @Override
    protected Class<? extends BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        LinkedHashMap<String, List<String>> expandableListDetail = buildData();

        ExpandableListView generalExpandableListView = findViewById(R.id.generalExpandableListView);
        generalExpandableListView.setAdapter(new HelpExpandableListAdapter(this, new ArrayList<>(expandableListDetail.keySet()), expandableListDetail));

        overridePendingTransition(0, 0);
    }

    private LinkedHashMap<String, List<String>> buildData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        expandableListDetail.put(getString(R.string.help_whatis), Collections.singletonList(getString(R.string.help_whatis_answer)));
        expandableListDetail.put(getString(R.string.help_feature_one), Collections.singletonList(getString(R.string.help_feature_one_answer)));
        expandableListDetail.put(getString(R.string.help_feature_two), Collections.singletonList(getString(R.string.help_feature_two_answer)));
        expandableListDetail.put(getString(R.string.help_privacy), Collections.singletonList(getString(R.string.help_privacy_answer)));
        expandableListDetail.put(getString(R.string.help_permission), Collections.singletonList(getString(R.string.help_permission_answer)));

        return expandableListDetail;
    }

    protected int getNavigationDrawerID() {
        return R.id.nav_help;
    }

}
