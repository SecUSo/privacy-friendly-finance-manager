package org.secuso.privacyfriendlyfinance.domain.model.common;

import java.util.List;

public abstract class Name2IdCreateIfNotExists<T extends NameWithIdProvider> extends Name2Id<T> {
    public Name2IdCreateIfNotExists(List<T> items) {
        super(items);
    }

    @Override
    public Long get(String name) {
        Long id = super.get(name);
        if (id == null) {
            // not found: must create
            T newItem = createItem();
            newItem.setName(name);
            newItem = save(newItem);
            id = newItem.getId();
            if (id != null) {
                this.name2Id.put(name, id);
            }
        }
        return id;
    }

    abstract protected T createItem();

    abstract protected T save(T newItem);
}
