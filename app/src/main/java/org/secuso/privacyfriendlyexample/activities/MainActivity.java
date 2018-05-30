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

package org.secuso.privacyfriendlyexample.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.activities.adapter.CustomListViewAdapter;
import org.secuso.privacyfriendlyexample.activities.adapter.HelpExpandableListAdapter;
import org.secuso.privacyfriendlyexample.activities.helper.BaseActivity;
import org.secuso.privacyfriendlyexample.database.PFASQLiteHelper;
import org.secuso.privacyfriendlyexample.database.PFASampleDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Christopher Beckmann, Karola Marky
 * @version 20171016
 */
public class MainActivity extends BaseActivity {
    private PFASQLiteHelper myDB;
    private CustomListViewAdapter adapter;
    private List<PFASampleDataType> database_list;
    private ArrayList<PFASampleDataType> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //myDB.deleteAllSampleData();


        overridePendingTransition(0, 0);

        //Plus Button opens Dialog to add new Transaction
        FloatingActionButton add_expense = findViewById(R.id.add_expense);
        add_expense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                openDialog();
            }
        });

        //fill ListView with data from database
        myDB = new PFASQLiteHelper(this);

        database_list = myDB.getAllSampleData();
        list = new ArrayList<>();

        for (PFASampleDataType s : database_list){
            list.add(s);;
        }

        //fill TextView with total Balance of transactions
        Double balance = myDB.getBalance();
        TextView balanceView = (TextView) findViewById(R.id.totalBalance);
        balanceView.setText(balance.toString()+"€");
        if (balance<0){
            balanceView.setTextColor(getResources().getColor(R.color.red));
        }else{
            balanceView.setTextColor(getResources().getColor(R.color.green));
        }

        //init adapter
        adapter = new CustomListViewAdapter(this,list);
        ListView transactionList = (ListView) findViewById(R.id.transactionList);
        transactionList.setAdapter(adapter);

        //Menu for listview items
        registerForContextMenu(transactionList);




    }

    //opens the dialog for entering new transaction
    public void openDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),"Dialog");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_click_menu,menu);
    }

    /**
     * This method connects the Activity to the menu item
     *
     * @return ID of the menu item it belongs to
     */
    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
    }



}
