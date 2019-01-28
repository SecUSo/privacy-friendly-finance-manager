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

package org.secuso.privacyfriendlyfinance.domain.legacy;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class MigrationFromUnencrypted {
    public static final String TRANSACTION_DB_NAME = "PF_FinanceManager_DB";
    public static final String CATEGORY_DB_NAME = "PF_FinanceManager_DB_Categories";

    public static boolean legacyDatabaseExists(Context context) {
        File databaseFile = new File(context.getApplicationInfo().dataDir + "/databases/" + TRANSACTION_DB_NAME);
        return databaseFile.exists() && databaseFile.isFile();
    }

    public static void deleteLegacyDatabase(Context context) {
        File databaseDir = new File(context.getApplicationInfo().dataDir + "/databases");
        File[] files = databaseDir.listFiles();
        if(files != null) {
            for(File f: files) {
                if(!f.isDirectory()) {
                    if (f.getName().startsWith(TRANSACTION_DB_NAME) || f.getName().startsWith(CATEGORY_DB_NAME)) {
                        f.delete();
                    }
                }
            }
        }
    }

    public static void migrateTo(FinanceDatabase target, Context context) {
        String dbDir = context.getApplicationInfo().dataDir + "/databases/";
        DatabaseExporter categoryExporter = new DatabaseExporter(dbDir + CATEGORY_DB_NAME, "category");
        DatabaseExporter transactionExporter = new DatabaseExporter(dbDir + TRANSACTION_DB_NAME, "transaction");
        try {
            List<String> categoryNames = new ArrayList<>();
            JSONArray categories = categoryExporter.dbToJSON().getJSONObject("category").getJSONArray("CategoryData");
            for (int i = 0; i < categories.length(); ++i) {
                String categoryName = categories.getJSONObject(i).getString("categoryName");
                if (!categoryName.equals("Standard")) categoryNames.add(categoryName);
            }
            JSONArray transactions = transactionExporter.dbToJSON().getJSONObject("transaction").getJSONArray("FinanceData");
            for (int i = 0; i < transactions.length(); ++i) {
                String categoryName = transactions.getJSONObject(i).getString("transactionCategory");
                if (!categoryName.equals("Standard")) categoryNames.add(categoryName);
            }

            for (String categoryName : categoryNames) {
                Category category = target.categoryDao().getByName(categoryName).getValue();
                if (category == null) {
                    target.categoryDao().insert(new Category(categoryName));
                }
            }

            for (int i = 0; i < transactions.length(); ++i) {
                JSONObject sourceTransaction = transactions.getJSONObject(i);
                Transaction transaction = new Transaction();
                transaction.setAccountId(0L);
                transaction.setName(sourceTransaction.getString("transactionName"));
                long amount = (long) (sourceTransaction.getDouble("transactionAmount") * 100);
                if (sourceTransaction.getInt("transactionType") == 0) {
                    amount = -amount;
                }
                transaction.setAmount(amount);
                transaction.setDate(LocalDate.parse(
                        sourceTransaction.getString("transactionDate"),
                        DateTimeFormat.forPattern("dd/MM/yyyy")
                ));
                Category category = target.categoryDao().getByName(sourceTransaction.getString("transactionCategory")).getValue();
                if (category != null) {
                    transaction.setCategoryId(category.getId());
                }
                target.transactionDao().insert(transaction);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
