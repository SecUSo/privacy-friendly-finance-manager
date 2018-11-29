package org.secuso.privacyfriendlyfinance.activities;

import android.os.Bundle;

import org.secuso.privacyfriendlyfinance.R;

public class CategoryActivity extends TransactionListActivity {
//    @Override
//    protected void getTransactionListAsync() {
//        /*
//         * TODO: here, the correct list of transactions should be retrieved... something like:
//         *
//         * transactionDao.getAllForCategoryAsync(categoryId); (method does not exist in transactionDao yet)
//         *
//         */
//        transactionDao.getAllAsync(this);
//    }

    @Override
    protected String getTransactionListTitle() {
        Bundle args = getIntent().getExtras();
        String categoryName = args.getString("categoryName", null);
        if (categoryName != null) {
            return categoryName;
        } else {
            //TODO: do this with a string resource.
            return "unknown category";
        }
    }

    @Override
    protected String getTotalBalanceText() {
        return String.valueOf(42_000L);
    }

    @Override
    protected long getPreselectedAccountId() {
        return -1;
    }

    @Override
    protected long getPreselectedCategoryId() {
        return -1;
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }
}
