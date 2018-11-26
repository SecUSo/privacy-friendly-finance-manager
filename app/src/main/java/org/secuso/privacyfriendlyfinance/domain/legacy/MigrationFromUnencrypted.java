package org.secuso.privacyfriendlyfinance.domain.legacy;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;

import java.util.ArrayList;
import java.util.List;

public class MigrationFromUnencrypted {
    public static final String TRANSACTION_DB_NAME = "PF_FinanceManager_DB";
    public static final String CATEGORY_DB_NAME = "PF_FinanceManager_DB_Categories";

    public static void migrateTo(FinanceDatabase target, Context context) {
        String dbDir = context.getApplicationInfo().dataDir + "/databases/";
        DatabaseExporter categoryExporter = new DatabaseExporter(dbDir + CATEGORY_DB_NAME, "category");
        DatabaseExporter transactionExporter = new DatabaseExporter(dbDir + TRANSACTION_DB_NAME, "transaction");
        System.out.println("categoryJson1");
        try {
            List<String> categoryNames = new ArrayList<>();
            JSONArray categories = categoryExporter.dbToJSON().getJSONObject("category").getJSONArray("CategoryData");
            for (int i = 0; i < categories.length(); ++i) {
                String categoryName = categories.getJSONObject(i).getString("categoryName");
                if (categoryName != "Standard") categoryNames.add(categoryName);
            }
            JSONArray transactions = transactionExporter.dbToJSON().getJSONObject("transaction").getJSONArray("FinanceData");
            for (int i = 0; i < transactions.length(); ++i) {
                String categoryName = transactions.getJSONObject(i).getString("transactionCategory");
                if (categoryName != "Standard") categoryNames.add(categoryName);
            }

//            for (String categoryName : categoryNames) {
//                Category category = target.categoryDao().getByName(categoryName);
//                if (category == null) {
//                    target.categoryDao().insert(new Category(categoryName));
//                }
//            }

            for (int i = 0; i < transactions.length(); ++i) {
                JSONObject sourceTransaction = transactions.getJSONObject(i);
            }

//
//            JSONObject transactionJson = transactionExporter.dbToJSON();
//            System.out.println(categoryJson.toString(1));
            System.out.println(transactions.toString(1));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
