/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019-2023 Leonard Otto, Felix Hofmann, k3b

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
import java.util.stream.Collectors;

/**
 * Provides translation from "Long id" to "String name"
 * @param <T> where the data comes from
 */
public class Id2Name <T extends NameWithIdProvider>  {
    private final Map<Long, String> id2Name;

    public Id2Name(List<T> items) {
        Map<Long, String> map = new HashMap<>();
        for (T item : items) {
            if (map.put(item.getId(), item.getName()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        id2Name = map;
    }

    /**
     * Provides translation from "Long id" to "String name".
     *
     * @param id to be translated.
     * @return name corresponding to id or null if not found
     */
    public String get(Long id) {
        return id2Name.get(id);
    }
}
