/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2026

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 */

package org.secuso.privacyfriendlyfinance.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Provides currency selection options used by settings and account dialogs.
 */
public final class CurrencyConfigHelper {
    private CurrencyConfigHelper() {}

    public static class CurrencyOption {
        private final String code;
        private final String label;

        public CurrencyOption(String code) {
            this.code = code;
            Currency currency = Currency.getInstance(code);
            this.label = code + " - " + currency.getDisplayName(Locale.getDefault());
        }

        public CurrencyOption(String code, String label) {
            this.code = code;
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public static List<CurrencyOption> getCurrencyOptions() {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        List<CurrencyOption> options = new ArrayList<>(currencies.size());
        for (Currency currency : currencies) {
            options.add(new CurrencyOption(currency.getCurrencyCode()));
        }
        Collections.sort(options, new Comparator<CurrencyOption>() {
            @Override
            public int compare(CurrencyOption left, CurrencyOption right) {
                return left.getCode().compareTo(right.getCode());
            }
        });
        return options;
    }

    public static List<CurrencyOption> getCurrencyOptionsOrdered(String[] recentCodes) {
        List<CurrencyOption> all = getCurrencyOptions();
        LinkedHashSet<String> orderedCodes = new LinkedHashSet<>();
        orderedCodes.add("EUR");
        orderedCodes.add("USD");
        if (recentCodes != null) {
            for (String code : recentCodes) {
                if (code != null && !code.trim().isEmpty()) {
                    orderedCodes.add(code.trim().toUpperCase(Locale.ROOT));
                }
            }
        }

        List<CurrencyOption> ordered = new ArrayList<>();
        for (String code : orderedCodes) {
            int index = indexOfCode(all, code);
            if (index >= 0 && index < all.size() && all.get(index).getCode().equalsIgnoreCase(code)) {
                ordered.add(all.get(index));
            }
        }

        for (CurrencyOption option : all) {
            if (!orderedCodes.contains(option.getCode().toUpperCase(Locale.ROOT))) {
                ordered.add(option);
            }
        }
        return ordered;
    }

    public static int indexOfCode(List<CurrencyOption> options, String code) {
        if (options == null || code == null) {
            return -1;
        }
        for (int i = 0; i < options.size(); i++) {
            if (code.equalsIgnoreCase(options.get(i).getCode())) {
                return i;
            }
        }
        return -1;
    }
}
