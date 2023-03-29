/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2023 MaxIsV, k3b

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
