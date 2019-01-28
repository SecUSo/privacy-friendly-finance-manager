package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

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
