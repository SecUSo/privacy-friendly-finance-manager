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

import org.secuso.privacyfriendlyfinance.activities.viewmodel.MainViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;

public class MainActivity extends TransactionListActivity {
    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return MainViewModel.class;
    }
}
