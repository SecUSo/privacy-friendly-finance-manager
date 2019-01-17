package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(
    tableName = "Category",
    inheritSuperIndices = true,
    indices = @Index(value = "name", unique = true)
)
public class Category extends AbstractEntity {
    private String name;
    private Long budget;
    private Integer color;

    public Category() {}
    @Ignore
    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }
    public Integer getColor() {
        return color;
    }
    public void setColor(Integer color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }
}
