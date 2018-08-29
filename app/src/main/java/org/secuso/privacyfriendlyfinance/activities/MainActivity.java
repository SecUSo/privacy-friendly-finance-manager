/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlyfinance.activities;

import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.helper.BaseActivity;
import org.secuso.privacyfriendlyfinance.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyfinance.database.PFASampleDataType;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQuery;
import org.secuso.privacyfriendlyfinance.helpers.AsyncQueryDelete;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;


/**
 * @author David Meiborg
 * @version 2018
 */
public class MainActivity extends BaseActivity {
    private PFASQLiteHelper myDB;
    private List<PFASampleDataType> database_list;
    private ArrayList<PFASampleDataType> list;
    private Context context;
    private static String defaultCategory;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public static String getDefaultCategory(){
        return defaultCategory;
    }


    @Override
    protected void onResume() {
        super.onResume();
        ListView transactionList = (ListView) findViewById(R.id.transactionList);
        new AsyncQuery(transactionList,this).execute();
    }

    public Context getContext(){
        return MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(0, 0);

        context=this.getApplicationContext();
        defaultCategory = context.getResources().getString(R.string.firstCategoryName);


        //Plus Button opens Dialog to add new Transaction
        FloatingActionButton add_expense = findViewById(R.id.add_expense);
        add_expense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                openDialog();
            }
        });

        ListView transactionList = (ListView) findViewById(R.id.transactionList);
        TextView balanceView = (TextView) findViewById(R.id.totalBalance);
        new AsyncQuery(transactionList,this).execute();


        //fill TextView with total Balance of transactions
        myDB = new PFASQLiteHelper(this);
        Double balance = myDB.getBalance();
        NumberFormat format = NumberFormat.getCurrencyInstance();
        balanceView.setText(format.format(balance).toString());
        if (balance<0){
            balanceView.setTextColor(getResources().getColor(R.color.red));
        }else{
            balanceView.setTextColor(getResources().getColor(R.color.green));
        }

        //Edit Transaction if click on item
        transactionList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               new AsyncQueryUpdateOpenDialog(position,getApplicationContext()).execute();
            }
        });

        //Menu for listview items
        registerForContextMenu(transactionList);
    }

    //opens the dialog for entering new transaction
    public void openDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),"Dialog");

    }

    //opens menu for delete or edit list items
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_click_menu,menu);
    }

    //action when menu item is selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            //delete Item from DB and View
            case R.id.listDeleteItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete_dialog_title)
                        .setPositiveButton(R.string.delete_dialog_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new AsyncQueryDelete(info.position,MainActivity.this).execute();

                                Intent main = new Intent(getBaseContext(),MainActivity.class);
                                startActivity(main);
                            }
                        })
                        .setNegativeButton(R.string.delete_dialog_negative, null);

                        AlertDialog alert = builder.create();
                        alert.show();
                break;


            //edit Item in DB and View
            case R.id.listEditItem:

                new AsyncQueryUpdateOpenDialog(info.position,getApplicationContext()).execute();

                break;
        }

        return super.onContextItemSelected(item);
    }



    /**
     * This method connects the Activity to the menu item
     *
     * @return ID of the menu item it belongs to
     */
    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_main;
    }


    /**
     * This nested class opens the Edit Dialog with an AsyncTask
     *
     */
    private class AsyncQueryUpdateOpenDialog extends AsyncTask<Void,Void,Void> {

        private ArrayList<PFASampleDataType> list = new ArrayList<>();
        int position;
        PFASQLiteHelper myDB;
        Context context;


        public AsyncQueryUpdateOpenDialog(int position, Context context){
            this.position=position;
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDB = new PFASQLiteHelper(context);
            List<PFASampleDataType> database_list = myDB.getAllSampleData();
            list = new ArrayList<>();

            for (PFASampleDataType s : database_list){
                list.add(s);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EditDialog dialog = new EditDialog(list.get(position));
            dialog.show(getSupportFragmentManager(),"EditDialog");
        }
    }

}
