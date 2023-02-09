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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides translation from "String name" to "Long id"
 * @param <T> where the data comes from
 */
public class Name2Id<T extends NameWithIdProvider>  {
    protected final Map<String,Long> name2Id;

    public Name2Id(List<T> items) {
        Map<String,Long> map = new HashMap<>();
        for (T item : items) {
            if (map.put(item.getName(),item.getId()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        name2Id = map;
    }

    /**
     * Provides translation from "String name" to "Long id".
     *
     * @param name to be translated.
     * @return name corresponding to id or null if not found
     */
    public Long get(String name) {
        return name2Id.get(name);
    }
}
