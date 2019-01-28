package org.secuso.privacyfriendlyfinance.domain;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.domain.access.RepeatingTransactionDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

public class PeriodicDatabaseWorker {
    public static final long DURATION_BETWEEN_WORK = 10_000L;

    private static RepeatingTransactionDao repeatingTransactionDao = FinanceDatabase.getInstance().repeatingTransactionDao();
    private static TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();

    private PeriodicDatabaseWorker() {}

    public static void work() {
        PeriodicDatabaseTask periodicDatabaseTask = new PeriodicDatabaseTask();
        periodicDatabaseTask.execute();
    }

    private static boolean handleRepeatingTransaction(RepeatingTransaction repeatingTransaction) {
        // Calculate the local date for the next insert
        LocalDate nextInsert = null;
        if (repeatingTransaction.isWeekly()) {
            Period weeks = Period.weeks((int) repeatingTransaction.getInterval());
            LocalDate latestInsert = repeatingTransaction.getLatestInsert().withDayOfWeek(DateTimeConstants.MONDAY);
            nextInsert = latestInsert.plus(weeks).withDayOfWeek(DateTimeConstants.MONDAY);
        } else {
            Period months = Period.months((int) repeatingTransaction.getInterval());
            LocalDate latestInsert = repeatingTransaction.getLatestInsert().withDayOfMonth(1);
            nextInsert = latestInsert.plus(months).withDayOfMonth(1);
        }

        // Does the repeating transaction have an end?
        if (repeatingTransaction.getEnd() != null) {
            // Is the end before the calculated 'nextInsert'?
            if (repeatingTransaction.getEnd().isBefore(nextInsert)) {
                System.out.println("This repeating transaction already ended!");
                return false;
            }
        }

        // Is 'nextInsert' before 'now'?
        LocalDate now = LocalDate.now();
        if (nextInsert.isBefore(now) || nextInsert.isEqual(now)) {
            // Insert a new transaction
            Transaction newTransaction = repeatingTransaction.getTransaction();
            newTransaction.setDate(nextInsert);
            transactionDao.updateOrInsertAsync(newTransaction);

            // Set the latest insert date of the repeating transaction
            repeatingTransaction.setLatestInsert(nextInsert);
            repeatingTransactionDao.updateOrInsertAsync(repeatingTransaction);

            System.out.println("Inserted a new transaction");
            return true;
        } else {
            System.out.println("Don't have to insert a new transaction");
            return false;
        }
    }

    private static class PeriodicDatabaseTask extends CommunicantAsyncTask<Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            List<RepeatingTransaction> repeatingTransactions = repeatingTransactionDao.getAllSync();
            for (RepeatingTransaction r : repeatingTransactions) {
                // Add all transactions for this repeating transaction
                // (there can be more than one to add for one repeating transaction)
                while (handleRepeatingTransaction(r)) {
                }
            }
            return null;
        }
    }
}
