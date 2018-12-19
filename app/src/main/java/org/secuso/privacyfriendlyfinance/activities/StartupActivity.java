package org.secuso.privacyfriendlyfinance.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.FullTaskListener;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.helpers.SharedPreferencesManager;

public class StartupActivity extends AppCompatActivity implements FullTaskListener {
    ProgressBar progressBar;
    TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesManager.init(getApplicationContext());
        setContentView(R.layout.activity_startup);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        progressText = findViewById(R.id.progressText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(1000);
        FinanceDatabase.connect(getApplicationContext(), this);
    }

    @Override
    public void onDone(Object result, AsyncTask<?, ?, ?> task) {
        Intent mainIntent;
        if(SharedPreferencesManager.isFirstTimeLaunch()) {
            mainIntent = new Intent(this, TutorialActivity.class);
        } else {
            mainIntent = new Intent(this, TransactionsActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onProgress(final Double progress, AsyncTask<?, ?, ?> task) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(new Double(progress * 1000).intValue());
            }
        });
    }

    @Override
    public void onOperation(final String operation, AsyncTask<?, ?, ?> task) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressText.setText(operation);
            }
        });
    }
}
