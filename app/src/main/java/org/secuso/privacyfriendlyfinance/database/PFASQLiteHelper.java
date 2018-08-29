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

package org.secuso.privacyfriendlyfinance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 * @author David Meiborg
 * @version 20180710
 * Structure based on http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * accessed at 16th June 2016
 *
 * This class defines the structure of our database.
 */

public class PFASQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 17;

    /**
     * Use the following pattern for the name of the database
     * PF_[Name of the app]_DB
     */
    public static final String DATABASE_NAME = "PF_FinanceManager_DB";

    //Names of table in the database
    private static final String TABLE_SAMPLEDATA = "FinanceData";

    //Names of columns in the databases in this example we only use one table
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "transactionName";
    private static final String KEY_AMOUNT = "transactionAmount";
    private static final String KEY_TYPE = "transactionType";
    private static final String KEY_DATE = "transactionDate";
    private static final String KEY_CATEGORY = "transactionCategory";


    public PFASQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /**
         * Create the table sample data on the first start
         * Be careful with the final line of the query and the SQL syntax that is used in the String.
         */
        String CREATE_SAMPLEDATA_TABLE = "CREATE TABLE " + TABLE_SAMPLEDATA +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT NOT NULL," +
                KEY_AMOUNT +" REAL," +
                KEY_TYPE + " INTEGER," +
                KEY_DATE + " TEXT NOT NULL," +
                KEY_CATEGORY + " TEXT NOT NULL);";


        sqLiteDatabase.execSQL(CREATE_SAMPLEDATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SAMPLEDATA);

        onCreate(sqLiteDatabase);
    }


    /**
     * Adds a single sampleData to our Table
     * As no ID is provided and KEY_ID is autoincremented (see line 50)
     * the last available key of the table is taken and incremented by 1
     * @param sampleData data that will be added
     */
    public void addSampleData(PFASampleDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,sampleData.getTransactionName());
        values.put(KEY_AMOUNT,sampleData.getTransaction_amount());
        if(sampleData.isTransaction_type()==0){
            values.put(KEY_TYPE,0);
        }
        else values.put(KEY_TYPE,1);
        values.put(KEY_DATE,sampleData.getTransaction_date().toString());
        values.put(KEY_CATEGORY,sampleData.getTransaction_category());



        database.insert(TABLE_SAMPLEDATA, null, values);
        database.close();
    }


    /**
     * This method gets a single sampleData entry based on its ID
     * @param id of the sampleData that is requested, could be get by the get-method
     * @return the sampleData that is requested.
     */
    public PFASampleDataType getSampleData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d("DATABASE", Integer.toString(id));

        Cursor cursor = database.query(TABLE_SAMPLEDATA, new String[]{KEY_ID,
                        KEY_NAME,KEY_AMOUNT,KEY_TYPE,KEY_DATE,KEY_CATEGORY}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        PFASampleDataType sampleData = new PFASampleDataType();

        if( cursor != null && cursor.moveToFirst() ){
            sampleData.setID(Integer.parseInt(cursor.getString(0)));


            Log.d("DATABASE", "Read " + cursor.getString(1) + " from DB");

            cursor.close();
        }

        return sampleData;

    }

    /**
     * This method returns all data from the DB as a list
     * This could be used for instance to fill a recyclerView
     * @return A list of all available sampleData in the Database
     */
    public List<PFASampleDataType> getAllSampleData() {
        List<PFASampleDataType> sampleDataList = new ArrayList<PFASampleDataType>();

        String selectQuery = "SELECT  * FROM " + TABLE_SAMPLEDATA;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        PFASampleDataType sampleData = null;

        if (cursor.moveToFirst()) {
            do {
                //To adjust this class for your own data, please add your values here.
                //be careful to use the right get-method to get the data from the cursor
                sampleData = new PFASampleDataType();
                sampleData.setID(Integer.parseInt(cursor.getString(0)));
                sampleData.setTransactionName(cursor.getString(1));
                sampleData.setTransaction_amount(Double.parseDouble(cursor.getString(2)));
                sampleData.setTransaction_type(Integer.parseInt(cursor.getString(3)));
                sampleData.setTransaction_date(cursor.getString(4));
                sampleData.setTransaction_category(cursor.getString(5));


                sampleDataList.add(sampleData);
            } while (cursor.moveToNext());
        }

        //sorting of sampleDataList according to Date
        Collections.sort(sampleDataList, new Comparator<PFASampleDataType>() {

            @Override
            public int compare(PFASampleDataType o1, PFASampleDataType o2) {
                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
                int compareResult = 0;


                Date arg0Date = null;
                Date arg1Date = null;
                try {
                    arg0Date = format.parse(o1.getTransaction_date());
                    arg1Date = format.parse(o2.getTransaction_date());
                    compareResult = arg1Date.compareTo(arg0Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return compareResult;            }
        });

        return sampleDataList;
    }


    /**
     * This method returns the total balance of transactions in the DB
     * @return Double - total balance
     */
    public double getBalance(){
        PFASampleDataType sampleData = null;
        Double balance = new Double(0);

        String selectQuery = "SELECT  * FROM " + TABLE_SAMPLEDATA;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                sampleData = new PFASampleDataType();
                sampleData.setTransaction_type(cursor.getInt(3));
                if(sampleData.isTransaction_type()==0){
                    balance=balance + (cursor.getDouble(2))*(-1);
                }else{
                    balance=balance + (cursor.getDouble(2));
                }
            } while (cursor.moveToNext());
        }

        return balance;
    }

    /**
     * This method returns the total balance of all transactions with a specific category
     * @return Double - balance by category
     */
    public double getBalanceByCategory(String category){
        PFASampleDataType sampleData = null;
        Double balance = new Double(0);

        String selectQuery = "SELECT  * FROM " + TABLE_SAMPLEDATA;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                sampleData = new PFASampleDataType();
                sampleData.setTransaction_category(cursor.getString(5));
                sampleData.setTransaction_type(cursor.getInt(3));
                //sampleData.setTransaction_amount(cursor.getDouble(2));


                if(sampleData.isTransaction_type()==0){
                    if(sampleData.getTransaction_category().equals(category)) balance=balance + (cursor.getDouble(2))*(-1);
                }else{
                    if(sampleData.getTransaction_category().equals(category)) balance=balance + (cursor.getDouble(2));
                }

            } while (cursor.moveToNext());
        }

        return balance;
    }

    /**
     * Updates a database entry.
     * @param sampleData
     * @return actually makes the update
     */
    public void updateSampleData(PFASampleDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,sampleData.getTransactionName());
        values.put(KEY_AMOUNT,sampleData.getTransaction_amount());
        values.put(KEY_TYPE,sampleData.isTransaction_type());
        values.put(KEY_DATE,sampleData.getTransaction_date().toString());
        values.put(KEY_CATEGORY,sampleData.getTransaction_category());

        database.update(TABLE_SAMPLEDATA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sampleData.getID()) });

        database.close();

    }

    /**
     * Deletes sampleData from the DB
     * This method takes the sampleData and extracts its key to build the delete-query
     * @param sampleData that will be deleted
     */
    public void deleteSampleData(PFASampleDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_SAMPLEDATA, KEY_ID + " = ?",
                new String[] { Integer.toString(sampleData.getID()) });
        //always close the DB after deletion of single entries
        database.close();
    }

    /**
     * deletes all sampleData from the table.
     * This could be used in case of a reset of the app.
     */
    public void deleteAllSampleData() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from "+ TABLE_SAMPLEDATA);
    }


}
