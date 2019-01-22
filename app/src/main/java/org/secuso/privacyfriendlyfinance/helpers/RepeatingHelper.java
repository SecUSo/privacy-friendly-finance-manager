package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

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

        if (repeatingTransaction.getWeekly()) {
            if (repeatingTransaction.getInterval() == 1) {
                template = template.replace("%UNIT%", context.getString(R.string.repeat_unit_every_first_week));
            } else {
                String week = context.getString(R.string.repeat_unit_every_n_week);
                week = week.replace("%N%", String.valueOf(repeatingTransaction.getInterval()));
                template = template.replace("%UNIT%", week);
            }
        } else {
            if (repeatingTransaction.getInterval() == 1) {
                template = template.replace("%UNIT%", context.getString(R.string.repeat_unit_every_first_month));
            } else {
                String month = context.getString(R.string.repeat_unit_every_n_month);
                month = month.replace("%N%", String.valueOf(repeatingTransaction.getInterval()));
                template = template.replace("%UNIT%", month);
            }
        }

        return template;
    }
}
