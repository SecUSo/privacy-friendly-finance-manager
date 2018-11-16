package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.PrimaryKey;

public abstract class AbstractEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
