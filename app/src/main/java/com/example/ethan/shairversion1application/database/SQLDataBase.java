/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: SQLDataBase
 *
 *  class properties:
 *  DATABASE_VERSION: int
 *  DATABASE_NAME: String
 *  ACCOUNT_TABLE: String
 *  KEY_ID: String
 *  KEY_NAME: String
 *  KEY_PWD: String
 *  KEY_AUTO: String
 *  SEARCH_TABLE: String
 *  KEY_HISTORY: String
 *
 *
 *  class methods:
 *  onCreate(SQLiteDatabase db): void
 *  onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion): void
 *  deleteAllAccount(): void
 *  getCount(): int
 *  addItem(String accountName, String accountPassword, int number): void
 *  addHistory(String info): void
 *  getAcount(): ArrayList<Account>
 *  getAutoLoginFlag(): ArrayList<Integer>
 *  getHistory(): ArrayList<String>
 *  checkHistory(String info): boolean
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ethan.shairversion1application.entities.Account;

import java.util.ArrayList;


public class SQLDataBase extends SQLiteOpenHelper {
    /*
     * declare all the variables
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "local_database";
    private static final String ACCOUNT_TABLE = "accountInfo";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PWD = "password";
    private static final String KEY_AUTO = "autologin";

    private static final String SEARCH_TABLE = "searchInfo";
    private static final String KEY_HISTORY = "history";

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */



    public SQLDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    /*
     * declare all the other methods
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*
    create the table
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + ACCOUNT_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PWD + " TEXT,"
                + KEY_AUTO + " NUMBER" +
                ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        final String CREATE_SEARCH_TABLE = "create table if not exists " + SEARCH_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HISTORY + " TEXT" +
                ")";
        db.execSQL(CREATE_SEARCH_TABLE);

    }

    /*
        upgrade the table
         */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        onCreate(db);
    }

    public void deleteAllAccount() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        onCreate(db);
    }

    @SuppressWarnings("unused")
    public void deleteAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + SEARCH_TABLE);
        onCreate(db);
    }


    @SuppressWarnings("unused")
    public int getCount() {
        int count;
        String countQuery = "SELECT  * FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    /*
    add the data to the table
     */
    public void addItem(String accountName, String accountPassword, int number) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, accountName);
        values.put(KEY_PWD, accountPassword);
        values.put(KEY_AUTO, number);
        // Inserting Row
        db.insert(ACCOUNT_TABLE, null, values);
        db.close();
        System.out.println("database: account:   " + accountName);
        System.out.println("database: password:   " + accountPassword);
        System.out.println("database: autologin:   " + number);
    }


    public void addHistory(String info) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY, info);
        // Inserting Row
        db.insert(SEARCH_TABLE, null, values);
        db.close();
        System.out.println("add history:   " + info);
    }

    /*
    get all the data from the table
     */
    public ArrayList<Account> getAcount() {
        ArrayList<Account> accountArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ACCOUNT_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //build a student
                Account account = new Account();
                account.setAccountName(cursor.getString(1));
                account.setAccountPassword(cursor.getString(2));
                // Adding student to list
                accountArrayList.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return studentArrayList
        return accountArrayList;
    }

    public ArrayList<Integer> getAutoLoginFlag() {
        ArrayList<Integer> flagArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ACCOUNT_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    // Adding student to list
                    flagArrayList.add(cursor.getInt(3));
                } while (cursor.moveToNext());
            }
        } else {
            flagArrayList.add(0);
        }

        // looping through all rows and adding to list

        cursor.close();
        // return studentArrayList
        return flagArrayList;
    }


    public ArrayList<String> getHistory() {
        ArrayList<String> searchArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SEARCH_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //build a student

                searchArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return studentArrayList
        return searchArrayList;
    }

    public boolean checkHistory(String info) {
        String selectQuery = "SELECT  * FROM " + SEARCH_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //build a student
                if (cursor.getString(1).equals(info)) {
                    return true;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }


/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}

