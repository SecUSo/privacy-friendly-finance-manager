package org.secuso.privacyfriendlyfinance.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;

public class CategoryActivity extends BaseActivity implements TaskListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_category;
    }

    @Override
    public void onDone(Object result, AsyncTask<?, ?, ?> task) {

    }

    @Override
    public void onProgress(Double progress, AsyncTask<?, ?, ?> task) {

    }

    @Override
    public void onOperation(String operation, AsyncTask<?, ?, ?> task) {

    }
}
