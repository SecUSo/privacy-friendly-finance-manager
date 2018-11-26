package org.secuso.privacyfriendlyfinance.domain.legacy;

import android.content.Context;

import org.json.JSONObject;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;

public class MigrationFromUnencrypted {
    public static final String TRANSACTION_DB_NAME = "PF_FinanceManager_DB";
    public static final String CATEGORY_DB_NAME = "PF_FinanceManager_DB_Categories";

    public static void migrateTo(FinanceDatabase target, Context context) {
        String dbDir = context.getApplicationInfo().dataDir + "/databases/";
        DatabaseExporter categoryExporter = new DatabaseExporter(dbDir + CATEGORY_DB_NAME, "category");
        DatabaseExporter transactionExporter = new DatabaseExporter(dbDir + TRANSACTION_DB_NAME, "transaction");
        JSONObject categoryJson = categoryExporter.dbToJSON();

    }
}
