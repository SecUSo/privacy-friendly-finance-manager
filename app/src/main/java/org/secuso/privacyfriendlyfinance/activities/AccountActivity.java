package org.secuso.privacyfriendlyfinance.activities;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

public class AccountActivity extends TransactionListActivity {
    @Override
    protected void getTransactionListAsync() {
        CommunicantAsyncTask<Void, Long> task = new CommunicantAsyncTask<Void, List<Transaction>>() {
            @Override
            protected List<Transaction> doInBackground(Void... voids) {
                 return transactionDao.getForAccount();
            }
        };
        transactionDao.getAllAsync(this);
    }

    @Override
    protected String getTransactionListTitle() {
        return null;
    }

    @Override
    protected String getTotalBalanceText() {
        return null;
    }

    @Override
    protected long getPreselectedAccountId() {
        return 0;
    }

    @Override
    protected long getPreselectedCategoryId() {
        return 0;
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }
}
