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

/**
 *
 * @author David Meiborg
 * @version 20180710
 *
 * This class holds the "Category" that will be stored in the database
 * Each column of the database will be a private variable in this class.
 * For each data type one class is required.
 *
 */

public class CategoryDataType {

    private int ID;
    private String categoryName;



    /**
     * Always use this constructor to generate data with values.
     * @param ID The primary key for the database (will be automatically set by the DB)
     */
    public CategoryDataType(int ID, String categoryName) {

        this.ID=ID;
        this.categoryName=categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
