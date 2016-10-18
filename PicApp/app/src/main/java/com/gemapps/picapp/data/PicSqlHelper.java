package com.gemapps.picapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by edu on 10/5/16.
 */

public class PicSqlHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "gempicapp.db";

    public PicSqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(PicappContract.UserEntry.SQL_CREATE_TABLE);
        db.execSQL(PicappContract.PublicationEntry.SQL_CREATE_TABLE);
        db.execSQL(PicappContract.BookmarkEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //this should be backwards compatible, but cos the previous version was just for test and fast
        //I will manage this way now
        db.execSQL(PicappContract.UserEntry.SQL_DROP_TABLE);
        db.execSQL(PicappContract.PublicationEntry.SQL_DROP_TABLE);
        db.execSQL(PicappContract.BookmarkEntry.SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onOpen(db);
    }
}
