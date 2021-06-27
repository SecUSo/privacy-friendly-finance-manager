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

package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Bass class for view models.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class BaseViewModel extends AndroidViewModel {
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<Integer> titleId = new MutableLiveData<>();
    private MutableLiveData<Integer> navigationDrawerId = new MutableLiveData<>();
    private boolean showEditMenu;

    public void setShowEditMenu(boolean showEditMenu) {
        this.showEditMenu = showEditMenu;
    }
    public boolean doShowEditMenu() {
        return showEditMenu;
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getTitle() {
        return title;
    }    public LiveData<Integer> getTitleId() {
        return titleId;
    }
    public void setTitle(String title) {
        this.title.postValue(title);
    }
    public void setTitle(@StringRes int titleId) {
        this.titleId.postValue(titleId);
    }

    public LiveData<Integer> getNavigationDrawerId() {
        return navigationDrawerId;
    }

    public void setNavigationDrawerId(Integer navigationDrawerId) {
        this.navigationDrawerId.setValue(navigationDrawerId);
    }

    public boolean showDrawer() {
        return true;
    }
}
