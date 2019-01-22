package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.PrimaryKey;

import org.secuso.privacyfriendlyfinance.activities.adapter.IdProvider;



public abstract class AbstractEntity implements IdProvider {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
