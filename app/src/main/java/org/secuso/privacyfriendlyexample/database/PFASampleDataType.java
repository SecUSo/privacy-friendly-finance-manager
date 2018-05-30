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

package org.secuso.privacyfriendlyexample.database;

import java.util.Date;

/**
 *
 * @author Karola Marky
 * @version 20161223
 *
 * This class holds the "data type" that will be stored in the database
 * Each column of the database will be a private variable in this class.
 * For each data type one class is required.
 * In our example we only use one data type, which is sampleData
 *
 */

public class PFASampleDataType {

    private int ID;
    private String transactionName;
    private Double transaction_amount;
    private boolean transaction_type;
    private String transaction_account;
    private String transaction_date;

    public PFASampleDataType() {    }



    /**
     * Always use this constructor to generate data with values.
     * @param ID The primary key for the database (will be automatically set by the DB)
     */
    public PFASampleDataType(int ID, String transactionName, Double transaction_amount, boolean transaction_type,String transaction_account,String transaction_date) {

        this.ID=ID;
        this.transactionName=transactionName;
        this.transaction_amount=transaction_amount;
        this.transaction_type=transaction_type;
        this.transaction_account=transaction_account;
        this.transaction_date=transaction_date;
    }

    /**
     * All variables need getters and setters. because the variables are private.
     */


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(Double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public boolean isTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(boolean transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_account() {
        return transaction_account;
    }

    public void setTransaction_account(String transaction_account) {
        this.transaction_account = transaction_account;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

}
