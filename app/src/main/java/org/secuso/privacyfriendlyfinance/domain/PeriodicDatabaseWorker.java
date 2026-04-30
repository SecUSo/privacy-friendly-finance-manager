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

package org.secuso.privacyfriendlyfinance.domain;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.domain.access.RepeatingTransactionDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.helpers.SharedPreferencesManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Periodic database worker. Works in the background.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class PeriodicDatabaseWorker {
    public static final long DURATION_BETWEEN_WORK = TimeUnit.MINUTES.toMillis(5);

    private PeriodicDatabaseWorker() {}

    public static void work(Context context, FinanceDatabase database) {
        PeriodicDatabaseTask periodicDatabaseTask = new PeriodicDatabaseTask(context, database);
        periodicDatabaseTask.execute();
    }

    private static class PeriodicDatabaseTask extends CommunicantAsyncTask<Void, Void> {

        private final RepeatingTransactionDao repeatingTransactionDao;
        private final TransactionDao transactionDao;
        private final SharedPreferencesManager sharedPreferencesManager;

        public PeriodicDatabaseTask(Context context, FinanceDatabase database) {
            repeatingTransactionDao = database.repeatingTransactionDao();
            transactionDao = database.transactionDao();
            sharedPreferencesManager = SharedPreferencesManager.get(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<RepeatingTransaction> repeatingTransactions = repeatingTransactionDao.getAllSync();
            for (RepeatingTransaction r : repeatingTransactions) {
                // Add all transactions for this repeating transaction
                // (there can be more than one to add for one repeating transaction)
                while (handleRepeatingTransaction(r)) {}
            }

            return null;
        }

        private boolean handleRepeatingTransaction(RepeatingTransaction repeatingTransaction) {
            // Calculate the local date for the next insert
            if (repeatingTransaction.getLatestInsert() == null) {
                repeatingTransaction.setLatestInsert(LocalDate.now());
            }
            long interval = repeatingTransaction.getInterval();
            if (interval < 1) {
                interval = 1;
            }
            LocalDate nextInsert;
            if (repeatingTransaction.isWeekly()) {
                long repeatingId = repeatingTransaction.getId() == null ? -1L : repeatingTransaction.getId();
                int dayOfWeek = sharedPreferencesManager.getRepeatingWeekDay(
                        repeatingId,
                        repeatingTransaction.getLatestInsert().getDayOfWeek());
                Period weeks = Period.weeks((int) interval);
                LocalDate latestInsert = repeatingTransaction.getLatestInsert();
                nextInsert = latestInsert.plus(weeks).withDayOfWeek(dayOfWeek);
                while (!nextInsert.isAfter(latestInsert)) {
                    nextInsert = nextInsert.plus(weeks);
                }
            } else {
                long repeatingId = repeatingTransaction.getId() == null ? -1L : repeatingTransaction.getId();
                int dayOfMonth = sharedPreferencesManager.getRepeatingMonthDay(
                        repeatingId,
                        repeatingTransaction.getLatestInsert().getDayOfMonth());
                Period months = Period.months((int) interval);
                LocalDate latestInsert = repeatingTransaction.getLatestInsert();
                LocalDate base = latestInsert.plus(months).withDayOfMonth(1);
                int maxDayOfMonth = base.dayOfMonth().getMaximumValue();
                nextInsert = base.withDayOfMonth(Math.min(dayOfMonth, maxDayOfMonth));
                while (!nextInsert.isAfter(latestInsert)) {
                    base = base.plus(months).withDayOfMonth(1);
                    maxDayOfMonth = base.dayOfMonth().getMaximumValue();
                    nextInsert = base.withDayOfMonth(Math.min(dayOfMonth, maxDayOfMonth));
                }
            }

            // Does the repeating transaction have an end?
            if (repeatingTransaction.getEnd() != null) {
                // Is the end before the calculated 'nextInsert'?
                if (repeatingTransaction.getEnd().isBefore(nextInsert)) {
                    return false;
                }
            }

            // Is 'nextInsert' before 'now' or is 'nextInsert' today?
            LocalDate now = LocalDate.now();
            if (nextInsert.isBefore(now) || nextInsert.isEqual(now)) {
                // Insert a new transaction
                Transaction newTransaction = repeatingTransaction.getTransaction();
                newTransaction.setDate(nextInsert);
                transactionDao.updateOrInsertAsync(newTransaction);

                // Set the latest insert date of the repeating transaction
                repeatingTransaction.setLatestInsert(nextInsert);
                repeatingTransactionDao.updateOrInsertAsync(repeatingTransaction);
                return true;
            } else {
                return false;
            }
        }
    }
}
