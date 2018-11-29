package org.secuso.privacyfriendlyfinance.activities;

import org.secuso.privacyfriendlyfinance.R;

public class AccountActivity extends TransactionListActivity {
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";

//    public AccountActivity(LiveData<List<Transaction>> transactions) {
//        super(transactions);
//    }

//
//    @Override
//    protected void getTransactionListAsync() {
//
//    }

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
    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }
}
