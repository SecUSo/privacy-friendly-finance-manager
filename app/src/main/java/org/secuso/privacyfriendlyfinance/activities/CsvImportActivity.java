package org.secuso.privacyfriendlyfinance.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.secuso.privacyfriendlyfinance.csv.CsvImporter;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Name2Id;
import org.secuso.privacyfriendlyfinance.domain.model.common.Name2IdCreateIfNotExists;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvImportActivity extends AppCompatActivity {

    private static final String TAG = CsvImportActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentIn = getIntent();
        if (intentIn != null) {
            Uri uri = intentIn.getParcelableExtra(Intent.EXTRA_STREAM); // used by send
            if (uri == null) {
                uri = intentIn.getData(); // used by sendTo and view
            }

            if (uri != null) {
                final Uri finalUri = uri;
                new Thread(() -> ImportCsvAsync(finalUri)).start();
            }
        }
    }

    private void ImportCsvAsync(Uri uri) {
        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(uri);

            importCsv(in);
        } catch (Exception ex) {
            Log.w(TAG,"Error in onCreate-startActivityForResult " + uri, ex);
            // toast(this, getString(R.string.error_cannot_convert_or_resend, uri, ex.getMessage()));
            setResult(RESULT_CANCELED);
            finish();
        } finally {
            close(in, uri);
        }
    }

    private void importCsv(InputStream in) throws Exception {
        List<Account> accounts = loadAccountsFromDB();
        Name2Id<?> accountName2Id = new Name2IdCreateIfNotExists<Account>(accounts){

            @Override
            protected Account createItem() {
                return new Account();
            }

            @Override
            protected void save(Account newItem) {
                saveAccount(newItem);
            }
        };

        List<Category> categories = loadCategoriesFromDB();
        Name2Id<?> categoryName2Id = new Name2IdCreateIfNotExists<Category>(categories) {

            @Override
            protected Category createItem() {
                return new Category();
            }

            @Override
            protected void save(Category newItem) {
                saveCategories(newItem);

            }
        };

        CsvImporter importer = new CsvImporter(new InputStreamReader(in), accountName2Id, categoryName2Id);
        List<Transaction> transactions = importer.readFromCsv();

        saveTransactionsToDB(transactions);
        // todo save transactions to database
    }

    private void saveTransactionsToDB(List<Transaction> transactions) {
    }

    private List<Category> loadCategoriesFromDB() {
        return null;
    }

    private void saveCategories(Category newItem) {
    }

    private List<Account> loadAccountsFromDB() {
        return null; // todo
    }

    private void saveAccount(Account newItem) {
        // TODO
    }

    public static void close(Closeable stream, Object source) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.w(TAG, "Error close " + source, e);
            }
        }
    }
}