package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.Entity;

@Entity(tableName = "Category", inheritSuperIndices=true)
public class Category extends AbstractEntity {
    private String name;

    public Category() {}
    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
