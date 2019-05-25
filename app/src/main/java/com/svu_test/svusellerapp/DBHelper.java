package com.svu_test.svusellerapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBName = "svu_seller";
    private static final int DBVersion = 1;

    private static final String CREATE_SELLER_TABLE = "Create Table if not exists Sellers (" +
            "sellerID INTEGER primary key autoincrement," +
            "sellerName varchar(200) not null," +
            "sellerNumber varchar(4)," +
            "sellerRegion varchar(200) not null," +
            "sellerImage BLOB);";
    private static final String DROP_SELLER_TABLE = "DROP Table if exists Sellers;";

    private static final String CREATE_SALES_TABLE = "Create Table if not exists Sales (" +
            "salesID INTEGER primary key autoincrement," +
            "month varchar(15)," +
            "year varchar(4)," +
            "amount INTEGER(12)," +
            "sellerRegion varchar(200) not null," +
            "sellerID INTEGER);";
    private static final String DROP_SALES_TABLE = "DROP Table if exists Sales;";

    private static final String CREATE_COM_TABLE = "Create Table if not exists Commission (" +
            "comID INTEGER primary key autoincrement," +
            "month varchar(15)," +
            "year INTEGER(4)," +
            "amount INTEGER(12)," +
            "sellerID INTEGER);";
    private static final String DROP_COM_TABLE = "DROP Table if exists Commission;";

    DBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    // region seller

    public void insertSeller(SellerModel sellerModel) {
        byte[] data = getBitmapAsByteArray(sellerModel.getImage());

        ContentValues contentValues = new ContentValues();
        contentValues.put("sellerName", sellerModel.getName());
        contentValues.put("sellerNumber", sellerModel.getNumber());
        contentValues.put("sellerRegion", sellerModel.getRegion());
        contentValues.put("sellerImage", data);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert("Sellers", null, contentValues);
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void deleteSeller(String sellerID) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("Sellers", "sellerID=?", new String[]{sellerID});
    }

    public void updateSeller(SellerModel sellerModel) {
        byte[] data = getBitmapAsByteArray(sellerModel.getImage());

        ContentValues contentValues = new ContentValues();
        contentValues.put("sellerName", sellerModel.getName());
        contentValues.put("sellerNumber", sellerModel.getNumber());
        contentValues.put("sellerRegion", sellerModel.getRegion());
        contentValues.put("sellerImage", data);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update("Sellers", contentValues, "sellerID=" + sellerModel.getId(), null);
    }

    public ArrayList<SellerModel> getAllSellers() {
        ArrayList<SellerModel> sellerModels = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Sellers",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("sellerID"));
                String number = cursor.getString(cursor.getColumnIndex("sellerNumber"));
                String name = cursor.getString(cursor.getColumnIndex("sellerName"));
                String region = cursor.getString(cursor.getColumnIndex("sellerRegion"));
                byte[] imgByte = cursor.getBlob(cursor.getColumnIndex("sellerImage"));
                Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

                sellerModels.add(new SellerModel(id, name, region, number, image));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return sellerModels;
    }

    // endregion

    // region sales

    public void insertSale(SaleModel saleModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("month", saleModel.getMonth());
        contentValues.put("year", saleModel.getYear());
        contentValues.put("amount", saleModel.getAmount());
        contentValues.put("sellerRegion", saleModel.getRegion());
        contentValues.put("sellerID", saleModel.getSellerID());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert("Sales", null, contentValues);
    }

    // endregion

    // region com

    public void insertCommission(CommissionModel commissionModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("month", commissionModel.getMonth());
        contentValues.put("year", commissionModel.getYear());
        contentValues.put("amount", commissionModel.getAmount());
        contentValues.put("sellerID", commissionModel.getSellerID());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert("Commission", null, contentValues);
    }

    // endregion

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SELLER_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_COM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SELLER_TABLE);
        db.execSQL(DROP_SALES_TABLE);
        db.execSQL(DROP_COM_TABLE);
        onCreate(db);
    }
}
