/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

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

package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

/**
 * Helper class that forges string representations of repeating intervals for repeating transactions.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public final class RepeatingHelper {
    private RepeatingHelper() {}

    public static String forgeRepeatingText(Context context, RepeatingTransaction repeatingTransaction) {
        String template = null;
        if (repeatingTransaction.getEnd() != null) {
            // Not forever
            template = context.getString(R.string.repeat_text_unit_until_template);
            template = template.replace("%UNTIL%", repeatingTransaction.getEnd().toString());
        } else {
            //Forever
            template = context.getString(R.string.repeat_text_unit_template);
        }

        if (repeatingTransaction.isWeekly()) {
            if (repeatingTransaction.getInterval() == 1) {
                template = template.replace("%UNIT%", context.getString(R.string.repeat_unit_every_first_week));
            } else {
                String week = context.getString(R.string.repeat_unit_every_n_week);
                week = week.replace("%N%", String.valueOf(repeatingTransaction.getInterval()) + ".");
                template = template.replace("%UNIT%", week);
            }
        } else {
            if (repeatingTransaction.getInterval() == 1) {
                template = template.replace("%UNIT%", context.getString(R.string.repeat_unit_every_first_month));
            } else {
                String month = context.getString(R.string.repeat_unit_every_n_month);
                month = month.replace("%N%", String.valueOf(repeatingTransaction.getInterval()) + ".");
                template = template.replace("%UNIT%", month);
            }
        }

        return template;
    }
}
